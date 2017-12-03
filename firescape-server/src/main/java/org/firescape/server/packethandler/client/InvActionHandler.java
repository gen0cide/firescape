package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.entityhandling.defs.extras.ItemUnIdentHerbDef;
import org.firescape.server.event.MiniEvent;
import org.firescape.server.event.ShortEvent;
import org.firescape.server.event.SingleEvent;
import org.firescape.server.model.*;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;
import org.firescape.server.util.DataConversions;

public class InvActionHandler implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public static int rand( int bound ) {
    return rand(0, bound - 1);
  }

  public static int rand( int low, int high ) {
    return low + (int) (Math.random() * (high - low + 1));
  }

  public void handlePacket( Packet p, IoSession session ) throws Exception {
    Player player = (Player) session.getAttachment();
    if (player.isBusy()) {
      if (player.inCombat()) {
        player.getActionSender().sendMessage("You cannot do that whilst fighting!");
      }
      return;
    } // incExp(
    player.resetAll();
    int idx = (int) p.readShort();
    if (idx < 0 || idx >= player.getInventory().size()) {
      player.setSuspiciousPlayer(true);
      return;
    }
    InvItem item = player.getInventory().get(idx);
    if (item == null || item.getDef().getCommand().equals("")) {
      player.setSuspiciousPlayer(true);
      return;
    }
    if (item.isEdible()) {
      player.setBusy(true);
      player.getActionSender().sendSound("eat");
      player.getActionSender().sendMessage("You eat the " + item.getDef().getName() + ".");
      boolean heals = player.getCurStat(3) < player.getMaxStat(3);
      if (heals) {
        int newHp = player.getCurStat(3) + item.eatingHeals();
        if (newHp > player.getMaxStat(3)) {
          newHp = player.getMaxStat(3);
        }
        player.setCurStat(3, newHp);
        player.getActionSender().sendStat(3);
      }
      world.getDelayedEventHandler().add(new SingleEvent(player, 200) {
        public void action() {
          if (heals) {
            owner.getActionSender().sendMessage("It heals some health.");
          }
          owner.getInventory().remove(item);
          switch (item.getID()) {
            case 326: // Meat pizza
              owner.getInventory().add(new InvItem(328));
              break;
            case 327: // Anchovie pizza
              owner.getInventory().add(new InvItem(329));
              break;
            case 330: // Cake
              owner.getInventory().add(new InvItem(333));
              break;
            case 333: // Partical cake
              owner.getInventory().add(new InvItem(335));
              break;
            case 332: // Choc cake
              owner.getInventory().add(new InvItem(334));
              break;
            case 334: // Partical choc cake
              owner.getInventory().add(new InvItem(336));
              break;
            case 257: // Apple pie
              owner.getInventory().add(new InvItem(263));
              break;
            case 261: // Half apple pie
              owner.getInventory().add(new InvItem(251));
              break;
            case 258: // Redberry pie
              owner.getInventory().add(new InvItem(262));
              break;
            case 262: // Half redberry pie
              owner.getInventory().add(new InvItem(251));
              break;
            case 259: // Meat pie
              owner.getInventory().add(new InvItem(261));
              break;
            case 263: // Half meat pie
              owner.getInventory().add(new InvItem(251));
              break;
          }
          owner.getActionSender().sendInventory();
          owner.setBusy(false);
        }
      });
    } else if (item.getDef().getCommand().equalsIgnoreCase("bury")) {
      player.setBusy(true);
      player.getActionSender().sendMessage("You dig a hole in the ground.");
      world.getDelayedEventHandler().add(new MiniEvent(player) {
        public void action() {
          owner.getActionSender().sendMessage("You bury the " + item.getDef().getName() + ".");
          owner.getInventory().remove(item);
          switch (item.getID()) {
            case 20: // Bones
            case 604: // Bat bones
              owner.incExp(5, 150, true, true);
              break;
            case 413: // Big bones
              owner.incExp(5, 300, true, true);
              break;
            case 814: // Dragon bones
              owner.incExp(5, 600, true, true);
              break;
          }
          owner.getActionSender().sendStat(5);
          owner.getActionSender().sendInventory();
          owner.setBusy(false);
        }
      });
    } else if (item.getDef().getCommand().equalsIgnoreCase("clean")) {
      ItemUnIdentHerbDef herb = item.getUnIdentHerbDef();
      if (herb == null) {
        return;
      }
      if (player.getMaxStat(15) < herb.getLevelRequired()) {
        player.getActionSender().sendMessage("Your herblaw ability is not high enough to clean this herb.");
        return;
      }
      player.setBusy(true);
      world.getDelayedEventHandler().add(new MiniEvent(player) {
        public void action() {
          ItemUnIdentHerbDef herb = item.getUnIdentHerbDef();
          InvItem newItem = new InvItem(herb.getNewId());
          owner.getInventory().remove(item);
          owner.getInventory().add(newItem);
          owner.getActionSender().sendMessage("You clean the mud off the " + newItem.getDef().getName() + ".");
          owner.incExp(15, herb.getExp(), true, true);
          owner.getActionSender().sendStat(15);
          owner.getActionSender().sendInventory();
          owner.setBusy(false);
        }
      });
    } else if (item.getDef().getCommand().equalsIgnoreCase("drink")) {
      switch (item.getID()) {
        case 739: // Tea
          player.setBusy(true);
          player.getActionSender().sendMessage("You drink the " + item.getDef().getName() + ".");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              owner.getActionSender().sendMessage("It's nice and refreshing.");
              owner.getInventory().remove(item);
              owner.getActionSender().sendInventory();
              owner.setBusy(false);
            }
          });
          showBubble(player, item);
          break;
        case 193: // Beer
          player.setBusy(true);
          player.getActionSender().sendMessage("You drink the " + item.getDef().getName() + ".");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              owner.getActionSender().sendMessage("You feel slightly dizzy.");
              owner.setCurStat(0, owner.getCurStat(0) - 1);
              owner.getActionSender().sendStat(0);
              if (owner.getCurStat(2) <= owner.getMaxStat(2)) {
                owner.setCurStat(2, owner.getCurStat(2) + 2);
                owner.getActionSender().sendStat(2);
              }
              owner.getInventory().remove(item);
              owner.getInventory().add(new InvItem(620));
              owner.getActionSender().sendInventory();
              owner.setBusy(false);
            }
          });
          showBubble(player, item);
          break;
        case 830: // Greenmans Ale
          player.setBusy(true);
          player.getActionSender().sendMessage("You drink the " + item.getDef().getName() + ".");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              owner.getActionSender().sendMessage("It has a strange taste.");
              for (int stat = 0; stat < 3; stat++) {
                owner.setCurStat(stat, owner.getCurStat(stat) - 4);
                owner.getActionSender().sendStat(stat);
              }
              if (owner.getCurStat(15) <= owner.getMaxStat(15)) {
                owner.setCurStat(15, owner.getCurStat(15) + 1);
                owner.getActionSender().sendStat(15);
              }
              owner.getInventory().remove(item);
              owner.getInventory().add(new InvItem(620));
              owner.getActionSender().sendInventory();
              owner.setBusy(false);
            }
          });
          showBubble(player, item);
          break;
        case 268: // Mind Bomb
          player.setBusy(true);
          player.getActionSender().sendMessage("You drink the " + item.getDef().getName() + ".");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              owner.getActionSender().sendMessage("You feel very strange.");
              for (int stat = 0; stat < 3; stat++) {
                owner.setCurStat(stat, owner.getCurStat(stat) - 4);
                owner.getActionSender().sendStat(stat);
              }
              if (owner.getCurStat(6) <= owner.getMaxStat(6)) {
                owner.setCurStat(6, owner.getCurStat(6) + 2);
                owner.getActionSender().sendStat(6);
              }
              owner.getInventory().remove(item);
              owner.getInventory().add(new InvItem(620));
              owner.getActionSender().sendInventory();
              owner.setBusy(false);
            }
          });
          showBubble(player, item);
          break;
        case 269: // Dwarven Stout
          player.setBusy(true);
          player.getActionSender().sendMessage("You drink the " + item.getDef().getName() + ".");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              owner.getActionSender().sendMessage("It tastes foul.");
              for (int stat = 0; stat < 3; stat++) {
                owner.setCurStat(stat, owner.getCurStat(stat) - 4);
                owner.getActionSender().sendStat(stat);
              }
              if (owner.getCurStat(13) <= owner.getMaxStat(13)) {
                owner.setCurStat(13, owner.getCurStat(13) + 1);
                owner.getActionSender().sendStat(13);
              }
              if (owner.getCurStat(14) <= owner.getMaxStat(14)) {
                owner.setCurStat(14, owner.getCurStat(14) + 1);
                owner.getActionSender().sendStat(14);
              }
              owner.getInventory().remove(item);
              owner.getInventory().add(new InvItem(620));
              owner.getActionSender().sendInventory();
              owner.setBusy(false);
            }
          });
          showBubble(player, item);
          break;
        case 267: // Asgarnian Ale
          player.setBusy(true);
          player.getActionSender().sendMessage("You drink the " + item.getDef().getName() + ".");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              owner.getActionSender().sendMessage("You feel slightly reinvigorated");
              owner.getActionSender().sendMessage("And slightly dizzy too.");
              owner.setCurStat(0, owner.getCurStat(0) - 4);
              owner.getActionSender().sendStat(0);
              if (owner.getCurStat(2) <= owner.getMaxStat(2)) {
                owner.setCurStat(2, owner.getCurStat(2) + 2);
                owner.getActionSender().sendStat(2);
              }
              owner.getInventory().remove(item);
              owner.getInventory().add(new InvItem(620));
              owner.getActionSender().sendInventory();
              owner.setBusy(false);
            }
          });
          showBubble(player, item);
          break;
        case 829: // Dragon Bitter
          player.setBusy(true);
          player.getActionSender().sendMessage("You drink the " + item.getDef().getName() + ".");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              owner.getActionSender().sendMessage("You feel slightly dizzy.");
              owner.setCurStat(0, owner.getCurStat(0) - 4);
              owner.getActionSender().sendStat(0);
              if (owner.getCurStat(2) <= owner.getMaxStat(2)) {
                owner.setCurStat(2, owner.getCurStat(2) + 2);
                owner.getActionSender().sendStat(2);
              }
              owner.getInventory().remove(item);
              owner.getInventory().add(new InvItem(620));
              owner.getActionSender().sendInventory();
              owner.setBusy(false);
            }
          });
          showBubble(player, item);
          break;
        /** HANDLE WINE+ CURE POISON AND ANTIDOTE AND ZAMAROCK POTIONS **/
        case 221: // Strength Potion - 4 dose
          useNormalPotion(player, item, 2, 10, 2, 222, 3);
          break;
        case 222: // Strength Potion - 3 dose
          useNormalPotion(player, item, 2, 10, 2, 223, 2);
          break;
        case 223: // Strength Potion - 2 dose
          useNormalPotion(player, item, 2, 10, 2, 224, 1);
          break;
        case 224: // Strength Potion - 1 dose
          useNormalPotion(player, item, 2, 10, 2, 465, 0);
          break;
        case 474: // attack Potion - 3 dose
          useNormalPotion(player, item, 0, 10, 2, 475, 2);
          break;
        case 475: // attack Potion - 2 dose
          useNormalPotion(player, item, 0, 10, 2, 476, 1);
          break;
        case 476: // attack Potion - 1 dose
          useNormalPotion(player, item, 0, 10, 2, 465, 0);
          break;
        case 477: // stat restoration Potion - 3 dose
          useStatRestorePotion(player, item, 478, 2);
          break;
        case 478: // stat restoration Potion - 2 dose
          useStatRestorePotion(player, item, 479, 1);
          break;
        case 479: // stat restoration Potion - 1 dose
          useStatRestorePotion(player, item, 465, 0);
          break;
        case 480: // defense Potion - 3 dose
          useNormalPotion(player, item, 1, 10, 2, 481, 2);
          break;
        case 481: // defense Potion - 2 dose
          useNormalPotion(player, item, 1, 10, 2, 482, 1);
          break;
        case 482: // defense Potion - 1 dose
          useNormalPotion(player, item, 1, 10, 2, 465, 0);
          break;
        case 483: // restore prayer Potion - 3 dose
          usePrayerPotion(player, item, 484, 2);
          break;
        case 484: // restore prayer Potion - 2 dose
          usePrayerPotion(player, item, 485, 1);
          break;
        case 485: // restore prayer Potion - 1 dose
          usePrayerPotion(player, item, 465, 0);
          break;
        case 486: // Super attack Potion - 3 dose
          useNormalPotion(player, item, 0, 15, 4, 487, 2);
          break;
        case 487: // Super attack Potion - 2 dose
          useNormalPotion(player, item, 0, 15, 4, 488, 1);
          break;
        case 488: // Super attack Potion - 1 dose
          useNormalPotion(player, item, 0, 15, 4, 465, 0);
          break;
        case 489: // fishing Potion - 3 dose
          useFishingPotion(player, item, 490, 2);
          break;
        case 490: // fishing Potion - 2 dose
          useFishingPotion(player, item, 491, 1);
          break;
        case 491: // fishing Potion - 1 dose
          useFishingPotion(player, item, 465, 0);
          break;
        case 492: // Super strength Potion - 3 dose
          useNormalPotion(player, item, 2, 15, 4, 493, 2);
          break;
        case 493: // Super strength Potion - 2 dose
          useNormalPotion(player, item, 2, 15, 4, 494, 1);
          break;
        case 494: // Super strength Potion - 1 dose
          useNormalPotion(player, item, 2, 15, 4, 465, 0);
          break;
        case 495: // Super defense Potion - 3 dose
          useNormalPotion(player, item, 1, 15, 4, 496, 2);
          break;
        case 496: // Super defense Potion - 2 dose
          useNormalPotion(player, item, 1, 15, 4, 497, 1);
          break;
        case 497: // Super defense Potion - 1 dose
          useNormalPotion(player, item, 1, 15, 4, 465, 0);
          break;
        case 498: // ranging Potion - 3 dose
          useNormalPotion(player, item, 4, 10, 2, 499, 2);
          break;
        case 499: // ranging Potion - 2 dose
          useNormalPotion(player, item, 4, 10, 2, 500, 1);
          break;
        case 500: // ranging Potion - 1 dose
          useNormalPotion(player, item, 4, 10, 2, 465, 0);
          break;
        default:
          player.getActionSender().sendMessage("Nothing interesting happens.");
          return;
      }
    } else {
      switch (item.getID()) {
        case 597: // Charged Dragonstone amulet
          player.getActionSender().sendMessage("You rub the amulet...");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              String[] options = {
                "Edgeville", "Karamja", "Draynor Village", "Al Kharid", "Mage Arena", "Seers", "Yanille"
              };
              owner.setMenuHandler(new MenuHandler(options) {
                public void handleReply( int option, String reply ) {
                  if (owner.isBusy() || owner.getInventory().get(item) == null) {
                    return;
                  }
                  if (owner.getLocation().wildernessLevel() >= 30 || (owner.getLocation().inModRoom() && !owner.isMod
                    ())) {
                    owner.getActionSender().sendMessage("A magical force stops you from teleporting.");
                    return;
                  }
                  owner.getActionSender().sendSound("spellok");
                  switch (option) {
                    case 0: // Edgeville
                      owner.teleport(193, 435, true);
                      break;
                    case 1: // Karamja
                      owner.teleport(360, 696, true);
                      break;
                    case 2: // Draynor Village
                      owner.teleport(214, 632, true);
                      break;
                    case 3: // Al Kharid
                      owner.teleport(72, 696, true);
                      break;
                    case 4: // Mage Arena
                      owner.teleport(447, 3371, true);
                      break;
                    case 5: // Seers
                      owner.teleport(516, 460, true);
                      break;
                    case 6: // Yanille
                      owner.teleport(587, 761, true);
                      break;
                    default:
                      return;

                  }
                  if (DataConversions.random(0, 5) == 1 && owner.getInventory().remove(item) > -1) {
                    owner.getInventory().add(new InvItem(522, 1));
                    owner.getActionSender().sendInventory();
                  }
                }
              });
              owner.getActionSender().sendMenu(options);
            }
          });
          break;
        case 18: // Cabbage
          player.setBusy(true);
          player.getActionSender().sendMessage("You eat the " + item.getDef().getName() + ". Yuck!");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              int newHp = owner.getCurStat(3) + 1;
              if (newHp > owner.getMaxStat(3)) {
                newHp = owner.getMaxStat(3);
              }
              owner.setCurStat(3, newHp);
              owner.getInventory().remove(item);
              owner.getActionSender().sendStat(3);
              owner.getActionSender().sendInventory();
              owner.setBusy(false);
            }
          });
          showBubble(player, item);
          break;
        case 387: // Disk of Returning
          if (player.getLocation().wildernessLevel() >= 30 || (player.getLocation().inModRoom() && !player.isMod())) {
            player.getActionSender().sendMessage("The disk doesn't seem to work here.");
            return;
          }
          player.getActionSender().sendMessage("The disk starts to spin...");
          world.getDelayedEventHandler().add(new MiniEvent(player) {
            public void action() {
              owner.resetPath();
              owner.teleport(131, 508, true);
              owner.getInventory().remove(item);
              owner.getActionSender().sendMessage("You find yourself back in Varrock");
              owner.getActionSender().sendMessage("The disk has now gone");
              owner.getActionSender().sendInventory();
            }
          });
          break;
        case 1263: // Sleeping bag
          player.resetPath();
          player.getActionSender().sendMessage("You rest in the sleeping bag");
          switch (rand(0, 10)) {
            case 0:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@blu@Yellow", "@ora@Dark Red", "@gre@Orange", "@whi@Blue", "@cya@Cyan", "@red@Gray", "@mag@White"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 4) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;

            case 1:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@red@Purple", "@yel@White", "@ora@Orange", "@mag@Blue", "@cya@Green", "@gre@Rainbow", "@blu@Black"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 2) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;
            case 2:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@or3@Silver", "@yel@Black", "@ran@Purple", "@blu@Brown", "@whi@Green", "@mag@Pink", "@dre@White"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 5) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;
            case 3:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@cya@Brown", "@or2@White", "@red@Orange", "@mag@Purple", "@whi@Gold", "@gre@Rainbow", "@blu@Black"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 3) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;
            case 4:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@yel@Cyan", "@whi@Brown", "@dre@Black", "@ran@Yellow", "@blu@Blue", "@mag@Green", "@gre@Red"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 4) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;
            case 5:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@mag@red", "@or2@Grey", "@yel@Dark Blue", "@whi@White", "@ran@Silver", "@dre@Rainbow", "@blu@Black"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 3) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;
            case 6:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@blu@Black", "@cya@White", "@whi@Teal", "@yel@Red", "@gre@Green", "@ran@Yellow", "@mag@Rainbow"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 4) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;
            case 7:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@cya@Gold", "@or1@White", "@dre@Green", "@blu@Brown", "@red@Orange", "@gre@Rainbow", "@yel@Yellow"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 6) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;
            case 8:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@ora@Blue", "@dre@Yellow", "@ran@Brown", "@mag@Gold", "@gre@Purple", "@cya@Cyan", "@whi@Black"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 5) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;
            case 9:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@or2@Cyan", "@dre@Rainbow", "@cya@Red", "@blu@White", "@yel@Silver", "@gre@Yellow", "@mag@Magenta"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 6) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;

            case 10:
              world.getDelayedEventHandler().add(new ShortEvent(player) {
                public void action() {
                  String[] options = {
                    "@mag@Brown",
                    "@whi@Green",
                    "@ran@Yellow",
                    "@yel@Turquoise",
                    "@red@Red",
                    "@gre@Orange",
                    "@blu@Purple"
                  };
                  owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply( int option, String reply ) {
                      if (option == 4) {
                        owner.setFatigue(0);
                        owner.getActionSender().sendFatigue();
                        owner.getActionSender().sendMessage("You wake up - feeling refreshed");
                      } else {
                        owner.getActionSender().sendMessage("Wrong answer, please try again!");
                      }
                    }
                  });
                  owner.getActionSender().sendMenu(options);

                }

              });
              break;

          }
        default:
          player.getActionSender().sendMessage("Match the color with the correct color.");
          return;
      }
    }
  }

  private void showBubble( Player player, InvItem item ) {
    Bubble bubble = new Bubble(player, item.getID());
    for (Player p1 : player.getViewArea().getPlayersInView()) {
      p1.informOfBubble(bubble);
    }
  }

  private void useNormalPotion( Player player, InvItem item, int affectedStat, int percentageIncrease, int modifier,
                                int newItem, int left ) {
    player.setBusy(true);
    player.getActionSender().sendMessage("You drink some of your " + item.getDef().getName() + ".");
    world.getDelayedEventHandler().add(new MiniEvent(player) {
      public void action() {
        owner.getActionSender().sendMessage("You have " + left + " doses left.");
        int baseStat = owner.getCurStat(affectedStat) > owner.getMaxStat(affectedStat) ? owner.getMaxStat
          (affectedStat) : owner.getCurStat(affectedStat);
        int newStat = baseStat + DataConversions.roundUp((owner.getMaxStat(affectedStat) / 100D) *
          percentageIncrease) + modifier;
        if (newStat > owner.getCurStat(affectedStat)) {
          owner.setCurStat(affectedStat, newStat);
          owner.getActionSender().sendStat(affectedStat);
        }
        owner.getInventory().remove(item);
        owner.getInventory().add(new InvItem(newItem));
        owner.getActionSender().sendInventory();
        owner.setBusy(false);
      }
    });
  }

  private void useStatRestorePotion( Player player, InvItem item, int newItem, int left ) {
    player.setBusy(true);
    player.getActionSender().sendMessage("You drink some of your " + item.getDef().getName() + ".");
    world.getDelayedEventHandler().add(new MiniEvent(player) {
      public void action() {
        owner.getActionSender().sendMessage("You have " + left + " doses left.");
        for (int i = 0; i < 18; i++) {
          if (i == 3 || i == 5) {
            continue;
          }
          int max = owner.getMaxStat(i);
          if (owner.getCurStat(i) < max) {
            owner.setCurStat(i, max);
            owner.getActionSender().sendStat(i);
          }
        }
        owner.getInventory().remove(item);
        owner.getInventory().add(new InvItem(newItem));
        owner.getActionSender().sendInventory();
        owner.setBusy(false);
      }
    });
  }

  private void usePrayerPotion( Player player, InvItem item, int newItem, int left ) {
    player.setBusy(true);
    player.getActionSender().sendMessage("You drink some of your " + item.getDef().getName() + ".");
    world.getDelayedEventHandler().add(new MiniEvent(player) {
      public void action() {
        owner.getActionSender().sendMessage("You have " + left + " doses left.");
        int newPrayer = owner.getCurStat(5) + 21;
        if (newPrayer > owner.getMaxStat(5)) {
          newPrayer = owner.getMaxStat(5);
        }
        owner.setCurStat(5, newPrayer);
        owner.getInventory().remove(item);
        owner.getInventory().add(new InvItem(newItem));
        owner.getActionSender().sendStat(5);
        owner.getActionSender().sendInventory();
        owner.setBusy(false);
      }
    });
  }

  private void useFishingPotion( Player player, InvItem item, int newItem, int left ) {
    player.setBusy(true);
    player.getActionSender().sendMessage("You drink some of your " + item.getDef().getName() + ".");
    world.getDelayedEventHandler().add(new MiniEvent(player) {
      public void action() {
        owner.getActionSender().sendMessage("You have " + left + " doses left.");
        owner.setCurStat(10, owner.getMaxStat(10) + 3);
        owner.getInventory().remove(item);
        owner.getInventory().add(new InvItem(newItem));
        owner.getActionSender().sendStat(10);
        owner.getActionSender().sendInventory();
        owner.setBusy(false);
      }
    });
  }
}