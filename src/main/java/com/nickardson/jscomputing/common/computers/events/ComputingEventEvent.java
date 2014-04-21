package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.common.computers.IEventableComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;

import java.util.HashMap;
import java.util.Map;

/**
 * A computing event representing an event.
 * wow
 *    so meta.
 */
public class ComputingEventEvent extends CancellableComputingEvent {

    private String name;
    private Map<String, Object> args;

    public ComputingEventEvent() {
    }

    public ComputingEventEvent(String name) {
        this(name, new HashMap<String, Object>());
    }

    public ComputingEventEvent(String name, Map<String, Object> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public void handle(IServerComputer computer) {
        if (computer instanceof IEventableComputer) {
            ((IEventableComputer) computer).onEvent(this);
        }
    }

    public String getName() {
        return name;
    }

    public Object get(String key) {
        if (args.containsKey(key)) {
            return args.get(key);
        } else {
            return null;
        }
    }

    public void put(String key, Object value) {
        args.put(key, value);
    }

    public Map<String, Object> getArgs() {
        return args;
    }
}
