package org.firescape.server.entityhandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.firescape.server.GameEngine;
import org.firescape.server.db.*;
import org.firescape.server.entityhandling.defs.*;
import org.firescape.server.entityhandling.defs.extras.*;
import org.firescape.server.model.Point;
import org.firescape.server.model.TelePoint;
import org.firescape.server.util.Formulae;
import org.firescape.server.util.PersistenceManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * This class handles the loading of entities from the conf files, and provides methods for relaying these entities to
 * the user.
 */
@SuppressWarnings("unchecked")
public class EntityHandler {

  private static final DoorDef[] doors;
  private static final GameObjectDef[] gameObjects;
  private static final NPCDef[] npcs;
  private static final PrayerDef[] prayers;
  private static final ItemDef[] items;
  private static final TileDef[] tiles;
  private static final AdvertDef[] adverts;

  private static final List[] keyChestLoots;
  private static final ItemHerbSecond[] herbSeconds;
  private static final HashMap<Integer, ItemDartTipDef> dartTips;
  private static final HashMap<Integer, ItemGemDef> gems;
  private static final HashMap<Integer, ItemLogCutDef> logCut;
  private static final HashMap<Integer, ItemBowStringDef> bowString;
  private static final HashMap<Integer, ItemArrowHeadDef> arrowHeads;
  private static final HashMap<Integer, FiremakingDef> firemaking;
  private static final HashMap<Integer, int[]> itemAffectedTypes;
  private static final HashMap<Integer, ItemWieldableDef> itemWieldable;
  private static final HashMap<Integer, ItemUnIdentHerbDef> itemUnIdentHerb;
  private static final HashMap<Integer, ItemHerbDef> itemHerb;
  private static final HashMap<Integer, Integer> itemEdibleHeals;
  private static final HashMap<Integer, ItemCookingDef> itemCooking;
  private static final ItemSmithingDef[] itemSmithing;
  private static final ItemCraftingDef[] itemCrafting;
  private static final HashMap<Integer, ItemSmeltingDef> itemSmelting;

  private static final SpellDef[] spells;
  private static final HashMap<Integer, Integer> spellAggressiveLvl;

  private static final HashMap<Point, TelePoint> objectTelePoints;
  private static final HashMap<Integer, CerterDef> certers;
  private static final HashMap<Integer, ObjectMiningDef> objectMining;
  private static final HashMap<Integer, ObjectWoodcuttingDef> objectWoodcutting;
  private static final HashMap<Integer, ObjectFishingDef[]> objectFishing;
  public static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

  static {
    doors = (DoorDef[]) PersistenceManager.load("defs/DoorDef.xml.gz");
    indexEntityArray(doors);
    gameObjects = (GameObjectDef[]) PersistenceManager.load("defs/GameObjectDef.xml.gz");
    indexEntityArray(gameObjects);
    npcs = (NPCDef[]) PersistenceManager.load("defs/NPCDef.xml.gz");
    indexEntityArray(npcs);
    prayers = (PrayerDef[]) PersistenceManager.load("defs/PrayerDef.xml.gz");
    indexEntityArray(prayers);
    items = (ItemDef[]) PersistenceManager.load("defs/ItemDef.xml.gz");
    indexEntityArray(items);
    spells = (SpellDef[]) PersistenceManager.load("defs/SpellDef.xml.gz");
    indexEntityArray(spells);
    tiles = (TileDef[]) PersistenceManager.load("defs/TileDef.xml.gz");
    indexEntityArray(tiles);
    keyChestLoots = (List[]) PersistenceManager.load("defs/extras/KeyChestLoot.xml.gz");
    herbSeconds = (ItemHerbSecond[]) PersistenceManager.load("defs/extras/ItemHerbSecond.xml");
    indexEntityArray(herbSeconds);
    dartTips = (HashMap<Integer, ItemDartTipDef>) PersistenceManager.load("defs/extras/ItemDartTipDef" + ".xml");
    gems = (HashMap<Integer, ItemGemDef>) PersistenceManager.load("defs/extras/ItemGemDef.xml");
    logCut = (HashMap<Integer, ItemLogCutDef>) PersistenceManager.load("defs/extras/ItemLogCutDef.xml");
    bowString = (HashMap<Integer, ItemBowStringDef>) PersistenceManager.load("defs/extras/ItemBowStringDef.xml");
    arrowHeads = (HashMap<Integer, ItemArrowHeadDef>) PersistenceManager.load("defs/extras/ItemArrowHeadDef.xml");
    firemaking = (HashMap<Integer, FiremakingDef>) PersistenceManager.load("defs/extras/FiremakingDef" + ".xml");
    itemAffectedTypes = (HashMap<Integer, int[]>) PersistenceManager.load("defs/extras/ItemAffectedTypes.xml");
    itemWieldable = (HashMap<Integer, ItemWieldableDef>) PersistenceManager.load("defs/extras/ItemWieldableDef.xml");
    itemUnIdentHerb = (HashMap<Integer, ItemUnIdentHerbDef>) PersistenceManager.load("defs/extras/ItemUnIdentHerbDef" +
                                                                                     ".xml");
    itemHerb = (HashMap<Integer, ItemHerbDef>) PersistenceManager.load("defs/extras/ItemHerbDef.xml");
    itemEdibleHeals = (HashMap<Integer, Integer>) PersistenceManager.load("defs/extras/ItemEdibleHeals" + ".xml");
    itemCooking = (HashMap<Integer, ItemCookingDef>) PersistenceManager.load("defs/extras/ItemCookingDef.xml");
    itemSmelting = (HashMap<Integer, ItemSmeltingDef>) PersistenceManager.load("defs/extras/ItemSmeltingDef.xml");
    itemSmithing = (ItemSmithingDef[]) PersistenceManager.load("defs/extras/ItemSmithingDef.xml");
    indexEntityArray(itemSmithing);
    itemCrafting = (ItemCraftingDef[]) PersistenceManager.load("defs/extras/ItemCraftingDef.xml");
    indexEntityArray(itemCrafting);
    objectMining = (HashMap<Integer, ObjectMiningDef>) PersistenceManager.load("defs/extras/ObjectMining.xml");
    objectWoodcutting = (HashMap<Integer, ObjectWoodcuttingDef>) PersistenceManager.load(
      "defs/extras/ObjectWoodcutting.xml");
    objectFishing = (HashMap<Integer, ObjectFishingDef[]>) PersistenceManager.load("defs/extras/ObjectFishing.xml");
    spellAggressiveLvl = (HashMap<Integer, Integer>) PersistenceManager.load("defs/extras/SpellAggressiveLvl.xml.gz");
    adverts = (AdvertDef[]) PersistenceManager.load("defs/extras/Adverts.xml");
    objectTelePoints = (HashMap<Point, TelePoint>) PersistenceManager.load("locs/extras/ObjectTelePoints.xml.gz");
    certers = (HashMap<Integer, CerterDef>) PersistenceManager.load("defs/extras/NpcCerters.xml.gz");
  }

  public static void indexEntityArray(Object[] oa) {
    for (int i = 0; i < oa.length; i++) {
      EntityDef ed = (EntityDef) oa[i];
      ed.id = i;
    }
  }

  public static void writeGameDefs() {
    GameEngine.OpenDB();
    saveDoors();
    System.out.println("Saved Doors");
    saveGameObjects();
    System.out.println("Saved Game Objects");
    saveItems();
    System.out.println("Saved Items");
    saveNPCs();
    System.out.println("Saved NPCs");
    savePrayers();
    System.out.println("Saved Prayers");
    saveSpells();
    System.out.println("Saved Spells");
    saveStats();
    System.out.println("Saved Stats");
    saveTiles();
    System.out.println("Saved Tiles");
    GameEngine.CloseDB();
    //    writeGameDef("doors", doors);
    //    writeGameDef("game_objects", gameObjects);
    //    writeGameDef("npcs", npcs);
    //    writeGameDef("prayers", prayers);
    //    writeGameDef("items", items);
    //    writeGameDef("spells", spells);
    //    writeGameDef("tiles", tiles);
    //    writeGameDef("key_chest_loots", keyChestLoots);
    //    writeGameDef("herbs_advanced", herbSeconds);
    //    writeGameDef("dart_tips", dartTips);
    //    writeGameDef("gems", gems);
    //    writeGameDef("logcutting", logCut);
    //    writeGameDef("bow_stringing", bowString);
    //    writeGameDef("arrow_heads", arrowHeads);
    //    writeGameDef("firemaking", firemaking);
    //    writeGameDef("item_affected_types", itemAffectedTypes);
    //    writeGameDef("item_wieldable", itemWieldable);
    //    writeGameDef("item_unidentify_herb", itemUnIdentHerb);
    //    writeGameDef("item_herb", itemHerb);
    //    writeGameDef("item_heals", itemEdibleHeals);
    //    writeGameDef("item_cooking", itemCooking);
    //    writeGameDef("item_smelting", itemSmelting);
    //    writeGameDef("item_smithing", itemSmithing);
    //    writeGameDef("item_crafting", itemCrafting);
    //    writeGameDef("object_mining", objectMining);
    //    writeGameDef("object_woodcutting", objectWoodcutting);
    //    writeGameDef("object_fishing", objectFishing);
    //    writeGameDef("spell_aggressive_levels", spellAggressiveLvl);
    //    writeGameDef("object_telepoints", objectTelePoints);
    //    writeGameDef("certers", certers);
    return;
  }

  public static void saveDoors() {
    int id = 0;
    for (DoorDef door : doors) {
      Door d = new Door();
      d.set("id",
            id,
            "name",
            door.name,
            "description",
            door.description,
            "command1",
            door.command1,
            "command2",
            door.command2,
            "door_type",
            door.doorType,
            "unknown",
            door.unknown,
            "model_var1",
            door.modelVar1,
            "model_var2",
            door.modelVar2,
            "model_var3",
            door.modelVar3
      );
      d.insert();
      id++;
    }
  }

  public static void saveGameObjects() {
    int id = 0;
    for (GameObjectDef obj : gameObjects) {
      GameObject go = new GameObject();
      go.set("id",
             id,
             "name",
             obj.name,
             "description",
             obj.description,
             "command1",
             obj.command1,
             "command2",
             obj.command2,
             "object_type",
             obj.type,
             "width",
             obj.width,
             "height",
             obj.height,
             "ground_item_var",
             obj.groundItemVar,
             "object_model",
             obj.objectModel
      );
      go.insert();
      id++;
    }
  }

  public static void saveItems() {
    int id = 0;
    for (ItemDef obj : items) {
      Item i = new Item();
      i.set("id",
            id,
            "name",
            obj.name,
            "description",
            obj.description,
            "command",
            obj.command,
            "sprite_id",
            obj.sprite,
            "stackable",
            obj.stackable,
            "wieldable",
            obj.wieldable,
            "base_price",
            obj.basePrice,
            "picture_mask",
            obj.pictureMask
      );
      i.insert();
      id++;
    }
  }

  public static void saveNPCs() {
    int id = 0;
    for (NPCDef obj : npcs) {
      NPC n = new NPC();
      n.set("id",
            id,
            "name",
            obj.name,
            "description",
            obj.description,
            "command",
            obj.command,
            "hair_color",
            obj.hairColour,
            "top_color",
            obj.topColour,
            "skin_color",
            obj.skinColour,
            "camera1",
            obj.camera1,
            "camera2",
            obj.camera2,
            "walk_model",
            obj.walkModel,
            "combat_model",
            obj.combatModel,
            "combat_sprite",
            obj.combatSprite,
            "hits",
            obj.hits,
            "attack",
            obj.attack,
            "defense",
            obj.defense,
            "strength",
            obj.strength,
            "respawn_time",
            obj.respawnTime,
            "attackable",
            obj.attackable,
            "aggressive",
            obj.aggressive
      );
      n.insert();
      id++;
    }
  }

  public static void savePrayers() {
    int id = 0;
    for (PrayerDef obj : prayers) {
      Prayer p = new Prayer();
      p.set("id",
            id,
            "name",
            obj.name,
            "description",
            obj.description,
            "drain_rate",
            obj.drainRate,
            "required_level",
            obj.reqLevel
      );
      p.insert();
      id++;
    }
  }

  public static void saveSpells() {
    int id = 0;
    for (SpellDef obj : spells) {
      Spell s = new Spell();
      s.set("id",
            id,
            "name",
            obj.name,
            "description",
            obj.description,
            "spell_type",
            obj.type,
            "required_level",
            obj.reqLevel,
            "experience",
            obj.exp
      );
      s.insert();
      id++;
    }
  }

  public static void saveStats() {
    int id = 0;
    for (String s : Formulae.statArray) {
      Stat st = new Stat();
      st.set("id", id, "name", s);
      st.insert();
      id++;
    }
  }

  public static void saveTiles() {
    int id = 0;
    for (TileDef obj : tiles) {
      Tile t = new Tile();
      t.set("id",
            id,
            "name",
            obj.name,
            "description",
            obj.description,
            "color",
            obj.colour,
            "object_type",
            obj.objectType,
            "unknown",
            obj.unknown
      );
      t.insert();
      id++;
    }
  }

  public static void writeGameDef(String name, Object o) {
    try (PrintWriter out = new PrintWriter("conf/json/server/defs/" + name + ".json")) {
      out.println(gson.toJson(o));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static List[] getKeyChestLoots() {
    return keyChestLoots.clone();
  }

  public static AdvertDef[] getAdverts() {
    return adverts;
  }

  /**
   * @return the ItemHerbSecond for the given second ingredient
   */
  public static ItemHerbSecond getItemHerbSecond(int secondID, int unfinishedID) {
    for (ItemHerbSecond def : herbSeconds) {
      if (def.getSecondID() == secondID && def.getUnfinishedID() == unfinishedID) {
        return def;
      }
    }
    return null;
  }

  /**
   * @return the ItemDartTipDef for the given tip
   */
  public static ItemDartTipDef getItemDartTipDef(int id) {
    return dartTips.get(id);
  }

  /**
   * @return the ItemGemDef for the given gem
   */
  public static ItemGemDef getItemGemDef(int id) {
    return gems.get(id);
  }

  /**
   * @return the ItemArrowHeadDef for the given arrow
   */
  public static ItemArrowHeadDef getItemArrowHeadDef(int id) {
    return arrowHeads.get(id);
  }

  /**
   * @return the ItemLogCutDef for the given log
   */
  public static ItemLogCutDef getItemLogCutDef(int id) {
    return logCut.get(id);
  }

  /**
   * @return the ItemBowStringDef for the given bow
   */
  public static ItemBowStringDef getItemBowStringDef(int id) {
    return bowString.get(id);
  }

  /**
   * @return the FiremakingDef for the given log
   */
  public static FiremakingDef getFiremakingDef(int id) {
    return firemaking.get(id);
  }

  /**
   * @return the ItemCraftingDef for the requested item
   */
  public static ItemCraftingDef getCraftingDef(int id) {
    if (id < 0 || id >= itemCrafting.length) {
      return null;
    }
    return itemCrafting[id];
  }

  /**
   * @return the ItemSmithingDef for the requested item
   */
  public static ItemSmithingDef getSmithingDef(int id) {
    if (id < 0 || id >= itemSmithing.length) {
      return null;
    }
    return itemSmithing[id];
  }

  /**
   * @param id
   *   the npcs ID
   *
   * @return the CerterDef for the given npc
   */
  public static CerterDef getCerterDef(int id) {
    return certers.get(id);
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ItemSmeltingDef with the given ID
   */
  public static ItemSmeltingDef getItemSmeltingDef(int id) {
    return itemSmelting.get(id);
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ItemCookingDef with the given ID
   */
  public static ItemCookingDef getItemCookingDef(int id) {
    return itemCooking.get(id);
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ObjectFishingDef with the given ID
   */
  public static ObjectFishingDef getObjectFishingDef(int id, int click) {
    ObjectFishingDef[] defs = objectFishing.get(id);
    if (defs == null) {
      return null;
    }
    return defs[click];
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ObjectMiningDef with the given ID
   */
  public static ObjectMiningDef getObjectMiningDef(int id) {
    return objectMining.get(id);
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ObjectWoodcuttingDef with the given ID
   */
  public static ObjectWoodcuttingDef getObjectWoodcuttingDef(int id) {
    return objectWoodcutting.get(id);
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ItemHerbDef with the given ID
   */
  public static ItemHerbDef getItemHerbDef(int id) {
    return itemHerb.get(id);
  }

  /**
   * @param the
   *   point we are currently at
   *
   * @return the point we should be teleported to
   */
  public static Point getObjectTelePoint(Point location, String command) {
    TelePoint point = objectTelePoints.get(location);
    if (point == null) {
      return null;
    }
    if (command == null || point.getCommand().equalsIgnoreCase(command)) {
      return point;
    }
    return null;
  }

  /**
   * @param the
   *   spells id
   *
   * @return the lvl of the spell (for calculating what it hits)
   */
  public static int getSpellAggressiveLvl(int id) {
    Integer lvl = spellAggressiveLvl.get(id);
    if (lvl != null) {
      return lvl.intValue();
    }
    return 0;
  }

  /**
   * @param the
   *   items id
   *
   * @return the amount eating the item should heal
   */
  public static int getItemEdibleHeals(int id) {
    Integer heals = itemEdibleHeals.get(id);
    if (heals != null) {
      return heals.intValue();
    }
    return 0;
  }

  /**
   * @param the
   *   items type
   *
   * @return the types of items affected
   */
  public static int[] getItemAffectedTypes(int type) {
    return itemAffectedTypes.get(type);
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ItemUnIdentHerbDef with the given ID
   */
  public static ItemUnIdentHerbDef getItemUnIdentHerbDef(int id) {
    return itemUnIdentHerb.get(id);
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ItemWieldableDef with the given ID
   */
  public static ItemWieldableDef getItemWieldableDef(int id) {
    return itemWieldable.get(id);
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the DoorDef with the given ID
   */
  public static DoorDef getDoorDef(int id) {
    if (id < 0 || id >= doors.length) {
      return null;
    }
    return doors[id];
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the GameObjectDef with the given ID
   */
  public static GameObjectDef getGameObjectDef(int id) {
    if (id < 0 || id >= gameObjects.length) {
      return null;
    }
    return gameObjects[id];
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the ItemDef with the given ID
   */
  public static ItemDef getItemDef(int id) {
    if (id < 0 || id >= items.length) {
      return null;
    }
    return items[id];
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the TileDef with the given ID
   */
  public static TileDef getTileDef(int id) {
    if (id < 0 || id >= tiles.length) {
      return null;
    }
    return tiles[id];
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the NPCDef with the given ID
   */
  public static NPCDef getNpcDef(int id) {
    if (id < 0 || id >= npcs.length) {
      return null;
    }
    return npcs[id];
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the PrayerDef with the given ID
   */
  public static PrayerDef getPrayerDef(int id) {
    if (id < 0 || id >= prayers.length) {
      return null;
    }
    return prayers[id];
  }

  /**
   * @param id
   *   the entities ID
   *
   * @return the SpellDef with the given ID
   */
  public static SpellDef getSpellDef(int id) {
    if (id < 0 || id >= spells.length) {
      return null;
    }
    return spells[id];
  }

}
