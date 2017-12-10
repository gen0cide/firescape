package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for items
 */
public class ItemArrowHeadDef extends EntityDef {

  /**
   * The level required to attach this head to an arrow
   */
  public int requiredLvl;
  /**
   * The exp given by attaching this arrow head
   */
  public double exp;
  /**
   * The ID of the arrow created
   */
  public int arrowID;

  public int getArrowID() {
    return arrowID;
  }

  public int getReqLevel() {
    return requiredLvl;
  }

  public double getExp() {
    return exp;
  }
}
