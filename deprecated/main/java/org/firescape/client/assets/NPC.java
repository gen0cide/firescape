package org.firescape.client.assets;

public class NPC {

  public int id;
  public String name;
  public String description;
  public String command;
  public int attack;
  public int strength;
  public int hits;
  public int defense;
  public int attackable;
  public int[] spriteIDs;
  public int hairColor;
  public int topColor;
  public int bottomColor;
  public int skinColor;
  public int width;
  public int height;
  public int walkModel;
  public int combatModel;
  public int combatAnimation;

  public NPC(int id, String name, String description, String command, int attack, int strength, int hits, int defense, int attackable, int[] spriteIDs, int hairColor, int topColor, int bottomColor, int skinColor, int width, int height, int walkModel, int combatModel, int combatAnimation) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.command = command;
    this.attack = attack;
    this.strength = strength;
    this.hits = hits;
    this.defense = defense;
    this.attackable = attackable;
    this.spriteIDs = spriteIDs;
    this.hairColor = hairColor;
    this.topColor = topColor;
    this.bottomColor = bottomColor;
    this.skinColor = skinColor;
    this.width = width;
    this.height = height;
    this.walkModel = walkModel;
    this.combatModel = combatModel;
    this.combatAnimation = combatAnimation;
  }
}
