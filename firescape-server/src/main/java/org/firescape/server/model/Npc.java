package org.firescape.server.model;

import org.firescape.server.entityhandling.EntityHandler;
import org.firescape.server.entityhandling.defs.NPCDef;
import org.firescape.server.entityhandling.defs.extras.ItemDropDef;
import org.firescape.server.entityhandling.locs.NPCLoc;
import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.FightEvent;
import org.firescape.server.states.Action;
import org.firescape.server.states.CombatState;
import org.firescape.server.util.DataConversions;
import org.firescape.server.util.Formulae;

import java.util.Random;

public class Npc extends Mob {

  /**
   * World instance
   */
  private static final World world = World.getWorld();

  /**
   * Random
   */
  private final Random r = new Random();

  /**
   * The location of this npc
   */
  private final NPCLoc loc;

  /**
   * The definition of this npc
   */
  private final NPCDef def;

  /**
   * The npcs hitpoints
   */
  private int curHits;

  private int curAttack;

  private int curStrength;

  private int curDefense;

  /**
   * DelayedEvent used for unblocking an npc after set time
   */
  private DelayedEvent timeout;

  /**
   * The player currently blocking this npc
   */
  private Player blocker;

  /**
   * Should this npc respawn once it has been killed?
   **/
  private boolean shouldRespawn = true;

  public Npc(int id, int startX, int startY, int minX, int maxX, int minY, int maxY) {
    this(new NPCLoc(id, startX, startY, minX, maxX, minY, maxY));
  }

  public Npc(NPCLoc loc) {
    def = EntityHandler.getNpcDef(loc.getId());
    curHits = def.getHits();
    this.loc = loc;
    this.setID(loc.getId());
    this.setLocation(Point.location(loc.startX(), loc.startY()), true);
    this.setCombatLevel(Formulae.getCombatLevel(def.getAtt(), def.getDef(), def.getStr(), def.getHits(), 0, 0, 0));
  }

  public final int Rand(int low, int high) {
    return low + r.nextInt(high - low);
  }

  public void setRespawn(boolean respawn) {
    shouldRespawn = respawn;
  }

  public void blockedBy(Player player) {
    blocker = player;
    player.setNpc(this);
    setBusy(true);
    timeout = new DelayedEvent(null, 15000) {
      public void run() {
        unblock();
        running = false;
      }
    };
    world.getDelayedEventHandler().add(timeout);
  }

  public void unblock() {
    if (blocker != null) {
      blocker.setNpc(null);
      blocker = null;
    }
    if (timeout == null) {
      return;
    }
    setBusy(false);
    timeout.stop();
    timeout = null;
  }

  public NPCLoc getLoc() {
    return loc;
  }

  public void moveNpc(Path path) {
    this.setPath(path);
    super.updatePosition();
  }

  public NPCDef getDef() {
    return EntityHandler.getNpcDef(getID());
  }

  public void killedBy(Mob mob, boolean stake) {
    if (mob instanceof Player) {
      Player player = (Player) mob;
      player.getActionSender().sendSound("victory");
    }
    Mob opponent = this.getOpponent();
    if (opponent != null) {
      opponent.resetCombat(CombatState.WON);
    }
    resetCombat(CombatState.LOST);
    world.unregisterNpc(this);
    remove();
    Player owner = mob instanceof Player ? (Player) mob : null;
    ItemDropDef[] drops = def.getDrops();
    int total = 0;
    for (ItemDropDef drop : drops) {
      total += drop.getWeight();
    }
    int hit = DataConversions.random(0, total);
    total = 0;
    for (ItemDropDef drop : drops) {
      if (drop.getWeight() == 0) {
        world.registerItem(new Item(drop.getID(), getX(), getY(), drop.getAmount(), owner));
        continue;
      }
      if (hit >= total && hit < (total + drop.getWeight())) {
        world.registerItem(new Item(drop.getID(), getX(), getY(), drop.getAmount(), owner));
        break;
      }
      total += drop.getWeight();
    }
  }

  public void remove() {
    if (!removed && shouldRespawn && def.respawnTime() > 0) {
      world.getDelayedEventHandler().add(new DelayedEvent(null, def.respawnTime() * 1000) {
        public void run() {
          DelayedEvent.world.registerNpc(new Npc(loc));
          running = false;
        }
      });
    }
    removed = true;
  }

  public int getCombatStyle() {
    return 0;
  }

  public int getWeaponPowerPoints() {
    return 1;
  }

  public int getWeaponAimPoints() {
    return 1;
  }

  public int getArmourPoints() {
    return 1;
  }

  public int getAttack() {
    return def.getAtt();
  }

  public void setAttack(int lvl) {
    if (lvl <= 0) {
      lvl = 0;
    }
    curAttack = lvl;
  }

  public int getDefense() {
    return def.getDef();
  }

  public void setDefense(int lvl) {
    if (lvl <= 0) {
      lvl = 0;
    }
    curDefense = lvl;
  }

  public int getStrength() {
    return def.getStr();
  }

  public void setStrength(int lvl) {
    if (lvl <= 0) {
      lvl = 0;
    }
    curStrength = lvl;
  }

  public int getHits() {
    return curHits;
  }

  public void setHits(int lvl) {
    if (lvl <= 0) {
      lvl = 0;
    }
    curHits = lvl;
  }

  public void updatePosition() {
    long now = System.currentTimeMillis();
    Player victim = null;
    if (!isBusy() && def.isAggressive() && now - getCombatTimer() > 3000 && (victim = findVictim()) != null) {
      resetPath();
      victim.resetPath();
      victim.resetAll();
      victim.setStatus(Action.FIGHTING_MOB);
      victim.getActionSender().sendMessage("You are under attack!");
      setLocation(victim.getLocation(), true);
      for (Player p : getViewArea().getPlayersInView()) {
        p.removeWatchedNpc(this);
      }
      victim.setBusy(true);
      victim.setSprite(9);
      victim.setOpponent(this);
      victim.setCombatTimer();
      setBusy(true);
      setSprite(8);
      setOpponent(victim);
      setCombatTimer();
      FightEvent fighting = new FightEvent(victim, this, true);
      fighting.setLastRun(0);
      world.getDelayedEventHandler().add(fighting);
    }
    if (now - lastMovement > 6000) {
      lastMovement = now;
      if (!isBusy() && finishedPath() && DataConversions.random(0, 2) == 1) {
        this.setPath(new Path(
          getX(),
          getY(),
          DataConversions.random(loc.minX(), loc.maxX()),
          DataConversions.random(loc.minY(), loc.maxY())
        ));
      }
    }
    super.updatePosition();
  }

  private Player findVictim() {
    long now = System.currentTimeMillis();
    ActiveTile[][] tiles = getViewArea().getViewedArea(2, 2, 2, 2);
    for (int x = 0; x < tiles.length; x++) {
      for (int y = 0; y < tiles[x].length; y++) {
        ActiveTile t = tiles[x][y];
        if (t != null) {
          for (Player p : t.getPlayers()) {
            if (p.isBusy() ||
                now - p.getCombatTimer() <
                (p.getCombatState() == CombatState.RUNNING || p.getCombatState() == CombatState.WAITING ? 3000 : 500) ||
                !p.nextTo(this) ||
                !p.getLocation().inBounds(loc.minX - 4, loc.minY - 4, loc.maxX + 4, loc.maxY + 4)) {
              continue;
            }
            if (getLocation().inWilderness() || p.getCombatLevel() < (getCombatLevel() * 2) + 1) {
              return p;
            }
          }
        }
      }
    }
    return null;
  }

}
