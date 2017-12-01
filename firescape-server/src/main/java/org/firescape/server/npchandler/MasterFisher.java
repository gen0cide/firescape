package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class MasterFisher implements NpcHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(final Npc npc, Player player) throws Exception {
    player
            .informOfNpcMessage(new ChatMessage(npc, "Hey, would you like me to fill your inventory with sharks?", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = new String[]{"Yes Please", "No Thanks"};
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
                if (option == 0) {
                  {
                    Inventory inventory = owner.getInventory();
                    while (!inventory.full()) {
                      owner.getInventory().add(new InvItem(546, 1));
                      owner.getActionSender().sendInventory();
                      owner.getActionSender().sendInventory();
                    }
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