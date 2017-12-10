package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;

public class PlayerAppearanceIDHandler implements PacketHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    int mobCount = p.readShort();
    int[] indicies = new int[mobCount];
    int[] appearanceIDs = new int[mobCount];
    for (int x = 0; x < mobCount; x++) {
      indicies[x] = p.readShort();
      appearanceIDs[x] = p.readShort();
    }
    Player player = (Player) session.getAttachment();
    player.addPlayersAppearanceIDs(indicies, appearanceIDs);
  }

}
