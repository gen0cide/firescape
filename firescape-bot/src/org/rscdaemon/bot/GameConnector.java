package org.rscdaemon.bot;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.TreeMap;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.PacketQueue;
import org.rscdaemon.bot.net.RSCConnectionHandler;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.packethandler.PacketHandler;
import org.rscdaemon.bot.packethandler.PacketHandlerDef;
import org.rscdaemon.bot.util.Config;
import org.rscdaemon.bot.util.Logger;
import org.rscdaemon.bot.util.PersistenceManager;

public class GameConnector {

  public PacketQueue<RSCPacket> incomingPacketQueue;

  public PacketQueue<RSCPacket> outgoingPacketQueue;

  private TreeMap<Integer, PacketHandler> packetHandlers = new TreeMap<Integer, PacketHandler>();

  public IoSession session;

  public boolean running = true;

  public boolean loggedIn = false;

  private IoHandler connectionHandler = new RSCConnectionHandler(this);

  public int connectionAttempts = 0;

  public Player player;

  public GameConnector() {
    player = new Player();
    incomingPacketQueue = new PacketQueue<RSCPacket>();
    outgoingPacketQueue = new PacketQueue<RSCPacket>();
    loadPacketHandlers();
    connect();
    player.setIoSession(session);
  }

  public boolean connect() {
    Logger.net("Attempting to connect to server");
    NioSocketConnector conn = new NioSocketConnector(1);
    conn.getSessionConfig().setKeepAlive(true);
    conn.getSessionConfig().setTcpNoDelay(true);
    conn.setHandler(connectionHandler);
    ConnectFuture future = conn.connect(new InetSocketAddress(Config.SERVER_IP, Config.SERVER_PORT));
    future.awaitUninterruptibly();
    if (future.isConnected()) {
      session = future.getSession();
      Logger.net("Connected to server: " + Config.SERVER_IP + ":" + Config.SERVER_PORT);
      connectionAttempts = 0;
      return true;
    }
    if (connectionAttempts++ >= 100) {
      Logger.net("Unable to connect. Giving up after " + connectionAttempts + " tries.");
      System.exit(1);
      return false;
    }
    return connect();
  }

  public void loadPacketHandlers() {
    PacketHandlerDef[] handlerDefs = (PacketHandlerDef[]) PersistenceManager
        .load("defs" + File.separator + "PacketHandlers.xml");
    for (PacketHandlerDef handlerDef : handlerDefs) {
      try {
        String className = handlerDef.getClassName();
        Class c = Class.forName(className);
        if (c != null) {
          PacketHandler handler = (PacketHandler) c.newInstance();
          for (int packetID : handlerDef.getAssociatedPackets()) {
            Logger.info("Loaded " + className + " Packet Handler for pid = " + packetID);
            packetHandlers.put(packetID, handler);
          }
        }
      }
      catch (Exception e) {
        Logger.error(e);
      }
    }
  }

  public void loadPacketBuilders() {

  }

  public void processIncomingPackets() {
    if (incomingPacketQueue.hasPackets()) {
      List<RSCPacket> packets = incomingPacketQueue.getPackets();
      for (RSCPacket packet : packets) {
        // Logger.net("Processing Packet! " + packet.toString());
        PacketHandler handler;
        if (packetHandlers.containsKey(packet.getID())) {
          handler = packetHandlers.get(packet.getID());
          try {
            handler.handlePacket(packet, session);
          }
          catch (Exception e) {
            // Logger.error("Exception with Incoming Packet! pid=" +
            // packet.getID() + " message=" + e.getMessage());
          }
        } else {
          // Logger.net("Unhandled packet from Server! id=" + packet.getID());
        }
      }
    }
  }

  public void sendPacket(RSCPacket p) {
    session.write(p);
  }

  public void sendQueuedPackets() {
    List<RSCPacket> packets = outgoingPacketQueue.getPackets();
    for (RSCPacket packet : packets) {
      session.write(packet);
    }
  }

  public boolean isRunning() {
    // TODO Auto-generated method stub
    return running;
  }
}
