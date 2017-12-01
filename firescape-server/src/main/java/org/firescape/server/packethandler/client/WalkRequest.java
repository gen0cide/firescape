package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.model.Mob;
import org.firescape.server.model.Path;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.packethandler.PacketHandler;
import org.firescape.server.states.Action;
import org.firescape.server.states.CombatState;

public class WalkRequest implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    int pID = ((RSCPacket) p).getID();
    if (player.inCombat()) {
      if (pID == 132) {
        Mob opponent = player.getOpponent();
        if (opponent == null) { // This shouldn't happen
          player.setSuspiciousPlayer(true);
          return;
        }
        if (opponent.getHitsMade() >= 3) {
          if (player.isDueling() && player.getDuelSetting(0)) {
            player.getActionSender().sendMessage("Running has been disabled in this duel.");
            return;
          }
          player.resetCombat(CombatState.RUNNING);
          opponent.resetCombat(CombatState.WAITING);
        } else {
          player.getActionSender().sendMessage("You cannot retreat in the first 3 rounds of battle.");
          return;
        }
      } else {
        return;
      }
    } else if (player.isBusy()) {
      return;
    }
    player.resetAll();

    int startX = p.readShort();
    int startY = p.readShort();
    int numWaypoints = p.remaining() / 2;
    byte[] waypointXoffsets = new byte[numWaypoints];
    byte[] waypointYoffsets = new byte[numWaypoints];
    System.out.print("Walk Request: startX=" + startX + " startY=" + startY + " numWaypoints=" + numWaypoints + " ");
    for (int x = 0; x < numWaypoints; x++) {
      waypointXoffsets[x] = p.readByte();
      waypointYoffsets[x] = p.readByte();
      System.out.print("[" + x + "](" + waypointXoffsets[x] + "," + waypointYoffsets[x] + ") ");
    }
    System.out.print("\n");
    Path path = new Path(startX, startY, waypointXoffsets, waypointYoffsets);
    player.setStatus(Action.IDLE);
    player.setPath(path);
  }

}
