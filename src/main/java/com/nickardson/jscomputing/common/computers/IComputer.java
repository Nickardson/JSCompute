package com.nickardson.jscomputing.common.computers;

/**
 * A basic, extensible computer.
 */
public interface IComputer {
    /**
     * Gets the ID of the computer.
     */
    public int getID();

    /**
     * Starts this computer.
     */
    public void start();

    /**
     * Stops this computer.
     */
    public void stop();

}
