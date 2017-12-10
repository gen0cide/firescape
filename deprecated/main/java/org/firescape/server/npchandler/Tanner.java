package org.firescape.server.npchandler;

import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.ShortEvent;

public class Tanner implements NpcHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(Npc npc, Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Greeting friend i'm a manufacturer of leather", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = {
          "Can I buy some leather then?",
          "Here's some cow hides, can I buy some leather now?",
          "Leather is rather weak stuff"
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
                switch (option) {
                  case 0:
                    owner.informOfNpcMessage(new ChatMessage(npc, "I make leather from cow hides", owner));
                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.setBusy(false);
                        owner.informOfNpcMessage(new ChatMessage(npc,
                                                                 "Bring me some of them and a gold coin per" +
                                                                 "" +
                                                                 " hide",
                                                                 owner
                        ));
                        npc.unblock();
                      }
                    });
                    break;
                  case 1:
                    owner.informOfNpcMessage(new ChatMessage(npc, "Ok", owner));
                    DelayedEvent.world.getDelayedEventHandler().add(new DelayedEvent(owner, 500) {
                      public void run() {
                        InvItem hides = owner.getInventory().get(owner.getInventory().getLastIndexById(147));
                        if (hides == null) {
                          owner.getActionSender().sendMessage("You have run out of cow hides");
                          running = false;
                          owner.setBusy(false);
                        } else if (owner.getInventory().countId(10) < 1) {
                          owner.getActionSender().sendMessage("You have run out of coins");
                          running = false;
                          owner.setBusy(false);
                        } else if (owner.getInventory().remove(hides) > -1 && owner.getInventory().remove(10, 1) > -1) {
                          owner.getInventory().add(new InvItem(148, 1));
                          owner.getActionSender().sendInventory();
                        } else {
                          running = false;
                          owner.setBusy(false);
                        }
                      }
                    });
                    npc.unblock();
                    break;
                  case 2:
                    owner.setBusy(false);
                    owner.informOfNpcMessage(new ChatMessage(npc,
                                                             "Well yes if all you're concerned with is how " +
                                                             "much it will protect you in a fight",
                                                             owner
                    ));
                    npc.unblock();
                    break;
                  default:
                    owner.setBusy(false);
                    npc.unblock();
                    break;
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