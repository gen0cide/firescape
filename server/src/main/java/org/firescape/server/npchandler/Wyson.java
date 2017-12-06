package org.firescape.server.npchandler;

import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.MiniEvent;
import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class Wyson implements NpcHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(Npc npc, Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Hey, I heard that you are looking for woad leaves.", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = {
          "Well, yes I am. Can you get some?", "Who told you that?"
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
                if (option == 0) {
                  if (owner.getInventory().countId(10) >= 20) {
                    owner.setBusy(true);
                    owner.informOfNpcMessage(new ChatMessage(npc, "Yes i have some somewhere.", owner));
                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.informOfChatMessage(new ChatMessage(owner, "Can I buy one please?", npc));
                        DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                          public void action() {
                            owner.informOfNpcMessage(new ChatMessage(npc, "That will be 20gp.", owner));
                            DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                              public void action() {
                                owner.setBusy(true);
                                owner.getActionSender().sendMessage("You give Wyson 20gp.");
                                owner.getInventory().remove(10, 20);
                                owner.getInventory().add(new InvItem(281, 1));
                                owner.getActionSender().sendInventory();
                                owner.getActionSender()
                                     .sendMessage("Wyson the gardener gives you some woad " + "leaves.");
                                DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                                  public void action() {
                                    owner.informOfNpcMessage(new ChatMessage(npc,
                                                                             "Here have some more you're a " +
                                                                             "generous person.",
                                                                             owner
                                    ));
                                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                                      public void action() {
                                        owner.getInventory().add(new InvItem(281, 1));
                                        owner.getActionSender().sendInventory();
                                        owner.getActionSender()
                                             .sendMessage("Wyson the gardener gives you some " + "more leaves.");
                                        owner.setBusy(false);

                                      }
                                    });
                                  }
                                });
                              }
                            });
                          }
                        });
                      }
                    });
                  } else if (owner.getInventory().countId(10) < 20) {
                    owner.setBusy(true);
                    owner.informOfChatMessage(new ChatMessage(owner,
                                                              "I don't have enough money to buy the " +
                                                              "leaves. I'll come back later.",
                                                              npc
                    ));
                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        running = false;
                        owner.setBusy(false);
                        npc.unblock();

                      }
                    });
                  }
                }
                if (option == 1) {
                  owner.informOfNpcMessage(new ChatMessage(npc,
                                                           "I can't remember now. Someone who visits this " + "park.",
                                                           owner
                  ));
                  DelayedEvent.world.getDelayedEventHandler().add(new MiniEvent(getOwner(), 1500) {
                    public void action() {
                      owner.setBusy(false);
                    }
                  });
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