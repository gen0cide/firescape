package org.firescape.server.event;

import org.firescape.server.model.Player;
import org.firescape.server.model.Point;

public abstract class WalkToPointEvent extends DelayedEvent {
  private final int radius;
  private final boolean stop;
  protected Point location;

  public WalkToPointEvent( Player owner, Point location, int radius, boolean stop ) {
    super(owner, 500);
    this.location = location;
    this.radius = radius;
    this.stop = stop;
    if (stop && owner.withinRange(location, radius)) {
      owner.resetPath();
      arrived();
      this.running = false;
    }
  }

  public abstract void arrived();

  public final void run() {
    if (stop && owner.withinRange(location, radius)) {
      owner.resetPath();
      arrived();
    } else if (owner.hasMoved()) {
      return; // We're still moving
    } else if (owner.withinRange(location, radius)) {
      arrived();
    }
    this.running = false;
  }

  public Point getLocation() {
    return location;
  }

}