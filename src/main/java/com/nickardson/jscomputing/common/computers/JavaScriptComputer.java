package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.ComputingEventJavaScriptEval;
import com.nickardson.jscomputing.common.computers.events.ComputingEventShutDown;
import com.nickardson.jscomputing.common.computers.events.IComputingEvent;
import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;
import com.nickardson.jscomputing.javascript.api.APIComputer;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrint;
import com.nickardson.jscomputing.javascript.methods.APIFunctionWait;
import org.mozilla.javascript.ScriptableObject;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class JavaScriptComputer implements IComputer {
    private ScriptableObject scope;

    static int GLOBAL_TEMP_ID = 0;

    int id = 0;
    int tempID = ++GLOBAL_TEMP_ID;
    private TileEntityComputer tileEntity;

    private Thread thread;

    public ScriptableObject getScope() {
        return scope;
    }

    public TileEntityComputer getTileEntity() {
        return tileEntity;
    }

    public Thread getThread() {
        return thread;
    }

    public BlockingQueue<IComputingEvent> getQueue() {
        return queue;
    }

    /**
     * The queue that manages incoming ComputingEvents
     */
    private BlockingQueue<IComputingEvent> queue;

    public JavaScriptComputer(int id, TileEntityComputer entityComputer) {
        this.tileEntity = entityComputer;
        this.queue = new ArrayBlockingQueue(1024);

        this.scope = JavaScriptEngine.createScope();

        scope.defineProperty("print", new APIFunctionPrint(), ScriptableObject.READONLY);
        scope.defineProperty("wait", new APIFunctionWait(), ScriptableObject.READONLY);
        scope.defineProperty("computer", new APIComputer(tileEntity), ScriptableObject.READONLY);

        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getTempID() {
        return tempID;
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
    public void init() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JavaScriptEngine.contextEnter();
                while (true) {
                    try {
                        IComputingEvent event = queue.poll();
                        if (event != null) {
                            event.handle(JavaScriptComputer.this);

                            if (event instanceof ComputingEventShutDown) {
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                JavaScriptEngine.contextExit();
            }
        });
        thread.start();

        ComputerManager.add(this);
    }

    public boolean triggerEvent(IComputingEvent event) {
        return queue.offer(event);
    }

    @Override
    public void close() {
        triggerEvent(new ComputingEventShutDown());
        ComputerManager.remove(this);
    }
}