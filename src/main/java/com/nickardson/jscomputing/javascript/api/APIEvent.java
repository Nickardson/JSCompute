package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.common.computers.events.ComputingEventEvent;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Map;

public class APIEvent extends ScriptableObject {

    private String name;
    private Map<String, Object> properties;

    public APIEvent(String name, Map<String, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public APIEvent(ComputingEventEvent event) {
        this(event.getName(), event.getArgs());
    }

    @Override
    public String getClassName() {
        return "Event";
    }

    @Override
    public Object getDefaultValue(Class<?> typeHint) {
        return "[event]";
    }

    @Override
    public Object get(String name, Scriptable start) {
        return get(name);
    }

    @Override
    public Object get(Object key) {
        if (key.equals("name")) {
            return name;
        } else if (properties.containsKey(key)) {
            return properties.get(key);
        }
        return Scriptable.NOT_FOUND;
    }
}
