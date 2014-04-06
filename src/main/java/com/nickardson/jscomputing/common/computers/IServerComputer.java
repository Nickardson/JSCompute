package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.IComputingEvent;

/**
 * A computer running on the server.
 */
public interface IServerComputer extends IComputer {
    /**
     * Called upon server tick.
     */
    public void tick();

    /**
     * Called when a user provides input.
     * @param text The text input.
     */
    public void input(String text);

    /**
     * Adds an event to the event queue.
     * @param event The event to queue up.
     * @return
     * Whether the event was successfully queued.
     */
    public boolean triggerEvent(IComputingEvent event);

    /**
     * Called upon a player opening up the GUI.
     */
    public void onPlayerOpenGui();
}
