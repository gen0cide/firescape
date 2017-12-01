package org.firescape.server.npchandler;

import org.firescape.server.model.Npc;
import org.firescape.server.model.Player;

public interface NpcHandler {
  public void handleNpc(final Npc npc, Player player) throws Exception;
}
