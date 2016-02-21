package org.rscdaemon.bot;

import org.rscdaemon.bot.util.Config;
import org.rscdaemon.bot.util.Logger;

public class Bot implements Runnable {

  public final static String DEFAULT_CONFIG_FILE = "settings.ini";
  public GameConnector gameConn;

  @Override
  public void run() {
    gameConn = new GameConnector();
    int testTick = 0;
    while (gameConn.isRunning()) {
      Logger.info("Bot Loop");
      try {
        Thread.sleep(500);
      }
      catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (testTick > 100) {
        Logger.info("Bot loop stubbed out. Exiting.");
        gameConn.running = false;
      }
    }
    gameConn.session.close();
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
