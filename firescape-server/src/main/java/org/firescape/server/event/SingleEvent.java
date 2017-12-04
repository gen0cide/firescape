package org.firescape.server.event;

import org.firescape.server.model.Player;

public abstract class SingleEvent extends DelayedEvent {

  public SingleEvent(Player owner, int delay) {
    super(owner, delay);
  }

  public void run() {
    action();
    this.running = false;
  }

  public abstract void action();

}