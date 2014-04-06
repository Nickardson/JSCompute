package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.common.computers.IServerComputer;
import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import org.mozilla.javascript.Context;

public class ComputingEventJavaScriptEval implements IComputingEvent {
    private String code;
    private String source;

    public ComputingEventJavaScriptEval(String code) {
        this(code, "");
    }

    public ComputingEventJavaScriptEval(String code, String source) {
        this.code = code;
        this.source = source;
    }

    @Override
    public void handle(IServerComputer computer) {
        if (computer instanceof ServerTerminalComputer) {
            ServerTerminalComputer c = (ServerTerminalComputer) computer;
            Context.getCurrentContext().evaluateString(c.getScope(), code, source, 1, null);
        }
    }

    @Override
    public String toString() {
        return String.format("[event Eval: %s]", code);
    }
}
