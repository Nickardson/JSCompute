package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.IComputingEvent;

import java.util.concurrent.BlockingQueue;

public interface IComputer {

    /**
     * Gets the static ID of the computer.
     */
    public int getID();

    public int getTempID();

    /**
     * Evaluates code.
     * @param code
     */
    public void eval(String code);

    public Object get(String key);

    public void put(String key, Object value);

    public void init();

    public void close();

    public boolean triggerEvent(IComputingEvent event);
}
