package org.firescape.server;

import org.apache.mina.common.IoSession;
import org.firescape.server.entityhandling.EntityHandler;
import org.firescape.server.entityhandling.defs.extras.AdvertDef;
import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.SaveEvent;
import org.firescape.server.model.Player;
import org.firescape.server.model.Shop;
import org.firescape.server.model.World;
import org.firescape.server.net.PacketQueue;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.packethandler.PacketHandler;
import org.firescape.server.packethandler.PacketHandlerDef;
import org.firescape.server.util.Logger;
import org.firescape.server.util.PersistenceManager;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * The central motor of the game. This class is responsible for the primary
 * operation of the entire game.
 */
public final class GameEngine extends Thread {
  /**
   * World instance
   */
  private static final World world = World.getWorld();
  private static boolean running = true;
  /**
   * The packet queue to be processed
   */
  private final PacketQueue<RSCPacket> packetQueue;
  /**
   * The mapping of packet IDs to their handler
   */
  private final TreeMap<Integer, PacketHandler> packetHandlers = new TreeMap<Integer, PacketHandler>();
  /**
   * Responsible for updating all connected clients
   */
  private final ClientUpdater clientUpdater = new ClientUpdater();
  /**
   * Handles delayed events rather than events to be ran every iteration
   */
  private final DelayedEventHandler eventHandler = new DelayedEventHandler();
  /**
   * Whether the engine's thread is running
   */
  int curAdvert;
  int tickIdx;
  int[] tickTimings = new int[20];
  long lastTickTime;
  long currTickTime;
  private long lastAdvert;
  /**
   * When the update loop was last ran, required for throttle
   */
  private long lastSentClientUpdate;

  /**
   * Constructs a new game engine with an empty packet queue.
   */
  public GameEngine() {
    curAdvert = 0;
    lastAdvert = 0;
    packetQueue = new PacketQueue<RSCPacket>();

    loadPacketHandlers();
    for (Shop shop : world.getShops()) {
      shop.initRestock();
    }
    lastAdvert = System.currentTimeMillis();
  }

  /**
   * Loads the packet handling classes from the persistence manager.
   */
  protected void loadPacketHandlers() {
    PacketHandlerDef[] handlerDefs = (PacketHandlerDef[]) PersistenceManager.load("PacketHandlers.xml");
    int count = 0;
    for (PacketHandlerDef handlerDef : handlerDefs) {
      try {
        String className = handlerDef.getClassName();
        Class<?> c = Class.forName(className);
        if (c != null) {
          count++;

          PacketHandler handler = (PacketHandler) c.newInstance();
          for (int packetID : handlerDef.getAssociatedPackets()) {

            packetHandlers.put(packetID, handler);
          }

        }
      } catch (Exception e) {
        Logger.error(e);
      }
    }
    Logger.print(count + " Packet Handlers Loaded.", 3);
  }

  public static void kill() {
    Logger.print("Terminating GameEngine", 1);
    GameVars.serverRunning = false;
    running = false;

  }

  /**
   * The thread execution process.
   */
  public void run() {
    Logger.print("GameEngine now running", 3);
    int curAdvert = -1;
    Logger.print(GameVars.serverName + " is now Online!", 3);
    GameVars.serverRunning = true;
    running = true;

    eventHandler.add(new DelayedEvent(null, GameVars.saveAll * 60000) {
      public void run() {
        SaveEvent.saveAll();
      }
    });
    while (running) {
      long curTime = System.currentTimeMillis();
      if (curTime - lastAdvert >= 60000L) {
        lastAdvert = curTime;
        if (++curAdvert >= EntityHandler.getAdverts().length || curAdvert < 0) {
          curAdvert = 0;
        }
        AdvertDef advertDef = EntityHandler.getAdverts()[curAdvert];
        String advert = advertDef.getMessage();
        Player p;
        for (Iterator i$ = world.getPlayers().iterator(); i$.hasNext(); p.getActionSender().sendMessage((new
          StringBuilder()).append("@cya@[Server Message] @whi@").append(processAdvert(advert, p)).toString())) {
          p = (Player) i$.next();
        }

      }
      try {
        Thread.sleep(50);
      } catch (InterruptedException ie) {
      }
      processIncomingPackets();
      processEvents();
      processClients();
    }
    if (!running) {
      world.getServer().unbind();
    }
  }

  private static String processAdvert( String advert, Player p ) {
    advert = advert.replaceAll("%name", p.getUsername());
    advert = advert.replaceAll("%online", String.valueOf(world.getPlayers().size()));
    return advert;
  }

  /**
   * Processes incoming packets.
   */
  private void processIncomingPackets() {
    for (RSCPacket p : packetQueue.getPackets()) {
      IoSession session = p.getSession();
      Player player = (Player) session.getAttachment();
      player.ping();
      PacketHandler handler = packetHandlers.get(p.getID());
      if (handler != null) {
        try {
          handler.handlePacket(p, session);
        } catch (Exception e) {
          Logger.error("Exception with p[" + p.getID() + "] from " + player.getUsername() + " [" + player
            .getCurrentIP() + "]: " + e.getMessage());
          player.getActionSender().sendLogout();
          player.destroy(false);
        }
      } else {
        Logger.error("Unhandled packet from " + player.getCurrentIP() + ": " + p.getID());
      }
    }
  }

  private void processEvents() {
    eventHandler.doEvents();
  }

  private void processClients() {
    clientUpdater.sendQueuedPackets();

    long now = System.currentTimeMillis();
    if (now - lastSentClientUpdate >= 600) {
      lastSentClientUpdate = now;
      clientUpdater.updateClients();
    }
  }

  public void emptyWorld() {
    for (Player p : world.getPlayers()) {
      p.save();
      p.getActionSender().sendLogout();
    }
    // world.getServer().getLoginConnector().getActionSender().saveProfiles();
  }

  /**
   * Returns the current packet queue.
   *
   * @return A <code>PacketQueue</code>
   */
  public PacketQueue<RSCPacket> getPacketQueue() {
    return packetQueue;
  }

}
