package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.JavaScriptComputer;
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
    public void handle(IComputer computer) {
        if (computer instanceof JavaScriptComputer) {
            JavaScriptComputer c = (JavaScriptComputer) computer;
            Context.getCurrentContext().evaluateString(c.getScope(), code, source, 1, null);
        }
    }
}
