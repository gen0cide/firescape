package org.firescape.server.event;

import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.util.Logger;

public class SaveEvent {

  public static final World world = World.getWorld();

  public static void saveAll() {
    long cur = System.currentTimeMillis();
    for (Player p : world.getPlayers()) {
      p.save();
    }
    cur = System.currentTimeMillis() - cur;
    Logger.print(world.getPlayers().count() + " Players saved in " + cur + "ms", 3);
  }

}
