package org.firescape.client.assets;

public class Animation {
  public int id;
  public String name;
  public int color;
  public int meta;
  public int hasA;
  public int hasF;
  public int number;

  public Animation(int id, String name, int color, int meta, int hasA, int hasF, int number) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.meta = meta;
    this.hasA = hasA;
    this.hasF = hasF;
    this.number = number;
  }
}
