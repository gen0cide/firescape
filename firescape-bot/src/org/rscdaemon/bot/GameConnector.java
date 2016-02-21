package org.rscdaemon.bot;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.TreeMap;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.rscdaemon.bot.net.PacketQueue;
import org.rscdaemon.bot.net.RSCConnectionHandler;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.packethandler.PacketHandler;
import org.rscdaemon.bot.util.Config;
import org.rscdaemon.bot.util.Logger;

public class GameConnector {

  public PacketQueue<RSCPacket> incomingPacketQueue;

  public PacketQueue<RSCPacket> outgoingPacketQueue;

  private TreeMap<Integer, PacketHandler> packetHandlers = new TreeMap<Integer, PacketHandler>();

  public IoSession session;

  public boolean running = true;

  public boolean loggedIn = false;

  private IoHandler connectionHandler = new RSCConnectionHandler(this);

  public int connectionAttempts = 0;

  public GameConnector() {
    incomingPacketQueue = new PacketQueue<RSCPacket>();
    outgoingPacketQueue = new PacketQueue<RSCPacket>();
    loadPacketHandlers();
    reconnect();
  }

  public boolean reconnect() {
    // TODO Auto-generated method stub
    Logger.net("Attempting to connect to server");
    SocketConnector conn = new SocketConnector();
    SocketConnectorConfig config = new SocketConnectorConfig();
    ((SocketSessionConfig) config.getSessionConfig()).setKeepAlive(true);
    ((SocketSessionConfig) config.getSessionConfig()).setTcpNoDelay(true);
    ConnectFuture future = conn.connect(new InetSocketAddress(Config.SERVER_IP, Config.SERVER_PORT), connectionHandler,
        config);
    future.join(3000);
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
    return reconnect();
  }

  public void loadPacketHandlers() {
    // TODO Auto-generated method stub
  }

  public void loadPacketBuilders() {

  }

  public void processIncomingPackets() {
    List<RSCPacket> packets = incomingPacketQueue.getPackets();
    for (RSCPacket packet : packets) {
      PacketHandler handler;
      if (packetHandlers.containsKey(packet.getID())) {
        handler = packetHandlers.get(packet.getID());
        try {
          handler.handlePacket(packet, session);
        }
        catch (Exception e) {
          Logger.error("Exception with Incoming Packet! pid=" + packet.getID() + " message=" + e.getMessage());
        }
      } else {
        Logger.net("Unhandled packet from Server! id=" + packet.getID());
      }
    }
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
