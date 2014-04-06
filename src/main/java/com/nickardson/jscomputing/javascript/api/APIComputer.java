package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.common.computers.events.ComputingEventShutDown;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;

public class APIComputer {
    private TileEntityTerminalComputer computer;
    public APIComputer(TileEntityTerminalComputer computer) {
        this.computer = computer;
    }

    public void off() {
        computer.getServerComputer().triggerEvent(new ComputingEventShutDown(computer));
    }

    public int getId() {
        return computer.getComputerID();
    }
}
