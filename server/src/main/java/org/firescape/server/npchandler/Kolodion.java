package org.firescape.server.npchandler;

import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.*;

public class Kolodion implements NpcHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(Npc npc, Player player) throws Exception {
    if (player.getGuthixSpellCast() == 100 &&
        player.getSaradominSpellCast() == 100 &&
        player.getZamorakSpellCast() == 100) {
      player.informOfNpcMessage(new ChatMessage(npc,
                                                "Congratulations. You can now cast all god spells outside the " +
                                                "arena!",
                                                player
      ));
      return;
    }
    if (player.getGuthixSpellCast() > 0 && player.getGuthixSpellCast() < 100 ||
        player.getSaradominSpellCast() > 0 && player.getSaradominSpellCast() < 100 ||
        player.getZamorakSpellCast() > 0 && player.getZamorakSpellCast() < 100) {
      player.informOfNpcMessage(new ChatMessage(npc,
                                                "You still need to cast spells in the arena. Do you want to go " +
                                                "there now?",
                                                player
      ));
      player.setBusy(true);
      world.getDelayedEventHandler().add(new ShortEvent(player) {
        public void action() {
          player.setBusy(false);
          String[] option = {
            "Yes please", "No, not yet"
          };
          player.setMenuHandler(new MenuHandler(option) {
            public void handleReply(int option, String reply) {
              if (player.isBusy()) {
                return;
              }
              player.informOfChatMessage(new ChatMessage(player, reply, npc));
              player.setBusy(true);
              DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  player.setBusy(false);
                  if (option == 0) {
                    player.informOfNpcMessage(new ChatMessage(npc, "Ok! Good luck!", player));
                    player.teleport(228, 113, true);
                  } else {
                    player.informOfNpcMessage(new ChatMessage(npc, "Ok, come back when you are ready!", player));
                  }
                }
              });
            }
          });
          player.getActionSender().sendMenu(option);
        }
      });
    } else if (player.getGuthixSpellCast() == 0 &&
               player.getSaradominSpellCast() == 0 &&
               player.getZamorakSpellCast() == 0) {
      player.informOfNpcMessage(new ChatMessage(npc,
                                                "Cast 100 of each god spell inside the arena to use them " + "outside",
                                                player
      ));
      player.setBusy(true);
      world.getDelayedEventHandler().add(new ShortEvent(player) {
        public void action() {
          owner.setBusy(false);
          String[] options = {
            "Yes, I want to begin this miniquest", "I will do it later"
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
                    owner.informOfNpcMessage(new ChatMessage(npc,
                                                             "Once you enter the mage arena, your client " +
                                                             "information will change!",
                                                             owner
                    ));
                    DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                      public void action() {
                        owner.informOfNpcMessage(new ChatMessage(npc,
                                                                 "It will change into a casting counter, so" +
                                                                 "" +
                                                                 " you can see your progress!",
                                                                 owner
                        ));
                        DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
                          public void action() {
                            owner.informOfNpcMessage(new ChatMessage(npc,
                                                                     "I will now teleport you closer to the" +
                                                                     "" +
                                                                     " arena. Good luck!",
                                                                     owner
                            ));
                            player.teleport(234, 130, true);
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