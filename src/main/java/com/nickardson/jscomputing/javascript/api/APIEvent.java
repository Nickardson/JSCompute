package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.common.computers.events.ComputingEventEvent;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Map;

public class APIEvent {

    public static EventJSAPI create(String name, Map<String, Object> properties) {
        return new EventJSAPI(name, properties);
    }
    public static EventJSAPI create(ComputingEventEvent event) {
        return new EventJSAPI(event.getName(), event.getArgs());
    }

    public static class EventJSAPI extends ScriptableObject {
        private String name;
        private Map<String, Object> properties;

        private EventJSAPI(String name, Map<String, Object> properties) {
            this.name = name;
            this.properties = properties;
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
}
