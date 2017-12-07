package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class ManMeatSeller implements NpcHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(final Npc npc, final Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Would you like to buy some cooked meat? Only 15 gold", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = new String[] {
          "No way creep", "Gimme gimme gimme"
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
                  if (owner.getInventory().remove(10, 15) > -1) {
                    owner.getActionSender().sendMessage("You buy some funky smelling meat");
                    owner.getInventory().add(new InvItem(1269, 1));
                    owner.getActionSender().sendInventory();
                    npc.unblock();
                  } else {
                    owner.informOfChatMessage(new ChatMessage(owner, "You dont have enough money pal", npc));
                    owner.setBusy(true);
                    world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
                        owner.informOfNpcMessage(new ChatMessage(npc, "See ya later", owner));
                        npc.unblock();
                      }
                    });
                  }
                } else {
                  npc.unblock();
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