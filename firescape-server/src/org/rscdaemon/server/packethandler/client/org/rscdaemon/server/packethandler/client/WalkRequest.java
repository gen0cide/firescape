package org.rscdaemon.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.rscdaemon.server.model.Mob;
import org.rscdaemon.server.model.Path;
import org.rscdaemon.server.model.Player;
import org.rscdaemon.server.model.World;
import org.rscdaemon.server.net.Packet;
import org.rscdaemon.server.net.RSCPacket;
import org.rscdaemon.server.packethandler.PacketHandler;
import org.rscdaemon.server.states.Action;
import org.rscdaemon.server.states.CombatState;

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
