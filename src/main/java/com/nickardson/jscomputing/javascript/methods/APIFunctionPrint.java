package com.nickardson.jscomputing.javascript.methods;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class APIFunctionPrint extends JavaScriptFunction {
    @Override
    public Object invoke(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length > 0) {
            System.out.println(Context.jsToJava(args[0], String.class));
        }
        return null;
    }
}
