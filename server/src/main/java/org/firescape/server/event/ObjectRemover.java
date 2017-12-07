package org.firescape.server.event;

import org.firescape.server.model.ActiveTile;
import org.firescape.server.model.GameObject;
import org.firescape.server.model.World;

public class ObjectRemover extends DelayedEvent {
  public static final World world = World.getWorld();
  private final GameObject object;

  public ObjectRemover(GameObject object, int delay) {
    super(null, delay);
    this.object = object;
  }

  public void run() {
    ActiveTile tile = world.getTile(object.getLocation());
    if (!tile.hasGameObject() || !tile.getGameObject().equals(object)) {
      this.running = false;
      return;
    }
    tile.remove(object);
    world.unregisterGameObject(object);
    this.running = false;
  }

  public boolean equals(Object o) {
    if (o instanceof ObjectRemover) {
      return ((ObjectRemover) o).getObject().equals(getObject());
    }
    return false;
  }

  public GameObject getObject() {
    return object;
  }

}