package org.firescape.server.npchandler;

import org.firescape.server.model.ChatMessage;
import org.firescape.server.model.Npc;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;

public class CowSay implements NpcHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handleNpc(Npc npc, Player player) throws Exception {
    player.informOfNpcMessage(new ChatMessage(npc, "Moooooo", player));
  }

}