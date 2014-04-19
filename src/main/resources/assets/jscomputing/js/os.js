os = {
    "commands": [],
    "cwd": "/",
    "path": [
        "bin"
    ]
};

os.commands.push({
    "name": "cd",
    "help": "Changes the directory.",
    "execute": function (args) {
        var fullPath = fs.combine(os.cwd, args[0]);
        if (fs.exists(fullPath)) {
            if (fs.isDirectory(fullPath)) {
                os.cwd = fullPath;
            } else {
                print("Path is not a directory.");
            }
        } else {
            print("Cannot find the specified path");
        }
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
    "execute": function (args) {
        var fullPath = os.cwd;
        if (args.length > 0) {
            fullPath = fs.combine(os.cwd, args[0]);
        }

        fs.dir(fullPath).map(function(path) {
            var filePath = fs.combine(fullPath, path);
            if (fs.isFile(filePath)) {
                return path;
            } else if (fs.isDirectory(filePath)) {
                return path + "/";
            } else {
                return path + "?";
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
                if (result != undefined && typeof result != "function") {
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
            var cmd = os.getCommand(args[0]);

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
os.getCommand = function (name) {
    for (var i = 0; i < os.commands.length; i++) {
        var c = os.commands[i];
        if (c.name.toLowerCase() == name.toLowerCase()) {
            return c;
        }
    }
};

/**
 * Gets the text that should be displayed in the prompt area.
 * @param dir The directory.
 */
os.getDirectoryText = function (dir) {
    if ((dir.indexOf("/") == 0) || (dir.indexOf(".") == 0)) {
        return dir.substr(1);
    }
    return dir;
};

print("Booted up.");

//noinspection InfiniteLoopJS
while (true) {
    var _input = prompt(os.getDirectoryText(os.cwd)),
        _args = _input.split(" ");

    // Try to get the command.
    var _command = os.getCommand(_args[0]);
    if (_command) {
        try {
            _command.execute(_args.slice(1));
        } catch (ex) {
            print("OS Error: " + ex);
        }
    } else {
        function runWithPath(path) {
            var fullPath = fs.combine(path, _args[0]);
            if (fs.exists(fullPath) && fs.isFile(fullPath)) {
                run(fullPath, _args.slice(1));
                return true;
            }
            return false;
        }

        // Try to run the file, first looking on the path.
        var ran = false;
        for (var i = 0; i < os.path.length; i++) {
            if (runWithPath(os.path[i])) {
                ran = true;
                break;
            }
        }
        if (!ran) {
            runWithPath(os.cwd);
        }
    }
}