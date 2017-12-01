package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.util.Logger;
import org.firescape.server.packethandler.PacketHandler;

public class Trap implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    Logger.event(player.getUsername() + " [" + player.getUsernameHash() + "] was caught by a trap!");
  }
}
