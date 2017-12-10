package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for items
 */
public class ItemHerbDef extends EntityDef {

  /**
   * The exp smelting this item gives
   */
  public int exp;
  /**
   * The id of the related potion
   */
  public int potionId;
  /**
   * The level required to make this
   */
  public int requiredLvl;

  public int getExp() {
    return exp;
  }

  public int getPotionId() {
    return potionId;
  }

  public int getReqLevel() {
    return requiredLvl;
  }

}
