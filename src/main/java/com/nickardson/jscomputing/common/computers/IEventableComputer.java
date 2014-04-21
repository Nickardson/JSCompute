package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.ComputingEventEvent;
import com.nickardson.jscomputing.common.computers.events.IComputingEvent;

import java.util.concurrent.BlockingQueue;

/**
 * A computer capable of handling events.
 */
public interface IEventableComputer {
    /**
     * Called when an event is triggered.
     * @param event The event.
     */
    public void onEvent(ComputingEventEvent event);

    /**
     * Immediately handles the given event.
     * @param event The event to handle.
     */
    public void handleEvent(IComputingEvent event);

    /**
     * Gets the event queue.
     * @return The event queue.
     */
    public BlockingQueue getQueue();
}
