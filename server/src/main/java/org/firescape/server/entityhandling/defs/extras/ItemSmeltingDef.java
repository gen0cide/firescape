package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for items
 */
public class ItemSmeltingDef extends EntityDef {

  /**
   * The exp smelting this item gives
   */
  public int exp;
  /**
   * The id of the related bar
   */
  public int barId;
  /**
   * The level required to smelt this
   */
  public int requiredLvl;
  /**
   * The ores required in addition to this one
   */
  public ReqOreDef[] reqOres;

  public int getExp() {
    return exp;
  }

  public int getBarId() {
    return barId;
  }

  public int getReqLevel() {
    return requiredLvl;
  }

  public ReqOreDef[] getReqOres() {
    return reqOres;
  }

}
