package com.nickardson.jscomputing.javascript;

public interface IComputer {

    /**
     * Gets the static ID of the computer.
     */
    public int getID();

    public int getTempID();

    public void eval(String code);

    public void init();

    public void close();
}
