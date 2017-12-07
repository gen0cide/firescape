package org.firescape.server.npchandler;

import org.firescape.server.model.Npc;
import org.firescape.server.model.Player;

public interface NpcHandler {

  void handleNpc(Npc npc, Player player) throws Exception;
}
