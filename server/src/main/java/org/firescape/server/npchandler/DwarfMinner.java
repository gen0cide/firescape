package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class DwarfMinner implements NpcHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(final Npc npc, final Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Would you like to buy a pick ax? Only 20 gold", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = new String[] {
          "No thanks", "Deal!"
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
                  if (owner.getInventory().remove(10, 20) > -1) {
                    owner.getActionSender().sendMessage("You buy a iron pick ax");
                    owner.getInventory().add(new InvItem(1258, 1));
                    owner.getActionSender().sendInventory();
                    npc.unblock();
                  } else {
                    owner.informOfChatMessage(new ChatMessage(owner, "You dont have enough gold", npc));
                    owner.setBusy(true);
                    world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
                        owner.informOfNpcMessage(new ChatMessage(npc, "Bye", owner));
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