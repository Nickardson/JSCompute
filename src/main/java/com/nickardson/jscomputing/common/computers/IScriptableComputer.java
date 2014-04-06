package com.nickardson.jscomputing.common.computers;

/**
 * A Computer which can run scripts, and store values.
 */
public interface IScriptableComputer {
    /**
     * Evaluates code.
     * @param code
     */
    public void eval(String code);

    /**
     * Gets the value of the given key.
     * @param key The name of the variable to get.
     * @return
     * The returned object.
     */
    public Object get(String key);

    /**
     * Puts a value into the global scope.
     * @param key The name of the variable to set.
     * @param value The value to set.
     */
    public void put(String key, Object value);
}