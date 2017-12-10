package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

public class ItemSmithingDef extends EntityDef {

  /**
   * The smithing level required to make this item
   */
  public int level;
  /**
   * How many bars are required
   */
  public int bars;
  /**
   * The ID of the item produced
   */
  public int itemID;
  /**
   * The amount of the item produced
   */
  public int amount;

  public int getRequiredLevel() {
    return level;
  }

  public int getRequiredBars() {
    return bars;
  }

  public int getItemID() {
    return itemID;
  }

  public int getAmount() {
    return amount;
  }
}