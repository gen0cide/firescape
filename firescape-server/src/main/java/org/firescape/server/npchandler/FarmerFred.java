package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class FarmerFred implements NpcHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc( Npc npc, Player player ) throws Exception {
    if (player.getSheepShearerStatus() == 2) {
      player.informOfNpcMessage(new ChatMessage(npc, "Thank you. The wool yarn balls really came in handy!", player));
      return;
    }
    if (player.getSheepShearerStatus() == 1) {
      player.informOfNpcMessage(new ChatMessage(npc, "Have you got the 20 wool yarn balls?", player));
      player.setBusy(true);
      world.getDelayedEventHandler().add(new ShortEvent(player) {
        public void action() {
          player.setBusy(false);
          String[] option = {
            "Yes, I have them here", "No, sorry. I am still trying to get them"
          };
          player.setMenuHandler(new MenuHandler(option) {
            public void handleReply( int option, String reply ) {
              if (player.isBusy()) {
                return;
              }
              player.informOfChatMessage(new ChatMessage(player, reply, npc));
              player.setBusy(true);
              DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  player.setBusy(false);
                  if (option == 1) {
                    player.informOfNpcMessage(new ChatMessage(npc, "Ok. Come back when you have sheared those " +
                      "sheep!", player));
                    return;
                  }
                  if (option == 0 && player.getInventory().countId(207) >= 20) {
                    player.informOfNpcMessage(new ChatMessage(npc, "Thank you! Please take this as a reward for your " +
                      "" + "help!", player));
                    player.getInventory().add(new InvItem(10, 60));
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getInventory().remove(207, 1);
                    player.getActionSender().sendInventory();
                    player.isSheepShearerComplete();
                    player.setQuestPoints(player.getQuestPoints() + 1);
                    player.getActionSender().sendQuestPoints();
                    player.getActionSender().sendSheepShearerComplete();
                    player.incExp(12, 88, false, false);
                    player.getActionSender().sendStat(12);
                    player.getActionSender().sendMessage("@gre@Congratulations! You have just completed the: " +
                      "@or1@Sheep Shearer @gre@quest!");
                    player.getActionSender().sendMessage("@gre@You gained @or1@1 @gre@quest point!");
                  } else {
                    player.informOfNpcMessage(new ChatMessage(npc, "It appears you don't have all my wool yarn " +
                      "balls!", player));
                  }
                }
              });
            }
          });
          player.getActionSender().sendMenu(option);
        }
      });
    } else if (player.getSheepShearerStatus() == 0) {
      player.informOfNpcMessage(new ChatMessage(npc, "If you aren't busy, would you mind helping me out?", player));
      player.setBusy(true);
      world.getDelayedEventHandler().add(new ShortEvent(player) {
        public void action() {
          owner.setBusy(false);
          String[] options = {
            "Sure, give me a quest!", "I'm too busy, sorry"
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
                  if (option == 0) {
                    owner.informOfNpcMessage(new ChatMessage(npc, "I need you to go and find a tool to shear " +
                      "those sheep,", owner));
                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.informOfNpcMessage(new ChatMessage(npc, "spin it and bring me 20 wool yarn balls. " +
                          "Good luck", owner));
                        DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                          public void action() {
                            owner.informOfChatMessage(new ChatMessage(owner, "Ok, I will go get them right " +
                              "away!", npc));
                            owner.setSheepShearerStatus(1);
                            npc.unblock();
                          }
                        });
                      }
                    });
                  } else {
                    owner.setBusy(false);
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
}