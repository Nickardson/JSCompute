package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;

public class APIComputer {
    private TileEntityComputer computer;
    public APIComputer(TileEntityComputer computer) {
        this.computer = computer;
    }

    public void off() {
        JSComputingMod.computorz.turnOff(computer);
    }

    public int getId() {
        return computer.getComputerID();
    }
}
