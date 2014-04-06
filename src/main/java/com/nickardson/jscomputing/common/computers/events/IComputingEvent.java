package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.common.computers.IServerComputer;

/**
 * An event to be queued up to run on a computer.
 */
public interface IComputingEvent {
    /**
     * Called when this event is handled.
     * @param computer The computer this event is applied to.
     */
    public void handle(IServerComputer computer);
}
