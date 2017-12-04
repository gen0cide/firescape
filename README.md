# FireScape

This is a [r]unescape classic private server. It is strictly for research and learning purposes and serves absolutely no commercial value.

# Building
 1. If you're on Windows, go home.
 2. Make sure Java 1.8 is installed on your system:
  2.1 Oracle Java works best on Ubuntu:
   2.2 Run `sudo add-apt-repository ppa:webupd8team/java`
   2.3 Run `sudo apt-get update`
   2.4 Run `sudo apt-get install oracle-java8-installer`

 
 3. Make sure `JAVA_HOME` exists in your path.
 
## Client
 1. Download and install Apache Maven
 2. CD into `./firescape/firescape-client/`
 2. Run `mvn clean && mvn package`
 3. Run `java -cp target/firescape-jar-with-dependencies.jar org.firescape.client.mudclient`
 4. Enjoy!
 
## Server
 1. You will need to download and start Redis-Server locally:
  1.1 On Ubuntu run: `sudo apt install redis-server && sudo service redis-server start`
 2. Download and install Apache Maven
 3. CD into `./firescape/firescape-server/`
 4. Run `mvn clean && mvn package`
 5. Run `java -cp target/firescape-jar-with-dependencies.jar org.firescape.server.Server`
 6. Enjoy!
 
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
