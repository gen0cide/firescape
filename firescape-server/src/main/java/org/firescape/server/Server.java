package org.firescape.server;

import jnr.ffi.*;
import jnr.ffi.types.*;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.SingleEvent;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.RSCConnectionHandler;
import org.firescape.server.util.Config;
import org.firescape.server.util.DataConversions;
import org.firescape.server.util.Logger;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * The entry point for RSC server.
 */
public class Server {
  /**
   * World instance
   */
  private static final World world = World.getWorld();

  public interface MathLib {
    long Multiply(long x, long y);
  }

  /**
   * The game engine
   */
  private GameEngine engine;
  /**
   * The login server connection
   */
  /**
   * The SocketAcceptor
   */
  private IoAcceptor acceptor;
  /**
   * Update event - if the server is shutting down
   */
  private DelayedEvent updateEvent, pvpEvent, duelingEvent;
  /**
   * Is the server running still?
   */
  private boolean running;

  /**
   * Creates a new server instance, which in turn creates a new engine and
   * prepares the server socket to accept connections.
   */
  public Server() {
    String configFile = "FireScape.cfg";
    try {
      Config.initConfig(configFile);
    } catch (Exception e) {
      Logger.print(e, 1);
    }
    resetVars();
    try {
      Logger.print("FireScape Starting Up.", 3);
      GameVars.serverRunning = true;
    } catch (Exception r) {
      Logger.print(r.toString(), 1);
    }
    resetOnline();
    running = true;
    world.setServer(this);
    try {
      engine = new GameEngine();
      engine.start();
      acceptor = new SocketAcceptor();
      IoAcceptorConfig config = new SocketAcceptorConfig();
      config.setDisconnectOnUnbind(true);
      ((SocketSessionConfig) config.getSessionConfig()).setReuseAddress(true);
      acceptor.bind(new InetSocketAddress("0.0.0.0", GameVars.portNumber), new RSCConnectionHandler(engine), config);
    } catch (Exception e) {
      Logger.error(e);
    }
  }

  public static void resetVars() {
    GameVars.modsOnline = 0;
    GameVars.adminsOnline = 0;
    GameVars.userPeak = 0;
    GameVars.usersOnline = 0;
    GameVars.serverRunning = false;
  }

  public static boolean isOnline(String player) {
    Player p = world.getPlayer(DataConversions.usernameToHash(player));
    return p != null;
  }

  public static String readValue(String user, String key) {
    try {
      // System.out.println("Test 4");
      String username = user.replaceAll(" ", "_");
      File f = new File("players/" + username.toLowerCase() + ".cfg");
      Properties pr = new Properties();

      FileInputStream fis = new FileInputStream(f);
      pr.load(fis);

      String ret = pr.getProperty(key);
      fis.close();
      return ret;
    } catch (Exception e) {
      Logger.print(e, 1);
    }
    return null;
  }

  public static void writeValue(String user, String key, String value) {
    String username = user.replaceAll(" ", "_").toLowerCase();
    String redis_key = "players_" + username.toLowerCase();
    try (Jedis jedis = world.redis.getResource()) {
      if (jedis.exists(redis_key)) {
        ByteArrayInputStream ios = new ByteArrayInputStream(jedis.get(redis_key).getBytes(StandardCharsets.UTF_8));
        Logger.print("Loaded players_" + username.toLowerCase() + " from redis.", 3);
        Properties pr = new Properties();
        pr.load(ios);
        ios.close();
        pr.setProperty(key, value);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        pr.store(bos, "Redis backed character data");
        jedis.set(redis_key, bos.toString());
      }
    } catch (Exception e) {
      Logger.print(e, 1);
    }
  }

  public static void launchServer() {
    try {
      new Server();
    } catch (Exception r) {
      Logger.print(r.toString(), 1);
    }
  }

  public static void main(String[] args) {
    MathLib ml = LibraryLoader.create(MathLib.class).load("math");
    System.out.println(ml.Multiply(12345, 67890));
    try {

      // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      // UIManager.setLookAndFeel("com.easynth.lookandfeel.EaSynthLookAndFeel");
      launchServer();
    } catch (Exception e) {

    }
    // GUI.args = args;
    // new Server();

  }

  public boolean running() {
    return running;
  }

  /**
   * Shutdown the server in 60 seconds
   */
  public boolean shutdownForUpdate() {
    if (updateEvent != null) {
      return false;
    }
    updateEvent = new SingleEvent(null, 65000) {
      public void action() {
        kill();
      }
    };
    world.getDelayedEventHandler().add(updateEvent);
    return true;
  }

  /**
   * PvP Arena
   **/
  public boolean pvpTimerStart(int time) {
    if (pvpEvent != null) {
      return false;
    }
    pvpEvent = new SingleEvent(null, time * 1000) {
      public void action() {
        for (Player p : world.getPlayers()) {
          p.getActionSender().sendMessage("The PvP tournament has started!");
          if (world.getPvpEntry(p) && p.getLocation().inWaitingRoom()) {
            p.teleport(228, 130, false);
          }
        }
        duelingEvent();
      }
    };
    world.getDelayedEventHandler().add(pvpEvent);
    return true;
  }

  public boolean duelingEvent() {
    if (duelingEvent != null) {
      return false;
    }
    stopPvp();
    duelingEvent = new SingleEvent(null, 666666666) {
      public void action() {
        System.out.println("Shouldn't have reached here...Duel arena hackers.");
      }
    };
    world.getDelayedEventHandler().add(duelingEvent);
    return true;
  }

  public void stopPvp() {
    if (pvpEvent != null) {
      pvpEvent.stop();
      pvpEvent = null;
    }
  }

  public boolean pvpIsRunning() {
    if (duelingEvent != null) {
      return duelingEvent.isRunning();
    } else {
      return false;
    }
  }

  public boolean waitingIsRunning() {
    if (pvpEvent != null) {
      return pvpEvent.isRunning();
    } else {
      return false;
    }
  }

  public void stopDuel() {
    if (duelingEvent != null) {
      duelingEvent.stop();
      duelingEvent = null;
    }
    for (Player p : world.getPlayers()) {
      p.getActionSender().sendMessage("The winner of the PvP tournament was: @red@" + world.getWinner().getUsername());
      p.getActionSender().sendMessage("He won @gre@" + world.getJackPot() + "GP");
    }
  }

  /**
   * MS till the server shuts down
   */
  public int timeTillShutdown() {
    if (updateEvent == null) {
      return -1;
    }
    return updateEvent.timeTillNextRun();
  }

  public int timeTillPvp() {
    if (pvpEvent == null) {
      return -1;
    }
    return pvpEvent.timeTillNextRun();
  }

  public int timeTillDuel() {
    if (duelingEvent == null) {
      return -1;
    }
    return duelingEvent.timeTillNextRun();
  }

  public void resetOnline() {
    try {
      File files = new File("players/");
      int count = 0;
      for (File f : files.listFiles()) {

        if (f.getName().endsWith(".cfg")) {
          count++;
          Properties pr = new Properties();

          FileInputStream fis = new FileInputStream(f);
          pr.load(fis);
          fis.close();
          pr.setProperty("loggedin", "false");
          FileOutputStream fos = new FileOutputStream(f);
          pr.store(fos, "Character Data.");
          fos.close();
        }

      }
      Logger.print(count + " Accounts exist.", 3);
    } catch (Exception e) {
      Logger.print(e.toString(), 1);
    }
  }

  /**
   * Returns the game engine for this server
   */
  public GameEngine getEngine() {
    return engine;
  }

  public boolean isInitialized() {
    return engine != null;
  }

  /**
   * Kills the game engine and irc engine
   */
  public void kill() {
    // GUI.resetVars();
    Logger.print("CleanRSC Shutting Down...", 3);
    running = false;
    engine.emptyWorld();
  }

  /**
   * Unbinds the socket acceptor
   */
  public void unbind() {
    try {
      acceptor.unbindAll();
      // GUI.cout("Socket Closed", 3);
    } catch (Exception e) {
    }
  }
}
