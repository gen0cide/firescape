package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.DuelEvent;
import org.firescape.server.event.SingleEvent;
import org.firescape.server.event.WalkToMobEvent;
import org.firescape.server.model.InvItem;
import org.firescape.server.model.Inventory;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.opcode.Command;
import org.firescape.server.opcode.Opcode;
import org.firescape.server.packethandler.PacketHandler;
import org.firescape.server.states.Action;
import org.firescape.server.util.DataConversions;
import org.firescape.server.util.Formulae;

public class DuelHandler implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    int pID = ((RSCPacket) p).getID();
    Player affectedPlayer = player.getWishToDuel();
    if (player == affectedPlayer) {
      System.out.println("Warning : " + player.getUsername() + " tried to duel himself");
      unsetOptions(player);
      unsetOptions(affectedPlayer);
      return;
    }
    if (player.isDuelConfirmAccepted() && affectedPlayer != null && affectedPlayer.isDuelConfirmAccepted()) {
      // If we are actually dueling we shouldn't touch any settings (modify or
      // unset!)
      return;
    }
    if (busy(player) || player.getLocation().inWilderness()) {
      unsetOptions(player);
      unsetOptions(affectedPlayer);
      return;
    }
    if (player.getLocation().inModRoom()) {
      player.getActionSender().sendMessage("You cannot duel in here!");
      unsetOptions(player);
      unsetOptions(affectedPlayer);
      return;
    }
    if (pID == Opcode.getClient(204, Command.Client.CL_PLAYER_DUEL)) { // Sending duel request
      affectedPlayer = world.getPlayer(p.readShort());
      if (affectedPlayer == null ||
          affectedPlayer.isDueling() ||
          !player.withinRange(affectedPlayer, 8) ||
          player.isDueling() ||
          player.tradeDuelThrottling()) {
        unsetOptions(player);
        return;
      }
      if ((affectedPlayer.getPrivacySetting(3))) {
        player.getActionSender().sendMessage("This player has duel requests blocked.");
        return;
      }
      player.setWishToDuel(affectedPlayer);
      player.getActionSender()
            .sendMessage(affectedPlayer.isDueling() ? (affectedPlayer.getUsername() +
                                                       " is " +
                                                       "already" +
                                                       " in a duel") : "Sending duel request");
      affectedPlayer.getActionSender()
                    .sendMessage(player.getUsername() +
                                 " " +
                                 Formulae.getLvlDiffColour(affectedPlayer.getCombatLevel() - player.getCombatLevel()) +
                                 "(level-" +
                                 player.getCombatLevel() +
                                 ")" +
                                 "@whi@" +
                                 " wishes to duel with you");
      if (!player.isDueling() &&
          affectedPlayer.getWishToDuel() != null &&
          affectedPlayer.getWishToDuel().equals(player) &&
          !affectedPlayer.isDueling()) {
        player.setDueling(true);
        player.resetPath();
        player.clearDuelOptions();
        player.resetAllExceptDueling();
        affectedPlayer.setDueling(true);
        affectedPlayer.resetPath();
        affectedPlayer.clearDuelOptions();
        affectedPlayer.resetAllExceptDueling();
        player.getActionSender().sendDuelWindowOpen();
        affectedPlayer.getActionSender().sendDuelWindowOpen();
      }
      return;
    } else if (pID == Opcode.getClient(204, Command.Client.CL_DUEL_ACCEPT)) { // Duel accepted
      affectedPlayer = player.getWishToDuel();
      if (affectedPlayer == null || busy(affectedPlayer) || !player.isDueling() || !affectedPlayer.isDueling()) {
        // This
        // shouldn't
        // happen
        player.setSuspiciousPlayer(true);
        unsetOptions(player);
        unsetOptions(affectedPlayer);
        return;
      }
      player.setDuelOfferAccepted(true);
      player.getActionSender().sendDuelAcceptUpdate();
      affectedPlayer.getActionSender().sendDuelAcceptUpdate();
      if (affectedPlayer.isDuelOfferAccepted()) {
        player.getActionSender().sendDuelAccept();
        affectedPlayer.getActionSender().sendDuelAccept();
      }
      return;
    } else if (pID == Opcode.getClient(204, Command.Client.CL_DUEL_CONFIRM_ACCEPT)) { // Confirm accepted
      affectedPlayer = player.getWishToDuel();
      if (affectedPlayer == null ||
          busy(affectedPlayer) ||
          !player.isDueling() ||
          !affectedPlayer.isDueling() ||
          !player.isDuelOfferAccepted() ||
          !affectedPlayer.isDuelOfferAccepted()) { // This
        // shouldn't
        // happen
        player.setSuspiciousPlayer(true);
        unsetOptions(player);
        unsetOptions(affectedPlayer);
        return;
      }
      player.setDuelConfirmAccepted(true);
      if (affectedPlayer.isDuelConfirmAccepted()) {
        player.getActionSender().sendDuelWindowClose();
        player.getActionSender().sendMessage("Commencing Duel");
        affectedPlayer.getActionSender().sendDuelWindowClose();
        affectedPlayer.getActionSender().sendMessage("Commencing Duel");
        player.resetAllExceptDueling();
        player.setBusy(true);
        player.setStatus(Action.DUELING_PLAYER);
        affectedPlayer.resetAllExceptDueling();
        affectedPlayer.setBusy(true);
        affectedPlayer.setStatus(Action.DUELING_PLAYER);
        if (player.getDuelSetting(3)) {
          for (InvItem item : player.getInventory().getItems()) {
            if (item.isWielded()) {
              item.setWield(false);
              player.updateWornItems(
                item.getWieldableDef().getWieldPos(),
                player.getPlayerAppearance().getSprite(item.getWieldableDef().getWieldPos())
              );
            }
          }
          player.getActionSender().sendSound("click");
          player.getActionSender().sendInventory();
          player.getActionSender().sendEquipmentStats();
          for (InvItem item : affectedPlayer.getInventory().getItems()) {
            if (item.isWielded()) {
              item.setWield(false);
              affectedPlayer.updateWornItems(
                item.getWieldableDef().getWieldPos(),
                affectedPlayer.getPlayerAppearance().getSprite(item.getWieldableDef().getWieldPos())
              );
            }
          }
          affectedPlayer.getActionSender().sendSound("click");
          affectedPlayer.getActionSender().sendInventory();
          affectedPlayer.getActionSender().sendEquipmentStats();

        }
        if (player.getDuelSetting(2)) {
          for (int x = 0; x < 14; x++) {
            if (player.isPrayerActivated(x)) {
              player.removePrayerDrain(x);
              player.setPrayer(x, false);
            }
            if (affectedPlayer.isPrayerActivated(x)) {
              affectedPlayer.removePrayerDrain(x);
              affectedPlayer.setPrayer(x, false);
            }
          }
          player.getActionSender().sendPrayers();
          affectedPlayer.getActionSender().sendPrayers();
        }
        player.setFollowing(affectedPlayer);
        WalkToMobEvent walking = new WalkToMobEvent(player, affectedPlayer, 1) {
          public void arrived() {
            DelayedEvent.world.getDelayedEventHandler().add(new SingleEvent(owner, 300) {
              public void action() {
                Player affectedPlayer = (Player) affectedMob;
                owner.resetPath();
                if (!owner.nextTo(affectedPlayer)) {
                  unsetOptions(owner);
                  unsetOptions(affectedPlayer);
                  return;
                }
                affectedPlayer.resetPath();
                owner.resetAllExceptDueling();
                affectedPlayer.resetAllExceptDueling();
                owner.setLocation(affectedPlayer.getLocation(), true);
                for (Player p : owner.getViewArea().getPlayersInView()) {
                  p.removeWatchedPlayer(owner);
                }
                owner.setSprite(9);
                owner.setOpponent(affectedMob);
                owner.setCombatTimer();
                affectedPlayer.setSprite(8);
                affectedPlayer.setOpponent(owner);
                affectedPlayer.setCombatTimer();
                Player attacker, opponent;
                if (owner.getCombatLevel() > affectedPlayer.getCombatLevel()) {
                  attacker = affectedPlayer;
                  opponent = owner;
                } else if (affectedPlayer.getCombatLevel() > owner.getCombatLevel()) {
                  attacker = owner;
                  opponent = affectedPlayer;
                } else if (DataConversions.random(0, 1) == 1) {
                  attacker = owner;
                  opponent = affectedPlayer;
                } else {
                  attacker = affectedPlayer;
                  opponent = owner;
                }
                DuelEvent dueling = new DuelEvent(attacker, opponent);
                dueling.setLastRun(0);
                DelayedEvent.world.getDelayedEventHandler().add(dueling);
              }
            });
          }

          public void failed() {
            Player affectedPlayer = (Player) affectedMob;
            owner.getActionSender()
                 .sendMessage("Error walking to " + affectedPlayer.getUsername() + " (known " + "bug)");
            affectedPlayer.getActionSender()
                          .sendMessage("Error walking to " + owner.getUsername() + " (known " + "bug)");
            unsetOptions(owner);
            unsetOptions(affectedPlayer);
            owner.setBusy(false);
            affectedPlayer.setBusy(false);
          }
        };
        walking.setLastRun(System.currentTimeMillis() + 500);
        world.getDelayedEventHandler().add(walking);
      }
      return;
    } else if (pID == Opcode.getClient(204, Command.Client.CL_DUEL_DECLINE)) { // Decline duel
      affectedPlayer = player.getWishToDuel();
      if (affectedPlayer == null || busy(affectedPlayer) || !player.isDueling() || !affectedPlayer.isDueling()) {
        // This
        // shouldn't
        // happen
        player.setSuspiciousPlayer(true);
        unsetOptions(player);
        unsetOptions(affectedPlayer);
        return;
      }
      affectedPlayer.getActionSender().sendMessage(player.getUsername() + " has declined the duel.");
      unsetOptions(player);
      unsetOptions(affectedPlayer);
      return;
    } else if (pID == Opcode.getClient(204, Command.Client.CL_DUEL_ITEM_UPDATE)) { // Receive offered item data
      affectedPlayer = player.getWishToDuel();
      if (affectedPlayer == null ||
          busy(affectedPlayer) ||
          !player.isDueling() ||
          !affectedPlayer.isDueling() ||
          (player.isDuelOfferAccepted() && affectedPlayer.isDuelOfferAccepted()) ||
          player.isDuelConfirmAccepted() ||
          affectedPlayer.isDuelConfirmAccepted()) { // This shouldn't happen
        player.setSuspiciousPlayer(true);
        unsetOptions(player);
        unsetOptions(affectedPlayer);
        return;
      }
      player.setDuelOfferAccepted(false);
      player.setDuelConfirmAccepted(false);
      affectedPlayer.setDuelOfferAccepted(false);
      affectedPlayer.setDuelConfirmAccepted(false);
      player.getActionSender().sendDuelAcceptUpdate();
      affectedPlayer.getActionSender().sendDuelAcceptUpdate();
      Inventory duelOffer = new Inventory();
      player.resetDuelOffer();
      int count = (int) p.readByte();
      for (int slot = 0; slot < count; slot++) {
        InvItem tItem = new InvItem(p.readShort(), p.readInt());
        if (tItem.getAmount() < 1) {
          player.setSuspiciousPlayer(true);
          continue;
        }
        duelOffer.add(tItem);
      }
      for (InvItem item : duelOffer.getItems()) {
        if (duelOffer.countId(item.getID()) > player.getInventory().countId(item.getID())) {
          player.setSuspiciousPlayer(true);
          return;
        }
        player.addToDuelOffer(item);
      }
      player.setRequiresOfferUpdate(true);
      return;
    } else if (pID == Opcode.getClient(204, Command.Client.CL_DUEL_SETTINGS)) { // Set duel options
      affectedPlayer = player.getWishToDuel();
      if (affectedPlayer == null ||
          busy(affectedPlayer) ||
          !player.isDueling() ||
          !affectedPlayer.isDueling() ||
          (player.isDuelOfferAccepted() && affectedPlayer.isDuelOfferAccepted()) ||
          player.isDuelConfirmAccepted() ||
          affectedPlayer.isDuelConfirmAccepted()) { // This shouldn't happen
        player.setSuspiciousPlayer(true);
        unsetOptions(player);
        unsetOptions(affectedPlayer);
        return;
      }
      player.setDuelOfferAccepted(false);
      player.setDuelConfirmAccepted(false);
      affectedPlayer.setDuelOfferAccepted(false);
      affectedPlayer.setDuelConfirmAccepted(false);
      player.getActionSender().sendDuelAcceptUpdate();
      affectedPlayer.getActionSender().sendDuelAcceptUpdate();
      for (int i = 0; i < 4; i++) {
        boolean b = p.readByte() == 1;
        player.setDuelSetting(i, b);
        affectedPlayer.setDuelSetting(i, b);
      }
      player.getActionSender().sendDuelSettingUpdate();
      affectedPlayer.getActionSender().sendDuelSettingUpdate();
      return;
    }
  }

  private void unsetOptions(Player p) {
    if (p == null) {
      return;
    }
    p.resetDueling();
  }

  private boolean busy(Player player) {
    return player.isBusy() || player.isRanging() || player.accessingBank() || player.isTrading();
  }

}