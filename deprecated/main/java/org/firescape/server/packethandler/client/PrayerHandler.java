package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.entityhandling.EntityHandler;
import org.firescape.server.entityhandling.defs.PrayerDef;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.opcode.Command;
import org.firescape.server.opcode.Opcode;
import org.firescape.server.packethandler.PacketHandler;

public class PrayerHandler implements PacketHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  // loginConnector
  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    int pID = ((RSCPacket) p).getID();
    int prayerID = (int) p.readByte();
    if (prayerID < 0 || prayerID >= 14) {
      player.setSuspiciousPlayer(true);
      player.getActionSender().sendPrayers();
      return;
    }
    if (player.isDueling() && player.getDuelSetting(2)) {
      player.getActionSender().sendMessage("Prayer is disabled in this duel");
      player.getActionSender().sendPrayers();
      return;
    }
    PrayerDef prayer = EntityHandler.getPrayerDef(prayerID);
    if (pID == Opcode.getClient(204, Command.Client.CL_PRAYER_ON)) {
      if (player.getMaxStat(5) < prayer.getReqLevel()) {
        player.setSuspiciousPlayer(true);
        player.getActionSender().sendMessage("Your prayer ability is not high enough to use this prayer");
        player.getActionSender().sendPrayers();
        return;
      }
      if (player.getCurStat(5) <= 0) {
        player.setPrayer(prayerID, false);
        player.getActionSender().sendMessage("You have run out of prayer points. Return to a church to recharge");
        player.getActionSender().sendPrayers();
        return;
      }
      activatePrayer(player, prayerID);
    } else if (pID == Opcode.getClient(204, Command.Client.CL_PRAYER_OFF)) {
      deactivatePrayer(player, prayerID);
    }
    player.getActionSender().sendPrayers();
  }

  private boolean activatePrayer(Player player, int prayerID) {
    if (!player.isPrayerActivated(prayerID)) {
      if (prayerID == 11) {
        deactivatePrayer(player, 5);
        deactivatePrayer(player, 2);
      } else if (prayerID == 5) {
        deactivatePrayer(player, 2);
        deactivatePrayer(player, 11);
      } else if (prayerID == 2) {
        deactivatePrayer(player, 5);
        deactivatePrayer(player, 11);
      } else if (prayerID == 10) {
        deactivatePrayer(player, 4);
        deactivatePrayer(player, 1);
      } else if (prayerID == 4) {
        deactivatePrayer(player, 10);
        deactivatePrayer(player, 1);
      } else if (prayerID == 1) {
        deactivatePrayer(player, 10);
        deactivatePrayer(player, 4);
      } else if (prayerID == 9) {
        deactivatePrayer(player, 3);
        deactivatePrayer(player, 0);
      } else if (prayerID == 3) {
        deactivatePrayer(player, 9);
        deactivatePrayer(player, 0);
      } else if (prayerID == 0) {
        deactivatePrayer(player, 9);
        deactivatePrayer(player, 3);
      }
      player.addPrayerDrain(prayerID);
      player.setPrayer(prayerID, true);
      return true;
    }
    return false;
  }

  private boolean deactivatePrayer(Player player, int prayerID) {
    if (player.isPrayerActivated(prayerID)) {
      player.removePrayerDrain(prayerID);
      player.setPrayer(prayerID, false);
      return true;
    }
    return false;
  }

}