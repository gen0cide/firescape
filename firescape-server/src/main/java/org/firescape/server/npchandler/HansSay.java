package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class HansSay implements NpcHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(final Npc npc, final Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Hey, I sell starter bows and crossbow sets, 50gp each", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = new String[]{
                "No thank you",
                "Sure, I'll take a bow and arrows!",
                "Sure, I'll take a crossbow and bolts!",
                "I heard you may have party hats for 500?"
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
                  if (owner.getInventory().remove(10, 50) > -1) {
                    owner.getActionSender().sendMessage("You buy a bow and some arrows");
                    owner.getInventory().add(new InvItem(189, 1));
                    owner.getInventory().add(new InvItem(11, 20));
                    owner.getActionSender().sendInventory();
                    npc.unblock();
                  } else {
                    owner.informOfChatMessage(new ChatMessage(owner, "Scram!", npc));
                    owner.setBusy(true);
                    world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
                        npc.unblock();
                      }
                    });
                  }
                } else if (option == 2) {
                  if (owner.getInventory().remove(10, 50) > -1) {
                    owner.getActionSender().sendMessage("You buy a crossbow and some bolts");
                    owner.getInventory().add(new InvItem(60, 1));
                    owner.getInventory().add(new InvItem(190, 20));
                    owner.getActionSender().sendInventory();
                    npc.unblock();
                  } else {
                    owner.informOfChatMessage(new ChatMessage(owner, "Scram!", npc));
                    owner.setBusy(true);
                    world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
                        npc.unblock();
                      }
                    });
                  } 
                } else if (option == 3) {
                  if (owner.getInventory().remove(10, 500) > -1) {
                    owner.getActionSender().sendMessage("You buy a party hat!");
                    owner.getInventory().add(new InvItem(576, 1));
                    owner.getActionSender().sendInventory();
                    npc.unblock();
                  } else {
                    owner.informOfChatMessage(new ChatMessage(owner, "Scram!", npc));
                    owner.setBusy(true);
                    world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
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