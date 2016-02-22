package org.rscdaemon.bot.util;

public class Utils {

  public static void sleep(int i) {
    try {
      Thread.sleep(i);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
