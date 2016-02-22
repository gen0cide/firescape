package org.rscdaemon.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.rscdaemon.server.model.Player;
import org.rscdaemon.server.model.World;
import org.rscdaemon.server.net.Packet;
import org.rscdaemon.server.packetbuilder.RSCPacketBuilder;
import org.rscdaemon.server.packethandler.PacketHandler;
import org.rscdaemon.server.util.Formulae;

public class SessionRequest implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    byte userByte = p.readByte();
    player.setClassName(p.readString().trim());
    System.out.println("Class Name: " + player.getClassName());
    long serverKey = Formulae.generateSessionKey(userByte);
    player.setServerKey(serverKey);
    RSCPacketBuilder pb = new RSCPacketBuilder();
    pb.setBare(true);
    pb.addLong(serverKey);
    session.write(pb.toPacket());
    System.out.println("Server Key: " + serverKey);
  }
}
