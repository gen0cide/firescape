package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.FightEvent;
import org.firescape.server.event.RangeEvent;
import org.firescape.server.event.WalkToMobEvent;
import org.firescape.server.model.Mob;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.opcode.Command;
import org.firescape.server.opcode.Opcode;
import org.firescape.server.packethandler.PacketHandler;
import org.firescape.server.states.Action;

public class AttackHandler implements PacketHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    int pID = ((RSCPacket) p).getID();
    if (player.isBusy()) {
      player.resetPath();
      return;
    }
    player.resetAll();
    Mob affectedMob = null;
    int serverIndex = p.readShort();
    if (pID == Opcode.getClient(204, Command.Client.CL_PLAYER_ATTACK)) {
      affectedMob = world.getPlayer(serverIndex);
    } else if (pID == Opcode.getClient(204, Command.Client.CL_NPC_ATTACK)) {
      affectedMob = world.getNpc(serverIndex);
    }
    if (affectedMob == null || affectedMob.equals(player)) { // This shouldn't
      // happen
      player.resetPath();
      return;
    }
    player.setFollowing(affectedMob);
    player.setStatus(Action.ATTACKING_MOB);
    if (player.getRangeEquip() < 0) {
      world.getDelayedEventHandler().add(new WalkToMobEvent(player, affectedMob, 2) {
        public void arrived() {
          owner.resetPath();
          if (owner.isBusy() ||
              affectedMob.isBusy() ||
              !owner.nextTo(affectedMob) ||
              !owner.checkAttack(affectedMob, false) ||
              owner.getStatus() != Action.ATTACKING_MOB) {
            return;
          }
          owner.resetAll();
          owner.setStatus(Action.FIGHTING_MOB);
          if (affectedMob instanceof Player) {
            Player affectedPlayer = (Player) affectedMob;
            owner.setSkulledOn(affectedPlayer);
            affectedPlayer.resetAll();
            affectedPlayer.setStatus(Action.FIGHTING_MOB);
            affectedPlayer.getActionSender().sendMessage("You are under attack!");
          }
          affectedMob.resetPath();
          owner.setLocation(affectedMob.getLocation(), true);
          for (Player p : owner.getViewArea().getPlayersInView()) {
            p.removeWatchedPlayer(owner);
          }
          owner.setBusy(true);
          owner.setSprite(9);
          owner.setOpponent(affectedMob);
          owner.setCombatTimer();
          affectedMob.setBusy(true);
          affectedMob.setSprite(8);
          affectedMob.setOpponent(owner);
          affectedMob.setCombatTimer();
          FightEvent fighting = new FightEvent(owner, affectedMob);
          fighting.setLastRun(0);
          DelayedEvent.world.getDelayedEventHandler().add(fighting);
        }
      });
    } else {
      world.getDelayedEventHandler().add(new WalkToMobEvent(player, affectedMob, 5) {
        public void arrived() {
          owner.resetPath();
          if (owner.isBusy() || !owner.checkAttack(affectedMob, true) || owner.getStatus() != Action.ATTACKING_MOB) {
            return;
          }
          owner.resetAll();
          owner.setStatus(Action.RANGING_MOB);
          if (affectedMob instanceof Player) {
            Player affectedPlayer = (Player) affectedMob;
            owner.setSkulledOn(affectedPlayer);
            affectedPlayer.resetTrade();
            if (affectedPlayer.getMenuHandler() != null) {
              affectedPlayer.resetMenuHandler();
            }
            if (affectedPlayer.accessingBank()) {
              affectedPlayer.resetBank();
            }
            if (affectedPlayer.accessingShop()) {
              affectedPlayer.resetShop();
            }
            if (affectedPlayer.getNpc() != null) {
              affectedPlayer.getNpc().unblock();
              affectedPlayer.setNpc(null);
            }
          }
          if (affectedMob.isPrayerActivated(13)) {
            owner.getActionSender().sendMessage("Your missiles are blocked");
            return;
          }
          owner.setRangeEvent(new RangeEvent(owner, affectedMob));
        }
      });
    }
  }
}