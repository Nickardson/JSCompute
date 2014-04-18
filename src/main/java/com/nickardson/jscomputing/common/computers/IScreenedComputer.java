package com.nickardson.jscomputing.common.computers;

/**
 * A computer with a text screen.
 */
public interface IScreenedComputer {
    /**
     * Sets the lines.
     * @param lines A two-dimensional char array containing the terminal text
     */
    public void setLines(byte[][] lines);

    /**
     * Sets the lines, and triggers an update packet.
     * @param lines A two-dimensional char array containing the terminal text
     */
    public void updateLines(byte[][] lines);

    /**
     * Gets the lines.
     * @return
     * A two-dimensional char array containing the terminal text.
     */
    public byte[][] getLines();

    /**
     * Update the terminal lines, ie send to the clients.
     * @param force Whether to force the lines to be sent, regardless of lag-reduction or other situations.
     */
    public void sendLines(boolean force);

    /**
     * Provides the width of the terminal
     */
    public int getWidth();

    /**
     * Provides the height of the terminal
     */
    public int getHeight();

    /**
     * The "x" coordinate of the cursor.
     */
    public int getCursorX();

    /**
     * The "y" coordinate of the cursor.
     */
    public int getCursorY();

    /**
     * Sets the cursor's position, 0-based index.
     * @param x
     * The X coordinate
     * @param y
     * The Y coordinate
     */
    public void setCursor(int x, int y);

    /**
     * Gets whether the cursor should be rendered.
     * @return Whether the cursor is visible.
     */
    public boolean isCursorVisible();

    /**
     * Sets whether the cursor should be rendered.
     * @param visible Whether the cursor is visible.
     */
    public void setCursorVisible(boolean visible);
}
