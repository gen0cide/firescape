package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for rocks
 */
public class ObjectMiningDef extends EntityDef {

  /**
   * Herblaw level required to identify
   */
  public int requiredLvl;
  /**
   * How much experience identifying gives
   */
  public int exp;
  /**
   * How long the rock takes to respawn afterwards
   */
  public int respawnTime;
  /**
   * The id of the ore this turns into
   */
  private int oreId;

  public int getExp() {
    return exp;
  }

  public int getOreId() {
    return oreId;
  }

  public int getReqLevel() {
    return requiredLvl;
  }

  public int getRespawnTime() {
    return respawnTime;
  }

}
