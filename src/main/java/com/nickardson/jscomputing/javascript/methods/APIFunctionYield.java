package com.nickardson.jscomputing.javascript.methods;

import com.nickardson.jscomputing.common.computers.IEventableComputer;
import com.nickardson.jscomputing.common.computers.events.ComputingEventEvent;
import com.nickardson.jscomputing.common.computers.events.IComputingEvent;
import com.nickardson.jscomputing.javascript.api.APIEvent;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class APIFunctionYield extends JavaScriptFunction {

    private IEventableComputer computer;

    public APIFunctionYield(IEventableComputer computer) {
        this.computer = computer;
    }

    @Override
    public Object invoke(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        try {
            while (true) {
                IComputingEvent event = (IComputingEvent) computer.getQueue().take();

                if (event instanceof ComputingEventEvent) {
                    ComputingEventEvent e = (ComputingEventEvent) event;
                    return APIEvent.create(e);
                }

                computer.handleEvent(event);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
