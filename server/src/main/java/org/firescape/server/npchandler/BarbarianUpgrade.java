package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class BarbarianUpgrade implements NpcHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(final Npc npc, final Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Hey, wana upgrade one of those wimpy axes for another 100 gold?", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = new String[]{
                "Axes aren't my thing.",
                "Sure!"
        };
        owner.setMenuHandler(new MenuHandler(options) {
          public void handleReply(final int option, final String reply) {
            if (owner.isBusy()) {
              return;
            }
            owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
            owner.setBusy(true);
            world.getDelayedEventHandler().add(new ShortEvent(owner) {
              public void action() {
                owner.setBusy(false);
                if (option == 1) {
                    if (owner.getInventory().countId(90) < 1) {
                      owner.getActionSender().sendMessage("You don't have an axe to upgrade");
                      owner.setBusy(false);
                      npc.unblock();
                    } else if (owner.getInventory().countId(10) < 100) {
                      owner.getActionSender().sendMessage("You do not have enough gold.");
                      running = false;
                      owner.setBusy(false);
                      npc.unblock();
                    } else {
                      owner.getInventory().remove(90, 1);
                      owner.getInventory().remove(10, 100);
                      owner.getInventory().add(new InvItem(91, 1));
                      owner.getActionSender().sendInventory();
                      npc.unblock();
                      owner.setBusy(false);
                    }
                } else {
                    owner.informOfChatMessage(new ChatMessage(owner, "Not today!", npc));
                    owner.setBusy(true);
                    world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
                        npc.unblock();
                      }
                    });
                }
                }
              
            });
          }
        });
        owner.getActionSender().sendMenu(options);
      }
    });
    npc.blockedBy(player);
  }

}