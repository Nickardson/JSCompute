package com.nickardson.jscomputing.javascript;

import com.cloudbees.util.rhino.sandbox.SandboxContextFactory;
import org.mozilla.javascript.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class JavaScriptEngine {
    /**
     * Represents an OptimizationLevel with no optimizations.
     */
    public static int INTERPRETED = -1;

    /**
     * Represents an OptimizationLevel with full optimization.
     */
    public static int OPTIMIZED = 9;

    public static SandboxContextFactory contextFactory = new SandboxContextFactory();

    /**
     * Called when an exception is thrown by a script.
     * @param exception
     */
    public static void handleScriptError(Throwable exception) {
        // Unwrap exception
        if (exception instanceof WrappedException) {
            exception = ((WrappedException)exception).getWrappedException();
        }
        exception.printStackTrace(System.out);
    }

    /**
     * Evaluates a string as JavaScript code.
     * @param scope The global object.
     * @param code The code to run.
     * @return
     * The result of the evaluation.
     */
    public static Object eval(ScriptableObject scope, String code) {
        return eval(scope, code, "<eval>");
    }

    /**
     * Evaluates a string as JavaScript code.
     * @param scope The global object.
     * @param code The code to run.
     * @param source The name of the source where this code came from.
     * @return
     * The result of the evaluation.
     */
    public static Object eval(ScriptableObject scope, String code, String source) {
        contextEnter();
        try {
            if (scope != null) {
                return getContext().evaluateString(scope, code, source, 1, null);
            }
        } catch (Exception e) {
            handleScriptError(e);
        } finally {
            contextExit();
        }
        return null;
    }

    /**
     * Evaluates a resource from the classpath.
     * @param scope The global object.
     * @param path The fully qualified path to the file.
     */
    public static void runResource(ScriptableObject scope, String path) {
        runInputStream(scope, JavaScriptEngine.class.getResourceAsStream(path), "classpath://" + path);
    }

    /**
     * Evaluates a script from the stored JavaScript scripts.
     * @param scope The global object.
     * @param name The name of the library to run.
     */
    public static void runLibrary(ScriptableObject scope, String name) {
        runResource(scope, "/com/nickardson/jscomputing/js/" + name);
    }

    /**
     * Evaluates an InputStream.
     * @param scope The global object.
     * @param stream The stream.
     */
    public static void runInputStream(ScriptableObject scope, InputStream stream) {
        runInputStream(scope, stream, "<unknown>");
    }

    /**
     * Evaluates an InputStream.
     * @param scope The global object.
     * @param stream The stream.
     * @param source The name of the source where this code came from.
     */
    public static void runInputStream(ScriptableObject scope, InputStream stream, String source) {
        eval(scope, new Scanner(stream).useDelimiter("\\Z").next(), source);
    }

    /**
     * Evaluates a file.
     * @param scope The global object.
     * @param file The file to run.
     */
    public static void runFile(ScriptableObject scope, File file) {
        try {
            eval(scope, new Scanner(file).useDelimiter("\\Z").next(), file.getPath());
        } catch (FileNotFoundException ex) {
            System.out.println("No such file: " + file);
        }
    }

    /**
     * Evaluates a file.
     * @param scope The global object.
     * @param file The file to run.
     */
    public static void runFile(ScriptableObject scope, String file) {
        runFile(scope, new File(file));
    }

    /**
     * Gets the current script context.
     * @return The current script context.
     */
    public static Context getContext() {
        return Context.getCurrentContext();
    }

    /**
     * Enters a script context.
     */
    public static void contextEnter() {
        Context c = contextFactory.makeContext();
        contextFactory.enterContext(c);
        c.setOptimizationLevel(optimizationLevel);
        c.setLanguageVersion(Context.VERSION_1_8);
    }

    /**
     * Exits the current context.
     */
    public static void contextExit() {
        Context.exit();
    }

    private static int optimizationLevel;

    /**
     * Gets the current script optimization level.
     * @return The level of optimization, from -1 to 9.
     */
    public static int getOptimizationLevel() {
        return optimizationLevel;
    }

    /**
     * Sets the current script optimization level.
     * @param optimizationLevel The level of optimization, from -1 to 9.
     */
    public static void setOptimizationLevel(int optimizationLevel) {
        JavaScriptEngine.optimizationLevel = optimizationLevel;
    }

    /**
     * Creates a new scope (global object).
     * @return The created scope.
     */
    public static ScriptableObject createScope() {
        contextEnter();
        ScriptableObject scope = new ImporterTopLevel(getContext());
        Context.exit();
        return scope;
    }
}
