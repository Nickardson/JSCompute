package com.nickardson.jscomputing.common.computers;

public interface IScreenedComputer {
    public void setLines(char[][] lines);

    public char[][] getLines();

    public void tickTerminalLines();

    public int getWidth();
    public int getHeight();

    public int getCursorX();
    public int getCursorY();
    public void setCursor(int x, int y);
}
