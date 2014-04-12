package com.nickardson.jscomputing.javascript.methods;

import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import com.nickardson.jscomputing.common.computers.events.ComputingEventEvent;
import com.nickardson.jscomputing.common.computers.events.IComputingEvent;
import com.nickardson.jscomputing.javascript.api.APIEvent;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class APIFunctionYield extends JavaScriptFunction {

    private ServerTerminalComputer computer;

    public APIFunctionYield(ServerTerminalComputer computer) {
        this.computer = computer;
    }

    @Override
    public Object invoke(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        try {
            while (true) {
                IComputingEvent event = computer.getQueue().take();

                if (event instanceof ComputingEventEvent) {
                    ComputingEventEvent e = (ComputingEventEvent) event;
                    return APIEvent.create(e);
                } else {
                    computer.handleEvent(event);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
