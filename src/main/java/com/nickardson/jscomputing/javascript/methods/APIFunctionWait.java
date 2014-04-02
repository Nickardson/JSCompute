package com.nickardson.jscomputing.javascript.methods;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class APIFunctionWait extends JavaScriptFunction {
    @Override
    public Object invoke(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        long millis = Math.max(1, (Long) Context.jsToJava(args[0], Long.TYPE));

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        } finally {
            return millis;
        }
    }
}
