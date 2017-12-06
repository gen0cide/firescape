package org.firescape.server;

import org.firescape.server.event.DelayedEvent;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;

import java.util.ArrayList;
import java.util.Iterator;

public final class DelayedEventHandler {
  private static final World world = World.getWorld();
  private final ArrayList<DelayedEvent> toAdd = new ArrayList<DelayedEvent>();
  private final ArrayList<DelayedEvent> events = new ArrayList<DelayedEvent>();

  public DelayedEventHandler() {
    world.setDelayedEventHandler(this);
  }

  public boolean contains(DelayedEvent event) {
    return events.contains(event);
  }

  public ArrayList<DelayedEvent> getEvents() {
    return events;
  }

  public void add(DelayedEvent event) {
    if (!events.contains(event)) {
      toAdd.add(event);
    }
  }

  public void remove(DelayedEvent event) {
    events.remove(event);
  }

  public void removePlayersEvents(Player player) {
    Iterator<DelayedEvent> iterator = events.iterator();
    while (iterator.hasNext()) {
      DelayedEvent event = iterator.next();
      if (event.belongsTo(player)) {
        iterator.remove();
      }
    }
  }

  public void doEvents() {
    if (toAdd.size() > 0) {
      events.addAll(toAdd);
      toAdd.clear();
    }
    Iterator<DelayedEvent> iterator = events.iterator();
    while (iterator.hasNext()) {
      DelayedEvent event = iterator.next();
      if (event.shouldRun()) {
        event.run();
        event.updateLastRun();
      }
      if (event.shouldRemove()) {
        iterator.remove();
      }
    }
  }

}