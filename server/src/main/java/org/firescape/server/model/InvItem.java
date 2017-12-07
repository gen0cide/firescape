package org.firescape.server.model;

import org.firescape.server.entityhandling.EntityHandler;
import org.firescape.server.entityhandling.defs.ItemDef;
import org.firescape.server.entityhandling.defs.extras.ItemCookingDef;
import org.firescape.server.entityhandling.defs.extras.ItemSmeltingDef;
import org.firescape.server.entityhandling.defs.extras.ItemUnIdentHerbDef;
import org.firescape.server.entityhandling.defs.extras.ItemWieldableDef;

import java.io.Serializable;

public class InvItem extends Entity implements Comparable<InvItem>, Serializable {

  private int amount;
  private boolean wielded;

  public InvItem(int id) {
    setID(id);
    setAmount(1);
  }

  public InvItem(int id, int amount) {
    setID(id);
    setAmount(amount);
  }

  public ItemSmeltingDef getSmeltingDef() {
    return EntityHandler.getItemSmeltingDef(id);
  }

  public ItemCookingDef getCookingDef() {
    return EntityHandler.getItemCookingDef(id);
  }

  public ItemUnIdentHerbDef getUnIdentHerbDef() {
    return EntityHandler.getItemUnIdentHerbDef(id);
  }

  public boolean isWielded() {
    return wielded;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    if (amount < 0) {
      amount = 0;
    }
    this.amount = amount;
  }

  public void setWield(boolean wielded) {
    this.wielded = wielded;
  }

  public boolean wieldingAffectsItem(InvItem i) {
    if (!i.isWieldable() || !isWieldable()) {
      return false;
    }
    for (int affected : getWieldableDef().getAffectedTypes()) {
      if (i.getWieldableDef().getType() == affected) {
        return true;
      }
    }
    return false;
  }

  public boolean isWieldable() {
    return EntityHandler.getItemWieldableDef(id) != null;
  }

  public ItemWieldableDef getWieldableDef() {
    return EntityHandler.getItemWieldableDef(id);
  }

  public int eatingHeals() {
    if (!isEdible()) {
      return 0;
    }
    return EntityHandler.getItemEdibleHeals(id);
  }

  public boolean isEdible() {
    return EntityHandler.getItemEdibleHeals(id) > 0;
  }

  public boolean equals(Object o) {
    if (o instanceof InvItem) {
      InvItem item = (InvItem) o;
      return item.getID() == getID();
    }
    return false;
  }

  public int compareTo(InvItem item) {
    if (item.getDef().isStackable()) {
      return -1;
    }
    if (getDef().isStackable()) {
      return 1;
    }
    return item.getDef().getBasePrice() - getDef().getBasePrice();
  }

  public ItemDef getDef() {
    return EntityHandler.getItemDef(id);
  }

}
