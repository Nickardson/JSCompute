os = {
    "cwd": "/",
    "path": ["bin"]
};

print("Booted up.");

//noinspection InfiniteLoopJS
while (true) {
    var _input = prompt(os.cwd),
        _args = String.args(_input);

    var path = os.path.concat(os.cwd);
    for (var i = 0; i < path.length; i++) {
        var fullPath = fs.combine(path[i], _args[0]);

        if (fs.isFile(fullPath)) {
            run(fullPath, _args.slice(1));
            break;
        }
    }
}