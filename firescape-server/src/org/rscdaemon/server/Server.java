package org.rscdaemon.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import javax.swing.UIManager;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.rscdaemon.server.event.DelayedEvent;
import org.rscdaemon.server.event.SingleEvent;
import org.rscdaemon.server.model.Player;
import org.rscdaemon.server.model.World;
import org.rscdaemon.server.net.RSCConnectionHandler;
import org.rscdaemon.server.util.Logger;

/**
 * The entry point for RSC server.
 */
public class Server {
  /**
   * World instance
   */
  private static final World world = World.getWorld();
  /**
   * The game engine
   */
  private GameEngine engine;
  /**
   * The SocketAcceptor
   */
  private IoAcceptor acceptor;
  /**
   * Update event - if the server is shutting down
   */
  private DelayedEvent updateEvent, pvpEvent, duelingEvent;
  /**
   * The login server connection
   */
  /**
   * Is the server running still?
   */
  private boolean running;

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
    }
    catch (Exception e) {
      Logger.print(e.toString(), 1);
    }
  }

  /**
   * Creates a new server instance, which in turn creates a new engine and
   * prepares the server socket to accept connections.
   */
  public Server() {
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
      acceptor.bind(new InetSocketAddress("localhost", GameVars.portNumber), new RSCConnectionHandler(engine), config);
    }
    catch (Exception e) {
      Logger.error(e);
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
    GUI.resetVars();
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
      GUI.cout("Socket Closed", 3);
    }
    catch (Exception e) {
    }
  }

  public static void main(String[] args) throws IOException {
    try {

      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      // UIManager.setLookAndFeel("com.easynth.lookandfeel.EaSynthLookAndFeel");
    }
    catch (Exception e) {

    }
    GUI.args = args;
    new GUI();

  }
}
