package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.common.computers.AbstractTileEntityComputer;
import com.nickardson.jscomputing.common.computers.IEventableComputer;
import com.nickardson.jscomputing.common.computers.events.ComputingEventShutDown;

public class APIComputer {
    public static ComputerJSAPI create(AbstractTileEntityComputer computer) {
        return new ComputerJSAPI(computer);
    }

    public static class ComputerJSAPI {
        private AbstractTileEntityComputer computer;

        private ComputerJSAPI(AbstractTileEntityComputer computer) {
            this.computer = computer;
        }

        public void off() {
            ((IEventableComputer) computer.getComputer()).triggerEvent(new ComputingEventShutDown(computer));
        }

        public int getId() {
            return computer.getComputerID();
        }
    }
}
