package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for trees
 */
public class ObjectWoodcuttingDef extends EntityDef {

  /**
   * Herblaw level required to identify
   */
  public int requiredLvl;
  /**
   * How much experience identifying gives
   */
  public int exp;
  /**
   * Percent chance the tree will be felled
   */
  public int fell;
  /**
   * How long the tree takes to respawn afterwards
   */
  public int respawnTime;
  /**
   * The id of the ore this turns into
   */
  private int logId;

  public int getExp() {
    return exp;
  }

  public int getLogId() {
    return logId;
  }

  public int getReqLevel() {
    return requiredLvl;
  }

  public int getFell() {
    return fell;
  }

  public int getRespawnTime() {
    return respawnTime;
  }

}
