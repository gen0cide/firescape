package org.firescape.client.assets;

public class Spell {
  public int id;
  public String name;
  public String description;
  public int level;
  public int totalRequiredRuneCount;
  public int type;
  public int[] requiredRuneIDs;
  public int[] requiredRuneCounts;

  public Spell(int i, String n, String d, int l, int rrc, int t, int[] rrid, int[] rrcs) {
    this.id = i;
    this.name = n;
    this.description = d;
    this.level = l;
    this.totalRequiredRuneCount = rrc;
    this.type = t;
    this.requiredRuneIDs = rrid;
    this.requiredRuneCounts = rrcs;
  }
}
