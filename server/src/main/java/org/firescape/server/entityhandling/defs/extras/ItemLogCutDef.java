package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

/**
 * The definition wrapper for items
 */
public class ItemLogCutDef extends EntityDef {

  public int shortbowID;
  public int shortbowLvl;
  public int shortbowExp;

  public int longbowID;
  public int longbowLvl;
  public int longbowExp;

  public int shaftAmount;
  public int shaftLvl;

  public int getShaftAmount() {
    return shaftAmount;
  }

  public int getShaftLvl() {
    return shaftLvl;
  }

  public int getShaftExp() {
    return shaftAmount;
  }

  public int getShortbowID() {
    return shortbowID;
  }

  public int getShortbowLvl() {
    return shortbowLvl;
  }

  public int getShortbowExp() {
    return shortbowExp;
  }

  public int getLongbowID() {
    return longbowID;
  }

  public int getLongbowLvl() {
    return longbowLvl;
  }

  public int getLongbowExp() {
    return longbowExp;
  }

}
