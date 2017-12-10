package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

public class FiremakingDef extends EntityDef {

  /**
   * The firemaking level required to light these logs
   */
  public int level;
  /**
   * The exp given by these logs
   */
  public int exp;
  /**
   * How many ms the fire should last for
   */
  public int length;

  public int getRequiredLevel() {
    return level;
  }

  public int getExp() {
    return exp;
  }

  public int getLength() {
    return length * 1000;
  }
}