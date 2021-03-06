package com.nickardson.jscomputing.common.computers;

/**
 * A computer which implements a Terminal screen.
 */
public abstract class AbstractTerminalComputer extends AbstractComputer implements IScreenedComputer {
    public static int TERMINAL_WIDTH = 50;
    public static int TERMINAL_HEIGHT = 20;

    byte[][] lines;

    int cursorX = 0;
    int cursorY = 0;

    boolean cursorVisible = false;
    boolean cursorUpdated = false;

    public AbstractTerminalComputer(int id) {
        super(id);

        lines = new byte[getHeight()][getWidth()];
    }

    public boolean isCursorUpdated() {
        return cursorUpdated;
    }

    public void setCursorUpdated(boolean cursorUpdated) {
        this.cursorUpdated = cursorUpdated;
    }

    @Override
    public byte[][] getLines() {
        return lines;
    }

    @Override
    public boolean isCursorVisible() {
        return cursorVisible;
    }

    @Override
    public void setCursorVisible(boolean cursorVisible) {
        this.cursorVisible = cursorVisible;
        cursorUpdated = true;
    }

    @Override
    public void setLines(byte[][] lines) {
        this.lines = lines;
    }

    @Override
    public int getWidth() {
        return TERMINAL_WIDTH;
    }

    @Override
    public int getHeight() {
        return TERMINAL_HEIGHT;
    }

    @Override
    public int getCursorX() {
        return cursorX;
    }

    @Override
    public int getCursorY() {
        return cursorY;
    }

    @Override
    public void setCursor(int x, int y) {
        this.cursorX = Math.min(Math.max(x, 0), getWidth() - 1);
        this.cursorY = Math.min(Math.max(y, 0), getHeight() - 1);
        this.cursorUpdated = true;
    }
}
