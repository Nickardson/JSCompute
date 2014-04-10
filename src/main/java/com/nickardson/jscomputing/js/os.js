os = {
    "commands": []
};

os.commands.push({
    "name": "dir",
    "help": "Gives a list of files in the current directory.",
    "execute": function (args, directory) {
        fs.dir(directory).forEach(print);
    }
});

os.commands.push({
    "name": "echo",
    "execute": function (args) {
        print(args.join(" "));
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
            eval(prompt("JS"));
        }
    }
});

os.commands.push({
    "name": "help",
    "args": ["[command-name]"],
    "help": "Displays this help message.",
    "execute": function (args) {
        if (args.length == 0) {
            os.commands.map(function (command) {
                return command.name;
            }).forEach(print);
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
        if (c.name == name) {
            return c;
        }
    }
}

/**
 * Gets the text that should be displayed in the prompt area.
 * @param dir The directory.
 */
function getDirectoryText (dir) {
    if (dir == ".") {
        return "";
    }
    return dir;
}

var current = ".";

print("Booted up.");

//noinspection InfiniteLoopJS
while (true) {
    var input = prompt(getDirectoryText(current)),
        split = input.split(" ");

    var c = getCommand(split[0]);
    if (c) {
        try {
            c.execute(split.slice(1), current);
        } catch (ex) {
            print("OS Error: " + ex);
        }
    }
}