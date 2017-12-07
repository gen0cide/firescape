package org.firescape.server.npchandler;

import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class Aggie implements NpcHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(Npc npc, Player player) throws Exception {
    player.setBusy(true);
    player.informOfNpcMessage(new ChatMessage(npc, "What can I help you with?", player));
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        String[] options = {
          "Can you turn people into frogs?", "You mad old witch, you can't help me", "Can you make dyes for me please?"
        };
        String[] options2 = {
          "Blue Dye", "Red Dye", "Yellow Dye"
        };
        owner.getActionSender().sendMenu(options);
        owner.setMenuHandler(new MenuHandler(options) {
          public void handleReply(int option, String reply) {
            /*
             * if(owner.isBusy()) { return; }
             */
            owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
            DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
              public void action() {
                if (option == 0) {
                  owner.setBusy(true);
                  owner.informOfNpcMessage(new ChatMessage(npc,
                                                           "Oh, not for years, but if you meet a talking " + "chicken",
                                                           owner
                  ));
                  DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                    public void action() {
                      owner.informOfNpcMessage(new ChatMessage(npc,
                                                               "You have problably met the professor in the" +
                                                               "" +
                                                               " mainor north of here",
                                                               owner
                      ));
                      DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                        public void action() {
                          owner.informOfNpcMessage(new ChatMessage(npc,
                                                                   "A few years ago it was flying fish, " +
                                                                   "that machine is a menace",
                                                                   owner
                          ));
                          DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                            public void action() {
                              owner.setBusy(false);
                            }
                          });
                        }
                      });
                    }
                  });

                }
                if (option == 1) {
                  owner.setBusy(true);
                  if (owner.getInventory().countId(10) >= 20) { // change the
                    // "100000"s to
                    // whatever u
                    // want the
                    // price to be
                    owner.informOfNpcMessage(new ChatMessage(npc, "Oh, you like to call witch, do you?", owner));
                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.getInventory().remove(10, 20);
                        owner.getActionSender().sendInventory();
                        owner.getActionSender()
                             .sendMessage("Aggie waves her hands about, and you seem to be 20 " + "dollar poorer");
                        owner.informOfNpcMessage(new ChatMessage(npc,
                                                                 "Thats a fine for insulting a witch, you " +
                                                                 "should learn some respect",
                                                                 owner
                        ));
                        DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                          public void action() {
                          }
                        });
                      }
                    });
                  } else if (owner.getInventory().countId(10) < 20) {
                    owner.informOfNpcMessage(new ChatMessage(npc, "Oh, you like to call witch, do you?", owner));
                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.informOfNpcMessage(new ChatMessage(npc,
                                                                 "You should be careful about insulting a " + "witch",
                                                                 owner
                        ));
                        DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                          public void action() {
                            owner.informOfNpcMessage(new ChatMessage(npc,
                                                                     "You never know what shape you could " +
                                                                     "wake up in",
                                                                     owner
                            ));
                            DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                              public void action() {
                              }
                            });
                          }
                        });
                      }
                    });
                  }
                }
                if (option == 2) {
                  owner.setBusy(true);
                  owner.informOfNpcMessage(new ChatMessage(npc, "Ofcourse i can, make your choice", owner));
                  DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                    public void action() {
                      owner.getActionSender().sendMenu(options2);
                      owner.setMenuHandler(new MenuHandler(options2) {
                        public void handleReply(int option2, String reply) {
                          owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
                          DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                            public void action() {
                              if (option2 == 0) {
                                owner.setBusy(true);
                                owner.informOfNpcMessage(new ChatMessage(npc,
                                                                         "You will need woad leaves and 5 " +
                                                                         "$, then I can make you that dye",
                                                                         owner
                                ));
                                DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                                  public void action() {
                                    if (owner.getInventory().countId(281) < 2) {
                                      owner.getActionSender().sendMessage("You don't have enough woad leaves.");
                                      owner.setBusy(false);
                                      npc.unblock();
                                    } else if (owner.getInventory().countId(10) < 5) {
                                      owner.getActionSender().sendMessage("You do not have enough cash.");
                                      running = false;
                                      owner.setBusy(false);
                                      npc.unblock();
                                    } else {
                                      owner.getInventory().remove(281, 2);
                                      owner.getInventory().remove(10, 5);
                                      owner.getInventory().add(new InvItem(272, 1));
                                      owner.getActionSender().sendInventory();
                                      npc.unblock();
                                      owner.setBusy(false);
                                    }
                                  }
                                });
                              } else if (option2 == 1) {
                                owner.setBusy(true);
                                owner.informOfNpcMessage(new ChatMessage(npc,
                                                                         "You will need readberries and 5 " +
                                                                         "$, then I can make you that dye",
                                                                         owner
                                ));
                                DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                                  public void action() {
                                    if (owner.getInventory().countId(236) < 3) {
                                      owner.getActionSender().sendMessage("You don't have enough redberries.");
                                      owner.setBusy(false);
                                      npc.unblock();
                                    } else if (owner.getInventory().countId(10) < 5) {
                                      owner.getActionSender().sendMessage("You do not have enough cash.");
                                      running = false;
                                      owner.setBusy(false);
                                      npc.unblock();
                                    } else {
                                      owner.getInventory().remove(236, 3);
                                      owner.getInventory().remove(10, 5);
                                      owner.getInventory().add(new InvItem(238, 1));
                                      owner.getActionSender().sendInventory();
                                      npc.unblock();
                                      owner.setBusy(false);
                                    }
                                  }
                                });
                              } else if (option2 == 2) {
                                owner.setBusy(true);
                                owner.informOfNpcMessage(new ChatMessage(npc,
                                                                         "You will need onions and 5 $, " +
                                                                         "then I can make you that dye",
                                                                         owner
                                ));
                                DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                                  public void action() {
                                    if (owner.getInventory().countId(241) < 2) {
                                      owner.getActionSender().sendMessage("You don't have enough onions.");
                                      owner.setBusy(false);
                                      npc.unblock();
                                    } else if (owner.getInventory().countId(10) < 5) {
                                      owner.getActionSender().sendMessage("You do not have enough cash.");
                                      running = false;
                                      owner.setBusy(false);
                                      npc.unblock();
                                    } else {
                                      owner.getInventory().remove(241, 2);
                                      owner.getInventory().remove(10, 5);
                                      owner.getInventory().add(new InvItem(239, 1));
                                      owner.getActionSender().sendInventory();
                                      npc.unblock();
                                      owner.setBusy(false);
                                    }
                                  }
                                });
                              }
                            }
                          });
                        }
                      });
                    }
                  });
                }
                npc.unblock();
                owner.setBusy(false);
              }
            });
          }
        });
      }
    });
    npc.blockedBy(player);
  }

}