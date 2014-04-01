package com.nickardson.jscomputing.javascript;

import com.cloudbees.util.rhino.sandbox.SandboxContextFactory;
import com.nickardson.jscomputing.javascript.api.APIRobot;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrint;
import org.mozilla.javascript.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class JavaScriptEngine {
    public static ScriptableObject GLOBAL_SCOPE;

    public static boolean verbose = true;

    public static int INTERPRETED = -1;
    public static int OPTIMIZED = 9;

    private static int optimizationLevel;

    public static SandboxContextFactory contextFactory = new SandboxContextFactory();

    public static void setup() {
        GLOBAL_SCOPE = createScope();
    }

    public static void handleScriptError(Throwable exception) {
        if (exception instanceof WrappedException) {
            exception = ((WrappedException)exception).getWrappedException();
        }

        if (getScope().get("events") == null) {
            exception.printStackTrace(System.out);
        }
        else {
            invoke("exception", exception);
        }
    }

    private static String getErrorMessage(String error, Throwable exception) {
        if (error == null)
            return "Internal error: " + exception.getMessage();

        String[] split = error.split("EcmaError: ");
        if (split.length == 2) {
            return split[1];
        }
        else {
            return error;
        }
    }

    public static void invokeWithEval(String event, String arg) {
        contextEnter();
        try {
            Function trigger = ((Function) ((ScriptableObject) getScope().get("events")).get("trigger"));
            trigger.call(getContext(), getScope(), GLOBAL_SCOPE, new Object[]{event, eval(arg)});
        } catch (Exception e) {
            handleScriptError(e);
            // System.out.println("Lost event for " + event + " with argument " + arg);
        } finally {
            contextExit();
        }
    }

    public static void invoke(String event, Object arg) {
        contextEnter();
        try {
            if (getScope() != null) {
                Function trigger = ((Function) ((ScriptableObject) getScope().get("events")).get("trigger"));
                trigger.call(getContext(), getScope(), trigger, new Object[]{event, arg});
            }
        } catch (Exception e) {
            handleScriptError(e);
            System.out.println("Lost event for " + event + " with argument " + arg);
        } finally {
            contextExit();
        }
    }

    public static Object eval(String code) {
        return eval(code, "<eval>");
    }
    public static Object eval(String code, String source) {
        contextEnter();
        try {
            if (getScope() != null)
                return getContext().evaluateString(getScope(), code, source, 1, null);
        } catch (Exception e) {
            handleScriptError(e);
        } finally {
            contextExit();
        }
        return null;
    }

    public static void runResource(String path) {
        if (verbose)
            System.out.println("Run: " + path);

        runFile(JavaScriptEngine.class.getResourceAsStream(path), "classpath://"+path);
    }

    public static void runLibrary(String name) {
        runResource("/com/nickardson/jscomputing/js/" + name);
    }

    public static void runFile(InputStream file) {
        runFile(file, "<unknown>");
    }
    public static void runFile(InputStream file, String source) {
        eval(new Scanner(file).useDelimiter("\\Z").next(), source);
    }

    public static void runFile(File file) {
        if (verbose)
            System.out.println("Run: " + file);

        try {
            eval(new Scanner(file).useDelimiter("\\Z").next(), file.getPath());
        } catch (FileNotFoundException ex) {
            System.out.println("No such file: " + file);
        }
    }

    public static void runFile(String file) {
        runFile(new File(file));
    }

    public static ScriptableObject getScope() {
        return GLOBAL_SCOPE;
    }

    public static Context getContext() {
        return Context.getCurrentContext();
    }

    public static void put(String name, Object obj) {
        getScope().put(name, getScope(), obj);
    }

    public static void contextEnter() {
        Context c = contextFactory.makeContext();
        contextFactory.enterContext(c);
        c.setOptimizationLevel(optimizationLevel);
        c.setLanguageVersion(Context.VERSION_1_8);
    }

    public static void contextExit() {
        Context.exit();
    }

    public static void setOptimizationLevel(int optimizationLevel) {
        JavaScriptEngine.optimizationLevel = optimizationLevel;
    }

    public static ScriptableObject createScope() {
        contextEnter();
        ScriptableObject scope = new ImporterTopLevel(getContext());

        scope.put("robot", scope, new APIRobot());
        scope.put("print", scope, new APIFunctionPrint());

        runLibrary("main.js");
        Context.exit();
        return scope;
    }
}
