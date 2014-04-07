package com.nickardson.jscomputing.common.computers;

/**
 * A computer capable of handling events.
 */
public interface IEventableComputer {
    /**
     * Called when an event is triggered.
     * @param name The name of the event
     * @param args The arguments to the event.
     */
    public void onEvent(String name, Object[] args);
}
