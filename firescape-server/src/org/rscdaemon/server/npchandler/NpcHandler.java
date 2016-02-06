package org.rscdaemon.server.npchandler;

import org.rscdaemon.server.model.Npc;
import org.rscdaemon.server.model.Player;

public interface NpcHandler {
  public void handleNpc(final Npc npc, Player player) throws Exception;
}
