package org.rscdaemon.bot;

import java.util.List;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.util.Config;
import org.rscdaemon.bot.util.Logger;
import org.rscdaemon.bot.util.Utils;

public class Bot implements Runnable {

  public final static String DEFAULT_CONFIG_FILE = "settings.ini";
  public GameConnector gameConn;

  @Override
  public void run() {
    gameConn = new GameConnector();
    int testTick = 0;
    Player p = gameConn.player;
    p.setupSession();
    while (p.sessionID == 0) {
      gameConn.processIncomingPackets();
      Utils.sleep(100);
    }
    p.login();
    while (!p.isLoggedIn) {
      gameConn.processIncomingPackets();
      Utils.sleep(100);
    }
    Utils.sleep(500);
    while (gameConn.isRunning()) {
      List<RSCPacket> pkts = gameConn.incomingPacketQueue.getPackets();
      switch (testTick) {
      case 0:
        p.sendChatMessage("whats up guys");
        break;
      case 1:
        p.sendChatMessage("Looking forward to some epic PKing.");
        break;
      case 2:
        p.sendChatMessage("@cya@press 666 to sell shark certs");
        break;
      case 3:
        p.sendChatMessage("thnx for the trade m8");
        break;
      case 4:
        p.sendChatMessage("Moms calling, got to go eat dinner. bbiab.");
        break;
      }
      Utils.sleep(1500);
      testTick++;
      if (testTick > 5) {
        p.sendChatMessage("Logging out, peace.");
        gameConn.running = false;
      }
    }
    gameConn.session.close(true);
    Logger.info("Closed network session.");
    System.exit(0);
  }

  public Bot(String configFile) throws Exception {
    Config.initConfig(configFile);
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Started Bot");
    Bot bot = null;
    if (args.length > 0) {
      bot = new Bot(args[0]);
    } else {
      bot = new Bot(DEFAULT_CONFIG_FILE);
    }
    bot.run();
  }
}
