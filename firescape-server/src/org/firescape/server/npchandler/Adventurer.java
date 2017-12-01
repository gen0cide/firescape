package org.firescape.server.npchandler;

import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.ChatMessage;
import org.firescape.server.model.MenuHandler;
import org.firescape.server.model.Npc;
import org.firescape.server.model.Player;
import org.firescape.server.model.Point;
import org.firescape.server.model.World;

public class Adventurer implements NpcHandler {
  /*
   * castle 271 353 Dragon maze 268 197 Mage Arena 447 3371 --- 445 3373 (NPC)
   * Draynor 214 632 --- 220 637 (NPC) Ardy 550 595 Lost City 130 3545 Varrock
   * 130 508 Watch Tower Spawn 631 739 and 636 2624 Edge 216 452 Fally 313 541
   * and 316 540
   */

  public static final World world = World.getWorld();

  private static final String[] destinationNames = { "Edgeville", "Varrock", "Falador", "Ardougne", "*Castle*",
      "*Dragon Maze*", "*Mage Arena*" };
  private static final Point[] destinationCoords = { Point.location(216, 452), Point.location(130, 508),
      Point.location(313, 541), Point.location(550, 596), Point.location(271, 353), Point.location(268, 197),
      Point.location(447, 3371) };

  public void handleNpc(final Npc npc, Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Where would you like to be sent?", player));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.setBusy(false);
        owner.setMenuHandler(new MenuHandler(destinationNames) {
          public void handleReply(final int option, final String reply) {
            if (owner.isBusy() || option < 0 || option >= destinationNames.length) {
              npc.unblock();
              return;
            }
            // owner.informOfChatMessage(new ChatMessage(owner, reply + ".",
            // npc));
            owner.setBusy(true);
            world.getDelayedEventHandler().add(new ShortEvent(owner) {
              public void action() {
                world.getDelayedEventHandler().add(new ShortEvent(owner) {
                  public void action() {
                    Point p = destinationCoords[option];
                    owner.teleport(p.getX(), p.getY(), false);
                    owner.setBusy(false);
                    npc.unblock();
                  }
                });
              }
            });
          }
        });
        owner.getActionSender().sendMenu(destinationNames);
      }
    });
    npc.blockedBy(player);
  }

}