function draw() {
    if (mc.loggedIn == 0 && mc.the_username.length > 0 && mc.the_password.length > 0) {
        mc.login(mc.the_username, mc.the_password, false);
    }
}
