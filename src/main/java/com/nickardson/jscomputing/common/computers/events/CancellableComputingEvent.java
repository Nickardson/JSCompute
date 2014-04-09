package com.nickardson.jscomputing.common.computers.events;

/**
 * An event which can be cancelled.  What cancelling means is up to the the implementation.
 */
public abstract class CancellableComputingEvent implements IComputingEvent {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }
}
