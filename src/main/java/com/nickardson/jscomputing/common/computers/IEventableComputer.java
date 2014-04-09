package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.ComputingEventEvent;

/**
 * A computer capable of handling events.
 */
public interface IEventableComputer {
    /**
     * Called when an event is triggered.
     * @param event The event.
     */
    public void onEvent(ComputingEventEvent event);
}
