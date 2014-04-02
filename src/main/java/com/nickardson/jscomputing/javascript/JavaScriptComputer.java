package com.nickardson.jscomputing.javascript;

import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;
import com.nickardson.jscomputing.javascript.api.APIComputer;
import com.nickardson.jscomputing.javascript.api.APIRobot;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrint;
import org.mozilla.javascript.ScriptableObject;

public class JavaScriptComputer implements IComputer {
    ScriptableObject scope;

    static int GLOBAL_TEMP_ID = 0;

    int id = 0;
    int tempID = ++GLOBAL_TEMP_ID;
    private TileEntityComputer tileEntity;

    public JavaScriptComputer(int id, TileEntityComputer entityComputer) {
        this.tileEntity = entityComputer;
        this.scope = JavaScriptEngine.createScope();

        scope.put("print", scope, new APIFunctionPrint());
        scope.put("computer", scope, new APIComputer(tileEntity));

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
        try {
            JavaScriptEngine.getContext().evaluateString(scope, code, "<eval>", 1, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JavaScriptEngine.contextExit();
        }
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
