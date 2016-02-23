package org.rscdaemon.bot.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Config {
  /**
   * Called to load config settings from the given file
   */
  public static void initConfig(String file) throws IOException {
    START_TIME = System.currentTimeMillis();

    VERSION = 3;

    Properties props = new Properties();
    props.load(new FileInputStream(file));

    Map<String, String> env = System.getenv();

    // override config file if ENV variables are set
    USERNAME = env.containsKey("FIRE_USER") ? env.get("FIRE_USER") : props.getProperty("username");
    PASSWORD = env.containsKey("FIRE_PASS") ? env.get("FIRE_PASS") : props.getProperty("password");
    SERVER_IP = env.containsKey("FIRE_SERVER") ? env.get("FIRE_SERVER") : props.getProperty("server");
    SERVER_PORT = env.containsKey("FIRE_PORT") ? Integer.parseInt(env.get("FIRE_PORT"))
        : Integer.parseInt(props.getProperty("port"));
    CONF_DIR = env.containsKey("FIRE_CONF_DIR") ? env.get("FIRE_CONF_DIR") : props.getProperty("config_dir");

    props.clear();
  }

  public static String SERVER_IP, CONF_DIR, USERNAME, PASSWORD;
  public static int SERVER_PORT, VERSION;
  public static long START_TIME;
}