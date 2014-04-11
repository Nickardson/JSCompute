os = {
    "commands": [],
    "cwd": "/"
};

os.commands.push({
    "name": "cls",
    "help": "Clears the console.",
    "execute": function (args) {
        screen.scroll(-screen.height + 1);
    }
});

os.commands.push({
    "name": "cd",
    "help": "Changes the directory.",
    "execute": function (args) {
        os.cwd = fs.combine(os.cwd, args[0]);
    }
});

os.commands.push({
    "name": "shutdown",
    "help": "Turns the computer off.",
    "execute": function () {
        computer.off();
    }
});

os.commands.push({
    "name": "dir",
    "help": "Gives a list of files in the current directory.",
    "execute": function () {
        fs.dir(os.cwd).map(function(path) {
            if (fs.isFile(path)) {
                return path;
            } else {
                return path + "/";
            }
        }).forEach(print);
    }
});

os.commands.push({
    "name": "js",
    "help": "Starts a JavaScript console.",
    "execute": function () {
        print("Run exit() to stop.");

        var running = true;
        while (running) {
            function exit() {
                running = false;
            }

            try {
                var result = eval(prompt("JS"));
                if (result) {
                    print(result);
                }
            } catch (e) {
                print("Error: " + e);
            }
        }
    }
});

os.commands.push({
    "name": "help",
    "args": ["[command-name]"],
    "help": "Displays this help message.",
    "execute": function (args) {
        // Display all commands
        if (args.length == 0) {
            os.commands.map(function (cmd) {
                return cmd.name;
            }).forEach(print);
        } else {
            // Get information on the given command.
            var cmd = getCommand(args[0]);

            if (cmd) {
                // Print the name and arguments, if any.
                print(cmd.name + ": " + (cmd.args || []).join(" "));
                // Print the help message, if any.
                if (cmd.help) {
                    print(" - " + cmd.help);
                }
            }
        }
    }
});

/**
 * Gets the OS command with the given name.
 * @param name The name of the command.
 * @returns {*}
 */
function getCommand(name) {
    for (var i = 0; i < os.commands.length; i++) {
        var c = os.commands[i];
        if (c.name.toLowerCase() == name.toLowerCase()) {
            return c;
        }
    }
}

/**
 * Gets the text that should be displayed in the prompt area.
 * @param dir The directory.
 */
function getDirectoryText (dir) {
    if ((dir.indexOf("/") == 0) || (dir.indexOf(".") == 0)) {
        return dir.substr(1);
    }
    return dir;
}

print("Booted up.");

//noinspection InfiniteLoopJS
while (true) {
    var input = prompt(getDirectoryText(os.cwd)),
        args = input.split(" ");

    // Try to get the command.
    var c = getCommand(args[0]);
    if (c) {
        try {
            c.execute(args.slice(1));
        } catch (ex) {
            print("OS Error: " + ex);
        }
    } else {
        // Try to run the file.
        if (fs.exists(args[0])) {
            (function () {
                include(fs.combine(os.cwd, args[0]));
            })();
        }
    }
}