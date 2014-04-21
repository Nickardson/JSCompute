package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.common.computers.IScreenedComputer;
import org.mozilla.javascript.Context;

public class APIScreen {

    public static ScreenJSAPI create(IScreenedComputer computer) {
        return new ScreenJSAPI(computer);
    }

    public static class ScreenJSAPI {
        private IScreenedComputer computer;

        public class Cursor {
            public int getX() {
                return computer.getCursorX();
            }

            public void setX(int x) {
                set(x, getY());
            }

            public int getY() {
                return computer.getCursorY();
            }

            public void setY(int y) {
                set(getX(), y);
            }

            public void set(int x, int y) {
                computer.setCursor(x, y);
            }

            public void setVisible(boolean visible) {
                computer.setCursorVisible(visible);
            }

            public boolean isVisible() {
                return computer.isCursorVisible();
            }
        }

        private Cursor cursor;

        private ScreenJSAPI(IScreenedComputer computer) {
            this.computer = computer;
            this.cursor = new Cursor();
        }

        /**
         * Sends line updates to the clients.
         */
        private void update() {
            computer.updateLines(computer.getLines());
        }

        /**
         * Gets the cursor.
         *
         * @return The cursor.
         */
        public Cursor getCursor() {
            return cursor;
        }

        /**
         * Gets the character at the given screen coordinates.
         *
         * @param x x coordinate
         * @param y y coordinate
         * @return The character at the given location, or an empty string if out of bounds.
         */
        public String get(int x, int y) {
            if (x < 0 || y < 0 || x >= computer.getWidth() || y >= computer.getHeight()) {
                return "";
            }

            return Character.toString((char) (computer.getLines()[y][x] & 0xFF));
        }

        /**
         * Gets the width of the computer's screen.
         *
         * @return The width of the computer's screen.
         */
        public int getWidth() {
            return computer.getWidth();
        }

        /**
         * Gets the height of the computer's screen.
         *
         * @return The height of the computer's screen.
         */
        public int getHeight() {
            return computer.getHeight();
        }

        /**
         * Shifts the lines in the given array up, such that index 0 now contains index 1.
         *
         * @param lines The lines to shift.
         * @param count The number of times to shift.
         */
        private void shiftLinesUp(byte[][] lines, int count) {
            System.arraycopy(lines, count, lines, 0, lines.length - count);

            for (int i = lines.length - count; i < lines.length; i++) {
                lines[i] = new byte[lines[0].length];
            }
        }

        /**
         * Shifts the lines in the given array down, such that index 1 now contains index 0.
         *
         * @param lines The lines to shift.
         * @param count The number of times to shift.
         */
        private void shiftLinesDown(byte[][] lines, int count) {
            System.arraycopy(lines, 1 - count, lines, 1, lines.length - count);

            for (int i = 0; i < count; i++) {
                lines[i] = new byte[lines[0].length];
            }
        }

        /**
         * Causes the lines to scroll in the given direction, does not send a screen update.
         *
         * @param dir The direction and number to scroll. Negative scrolls up, positive down.
         */
        private void scrollInternal(int dir) {
            if (dir == 0) {
                return;
            }

            boolean up = dir < 0;
            dir = Math.min(dir, getHeight());

            if (up) {
                shiftLinesUp(computer.getLines(), Math.abs(dir));
            } else {
                shiftLinesDown(computer.getLines(), dir);
            }

            cursor.setY(cursor.getY() + dir);
        }

        /**
         * Causes the lines to scroll in the given direction.
         *
         * @param dir The direction and number to scroll. Negative scrolls up, positive down.
         */
        public void scroll(int dir) {
            scrollInternal(dir);
            update();
        }

        /**
         * Writes the given object after the current cursor position.
         *
         * @param o The object to write.
         */
        public void write(Object o) {
            // Convert the argument to a string
            String result = ((String) Context.jsToJava(o, String.class)).replace("\n", "");

            byte[][] lines = computer.getLines();

            int x = computer.getCursorX(),
                    y = computer.getCursorY();

            // Write the string until we either reach the width, or run out of space.
            for (int i = 0; i < result.length() && x < computer.getWidth(); i++) {
                if (x + i < computer.getWidth()) {
                    lines[y][x + i] = (byte) result.charAt(i);
                } else {
                    break;
                }
            }

            computer.setCursor(x + result.length(), y);
            update();
        }

        /**
         * Writes the given object, and a new line, scrolling if necessary.
         *
         * @param o The object to print.
         */
        public void print(Object o) {
            if (computer.getCursorY() == computer.getHeight() - 1) {
                scrollInternal(-1);
            }
            write(o);
            computer.setCursor(0, computer.getCursorY() + 1);
        }

        /**
         * Clears the current line.
         */
        public void clearLine() {
            clearLine(getCursor().getY());
        }

        /**
         * Clears the current line to the given character.
         * @param str The character the screen will be cleared to.
         */
        public void clearLine(String str) {
            clearLine(getCursor().getY(), str);
        }

        /**
         * Clears the given line.
         * @param y Clears the given line.
         */
        public void clearLine(int y) {
            clearLineInternal(y, (byte) 0);
        }

        /**
         * Clears the given line.
         * @param y Clears the given line.
         * @param str The character the screen will be cleared to.
         */
        public void clearLine(int y, String str) {
            byte b = 0;
            if (str.length() > 0) {
                b = (byte) str.charAt(0);
            }

            clearLineInternal(y, b);
        }

        private void clearLineInternal(int y, byte b) {
            byte[][] lines = computer.getLines();
            for (int x = 0; x < getWidth(); x++) {
                lines[y][x] = b;
            }
        }

        /**
         * Clears the screen, with the first character of the given string.
         *
         * @param str The character the screen will be cleared to.
         */
        public void clear(String str) {
            byte b = 0;
            if (str.length() > 0) {
                b = (byte) str.charAt(0);
            }

            for (int y = 0; y < getHeight(); y++) {
                clearLineInternal(y, b);
            }

            computer.setCursor(0, 0);
            update();
        }

        /**
         * Clears the screen.
         */
        public void clear() {
            clear("");
        }

        public boolean isPrintable(char c) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
            return !Character.isISOControl(c) && block != null && block != Character.UnicodeBlock.SPECIALS;
        }
    }
}
