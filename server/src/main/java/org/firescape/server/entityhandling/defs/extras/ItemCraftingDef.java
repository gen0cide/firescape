package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

public class ItemCraftingDef extends EntityDef {

  /**
   * The crafting level required to make this item
   */
  public int requiredLvl;
  /**
   * The ID of the item produced
   */
  public int itemID;
  /**
   * The exp given
   */
  public int exp;

  public int getReqLevel() {
    return requiredLvl;
  }

  public int getItemID() {
    return itemID;
  }

  public int getExp() {
    return exp;
  }
}