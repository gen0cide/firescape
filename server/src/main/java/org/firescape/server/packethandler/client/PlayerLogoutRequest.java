package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.Server;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;
import org.firescape.server.util.Logger;

public class PlayerLogoutRequest implements PacketHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  // has logged
  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    if (player.canLogout()) {
      if (!player.bad_login) {
        Logger.print("Player logging out: " + player.getUsername().replaceAll(" ", " "), 4);
        Server.writeValue(player.getUsername(), "loggedin", "false");
        for (Player pla : world.getPlayers()) {
          if (pla.isFriendsWith(player.getUsername())) {
            pla.getActionSender().sendFriendUpdate(player.getUsernameHash(), 0);
          }
        }
      }
      player.destroy(true);

    } else {
      player.getActionSender().sendCantLogout();
    }
  }
}
