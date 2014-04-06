package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.JSComputingMod;

/**
 * A computer which implements a Terminal screen.
 */
public abstract class AbstractTerminalComputer extends AbstractComputer implements IScreenedComputer {
    char[][] lines;

    int cursorX = 0;
    int cursorY = 0;

    public AbstractTerminalComputer(int id) {
        super(id);

        lines = new char[getHeight()][getWidth()];
    }

    @Override
    public char[][] getLines() {
        return lines;
    }

    @Override
    public void setLines(char[][] lines) {
        this.lines = lines;
    }

    @Override
    public int getWidth() {
        return JSComputingMod.TERMINAL_WIDTH;
    }

    @Override
    public int getHeight() {
        return JSComputingMod.TERMINAL_HEIGHT;
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
        this.cursorY = Math.min(Math.max(x, 0), getWidth() - 1);
        this.cursorY = Math.min(Math.max(y, 0), getHeight() - 1);
    }
}
