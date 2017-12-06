package org.firescape.server.model;

import org.firescape.server.entityhandling.EntityHandler;
import org.firescape.server.entityhandling.defs.ItemDef;
import org.firescape.server.entityhandling.locs.ItemLoc;
import org.firescape.server.event.DelayedEvent;

public class Item extends Entity {
  /**
   * World instance
   */
  private static final World world = World.getWorld();
  /**
   * The time that the item was spawned
   */
  private final long spawnedTime;
  /**
   * Contains the player that the item belongs to, if any
   */
  private Player owner;
  /**
   * Amount (for stackables)
   */
  private int amount;
  /**
   * Set when the item has been destroyed to alert players
   */
  private boolean removed;
  /**
   * Location definition of the item
   */
  private ItemLoc loc;

  public Item(ItemLoc loc) {
    this.loc = loc;
    setID(loc.id);
    setAmount(loc.amount);
    spawnedTime = System.currentTimeMillis();
    setLocation(Point.location(loc.x, loc.y));
  }

  public ItemDef getDef() {
    return EntityHandler.getItemDef(id);
  }

  public Item(int id, int x, int y, int amount, Player owner) {
    setID(id);
    setAmount(amount);
    this.owner = owner;
    spawnedTime = System.currentTimeMillis();
    setLocation(Point.location(x, y));
  }

  public ItemLoc getLoc() {
    return loc;
  }

  public boolean isRemoved() {
    return removed;
  }

  public boolean visibleTo(Player p) {
    if (owner == null || p.equals(owner)) {
      return true;
    }
    return System.currentTimeMillis() - spawnedTime > 60000;
  }

  public void remove() {
    if (!removed && loc != null && loc.getRespawnTime() > 0) {
      world.getDelayedEventHandler().add(new DelayedEvent(null, loc.getRespawnTime() * 1000) {
        public void run() {
          DelayedEvent.world.registerItem(new Item(loc));
          running = false;
        }
      });
    }
    removed = true;
  }

  public boolean equals(Object o) {
    if (o instanceof Item) {
      Item item = (Item) o;
      return item.getID() == getID() &&
             item.getAmount() == getAmount() &&
             item.getSpawnedTime() == getSpawnedTime() &&
             (item.getOwner() == null || item.getOwner().equals(getOwner())) &&
             item.getLocation().equals(getLocation());
    }
    return false;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    if (getDef().isStackable()) {
      this.amount = amount;
    } else {
      this.amount = 1;
    }
  }

  public long getSpawnedTime() {
    return spawnedTime;
  }

  public Player getOwner() {
    return owner;
  }

  public boolean isOn(int x, int y) {
    return x == getX() && y == getY();
  }

}
