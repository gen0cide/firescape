package org.firescape.server.entityhandling.defs;

import org.firescape.server.entityhandling.defs.extras.ItemDropDef;
import org.firescape.server.model.Item;
import org.firescape.server.model.World;
import org.firescape.server.util.DataConversions;

/**
 * The definition wrapper for npcs
 */
public class NPCDef extends EntityDef {

  /**
   * The primary command
   */
  public String command;
  /**
   * Sprites used to make up this npc
   */
  public int[] sprites;
  /**
   * Colour of our hair
   */
  public int hairColour;
  /**
   * Colour of our top
   */
  public int topColour;
  /**
   * Colour of our legs
   */
  public int bottomColour;
  /**
   * Skin colour
   */
  public int skinColour;
  /**
   * Something to do with the camera
   */
  public int camera1, camera2;
  /**
   * Something to do with models
   */
  public int walkModel, combatModel, combatSprite;
  /**
   * The hit points
   */
  public int hits;
  /**
   * The attack lvl
   */
  public int attack;
  /**
   * The def lvl
   */
  public int defense;
  /**
   * The strength lvl
   */
  public int strength;
  /**
   * Whether the npc is attackable
   */
  public boolean attackable;
  /**
   * How long the npc takes to respawn
   */
  public int respawnTime;
  /**
   * Whether the npc is aggressive
   */
  public boolean aggressive;
  /**
   * Possible drops
   */
  public ItemDropDef[] drops;

  public ItemDropDef[] getDrops() {
    return drops;
  }

  public ItemDropDef getNextDrop() {
    int total = 0;
    for (ItemDropDef drop : drops) {
      total += drop.getWeight();
    }
    int hit = DataConversions.random(0, total);
    total = 0;
    for (ItemDropDef drop : drops) {
      if (drop.getWeight() == 0) {
        return drop;
      }
      if (hit >= total && hit < (total + drop.getWeight())) {
        return drop;
      }
      total += drop.getWeight();
    }
    return drops[0];
  }

  public String getCommand() {
    return command;
  }

  public int getSprite(int index) {
    return sprites[index];
  }

  public int getHairColour() {
    return hairColour;
  }

  public int getTopColour() {
    return topColour;
  }

  public int getBottomColour() {
    return bottomColour;
  }

  public int getSkinColour() {
    return skinColour;
  }

  public int getCamera1() {
    return camera1;
  }

  public int getCamera2() {
    return camera2;
  }

  public int getWalkModel() {
    return walkModel;
  }

  public int getCombatModel() {
    return combatModel;
  }

  public int getCombatSprite() {
    return combatSprite;
  }

  public int getHits() {
    return hits;
  }

  public int getAtt() {
    return attack;
  }

  public int getDef() {
    return defense;
  }

  public int getStr() {
    return strength;
  }

  public int[] getStats() {
    return new int[] {
      attack, defense, strength
    };
  }

  public boolean isAttackable() {
    return attackable;
  }

  public int respawnTime() {
    return respawnTime;
  }

  public boolean isAggressive() {
    return attackable && aggressive;
  }
}
