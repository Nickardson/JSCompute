package com.nickardson.jscomputing.javascript.methods;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class APIFunctionWait extends JavaScriptFunction {
    @Override
    public Object invoke(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        double seconds = Math.max(0.05, (Double) Context.jsToJava(args[0], Double.TYPE));

        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException ignored) {
        }

        return seconds;
    }
}
