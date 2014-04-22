package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.CancellableComputingEvent;
import com.nickardson.jscomputing.common.computers.events.ComputingEventEvent;
import com.nickardson.jscomputing.common.computers.events.ComputingEventShutDown;
import com.nickardson.jscomputing.common.computers.events.IComputingEvent;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import com.nickardson.jscomputing.javascript.api.APIComputer;
import com.nickardson.jscomputing.javascript.api.APIEvent;
import com.nickardson.jscomputing.javascript.api.APIFile;
import com.nickardson.jscomputing.javascript.api.APIScreen;
import com.nickardson.jscomputing.javascript.api.fs.*;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrint;
import com.nickardson.jscomputing.javascript.methods.APIFunctionWait;
import com.nickardson.jscomputing.javascript.methods.APIFunctionYield;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractJavaScriptServerTerminalComputer extends AbstractServerTerminalComputer implements IScriptableComputer, IEventableComputer {
    public AbstractJavaScriptServerTerminalComputer(int id, AbstractTileEntityComputer entityComputer) {
        super(id);

        this.tileEntity = entityComputer;
        this.queue = new ArrayBlockingQueue<IComputingEvent>(1024);
        this.scope = JavaScriptEngine.createScope();
    }

    @Override
    public boolean triggerEvent(IComputingEvent event) {
        return getQueue().offer(event);
    }

    @Override
    public void handleEvent(IComputingEvent event) {
        if (!(event instanceof CancellableComputingEvent) || !((CancellableComputingEvent) event).isCancelled()) {
            event.handle(this);
        }

        if (event instanceof ComputingEventShutDown) {
            shutdown = true;
        }
    }

    private boolean shutdown = false;

    @Override
    public void start() {
        getScope().defineProperty("wait", new APIFunctionWait(), ScriptableObject.READONLY);
        getScope().defineProperty("stdout", new APIFunctionPrint(), ScriptableObject.READONLY);
        getScope().defineProperty("pull", new APIFunctionYield(this), ScriptableObject.READONLY);
        getScope().defineProperty("computer", APIComputer.create(getTileEntity()), ScriptableObject.READONLY);
        getScope().defineProperty("screen", APIScreen.create(this), ScriptableObject.READONLY);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JavaScriptEngine.contextEnter();
                JavaScriptEngine.runLibrary(getScope(), "system");
                JavaScriptEngine.runLibrary(getScope(), "os");

                while (!shutdown) {
                    try {
                        IComputingEvent event = getQueue().take();
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

    @Override
    public Object convert(Object object) {
        if (object.getClass().isArray()) {
            Object[] ls = (Object[]) object;
            return JavaScriptEngine.getContext().newArray(getScope(), Arrays.copyOf(ls, ls.length, Object[].class));
        }

        return JavaScriptEngine.convert(object, getScope());
    }

    @Override
    public Object get(String key) {
        return getScope().get(key);
    }

    @Override
    public void put(String key, Object value) {
        getScope().put(key, getScope(), value);
    }

    @Override
    public void eval(String code) {
        eval(code, "");
    }

    @Override
    public void eval(String code, String source) {
        triggerEvent(new ComputingEventEval(code, source));
    }

    private class ComputingEventEval extends CancellableComputingEvent {
        String code;
        String source;

        public ComputingEventEval(String code, String source) {
            this.code = code;
            this.source = source;
        }

        @Override
        public void handle(IServerComputer computer) {
            Context.getCurrentContext().evaluateString(AbstractJavaScriptServerTerminalComputer.this.getScope(), code, source, 1, null);
        }

        @Override
        public String toString() {
            return String.format("[event Eval: %s]", code);
        }
    }

    @Override
    public void stop() {
        triggerEvent(new ComputingEventShutDown(getTileEntity()));
        ComputerManager.removeComputer(getID());
    }

    private AbstractTileEntityComputer tileEntity;
    public AbstractTileEntityComputer getTileEntity() {
        return tileEntity;
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
    @Override
    public BlockingQueue<IComputingEvent> getQueue() {
        return queue;
    }
}
