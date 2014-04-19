package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.*;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketCursorUpdate;
import com.nickardson.jscomputing.common.network.PacketScreenUpdate;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import com.nickardson.jscomputing.javascript.api.APIComputer;
import com.nickardson.jscomputing.javascript.api.APIEvent;
import com.nickardson.jscomputing.javascript.api.APIFile;
import com.nickardson.jscomputing.javascript.api.APIScreen;
import com.nickardson.jscomputing.javascript.api.fs.*;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrint;
import com.nickardson.jscomputing.javascript.methods.APIFunctionWait;
import com.nickardson.jscomputing.javascript.methods.APIFunctionYield;
import net.minecraft.entity.player.EntityPlayerMP;
import org.lwjgl.input.Keyboard;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.FileNotFoundException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A ServerJavaScriptComputer with a physical TileEntity, and a basic DOS interface.
 */
public class ServerTerminalComputer extends AbstractTerminalComputer implements IServerComputer, IScriptableComputer, IEventableComputer, IKeyboardableComputer {
    private boolean shutdown = false;

    @Override
    public void updateLines(byte[][] lines) {
        linesUpdated = true;
        setLines(lines);
        sendLines(false);
    }

    /**
     * Number of ticks that must pass before sending a terminal update.
     */
    public static int LINE_SEND_TICK_INTERVAL = 2;

    /**
     * How many ticks ago the last terminal update was.
     */
    private int lastLineSend = 0;

    /**
     * Whether the terminal has changed since the last update sent.
     */
    private boolean linesUpdated;

    @Override
    public void sendLines(boolean force) {
        if (force || (linesUpdated && lastLineSend >= LINE_SEND_TICK_INTERVAL)) {
            linesUpdated = false;
            lastLineSend = 0;

            PacketScreenUpdate packet = new PacketScreenUpdate(getID(), lines, isCursorVisible(), getCursorX(), getCursorY());

            for (EntityPlayerMP player : ComputerManager.getPlayersWithContainer(ContainerTerminalComputer.class)) {
                IComputer computer = ((ContainerTerminalComputer) player.openContainer).getTileEntity().getServerComputer();
                if (computer != null && computer.getID() == this.getID()) {
                    ChannelHandler.sendTo(packet, player);
                }
            }
        } else {
            if (isCursorUpdated()) {
                setCursorUpdated(false);
                PacketCursorUpdate packet = new PacketCursorUpdate(getID(), isCursorVisible(), getCursorX(), getCursorY());

                for (EntityPlayerMP player : ComputerManager.getPlayersWithContainer(ContainerTerminalComputer.class)) {
                    IComputer computer = ((ContainerTerminalComputer) player.openContainer).getTileEntity().getServerComputer();
                    if (computer != null && computer.getID() == this.getID()) {
                        ChannelHandler.sendTo(packet, player);
                    }
                }
            }
        }
    }

    private TileEntityTerminalComputer tileEntity;

    public ServerTerminalComputer(int id, TileEntityTerminalComputer entityComputer) {
        super(id);

        this.tileEntity = entityComputer;
        this.queue = new ArrayBlockingQueue<IComputingEvent>(1024);
        this.scope = JavaScriptEngine.createScope();
    }

    public TileEntityTerminalComputer getTileEntity() {
        return tileEntity;
    }

    @Override
    public void tick() {
        sendLines(false);
        lastLineSend++;
    }

    @Override
    public void input(String text) {
        eval(text);
    }

    @Override
    public void onPlayerOpenGui() {
        lastLineSend = LINE_SEND_TICK_INTERVAL;
        sendLines(false);
    }

    @Override
    public void stop() {
        triggerEvent(new ComputingEventShutDown(getTileEntity()));

        if (getTileEntity().getBlockMetadata() >= 6) {
            getTileEntity().setBlockMetadata(getTileEntity().getBlockMetadata() - 6);
        }

        ComputerManager.removeComputer(getID());
    }

    @Override
    public void eval(String code) {
        triggerEvent(new ComputingEventJavaScriptEval(code));
    }

    @Override
    public Object get(String key) {
        return scope.get(key);
    }

    @Override
    public void put(String key, Object value) {
        scope.put(key, scope, value);
    }

    @Override
    public void start() {
        scope.defineProperty("wait", new APIFunctionWait(), ScriptableObject.READONLY);
        scope.defineProperty("stdout", new APIFunctionPrint(), ScriptableObject.READONLY);
        scope.defineProperty("pull", new APIFunctionYield(this), ScriptableObject.READONLY);
        scope.defineProperty("computer", APIComputer.create(tileEntity), ScriptableObject.READONLY);
        scope.defineProperty("screen", APIScreen.create(this), ScriptableObject.READONLY);

        ComputerFileStorage.ComputerFileStorageJSAPI computerFS = ComputerFileStorage.create(this);

        final WritableMultiFileStorage.WritableMultiFileStorageJSAPI fs = WritableMultiFileStorage.create(this, new IFileStorage[] {
                JarFileStorage.create(this, "assets/jscomputing/js/bin", "bin"),
                computerFS
        }, computerFS);

        scope.defineProperty("fs", fs, ScriptableObject.READONLY);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JavaScriptEngine.contextEnter();
                JavaScriptEngine.runLibrary(scope, "system.js");
                try {
                    if (fs.exists("os.js")) {
                        try {
                            FileReadableJSAPI f = fs.read("os.js");
                            eval((String) Context.jsToJava(f.readAll(), String.class));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JavaScriptEngine.runLibrary(scope, "os.js");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                while (!shutdown) {
                    try {
                        IComputingEvent event = queue.take();
                        if (event != null) {
                            handleEvent(event);
                        } else {
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                JavaScriptEngine.contextExit();

                for (ComputerFile file : APIFile.openFiles) {
                    file.close();
                }
                APIFile.openFiles.clear();
            }
        });
        thread.start();

        ComputerManager.addServerComputer(this);
    }

    public boolean triggerEvent(IComputingEvent event) {
        return queue.offer(event);
    }

    public void handleEvent(IComputingEvent event) {
        if (!(event instanceof CancellableComputingEvent) || !((CancellableComputingEvent) event).isCancelled()) {
            event.handle(ServerTerminalComputer.this);
        }

        if (event instanceof ComputingEventShutDown) {
            shutdown = true;
        }
    }

    private ScriptableObject scope;

    public ScriptableObject getScope() {
        return scope;
    }

    private Thread thread;

    public Thread getThread() {
        return thread;
    }

    private BlockingQueue<IComputingEvent> queue;

    public BlockingQueue<IComputingEvent> getQueue() {
        return queue;
    }

    @Override
    public void onKey(int key, char character, boolean down) {
        ComputingEventEvent event = new ComputingEventEvent("key");

        event.put("id", key);
        event.put("character", character);
        event.put("down", down);
        event.put("key", Keyboard.getKeyName(key));

        triggerEvent(event);
    }

    @Override
    public void onEvent(ComputingEventEvent event) {
        Object events = get("events");
        if (events instanceof ScriptableObject) {
            Object trigger = ((ScriptableObject) events).get("trigger");
            if (trigger instanceof Function) {
                try {
                    ((Function) trigger).call(
                            JavaScriptEngine.getContext(),
                            getScope(),
                            (Scriptable) events,
                            new Object[]{
                                    event.getName(),
                                    Context.javaToJS(APIEvent.create(event), getScope())
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
