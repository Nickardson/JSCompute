package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.*;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketScreenUpdate;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import com.nickardson.jscomputing.javascript.api.APIComputer;
import com.nickardson.jscomputing.javascript.methods.APIFunctionIncludeClasspath;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrint;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrintToScreen;
import com.nickardson.jscomputing.javascript.methods.APIFunctionWait;
import net.minecraft.entity.player.EntityPlayerMP;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A ServerJavaScriptComputer with a physical TileEntity, and a basic DOS interface.
 */
public class ServerTerminalComputer extends AbstractTerminalComputer implements IServerComputer, IScriptableComputer, IEventableComputer, IKeyboardableComputer {
    @Override
    public void updateLines(byte[][] lines) {
        linesUpdated = true;
        setLines(lines);
        sendLines();
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
    public void sendLines() {
        if (linesUpdated && lastLineSend >= LINE_SEND_TICK_INTERVAL) {
            linesUpdated = false;
            lastLineSend = 0;

            PacketScreenUpdate packet = new PacketScreenUpdate(getID(), lines);

            for (EntityPlayerMP player : ComputerManager.getPlayersWithContainer(ContainerTerminalComputer.class)) {
                IComputer computer = ((ContainerTerminalComputer) player.openContainer).getTileEntity().getServerComputer();
                if (computer != null && computer.getID() == this.getID()) {
                    ChannelHandler.sendTo(packet, player);
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
        sendLines();
        lastLineSend++;
    }

    @Override
    public void input(String text) {
        eval(text);
    }

    @Override
    public void onPlayerOpenGui() {
        lastLineSend = LINE_SEND_TICK_INTERVAL;
        sendLines();
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
        scope.defineProperty("print", new APIFunctionPrintToScreen(this), ScriptableObject.READONLY);
        scope.defineProperty("stdout", new APIFunctionPrint(), ScriptableObject.READONLY);
        scope.defineProperty("computer", new APIComputer(tileEntity), ScriptableObject.READONLY);
        scope.defineProperty("includeLibrary", new APIFunctionIncludeClasspath("/com/nickardson/jscomputing/js/"), ScriptableObject.READONLY);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JavaScriptEngine.contextEnter();
                JavaScriptEngine.runLibrary(scope, "main.js");
                while (true) {
                    try {
                        IComputingEvent event = queue.take();
                        if (event != null) {
                            event.handle(ServerTerminalComputer.this);

                            if (event instanceof ComputingEventShutDown) {
                                break;
                            }
                        } else {
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                JavaScriptEngine.contextExit();
            }
        });
        thread.start();

        ComputerManager.addServerComputer(this);
    }

    public boolean triggerEvent(IComputingEvent event) {
        return queue.offer(event);
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

    /**
     * The queue that manages incoming ComputingEvents
     */
    public BlockingQueue<IComputingEvent> getQueue() {
        return queue;
    }

    @Override
    public void onKey(int key, char character, boolean state) {
        triggerEvent(new ComputingEventEvent("key", key, character, state));
    }

    @Override
    public void onEvent(String name, Object[] args) {
        Object events = get("events");
        if (events instanceof ScriptableObject) {
            Object trigger = ((ScriptableObject) events).get("trigger");
            if (trigger instanceof Function) {
                try {
                    Object[] functionArgs = new Object[args.length + 1];
                    functionArgs[0] = name;
                    for (int i = 0; i < args.length; i++) {
                        functionArgs[i + 1] = args[i];
                    }
                    ((Function) trigger).call(JavaScriptEngine.getContext(), getScope(), (Scriptable) events, functionArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
