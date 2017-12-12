package org.firescape.server;

/**
 * @author Ent All Game variables that may be tweaked by the Control Panel will be stored here.
 */
public class GameVars {

  /**
   * FireScape's current version.
   */
  public static double projectVersion = 1.1;
  /**
   * if the server is running or not.
   */
  public static boolean serverRunning;
  /**
   * the Amount of minutes the server saves all profiles (Default) is 15 minutes.
   */
  public static int saveAll = 15;
  /**
   * the Server name, will be used when logging in, and anything else that needs to print the server's name.
   */
  public static String serverName = "FireScape Test Bed";
  /**
   * the Server's location it's being hosted at.
   */
  public static String serverLocation = "Australia";
  /**
   * the amount of Users Online.
   */
  public static int usersOnline;
  /**
   * the Moderators that are currently Online.
   */
  public static int modsOnline;
  /**
   * the Admin's that are currently online.
   */
  public static int adminsOnline;
  /**
   * the Maximum amount of users allowed on this server
   */
  public static int maxUsers = 200;
  /**
   * User Peak (Since last Shutdown) the most users that have been online.
   */
  public static int userPeak;
  /**
   * Experience multiplier - name says it all.
   */
  public static double expMultiplier = 1.0;
  /**
   * Arrow delay speed (Default
   */
  public static int rangedDelaySpeed = 2000;
  /**
   * the Client version code to verify the correct client connecting.
   */
  public static int clientVersion = 4;
  /**
   * the port number
   */
  public static int portNumber = 43594;
  /**
   * if fatigue is used or not.
   */
  public static boolean useFatigue = true;
}
