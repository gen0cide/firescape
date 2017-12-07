package org.firescape.server.util;
/**
 * A class to handle loading configuration from XML
 */

import org.firescape.server.GameVars;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
  public static int SERVER_NUM = 72;
  public static String CONF_DIR, RSCD_HOME;
  // public static int SERVER_PORT, SERVER_VERSION, MAX_PLAYERS, LS_PORT,
  // SERVER_NUM;
  public static long START_TIME;

  static {
    loadEnv();
  }

  /**
   * Called to load config settings from the given file
   *
   * @param file the xml file to load settings from
   *
   * @throws IOException if an i/o error occurs
   */
  public static void initConfig(String file) throws IOException {
    START_TIME = System.currentTimeMillis();
    Properties props = new Properties();
    InputStream fis = Config.class.getResourceAsStream("/org/firescape/server/" + file);
    props.load(fis);
    GameVars.clientVersion = Integer.valueOf(props.getProperty("ClientVersion"));
    GameVars.portNumber = Integer.valueOf(props.getProperty("PortNumber"));
    GameVars.rangedDelaySpeed = Integer.valueOf(props.getProperty("ArrowDelaySpeed"));
    GameVars.serverLocation = props.getProperty("ServerLocation");
    GameVars.useFatigue = Integer.valueOf(props.getProperty("UseFatigue")) == 1;
    GameVars.maxUsers = Integer.valueOf(props.getProperty("MaxPlayers"));
    GameVars.saveAll = Integer.valueOf(props.getProperty("SaveAll"));
    GameVars.expMultiplier = Double.valueOf(props.getProperty("ExpMultiplier"));
    GameVars.serverName = props.getProperty("ServerName");
    CONF_DIR = "conf" + File.separator + "server";
    fis.close();
  }

  /**
   * Called to load RSCD_HOME and CONF_DIR Used to be situated in PersistenceManager
   */
  private static void loadEnv() {
    String home = System.getenv("RSCD_HOME");
    if (home == null) { // the env var hasnt been set, fall back to .
      home = ".";
    }
    CONF_DIR = "conf" + File.separator + "server";
    RSCD_HOME = home;
  }
}
