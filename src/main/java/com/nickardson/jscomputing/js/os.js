includeLibrary("Events.js");

/**
 * Wraps a string to the given length.
 * @param width The maximum width of the wrapped text.
 * @param newline (Optional) The separator between wrapped lines.  Defaults to newline.
 * @returns {String} The wrapped string.
 */
String.prototype.wrap = function (width, newline) {
    newline = newline || "\n";
    if (this.length > width) {
        var i = width;
        for (; i > 0 && this[i] != ' '; i--) {}
        if (i > 0) {
            var left = this.substring(0, i);
            var right = this.substring(i + 1);
            return left + newline + String.prototype.wrap.call(right, width, newline);
        }
    }
    return this;
};

/**
 * Prints the given argument, with text wrapping.
 * @param arg The object to print out.
 */
var print = function (arg) {
    var split = (arg + "").wrap(screen.width).split("\n");
    for (var i = 0; i < split.length; i++) {
        screen.print(split[i]);
    }
};

/**
 * Writes the given argument at the current cursor position.
 * @param arg The object to write out.
 */
var write = function (arg) {
    screen.write(arg);
};

includeLibrary("main.js");