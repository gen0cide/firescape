package org.firescape.server.entityhandling.defs;

/**
 * The abstract class EntityDef implements methods for return values which are shared between entities.
 */
public abstract class EntityDef {

  public int id;

  /**
   * The name of the entity
   */
  public String name;
  /**
   * The description of the entity
   */
  public String description;

  /**
   * Returns the name of the entity
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the description of the entity
   */
  public String getDescription() {
    return description;
  }
}
