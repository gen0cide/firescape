package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for items
 */
public class ItemGemDef extends EntityDef {

  /**
   * The level required to attach this bow string
   */
  public int requiredLvl;
  /**
   * The exp given by attaching this bow string
   */
  public int exp;
  /**
   * The ID of the gem
   */
  public int gemID;

  public int getGemID() {
    return gemID;
  }

  public int getReqLevel() {
    return requiredLvl;
  }

  public int getExp() {
    return exp;
  }
}
