package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.event.Thieving;
import org.firescape.server.model.Mob;
import org.firescape.server.model.Npc;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;

public class NpcCommand implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    int serverIndex = p.readShort();
    final Player player = (Player) session.getAttachment();
    if (player.isBusy()) {
      return;
    }
    final Mob affectedMob = world.getNpc(serverIndex);
    final Npc affectedNpc = (Npc) affectedMob;
    if (affectedNpc == null || affectedMob == null || player == null)
      return;
    affectedNpc.getID();
    Thieving thiev = new Thieving(player, affectedNpc, affectedMob);
    thiev.beginPickpocket();
    return;

  }

}