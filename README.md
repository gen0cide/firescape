# FireScape

This is a [r]unescape classic private server. It is strictly for research and learning purposes and serves absolutely no commercial value.

# Building
 1. If you're on Windows, go home.
 2. Make sure Java 1.8 is installed on your system.
 3. Make sure `JAVA_HOME` exists in your path.
 
## Client
 1. Only the first time, `chmod +x ant/bin/*` to correctly permission Apache Ant if it's not.
 2. `ant/bin/ant compile` from the base directory will build the client.
 3. `ant/bin/ant runclient` will then run the client.
 
## Server
 1. Same Ant permissions as above need to be checked (executable).
 2. `ant/bin/ant compile` to build.
 3. `ant/bin/ant runserver` to run.
 
# Contributing
Each directory (-client and -server) are Eclipse projects and can be imported as such. If you want to contribute, you **must** use the `firescape.xml` Code Style Profile and have Eclipse auto format on save.

# TODO:

 * Move all player information into PostgreSQL or Redis.
 * Move Server administration from Applet to headless w/ interface (pub/sub?)
 * Move entity management from XML/GZip to something easier to edit/modify.
 * Create an entity-sync system between client and server.
 * Implement a scripting engine into the client
 * Make a headless version of the client (hopefully w/ scripting!)
 * Profile client and server for memory leaks / bottlenecks
 * optimize, optimize, optimize!
   
# Credits

[RuneScape © 1999–2014, Jagex Ltd.](https://www.runescape.com)

A list of folks that have inspired/taught me a lot over the years (inb4 shoutout):
 * reines
 * petz
 * tsunami
 * cy4n
 * ephemeral
 * trunks
 * zer0
 * eXemplar
 * xEnt
 * dubaholic
 * DuelShark
 * p0t
