/**
 * Wraps a string to the given length.
 * @param width The maximum width of the wrapped text.
 * @param [newline] (Optional) The separator between wrapped lines.  Defaults to newline.
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
 * Centers a text with whitespace padding.
 * @param width The width to fill.
 * @param padding (Optional) The character to pad the blanks with.
 */
String.prototype.center = function (width, padding) {
    padding = padding || " "
    if (this.length < width) {
        return new Array(Math.floor((width - this.length) / 2)).join(padding) + this + new Array(Math.ceil((width - this.length) / 2)).join(padding);
    }
    return this + "";
};
