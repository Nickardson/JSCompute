package com.nickardson.jscomputing.javascript.methods;

import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Includes a file from within the given classpath.
 */
public class APIFunctionIncludeClasspath extends JavaScriptFunction {

    private String root;

    public APIFunctionIncludeClasspath(String root) {
        this.root = root;
    }

    @Override
    public Object invoke(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        String file = String.valueOf(args[0]);

        if (file.contains("..")) {
            System.out.println("Trying to escape out of directory");
            return false;
        }

        try {
            JavaScriptEngine.runResource(scope, root + file);
        } catch (Exception e) {
            System.out.println("Could not load " + root + file);
        }
        return true;
    }
}
