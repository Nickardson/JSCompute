package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;

public class APIComputer {
    private TileEntityTerminalComputer computer;
    public APIComputer(TileEntityTerminalComputer computer) {
        this.computer = computer;
    }

    public void off() {
        JSComputingMod.computorz.turnOff(computer);
    }

    public int getId() {
        return computer.getComputerID();
    }
}
