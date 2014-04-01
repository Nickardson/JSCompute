package com.nickardson.jscomputing.javascript.methods;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.Scriptable;

public abstract class JavaScriptFunction extends NativeFunction {
    @Override
    protected int getLanguageVersion() {
        return Context.VERSION_1_8;
    }

    public abstract Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args);

    @Override
    protected int getParamCount() {
        return 0;
    }

    @Override
    protected int getParamAndVarCount() {
        return 0;
    }

    @Override
    protected String getParamOrVarName(int index) {
        return null;
    }
}
