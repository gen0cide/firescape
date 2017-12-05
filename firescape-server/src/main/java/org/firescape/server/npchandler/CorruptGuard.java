package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class CorruptGuard implements NpcHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(final Npc npc, final Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Hey look, I'm not supposed to do this, but I'll sell you some guard gear for 150 gold", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = new String[]{
                "No way, is that even legal?",
                "Sure, It's super stylish"
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
                  if (owner.getInventory().remove(10, 150) > -1) {
                    owner.getActionSender().sendMessage("You buy some guard gear");
                    owner.getInventory().add(new InvItem(104, 1));
                    owner.getInventory().add(new InvItem(7, 1));
                    owner.getActionSender().sendInventory();
                    npc.unblock();
                  } else {
                    owner.informOfChatMessage(new ChatMessage(owner, "I oughta lock you up!", npc));
                    owner.setBusy(true);
                    world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
                        owner.informOfNpcMessage(new ChatMessage(npc, "Stay safe out there", owner));
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