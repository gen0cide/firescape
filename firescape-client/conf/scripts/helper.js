function handleCommand(command) {
    if (command.startsWith("r")) {
        if (command == "reload") {
            Manager.static.clear();
            Manager.static.load(mc.scriptDir);
        }
    }
}
