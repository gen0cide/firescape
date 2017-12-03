package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class MakeOverMage implements NpcHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc( Npc npc, Player player ) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Are you happy with your looks?", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.informOfNpcMessage(new ChatMessage(npc, "If not i can change them for the cheap cheap price of " +
          "3000 coins", owner));
        DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
          public void action() {
            owner.setBusy(false);
            String[] options = {
              "I'm happy with how I look thank you", "Yes change my looks please"
            };
            owner.setMenuHandler(new MenuHandler(options) {
              public void handleReply( int option, String reply ) {
                if (owner.isBusy()) {
                  return;
                }
                owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
                owner.setBusy(true);
                DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                  public void action() {
                    owner.setBusy(false);
                    switch (option) {
                      case 1:
                        if (owner.getInventory().countId(10) < 3000) {
                          owner.informOfChatMessage(new ChatMessage(owner, "I'll just go get the cash", npc));
                        } else if (owner.getInventory().remove(10, 3000) > -1) {
                          owner.setChangingAppearance(true);
                          owner.getActionSender().sendAppearanceScreen();
                          owner.getActionSender().sendInventory();
                        }
                        break;
                    }
                    npc.unblock();
                  }
                });
              }
            });
            owner.getActionSender().sendMenu(options);
          }
        });
      }
    });
    npc.blockedBy(player);
  }

}