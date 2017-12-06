package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;

public class FollowRequest implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    Player affectedPlayer = world.getPlayer(p.readShort());
    if (affectedPlayer == null) {
      player.setSuspiciousPlayer(true);
      return;
    }
    if (player.isBusy()) {
      player.resetPath();
      return;
    }
    player.resetAll();
    player.setFollowing(affectedPlayer, 1);
    player.getActionSender().sendMessage("Now following " + affectedPlayer.getUsername());
  }
}
