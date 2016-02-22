package org.rscdaemon.bot;

import java.util.List;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.util.Config;
import org.rscdaemon.bot.util.Logger;

public class Bot implements Runnable {

  public final static String DEFAULT_CONFIG_FILE = "settings.ini";
  public GameConnector gameConn;

  @Override
  public void run() {
    gameConn = new GameConnector();
    int testTick = 0;
    Player p = gameConn.player;
    System.out.println("Session: " + gameConn.session.toString());
    Logger.net("Setting up session");
    Logger.net("P = " + p.toString());
    p.setupSession();
    while (p.sessionID == 0) {
      gameConn.processIncomingPackets();
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Session is good, now to login");
    p.login();
    while (!p.isLoggedIn) {
      gameConn.processIncomingPackets();
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    while (gameConn.isRunning()) {
      List<RSCPacket> pkts = gameConn.incomingPacketQueue.getPackets();
      for (RSCPacket pkt : pkts) {
        Logger.net("Packet: " + pkt.toString());
      }
      Logger.info("Bot Loop");
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
      try {
        Thread.sleep(1500);
        testTick++;
      }
      catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (testTick > 5) {
        p.sendChatMessage("Logging out, peace.");
        Logger.info("Bot loop stubbed out. Exiting.");
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

  public static void main(String[] args) {
    System.out.println("Started Bot");
    Bot b = null;
    try {
      if (args.length > 0) {
        b = new Bot(args[0]);
      } else {
        b = new Bot(DEFAULT_CONFIG_FILE);
      }
      Thread bot = new Thread(b);
      bot.start();
      bot.join();
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    }
  }
}
