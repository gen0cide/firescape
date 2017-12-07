function handleCommand(command) {
    if (command.startsWith("test1")) {
        mc.showMessage(0, command.substring(5), null, null, 1, null, false);
        return true;
    }
    else if (command.startsWith("test2")) {
        mc.showMessage(0, command.substring(5), null, null, 2, null, false);
        return true;
    }
    else if (command.startsWith("test3")) {
        mc.showMessage(0, command.substring(5), null, null, 3, null, false);
        return true;
    }
    else if (command.startsWith("test4")) {
        mc.showMessage(0, command.substring(5), null, null, 4, null, false);
        return true;
    }
    else if (command.startsWith("test5")) {
        mc.showMessage(0, command.substring(5), "SenderMan", "SenderClan", 4, "@mag@", true);
        return true;
    }
    return false;
}
