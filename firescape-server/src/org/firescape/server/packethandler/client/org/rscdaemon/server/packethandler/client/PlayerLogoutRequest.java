package org.firescape.server.packethandler.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.mina.common.IoSession;
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
      // String user = player.getUsername().replaceAll(" ", "_").toLowerCase();
      if (!player.bad_login) {

        File f = new File("players/" + player.getUsername().replaceAll(" ", "_").toLowerCase() + ".cfg");
        Logger.print("Player logging out: " + player.getUsername().replaceAll(" ", " "), 4);
        Properties pr = new Properties();

        FileInputStream fis = new FileInputStream(f);
        pr.load(fis);
        fis.close();

        FileOutputStream fos = new FileOutputStream(f);
        pr.setProperty("loggedin", "false");
        pr.store(fos, "Character Data.");
        fos.close();

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
