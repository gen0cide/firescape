package org.rscdaemon.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.rscdaemon.server.event.ShortEvent;
import org.rscdaemon.server.event.WalkToMobEvent;
import org.rscdaemon.server.model.Bubble;
import org.rscdaemon.server.model.InvItem;
import org.rscdaemon.server.model.Player;
import org.rscdaemon.server.model.World;
import org.rscdaemon.server.net.Packet;
import org.rscdaemon.server.packethandler.PacketHandler;
import org.rscdaemon.server.states.Action;
import org.rscdaemon.server.util.DataConversions;

public class InvUseOnPlayer implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    if (player.isBusy()) {
      player.resetPath();
      return;
    }
    player.resetAll();
    final Player affectedPlayer = world.getPlayer(p.readShort());
    final InvItem item = player.getInventory().get(p.readShort());
    if (affectedPlayer == null || item == null) { // This shouldn't happen
      return;
    }
    player.setFollowing(affectedPlayer);
    player.setStatus(Action.USING_INVITEM_ON_PLAYER);
    world.getDelayedEventHandler().add(new WalkToMobEvent(player, affectedPlayer, 1) {
      public void arrived() {
        owner.resetPath();
        if (!owner.getInventory().contains(item) || !owner.nextTo(affectedPlayer) || owner.isBusy() || owner.isRanging()
            || owner.getStatus() != Action.USING_INVITEM_ON_PLAYER) {
          return;
        }
        owner.resetAll();
        switch (item.getID()) {
        case 575: // Christmas cracker
          owner.setBusy(true);
          affectedPlayer.setBusy(true);
          owner.resetPath();
          affectedPlayer.resetPath();
          Bubble crackerBubble = new Bubble(owner, 575);
          for (Player p : owner.getViewArea().getPlayersInView()) {
            p.informOfBubble(crackerBubble);
          }
          owner.getActionSender().sendMessage("You pull the cracker with " + affectedPlayer.getUsername() + "...");
          affectedPlayer.getActionSender().sendMessage(owner.getUsername() + " is pulling a cracker with you...");
          world.getDelayedEventHandler().add(new ShortEvent(owner) {
            public void action() {
              InvItem phat = new InvItem(DataConversions.random(576, 581));
              if (DataConversions.random(0, 1) == 1) {
                owner.getActionSender().sendMessage("Out comes a " + phat.getDef().getName() + "!");
                affectedPlayer.getActionSender().sendMessage(owner.getUsername() + " got the contents!");
                owner.getInventory().add(phat);
              } else {
                owner.getActionSender().sendMessage(affectedPlayer.getUsername() + " got the contents!");
                affectedPlayer.getActionSender().sendMessage("Out comes a " + phat.getDef().getName() + "!");
                affectedPlayer.getInventory().add(phat);
              }
              owner.getInventory().remove(item);
              owner.setBusy(false);
              affectedPlayer.setBusy(false);
              owner.getActionSender().sendInventory();
              affectedPlayer.getActionSender().sendInventory();
            }
          });
          break;
        case 981: // Gnome Ball Passing
          if (!affectedPlayer.getInventory().full()) {
            owner.getInventory().remove(item);
            owner.getActionSender().sendInventory();
            owner.getActionSender().sendMessage("You have passed the gnome ball to " + affectedPlayer.getUsername());
            affectedPlayer.getInventory().add(new InvItem(981));
            affectedPlayer.getActionSender().sendInventory();
            affectedPlayer.getActionSender().sendMessage(owner.getUsername() + " has passed you the gnome ball.");
          } else {
            owner.getActionSender().sendMessage("You have tried to pass the gnome ball to "
                + affectedPlayer.getUsername() + " but they can't hold it.");
            affectedPlayer.getActionSender()
                .sendMessage(owner.getUsername() + " tried to pass you the gnome ball, but you can't hold it.");
          }
          break;
        default:
          owner.getActionSender().sendMessage("Nothing interesting happens.");
          break;
        }
      }
    });
  }

}