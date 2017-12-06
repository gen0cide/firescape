package org.firescape.server.npchandler;

import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.ShortEvent;
import org.firescape.server.model.ChatMessage;
import org.firescape.server.model.Npc;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;

public class Thrander implements NpcHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(Npc npc, Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc,
                                              "Hello i'm thrander the smith, I'm an expert in armour " + "modification",
                                              player
    ));
    player.setBusy(true);
    world.getDelayedEventHandler().add(new ShortEvent(player) {
      public void action() {
        owner.informOfNpcMessage(new ChatMessage(npc,
                                                 "Give me your armour designed for men and I can convert " + "it",
                                                 owner
        ));
        DelayedEvent.world.getDelayedEventHandler().add(new ShortEvent(owner) {
          public void action() {
            owner.setBusy(false);
            owner.informOfNpcMessage(new ChatMessage(npc,
                                                     "Into something more comfortable for a woman, and vice" +
                                                     "" +
                                                     " versa",
                                                     owner
            ));
            npc.unblock();
          }
        });
      }
    });
    npc.blockedBy(player);
  }

}