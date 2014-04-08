package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.common.computers.IEventableComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;

public class ComputingEventEvent implements IComputingEvent {

    private String name;
    private Object[] args;

    @SuppressWarnings("UnusedDeclaration")
    public ComputingEventEvent() {
    }

    public ComputingEventEvent(String name, Object... args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public void handle(IServerComputer computer) {
        if (computer instanceof IEventableComputer) {
            ((IEventableComputer) computer).onEvent(name, args);
        }
    }
}
