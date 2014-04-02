package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.common.computers.IComputer;

public interface IComputingEvent {
    public void handle(IComputer computer);
}
