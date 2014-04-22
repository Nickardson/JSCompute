package com.nickardson.jscomputing.common.computers;

/**
 * A computer running on the server.
 */
public interface IServerComputer extends IComputer {
    /**
     * Called upon server tick.
     */
    public void tick();

    /**
     * Called upon a player opening up the GUI.
     */
    public void onPlayerOpenGui();
}
