package org.firescape.client.assets;

public class WallObject {
  public int id;
  public String name;
  public String description;
  public String command1;
  public String command2;
  public int height;
  public int textureFront;
  public int textureBack;
  public int adjacency;
  public int invisible;

  public WallObject(
    int i,
    String n,
    String d,
    String c1,
    String c2,
    int h,
    int tf,
    int tb,
    int a,
    int inv
  ) {
    this.id = i;
    this.name = n;
    this.description = d;
    this.command1 = c1;
    this.command2 = c2;
    this.height = h;
    this.textureFront = tf;
    this.textureBack = tb;
    this.adjacency = a;
    this.invisible = inv;
  }
}
