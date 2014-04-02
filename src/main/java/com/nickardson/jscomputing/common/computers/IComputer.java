package com.nickardson.jscomputing.common.computers;

public interface IComputer {

    /**
     * Gets the static ID of the computer.
     */
    public int getID();

    public int getTempID();

    public void eval(String code);

    public Object get(String key);

    public void put(String key, Object value);

    public void init();

    public void close();
}
