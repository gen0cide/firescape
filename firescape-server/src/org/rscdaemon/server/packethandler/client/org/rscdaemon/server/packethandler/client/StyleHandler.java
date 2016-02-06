package org.rscdaemon.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.rscdaemon.server.model.Player;
import org.rscdaemon.server.model.World;
import org.rscdaemon.server.net.Packet;
import org.rscdaemon.server.packethandler.PacketHandler;

public class StyleHandler implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    int style = p.readByte();
    if (style < 0 || style > 3) {
      player.setSuspiciousPlayer(true);
      return;
    }
    player.setCombatStyle(style);
  }

}