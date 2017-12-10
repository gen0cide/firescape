package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for items
 */
public class ItemBowStringDef extends EntityDef {

  /**
   * The level required to attach this bow string
   */
  public int requiredLvl;
  /**
   * The exp given by attaching this bow string
   */
  public int exp;
  /**
   * The ID of the bow created
   */
  public int bowID;

  public int getBowID() {
    return bowID;
  }

  public int getReqLevel() {
    return requiredLvl;
  }

  public int getExp() {
    return exp;
  }
}
