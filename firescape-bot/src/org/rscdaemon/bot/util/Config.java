package org.rscdaemon.bot.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
  /**
   * Called to load config settings from the given file
   */
  public static void initConfig(String file) throws IOException {
    START_TIME = System.currentTimeMillis();

    Properties props = new Properties();
    props.load(new FileInputStream(file));

    SERVER_IP = props.getProperty("server");
    SERVER_PORT = Integer.parseInt(props.getProperty("port"));
    CONF_DIR = props.getProperty("config_dir");

    props.clear();
  }

  public static String SERVER_IP, CONF_DIR;
  public static int SERVER_PORT;
  public static long START_TIME;
}