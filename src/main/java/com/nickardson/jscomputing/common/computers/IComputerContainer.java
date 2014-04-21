package com.nickardson.jscomputing.common.computers;

public interface IComputerContainer {
    int getComputerID();
    void setComputerID(int id);

    boolean isOn();
    void setOn(boolean on);

    IComputer getComputer();
}
