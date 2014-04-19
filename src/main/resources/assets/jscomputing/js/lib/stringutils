/**
 * Wraps a string to the given length.
 * @param width The maximum width of the wrapped text.
 * @param [newline] (Optional) The separator between wrapped lines.  Defaults to newline.
 * @returns {String} The wrapped string.
 */
String.wrap = function (str, width, newline) {
    newline = newline || "\n";
    if (str.length > width) {
        var i = width;
        for (; i > 0 && this[i] != ' '; i--) {}
        if (i > 0) {
            var left = this.substring(0, i);
            var right = this.substring(i + 1);
            return left + newline + String.prototype.wrap.call(right, width, newline);
        }
    }
    return str;
};

/**
 * Centers a text with whitespace padding.
 * @param width The width to fill.
 * @param padding (Optional) The character to pad the blanks with.
 */
String.center = function (str, width, padding) {
    padding = padding || " "
    if (str.length < width) {
        return new Array(Math.floor((width - str.length) / 2)).join(padding) + str + new Array(Math.ceil((width - str.length) / 2)).join(padding);
    }
    return str + "";
};

/**
 * Converts a string into a list of arguments, respecting quoted text as one argument.
 * @param {String} str The string to split into arguments.
 * @returns {Array}
 * @example
 * String.args('a "b c" d');
 * // > ["a", "b c", "d"]
 */
String.args = function (str) {
    var regex = /(?:")([^"]+)(?:")|([^\s"]+)(?=\s+|$)/g,
        list = [],
        results;
    while (results = regex.exec(str)) {
        list.push(results[1] ? results[1] : results[0]);
    }
    return list;
};