package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.common.computers.events.ComputingEventShutDown;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;

public class APIComputer {
    public static ComputerJSAPI create(TileEntityTerminalComputer computer) {
        return new ComputerJSAPI(computer);
    }

    public static class ComputerJSAPI {
        private TileEntityTerminalComputer computer;

        private ComputerJSAPI(TileEntityTerminalComputer computer) {
            this.computer = computer;
        }

        public void off() {
            computer.getServerComputer().triggerEvent(new ComputingEventShutDown(computer));
        }

        public int getId() {
            return computer.getComputerID();
        }
    }
}
