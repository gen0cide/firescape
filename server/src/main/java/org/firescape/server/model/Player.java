package org.firescape.server.model;

import com.google.gson.Gson;
import org.apache.mina.common.IoSession;
import org.firescape.server.GameVars;
import org.firescape.server.Server;
import org.firescape.server.entityhandling.EntityHandler;
import org.firescape.server.entityhandling.defs.PrayerDef;
import org.firescape.server.event.DelayedEvent;
import org.firescape.server.event.MiniEvent;
import org.firescape.server.event.RangeEvent;
import org.firescape.server.event.ShortEvent;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.packetbuilder.RSCPacketBuilder;
import org.firescape.server.packetbuilder.client.MiscPacketBuilder;
import org.firescape.server.states.Action;
import org.firescape.server.states.CombatState;
import org.firescape.server.util.DataConversions;
import org.firescape.server.util.Formulae;
import org.firescape.server.util.Logger;
import org.firescape.server.util.StatefulEntityCollection;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A single player.
 */
public final class Player extends Mob {

  /**
   * The IO session of this player
   */
  private final transient IoSession ioSession;
  /**
   * The current stat array
   */
  private final int[] curStat = new int[18];
  /**
   * The max stat array
   */
  private final int[] maxStat = new int[18];
  /**
   * List of players this player 'knows' (recieved from the client) about
   */
  private final transient HashMap<Integer, Integer> knownPlayersAppearanceIDs = new HashMap<Integer, Integer>();
  /**
   * Nearby players that we should be aware of
   */
  private final transient StatefulEntityCollection<Player> watchedPlayers = new StatefulEntityCollection<Player>();
  /**
   * Nearby game objects that we should be aware of
   */
  private final transient StatefulEntityCollection<GameObject> watchedObjects = new StatefulEntityCollection<GameObject>();
  /**
   * Nearby items that we should be aware of
   */
  private final transient StatefulEntityCollection<Item> watchedItems = new StatefulEntityCollection<Item>();
  /**
   * Nearby npcs that we should be aware of
   */
  private final transient StatefulEntityCollection<Npc> watchedNpcs = new StatefulEntityCollection<Npc>();
  /**
   * Users privacy settings, chat block etc.
   */
  private final boolean[] privacySettings = new boolean[4];
  /**
   * Users game settings, camera rotation preference etc
   */
  private final boolean[] gameSettings = new boolean[7]; // Why
  /**
   * Methods to send packets related to actions
   */
  private final transient MiscPacketBuilder actionSender;
  /**
   * List of items offered in the current trade
   */
  private final transient ArrayList<InvItem> tradeOffer = new ArrayList<InvItem>();
  /**
   * List of items offered in the current duel
   */
  private final transient ArrayList<InvItem> duelOffer = new ArrayList<InvItem>();
  /**
   * Map of players on players friend list
   */
  private final ArrayList<String> friendList = new ArrayList<String>();
  /**
   * List of usernameHash's of players on players ignore list
   */
  private final HashSet<String> ignoreList = new HashSet<String>();
  /**
   * List of all projectiles needing displayed
   */
  private final transient ArrayList<Projectile> projectilesNeedingDisplayed = new ArrayList<Projectile>();
  /**
   * List of players who have been hit
   */
  private final transient ArrayList<Player> playersNeedingHitsUpdate = new ArrayList<Player>();
  /**
   * List of players who have been hit
   */
  private final transient ArrayList<Npc> npcsNeedingHitsUpdate = new ArrayList<Npc>();
  /**
   * Chat messages needing displayed
   */
  private final transient ArrayList<ChatMessage> chatMessagesNeedingDisplayed = new ArrayList<ChatMessage>();
  /**
   * NPC messages needing displayed
   */
  private final transient ArrayList<ChatMessage> npcMessagesNeedingDisplayed = new ArrayList<ChatMessage>();
  /**
   * Bubbles needing displayed
   */
  private final transient ArrayList<Bubble> bubblesNeedingDisplayed = new ArrayList<Bubble>();
  /**
   * Players we have been attacked by signed login, used to check if we should get a skull for attacking back
   */
  private final transient HashMap<Long, Long> attackedBy = new HashMap<Long, Long>();
  /**
   * Killing Spree Ranks
   */
  private final String[] killingSpreeRank = {
    "No Rank", "Noob", "Pker", "Hitman", "Assassin", "Legend"
  };
  // is
  // 1
  // empty?
  /**
   * Queue of last 100 packets, used for auto detection purposes
   */
  private final LinkedList<RSCPacket> lastPackets = new LinkedList<RSCPacket>();
  /**
   * List of chat messages to send
   */
  private final LinkedList<ChatMessage> chatQueue = new LinkedList<ChatMessage>();
  /**
   * Duel options
   */
  private final boolean[] duelOptions = new boolean[4];
  public int rank;
  public boolean bad_login;
  /**
   * Clan System
   */
  public boolean inParty;
  public ArrayList<String> myParty = new ArrayList<String>();
  public ArrayList<String> invitedPlayers = new ArrayList<String>();
  public String lastPartyInvite;
  public String summonLeader;
  /**
   * Mute System
   */
  public int mute;
  /**
   * Romeo and Juliet @author Yong Min
   */
  public int romeostatus;
  /**
   * Sheep Shearer @author Yong Min
   */
  public int shearerstatus;
  /**
   * Druidic Ritual @author Yong Min
   */
  public int druidicritualstatus;
  /**
   * Witches Potion @author Yong Min
   */
  public int witchpotstatus;
  /**
   * Doric's Quest @author Yong Min
   */
  public int doricsqueststatus;
  /**
   * Cook's Assistant @author Yong Min
   */
  public int cooksassisstatus;
  /**
   * Imp Catcher @author Yong Min
   */
  public int impcatcherstatus;
  public boolean[] npcThief = {
    false, false, false, false, false, false
  }; // Baker,
  public boolean muted;
  /**
   * The player's username
   */

  private String username;
  /**
   * The player's username hash
   */
  private long usernameHash;
  /**
   * The player's password
   */

  private String password;
  /**
   * Whether the player is currently logged in
   */
  private boolean loggedIn;
  /**
   * Last time a 'ping' was received
   */
  private long lastPing = System.currentTimeMillis();
  /**
   * The Players appearance
   */
  private PlayerAppearance appearance;
  /**
   * The items being worn by the player
   */
  private int[] wornItems = new int[12];
  /**
   * The exp level array
   */
  private int[] exp = new int[18];
  /**
   * If the player has been sending suscicious packets
   */
  private boolean suspicious;
  /**
   * Inventory to hold items
   */
  private Inventory inventory;
  /**
   * Bank for banked items
   */
  private Bank bank;
  /**
   * Unix time when the player last logged in
   */
  private long lastLogin;
  /**
   * Unix time when the player logged in
   */
  private long currentLogin;
  /**
   * Stores the last IP address used
   */
  private String lastIP = "0.0.0.0";
  /**
   * Stores the current IP address used
   */
  private transient String currentIP = "0.0.0.0";
  /**
   * If the player is reconnecting after connection loss
   */
  private boolean reconnecting;
  /**
   * Controls if were allowed to accept appearance updates
   */
  private boolean changingAppearance;
  /**
   * Is the character male?
   */
  private boolean maleGender;
  /**
   * The player we last requested to trade with, or null for none
   */
  private transient Player wishToTrade;
  /**
   * The player we last requested to duel with, or null for none
   */
  private transient Player wishToDuel;
  /**
   * If the player is currently in a trade
   */
  private transient boolean isTrading;
  /**
   * If the player is currently in a duel
   */
  private transient boolean isDueling;
  /**
   * If the first trade screen has been accepted
   */
  private transient boolean tradeOfferAccepted;
  /**
   * If the first duel screen has been accepted
   */
  private transient boolean duelOfferAccepted;
  /**
   * If the second trade screen has been accepted
   */
  private transient boolean tradeConfirmAccepted;
  /**
   * If the second duel screen has been accepted
   */
  private transient boolean duelConfirmAccepted;
  /**
   * The time of the last spell cast, used as a throttle
   */
  private transient long lastSpellCast;
  /**
   * Time last report was sent, used to throttle reports
   */
  private transient long lastReport;
  /**
   * Time of last charge spell
   */
  private transient long lastCharge;
  /**
   * Combat style: 0 - all, 1 - str, 2 - att, 3 - def
   */
  private int combatStyle;
  /**
   * Should we destroy this player?
   */
  private transient boolean destroy;
  /**
   * Session keys for the players session
   */
  private transient int[] sessionKeys = new int[4];
  /**
   * Is the player accessing their bank?
   */
  private transient boolean inBank;
  /**
   * A handler for any menu we are currently in
   */
  private MenuHandler menuHandler;
  /**
   * DelayedEvent responsible for handling prayer drains
   */
  private DelayedEvent drainer;
  /**
   * The drain rate of the prayers currently enabled
   */
  private transient int drainRate;
  /**
   * DelayedEvent used for removing players skull after 20mins
   */
  private transient DelayedEvent skullEvent;
  private long summonTime;
  private Player summoner;
  /**
   * Amount of fatigue - 0 to 100
   */
  private int fatigue;
  /**
   * Has the player been registered into the world?
   */
  private boolean initialized;
  /**
   * The shop (if any) the player is currently accessing
   */
  private Shop shop;
  /**
   * The npc we are currently interacting with
   */
  private Npc interactingNpc;
  /**
   * The ID of the owning account
   */
  private int owner = 1;
  /**
   * When the users subscription expires (or 0 if they don't have one)
   */
  private long subscriptionExpires;
  /**
   * Who we are currently following (if anyone)
   */
  private Mob following;
  /**
   * Event to handle following
   */
  private DelayedEvent followEvent;
  /**
   * Ranging event
   */
  private RangeEvent rangeEvent;
  /**
   * Last arrow fired
   */
  private long lastArrow;
  /**
   * Last packet count time
   */
  private long lastCount;
  /**
   * Amount of packets since last count
   */
  private int packetCount;
  /**
   * Time of last trade/duel request
   */
  private long lastTradeDuelRequest;
  /**
   * The name of the client class they are connecting from
   */
  private String className = "NOT_SET";
  /**
   * The current status of the player
   */
  private Action status = Action.IDLE;
  /**
   * Is a trade/duel update required?
   */

  private boolean requiresOfferUpdate;
  /**
   * Invisibility @author Yong Min
   */
  private boolean isInvisible;
  /**
   * Quest Points @author Yong Min
   */
  private int questpoints;
  /**
   * Kills and Deaths @author Yong Min
   */
  private int kills;
  private int deaths;
  /**
   * Godspell Casts @author Yong Min
   */
  private int guthixspellcast;
  private int saradominspellcast;
  private int zamorakspellcast;
  /**
   * Killing Spree @author Yong Min
   */
  private int killingspree;
  // Silver,
  // Spices,
  // Gem.
  private boolean packetSpam;

  public Player(IoSession ios) {
    ioSession = ios;
    currentIP = ((InetSocketAddress) ios.getRemoteAddress()).getAddress().getHostAddress();
    currentLogin = System.currentTimeMillis();
    actionSender = new MiscPacketBuilder(this);
    setBusy(true);
  }

  public void setSEvent(ShortEvent sEvent) {
    Entity.world.getDelayedEventHandler().add(sEvent);
  }

  public void clearMyParty() {
    myParty.clear();
    inParty = false;
  }

  public Player getSummoner() {
    return summoner;
  }

  public void setSummoner(Player summoner) {
    this.summoner = summoner;
  }

  public long getSummonTime() {
    return summonTime;
  }

  public void setSummonTime(long newTime) {
    summonTime = newTime;
  }

  public void updateRemovedPlayer() {
    Player leader = Entity.world.getPlayer(myParty.get(0));
    myParty = leader.myParty;
  }

  public void incMute() {
    mute++;
  }

  public boolean isMuted() {
    return mute != 0;
  }

  public void goInvisible() {
    isInvisible = !isInvisible;
    loggedIn = !isInvisible;
  }

  public boolean isInvisible() {
    return isInvisible;
  }

  public void setInvisible(boolean invisible) {
    isInvisible = invisible;
  }

  public int isRomeoJulietComplete() {
    return romeostatus = 3;
  }

  public int isSheepShearerComplete() {
    return shearerstatus = 2;
  }

  public int isDruidicRitualComplete() {
    return druidicritualstatus = 4;
  }

  public int isWitchPotionComplete() {
    return witchpotstatus = 3;
  }

  public int isDoricsQuestComplete() {
    return doricsqueststatus = 2;
  }

  public int isCooksAssistantComplete() {
    return cooksassisstatus = 2;
  }

  public int isImpCatcherComplete() {
    return impcatcherstatus = 2;
  }

  public void setRequiresOfferUpdate(boolean b) {
    requiresOfferUpdate = b;
  }

  public boolean requiresOfferUpdate() {
    return requiresOfferUpdate;
  }

  public Action getStatus() {
    return status;
  }

  public void setStatus(Action a) {
    status = a;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public boolean getSpam() {
    return packetSpam;
  }

  public void setSpam(boolean spam) {
    packetSpam = spam;
  }

  public boolean tradeDuelThrottling() {
    long now = System.currentTimeMillis();
    if (now - lastTradeDuelRequest > 1000) {
      lastTradeDuelRequest = now;
      return false;
    }
    return true;
  }

  public void addMessageToChatQueue(byte[] messageData) {
    try (Jedis jedis = Entity.world.redis.getResource()) {
      String chat_message = new String(Arrays.copyOfRange(messageData, 0, messageData.length));
      jedis.publish("game_chat", "(" + this.getUsername() + ") " + chat_message);
    }
    chatQueue.add(new ChatMessage(this, messageData));
    if (chatQueue.size() > 2) {
      destroy(false);
    }
  }

  public ChatMessage getNextChatMessage() {
    return chatQueue.poll();
  }

  public void setArrowFired() {
    lastArrow = System.currentTimeMillis();
  }

  public void setRangeEvent(RangeEvent event) {
    if (isRanging()) {
      resetRange();
    }
    rangeEvent = event;
    rangeEvent.setLastRun(lastArrow);
    Entity.world.getDelayedEventHandler().add(rangeEvent);
  }

  public boolean isRanging() {
    return rangeEvent != null;
  }

  public void resetRange() {
    if (rangeEvent != null) {
      rangeEvent.stop();
      rangeEvent = null;
    }
    setStatus(Action.IDLE);
  }

  public boolean canLogout() {
    return !isBusy() && System.currentTimeMillis() - getCombatTimer() > 10000;
  }

  public boolean isFollowing(Mob mob) {
    return isFollowing() && mob.equals(following);
  }

  public boolean isFollowing() {
    return followEvent != null && following != null;
  }

  public void setFollowing(Mob mob) {
    setFollowing(mob, 0);
  }

  public void setFollowing(Mob mob, int radius) {
    if (isFollowing()) {
      resetFollowing();
    }
    following = mob;
    followEvent = new DelayedEvent(this, 500) {
      public void run() {
        if (!owner.withinRange(mob) || mob.isRemoved() || (owner.isBusy() && !owner.isDueling())) {
          resetFollowing();
        } else if (!owner.finishedPath() && owner.withinRange(mob, radius)) {
          owner.resetPath();
        } else if (owner.finishedPath() && !owner.withinRange(mob, radius + 1)) {
          owner.setPath(new Path(owner.getX(), owner.getY(), mob.getX(), mob.getY()));
        }
      }
    };
    Entity.world.getDelayedEventHandler().add(followEvent);
  }

  public void resetFollowing() {
    following = null;
    if (followEvent != null) {
      followEvent.stop();
      followEvent = null;
    }
    resetPath();
  }

  public boolean withinRange(Entity e) {
    int xDiff = location.getX() - e.getLocation().getX();
    int yDiff = location.getY() - e.getLocation().getY();
    return xDiff <= 16 && xDiff >= -15 && yDiff <= 16 && yDiff >= -15;
  }

  public boolean isDueling() {
    return isDueling;
  }

  public void setDueling(boolean b) {
    isDueling = b;
  }

  public void setSkulledOn(Player player) {
    player.addAttackedBy(this);
    if (System.currentTimeMillis() - lastAttackedBy(player) > 1200000) {
      addSkull(1200000);
    }
  }

  public void addAttackedBy(Player p) {
    attackedBy.put(p.getUsernameHash(), System.currentTimeMillis());
  }

  public long lastAttackedBy(Player p) {
    Long time = attackedBy.get(p.getUsernameHash());
    if (time != null) {
      return time;
    }
    return 0;
  }

  // destroy
  public void addSkull(long timeLeft) {
    if (!isSkulled()) {
      skullEvent = new DelayedEvent(this, 1200000) {
        public void run() {
          removeSkull();
        }
      };
      Entity.world.getDelayedEventHandler().add(skullEvent);
      this.setAppearnceChanged(true);
    }
    skullEvent.setLastRun(System.currentTimeMillis() - (1200000 - timeLeft));
  }

  public long getUsernameHash() {
    return usernameHash;
  }

  public boolean isSkulled() {
    return skullEvent != null;
  }

  public void removeSkull() {
    if (!isSkulled()) {
      return;
    }
    this.setAppearnceChanged(true);
    skullEvent.stop();
    skullEvent = null;
  }

  public void setSubscriptionExpires(long expires) {
    subscriptionExpires = expires;
  }

  public int getDaysSubscriptionLeft() {
    long now = (System.currentTimeMillis() / 1000);
    if (subscriptionExpires == 0 || now >= subscriptionExpires) {
      return 0;
    }
    return (int) ((subscriptionExpires - now) / 86400);
  }

  public void addPacket(RSCPacket p) {
    long now = System.currentTimeMillis();
    if (now - lastCount > 3000) {
      lastCount = now;
      packetCount = 0;
    }
    if (!DataConversions.inArray(Formulae.safePacketIDs, p.getID()) && packetCount++ >= 60) {
      destroy(false);
    }
    if (lastPackets.size() >= 60) {
      lastPackets.remove();
    }
    lastPackets.addLast(p);
  }

  public List<RSCPacket> getPackets() {
    return lastPackets;
  }

  public boolean isSuspicious() {
    return suspicious;
  }

  public int getOwner() {
    return owner;
  }

  public void setOwner(int owner) {
    this.owner = owner;
  }

  public Npc getNpc() {
    return interactingNpc;
  }

  public void setNpc(Npc npc) {
    interactingNpc = npc;
  }

  public void remove() {
    removed = true;
  }

  public int getHits() {
    return getCurStat(3);
  }

  public void setHits(int lvl) {
    setCurStat(3, lvl);
  }

  public void setCurStat(int id, int lvl) {
    if (lvl <= 0) {
      lvl = 0;
    }
    curStat[id] = lvl;
  }

  public int getCurStat(int id) {
    return curStat[id];
  }

  public int getAttack() {
    return getCurStat(0);
  }

  public void setAttack(int lvl) {
    setCurStat(0, lvl);
  }

  public int getDefense() {
    return getCurStat(1);
  }

  public void setDefense(int lvl) {
    setCurStat(1, lvl);
  }

  public int getStrength() {
    return getCurStat(2);
  }

  public void setStrength(int lvl) {
    setCurStat(2, lvl);
  }

  public int getWeaponPowerPoints() {
    int points = 1;
    for (InvItem item : inventory.getItems()) {
      if (item.isWielded()) {
        points += item.getWieldableDef().getWeaponPowerPoints();
      }
    }
    return points < 1 ? 1 : points;
  }

  public int getWeaponAimPoints() {
    int points = 1;
    for (InvItem item : inventory.getItems()) {
      if (item.isWielded()) {
        points += item.getWieldableDef().getWeaponAimPoints();
      }
    }
    return points < 1 ? 1 : points;
  }

  public int getArmourPoints() {
    int points = 1;
    for (InvItem item : inventory.getItems()) {
      if (item.isWielded()) {
        points += item.getWieldableDef().getArmourPoints();
      }
    }
    return points < 1 ? 1 : points;
  }

  public boolean initialized() {
    return initialized;
  }

  public void setInitialized() {
    initialized = true;
  }

  public int getDrainRate() {
    return drainRate;
  }

  public void setDrainRate(int rate) {
    drainRate = rate;
  }

  public int getRangeEquip() {
    for (InvItem item : inventory.getItems()) {
      if (item.isWielded() &&
          (DataConversions.inArray(Formulae.bowIDs, item.getID()) ||
           DataConversions.inArray(Formulae.xbowIDs, item.getID()))) {
        return item.getID();
      }
    }
    return -1;
  }

  public void resetAll() {
    resetAllExceptTradeOrDuel();
    resetTrade();
    resetDuel();
  }

  private void resetAllExceptTradeOrDuel() {
    if (getMenuHandler() != null) {
      resetMenuHandler();
    }
    if (accessingBank()) {
      resetBank();
    }
    if (accessingShop()) {
      resetShop();
    }
    if (interactingNpc != null) {
      interactingNpc.unblock();
    }
    if (isFollowing()) {
      resetFollowing();
    }
    if (isRanging()) {
      resetRange();
    }
    setStatus(Action.IDLE);
  }

  public void resetTrade() {
    Player opponent = getWishToTrade();
    if (opponent != null) {
      opponent.resetTrading();
    }
    resetTrading();
  }

  public void resetDuel() {
    Player opponent = getWishToDuel();
    if (opponent != null) {
      opponent.resetDueling();
    }
    resetDueling();
  }

  public MenuHandler getMenuHandler() {
    return menuHandler;
  }

  public void resetMenuHandler() {
    menuHandler = null;
    actionSender.hideMenu();
  }

  public boolean accessingBank() {
    return inBank;
  }

  public void resetBank() {
    setAccessingBank(false);
    actionSender.hideBank();
  }

  public boolean accessingShop() {
    return shop != null;
  }

  public void resetShop() {
    if (shop != null) {
      shop.removePlayer(this);
      shop = null;
      actionSender.hideShop();
    }
  }

  public Player getWishToTrade() {
    return wishToTrade;
  }

  public void resetTrading() {
    if (isTrading()) {
      actionSender.sendTradeWindowClose();
      setStatus(Action.IDLE);
    }
    setWishToTrade(null);
    setTrading(false);
    setTradeOfferAccepted(false);
    setTradeConfirmAccepted(false);
    resetTradeOffer();
  }

  public Player getWishToDuel() {
    return wishToDuel;
  }

  public void resetDueling() {
    if (isDueling()) {
      actionSender.sendDuelWindowClose();
      setStatus(Action.IDLE);
    }
    inDuel = false;
    setWishToDuel(null);
    setDueling(false);
    setDuelOfferAccepted(false);
    setDuelConfirmAccepted(false);
    resetDuelOffer();
    clearDuelOptions();
  }

  public void setAccessingBank(boolean b) {
    inBank = b;
  }

  public boolean isTrading() {
    return isTrading;
  }

  public void setTrading(boolean b) {
    isTrading = b;
  }

  public void resetTradeOffer() {
    tradeOffer.clear();
  }

  public void resetDuelOffer() {
    duelOffer.clear();
  }

  // mute
  public void clearDuelOptions() {
    for (int i = 0; i < 4; i++) {
      duelOptions[i] = false;
    }
  }

  public void setWishToDuel(Player p) {
    wishToDuel = p;
  }

  public void setWishToTrade(Player p) {
    wishToTrade = p;
  }

  public void setMenuHandler(MenuHandler menuHandler) {
    menuHandler.setOwner(this);
    this.menuHandler = menuHandler;
  }

  public void setQuestMenuHandler(MenuHandler menuHandler) {
    this.menuHandler = menuHandler;
    menuHandler.setOwner(this);
    actionSender.sendMenu(menuHandler.getOptions());
  }

  public void resetAllExceptTrading() {
    resetAllExceptTradeOrDuel();
    resetDuel();
  }

  public void resetAllExceptDueling() {
    resetAllExceptTradeOrDuel();
    resetTrade();
  }

  public void setAccessingShop(Shop shop) {
    this.shop = shop;
    if (shop != null) {
      shop.addPlayer(this);
    }
  }

  public void setServerKey(long key) {
    sessionKeys[2] = (int) (key >> 32);
    sessionKeys[3] = (int) key;
  }

  public Shop getShop() {
    return shop;
  }

  public boolean setSessionKeys(int[] keys) {
    boolean valid = (sessionKeys[2] == keys[2] && sessionKeys[3] == keys[3]);
    sessionKeys = keys;
    return valid;
  }

  public void destroy(boolean force) {
    if (destroy) {
      return;
    }
    String user = this.getUsername();
    if (force || canLogout()) {
      if (user == null) {
        destroy = true;
        actionSender.sendLogout();
        return;
      }
      destroy = true;
      actionSender.sendLogout();
      Server.writeValue(user, "loggedin", "false");
      if (this.isAdmin()) {
        GameVars.adminsOnline--;
      } else if (this.rank == 3 || this.rank == 2) {
        GameVars.modsOnline--;
      }
    } else {
      long startDestroy = System.currentTimeMillis();
      Entity.world.getDelayedEventHandler().add(new DelayedEvent(this, 3000) {
        public void run() {
          if (owner.canLogout() ||
              (!(owner.inCombat() && owner.isDueling()) && System.currentTimeMillis() - startDestroy > 60000)) {
            owner.destroy(true);
            running = false;
          }
        }
      });
    }
  }

  // save
  public boolean destroyed() {
    return destroy;
  }

  public void load(String username, String password, int uid, boolean reconnecting) {
    try {
      InputStream ios = null;
      String redis_key = "players_" + username.toLowerCase();
      try (Jedis jedis = Entity.world.redis.getResource()) {
        if (jedis.exists(redis_key)) {
          ios = new ByteArrayInputStream(jedis.get(redis_key).getBytes(StandardCharsets.UTF_8));
          Logger.print("Loaded players_" + username.toLowerCase() + " from redis.", 3);
        } else {
          this.destroy(true);
          return;
        }
      }
      setID(uid);
      this.password = password;
      this.reconnecting = reconnecting;
      usernameHash = DataConversions.usernameToHash(username);
      this.username = DataConversions.hashToUsername(usernameHash);
      Entity.world.getDelayedEventHandler().add(new DelayedEvent(this, 60000) {
        public void run() {
          for (int statIndex = 0; statIndex < 18; statIndex++) {
            if (statIndex == 5) {
              continue;
            }
            int curStat = getCurStat(statIndex);
            int maxStat = getMaxStat(statIndex);
            if (curStat > maxStat) {
              setCurStat(statIndex, curStat - 1);
              getActionSender().sendStat(statIndex);
              checkStat(statIndex);
            } else if (curStat < maxStat) {
              setCurStat(statIndex, curStat + 1);
              getActionSender().sendStat(statIndex);
              checkStat(statIndex);
            }
          }
        }

        private void checkStat(int statIndex) {
          if (statIndex != 3 && owner.getCurStat(statIndex) == owner.getMaxStat(statIndex)) {
            owner.getActionSender()
                 .sendMessage("Your " + Formulae.statArray[statIndex] + " ability has returned" + "" + " to normal.");
          }
        }
      });
      drainer = new DelayedEvent(this, Integer.MAX_VALUE) {
        public void run() {
          int curPrayer = getCurStat(5);
          if (getDrainRate() > 0 && curPrayer > 0) {
            incCurStat(5, -1);
            getActionSender().sendStat(5);
            if (curPrayer <= 1) {
              for (int prayerID = 0; prayerID < 14; prayerID++) { // Prayer was
                // < 14
                setPrayer(prayerID, false);
              }
              setDrainRate(0);
              setDelay(Integer.MAX_VALUE);
              getActionSender().sendMessage("You have run out of prayer points. Return to a church to " + "recharge");
              getActionSender().sendPrayers();
            }
          }
        }
      };
      Entity.world.getDelayedEventHandler().add(drainer);
      Properties props = new Properties();
      props.load(ios);
      setSubscriptionExpires(0); // No sub atm.
      setLastIP(props.getProperty("ip"));
      setLastLogin(Long.parseLong(props.getProperty("ll"))); // Temporary.
      rank = Integer.parseInt(props.getProperty("rank"));
      if (this.isAdmin()) {
        GameVars.adminsOnline++;
      } else if (this.rank == 3 || this.rank == 2) {
        GameVars.modsOnline++;
      }
      setLocation(Point.location(Integer.parseInt(props.getProperty("x")), Integer.parseInt(props.getProperty("y"))),
                  true
      );
      setFatigue(Integer.parseInt(props.getProperty("fat")));
      impcatcherstatus = Integer.parseInt(props.getProperty("impcatcherstatus"));
      doricsqueststatus = Integer.parseInt(props.getProperty("doricsqueststatus"));
      cooksassisstatus = Integer.parseInt(props.getProperty("cooksassisstatus"));
      romeostatus = Integer.parseInt(props.getProperty("romeostatus"));
      shearerstatus = Integer.parseInt(props.getProperty("shearerstatus"));
      witchpotstatus = Integer.parseInt(props.getProperty("witchpotstatus"));
      druidicritualstatus = Integer.parseInt(props.getProperty("druidicritualstatus"));
      setGuthixSpellCast(Integer.parseInt(props.getProperty("guthixspellcast")));
      setSaradominSpellCast(Integer.parseInt(props.getProperty("saradominspellcast")));
      setZamorakSpellCast(Integer.parseInt(props.getProperty("zamorakspellcast")));
      setKillingSpree(Integer.parseInt(props.getProperty("killingspree")));
      setMute(Integer.parseInt(props.getProperty("mute")));
      setKills(Integer.parseInt(props.getProperty("kills")));
      setDeaths(Integer.parseInt(props.getProperty("deaths")));
      setQuestPoints(Integer.parseInt(props.getProperty("questpoints")));
      setCombatStyle(Integer.parseInt(props.getProperty("cs")));
      setPrivacySetting(0, Integer.parseInt(props.getProperty("ps0")) == 1);
      setPrivacySetting(1, Integer.parseInt(props.getProperty("ps1")) == 1);
      setPrivacySetting(2, Integer.parseInt(props.getProperty("ps2")) == 1);
      setPrivacySetting(3, Integer.parseInt(props.getProperty("ps3")) == 1);
      setGameSetting(0, Integer.parseInt(props.getProperty("gs0")) == 1);
      setGameSetting(2, Integer.parseInt(props.getProperty("gs2")) == 1);
      setGameSetting(3, Integer.parseInt(props.getProperty("gs3")) == 1);
      setGameSetting(4, Integer.parseInt(props.getProperty("gs4")) == 1);
      setGameSetting(5, Integer.parseInt(props.getProperty("gs5")) == 1);
      setGameSetting(6, Integer.parseInt(props.getProperty("gs6")) == 1);
      PlayerAppearance appearance = new PlayerAppearance(Integer.parseInt(props.getProperty("a1")),
                                                         Integer.parseInt(props.getProperty("a2")),
                                                         Integer.parseInt(props.getProperty("a3")),
                                                         Integer.parseInt(props.getProperty("a4")),
                                                         Integer.parseInt(props.getProperty("a5")),
                                                         Integer.parseInt(props.getProperty("a6"))
      );
      if (!appearance.isValid()) {
        destroy(true);
        getSession().close();
      }
      setAppearance(appearance);
      setWornItems(getPlayerAppearance().getSprites());
      setMale(Integer.parseInt(props.getProperty("male")) == 1);
      long skull = Long.parseLong(props.getProperty("skull"));
      if (skull > 0L) {
        addSkull(skull);
      }
      for (int i = 0; i < 18; i++) {
        int exp = Integer.parseInt(props.getProperty("e" + (i + 1)));
        setExp(i, exp);
        setMaxStat(i, Formulae.experienceToLevel(exp));
        setCurStat(i, Integer.parseInt(props.getProperty("c" + (i + 1))));
      }
      setCombatLevel(Formulae.getCombatlevel(getMaxStats()));
      int count = Integer.parseInt(props.getProperty("fcount"));
      for (int i = 0; i < count; i++) {
        this.getFriendList().add(props.getProperty("f" + i));
      }
      Inventory inventory = new Inventory(this);
      int invCount = Integer.parseInt(props.getProperty("icount"));
      for (int i = 0; i < invCount; i++) {
        int id = Integer.parseInt(props.getProperty("i" + i));
        int amount = Integer.parseInt(props.getProperty("ia" + i));
        int wear = Integer.parseInt(props.getProperty("iw" + i));
        if (id != 7000) {
          InvItem item = new InvItem(id, amount);
          if (wear == 1 && item.isWieldable()) {
            item.setWield(true);
            updateWornItems(item.getWieldableDef().getWieldPos(), item.getWieldableDef().getSprite());
          }
          inventory.add(item);

        }
      }
      setInventory(inventory);
      Bank bank = new Bank();
      int bnkCount = Integer.parseInt(props.getProperty("bcount"));
      for (int i = 0; i < bnkCount; i++) {
        int id = Integer.parseInt(props.getProperty("b" + i));
        int amount = Integer.parseInt(props.getProperty("ba" + i));
        if (id != 7000) {
          bank.add(new InvItem(id, amount));
        }
      }
      setBank(bank);
      if (!this.bad_login) {
        ios.close();
        try (Jedis jedis = Entity.world.redis.getResource()) {
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          props.setProperty("loggedin", "true");
          props.store(bos, "Redis backed character data");
          jedis.set("players_" + username.toLowerCase(), bos.toString());
          Logger.print("Saved players_" + username.toLowerCase() + " data to redis.", 3);
          bos.close();
        }

      }

      /* End of loading methods */
      Entity.world.registerPlayer(this);
      MiscPacketBuilder sender = getActionSender();
      if (getLastLogin() == 0L) {
        setChangingAppearance(true);
        sender.sendAppearanceScreen();
      }
      setLastLogin(System.currentTimeMillis());
      sender.sendLoginBox();
      setLoggedIn(true);
      setBusy(false);
      RSCPacketBuilder pb = new RSCPacketBuilder();
      pb.setBare(true);
      pb.addByte((byte) 0);
      getSession().write(pb.toPacket());
      updateViewedPlayers();
      updateViewedObjects();
      updateViewedNpcs();
      updateViewedItems();
      //      sender.sendServerInfo();
      //      sender.sendFatigue();
      //      sender.sendImpCatcherComplete();
      //      sender.sendRomeoJulietComplete();
      //      sender.sendSheepShearerComplete();
      //      sender.sendWitchPotionComplete();
      //      sender.sendDoricsQuestComplete();
      //      sender.sendCooksAssistantComplete();
      //      sender.sendDruidicRitualComplete();
      //      sender.sendGuthixSpellCast();
      //      sender.sendSaradominSpellCast();
      //      sender.sendZamorakSpellCast();
      //      sender.sendKillingSpree();
      sender.sendMute();
      sender.sendQuestPoints();
      sender.sendKills();
      sender.sendDeaths();
      sender.sendWorldInfo();
      sender.sendInventory();
      sender.sendEquipmentStats();
      sender.sendStats();
      sender.sendPrivacySettings();
      sender.sendGameSettings();
      sender.sendFriendList();
      sender.sendIgnoreList();
      sender.sendCombatStyle();
      // GUI.populateWorldList();
      for (Player p : Entity.world.getPlayers()) {
        if (p.isFriendsWith(this.getUsername())) {
          p.getActionSender().sendFriendUpdate(DataConversions.usernameToHash(this.username), 1);
        }
      }
      for (String player : getFriendList()) {
        Player p = Entity.world.getPlayer(DataConversions.usernameToHash(player));
        if (p != null) {
          sender.sendFriendUpdate(DataConversions.usernameToHash(player), 1);
        } else {
          sender.sendFriendUpdate(DataConversions.usernameToHash(player), 0);
        }
      }
      int timeTillShutdown = Entity.world.getServer().timeTillShutdown();
      if (timeTillShutdown > -1) {
        sender.startShutdown(timeTillShutdown / 1000);
      }

    } catch (Exception e) {
      e.printStackTrace();
      Logger.print(e.toString(), 1);
    }

  }

  public void save() {
    try {
      if (!this.bad_login) {
        Properties pr = new Properties();
        String username = this.getUsername().replaceAll(" ", "_");
        String redis_key = "players_" + username.toLowerCase();
        try (Jedis jedis = Entity.world.redis.getResource()) {
          if (jedis.exists(redis_key)) {
            InputStream ios = new ByteArrayInputStream(jedis.get(redis_key).getBytes(StandardCharsets.UTF_8));
            pr.load(ios);
            ios.close();
            Logger.print("Loaded players_" + username.toLowerCase() + " from redis.", 3);
          } else {
            File f = new File("players/" + username.toLowerCase() + ".cfg");
            FileInputStream fis = new FileInputStream(f);
            pr.load(fis);
            fis.close();
            Logger.print("Key players_" + username.toLowerCase() + " not in redis. Loading from file.", 3);
          }
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          pr.store(bos, "Redis backed character data");
          jedis.set("players_" + username.toLowerCase(), bos.toString());

        }
        pr.setProperty("rank", "" + this.rank);
        pr.setProperty("x", "" + this.getLocation().getX());
        pr.setProperty("y", "" + this.getLocation().getY());
        pr.setProperty("fat", "" + this.getFatigue());
        pr.setProperty("impcatcherstatus", "" + this.getImpCatcherStatus());
        pr.setProperty("romeostatus", "" + this.getRomeoJulietStatus());
        pr.setProperty("shearerstatus", "" + this.getSheepShearerStatus());
        pr.setProperty("witchpotstatus", "" + this.getWitchPotionStatus());
        pr.setProperty("doricsqueststatus", "" + this.getDoricsQuestStatus());
        pr.setProperty("cooksassisstatus", "" + this.getCooksAssistantStatus());
        pr.setProperty("druidicritualstatus", "" + this.getDruidicRitualStatus());
        pr.setProperty("guthixspellcast", "" + this.getGuthixSpellCast());
        pr.setProperty("saradominspellcast", "" + this.getSaradominSpellCast());
        pr.setProperty("zamorakspellcast", "" + this.getZamorakSpellCast());
        pr.setProperty("killingspree", "" + this.getKillingSpree());
        pr.setProperty("mute", "" + this.getMute());
        pr.setProperty("kills", "" + this.getKills());
        pr.setProperty("deaths", "" + this.getDeaths());
        pr.setProperty("questpoints", "" + this.getQuestPoints());
        pr.setProperty("ip", "" + this.getLastIP());
        pr.setProperty("ll", "" + this.getLastLogin());
        pr.setProperty("cs", "" + this.getCombatStyle());
        pr.setProperty("ps0", "" + (this.getPrivacySetting(0) ? 1 : 0));
        pr.setProperty("ps1", "" + (this.getPrivacySetting(1) ? 1 : 0));
        pr.setProperty("ps2", "" + (this.getPrivacySetting(2) ? 1 : 0));
        pr.setProperty("ps3", "" + (this.getPrivacySetting(3) ? 1 : 0));
        pr.setProperty("gs0", "" + (this.getGameSetting(0) ? 1 : 0));
        pr.setProperty("gs2", "" + (this.getGameSetting(2) ? 1 : 0));
        pr.setProperty("gs3", "" + (this.getGameSetting(3) ? 1 : 0));
        pr.setProperty("gs4", "" + (this.getGameSetting(4) ? 1 : 0));
        pr.setProperty("gs5", "" + (this.getGameSetting(5) ? 1 : 0));
        pr.setProperty("gs6", "" + (this.getGameSetting(6) ? 1 : 0));
        pr.setProperty("a1", "" + this.appearance.getHairColour());
        pr.setProperty("a2", "" + this.appearance.getTopColour());
        pr.setProperty("a3", "" + this.appearance.getTrouserColour());
        pr.setProperty("a4", "" + this.appearance.getSkinColour());
        pr.setProperty("a5", "" + this.appearance.head);
        pr.setProperty("a6", "" + this.appearance.body);
        pr.setProperty("male", "" + (this.isMale() ? 1 : 0));
        pr.setProperty("skull", "" + (this.getSkullTime() > 0 ? this.getSkullTime() : 0));
        for (int i = 0; i < 18; i++) {
          pr.setProperty("c" + (i + 1), "" + this.getCurStat(i));
          pr.setProperty("e" + (i + 1), "" + this.getExp(i));
        }
        int count = this.getInventory().size();
        pr.setProperty("icount", "" + count);
        for (int i = 0; i < count; i++) {
          InvItem item = this.getInventory().get(i);
          pr.setProperty("i" + i, "" + item.getID());
          pr.setProperty("ia" + i, "" + item.getAmount());
          pr.setProperty("iw" + i, "" + (item.isWielded() ? 1 : 0));
        }
        count = this.getFriendList().size();
        pr.setProperty("fcount", "" + count);
        for (int i = 0; i < count; i++) {
          pr.setProperty("f" + i, "" + this.getFriendList().get(i));
        }
        count = this.getBank().size();
        pr.setProperty("bcount", "" + count);
        for (int i = 0; i < count; i++) {
          InvItem item = this.getBank().get(i);
          pr.setProperty("b" + i, "" + item.getID());
          pr.setProperty("ba" + i, "" + item.getAmount());
        }
        // FileOutputStream fos = new FileOutputStream(f);
        // pr.store(fos, "Character Data.");
        // fos.close();
        try (Jedis jedis = Entity.world.redis.getResource()) {
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          pr.store(bos, "Redis backed character data");
          jedis.set("players_" + username.toLowerCase(), bos.toString());
          Logger.print("Saved players_" + username.toLowerCase() + " data to redis.", 3);
        }

      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public String getUsername() {
    return username;
  }

  public int getFatigue() {
    return fatigue;
  }

  public int getImpCatcherStatus() {
    return impcatcherstatus;
  }

  public int getRomeoJulietStatus() {
    return romeostatus;
  }

  public int getSheepShearerStatus() {
    return shearerstatus;
  }

  public int getWitchPotionStatus() {
    return witchpotstatus;
  }

  public int getDoricsQuestStatus() {
    return doricsqueststatus;
  }

  public int getCooksAssistantStatus() {
    return cooksassisstatus;
  }

  public int getDruidicRitualStatus() {
    return druidicritualstatus;
  }

  public int getGuthixSpellCast() {
    return guthixspellcast;
  }

  public int getSaradominSpellCast() {
    return saradominspellcast;
  }

  public int getZamorakSpellCast() {
    return zamorakspellcast;
  }

  public int getKillingSpree() {
    return killingspree;
  }

  public int getMute() {
    return mute;
  }

  public int getKills() {
    return kills;
  }

  public int getDeaths() {
    return deaths;
  }

  public int getQuestPoints() {
    return questpoints;
  }

  public String getLastIP() {
    return lastIP;
  }

  public long getLastLogin() {
    return lastLogin;
  }

  public int getCombatStyle() {
    return combatStyle;
  }

  public void setCombatStyle(int style) {
    combatStyle = style;
  }

  public boolean getPrivacySetting(int i) {
    return privacySettings[i];
  }

  public boolean getGameSetting(int i) {
    return gameSettings[i];
  }

  public boolean isMale() {
    return maleGender;
  }

  public int getSkullTime() {
    if (isSkulled()) {
      return skullEvent.timeTillNextRun();
    }
    return 0;
  }

  public int getExp(int id) {
    return exp[id];
  }

  public Inventory getInventory() {
    return inventory;
  }

  public List<String> getFriendList() {
    return friendList;
  }

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank b) {
    bank = b;
  }

  public void setInventory(Inventory i) {
    inventory = i;
  }

  public void setMale(boolean male) {
    maleGender = male;
  }

  public void setLastLogin(long l) {
    lastLogin = l;
  }

  public void setLastIP(String ip) {
    lastIP = ip;
  }

  public void setQuestPoints(int i) {
    questpoints = i;
    actionSender.sendQuestPoints();
  }

  public void setDeaths(int i) {
    deaths = i;
  }

  public void setKills(int i) {
    kills = i;
  }

  public void setMute(int i) {
    mute = i;
  }

  public void setKillingSpree(int i) {
    killingspree = i;
  }

  public void setZamorakSpellCast(int i) {
    zamorakspellcast = i;
    actionSender.sendZamorakSpellCast();
  }

  public void setSaradominSpellCast(int i) {
    saradominspellcast = i;
    actionSender.sendSaradominSpellCast();
  }

  public void setGuthixSpellCast(int i) {
    guthixspellcast = i;
    actionSender.sendGuthixSpellCast();
  }

  public void setDruidicRitualStatus(int druidicritualstatus) {
    this.druidicritualstatus = druidicritualstatus;
  }

  public void setCooksAssistantStatus(int cooksassisstatus) {
    this.cooksassisstatus = cooksassisstatus;
  }

  public void setDoricsQuestStatus(int doricsqueststatus) {
    this.doricsqueststatus = doricsqueststatus;
  }

  public void setWitchPotionStatus(int witchpotstatus) {
    this.witchpotstatus = witchpotstatus;
  }

  public void setSheepShearerStatus(int shearerstatus) {
    this.shearerstatus = shearerstatus;
  }

  public void setRomeoJulietStatus(int romeostatus) {
    this.romeostatus = romeostatus;
  }

  public void setImpCatcherStatus(int impcatcherstatus) {
    this.impcatcherstatus = impcatcherstatus;
  }

  public void setFatigue(int fatigue) {
    this.fatigue = fatigue;
  }

  public String show_player_json() {
    Gson gson = new Gson();
    String json = gson.toJson(this);
    return json;
  }

  public boolean wielding(int id) {
    ListIterator iterator = getInventory().iterator();
    for (int slot = 0; iterator.hasNext(); slot++) {
      InvItem item = (InvItem) iterator.next();
      if (item.isWielded() && item.getID() == id) {
        return true;
      }
    }
    return false;
  }

  public void setCharged() {
    lastCharge = System.currentTimeMillis();
  }

  public boolean isCharged() {
    return System.currentTimeMillis() - lastCharge > 600000;
  }

  public void killedBy(Mob mob) {
    killedBy(mob, false);
  }

  public void killedBy(Mob mob, boolean stake) {
    boolean drop = true;
    if (!loggedIn) {
      Logger.error(username + " not logged in, but killed!");
      return;
    }
    if (mob instanceof Player) {
      Player player = (Player) mob;
      player.getActionSender().sendMessage("You have defeated " + getUsername() + "!");
      player.incKills();
      actionSender.sendKills();
      player.incKillingSpree();
      actionSender.sendKillingSpree();
      player.getActionSender().sendSound("victory");
      ArrayList<Player> playersToSend = new ArrayList<Player>();
      for (Player p : Entity.world.getPlayers()) {
        playersToSend.add(p);
      }
      for (Player pl : playersToSend) {
        if (player.getKillingSpree() == 1) {
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ has just " +
                         "gained the " +
                         killingSpreeRank[1] +
                         " killing spree rank!");
        } else if (player.getKillingSpree() == 5) {
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ now has a " +
                         "killing spree of: @or1@" +
                         player.getKillingSpree());
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ has just " +
                         "gained the " +
                         killingSpreeRank[2] +
                         " killing spree rank!");
        } else if (player.getKillingSpree() == 10) {
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ now has a " +
                         "killing spree of: @or1@" +
                         player.getKillingSpree());
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ has just " +
                         "gained the " +
                         killingSpreeRank[3] +
                         " killing spree rank!");
        } else if (player.getKillingSpree() == 15) {
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ now has a " +
                         "killing spree of: @or1@" +
                         player.getKillingSpree());
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ has just " +
                         "gained the " +
                         killingSpreeRank[4] +
                         " killing spree rank!");
        } else if (player.getKillingSpree() == 20) {
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ now has a " +
                         "killing spree of: @or1@" +
                         player.getKillingSpree());
          pl.getActionSender()
            .sendMessage("[@red@FireScape@whi@] @or1@" +
                         player.getUsername() +
                         "@whi@ has just " +
                         "gained the " +
                         killingSpreeRank[5] +
                         " killing spree rank!");
        } else {
          pl.getActionSender()
            .sendMessage("@or1@" +
                         player.getUsername() +
                         "@whi@ has just @red@owned @or1@" +
                         getUsername() +
                         "@whi@ !");
          pl.getActionSender()
            .sendMessage("@or1@" + getUsername() + "@whi@'s killing spree of " + getKillingSpree() + " has now ended.");
        }
      }
      Entity.world.getDelayedEventHandler().add(new MiniEvent(player) {
        public void action() {
          owner.getActionSender().sendScreenshot();
          owner.actionSender.sendKills();
          owner.actionSender.sendKillingSpree();
        }
      });// setNpc
      // world.getServer().getLoginConnector().getActionSender().logKill(player.getUsernameHash(),
      // usernameHash, stake);
      if (Entity.world.getServer().pvpIsRunning() && Entity.world.getPvpSize() == 2 && Entity.world.getPvpEntry(this)) {
        Entity.world.setWinner(player);
        Entity.world.removePvpEntry(player);
        Entity.world.removePvpEntry(this);
        player.getInventory().add(new InvItem(10, Entity.world.getJackPot()));
        player.getActionSender().sendInventory();
        Entity.world.getServer().stopDuel();
        Entity.world.clearJackPot();
        player.teleport(220, 445, false);
        drop = false;
      } else if (Entity.world.getServer().pvpIsRunning()) {
        Entity.world.removePvpEntry(this);
        drop = false;
      }
    }
    Mob opponent = this.getOpponent();
    if (opponent != null) {
      opponent.resetCombat(CombatState.WON);
    }
    actionSender.sendDied();
    for (int i = 0; i < 18; i++) {
      curStat[i] = maxStat[i];
      actionSender.sendStat(i);
    }
    Player player = mob instanceof Player ? (Player) mob : null;
    if (stake) {
      for (InvItem item : duelOffer) {
        InvItem affectedItem = getInventory().get(item);
        if (affectedItem == null) {
          setSuspiciousPlayer(true);
          Logger.error("Missing staked item [" +
                       item.getID() +
                       ", " +
                       item.getAmount() +
                       "] from = " +
                       usernameHash +
                       "; to = " +
                       player.getUsernameHash() +
                       ";");
          continue;
        }
        if (affectedItem.isWielded()) {
          affectedItem.setWield(false);
          updateWornItems(affectedItem.getWieldableDef().getWieldPos(),
                          getPlayerAppearance().getSprite(affectedItem.getWieldableDef().getWieldPos())
          );
        }
        getInventory().remove(item);
        Entity.world.registerItem(new Item(item.getID(), getX(), getY(), item.getAmount(), player));
      }
    } else {
      inventory.sort();
      ListIterator<InvItem> iterator = inventory.iterator();
      if (!isSkulled()) {
        for (int i = 0; i < 3 && iterator.hasNext(); i++) {
          if ((iterator.next()).getDef().isStackable()) {
            iterator.previous();
            break;
          }
        }
      }
      if (activatedPrayers[8] && iterator.hasNext()) {
        if (iterator.next().getDef().isStackable()) {
          iterator.previous();
        }
      }
      for (int slot = 0; iterator.hasNext(); slot++) {
        InvItem item = iterator.next();
        if (item.isWielded()) {
          item.setWield(false);
          updateWornItems(item.getWieldableDef().getWieldPos(),
                          appearance.getSprite(item.getWieldableDef().getWieldPos())
          );
        }
        iterator.remove();
        Entity.world.registerItem(new Item(item.getID(), getX(), getY(), item.getAmount(), player));
      }
      removeSkull();
    }
    Entity.world.registerItem(new Item(20, getX(), getY(), 1, player));
    for (int x = 0; x < activatedPrayers.length; x++) {
      if (activatedPrayers[x]) {
        removePrayerDrain(x);
        activatedPrayers[x] = false;
      }
    }
    actionSender.sendPrayers();
    setLocation(Point.location(122, 647), true);
    Player affectedPlayer = Entity.world.getPlayer(usernameHash);
    Collection<Player> allWatched = watchedPlayers.getAllEntities();
    for (Player p : allWatched) {
      p.removeWatchedPlayer(this);
    }
    resetPath();
    resetCombat(CombatState.LOST);
    actionSender.sendWorldInfo();
    actionSender.sendEquipmentStats();
    actionSender.sendInventory();
    affectedPlayer.incDeaths();
    affectedPlayer.actionSender.sendDeaths();
    affectedPlayer.setKillingSpree(affectedPlayer.getKillingSpree() - affectedPlayer.getKillingSpree());
    affectedPlayer.actionSender.sendKillingSpree();
  }

  public MiscPacketBuilder getActionSender() {
    return actionSender;
  }

  public void incKills() {
    kills++;
  }

  public void incKillingSpree() {
    killingspree++;
  }

  public void teleport(int x, int y, boolean bubble) {
    Mob opponent = this.getOpponent();
    if (inCombat()) {
      resetCombat(CombatState.ERROR);
    }
    if (opponent != null) {
      opponent.resetCombat(CombatState.ERROR);
    }
    for (Object o : getWatchedPlayers().getAllEntities()) {
      Player p = ((Player) o);
      if (bubble) {
        p.getActionSender().sendTeleBubble(getX(), getY(), false);
      }
      p.removeWatchedPlayer(this);
    }
    if (bubble) {
      actionSender.sendTeleBubble(getX(), getY(), false);
    }
    setLocation(Point.location(x, y), true);
    resetPath();
    actionSender.sendWorldInfo();
  }

  public void setSuspiciousPlayer(boolean suspicious) {
    this.suspicious = suspicious;
  }

  public void updateWornItems(int index, int id) {
    wornItems[index] = id;
    this.ourAppearanceChanged = true;
  }

  public PlayerAppearance getPlayerAppearance() {
    return appearance;
  }

  public void removePrayerDrain(int prayerID) {
    PrayerDef prayer = EntityHandler.getPrayerDef(prayerID);
    drainRate -= prayer.getDrainRate();
    if (drainRate <= 0) {
      drainRate = 0;
      drainer.setDelay(Integer.MAX_VALUE);
    } else {
      drainer.setDelay(240000 / drainRate);
    }
  }

  public void removeWatchedPlayer(Player p) {
    watchedPlayers.remove(p);
  }

  public void incDeaths() {
    deaths++;
  }

  public StatefulEntityCollection<Player> getWatchedPlayers() {
    return watchedPlayers;
  }

  public boolean canReport() {
    return System.currentTimeMillis() - lastReport > 60000;
  }

  public void setLastReport() {
    lastReport = System.currentTimeMillis();
  }

  public void setCastTimer() {
    lastSpellCast = System.currentTimeMillis();
  }

  public void setSpellFail() {
    lastSpellCast = System.currentTimeMillis() + 20000;
  }

  public int getSpellWait() {
    return DataConversions.roundUp((double) (1200 - (System.currentTimeMillis() - lastSpellCast)) / 1000D);
  }

  public long getCastTimer() {
    return lastSpellCast;
  }

  public boolean castTimer() {
    return System.currentTimeMillis() - lastSpellCast > 1200;
  }

  // destroy
  public boolean checkAttack(Mob mob, boolean missile) {
    if (mob instanceof Player) {
      Player victim = (Player) mob;
      if ((inCombat() && isDueling()) && (victim.inCombat() && victim.isDueling())) {
        Player opponent = (Player) getOpponent();
        if (opponent != null && victim.equals(opponent)) {
          return true;
        }
      }
      if (System.currentTimeMillis() - mob.getCombatTimer() <
          (mob.getCombatState() == CombatState.RUNNING || mob.getCombatState() == CombatState.WAITING ? 3000 : 500) &&
          !mob.inCombat()) {
        return false;
      }
      int myWildLvl = getLocation().wildernessLevel();
      int victimWildLvl = victim.getLocation().wildernessLevel();
      if (myWildLvl < 1 || victimWildLvl < 1) {
        actionSender.sendMessage("You cannot attack other players outside of the wilderness!");
        return false;
      }
      int combDiff = Math.abs(getCombatLevel() - victim.getCombatLevel());
      if (combDiff > myWildLvl) {
        actionSender.sendMessage("You must move to at least level " +
                                 combDiff +
                                 " wilderness to attack " +
                                 victim.getUsername() +
                                 "!");
        return false;
      }
      if (combDiff > victimWildLvl) {
        actionSender.sendMessage(victim.getUsername() + " is not in high enough wilderness for you to attack!");
        return false;
      }
      return true;
    } else if (mob instanceof Npc) {
      Npc victim = (Npc) mob;
      if (!victim.getDef().isAttackable()) {
        setSuspiciousPlayer(true);
        return false;
      }
      return true;
    }
    return true;
  }

  public boolean equals(Object o) {
    if (o instanceof Player) {
      Player p = (Player) o;
      return usernameHash == p.getUsernameHash();
    }
    return false;
  }

  public void informOfBubble(Bubble b) {
    bubblesNeedingDisplayed.add(b);
  }

  public List<Bubble> getBubblesNeedingDisplayed() {
    return bubblesNeedingDisplayed;
  }

  public void clearBubblesNeedingDisplayed() {
    bubblesNeedingDisplayed.clear();
  }

  public void informOfChatMessage(ChatMessage cm) {
    chatMessagesNeedingDisplayed.add(cm);
  }

  public void sayMessage(String msg, Mob to) {
    ChatMessage cm = new ChatMessage(this, msg, to);
    chatMessagesNeedingDisplayed.add(cm);
  }

  public void informOfNpcMessage(ChatMessage cm) {
    npcMessagesNeedingDisplayed.add(cm);
  }

  public List<ChatMessage> getNpcMessagesNeedingDisplayed() {
    return npcMessagesNeedingDisplayed;
  }

  public List<ChatMessage> getChatMessagesNeedingDisplayed() {
    return chatMessagesNeedingDisplayed;
  }

  public void clearNpcMessagesNeedingDisplayed() {
    npcMessagesNeedingDisplayed.clear();
  }

  public void clearChatMessagesNeedingDisplayed() {
    chatMessagesNeedingDisplayed.clear();
  }

  public void informOfModifiedHits(Mob mob) {
    if (mob instanceof Player) {
      playersNeedingHitsUpdate.add((Player) mob);
    } else if (mob instanceof Npc) {
      npcsNeedingHitsUpdate.add((Npc) mob);
    }
  }

  public List<Player> getPlayersRequiringHitsUpdate() {
    return playersNeedingHitsUpdate;
  }

  public List<Npc> getNpcsRequiringHitsUpdate() {
    return npcsNeedingHitsUpdate;
  }

  public void clearPlayersNeedingHitsUpdate() {
    playersNeedingHitsUpdate.clear();
  }

  public void clearNpcsNeedingHitsUpdate() {
    npcsNeedingHitsUpdate.clear();
  }

  public void informOfProjectile(Projectile p) {
    projectilesNeedingDisplayed.add(p);
  }

  public List<Projectile> getProjectilesNeedingDisplayed() {
    return projectilesNeedingDisplayed;
  }

  public void clearProjectilesNeedingDisplayed() {
    projectilesNeedingDisplayed.clear();
  }

  public void addPrayerDrain(int prayerID) {
    PrayerDef prayer = EntityHandler.getPrayerDef(prayerID);
    drainRate += prayer.getDrainRate();
    drainer.setDelay(240000 / drainRate);
  }

  public boolean isFriendsWith(String username) {
    return friendList.contains(username);
  }

  public boolean isIgnoring(String user) {
    return ignoreList.contains(user);
  }

  public HashSet<String> getIgnoreList() {
    return ignoreList;
  }

  public void removeFriend(String user) {
    friendList.remove(user);
  }

  public void removeIgnore(String user) {
    ignoreList.remove(user);
  }

  public void addFriend(String name) {
    if (friendList.size() >= 50) {
      getActionSender().sendMessage("Sorry your friends list is Full.");
    } else {
      friendList.add(name);
    }
  }

  public void addIgnore(String user) {
    ignoreList.add(user);
  }

  public int friendCount() {
    return friendList.size();
  }

  public int ignoreCount() {
    return ignoreList.size();
  }

  public boolean isTradeConfirmAccepted() {
    return tradeConfirmAccepted;
  }

  public void setTradeConfirmAccepted(boolean b) {
    tradeConfirmAccepted = b;
  }

  public boolean isDuelConfirmAccepted() {
    return duelConfirmAccepted;
  }

  public void setDuelConfirmAccepted(boolean b) {
    duelConfirmAccepted = b;
  }

  public boolean isTradeOfferAccepted() {
    return tradeOfferAccepted;
  }

  public void setTradeOfferAccepted(boolean b) {
    tradeOfferAccepted = b;
  }

  public boolean isDuelOfferAccepted() {
    return duelOfferAccepted;
  }

  public void setDuelOfferAccepted(boolean b) {
    duelOfferAccepted = b;
  }

  public void addToTradeOffer(InvItem item) {
    tradeOffer.add(item);
  }

  public void addToDuelOffer(InvItem item) {
    duelOffer.add(item);
  }

  public ArrayList<InvItem> getTradeOffer() {
    return tradeOffer;
  }

  public boolean getDuelSetting(int i) {
    try {
      for (InvItem item : duelOffer) {
        if (DataConversions.inArray(Formulae.runeIDs, item.getID())) {
          setDuelSetting(1, true);
          break;
        }
      }
      for (InvItem item : wishToDuel.getDuelOffer()) {
        if (DataConversions.inArray(Formulae.runeIDs, item.getID())) {
          setDuelSetting(1, true);
          break;
        }
      }
    } catch (Exception e) {
    }
    return duelOptions[i];
  }

  // IoSession
  public void setDuelSetting(int i, boolean b) {
    duelOptions[i] = b;
  }

  public ArrayList<InvItem> getDuelOffer() {
    return duelOffer;
  }

  public boolean isChangingAppearance() {
    return changingAppearance;
  }

  public void setChangingAppearance(boolean b) {
    changingAppearance = b;
  }

  public boolean isReconnecting() {
    return reconnecting;
  }

  public int getDaysSinceLastLogin() {
    long now = Calendar.getInstance().getTimeInMillis() / 1000;
    return (int) ((now - lastLogin) / 86400);
  }

  public long getCurrentLogin() {
    return currentLogin;
  }

  public String getCurrentIP() {
    return currentIP;
  }

  public int getGroupID() {
    return rank;
  }

  public void setGroupID(int id) {
    rank = id;
  }

  public boolean isSubscriber() {
    return rank == 1 || isEvent() || isPMod() || isMod() || isAdmin();
  }

  public boolean isEvent() {
    return rank == 7 || isPMod() || isMod() || isAdmin();
  }

  public boolean isPMod() {
    return rank == 2 || isMod() || isAdmin();
  }

  public boolean isMod() {
    return rank == 3 || isAdmin();
  }

  public boolean isAdmin() {
    return rank == 4;
  }

  public int getMagicPoints() {
    int points = 1;
    for (InvItem item : inventory.getItems()) {
      if (item.isWielded()) {
        points += item.getWieldableDef().getMagicPoints();
      }
    }
    return points < 1 ? 1 : points;
  }

  public int getPrayerPoints() {
    int points = 1;
    for (InvItem item : inventory.getItems()) {
      if (item.isWielded()) {
        points += item.getWieldableDef().getPrayerPoints();
      }
    }
    return points < 1 ? 1 : points;
  }

  public int getRangePoints() {
    int points = 1;
    for (InvItem item : inventory.getItems()) {
      if (item.isWielded()) {
        points += item.getWieldableDef().getRangePoints();
      }
    }
    return points < 1 ? 1 : points;
  }

  public int[] getWornItems() {
    return wornItems;
  }

  public void setWornItems(int[] worn) {
    wornItems = worn;
    this.ourAppearanceChanged = true;
  }

  public void setGameSetting(int i, boolean b) {
    gameSettings[i] = b;
  }

  public void setPrivacySetting(int i, boolean b) {
    privacySettings[i] = b;
  }

  public long getLastPing() {
    return lastPing;
  }

  public IoSession getSession() {
    return ioSession;
  }

  public void setLoggedIn(boolean loggedIn) {
    if (loggedIn) {
      currentLogin = System.currentTimeMillis();
    }
    this.loggedIn = loggedIn;
  }
  // destroy

  public String getPassword() {
    return password;
  }

  public void ping() {
    lastPing = System.currentTimeMillis();
  }

  public void setAppearance(PlayerAppearance appearance) {
    this.appearance = appearance;
  }

  public void addPlayersAppearanceIDs(int[] indicies, int[] appearanceIDs) {
    for (int x = 0; x < indicies.length; x++) {
      knownPlayersAppearanceIDs.put(indicies[x], appearanceIDs[x]);
    }
  }

  public List<Player> getPlayersRequiringAppearanceUpdate() {
    List<Player> needingUpdates = new ArrayList<Player>();
    needingUpdates.addAll(watchedPlayers.getNewEntities());
    if (this.ourAppearanceChanged) {
      needingUpdates.add(this);
    }
    for (Player p : watchedPlayers.getKnownEntities()) {
      if (needsAppearanceUpdateFor(p)) {
        needingUpdates.add(p);
      }
    }
    return needingUpdates;
  }

  private boolean needsAppearanceUpdateFor(Player p) {
    int playerServerIndex = p.getIndex();
    if (knownPlayersAppearanceIDs.containsKey(playerServerIndex)) {
      int knownPlayerAppearanceID = knownPlayersAppearanceIDs.get(playerServerIndex);
      return knownPlayerAppearanceID != p.getAppearanceID();
    } else {
      return true;
    }
  }

  public void updateViewedPlayers() {
    List<Player> playersInView = viewArea.getPlayersInView();
    for (Player p : playersInView) {
      if (p.getIndex() != getIndex() && p.loggedIn()) {
        this.informOfPlayer(p);
      }
      if (p.isInvisible()) {
        p.informOfPlayer(this);
      }
    }
  }

  public void updateViewedObjects() {
    List<GameObject> objectsInView = viewArea.getGameObjectsInView();
    for (GameObject o : objectsInView) {
      if (!watchedObjects.contains(o) && !o.isRemoved() && withinRange(o)) {
        watchedObjects.add(o);
      }
    }
  }

  public void updateViewedItems() {
    List<Item> itemsInView = viewArea.getItemsInView();
    for (Item i : itemsInView) {
      if (!watchedItems.contains(i) && !i.isRemoved() && withinRange(i) && i.visibleTo(this)) {
        watchedItems.add(i);
      }
    }
  }

  public void updateViewedNpcs() {
    List<Npc> npcsInView = viewArea.getNpcsInView();
    for (Npc n : npcsInView) {
      if ((!watchedNpcs.contains(n) || watchedNpcs.isRemoving(n)) && withinRange(n)) {
        watchedNpcs.add(n);
      }
    }
  }

  /**
   * This is a 'another player has tapped us on the shoulder' method.
   * <p>
   * If we are in another players viewArea, they should in theory be in ours. So they will call this method to let the
   * player know they should probably be informed of them.
   */
  public void informOfPlayer(Player p) {
    if ((!watchedPlayers.contains(p) || watchedPlayers.isRemoving(p)) && withinRange(p)) {
      watchedPlayers.add(p);
    }
  }

  public StatefulEntityCollection<GameObject> getWatchedObjects() {
    return watchedObjects;
  }

  public StatefulEntityCollection<Item> getWatchedItems() {
    return watchedItems;
  }

  public StatefulEntityCollection<Npc> getWatchedNpcs() {
    return watchedNpcs;
  }

  public void removeWatchedNpc(Npc n) {
    watchedNpcs.remove(n);
  }

  public void revalidateWatchedPlayers() {
    for (Player p : watchedPlayers.getKnownEntities()) {
      if (!withinRange(p) || !p.loggedIn()) {
        watchedPlayers.remove(p);
        knownPlayersAppearanceIDs.remove(p.getIndex());
      }
    }
  }

  public boolean loggedIn() {
    return loggedIn;
  }

  public void revalidateWatchedObjects() {
    for (GameObject o : watchedObjects.getKnownEntities()) {
      if (!withinRange(o) || o.isRemoved()) {
        watchedObjects.remove(o);
      }
    }
  }

  public void revalidateWatchedItems() {
    for (Item i : watchedItems.getKnownEntities()) {
      if (!withinRange(i) || i.isRemoved() || !i.visibleTo(this)) {
        watchedItems.remove(i);
      }
    }
  }

  public void revalidateWatchedNpcs() {
    for (Npc n : watchedNpcs.getKnownEntities()) {
      if (!withinRange(n) || n.isRemoved()) {
        watchedNpcs.remove(n);
      }
    }
  }

  public int[] getCurStats() {
    return curStat;
  }

  public int getMaxStat(int id) {
    return maxStat[id];
  }

  public void setMaxStat(int id, int lvl) {
    if (lvl < 0) {
      lvl = 0;
    }
    maxStat[id] = lvl;
  }

  public int[] getMaxStats() {
    return maxStat;
  }

  public int getSkillTotal() {
    int total = 0;
    for (int stat : maxStat) {
      total += stat;
    }
    return total;
  }

  public void incExp(int i, int amount, boolean useFatigue, boolean multiplied) {
    if (GameVars.useFatigue) {
      if (useFatigue) {
        if (fatigue >= 100) {
          actionSender.sendMessage("@gre@You are too tired to gain experience, get some rest!");
          return;
        }
        if (fatigue >= 96) {
          actionSender.sendMessage("@gre@You start to feel tired, maybe you should rest soon.");
        }
        fatigue++;
        actionSender.sendFatigue();
      }
    }
    if (multiplied) {
      amount *= GameVars.expMultiplier;
    }
    exp[i] += amount;
    if (exp[i] < 0) {
      exp[i] = 0;
    }
    int level = Formulae.experienceToLevel(exp[i]);
    if (level != maxStat[i]) {
      int advanced = level - maxStat[i];
      incCurStat(i, advanced);
      incMaxStat(i, advanced);
      actionSender.sendStat(i);
      actionSender.sendMessage("@gre@You just advanced " + advanced + " " + Formulae.statArray[i] + " level!");
      actionSender.sendSound("advance");
      Entity.world.getDelayedEventHandler().add(new MiniEvent(this) {
        public void action() {
          owner.getActionSender().sendScreenshot();
        }
      });
      int comb = Formulae.getCombatlevel(maxStat);
      if (comb != getCombatLevel()) {
        actionSender.sendMessage("@gre@Your combat level is now:@whi@ " + comb + "!");
        actionSender.sendSound("advance");
        setCombatLevel(comb);
      }
    }
  }

  public void incCurStat(int i, int amount) {
    curStat[i] += amount;
    if (curStat[i] < 0) {
      curStat[i] = 0;
    }
  }

  public void incMaxStat(int i, int amount) {
    maxStat[i] += amount;
    if (maxStat[i] < 0) {
      maxStat[i] = 0;
    }
  }

  // destroy
  public int[] getExps() {
    return exp;
  }

  public void setExp(int id, int lvl) {
    if (lvl < 0) {
      lvl = 0;
    }
    exp[id] = lvl;
  }

  public void setExp(int[] lvls) {
    exp = lvls;
  }

}