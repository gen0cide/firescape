package org.firescape.server.entityhandling.defs.extras;

/**
 * The definition wrapper for herbs
 */
public class ItemUnIdentHerbDef {

  /**
   * Herblaw level required to identify
   */
  public int requiredLvl;
  /**
   * How much experience identifying gives
   */
  public int exp;
  /**
   * The id of the herb this turns into
   */
  private int newId;

  public int getExp() {
    return exp;
  }

  public int getNewId() {
    return newId;
  }

  public int getLevelRequired() {
    return requiredLvl;
  }

}
