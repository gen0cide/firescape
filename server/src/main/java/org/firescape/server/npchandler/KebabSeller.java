package org.firescape.server.npchandler;

import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class KebabSeller implements NpcHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(Npc npc, Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Would you like to buy a nice kebab? Only 1 gold", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = {
          "I think I'll give it a miss", "Yes please"
        };
        owner.setMenuHandler(new MenuHandler(options) {
          public void handleReply(int option, String reply) {
            if (owner.isBusy()) {
              return;
            }
            owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
            owner.setBusy(true);
            DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
              public void action() {
                owner.setBusy(false);
                if (option == 1) {
                  if (owner.getInventory().remove(10, 1) > -1) {
                    owner.getActionSender().sendMessage("You buy a kebab");
                    owner.getInventory().add(new InvItem(210, 1));
                    owner.getActionSender().sendInventory();
                    npc.unblock();
                  } else {
                    owner.informOfChatMessage(new ChatMessage(owner,
                                                              "Oops I forgot to bring any money with" + " me",
                                                              npc
                    ));
                    owner.setBusy(true);
                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
                        owner.informOfNpcMessage(new ChatMessage(npc, "Come back when you have some", owner));
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