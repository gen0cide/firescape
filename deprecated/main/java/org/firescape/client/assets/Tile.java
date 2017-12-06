package org.firescape.client.assets;

public class Tile {
  public int id;
  public int decoration;
  public int type;
  public int adjacency;

  public Tile(int i, int d, int t, int a) {
    this.id = i;
    this.decoration = d;
    this.type = t;
    this.adjacency = a;
  }
}
