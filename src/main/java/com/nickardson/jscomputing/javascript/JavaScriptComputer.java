package com.nickardson.jscomputing.javascript;

import org.mozilla.javascript.ScriptableObject;

public class JavaScriptComputer implements IComputer {
    ScriptableObject scope;

    static int GLOBAL_TEMP_ID = 0;

    int id = 0;
    int tempID = ++GLOBAL_TEMP_ID;

    public JavaScriptComputer(int id) {
        this.scope = JavaScriptEngine.createScope();
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
        JavaScriptEngine.contextEnter();
        JavaScriptEngine.getContext().evaluateString(scope, code, "<eval>", 1, null);
        JavaScriptEngine.contextExit();
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
        ComputerManager.add(this);
    }

    @Override
    public void close() {
        ComputerManager.remove(this);
    }
}
