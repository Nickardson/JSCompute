delete Packages;
delete importClass;
delete importPackage;
delete java;
delete com;
delete net;

includeLibrary("Events.js");
includeLibrary("stringutils.js");

/**
 * Prints the given argument, with text wrapping.
 * @param arg The object to print out.
 */
var print = function (arg) {
    if (arg != undefined) {
        // Split up the wrapped text by newlines, then print each on a new line.
        var split = String.wrap((arg + ""), screen.width).split("\n");
        for (var i = 0; i < split.length; i++) {
            screen.print(split[i]);
        }
    } else {
        screen.print("");
    }
};

/**
 * Writes the given argument at the current cursor position.
 * @param arg The object to write out.
 */
var write = function (arg) {
    screen.write(arg);
};

/**
 * Reads input from the terminal, suspending all execution while running.
 * @param {String} [prefix] The prefix to the prompt.
 * @param {Function} [filter] A function who takes the display string as it's argument, and should return a modified string.
 * @returns {String} The input provided by the user.
 * @example
 * prompt();
 *
 * prompt("What's your name? ");
 *
 * // Regex to replace all occurrences (denoted by //g) of any character (denoted by .), with an asterisk.
 * prompt("Password:", function (s) {
 *     return s.replace(/./g, "*")
 * });
 */
var prompt = function (prefix, filter) {
    prefix = prefix || "";

    var result = "",
        x = screen.cursor.x,
        y = screen.cursor.y;
    screen.cursor.visible = true;
    while (true) {
        // Reset cursor to the start.
        screen.cursor.set(x, y);

        var displayed = prefix + "> " + result;

        // Apply a filter, if any exists.
        if (filter != null) {
            displayed = filter(displayed);
        }

        // Fit the text by chopping off anything too far to the left, and fill the blank area.
        displayed = displayed.substr(Math.max(0, displayed.length - screen.width));

        screen.clearLine();
        screen.cursor.x = 0;
        screen.write(displayed);

        // Wait for an event.
        var event = events.pull("key");
        // If the character is printable, add it to the input.
        if (screen.isPrintable(event.character)) {
            result += event.character;
        } else {
            if (event.key == "BACK" && result.length > 0) {
                // Backspace.
                result = result.substr(0, result.length - 1);
            }
        }

        // Enter breaks out of the input loop, and we can return our input.
        if (event.key == "RETURN") {
            screen.cursor.visible = false;
            print();
            return result;
        }
    }
};

var error = function (err) {
    print("ERR: " + err);
};

var include = function (filename) {
    var file = fs.read(filename);
    if (file) {
        var s = file.readAll();
        file.close();
        try {
            eval(String(s));
        } catch (e) {
            error(String(e));
        }
    } else {
        error("Couldn't read file!");
    }
};

//noinspection JSUnusedLocalSymbols
var run = function (filename, args) {
    if (filename) {
        var file = fs.read(filename);
        if (file) {
            var s = file.readAll();
            file.close();
            try {
                return eval(String(s));
            } catch (e) {
                if (typeof e == "number") {
                    if (e != 0) {
                        error("exited with code " + e);
                    }
                } else {
                    error(String(e));
                }
                return e;
            }
        }
    }
    error("Couldn't read file!");
};

events.pull = function (filter) {
    if (typeof filter == "string") {
        var name = filter;
        filter = function (event) {
            return event.name == name;
        }
    }

    var event;
    while (true) {
        event = pull();
        if (filter(event)) {
            return event;
        }
    }
};
