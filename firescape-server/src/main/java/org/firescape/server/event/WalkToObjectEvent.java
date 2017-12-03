package org.firescape.server.event;

import org.firescape.server.model.GameObject;
import org.firescape.server.model.Player;

public abstract class WalkToObjectEvent extends DelayedEvent {
  private final boolean stop;
  protected GameObject object;

  public WalkToObjectEvent( Player owner, GameObject object, boolean stop ) {
    super(owner, 500);
    this.object = object;
    this.stop = stop;
    if (stop && owner.atObject(object)) {
      owner.resetPath();
      arrived();
      this.running = false;
    }
  }

  public abstract void arrived();

  public final void run() {
    if (stop && owner.atObject(object)) {
      owner.resetPath();
      arrived();
    } else if (owner.hasMoved()) {
      return; // We're still moving
    } else if (owner.atObject(object)) {
      arrived();
    }
    this.running = false;
  }

}