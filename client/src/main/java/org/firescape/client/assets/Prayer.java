package org.firescape.client.assets;

public class Prayer {

  public int id;
  public String name;
  public String description;
  public int level;
  public int drainRate;

  public Prayer(int i, String n, String d, int l, int dr) {
    this.id = i;
    this.name = n;
    this.description = d;
    this.level = l;
    this.drainRate = dr;
  }
}
