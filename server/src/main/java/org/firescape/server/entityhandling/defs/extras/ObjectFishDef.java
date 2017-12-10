package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for fish
 */
public class ObjectFishDef extends EntityDef {

  /**
   * The id of the fish
   */
  public int fishId;
  /**
   * The fishing level required to fish
   */
  public int requiredLevel;
  /**
   * How much experience this fish should give
   */
  public int exp;

  public int getId() {
    return fishId;
  }

  public int getReqLevel() {
    return requiredLevel;
  }

  public int getExp() {
    return exp;
  }

}
