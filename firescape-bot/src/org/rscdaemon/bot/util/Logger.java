package org.rscdaemon.bot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

  public static void print(String func, Object o) {
    long ms = System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
    Date d = new Date(ms);
    System.out.println(sdf.format(d) + " " + func + " " + o.toString());
  }

  public static void error(Object o) {
    Logger.print("ERROR", o);
  }

  public static void net(Object o) {
    Logger.print("NET", o);
  }

  public static void debug(Object o) {
    Logger.print("DEBUG", o);
  }

  public static void entity(Object o) {
    Logger.print("ENTITY", o);
  }

  public static void info(Object o) {
    Logger.print("INFO", o);
  }
}
