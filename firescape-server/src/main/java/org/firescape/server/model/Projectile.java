package org.firescape.server.model;

public class Projectile {
  /**
   * Who fired the projectile
   */
  private final Mob caster;
  /**
   * Who the projectile is being fired at
   */
  private final Mob victim;
  /**
   * The type: 1 = magic, 2 = ranged
   */
  private final int type;

  public Projectile( Mob caster, Mob victim, int type ) {
    this.caster = caster;
    this.victim = victim;
    this.type = type;
  }

  public Mob getCaster() {
    return caster;
  }

  public Mob getVictim() {
    return victim;
  }

  public int getType() {
    return type;
  }

}
