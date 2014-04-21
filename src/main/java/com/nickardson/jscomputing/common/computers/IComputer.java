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
     * Sets the ID of the computer.
     */
    public void setID(int id);

    /**
     * Starts this computer.
     */
    public void start();

    /**
     * Stops this computer.
     */
    public void stop();

    /**
     * Gets an object in a form which APIs can use it.
     * IE, turning a Java object into a Scriptable Object.
     * @param object The object to convert.
     * @return The converted object.
     */
    public Object convert(Object object);
}
