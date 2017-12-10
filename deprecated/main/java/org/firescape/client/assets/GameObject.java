package org.firescape.client.assets;

public class GameObject {

  public int id;
  public String name;
  public String description;
  public String command1;
  public String command2;
  public int modelIndex;
  public int width;
  public int height;
  public int type;
  public int elevation;

  public GameObject(int id, String name, String description, String command1, String command2, int modelIndex, int width, int height, int type, int elevation) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.command1 = command1;
    this.command2 = command2;
    this.modelIndex = modelIndex;
    this.width = width;
    this.height = height;
    this.type = type;
    this.elevation = elevation;
  }
}
