package org.firescape.server.event;

import org.firescape.server.model.Npc;
import org.firescape.server.model.Path;
import org.firescape.server.model.Player;

public abstract class NpcWalkEvent extends DelayedEvent {
  private final int radius;
  protected Npc affectedMob;

  public NpcWalkEvent(Player owner, Npc affectedMob, int radius) {
    super(owner, 500);
    this.affectedMob = affectedMob;
    this.radius = radius;
    affectedMob.moveNpc(new Path(affectedMob.getX(),
                                 affectedMob.getY(),
                                 owner.getLocation().getX(),
                                 owner.getLocation().getY()
    ));
    if (affectedMob.withinRange(owner, radius)) {
      arrived();
      this.running = false;
    }
  }

  public abstract void arrived();

  public final void run() {
    if (owner.withinRange(affectedMob, radius)) {
      arrived();
    } else if (owner.hasMoved()) {
      return; // We're still moving
    } else {
      failed();
    }
    this.running = false;
  }

  public void failed() {
  } // Not abstract as isn't required

  public Npc getAffectedMob() {
    return affectedMob;
  }

}