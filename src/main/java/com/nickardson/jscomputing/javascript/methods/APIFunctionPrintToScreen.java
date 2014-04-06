package com.nickardson.jscomputing.javascript.methods;

import com.nickardson.jscomputing.common.computers.IScreenedComputer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class APIFunctionPrintToScreen extends JavaScriptFunction {

    IScreenedComputer computer;

    public APIFunctionPrintToScreen(IScreenedComputer computer) {
        this.computer = computer;
    }

    /**
     * Shifts the lines in the given array up, such that index 0 now contains index 1.
     * @param lines
     */
    private void shiftLinesUp(char[][] lines) {
        for (int i = 1; i < lines.length; i++) {
            lines[i - 1] = lines[i];
        }
        lines[lines.length - 1] = new char[lines[0].length];
    }

    /**
     * Shifts the lines in the given array down, such that index 1 now contains index 0.
     * @param lines
     */
    private void shiftLinesDown(char[][] lines) {
        for (int i = lines.length - 1; i > 0; i--) {
            lines[i] = lines[i - 1];
        }
    }

    @Override
    public Object invoke(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length > 0) {
            // Convert the argument to a string
            String result = (String) Context.jsToJava(args[0], String.class);

            char[][] lines = computer.getLines();

            int x = computer.getCursorX(),
                y = computer.getCursorY();

            // If the cursor is at the end, shift all lines up.  Otherwise, shift to a new line.
            if (y == computer.getHeight() - 1) {
                shiftLinesUp(lines);
            } else {
                computer.setCursor(0, ++y);
            }

            // Write the string until we either reach the width, or run out of space.
            for (int i = 0; i < result.length() && x < computer.getWidth(); i++, x++) {
                lines[y][x] = result.charAt(i);
            }
            computer.updateLines(lines);
        }
        return null;
    }
}
