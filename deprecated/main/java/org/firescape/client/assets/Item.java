package org.firescape.client.assets;

public class Item {
  public int id;
  public String name;
  public String description;
  public String command;
  public int picture;
  public int basePrice;
  public int stackable;
  public int unused;
  public int wearable;
  public int mask;
  public int special;
  public int members;

  public Item(int id, String name, String description, String command, int picture, int basePrice, int stackable, int unused, int wearable, int mask, int special, int members) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.command = command;
    this.picture = picture;
    this.basePrice = basePrice;
    this.stackable = stackable;
    this.unused = unused;
    this.wearable = wearable;
    this.mask = mask;
    this.special = special;
    this.members = members;
  }
}
