package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for ores
 */
public class ReqOreDef extends EntityDef {

  /**
   * The id of the ore
   */
  public int oreId;
  /**
   * The amount of the ore required
   */
  public int amount;

  public int getId() {
    return oreId;
  }

  public int getAmount() {
    return amount;
  }

}
