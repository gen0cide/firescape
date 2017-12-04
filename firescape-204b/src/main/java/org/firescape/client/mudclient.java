package org.firescape.client;

import org.firescape.client.opcode.Command;
import org.firescape.client.opcode.Opcode;
import org.firescape.client.script.Manager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Paths;

public class mudclient extends GameConnection {

  public static final int DEBUG_HUD_ALL = 3;
  public static final int DEBUG_HUD_NAMES = 2;
  public static final int DEBUG_HUD_HITBOXES = 1;
  public static final int DEBUG_HUD_NONE = 0;
  public static int _DEBUG_HUD_ALL = 3;
  public static int _DEBUG_HUD_NAMES = 2;
  public static int _DEBUG_HUD_HITBOXES = 1;
  public static int _DEBUG_HUD_NONE = 0;
  public final int playerStatEquipmentCount = 5;// not sure, could also be the number of messages shown on the screen
  /**
   * Defines the maximum number of items in the right-click context menu.
   *
   * @see mudclient#menuIndices
   * @see mudclient#menuItemX
   * @see mudclient#menuItemY
   * @see mudclient#menuItemText1
   * @see mudclient#menuSourceType
   * @see mudclient#menuSourceIndex
   * @see mudclient#menuTargetIndex
   * @see mudclient#menuItemID
   * @see mudclient#menuItemText2
   */
  private final int menuMaxSize = 250;
  private final int pathStepsMax = 8000;
  /**
   * Defines the maximum number of player characters supported.
   *
   * @see mudclient#playerServerIndexes
   * @see mudclient#players
   * @see mudclient#knownPlayers
   */
  private final int playersMax = 500;
  /**
   * Defines thet maximum number of non-player characters supported.
   *
   * @see mudclient#npcsCache
   * @see mudclient#npcs
   */
  private final int npcsMax = 5000;
  /**
   * Defines the maximum number of wall objects supported.
   *
   * @see mudclient#wallObjectDirection
   * @see mudclient#wallObjectId
   * @see mudclient#wallObjectAlreadyInMenu
   * @see mudclient#wallObjectModel
   * @see mudclient#wallObjectX
   * @see mudclient#wallObjectY
   */
  private final int wallObjectsMax = 500;
  private final int playersServerMax = 4000;
  private final int groundItemsMax = 5000;
  private final int npcsServerMax = 5000;
  private final int objectsMax = 1500;
  /**
   * The number of player skills.
   *
   * @see mudclient#playerStatCurrent
   * @see mudclient#playerStatBase
   * @see mudclient#playerExperience
   */
  private final int playerStatCount = 18;
  /**
   * The number of quests shown to the player and read from the server
   *
   * @see mudclient#questComplete
   * @see mudclient#questName
   */
  private final int questCount = 50;// seems most likely
  public String scriptDir;
  public int debugHud = DEBUG_HUD_NONE;
  /**
   * Represents the players local x coordinate in the current region
   * <p>
   * World coordinate obtained by localRegionX + {@link mudclient#regionX}
   */
  public int localRegionX;
  /**
   * @see mudclient#localRegionX
   */
  public int localRegionY;
  /**
   * Holds the message for messages the player has received that are to be shown above players or npcs heads
   */
  public String receivedMessages[];
  public int wallObjectDirection[];
  public int wallObjectId[];
  public Scene scene;
  public int inventoryMaxItemCount;
  public int receivedMessageX[];
  public int receivedMessageY[];
  public int receivedMessageMidPoint[];
  public int receivedMessageHeight[];
  public SurfaceSprite surface;
  /**
   * The Panel instance for messaging tabs.
   */
  public Panel panelMessageTabs;
  public int regionX;
  public int regionY;
  public boolean errorLoadingData;
  public int healthBarCount;
  public int spriteMedia;
  public int spriteItem;
  public int receivedMessagesCount;
  public GameCharacter players[];
  public int loggedIn;
  public int npcCount;
  public int gameWidth;
  public int gameHeight;
  public int playerStatCurrent[];
  public int playerStatBase[];
  public int groundItemX[];
  public int groundItemY[];
  public int groundItemId[];
  public int groundItemZ[];
  public int objectX[];
  public int objectY[];
  public int objectId[];
  public int objectDirection[];
  public int itemsAboveHeadCount;
  public int statFatigue;
  public int actionBubbleX[];
  public int actionBubbleY[];
  public int playerQuestPoints;
  public String equipmentStatNames[] = {
    "Armour", "WeaponAim", "WeaponPower", "Magic", "Prayer"
  };
  public int inventoryItemsCount;
  public int inventoryItemId[];
  public int inventoryItemStackCount[];
  public int inventoryEquipped[];
  public String skillNameShort[] = {
    "Attack",
    "Defense",
    "Strength",
    "Hits",
    "Ranged",
    "Prayer",
    "Magic",
    "Cooking",
    "Woodcut",
    "Fletching",
    "Fishing",
    "Firemaking",
    "Crafting",
    "Smithing",
    "Mining",
    "Herblaw",
    "Agility",
    "Thieving"
  };
  public int objectCount;
  public int actionBubbleScale[];
  public int actionBubbleItem[];
  public GameCharacter npcs[];
  public int healthBarX[];
  public int healthBarY[];
  public int healthBarMissing[];
  public String skillNameLong[] = {
    "Attack",
    "Defense",
    "Strength",
    "Hits",
    "Ranged",
    "Prayer",
    "Magic",
    "Cooking",
    "Woodcutting",
    "Fletching",
    "Fishing",
    "Firemaking",
    "Crafting",
    "Smithing",
    "Mining",
    "Herblaw",
    "Agility",
    "Thieving"
  };
  public int wallObjectCount;
  public int wallObjectX[];
  public int wallObjectY[];
  public int playerStatEquipment[];
  public World world;
  /**
   * The players index in the server's player array
   * <p>
   * Used to create {@link mudclient#localPlayer}, the player's {@link GameCharacter} reference
   */
  int localPlayerServerIndex;
  /**
   * Handle to the control on the {@link Panel} {@link mudclient#panelMessageTabs} for the chat history text list
   *
   * @see mudclient#messageTabSelected
   */
  int controlTextListChat;
  /**
   * Handle to the control on the {@link Panel} {@link mudclient#panelMessageTabs} for all the history text list, as
   * well as the text input
   *
   * @see mudclient#messageTabSelected
   */
  int controlTextListAll;
  /**
   * Handle to the control on the {@link Panel} {@link mudclient#panelMessageTabs} for the quest history text list
   *
   * @see mudclient#messageTabSelected
   */
  int controlTextListQuest;
  /**
   * Handle to the control on the {@link Panel} {@link mudclient#panelMessageTabs} for the private history text list
   *
   * @see mudclient#messageTabSelected
   */
  int controlTextListPrivate;
  /**
   * Determines which chat history tab is active.
   * <p>
   * Can be 0 through 3 to represent {@link mudclient#controlTextListAll}, {@link mudclient#controlTextListChat}, {@link
   * mudclient#controlTextListQuest} or {@link mudclient#controlTextListPrivate} respetively
   */
  int messageTabSelected;
  /**
   * Stores the x screen coordinate history of our mouse clicks. Only used in {@link mudclient#handleMouseDown(int, int,
   * * int)} to call {@link mudclient#sendLogout} if we've been clicking on the same place for a while (presumably to
   * stop dumb powerminers
   */
  int mouseClickXHistory[];
  /**
   * @see mudclient#mouseClickXHistory
   */
  int mouseClickYHistory[];
  /**
   * The screen x coordinate to draw the little x that comes up when the screen has been clicked
   */
  int mouseClickXX;
  /**
   * @see mudclient#mouseClickXX
   */
  int mouseClickXY;
  /**
   * Handle to the control on the {@link Panel} {@link mudclient#panelSocialList} to list friends or ignores based on
   * {@link mudclient#uiTabSocialSubTab}.
   * <p>
   * Shown when moused over the smiley face ui tab which sets {@link mudclient#showUiTab} = 5
   *
   * @see mudclient#messageTabSelected
   */
  int controlListSocialPlayers;
  /**
   * Which sub tab is selected on the {@link Panel} {@link mudclient#panelSocialList} 0 for friends, 1 for ignore
   * <p>
   * Affects the list shown in {@link mudclient#controlListSocialPlayers}
   */
  int uiTabSocialSubTab;
  /**
   * The hash of the target for the private message box. Set when clicked on an entry in the friend list and {@link
   * mudclient#showDialogSocialInput} = 2
   *
   * @see mudclient #controlListSocialPlayers
   */
  String privateMessageTarget;
  /**
   * The name of the item selected from the inventory and ready to be used on something
   */
  String selectedItemName;
  /**
   * Handle to the control on the {@link Panel} {@link mudclient#panelQuestList} to list stats or quests based on {@link
   * mudclient#uiTabPlayerInfoSubTab}.
   * <p>
   * Shown when moused over the chart ui tab which sets {@link mudclient#showUiTab} = 3
   *
   * @see mudclient#messageTabSelected
   */
  int controlListQuest;
  /**
   * Which sub tab is selected on the player info ui tab 0 for player stats, 1 for {@link mudclient#controlListQuest}
   */
  int uiTabPlayerInfoSubTab;
  int controlListMagic;
  int tabMagicPrayer;
  int reportReason = 0;
  private boolean inputPopupSubmit;
  private int inputPopupType;
  private int inputPopupWidth;
  private int inputPopupHeight;
  private String[] inputPopupText;
  private boolean inputPopupShowInput;
  private int alphaizeDistMult = 18;
  private int messageShitSize = Version.CLIENT > 204 ? 100 : 10;
  private boolean zoomControls = false;
  private int axisRotation = 912;
  private boolean useJmFormat = false;
  private String reportName = "";
  private boolean reportMutePlayer = false;
  private int spriteCrowns;
  private int[] messageTypes = new int[messageShitSize];
  private String[] messageSenders = new String[messageShitSize];
  private int[] messageCrowns = new int[messageShitSize];
  private String[] messageSenderClans = new String[messageShitSize];
  private String[] messageMessages = new String[messageShitSize];
  private String[] messageColor = new String[messageShitSize];
  private String[] messageColors = new String[] {
    "@whi@", "@cya@", "@cya@", "@whi@", "@yel@", "@cya@", "@whi@", "@whi@"
  };
  private int packetErrorCount;
  private int menuIndices[];
  private boolean cameraAutoAngleDebug;
  private int mouseButtonDownTime;
  private int mouseButtonItemCountIncrement;
  private int anInt659;
  private int anInt660;
  private int cameraRotationX;
  private int cameraRotationXIncrement;
  private int bankItemsMax;
  private String optionMenuEntry[];
  private int newBankItems[];
  private int newBankItemsCount[];
  private int showDialogReportAbuseStep;
  private int loginScreen;
  private int teleportBubbleTime[];
  private boolean showDialogTradeConfirm;
  private boolean tradeConfirmAccepted;
  private int menuItemX[];
  private String menuTextVar[];
  private int menuItemY[];
  private boolean showDialogTrade;
  private int bankItems[];
  private int bankItemsCount[];
  private StreamAudioPlayer audioPlayer;
  private int appearanceHeadType;
  private int appearanceBodyGender;
  private int appearance2Colour;
  private int appearanceHairColour;
  private int appearanceTopColour;
  private int appearanceBottomColour;
  private int appearanceSkinColour;
  private int appearanceHeadGender;
  private String loginUser;
  private String loginPass;
  private int showDialogSocialInput;
  private int cameraAngle;
  private int anInt707;
  private boolean members;
  private int deathScreenTimeout;
  private boolean optionSoundDisabled;
  private boolean showRightClickMenu;
  private int cameraRotationY;
  private int cameraRotationYIncrement;
  private boolean objectAlreadyInMenu[];
  private int combatStyle;
  private String menuItemText1[];
  private int welcomeUnreadMessages;
  private int controlButtonAppearanceHead1;
  private int controlButtonAppearanceHead2;
  private int controlButtonAppearanceHair1;
  private int controlButtonAppearanceHair2;
  private int controlButtonAppearanceGender1;
  private int controlButtonAppearanceGender2;
  private int controlButtonAppearanceTop1;
  private int controlButtonAppearanceTop2;
  private int controlButtonAppearanceSkin1;
  private int controlButtonAppearanceSkin2;
  private int controlButtonAppearanceBottom1;
  private int controlButtonAppearanceBottom2;
  private int controlButtonAppearanceAccept;
  private int logoutTimeout;
  private long tradeRecipientConfirmHash;
  private String tradeRecipientConfirmName;
  private int loginTimer;
  private int npcCombatModelArray2[] = {
    0, 0, 0, 0, 0, 1, 2, 1
  };
  private int systemUpdate;
  private String duelOpponentName;
  private int lastObjectAnimationNumberFireLightningSpell;
  private int lastObjectAnimationNumberTorch;
  private int lastOjectAnimationNumberClaw;
  private Graphics graphics;
  private int planeIndex;
  //private String aString744;// unused
  private boolean welcomScreenAlreadyShown;
  private int mouseButtonClick;
  private boolean isSleeping;
  private int cameraRotation;
  private String questName[] = {
    "Black knight's fortress",
    "Cook's assistant",
    "Demon slayer",
    "Doric's quest",
    "The restless ghost",
    "Goblin diplomacy",
    "Ernest the chicken",
    "Imp catcher",
    "Pirate's treasure",
    "Prince Ali rescue",
    "Romeo & Juliet",
    "Sheep shearer",
    "Shield of Arrav",
    "The knight's sword",
    "Vampire slayer",
    "Witch's potion",
    "Dragon slayer",
    "Witch's house (members)",
    "Lost city (members)",
    "Hero's quest (members)",
    "Druidic ritual (members)",
    "Merlin's crystal (members)",
    "Scorpion catcher (members)",
    "Family crest (members)",
    "Tribal totem (members)",
    "Fishing contest (members)",
    "Monk's friend (members)",
    "Temple of Ikov (members)",
    "Clock tower (members)",
    "The Holy Grail (members)",
    "Fight Arena (members)",
    "Tree Gnome Village (members)",
    "The Hazeel Cult (members)",
    "Sheep Herder (members)",
    "Plague City (members)",
    "Sea Slug (members)",
    "Waterfall quest (members)",
    "Biohazard (members)",
    "Jungle potion (members)",
    "Grand tree (members)",
    "Shilo village (members)",
    "Underground pass (members)",
    "Observatory quest (members)",
    "Tourist trap (members)",
    "Watchtower (members)",
    "Dwarf Cannon (members)",
    "Murder Mystery (members)",
    "Digsite (members)",
    "Gertrude's Cat (members)",
    "Legend's Quest (members)"
  };
  private int teleportBubbleX[];
  private int playerExperience[];
  private int spriteUtil;
  private int spriteProjectile;
  private int spriteTexture;
  private int spriteTextureWorld;
  private int spriteLogo;
  private int controlLoginStatus;
  private int controlLoginUser;
  private int controlLoginPass;
  private int controlLoginOk;
  private int controlLoginCancel;
  private boolean tradeRecipientAccepted;
  private boolean tradeAccepted;
  private int teleportBubbleCount;
  private int mouseClickCount;
  private int shopSellPriceMod;
  private int shopBuyPriceMod;
  private int shopPriceMultiplier;
  private boolean showDialogWelcome;
  private int duelOptionRetreat;
  private int duelOptionMagic;
  private int duelOptionPrayer;
  private int duelOptionWeapons;
  private int playerServerIndexes[];
  private int groundItemCount;
  private int teleportBubbleY[];
  private int messageTabFlashAll;
  private int messageTabFlashHistory;
  private int messtageTabFlashQuest;
  private int messageTabFlashPrivate;
  private boolean showDialogDuelConfirm;
  private boolean duelAccepted;
  private int bankItemCount;
  private boolean prayerOn[];
  //private String aString793;// unused
  private int menuSourceType[];
  private int menuSourceIndex[];
  private int menuTargetIndex[];
  private boolean wallObjectAlreadyInMenu[];
  private int objectAnimationNumberFireLightningSpell;
  private int objectAnimationNumberTorch;
  private int objectAnimationNumberClaw;
  private int magicLoc;
  private int npcCacheCount;
  private int objectAnimationCount;
  private boolean errorLoadingMemory;
  private boolean fogOfWar;
  private int const_9;
  private int tradeConfirmItemsCount;
  private int tradeConfirmItems[];
  private int tradeConfirmItemCount[];
  private String tradeRecipientName;
  private int selectedSpell;
  private boolean showOptionMenu;
  private int mouseClickXStep;
  private int newBankItemCount;
  private int npcAnimationArray[][] = {
    {
      11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4
    }, {
      11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4
    }, {
      11, 3, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4
    }, {
      3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5
    }, {
      3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5
    }, {
      4, 3, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5
    }, {
      11, 4, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3
    }, {
      11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4, 3
    }
  };
  private int controlWelcomeNewuser;
  private int controlWelcomeExistinguser;
  private int npcWalkModel[] = {
    0, 1, 2, 1
  };
  private int referid;
  private int anInt827;
  private int controlLoginNewOk;
  private int teleportBubbleType[];
  private Panel panelLoginWelcome;
  private int combatTimeout;
  private Panel panelLoginNewuser;
  private int optionMenuCount;
  private boolean errorLoadingCodebase;
  private boolean showDialogShop;
  private int shopItem[];
  private int shopItemCount[];
  private int shopItemPrice[];
  private boolean duelOfferOpponentAccepted;
  private boolean duelOfferAccepted;
  private GameModel gameModels[];
  private int reportAbuseOffence;
  private boolean showDialogDuel;
  private String serverMessage;
  private boolean serverMessageBoxTop;
  private int cameraRotationTime;
  private int duelOpponentItemsCount;
  private int duelOpponentItems[];
  private int duelOpponentItemCount[];
  private int duelItemsCount;
  private int duelItems[];
  private int duelItemCount[];
  private Panel panelSocialList;
  private GameCharacter npcsCache[];
  private boolean appletMode;
  private int characterSkinColours[] = {
    0xecded0, 0xccb366, 0xb38c40, 0x997326, 0x906020
  };
  private int bankSelectedItemSlot;
  private int bankSelectedItem;
  private int duelOfferOpponentItemCount;
  private int duelOfferOpponentItemId[];
  private int duelOfferOpponentItemStack[];
  private int messageHistoryTimeout[];
  private boolean optionCameraModeAuto;
  private int characterTopBottomColours[] = {
    0xff0000,
    0xff8000,
    0xffe000,
    0xa0e000,
    57344,
    32768,
    41088,
    45311,
    33023,
    12528,
    0xe000e0,
    0x303030,
    0x604000,
    0x805000,
    0xffffff
  };
  private int showUiWildWarn;
  private int selectedItemInventoryIndex;
  private byte soundData[];
  private int fatigueSleeping;
  private boolean loadingArea;
  private int tradeRecipientConfirmItemsCount;
  private int tradeRecipientConfirmItems[];
  private int tradeRecipientConfirmItemCount[];
  private int tradeRecipientItemsCount;
  private int tradeRecipientItems[];
  private int tradeRecipientItemCount[];
  private boolean showDialogServermessage;
  private int menuItemID[];
  private boolean questComplete[];
  private GameModel wallObjectModel[];
  private int menuX;
  private int menuY;
  private int menuWidth;
  private int menuHeight;
  private int menuItemsCount;
  private Panel panelQuestList;
  private int cameraZoom;
  private Panel panelMagic;
  private int showUiTab;
  private int tradeItemsCount;
  private int tradeItems[];
  private int tradeItemCount[];
  private int planeWidth;
  private int planeHeight;
  private int planeMultiplier;
  private int lastHeightOffset;
  //private int anInt917;// unused
  private boolean duelSettingsRetreat;
  private boolean duelSettingsMagic;
  private boolean duelSettingsPrayer;
  private boolean duelSettingsWeapons;
  private boolean showDialogBank;
  private String loginUserDesc;
  private String loginUserDisp;
  private int characterHairColours[] = {
    0xffc030, 0xffa040, 0x805030, 0x604020, 0x303030, 0xff6020, 0xff4000, 0xffffff, 65280, 65535
  };
  private int bankActivePage;
  private int welcomeLastLoggedInDays;
  private boolean optionMouseButtonOne;
  private GameCharacter knownPlayers[];
  private String messageHistory[];
  private long duelOpponentNameHash;
  private Panel panelAppearance;
  private int minimapRandom_1;
  private int minimapRandom_2;
  private Panel panelLoginExistinguser;
  private boolean reportAbuseMute;
  private int duelOfferItemCount;
  private int duelOfferItemId[];
  private int duelOfferItemStack[];
  private int cameraAutoRotatePlayerX;
  private int cameraAutoRotatePlayerY;
  private boolean sleepWordDelay;
  private boolean showAppearanceChange;
  private int shopSelectedItemIndex;
  private int shopSelectedItemType;
  private int projectileMaxRange;
  private String sleepingStatusText;
  private int npcCombatModelArray1[] = {
    0, 1, 2, 1, 0, 0, 0, 0
  };
  private int experienceArray[];
  private GameCharacter playerServer[];
  private int playerCount;
  private int knownPlayerCount;
  private int spriteCount;
  private int walkPathX[];
  private int walkPathY[];
  private String welcomeLastLoggedInHost;
  private int welcomeRecoverySetDays;
  private int localLowerX;
  private int localLowerY;
  private int localUpperX;
  private int localUpperY;
  private int welcomeLastLoggedInIP;
  private String menuItemText2[];
  private GameCharacter npcsServer[];
  private int sleepWordDelayTimer;
  private GameModel objectModel[];
  private boolean inTutorial;

  public mudclient() {
    menuIndices = new int[menuMaxSize];
    cameraAutoAngleDebug = false;
    wallObjectDirection = new int[wallObjectsMax];
    wallObjectId = new int[wallObjectsMax];
    cameraRotationXIncrement = 2;
    inventoryMaxItemCount = 30;
    bankItemsMax = 48;
    optionMenuEntry = new String[5];
    newBankItems = new int[256];
    newBankItemsCount = new int[256];
    teleportBubbleTime = new int[50];
    showDialogTradeConfirm = false;
    tradeConfirmAccepted = false;
    receivedMessageX = new int[500];
    receivedMessageY = new int[500];
    receivedMessageMidPoint = new int[500];
    receivedMessageHeight = new int[500];
    localPlayer = new GameCharacter();
    localPlayerServerIndex = -1;
    menuItemX = new int[menuMaxSize];
    menuTextVar = new String[menuMaxSize];
    menuItemY = new int[menuMaxSize];
    showDialogTrade = false;
    bankItems = new int[256];
    bankItemsCount = new int[256];
    appearanceBodyGender = 1;
    appearance2Colour = 2;
    appearanceHairColour = 2;
    appearanceTopColour = 8;
    appearanceBottomColour = 14;
    appearanceHeadGender = 1;
    loginUser = "";
    loginPass = "";
    cameraAngle = 1;
    members = false;
    optionSoundDisabled = false;
    showRightClickMenu = false;
    cameraRotationYIncrement = 2;
    objectAlreadyInMenu = new boolean[objectsMax];
    menuItemText1 = new String[menuMaxSize];
    duelOpponentName = "";
    lastObjectAnimationNumberFireLightningSpell = -1;
    lastObjectAnimationNumberTorch = -1;
    lastOjectAnimationNumberClaw = -1;
    planeIndex = -1;
    //aString744 = "";// unused
    welcomScreenAlreadyShown = false;
    isSleeping = false;
    cameraRotation = 128;
    teleportBubbleX = new int[50];
    errorLoadingData = false;
    playerExperience = new int[playerStatCount];
    tradeRecipientAccepted = false;
    tradeAccepted = false;
    mouseClickXHistory = new int[8192];
    mouseClickYHistory = new int[8192];
    showDialogWelcome = false;
    playerServerIndexes = new int[playersMax];
    teleportBubbleY = new int[50];
    receivedMessages = new String[500];
    showDialogDuelConfirm = false;
    duelAccepted = false;
    players = new GameCharacter[playersMax];
    prayerOn = new boolean[50];
    //aString793 = "";// unused
    menuSourceType = new int[menuMaxSize];
    menuSourceIndex = new int[menuMaxSize];
    menuTargetIndex = new int[menuMaxSize];
    wallObjectAlreadyInMenu = new boolean[wallObjectsMax];
    magicLoc = 128;
    errorLoadingMemory = false;
    fogOfWar = false;
    gameWidth = 512; // 512
    gameHeight = 334; // 334
    const_9 = 9;
    tradeConfirmItems = new int[14];
    tradeConfirmItemCount = new int[14];
    tradeRecipientName = "";
    selectedSpell = -1;
    showOptionMenu = false;
    playerStatCurrent = new int[playerStatCount];
    teleportBubbleType = new int[50];
    errorLoadingCodebase = false;
    showDialogShop = false;
    shopItem = new int[256];
    shopItemCount = new int[256];
    shopItemPrice = new int[256];
    duelOfferOpponentAccepted = false;
    duelOfferAccepted = false;
    gameModels = new GameModel[1000];
    showDialogDuel = false;
    serverMessage = "";
    serverMessageBoxTop = false;
    duelOpponentItems = new int[8];
    duelOpponentItemCount = new int[8];
    duelItems = new int[8];
    duelItemCount = new int[8];
    playerStatBase = new int[playerStatCount];
    npcsCache = new GameCharacter[npcsMax];
    appletMode = true;
    groundItemX = new int[groundItemsMax];
    groundItemY = new int[groundItemsMax];
    groundItemId = new int[groundItemsMax];
    groundItemZ = new int[groundItemsMax];
    bankSelectedItemSlot = -1;
    bankSelectedItem = -2;
    duelOfferOpponentItemId = new int[8];
    duelOfferOpponentItemStack = new int[8];
    messageHistoryTimeout = new int[messageShitSize]; // lelele
    optionCameraModeAuto = true;
    objectX = new int[objectsMax];
    objectY = new int[objectsMax];
    objectId = new int[objectsMax];
    objectDirection = new int[objectsMax];
    selectedItemInventoryIndex = -1;
    selectedItemName = "";
    loadingArea = false;
    tradeRecipientConfirmItems = new int[14];
    tradeRecipientConfirmItemCount = new int[14];
    tradeRecipientItems = new int[14];
    tradeRecipientItemCount = new int[14];
    showDialogServermessage = false;
    menuItemID = new int[menuMaxSize];
    questComplete = new boolean[questCount];
    wallObjectModel = new GameModel[wallObjectsMax];
    actionBubbleX = new int[50];
    actionBubbleY = new int[50];
    cameraZoom = 550;
    tradeItems = new int[14];
    tradeItemCount = new int[14];
    lastHeightOffset = -1;
    //anInt917 = 12345678;// unused
    duelSettingsRetreat = false;
    duelSettingsMagic = false;
    duelSettingsPrayer = false;
    duelSettingsWeapons = false;
    showDialogBank = false;
    loginUserDesc = "";
    loginUserDisp = "";
    optionMouseButtonOne = false;
    inventoryItemId = new int[35];
    inventoryItemStackCount = new int[35];
    inventoryEquipped = new int[35];
    knownPlayers = new GameCharacter[playersMax];
    messageHistory = new String[messageShitSize];
    reportAbuseMute = false;
    duelOfferItemId = new int[8];
    duelOfferItemStack = new int[8];
    actionBubbleScale = new int[50];
    actionBubbleItem = new int[50];
    sleepWordDelay = true;
    showAppearanceChange = false;
    shopSelectedItemIndex = -1;
    shopSelectedItemType = -2;
    projectileMaxRange = 40;
    npcs = new GameCharacter[npcsMax];
    experienceArray = new int[99];
    healthBarX = new int[50];
    healthBarY = new int[50];
    healthBarMissing = new int[50];
    playerServer = new GameCharacter[playersServerMax];
    walkPathX = new int[pathStepsMax];
    walkPathY = new int[pathStepsMax];
    wallObjectX = new int[wallObjectsMax];
    wallObjectY = new int[wallObjectsMax];
    menuItemText2 = new String[menuMaxSize];
    npcsServer = new GameCharacter[npcsServerMax];
    playerStatEquipment = new int[playerStatEquipmentCount];
    objectModel = new GameModel[objectsMax];
    inTutorial = false;
    shopPriceMultiplier = 0;
  }

  public static void main(String args[]) {
    mudclient mc = new mudclient();

    mc.appletMode = false;

    // default args
    mc.members = true;
    mc.limit30 = false;
    mc.server = "classic4.runescape.com";
    mc.port = 43594;
    Version.CLIENT = 204;
    mc.username = "";
    mc.password = "";
    mc.gameWidth = 512;
    mc.gameHeight = 334;
    mc.useJmFormat = false;
    mc.scriptDir = "conf/scripts";

    if (args.length > 0) {
      try {
        for (int i = 0; i < args.length; i++) {
          switch (args[i]) {
            case "-m":
            case "--members":
              mc.members = true;
              break;
            case "-l":
            case "--limit30":
              mc.limit30 = true;
              break;
            case "-s":
            case "--server":
              mc.server = args[i + 1];
              break;
            case "-p":
            case "--port":
              mc.port = Integer.parseInt(args[i + 1]);
              break;
            case "-v":
            case "--version":
              Version.CLIENT = Integer.parseInt(args[i + 1]);
              break;
            case "--username":
              mc.the_username = args[i + 1];
              break;
            case "--password":
              mc.the_password = args[i + 1];
              break;
            case "-w":
            case "--width":
              mc.gameWidth = Integer.parseInt(args[i + 1]);
              break;
            case "-h":
            case "--height":
              mc.gameHeight = Integer.parseInt(args[i + 1]);
              break;
            case "-j":
            case "--use-jm":
              mc.useJmFormat = true;
              break;
            case "-d":
            case "--script-dir":
              mc.scriptDir = args[i + 1];
              break;
          }
        }
      } catch (Exception exemplar) {
        System.out.println("Exception while parsing arguments:");
        exemplar.printStackTrace();
      }
    }

    Manager.addBinding("mc", mc);
    Manager.addBinding("Version", Version.class);
    Manager.addBinding("GameData", GameData.class);
    Manager.addBinding("Utility", Utility.class);
    Manager.addBinding("Manager", Manager.class);
    Manager.load(Paths.get(mc.scriptDir));

    if (Version.CLIENT > 204) {
      Version.MAPS = Version.MAPS_233;
      Version.MEDIA = Version.MEDIA_233;
    }

    System.out.println(String.format("members=%s limit30=%s server=%s port=%d version=%d",
                                     String.valueOf(mc.members),
                                     String.valueOf(mc.limit30),
                                     mc.server,
                                     mc.port,
                                     Version.CLIENT
    ));

    mc.startApplication(mc.gameWidth, mc.gameHeight + 11, "Runescape by Andrew Gower", false);
    mc.threadSleep = 10;
  }

  private static String formatNumber(int i) // wonder why this wasn't in the utility class
  {
    String s = String.valueOf(i);
    for (int j = s.length() - 3; j > 0; j -= 3) {
      s = s.substring(0, j) + "," + s.substring(j);
    }

    if (s.length() > 8) {
      s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
    } else if (s.length() > 4) {
      s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
    }
    return s;
  }

  private void showInputPopup(int type, String[] text, boolean showInput) {
    showInputPopup(type, text, 0, "", showInput);
  }

  private void showInputPopup(int type, String[] text, int textStartIndex, String defaultInput, boolean showInput) {
    inputPopupText = text;
    inputPopupWidth = 400;

    for (int i = textStartIndex; text.length > i; ++i) {
      int w = surface.textWidth(text[i], 1) + 10;
      if (w > inputPopupWidth) {
        inputPopupWidth = w;
      }
    }

    inputPopupHeight = 15 + (surface.textHeight(1) + 2) * (text.length + 1) + surface.textHeight(4);
    inputPopupShowInput = showInput;
    inputPopupType = type;
    inputPopupSubmit = false;
    super.inputTextCurrent = defaultInput;
    super.inputTextFinal = "";
  }

  private void drawInputPopup() {
    if (super.inputTextFinal.length() <= 0 && !inputPopupSubmit) {
      if (inputPopupType >= 1 && inputPopupType <= 8) {
        String validInput = "";

        for (int i = 0; super.inputTextCurrent.length() > i; ++i) {
          char chr = super.inputTextCurrent.charAt(i);
          if (java.lang.Character.isDigit(chr)) {
            validInput = validInput + chr;
          }
        }

        super.inputTextCurrent = validInput;
      }

      int dialogX = gameWidth / 2 - inputPopupWidth / 2; // 256
      int dialogY = gameHeight / 2 + 13 - inputPopupHeight / 2; // 180
      surface.drawBox(dialogX, dialogY, inputPopupWidth, inputPopupHeight, 0);
      surface.drawBoxEdge(dialogX, dialogY, inputPopupWidth, inputPopupHeight, 0xffffff);
      int font1Height = surface.textHeight(1);
      int font4Height = surface.textHeight(4);
      int font1HeightWithSomeFreeSpace = 2 + font1Height;

      for (int i = 0; i < inputPopupText.length; ++i) {
        surface.drawstring(inputPopupText[i],
                           gameWidth / 2 - surface.textWidth(inputPopupText[i], 1) / 2,
                           5 + dialogY + font1Height + i * font1HeightWithSomeFreeSpace,
                           1,
                           0xffff00
        );
      }

      if (inputPopupShowInput) {
        surface.drawstring(super.inputTextCurrent + "*",
                           gameWidth / 2 - surface.textWidth(super.inputTextCurrent + "*", 4) / 2,
                           font1HeightWithSomeFreeSpace * inputPopupText.length + dialogY + 5 + 3 + font4Height,
                           4,
                           0xffffff
        );
      }

      int color = 0xffffff;
      int y = font4Height + dialogY -
              (-5 + (-(font1HeightWithSomeFreeSpace * inputPopupText.length) + -3 - 2) - font1Height);
      if (super.mouseX > gameWidth / 2 - 26 &&
          super.mouseX < gameWidth / 2 - 8 &&
          super.mouseY > y - font1Height &&
          super.mouseY < y) {
        if (mouseButtonClick != 0) {
          mouseButtonClick = 0;
          super.inputTextFinal = super.inputTextCurrent;
          inputPopupSubmit = true;
        }

        color = 0xffff00;
      }

      surface.drawstring("OK", gameWidth / 2 - 26, y, 1, color);
      color = 0xffffff;
      if (super.mouseX > gameWidth / 2 + 8 &&
          super.mouseX < gameHeight / 2 + 137 &&
          y - font1Height < super.mouseY &&
          super.mouseY < y) {
        color = 0xffff00;
        if (mouseButtonClick != 0) {
          mouseButtonClick = 0;
          inputPopupType = 0;
        }
      }

      surface.drawstring("Cancel", gameWidth / 2 + 8, y, 1, color); // 264
      if (mouseButtonClick == 1) {
        if (super.mouseX < dialogX ||
            (inputPopupWidth + dialogX) < super.mouseX ||
            dialogY > super.mouseY ||
            super.mouseY > (inputPopupHeight + dialogY)) {
          inputPopupType = 0;
          mouseButtonClick = 0;
        }

      }
    } else {
      String input = super.inputTextFinal.trim();
      super.inputTextFinal = "";
      super.inputTextCurrent = "";
      if (inputPopupType ==
          1) { // offer trade item, would require implementing some ebin right click shit in the trade windows so CANT FUCKING BOTHER
        try {
          //this.addTradeOffer(this.anInt1393, var1 + 29843, Integer.parseInt(input));
        } catch (NumberFormatException nex) {
        }
      } else if (inputPopupType == 2) { // update trade item (probably remove offer) see asbove
        try {
          //this.updateTradeItems(this.anInt1393, Integer.parseInt(input), var1 + 29853);
        } catch (NumberFormatException nex) {
        }
      } else if (inputPopupType == 3) { // bank withdraw
        try {
          int item;
          if (bankSelectedItemSlot < 0) {
            item = -1;
          } else {
            item = bankItems[bankSelectedItemSlot];
          }
          int amount = Integer.parseInt(input);
          clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
          clientStream.putShort(item);
          clientStream.putInt(amount);
          clientStream.putInt(0x12345678);
          clientStream.sendPacket();
        } catch (NumberFormatException nex) {
        }
      } else if (inputPopupType == 4) { // bank deposit
        try {
          int item;
          if (bankSelectedItemSlot < 0) {
            item = -1;
          } else {
            item = bankItems[bankSelectedItemSlot];
          }
          int amount = Integer.parseInt(input);
          clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
          clientStream.putShort(item);
          clientStream.putInt(amount);
          clientStream.putInt(-0x789abcdf);
          clientStream.sendPacket();
        } catch (NumberFormatException nex) {
        }
      } else if (inputPopupType == 5) { // shop buy
        try {
          int item = shopItem[shopSelectedItemIndex];
          if (item != -1) {
            int amount = Integer.parseInt(input);
            clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SHOP_BUY));
            clientStream.putShort(item);
            clientStream.putShort(shopItemCount[shopSelectedItemIndex]);
            clientStream.putShort(amount);
            clientStream.sendPacket();
          }
        } catch (NumberFormatException nex) {
        }
      } else if (inputPopupType == 6) { // shop sell
        try {
          int item = shopItem[shopSelectedItemIndex];
          if (item != -1) {
            int amount = Integer.parseInt(input);
            clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SHOP_SELL));
            clientStream.putShort(shopItem[shopSelectedItemIndex]);
            clientStream.putShort(shopItemCount[shopSelectedItemIndex]);
            clientStream.putShort(amount);
            clientStream.sendPacket();
          }
        } catch (NumberFormatException nex) {
        }
      } else if (inputPopupType == 7) { // probably offer item in duel see above
        try {
          //this.drawDialogDuel(var1 ^ -29788, Integer.parseInt(input), this.anInt1460);
        } catch (NumberFormatException nex) {
        }
      } else if (inputPopupType == 8) { // probably remove item from duel offer see above
        try {
          //this.updateDuelItems(this.anInt1460, 5, Integer.parseInt(input));
        } catch (NumberFormatException nex) {
        }
      } else if (inputPopupType == 9) { // skip tutorial
        clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SKIP_TUTORIAL));
        clientStream.sendPacket();
      } else {
        Manager.run("handleInputPopup", inputPopupType, input);
      }
      inputPopupType = 0;
    }
  }

  private void showInputPopup() {

  }

  private void playSoundFile(String s) {
    showMessage(0, "Play sound: " + s, null, null, 0, null, false);
    if (audioPlayer == null) {
      return;
    }
    if (!optionSoundDisabled) {
      audioPlayer.writeStream(soundData,
                              Utility.getDataFileOffset(s + ".pcm", soundData),
                              Utility.getDataFileLength(s + ".pcm", soundData)
      );
    }
  }

  private void drawDialogReportAbuseInputNew() {
    if (super.inputTextFinal.length() > 0) {
      reportName = super.inputTextFinal.trim();
      showDialogReportAbuseStep = 2;
      reportReason = 0;
    } else {
      byte admin = 0;
      if (super.moderatorLevel2 < 2 && moderatorLevel1 < 7) {
        if (moderatorLevel1 >= 5) {
          admin = 1;
        }
      } else {
        admin = 2;
      }

      int textHeight1 = surface.textHeight(1);
      int textHeight4 = surface.textHeight(4);
      int dialogW = 400;
      int dialogH = (admin > 0 ? textHeight1 + 5 : 0) + 70;
      int dialogX = gameWidth / 2 - dialogW / 2;
      int dialogY = gameHeight / 2 + 13 - dialogH / 2;
      surface.drawBox(dialogX, dialogY, dialogW, dialogH, 0);
      surface.drawBoxEdge(dialogX, dialogY, dialogW, dialogH, 0xffffff);
      surface.drawStringCenter("Enter the name of the player you wish to report:",
                               gameWidth / 2,
                               dialogY + textHeight1 + 5,
                               1,
                               0xffff00
      );
      int textHeight12 = textHeight1 + 2;
      surface.drawStringCenter(super.inputTextCurrent + "*",
                               gameWidth / 2,
                               dialogY + 5 + textHeight4 + 3 + textHeight12,
                               4,
                               0xffffff
      );
      int textHeightAyy = dialogY + 3 + textHeight1 + textHeight12 + 5 + textHeight4 + 2;
      int color = 0xffffff;
      if (admin > 0) {
        String sel = reportMutePlayer ? "[X]" : "[ ]";
        if (admin > 1) {
          sel += " Mute player";
        } else {
          sel += " Suggest mute";
        }
        int textWidth1 = surface.textWidth(sel, 1);
        if (super.mouseX > gameWidth / 2 - textWidth1 / 2 &&
            super.mouseX < gameWidth / 2 + textWidth1 / 2 &&
            super.mouseY > textHeightAyy - textHeight1 &&
            super.mouseY < textHeightAyy) {
          color = 0xffff00;
          if (mouseButtonClick != 0) {
            mouseButtonClick = 0;
            reportMutePlayer = !reportMutePlayer;
          }
        }
        surface.drawStringCenter(sel, gameWidth / 2, textHeightAyy, 1, color);
        textHeightAyy += textHeight1 + 10;
      }

      color = 0xffffff;
      if (super.mouseX > gameWidth / 2 - 46 &&
          super.mouseX < gameWidth / 2 - 28 &&
          super.mouseY > textHeightAyy - textHeight1 &&
          super.mouseY < textHeightAyy) {
        if (mouseButtonClick != 0) {
          super.inputTextFinal = super.inputTextCurrent;
          mouseButtonClick = 0;
        }
        color = 0xffff00;
      }
      surface.drawstring("OK", gameWidth / 2 - 46, textHeightAyy, 1, color);

      color = 0xffffff;
      if (super.mouseX > gameWidth / 2 + 8 &&
          super.mouseX < gameWidth / 2 + 48 &&
          super.mouseY > textHeightAyy - textHeight1 &&
          super.mouseY < textHeightAyy) {
        if (mouseButtonClick != 0) {
          showDialogReportAbuseStep = 0;
          mouseButtonClick = 0;
        }
        color = 0xffff00;
      }
      surface.drawstring("Cancel", gameWidth / 2 + 8, textHeightAyy, 1, color);

      if (mouseButtonClick == 1) {
        if (super.mouseX < dialogX ||
            super.mouseX > dialogX + dialogW ||
            super.mouseY < dialogY ||
            super.mouseY > dialogY + dialogX) {
          showDialogReportAbuseStep = 0;
          mouseButtonClick = 0;
        }
      }
    }
  }

  private void drawDialogReportAbuseNew() {
    // drawBox: y color w boolean dialogX h
    // drawBoxEdge: y w boolean dialogX h color

    reportReason = 0;
    boolean reporTED = true;
    if (super.mouseX >= gameWidth / 2 - 220 && super.mouseX < gameWidth / 2 - 80) {
      reportReason = 1;
    } else if (super.mouseX >= gameWidth / 2 - 70 && super.mouseX < gameWidth / 2 + 70) {
      reportReason = 7;
    } else if (super.mouseX >= gameWidth / 2 + 80 && super.mouseX < gameWidth / 2 + 220) {
      reportReason = 12;
    } else {
      reporTED = false;
    }

    int y = gameHeight / 2 - 11;
    if (reporTED) {
      reporTED = false;
      for (int i = 0; i < 6; i++) {
        int var5 = i == 0 ? 30 : 18;
        if (super.mouseY > y - 12 && super.mouseY < y - 12 + var5) {
          if (reportReason == 1) {
            reporTED = true;
            reportReason += i;
            break;
          }
          if (reportReason == 7) {
            if (i < 5) {
              reporTED = true;
              reportReason += i;
            }
            break;
          }
          if (reportReason == 12) {
            if (i < 3) {
              reporTED = true;
              reportReason += i;
            }
            break;
          }
        }
        y += var5 + 2;
      }
    }

    if (!reporTED) {
      reportReason = 0;
    }

    int dialogX = gameWidth / 2 - 225; // 31
    int dialogY = gameHeight / 2 - 132; // 35
    int dialogW = 450;
    int dialogH = 275;

    if (mouseButtonClick != 0 && reportReason != 0) {
      clientStream.newPacket(206);
      clientStream.pjstr2(reportName);
      clientStream.putByte(reportReason);
      clientStream.putByte(reportMutePlayer ? 0 : 1);
      clientStream.sendPacket();
      inputTextCurrent = "";
      inputTextFinal = "";
      mouseButtonClick = 0;
      showDialogReportAbuseStep = 0;
      return;
    } else {
      y += 15;
      if (mouseButtonClick != 0) {
        mouseButtonClick = 0;
        if (super.mouseX < dialogX ||
            super.mouseY < dialogY ||
            super.mouseX > dialogX + dialogW ||
            super.mouseY > dialogY + dialogH) {
          showDialogReportAbuseStep = 0;
          return;
        }
        if (super.mouseX > gameWidth / 2 - 190 &&
            super.mouseX < gameWidth / 2 + 190 &&
            super.mouseY >= y - 15 &&
            super.mouseY < y + 5) {
          showDialogReportAbuseStep = 0;
          return;
        }
      }
    }

    int col1center = gameWidth / 2 - 150; // 106
    int col1left = col1center - 70; // 36
    int col2center = gameWidth / 2; // 256
    int col2left = col2center - 70; // 186
    int col3center = gameWidth / 2 + 150; // 406
    int col3left = col3center - 70; // 336

    surface.drawBox(dialogX, dialogY, dialogW, dialogH, 0);
    surface.drawBoxEdge(dialogX, dialogY, dialogW, dialogH, 0xffffff);
    y = dialogY + 15; // 50
    surface.drawStringCenter("This form is for reporting players who are breaking our rules",
                             col2center,
                             y,
                             1,
                             0xffffff
    );
    y += 15;
    surface.drawStringCenter("Using it sends a snapshot of the last 60 seconds of activity to us",
                             col2center,
                             y,
                             1,
                             0xffffff
    );
    y += 15;
    surface.drawStringCenter("If you misuse this form, you bill be banned.", col2center, y, 1, 0xff8000);
    y += 15;
    y += 10;
    surface.drawStringCenter("Click on the most suitable option from the Rules of RuneScape.",
                             col2center,
                             y,
                             1,
                             0xffff00
    );
    y += 15;
    surface.drawStringCenter("This will send a report to our Player Support team for investigation.",
                             col2center,
                             y,
                             1,
                             0xffff00
    );
    y += 18;
    surface.drawStringCenter("Honour", col1center, y, 4, 0xff0000);
    surface.drawStringCenter("Respect", col2center, y, 4, 0xff0000);
    surface.drawStringCenter("Security", col3center, y, 4, 0xff0000);
    y += 18;

    if (reportReason == 1) {
      surface.drawBox(col1left, y - 12, 140, 30, 0x303030);
    }
    surface.drawBoxEdge(col1left, y - 12, 140, 30, 0x404040);

    if (reportReason == 7) {
      surface.drawBox(col2left, y - 12, 140, 30, 0x303030);
    }
    surface.drawBoxEdge(col2left, y - 12, 140, 30, 0x404040);

    if (reportReason == 12) {
      surface.drawBox(col3left, y - 12, 140, 30, 0x303030);
    }
    surface.drawBoxEdge(col3left, y - 12, 140, 30, 0x404040);

    surface.drawStringCenter("Buying or", col1center, y, 0, reportReason == 1 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("Seriously offensive", col2center, y, 0, reportReason == 7 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("Asking for or providing", col3center, y, 0, reportReason == 12 ? 0xff8000 : 0xffffff);

    y += 12;
    surface.drawStringCenter("selling an account", col1center, y, 0, reportReason == 1 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("language", col2center, y, 0, reportReason == 7 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("contact information", col3center, y, 0, reportReason == 12 ? 0xff8000 : 0xffffff);

    y += 20;
    if (reportReason == 2) {
      surface.drawBox(col1left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col1left, y - 12, 140, 18, 0x404040);

    if (reportReason == 8) {
      surface.drawBox(col2left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col2left, y - 12, 140, 18, 0x404040);

    if (reportReason == 13) {
      surface.drawBox(col3left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col3left, y - 12, 140, 18, 0x404040);

    surface.drawStringCenter("Encouraging rule-breaking", col1center, y, 0, reportReason == 2 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("Solicitation", col2center, y, 0, reportReason == 8 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("Breaking real-world laws", col3center, y, 0, reportReason == 13 ? 0xff8000 : 0xffffff);

    y += 20;
    if (reportReason == 3) {
      surface.drawBox(col1left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col1left, y - 12, 140, 18, 0x404040);

    if (reportReason == 9) {
      surface.drawBox(col2left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col2left, y - 12, 140, 18, 0x404040);

    if (reportReason == 14) {
      surface.drawBox(col3left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col3left, y - 12, 140, 18, 0x404040);

    surface.drawStringCenter("Staff impersonation", col1center, y, 0, reportReason == 3 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("Disruptive behaviour", col2center, y, 0, reportReason == 9 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("Advertising websites", col3center, y, 0, reportReason == 14 ? 0xff8000 : 0xffffff);

    y += 20;
    if (reportReason == 4) {
      surface.drawBox(col1left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col1left, y - 12, 140, 18, 0x404040);

    if (reportReason == 10) {
      surface.drawBox(col2left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col2left, y - 12, 140, 18, 0x404040);

    surface.drawStringCenter("Macroing or use of bots", col1center, y, 0, reportReason == 4 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("Offensive account name", col2center, y, 0, reportReason == 10 ? 0xff8000 : 0xffffff);

    y += 20;
    if (reportReason == 5) {
      surface.drawBox(col1left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col1left, y - 12, 140, 18, 0x404040);

    if (reportReason == 11) {
      surface.drawBox(col2left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col2left, y - 12, 140, 18, 0x404040);

    surface.drawStringCenter("Scamming", col1center, y, 0, reportReason == 5 ? 0xff8000 : 0xffffff);
    surface.drawStringCenter("Real-life threats", col2center, y, 0, reportReason == 11 ? 0xff8000 : 0xffffff);

    y += 20;

    if (reportReason == 6) {
      surface.drawBox(col1left, y - 12, 140, 18, 0x303030);
    }
    surface.drawBoxEdge(col1left, y - 12, 140, 18, 0x404040);

    surface.drawStringCenter("Exploiting a bug", col1center, y, 0, reportReason == 6 ? 0xff8000 : 0xffffff);

    y += 18;
    y += 15;
    int color = 0xffffff;
    if (super.mouseX > col2center - 60 &&
        super.mouseX < col2center + 60 &&
        super.mouseY > y - 15 &&
        super.mouseY < y + 5) {
      color = 0xffff00;
    }
    surface.drawStringCenter("Click here to cancel", col2center, y, 1, color);
  }

  private void drawDialogReportAbuse() {
    int dialogX = gameWidth / 2 - 400 / 2;
    int dialogY = gameHeight / 2 - 290 / 2;
    reportAbuseOffence = 0;
    int y = gameHeight / 2 - 32;
    for (int i = 0; i < 12; i++) {
      if (super.mouseX > dialogX + 66 &&
          super.mouseX < dialogX + 446 &&
          super.mouseY >= dialogY + y - 12 &&
          super.mouseY < dialogY + y + 3) {
        reportAbuseOffence = i + 1;
      }
      y += 14;
    }

    if (mouseButtonClick != 0 && reportAbuseOffence != 0) {
      mouseButtonClick = 0;
      showDialogReportAbuseStep = 2;
      super.inputTextCurrent = "";
      super.inputTextFinal = "";
      return;
    }
    y += 15;
    if (mouseButtonClick != 0) {
      mouseButtonClick = 0;
      if (super.mouseX < dialogX + 56 ||
          super.mouseY < dialogX + 35 ||
          super.mouseX > dialogY + 456 ||
          super.mouseY > dialogY + 325) {
        showDialogReportAbuseStep = 0;
        return;
      }
      if (super.mouseX > dialogX + 66 &&
          super.mouseX < dialogX + 446 &&
          super.mouseY >= dialogY + y - 15 &&
          super.mouseY < dialogY + y + 5) {
        showDialogReportAbuseStep = 0;
        return;
      }
    }
    surface.drawBox(dialogX + 56, dialogY + 35, 400, 290, 0);
    surface.drawBoxEdge(dialogX + 56, dialogY + 35, 400, 290, 0xffffff);
    y = 50;
    surface.drawStringCenter("This form is for reporting players who are breaking our rules",
                             dialogX + 256,
                             dialogY + y,
                             1,
                             0xffffff
    );
    y += 15;
    surface.drawStringCenter("Using it sends a snapshot of the last 60 secs of activity to us",
                             dialogX + 256,
                             dialogY + y,
                             1,
                             0xffffff
    );
    y += 15;
    surface.drawStringCenter("If you misuse this form, you will be banned.", dialogX + 256, dialogY + y, 1, 0xff8000);
    y += 15;
    y += 10;
    surface.drawStringCenter("First indicate which of our 12 rules is being broken. For a detailed",
                             dialogX + 256,
                             dialogY + y,
                             1,
                             0xffff00
    );
    y += 15;
    surface.drawStringCenter("explanation of each rule please read the manual on our website.",
                             dialogX + 256,
                             dialogY + y,
                             1,
                             0xffff00
    );
    y += 15;
    int textColour;
    if (reportAbuseOffence == 1) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("1: Offensive language", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 2) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("2: Item scamming", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 3) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("3: Password scamming", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 4) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("4: Bug abuse", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 5) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("5: Jagex Staff impersonation", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 6) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("6: Account sharing/trading", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 7) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("7: Macroing", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 8) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("8: Mutiple logging in", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 9) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("9: Encouraging others to break rules", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 10) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("10: Misuse of customer support", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 11) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("11: Advertising / website", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    if (reportAbuseOffence == 12) {
      surface.drawBoxEdge(dialogX + 66, dialogY + y - 12, 380, 15, 0xffffff);
      textColour = 0xff8000;
    } else {
      textColour = 0xffffff;
    }
    surface.drawStringCenter("12: Real world item trading", dialogX + 256, dialogY + y, 1, textColour);
    y += 14;
    y += 15;
    textColour = 0xffffff;
    if (super.mouseX > dialogX + 196 &&
        super.mouseX < dialogX + 316 &&
        super.mouseY > dialogY + y - 15 &&
        super.mouseY < dialogY + y + 5) {
      textColour = 0xffff00;
    }
    surface.drawStringCenter("Click here to cancel", dialogX + 256, dialogY + y, 1, textColour);
  }

  private boolean walkToActionSource(int startX, int startY, int x1, int y1, int x2, int y2, boolean checkObjects, boolean walkToAction) {
    int steps = world.route(startX, startY, x1, y1, x2, y2, walkPathX, walkPathY, checkObjects);
    if (steps == -1) {
      if (walkToAction) {
        steps = 1;
        walkPathX[0] = x1;
        walkPathY[0] = y1;
      } else {
        return false;
      }
    }
    steps--;
    startX = walkPathX[steps];
    startY = walkPathY[steps];
    steps--;
    if (walkToAction) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_WALK_ACTION));
    } else {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_WALK));
    }
    super.clientStream.putShort(startX + regionX);
    super.clientStream.putShort(startY + regionY);
    if (walkToAction && steps == -1 && (startX + regionX) % 5 == 0) {
      steps = 0;
    }
    for (int l1 = steps; l1 >= 0 && l1 > steps - 25; l1--) {
      super.clientStream.putByte(walkPathX[l1] - startX);
      super.clientStream.putByte(walkPathY[l1] - startY);
    }

    super.clientStream.sendPacket();
    mouseClickXStep = -24;
    mouseClickXX = super.mouseX;
    mouseClickXY = super.mouseY;
    return true;
  }

  private boolean walkTo(
    int startX, int startY, int x1, int y1, int x2, int y2, boolean checkObjects, boolean walkToAction
  ) {
    int steps = world.route(startX, startY, x1, y1, x2, y2, walkPathX, walkPathY, checkObjects);
    if (steps == -1) {
      return false;
    }
    steps--;
    startX = walkPathX[steps];
    startY = walkPathY[steps];
    steps--;
    if (walkToAction) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_WALK_ACTION));
    } else {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_WALK));
    }
    super.clientStream.putShort(startX + regionX);
    super.clientStream.putShort(startY + regionY);
    if (walkToAction && steps == -1 && (startX + regionX) % 5 == 0) {
      steps = 0;
    }
    for (int l1 = steps; l1 >= 0 && l1 > steps - 25; l1--) {
      super.clientStream.putByte(walkPathX[l1] - startX);
      super.clientStream.putByte(walkPathY[l1] - startY);
    }

    super.clientStream.sendPacket();
    mouseClickXStep = -24;
    mouseClickXX = super.mouseX;
    mouseClickXY = super.mouseY;
    return true;
  }

  private void drawMinimapEntity(int x, int y, int c) {
    surface.setPixel(x, y, c);
    surface.setPixel(x - 1, y, c);
    surface.setPixel(x + 1, y, c);
    surface.setPixel(x, y - 1, c);
    surface.setPixel(x, y + 1, c);
  }

  private void updateBankItems() {
    bankItemCount = newBankItemCount;
    for (int i = 0; i < newBankItemCount; i++) {
      bankItems[i] = newBankItems[i];
      bankItemsCount[i] = newBankItemsCount[i];
    }

    for (int invidx = 0; invidx < inventoryItemsCount; invidx++) {
      if (bankItemCount >= bankItemsMax) {
        break;
      }
      int invid = inventoryItemId[invidx];
      boolean hasitemininv = false;
      for (int bankidx = 0; bankidx < bankItemCount; bankidx++) {
        if (bankItems[bankidx] != invid) {
          continue;
        }
        hasitemininv = true;
        break;
      }

      if (!hasitemininv) {
        bankItems[bankItemCount] = invid;
        bankItemsCount[bankItemCount] = 0;
        bankItemCount++;
      }
    }

  }

  private void drawDialogWildWarn() {
    int left = gameWidth / 2 - 170; // 86
    int top = gameHeight / 2 - 90; // 77
    int y = top + 20;
    surface.drawBox(left, top, 340, 180, 0);
    surface.drawBoxEdge(left, top, 340, 180, 0xffffff);
    surface.drawStringCenter("Warning! Proceed with caution", gameWidth / 2, y, 4, 0xff0000);
    y += 26;
    surface.drawStringCenter("If you go much further north you will enter the", gameWidth / 2, y, 1, 0xffffff);
    y += 13;
    surface.drawStringCenter("wilderness. This a very dangerous area where", gameWidth / 2, y, 1, 0xffffff);
    y += 13;
    surface.drawStringCenter("other players can attack you!", gameWidth / 2, y, 1, 0xffffff);
    y += 22;
    surface.drawStringCenter("The further north you go the more dangerous it", gameWidth / 2, y, 1, 0xffffff);
    y += 13;
    surface.drawStringCenter("becomes, but the more treasure you will find.", gameWidth / 2, y, 1, 0xffffff);
    y += 22;
    surface.drawStringCenter("In the wilderness an indicator at the bottom-right", gameWidth / 2, y, 1, 0xffffff);
    y += 13;
    surface.drawStringCenter("of the screen will show the current level of danger", gameWidth / 2, y, 1, 0xffffff);
    y += 22;
    int j = 0xffffff;
    if (super.mouseY > y - 12 && super.mouseY <= y && super.mouseX > left + 95 && super.mouseX < left + 245) {
      j = 0xff0000;
    }
    surface.drawStringCenter("Click here to close window", gameWidth / 2, y, 1, j);
    if (mouseButtonClick != 0) {
      if (super.mouseY > y - 12 && super.mouseY <= y && super.mouseX > left + 95 && super.mouseX < left + 245) {
        showUiWildWarn = 2;
      }
      if (super.mouseX < left || super.mouseX > left + 340 || super.mouseY < top || super.mouseY > top + 180) {
        showUiWildWarn = 2;
      }
      mouseButtonClick = 0;
    }
  }

  private void walkToActionSource(int sx, int sy, int dx, int dy, boolean action) {
    walkToActionSource(sx, sy, dx, dy, dx, dy, false, action);
  }

  private void drawUi() {
    boolean doShitGetHit = false;
    if (logoutTimeout != 0) {
      drawDialogLogout();
    } else if (showDialogWelcome) {
      showDialogWelcome = false;
      Runnable fucken = () -> {
        try {
          Thread.sleep(2000);
        } catch (Exception ex) {
        }
        zoomControls = true;
        debugHud = DEBUG_HUD_NAMES;
        messageTabSelected = 2;
      };
      new Thread(fucken).start();
    }
        /*drawDialogWelcome();
        else if (showDialogServermessage)
            drawDialogServermessage();
        else if (showUiWildWarn == 1)
            drawDialogWildWarn();*/
    else if (showDialogBank && combatTimeout == 0) {
      drawDialogBank();
    } else if (showDialogShop && combatTimeout == 0) {
      drawDialogShop();
    } else if (showDialogTradeConfirm) {
      drawDialogTradeConfirm();
    } else if (showDialogTrade) {
      drawDialogTrade();
    } else if (showDialogDuelConfirm) {
      drawDialogDuelConfirm();
    } else if (showDialogDuel) {
      drawDialogDuel();
    } else if (showDialogReportAbuseStep == 1 && Version.CLIENT > 204) {
      drawDialogReportAbuseInputNew();
    } else if (showDialogReportAbuseStep == 1 && Version.CLIENT <= 204) {
      drawDialogReportAbuse();
    } else if (showDialogReportAbuseStep == 2 && Version.CLIENT > 204) {
      drawDialogReportAbuseNew();
    } else if (showDialogReportAbuseStep == 2 && Version.CLIENT <= 204) {
      drawDialogReportAbuseInput();
    } else if (showDialogSocialInput != 0) {
      drawDialogSocialInput();
    } else {
      doShitGetHit = true;
    }

    if (inputPopupType != 0) {
      drawInputPopup();
    }

    if (doShitGetHit) {
      if (showOptionMenu) {
        drawOptionMenu();
      }
      if (localPlayer.animationCurrent == 8 || localPlayer.animationCurrent == 9) {
        drawDialogCombatStyle();
      }
      setActiveUiTab();
      boolean nomenus = !showOptionMenu && !showRightClickMenu;
      if (nomenus) {
        menuItemsCount = 0;
      }
      if (showUiTab == 0 && nomenus) {
        createRightClickMenu();
      }
      if (showUiTab == 1) {
        drawUiTabInventory(nomenus);
      }
      if (showUiTab == 2) {
        drawUiTabMinimap(nomenus);
      }
      if (showUiTab == 3) {
        drawUiTabPlayerInfo(nomenus);
      }
      if (showUiTab == 4) {
        drawUiTabMagic(nomenus);
      }
      if (showUiTab == 5) {
        drawUiTabSocial(nomenus);
      }
      if (showUiTab == 6) {
        drawUiTabOptions(nomenus);
      }
      if (!showRightClickMenu && !showOptionMenu) {
        createTopMouseMenu();
      }
      if (showRightClickMenu && !showOptionMenu) {
        drawRightClickMenu();
      }
    }
    mouseButtonClick = 0;
  }

  private void drawDialogTrade() {
    int dialogX = gameWidth / 2 - 234; // 22
    int dialogY = gameHeight / 2 - 131; // 36
    if (mouseButtonClick != 0 && mouseButtonItemCountIncrement == 0) {
      mouseButtonItemCountIncrement = 1;
    }
    if (mouseButtonItemCountIncrement > 0) {
      int mouseX = super.mouseX - dialogX;
      int mouseY = super.mouseY - dialogY;
      if (mouseX >= 0 && mouseY >= 0 && mouseX < 468 && mouseY < 262) {
        if (mouseX > 216 && mouseY > 30 && mouseX < 462 && mouseY < 235) {
          int slot = (mouseX - 217) / 49 + ((mouseY - 31) / 34) * 5;
          if (slot >= 0 && slot < inventoryItemsCount) {
            boolean sendUpdate = false;
            int itemCountAdd = 0;
            int itemType = inventoryItemId[slot];
            for (int itemIndex = 0; itemIndex < tradeItemsCount; itemIndex++) {
              if (tradeItems[itemIndex] == itemType) {
                if (GameData.itemStackable[itemType] == 0) {
                  for (int i4 = 0; i4 < mouseButtonItemCountIncrement; i4++) {
                    if (tradeItemCount[itemIndex] < inventoryItemStackCount[slot]) {
                      tradeItemCount[itemIndex]++;
                    }
                    sendUpdate = true;
                  }

                } else {
                  itemCountAdd++;
                }
              }
            }

            if (getInventoryCount(itemType) <= itemCountAdd) {
              sendUpdate = true;
            }
            if (GameData.itemSpecial[itemType] == 1) { // quest items? or just tagged as 'special'
              showMessage("This object cannot be traded with other players", 3);
              sendUpdate = true;
            }
            if (!sendUpdate && tradeItemsCount < 12) {
              tradeItems[tradeItemsCount] = itemType;
              tradeItemCount[tradeItemsCount] = 1;
              tradeItemsCount++;
              sendUpdate = true;
            }
            if (sendUpdate) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_ITEM_UPDATE));
              super.clientStream.putByte(tradeItemsCount);
              for (int j4 = 0; j4 < tradeItemsCount; j4++) {
                super.clientStream.putShort(tradeItems[j4]);
                super.clientStream.putInt(tradeItemCount[j4]);
              }

              super.clientStream.sendPacket();
              tradeRecipientAccepted = false;
              tradeAccepted = false;
            }
          }
        }
        if (mouseX > 8 && mouseY > 30 && mouseX < 205 && mouseY < 133) {
          int itemIndex = (mouseX - 9) / 49 + ((mouseY - 31) / 34) * 4;
          if (itemIndex >= 0 && itemIndex < tradeItemsCount) {
            int itemType = tradeItems[itemIndex];
            for (int i2 = 0; i2 < mouseButtonItemCountIncrement; i2++) {
              if (GameData.itemStackable[itemType] == 0 && tradeItemCount[itemIndex] > 1) {
                tradeItemCount[itemIndex]--;
                continue;
              }
              tradeItemsCount--;
              mouseButtonDownTime = 0;
              for (int l2 = itemIndex; l2 < tradeItemsCount; l2++) {
                tradeItems[l2] = tradeItems[l2 + 1];
                tradeItemCount[l2] = tradeItemCount[l2 + 1];
              }

              break;
            }

            super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_ITEM_UPDATE));
            super.clientStream.putByte(tradeItemsCount);
            for (int i3 = 0; i3 < tradeItemsCount; i3++) {
              super.clientStream.putShort(tradeItems[i3]);
              super.clientStream.putInt(tradeItemCount[i3]);
            }

            super.clientStream.sendPacket();
            tradeRecipientAccepted = false;
            tradeAccepted = false;
          }
        }
        if (mouseX >= 217 && mouseY >= 238 && mouseX <= 286 && mouseY <= 259) {
          tradeAccepted = true;
          super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_ACCEPT));
          super.clientStream.sendPacket();
        }
        if (mouseX >= 394 && mouseY >= 238 && mouseX < 463 && mouseY < 259) {
          showDialogTrade = false;
          super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_DECLINE));
          super.clientStream.sendPacket();
        }
      } else if (mouseButtonClick != 0) {
        showDialogTrade = false;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_DECLINE));
        super.clientStream.sendPacket();
      }
      mouseButtonClick = 0;
      mouseButtonItemCountIncrement = 0;
    }
    if (!showDialogTrade) {
      return;
    }
    surface.drawBox(dialogX, dialogY, 468, 12, 192);
    surface.drawBoxAlpha(dialogX, dialogY + 12, 468, 18, 0x989898, 160);
    surface.drawBoxAlpha(dialogX, dialogY + 30, 8, 248, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 205, dialogY + 30, 11, 248, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 462, dialogY + 30, 6, 248, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 133, 197, 22, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 258, 197, 20, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 216, dialogY + 235, 246, 43, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 30, 197, 103, 0xd0d0d0, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 155, 197, 103, 0xd0d0d0, 160);
    surface.drawBoxAlpha(dialogX + 216, dialogY + 30, 246, 205, 0xd0d0d0, 160);
    for (int j2 = 0; j2 < 4; j2++) {
      surface.drawLineHoriz(dialogX + 8, dialogY + 30 + j2 * 34, 197, 0);
    }

    for (int j3 = 0; j3 < 4; j3++) {
      surface.drawLineHoriz(dialogX + 8, dialogY + 155 + j3 * 34, 197, 0);
    }

    for (int l3 = 0; l3 < 7; l3++) {
      surface.drawLineHoriz(dialogX + 216, dialogY + 30 + l3 * 34, 246, 0);
    }

    for (int k4 = 0; k4 < 6; k4++) {
      if (k4 < 5) {
        surface.drawLineVert(dialogX + 8 + k4 * 49, dialogY + 30, 103, 0);
      }
      if (k4 < 5) {
        surface.drawLineVert(dialogX + 8 + k4 * 49, dialogY + 155, 103, 0);
      }
      surface.drawLineVert(dialogX + 216 + k4 * 49, dialogY + 30, 205, 0);
    }

    surface.drawstring("Trading with: " + tradeRecipientName, dialogX + 1, dialogY + 10, 1, 0xffffff);
    surface.drawstring("Your Offer", dialogX + 9, dialogY + 27, 4, 0xffffff);
    surface.drawstring("Opponent's Offer", dialogX + 9, dialogY + 152, 4, 0xffffff);
    surface.drawstring("Your Inventory", dialogX + 216, dialogY + 27, 4, 0xffffff);
    if (!tradeAccepted) {
      surface.drawSprite(dialogX + 217, dialogY + 238, spriteMedia + 25);
    }
    surface.drawSprite(dialogX + 394, dialogY + 238, spriteMedia + 26);
    if (tradeRecipientAccepted) {
      surface.drawStringCenter("Other player", dialogX + 341, dialogY + 246, 1, 0xffffff);
      surface.drawStringCenter("has accepted", dialogX + 341, dialogY + 256, 1, 0xffffff);
    }
    if (tradeAccepted) {
      surface.drawStringCenter("Waiting for", dialogX + 217 + 35, dialogY + 246, 1, 0xffffff);
      surface.drawStringCenter("other player", dialogX + 217 + 35, dialogY + 256, 1, 0xffffff);
    }
    for (int itemIndex = 0; itemIndex < inventoryItemsCount; itemIndex++) {
      int slotX = 217 + dialogX + (itemIndex % 5) * 49;
      int slotY = 31 + dialogY + (itemIndex / 5) * 34;
      surface.spriteClipping(slotX,
                             slotY,
                             48,
                             32,
                             spriteItem + GameData.itemPicture[inventoryItemId[itemIndex]],
                             GameData.itemMask[inventoryItemId[itemIndex]],
                             0,
                             0,
                             false
      );
      if (GameData.itemStackable[inventoryItemId[itemIndex]] == 0) {
        surface.drawstring(String.valueOf(inventoryItemStackCount[itemIndex]), slotX + 1, slotY + 10, 1, 0xffff00);
      }
    }

    for (int itemIndex = 0; itemIndex < tradeItemsCount; itemIndex++) {
      int slotX = 9 + dialogX + (itemIndex % 4) * 49;
      int slotY = 31 + dialogY + (itemIndex / 4) * 34;
      surface.spriteClipping(slotX,
                             slotY,
                             48,
                             32,
                             spriteItem + GameData.itemPicture[tradeItems[itemIndex]],
                             GameData.itemMask[tradeItems[itemIndex]],
                             0,
                             0,
                             false
      );
      if (GameData.itemStackable[tradeItems[itemIndex]] == 0) {
        surface.drawstring(String.valueOf(tradeItemCount[itemIndex]), slotX + 1, slotY + 10, 1, 0xffff00);
      }
      if (super.mouseX > slotX && super.mouseX < slotX + 48 && super.mouseY > slotY && super.mouseY < slotY + 32) {
        surface.drawstring(GameData.itemName[tradeItems[itemIndex]] +
                           ": @whi@" +
                           GameData.itemDescription[tradeItems[itemIndex]], dialogX + 8, dialogY + 273, 1, 0xffff00);
      }
    }

    for (int itemIndex = 0; itemIndex < tradeRecipientItemsCount; itemIndex++) {
      int slotX = 9 + dialogX + (itemIndex % 4) * 49;
      int slotY = 156 + dialogY + (itemIndex / 4) * 34;
      surface.spriteClipping(slotX,
                             slotY,
                             48,
                             32,
                             spriteItem + GameData.itemPicture[tradeRecipientItems[itemIndex]],
                             GameData.itemMask[tradeRecipientItems[itemIndex]],
                             0,
                             0,
                             false
      );
      if (GameData.itemStackable[tradeRecipientItems[itemIndex]] == 0) {
        surface.drawstring(String.valueOf(tradeRecipientItemCount[itemIndex]), slotX + 1, slotY + 10, 1, 0xffff00);
      }
      if (super.mouseX > slotX && super.mouseX < slotX + 48 && super.mouseY > slotY && super.mouseY < slotY + 32) {
        surface.drawstring(GameData.itemName[tradeRecipientItems[itemIndex]] +
                           ": @whi@" +
                           GameData.itemDescription[tradeRecipientItems[itemIndex]],
                           dialogX + 8,
                           dialogY + 273,
                           1,
                           0xffff00
        );
      }
    }

  }

  private void drawUiTabSocial(boolean nomenus) {
    int uiX = surface.width2 - 199;
    int uiY = 36;
    surface.drawSprite(uiX - 49, 3, spriteMedia + 5);
    int uiWidth = 196;//'\304';
    int uiHeight = 182;//'\266';
    int l;
    int k = l = Utility.rgb2long(160, 160, 160);
    if (uiTabSocialSubTab == 0) {
      k = Utility.rgb2long(220, 220, 220);
    } else {
      l = Utility.rgb2long(220, 220, 220);
    }
    surface.drawBoxAlpha(uiX, uiY, uiWidth / 2, 24, k, 128);
    surface.drawBoxAlpha(uiX + uiWidth / 2, uiY, uiWidth / 2, 24, l, 128);
    surface.drawBoxAlpha(uiX, uiY + 24, uiWidth, uiHeight - 24, Utility.rgb2long(220, 220, 220), 128);
    surface.drawLineHoriz(uiX, uiY + 24, uiWidth, 0);
    surface.drawLineVert(uiX + uiWidth / 2, uiY, 24, 0);
    surface.drawLineHoriz(uiX, (uiY + uiHeight) - 16, uiWidth, 0);
    surface.drawStringCenter("Friends", uiX + uiWidth / 4, uiY + 16, 4, 0);
    surface.drawStringCenter("Ignore", uiX + uiWidth / 4 + uiWidth / 2, uiY + 16, 4, 0);
    panelSocialList.clearList(controlListSocialPlayers);
    if (uiTabSocialSubTab == 0) {
      for (int index = 0; index < super.friendListCount; index++) {
        String s;
        if (Version.CLIENT > 204) {
          if ((super.friendListOnline[index] & 2) == 0) {
            if ((super.friendListOnline[index] & 4) == 0) {
              s = "@red@";
            } else {
              s = "@yel@";
            }
          } else {
            s = "@gre@";
          }
          String name = super.friendListNames[index];
          int j = 0;
          for (int i = friendListNames[index].length(); surface.textWidth(name, 1) >
                                                        120; name = friendListNames[index].substring(0, i - j) +
                                                                    "...") {
            j++;
          }
          panelSocialList.addListEntry(controlListSocialPlayers,
                                       index,
                                       s + name + "~" + (gameWidth - 73) + "~@whi@Remove         WWWWWWWWWW"
          );
        } else {
          if (super.friendListOnline[index] == 255) {
            s = "@gre@";
          } else if (super.friendListOnline[index] > 0) {
            s = "@yel@";
          } else {
            s = "@red@";
          }
          panelSocialList.addListEntry(controlListSocialPlayers,
                                       index,
                                       s +
                                       Utility.hash2username(super.friendListHashes[index]) +
                                       "~" +
                                       (gameWidth - 73) +
                                       "~@whi@Remove         WWWWWWWWWW"
          );
        }
      }

    }
    if (uiTabSocialSubTab == 1) {
      for (int index = 0; index < super.ignoreListCount; index++) {
        if (Version.CLIENT > 204) {
          String name = super.ignoreListNames[index];
          int j = 0;
          for (int i = ignoreListNames[index].length(); surface.textWidth(name, 1) >
                                                        120; name = ignoreListNames[index].substring(0, i - j) +
                                                                    "...") {
            j++;
          }
          panelSocialList.addListEntry(controlListSocialPlayers,
                                       index,
                                       "@yel@" + name + "~" + (gameWidth - 73) + "~@whi@Remove         WWWWWWWWWW"
          );
        } else {
          panelSocialList.addListEntry(controlListSocialPlayers,
                                       index,
                                       "@yel@" +
                                       Utility.hash2username(super.ignoreListHashes[index]) +
                                       "~" +
                                       (gameWidth - 73) +
                                       "~@whi@Remove         WWWWWWWWWW"
          );
        }
      }
    }
    panelSocialList.drawPanel();
    if (uiTabSocialSubTab == 0) {
      int index = panelSocialList.getListEntryIndex(controlListSocialPlayers);
      String name = index >= 0 ? Version.CLIENT >
                                 204 ? super.friendListNames[index] : Utility.hash2username(super.friendListHashes[index]) : null;
      if (index >= 0 && super.mouseX < gameWidth - 23) {
        if (super.mouseX > gameWidth - 83) {
          surface.drawStringCenter("Click to remove " + name, uiX + uiWidth / 2, uiY + 35, 1, 0xffffff);
        } else if (super.friendListOnline[index] == 255) {
          surface.drawStringCenter("Click to message " + name, uiX + uiWidth / 2, uiY + 35, 1, 0xffffff);
        } else if (super.friendListOnline[index] > 0) {
          if (super.friendListOnline[index] < 200) {
            surface.drawStringCenter(name + " is on world " + (super.friendListOnline[index] - 9),
                                     uiX + uiWidth / 2,
                                     uiY + 35,
                                     1,
                                     0xffffff
            );
          } else {
            surface.drawStringCenter(name + " is on classic " + (super.friendListOnline[index] - 219),
                                     uiX + uiWidth / 2,
                                     uiY + 35,
                                     1,
                                     0xffffff
            );
          }
        } else {
          surface.drawStringCenter(name + " is offline", uiX + uiWidth / 2, uiY + 35, 1, 0xffffff);
        }
      } else {
        surface.drawStringCenter("Click a name to send a message", uiX + uiWidth / 2, uiY + 35, 1, 0xffffff);
      }
      int colour;
      if (super.mouseX > uiX &&
          super.mouseX < uiX + uiWidth &&
          super.mouseY > (uiY + uiHeight) - 16 &&
          super.mouseY < uiY + uiHeight) {
        colour = 0xffff00;
      } else {
        colour = 0xffffff;
      }
      surface.drawStringCenter("Click here to add a friend", uiX + uiWidth / 2, (uiY + uiHeight) - 3, 1, colour);
    }
    if (uiTabSocialSubTab == 1) {
      int index = panelSocialList.getListEntryIndex(controlListSocialPlayers);
      String name = index >= 0 ? Version.CLIENT >
                                 204 ? super.friendListNames[index] : Utility.hash2username(super.friendListHashes[index]) : null;
      if (index >= 0 && super.mouseX < gameWidth - 23 && super.mouseX > gameWidth - 83) {
        if (super.mouseX > gameWidth - 83) {
          surface.drawStringCenter("Click to remove " + name, uiX + uiWidth / 2, uiY + 35, 1, 0xffffff);
        }
      } else {
        surface.drawStringCenter("Blocking messages from:", uiX + uiWidth / 2, uiY + 35, 1, 0xffffff);
      }
      int color;
      if (super.mouseX > uiX &&
          super.mouseX < uiX + uiWidth &&
          super.mouseY > (uiY + uiHeight) - 16 &&
          super.mouseY < uiY + uiHeight) {
        color = 0xffff00;
      } else {
        color = 0xffffff;
      }
      surface.drawStringCenter("Click here to add a name", uiX + uiWidth / 2, (uiY + uiHeight) - 3, 1, color);
    }
    if (!nomenus) {
      return;
    }
    uiX = super.mouseX - (surface.width2 - 199);
    uiY = super.mouseY - 36;
    if (uiX >= 0 && uiY >= 0 && uiX < 196 && uiY < 182) {
      panelSocialList.handleMouse(uiX + (surface.width2 - 199),
                                  uiY + 36,
                                  super.lastMouseButtonDown,
                                  super.mouseButtonDown
      );
      if (uiY <= 24 && mouseButtonClick == 1) {
        if (uiX < 98 && uiTabSocialSubTab == 1) {
          uiTabSocialSubTab = 0;
          panelSocialList.resetListProps(controlListSocialPlayers);
        } else if (uiX > 98 && uiTabSocialSubTab == 0) {
          uiTabSocialSubTab = 1;
          panelSocialList.resetListProps(controlListSocialPlayers);
        }
      }
      if (mouseButtonClick == 1 && uiTabSocialSubTab == 0) {
        int index = panelSocialList.getListEntryIndex(controlListSocialPlayers);
        if (index >= 0 && super.mouseX < gameWidth - 23) {
          if (super.mouseX > gameWidth - 83) {
            if (Version.CLIENT > 204) {
              friendRemove(super.friendListNames[index]);
            } else {
              friendRemove(super.friendListHashes[index]);
            }
          } else if (super.friendListOnline[index] != 0) {
            showDialogSocialInput = 2;
            privateMessageTarget = Version.CLIENT >
                                   204 ? super.friendListNames[index] : Utility.hash2username(super.friendListHashes[index]);
            super.inputPmCurrent = "";
            super.inputPmFinal = "";
          }
        }
      }
      if (mouseButtonClick == 1 && uiTabSocialSubTab == 1) {
        int index = panelSocialList.getListEntryIndex(controlListSocialPlayers);
        if (index >= 0 && super.mouseX < gameWidth - 23 && super.mouseX > gameWidth - 83) {
          if (Version.CLIENT > 204) {
            ignoreRemove(super.ignoreListNames[index]);
          } else {
            ignoreRemove(super.ignoreListHashes[index]);
          }
        }
      }
      if (uiY > 166 && mouseButtonClick == 1 && uiTabSocialSubTab == 0) {
        showDialogSocialInput = 1;
        super.inputTextCurrent = "";
        super.inputTextFinal = "";
      }
      if (uiY > 166 && mouseButtonClick == 1 && uiTabSocialSubTab == 1) {
        showDialogSocialInput = 3;
        super.inputTextCurrent = "";
        super.inputTextFinal = "";
      }
      mouseButtonClick = 0;
    }
  }

  private void sendLogout() {
    if (loggedIn == 0) {
      return;
    }
    if (combatTimeout > 450) {
      if (Version.CLIENT > 204) {
        showMessage("You can't logout during combat!", 0, "@cya@");
      } else {
        showMessage("@cya@You can't logout during combat!", 3);
      }
      return;
    }
    if (combatTimeout > 0) {
      if (Version.CLIENT > 204) {
        showMessage("You can't logout for 10 seconds after combat", 0, "@cya@");
      } else {
        showMessage("@cya@You can't logout for 10 seconds after combat", 3);
      }
      return;
    } else {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_LOGOUT));
      super.clientStream.sendPacket();
      logoutTimeout = 1000;
      return;
    }
  }

  private GameCharacter createPlayer(int serverIndex, int x, int y, int anim) {
    if (playerServer[serverIndex] == null) {
      playerServer[serverIndex] = new GameCharacter();
      playerServer[serverIndex].serverIndex = serverIndex;
      if (Version.CLIENT <= 204) {
        playerServer[serverIndex].serverId = 0;
      }
    }
    GameCharacter character = playerServer[serverIndex];
    boolean flag = false;
    for (int i1 = 0; i1 < knownPlayerCount; i1++) {
      if (knownPlayers[i1].serverIndex != serverIndex) {
        continue;
      }
      flag = true;
      break;
    }

    if (flag) {
      character.animationNext = anim;
      int j1 = character.waypointCurrent;
      if (x != character.waypointsX[j1] || y != character.waypointsY[j1]) {
        character.waypointCurrent = j1 = (j1 + 1) % 10;
        character.waypointsX[j1] = x;
        character.waypointsY[j1] = y;
      }
    } else {
      character.serverIndex = serverIndex;
      character.movingStep = 0;
      character.waypointCurrent = 0;
      character.waypointsX[0] = character.currentX = x;
      character.waypointsY[0] = character.currentY = y;
      character.animationNext = character.animationCurrent = anim;
      character.stepCount = 0;
    }
    players[playerCount++] = character;
    return character;
  }

  private void drawDialogSocialInput() {
    int left = gameWidth / 2 - 150; // 106
    int top = gameHeight / 2 - 22; // 145
    int right = gameWidth / 2 + 150; // 406
    int bottom = gameHeight / 2 + 48; // 215
    if (mouseButtonClick != 0) {
      mouseButtonClick = 0;
      if (showDialogSocialInput == 1 &&
          (super.mouseX < left || super.mouseY < top || super.mouseX > right || super.mouseY > bottom)) {
        showDialogSocialInput = 0;
        return;
      }
      if (showDialogSocialInput == 2 &&
          (super.mouseX < 6 || super.mouseY < top || super.mouseX > right + 100 || super.mouseY > bottom)) {
        showDialogSocialInput = 0;
        return;
      }
      if (showDialogSocialInput == 3 &&
          (super.mouseX < left || super.mouseY < top || super.mouseX > right || super.mouseY > bottom)) {
        showDialogSocialInput = 0;
        return;
      }
      if (super.mouseX > gameWidth / 2 - 20 &&
          super.mouseX < gameWidth / 2 + 20 &&
          super.mouseY > gameHeight / 2 + 26 &&
          super.mouseY < gameHeight / 2 + 46) {
        showDialogSocialInput = 0;
        return;
      }
    }
    int y = top;
    if (showDialogSocialInput == 1) {
      surface.drawBox(left, y, 300, 70, 0);
      surface.drawBoxEdge(left, y, 300, 70, 0xffffff);
      y += 20;
      surface.drawStringCenter("Enter name to add to friends list", gameWidth / 2, y, 4, 0xffffff);
      y += 20;
      surface.drawStringCenter(super.inputTextCurrent + "*", gameWidth / 2, y, 4, 0xffffff);
      if (super.inputTextFinal.length() > 0) {
        String s = super.inputTextFinal.trim();
        super.inputTextCurrent = "";
        super.inputTextFinal = "";
        showDialogSocialInput = 0;
        if (Version.CLIENT > 204) {
          if (s.length() > 0 && !Utility.formatName(s).equals(Utility.formatName(localPlayer.accountName))) {
            friendAdd(s);
          }
        } else {
          if (s.length() > 0 && Utility.username2hash(s) != localPlayer.hash) {
            friendAdd(Utility.username2hash(s));
          }
        }
      }
    }
    if (showDialogSocialInput == 2) {
      surface.drawBox(left - 100, y, 500, 70, 0);
      surface.drawBoxEdge(left - 100, y, 500, 70, 0xffffff);
      y += 20;
      surface.drawStringCenter("Enter message to send to " + privateMessageTarget, gameWidth / 2, y, 4, 0xffffff);
      y += 20;
      surface.drawStringCenter(super.inputPmCurrent + "*", gameWidth / 2, y, 4, 0xffffff);
      if (super.inputPmFinal.length() > 0) {
        String s1 = super.inputPmFinal;
        super.inputPmCurrent = "";
        super.inputPmFinal = "";
        showDialogSocialInput = 0;
        if (Version.CLIENT > 204) {
          sendPrivateMessage(privateMessageTarget, s1);
        } else {
          byte[] msg = Utility.stringToByteArray(s1);
          sendPrivateMessage(Utility.username2hash(privateMessageTarget), msg, msg.length);
          s1 = WordFilter.filter(s1);
          showServerMessage("@pri@You tell " + privateMessageTarget + ": " + s1);
        }
      }
    }
    if (showDialogSocialInput == 3) {
      surface.drawBox(left, y, 300, 70, 0);
      surface.drawBoxEdge(left, y, 300, 70, 0xffffff);
      y += 20;
      surface.drawStringCenter("Enter name to add to ignore list", gameWidth / 2, y, 4, 0xffffff);
      y += 20;
      surface.drawStringCenter(super.inputTextCurrent + "*", gameWidth / 2, y, 4, 0xffffff);
      if (super.inputTextFinal.length() > 0) {
        String s2 = super.inputTextFinal.trim();
        super.inputTextCurrent = "";
        super.inputTextFinal = "";
        showDialogSocialInput = 0;
        if (Version.CLIENT > 204) {
          if (s2.length() > 0 && !Utility.formatName(s2).equals(Utility.formatName(localPlayer.accountName))) {
            ignoreAdd(s2);
          }
        } else {
          if (s2.length() > 0 && Utility.username2hash(s2) != localPlayer.hash) {
            ignoreAdd(Utility.username2hash(s2));
          }
        }
      }
    }
    int j = 0xffffff;
    if (super.mouseX > gameWidth / 2 - 20 &&
        super.mouseX < gameWidth / 2 + 20 &&
        super.mouseY > gameHeight / 2 + 26 &&
        super.mouseY < gameHeight / 2 + 46) {
      j = 0xffff00;
    }
    surface.drawStringCenter("Cancel", gameWidth / 2, gameHeight / 2 + 41, 1, j);
  }

  protected Socket createSocket(String addr, int port) throws IOException {
    Socket socket1;
    if (getStartedAsApplet()) {
      socket1 = new Socket(InetAddress.getByName(getCodeBase().getHost()), port);
    } else {
      socket1 = new Socket(InetAddress.getByName(addr), port);
    }
    socket1.setSoTimeout(30000);
    socket1.setTcpNoDelay(true);
    return socket1;
  }

  public URL getCodeBase() {
    return super.getCodeBase();
  }

  private void drawDialogWelcome() {
    int left = gameWidth / 2 - 200; // 56
    int top = gameHeight / 2; // 167
    int height = 65;
    if (welcomeRecoverySetDays != 201) {
      height += 60;
    }
    if (welcomeUnreadMessages > 0) {
      height += 60;
    }
    if (welcomeLastLoggedInIP != 0) {
      height += 45;
    }
    int y = top - height / 2;
    surface.drawBox(left, top - height / 2, 400, height, 0);
    surface.drawBoxEdge(left, top - height / 2, 400, height, 0xffffff);
    y += 20;
    surface.drawStringCenter("Welcome to RuneScape " + localPlayer.accountName, gameWidth / 2, y, 4, 0xffff00);
    y += 30;
    String s;
    if (welcomeLastLoggedInDays == 0) {
      s = "earlier today";
    } else if (welcomeLastLoggedInDays == 1) {
      s = "yesterday";
    } else {
      s = welcomeLastLoggedInDays + " days ago";
    }
    if (welcomeLastLoggedInIP != 0) {
      surface.drawStringCenter("You last logged in " + s, gameWidth / 2, y, 1, 0xffffff);
      y += 15;
      if (welcomeLastLoggedInHost == null) {
        welcomeLastLoggedInHost = getHostnameIP(welcomeLastLoggedInIP);
      }
      surface.drawStringCenter("from: " + welcomeLastLoggedInHost, gameWidth / 2, y, 1, 0xffffff);
      y += 15;
      y += 15;
    }
    if (welcomeUnreadMessages > 0) {
      int k = 0xffffff;
      surface.drawStringCenter("Jagex staff will NEVER email you. We use the", gameWidth / 2, y, 1, k);
      y += 15;
      surface.drawStringCenter("message-centre on this website instead.", gameWidth / 2, y, 1, k);
      y += 15;
      if (welcomeUnreadMessages == 1) {
        surface.drawStringCenter("You have @yel@0@whi@ unread messages in your message-centre",
                                 gameWidth / 2,
                                 y,
                                 1,
                                 0xffffff
        );
      } else {
        surface.drawStringCenter("You have @gre@" +
                                 (welcomeUnreadMessages - 1) +
                                 " unread messages @whi@in your message-centre", gameWidth / 2, y, 1, 0xffffff);
      }
      y += 15;
      y += 15;
    }
    if (welcomeRecoverySetDays != 201) // this is an odd way of storing recovery day settings
    {
      if (welcomeRecoverySetDays == 200) // and this
      {
        surface.drawStringCenter("You have not yet set any password recovery questions.",
                                 gameWidth / 2,
                                 y,
                                 1,
                                 0xff8000
        );
        y += 15;
        surface.drawStringCenter("We strongly recommend you do so now to secure your account.",
                                 gameWidth / 2,
                                 y,
                                 1,
                                 0xff8000
        );
        y += 15;
        surface.drawStringCenter("Do this from the 'account management' area on our front webpage",
                                 gameWidth / 2,
                                 y,
                                 1,
                                 0xff8000
        );
        y += 15;
      } else {
        String s1;
        if (welcomeRecoverySetDays == 0) {
          s1 = "Earlier today";
        } else if (welcomeRecoverySetDays == 1) {
          s1 = "Yesterday";
        } else {
          s1 = welcomeRecoverySetDays + " days ago";
        }
        surface.drawStringCenter(s1 + " you changed your recovery questions", gameWidth / 2, y, 1, 0xff8000);
        y += 15;
        surface.drawStringCenter("If you do not remember making this change then cancel it immediately",
                                 gameWidth / 2,
                                 y,
                                 1,
                                 0xff8000
        );
        y += 15;
        surface.drawStringCenter("Do this from the 'account management' area on our front webpage",
                                 gameWidth / 2,
                                 y,
                                 1,
                                 0xff8000
        );
        y += 15;
      }
      y += 15;
    }
    int l = 0xffffff;
    if (super.mouseY > y - 12 && super.mouseY <= y && super.mouseX > left + 50 && super.mouseX < left + 350) {
      l = 0xff0000;
    }
    surface.drawStringCenter("Click here to close window", gameWidth / 2, y, 1, l);
    if (mouseButtonClick == 1) {
      if (l == 0xff0000) {
        showDialogWelcome = false;
      }
      if ((super.mouseX < left + 30 || super.mouseX > left + 370) &&
          (super.mouseY < top - height / 2 || super.mouseY > top + height / 2)) {
        showDialogWelcome = false;
      }
    }
    mouseButtonClick = 0;
  }

  private String getHostnameIP(int i) // and this? re: vvvv
  {
    return Utility.ip2string(i);
  }

  private void drawAppearancePanelCharacterSprites() {
    surface.interlace = false;
    surface.blackScreen();
    panelAppearance.drawPanel();
    int x = gameWidth / 2 - 116; // 140
    int y = gameHeight / 2 - 117; // 50
    x += 116;
    y -= 25;
    surface.spriteClipping(x - 32 - 55,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearance2Colour],
                           characterTopBottomColours[appearanceBottomColour]
    );
    surface.spriteClipping(x - 32 - 55,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearanceBodyGender],
                           characterTopBottomColours[appearanceTopColour],
                           characterSkinColours[appearanceSkinColour],
                           0,
                           false
    );
    surface.spriteClipping(x - 32 - 55,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearanceHeadType],
                           characterHairColours[appearanceHairColour],
                           characterSkinColours[appearanceSkinColour],
                           0,
                           false
    );
    surface.spriteClipping(x - 32,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearance2Colour] + 6,
                           characterTopBottomColours[appearanceBottomColour]
    );
    surface.spriteClipping(x - 32,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearanceBodyGender] + 6,
                           characterTopBottomColours[appearanceTopColour],
                           characterSkinColours[appearanceSkinColour],
                           0,
                           false
    );
    surface.spriteClipping(x - 32,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearanceHeadType] + 6,
                           characterHairColours[appearanceHairColour],
                           characterSkinColours[appearanceSkinColour],
                           0,
                           false
    );
    surface.spriteClipping((x - 32) + 55,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearance2Colour] + 12,
                           characterTopBottomColours[appearanceBottomColour]
    );
    surface.spriteClipping((x - 32) + 55,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearanceBodyGender] + 12,
                           characterTopBottomColours[appearanceTopColour],
                           characterSkinColours[appearanceSkinColour],
                           0,
                           false
    );
    surface.spriteClipping((x - 32) + 55,
                           y,
                           64,
                           102,
                           GameData.animationNumber[appearanceHeadType] + 12,
                           characterHairColours[appearanceHairColour],
                           characterSkinColours[appearanceSkinColour],
                           0,
                           false
    );
    surface.drawSprite(0, gameHeight, spriteMedia + 22);
    surface.draw(graphics, 0, 0);
  }

  void drawItem(int x, int y, int w, int h, int id, int tx, int ty) {
    // TODO distance, alphaize
    int picture = GameData.itemPicture[id] + spriteItem;
    int mask = GameData.itemMask[id];
    //System.out.println(String.format("x:%d y:%d w:%d h:%d id:%d tx:%d ty:%d", x, y, w, h ,id, tx ,ty));
    surface.spriteClipping(x, y, w, h, picture, mask, 0, 0, false);
    if (debugHud != DEBUG_HUD_NONE) {
      int dist = 0;
      int idx = 0;
      for (int i = 0; i < groundItemCount; i++) {
        if (groundItemId[i] == id) { // TODO this wont even work properly with multiples of the same item on the gorund
          dist = distance(localRegionX, localRegionY, groundItemX[i], groundItemY[i]);
          idx = i;
          break;
        }
      }
      if ((debugHud == DEBUG_HUD_NAMES || debugHud == DEBUG_HUD_ALL) && id >= 0 && id < GameData.itemName.length) {
        addReceivedMessage(x,
                           y,
                           w,
                           alphaize(0x800000, dist) +
                           GameData.itemName[id] +
                           " " +
                           alphaize(0x919191, dist) +
                           "(" +
                           groundItemId[idx] +
                           ")"
        );
      }
      if (debugHud == DEBUG_HUD_HITBOXES || debugHud == DEBUG_HUD_ALL) {
        surface.drawBoxAlpha(x, y, w, h, 0xff0000, 60);
      }
    }
  }

  public int distance(int x1, int y1, int x2, int y2) {
    x1 = Math.abs(x1);
    y1 = Math.abs(y1);
    x2 = Math.abs(x2);
    y2 = Math.abs(y2);
    return Math.max(x1, x2) - Math.min(x1, x2) + Math.max(y1, y2) - Math.min(y1, y2);
  }

  public void addReceivedMessage(int x, int y, int w, String s) {
    receivedMessageMidPoint[receivedMessagesCount] = surface.textWidth(s, 1) / 2;
    if (receivedMessageMidPoint[receivedMessagesCount] > 150) {
      receivedMessageMidPoint[receivedMessagesCount] = 150;
    }
    receivedMessageHeight[receivedMessagesCount] = (surface.textWidth(s, 1) / 300) * surface.textHeight(1);
    receivedMessageX[receivedMessagesCount] = x + w / 2;
    receivedMessageY[receivedMessagesCount] = y;
    receivedMessages[receivedMessagesCount++] = s;
  }

  public String alphaize(int colour, int distance) {
    return alphaize(colour, distance, true);
  }

  public String alphaize(int colour, int distance, boolean doodaa) {
    int a = 255 - Math.min(distance * alphaizeDistMult, 255);
    int r = (colour >> 16) & 0xff;
    int g = (colour >> 8) & 0xff;
    int b = (colour >> 0) & 0xff;
    int argb = ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | ((b & 0xff) << 0);
    String ebins = Integer.toHexString(argb);
    while (ebins.length() < 8) {
      ebins = '0' + ebins;
    }
    if (doodaa) {
      return "@0x" + ebins + '@';
    } else {
      return ebins;
    }
  }

  protected void resetGame() {
    systemUpdate = 0;
    combatStyle = 0;
    logoutTimeout = 0;
    loginScreen = 0;
    loggedIn = 1;
    resetPMText();
    surface.blackScreen();
    surface.draw(graphics, 0, 0);
    for (int i = 0; i < objectCount; i++) {
      scene.removeModel(objectModel[i]);
      world.removeObject(objectX[i], objectY[i], objectId[i]);
    }

    for (int j = 0; j < wallObjectCount; j++) {
      scene.removeModel(wallObjectModel[j]);
      world.removeWallObject(wallObjectX[j], wallObjectY[j], wallObjectDirection[j], wallObjectId[j]);
    }

    objectCount = 0;
    wallObjectCount = 0;
    groundItemCount = 0;
    playerCount = 0;
    for (int k = 0; k < playersServerMax; k++) {
      playerServer[k] = null;
    }

    for (int l = 0; l < playersMax; l++) {
      players[l] = null;
    }

    npcCount = 0;
    for (int i1 = 0; i1 < npcsServerMax; i1++) {
      npcsServer[i1] = null;
    }

    for (int j1 = 0; j1 < npcsMax; j1++) {
      npcs[j1] = null;
    }

    for (int k1 = 0; k1 < 50; k1++) {
      prayerOn[k1] = false;
    }

    mouseButtonClick = 0;
    super.lastMouseButtonDown = 0;
    super.mouseButtonDown = 0;
    showDialogShop = false;
    showDialogBank = false;
    isSleeping = false;
    super.friendListCount = 0;
    if (Version.CLIENT > 204) {
      showDialogReportAbuseStep = 0;

      for (int i = 0; i < messageShitSize; i++) {
        messageMessages[i] = null;
        messageHistoryTimeout[i] = 0;
        messageSenders[i] = null;
        messageCrowns[i] = 0;
        messageSenderClans[i] = null;
        messageColor[i] = null;
        messageTypes[i] = 0;
      }

      panelMessageTabs.clearList(controlTextListChat);
      panelMessageTabs.clearList(controlTextListQuest);
      panelMessageTabs.clearList(controlTextListPrivate);
    }
  }

  private void resetPMText() {
    super.inputPmCurrent = "";
    super.inputPmFinal = "";
  }

  protected void handleKeyPress(int code, char chr) {
    if (code == KeyEvent.VK_F2) {
      zoomControls = !zoomControls;
      showMessage("Zoom controls: " + (zoomControls ? "enabled" : "disabled"), 3);
    }
    if (code == KeyEvent.VK_F3) {
      switch (debugHud) {
        case DEBUG_HUD_ALL:
          debugHud = DEBUG_HUD_NONE;
          showMessage("Debug HUD show none", 3);
          break;
        case DEBUG_HUD_NAMES:
          debugHud = DEBUG_HUD_ALL;
          showMessage("Debug HUD show all", 3);
          break;
        case DEBUG_HUD_HITBOXES:
          debugHud = DEBUG_HUD_NAMES;
          showMessage("Debug HUD show names", 3);
          break;
        case DEBUG_HUD_NONE:
        default:
          debugHud = DEBUG_HUD_HITBOXES;
          showMessage("Debug HUD show hitboxes", 3);
          break;
      }
    }
    if (zoomControls) {
      if (code == KeyEvent.VK_PAGE_DOWN) {
        axisRotation += 5;
      }
      if (code == KeyEvent.VK_PAGE_UP) {
        axisRotation -= 5;
      }
      if (code == KeyEvent.VK_UP) {
        cameraZoom -= 5;
      }
      if (code == KeyEvent.VK_DOWN) {
        cameraZoom += 5;
      }
    }

    if (loggedIn == 0) {
      if (loginScreen == 0 && panelLoginWelcome != null) {
        panelLoginWelcome.keyPress(chr);
      }
      if (loginScreen == 1 && panelLoginNewuser != null) {
        panelLoginNewuser.keyPress(chr);
      }
      if (loginScreen == 2 && panelLoginExistinguser != null) {
        panelLoginExistinguser.keyPress(chr);
      }
    }
    if (loggedIn == 1) {
      if (showAppearanceChange && panelAppearance != null) {
        panelAppearance.keyPress(chr);
        return;
      }
      if (showDialogSocialInput == 0 &&
          showDialogReportAbuseStep == 0 &&
          !isSleeping &&
          panelMessageTabs != null &&
          inputPopupType == 0) {
        panelMessageTabs.keyPress(chr);
      }
    }
  }

  private void showMessage(String message, int type) {
    if (Version.CLIENT > 204) {
      // laziness
      if (type == 3) {
        type = 0;
      } else if (type == 4) {
        type = 7;
      }
      showMessage(type, message, null, null, 0, null, false);
      return;
    }
    if (type == 2 || type == 4 || type == 6) {
      for (; message.length() > 5 &&
             message.charAt(0) == '@' &&
             message.charAt(4) == '@'; message = message.substring(5)) {
      }
      int j = message.indexOf(":");
      if (j != -1) {
        String s1 = message.substring(0, j);
        long l = Utility.username2hash(s1);
        for (int i1 = 0; i1 < super.ignoreListCount; i1++) {
          if (super.ignoreListHashes[i1] == l) {
            return;
          }
        }

      }
    }
    if (type == 2) {
      message = "@yel@" + message;
    }
    if (type == 3 || type == 4) {
      message = "@whi@" + message;
    }
    if (type == 6) {
      message = "@cya@" + message;
    }
    if (messageTabSelected != 0) {
      if (type == 4 || type == 3) {
        messageTabFlashAll = 200;
      }
      if (type == 2 && messageTabSelected != 1) {
        messageTabFlashHistory = 200;
      }
      if (type == 5 && messageTabSelected != 2) {
        messtageTabFlashQuest = 200;
      }
      if (type == 6 && messageTabSelected != 3) {
        messageTabFlashPrivate = 200;
      }
      if (type == 3 && messageTabSelected != 0) {
        messageTabSelected = 0;
      }
      if (type == 6 && messageTabSelected != 3 && messageTabSelected != 0) {
        messageTabSelected = 0;
      }
    }
    for (int k = messageShitSize - 1; k > 0; k--) {
      messageHistory[k] = messageHistory[k - 1];
      messageHistoryTimeout[k] = messageHistoryTimeout[k - 1];
    }

    messageHistory[0] = message;
    messageHistoryTimeout[0] = 600; // 300
    if (type == 2) {
      if (panelMessageTabs.controlFlashText[controlTextListChat] ==
          panelMessageTabs.controlListEntryCount[controlTextListChat] - 4) {
        panelMessageTabs.removeListEntry(controlTextListChat, message, true);
      } else {
        panelMessageTabs.removeListEntry(controlTextListChat, message, false);
      }
    }
    if (type == 5) {
      if (panelMessageTabs.controlFlashText[controlTextListQuest] ==
          panelMessageTabs.controlListEntryCount[controlTextListQuest] - 4) {
        panelMessageTabs.removeListEntry(controlTextListQuest, message, true);
      } else {
        panelMessageTabs.removeListEntry(controlTextListQuest, message, false);
      }
    }
    if (type == 6) {
      if (panelMessageTabs.controlFlashText[controlTextListPrivate] ==
          panelMessageTabs.controlListEntryCount[controlTextListPrivate] - 4) {
        panelMessageTabs.removeListEntry(controlTextListPrivate, message, true);
        return;
      }
      panelMessageTabs.removeListEntry(controlTextListPrivate, message, false);
    }
  }

  @Override
  public void showMessage(int messageType, String message, String sender, String senderClan, int crownId, String colorOverride, boolean forceShow) {
    if ((messageType == 1 || messageType == 4 || messageType == 6) && senderClan != null && !forceShow) {
      String formattedName = Utility.formatName(senderClan);
      if (formattedName == null) {
        return;
      }
      for (int i = 0; i < ignoreListCount; i++) {
        if (formattedName.equals(Utility.formatName(ignoreListAccNames[i]))) {
          return;
        }
      }
    }

    String color = messageColors[messageType];
    if (colorOverride != null) {
      color = colorOverride;
    }

    if (messageTabSelected != 0) {
      if (messageType == 0 || messageType == 7) {
        messageTabFlashAll = 200;
      }
      if ((messageType == 5 || messageType == 1 || messageType == 2) && messageTabSelected != 3) {
        messageTabFlashPrivate = 200;
      }
      if (messageType == 3 && messageTabSelected != 2) {
        messtageTabFlashQuest = 200;
      }
      if (messageType == 4 && messageTabSelected != 1) {
        messageTabFlashHistory = 200;
      }
      if (messageType == 0 && messageTabSelected != 0) {
        messageTabSelected = 0;
      }
      if ((messageType == 5 || messageType == 1 || messageType == 2) &&
          messageTabSelected != 3 &&
          messageTabSelected != 0) {
        messageTabSelected = 0;
      }
    }

    for (int i = messageShitSize - 1; i > 0; i--) {
      messageTypes[i] = messageTypes[i - 1];
      messageHistoryTimeout[i] = messageHistoryTimeout[i - 1];
      messageCrowns[i] = messageCrowns[i - 1];
      messageSenders[i] = messageSenders[i - 1];
      messageSenderClans[i] = messageSenderClans[i - 1];
      messageMessages[i] = messageMessages[i - 1];
      messageColor[i] = messageColor[i - 1];
    }

    messageTypes[0] = messageType;
    messageHistoryTimeout[0] = 600; // 300
    messageSenders[0] = sender;
    messageCrowns[0] = crownId;
    messageSenderClans[0] = senderClan;
    messageMessages[0] = message;
    messageColor[0] = color;

    String fullMessage = color + formatMessage(message, sender, messageType);
    if (messageType == 4) {
      if (panelMessageTabs.controlFlashText[controlTextListChat] !=
          4 - panelMessageTabs.controlListEntryCount[controlTextListChat]) {
        panelMessageTabs.removeListEntry(controlTextListChat, fullMessage, false, senderClan, sender, crownId);
      } else {
        panelMessageTabs.removeListEntry(controlTextListChat, fullMessage, true, senderClan, sender, crownId);
      }
    }
    if (messageType == 3) {
      if (panelMessageTabs.controlFlashText[controlTextListQuest] !=
          4 - panelMessageTabs.controlListEntryCount[controlTextListQuest]) {
        panelMessageTabs.removeListEntry(controlTextListQuest, fullMessage, false, null, null, 0);
      } else {
        panelMessageTabs.removeListEntry(controlTextListQuest, fullMessage, true, null, null, 0);
      }
    }
    if (messageType == 1 || messageType == 2) {
      if (messageType != 1) {
        crownId = 0;
      }
      if (panelMessageTabs.controlFlashText[controlTextListPrivate] !=
          4 - panelMessageTabs.controlListEntryCount[controlTextListPrivate]) {
        panelMessageTabs.removeListEntry(controlTextListPrivate, fullMessage, false, senderClan, sender, crownId);
      } else {
        panelMessageTabs.removeListEntry(controlTextListPrivate, fullMessage, true, senderClan, sender, crownId);
      }
    }
  }

  private String formatMessage(String message, String sender, int type) {
    if (type == 5 || sender == null || sender.length() == 0) {
      return message;
    }
    switch (type) {
      case 0:
      case 3:
      case 4:
      case 7:
        return sender + ": " + message;
      case 1:
        return sender + " tells you: " + message;
      case 2:
        return "You tell " + sender + ": " + message;
      case 6:
        return sender + " wishes to trade with you.";
      default:
        return "";
    }
  }

  private void drawUiTabInventory(boolean nomenus) {
    int uiX = surface.width2 - 248;
    surface.drawSprite(uiX, 3, spriteMedia + 1);
    for (int itemIndex = 0; itemIndex < inventoryMaxItemCount; itemIndex++) {
      int slotX = uiX + (itemIndex % 5) * 49;
      int slotY = 36 + (itemIndex / 5) * 34;
      if (itemIndex < inventoryItemsCount && inventoryEquipped[itemIndex] == 1) {
        surface.drawBoxAlpha(slotX, slotY, 49, 34, 0xff0000, 128);
      } else {
        surface.drawBoxAlpha(slotX, slotY, 49, 34, Utility.rgb2long(181, 181, 181), 128);
      }
      if (itemIndex < inventoryItemsCount) {
        surface.spriteClipping(slotX,
                               slotY,
                               48,
                               32,
                               spriteItem + GameData.itemPicture[inventoryItemId[itemIndex]],
                               GameData.itemMask[inventoryItemId[itemIndex]],
                               0,
                               0,
                               false
        );
        if (GameData.itemStackable[inventoryItemId[itemIndex]] == 0) {
          surface.drawstring(String.valueOf(inventoryItemStackCount[itemIndex]), slotX + 1, slotY + 10, 1, 0xffff00);
        }
      }
    }

    for (int rows = 1; rows <= 4; rows++) {
      surface.drawLineVert(uiX + rows * 49, 36, (inventoryMaxItemCount / 5) * 34, 0);
    }

    for (int cols = 1; cols <= inventoryMaxItemCount / 5 - 1; cols++) {
      surface.drawLineHoriz(uiX, 36 + cols * 34, 245, 0);
    }

    if (!nomenus) {
      return;
    }
    int mouseX = super.mouseX - (surface.width2 - 248);
    int mouseY = super.mouseY - 36;
    if (mouseX >= 0 && mouseY >= 0 && mouseX < 248 && mouseY < (inventoryMaxItemCount / 5) * 34) {
      int itemIndex = mouseX / 49 + (mouseY / 34) * 5;
      if (itemIndex < inventoryItemsCount) {
        int i2 = inventoryItemId[itemIndex];
        if (selectedSpell >= 0) {
          if (GameData.spellType[selectedSpell] == 3) {
            menuItemText1[menuItemsCount] = "Cast " +
                                            GameData.spellName[selectedSpell] +
                                            " @0x919191@(" +
                                            selectedSpell +
                                            ") @whi@on";
            menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[i2] + " @0x919191@(" + i2 + ")";
            menuItemID[menuItemsCount] = 600;
            menuSourceType[menuItemsCount] = itemIndex;
            menuSourceIndex[menuItemsCount] = selectedSpell;
            menuItemsCount++;
            return;
          }
        } else {
          if (selectedItemInventoryIndex >= 0) {
            menuItemText1[menuItemsCount] = "Use " +
                                            selectedItemName +
                                            " @0x919191@(" +
                                            inventoryItemId[selectedItemInventoryIndex] +
                                            ") @whi@with";
            menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[i2] + " @0x919191@(" + i2 + ")";
            menuItemID[menuItemsCount] = 610;
            menuSourceType[menuItemsCount] = itemIndex;
            menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
            menuItemsCount++;
            return;
          }
          if (inventoryEquipped[itemIndex] == 1) {
            menuItemText1[menuItemsCount] = "Remove";
            menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[i2] + " @0x919191@(" + i2 + ")";
            menuItemID[menuItemsCount] = 620;
            menuSourceType[menuItemsCount] = itemIndex;
            menuItemsCount++;
          } else if (GameData.itemWearable[i2] != 0) {
            if ((GameData.itemWearable[i2] & 24) != 0)// 0x18
            {
              menuItemText1[menuItemsCount] = "Wield";
            } else {
              menuItemText1[menuItemsCount] = "Wear";
            }
            menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[i2] + " @0x919191@(" + i2 + ")";
            menuItemID[menuItemsCount] = 630;
            menuSourceType[menuItemsCount] = itemIndex;
            menuItemsCount++;
          }
          if (!GameData.itemCommand[i2].equals("")) {
            menuItemText1[menuItemsCount] = GameData.itemCommand[i2];
            menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[i2];
            if (inventoryEquipped[itemIndex] != 1 && GameData.itemWearable[i2] == 0) {
              menuItemText2[menuItemsCount] += " @0x919191@(" + i2 + ")";
            }
            menuItemID[menuItemsCount] = 640;
            menuSourceType[menuItemsCount] = itemIndex;
            menuItemsCount++;
          }
          menuItemText1[menuItemsCount] = "Use";
          menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[i2];
          if (inventoryEquipped[itemIndex] != 1 &&
              GameData.itemWearable[i2] == 0 &&
              GameData.itemCommand[i2].equals("")) {
            menuItemText2[menuItemsCount] += " @0x919191@(" + i2 + ")";
          }
          menuItemID[menuItemsCount] = 650;
          menuSourceType[menuItemsCount] = itemIndex;
          menuItemsCount++;
          menuItemText1[menuItemsCount] = "Drop";
          menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[i2];
          menuItemID[menuItemsCount] = 660;
          menuSourceType[menuItemsCount] = itemIndex;
          menuItemsCount++;
          menuItemText1[menuItemsCount] = "Examine";
          menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[i2];
          menuItemID[menuItemsCount] = 3600;
          menuSourceType[menuItemsCount] = i2;
          menuItemsCount++;
        }
      }
    }
  }

  private void autorotateCamera() {
    if ((cameraAngle & 1) == 1 && isValidCameraAngle(cameraAngle)) {
      return;
    }
    if ((cameraAngle & 1) == 0 && isValidCameraAngle(cameraAngle)) {
      if (isValidCameraAngle(cameraAngle + 1 & 7)) {
        cameraAngle = cameraAngle + 1 & 7;
        return;
      }
      if (isValidCameraAngle(cameraAngle + 7 & 7)) {
        cameraAngle = cameraAngle + 7 & 7;
      }
      return;
    }
    int ai[] = {
      1, -1, 2, -2, 3, -3, 4
    };
    for (int i = 0; i < 7; i++) {
      if (!isValidCameraAngle(cameraAngle + ai[i] + 8 & 7)) {
        continue;
      }
      cameraAngle = cameraAngle + ai[i] + 8 & 7;
      break;
    }

    if ((cameraAngle & 1) == 0 && isValidCameraAngle(cameraAngle)) {
      if (isValidCameraAngle(cameraAngle + 1 & 7)) {
        cameraAngle = cameraAngle + 1 & 7;
        return;
      }
      if (isValidCameraAngle(cameraAngle + 7 & 7)) {
        cameraAngle = cameraAngle + 7 & 7;
      }
    }
  }

  private void drawRightClickMenu() {
    if (mouseButtonClick != 0) {
      for (int i = 0; i < menuItemsCount; i++) {
        int k = menuX + 2;
        int i1 = menuY + 27 + i * 15;
        if (super.mouseX <= k - 2 ||
            super.mouseY <= i1 - 12 ||
            super.mouseY >= i1 + 4 ||
            super.mouseX >= (k - 3) + menuWidth) {
          continue;
        }
        menuItemClick(menuIndices[i]);
        break;
      }

      mouseButtonClick = 0;
      showRightClickMenu = false;
      return;
    }
    if (super.mouseX < menuX - 10 ||
        super.mouseY < menuY - 10 ||
        super.mouseX > menuX + menuWidth + 10 ||
        super.mouseY > menuY + menuHeight + 10) {
      showRightClickMenu = false;
      return;
    }
    surface.drawBoxAlpha(menuX, menuY, menuWidth, menuHeight, 0xd0d0d0, 160);
    surface.drawstring("Choose option", menuX + 2, menuY + 12, 1, 65535);
    for (int j = 0; j < menuItemsCount; j++) {
      int l = menuX + 2;
      int j1 = menuY + 27 + j * 15;
      int k1 = 0xffffff;
      if (super.mouseX > l - 2 &&
          super.mouseY > j1 - 12 &&
          super.mouseY < j1 + 4 &&
          super.mouseX < (l - 3) + menuWidth) {
        k1 = 0xffff00;
      }
      surface.drawstring(menuItemText1[menuIndices[j]] + " " + menuItemText2[menuIndices[j]], l, j1, 1, k1);
    }

  }

  public MinimapEntity[] drawMinimap(int uiX, int uiY, int uiWidth, int uiHeight, int scale, boolean drawEntities) {
    MinimapEntity[] entities = null;
    int entityIndex = 0;
    if (drawEntities) {
      entities = new MinimapEntity[objectCount + groundItemCount + npcCount + playerCount];
    }
    surface.drawBox(uiX, uiY, uiWidth, uiHeight, 0);
    surface.setBounds(uiX, uiY, uiX + uiWidth, uiY + uiHeight);
    int k = scale + minimapRandom_2;
    int i1 = cameraRotation + minimapRandom_1 & 255;//0xff;
    int k1 = ((localPlayer.currentX - 6040) * 3 * k) / 2048;
    int i3 = ((localPlayer.currentY - 6040) * 3 * k) / 2048;
    int k4 = Scene.sin2048Cache[1024 - i1 * 4 & 0x3ff];
    int i5 = Scene.sin2048Cache[(1024 - i1 * 4 & 0x3ff) + 1024];
    int k5 = i3 * k4 + k1 * i5 >> 18;
    i3 = i3 * i5 - k1 * k4 >> 18;
    k1 = k5;
    surface.drawMinimapSprite((uiX + uiWidth / 2) - k1,
                              uiY + uiHeight / 2 + i3,
                              spriteMedia - 1,
                              i1 + 64 & 255,
                              k
    );// landscape
    if (drawEntities) {
      for (int i = 0; i < objectCount; i++) {
        int x = (((objectX[i] * magicLoc + 64) - localPlayer.currentX) * 3 * k) / 2048;
        int y = (((objectY[i] * magicLoc + 64) - localPlayer.currentY) * 3 * k) / 2048;
        int l5 = y * k4 + x * i5 >> 18;
        y = y * i5 - x * k4 >> 18;
        x = l5;
        drawMinimapEntity(uiX + uiWidth / 2 + x, (uiY + uiHeight / 2) - y, 65535);
        entities[entityIndex++] = new MinimapEntity(uiX + uiWidth / 2 + x,
                                                    (uiY + uiHeight / 2) - y,
                                                    i,
                                                    MinimapEntityType.OBJECT
        );
      }

      for (int i = 0; i < groundItemCount; i++) {
        int x = (((groundItemX[i] * magicLoc + 64) - localPlayer.currentX) * 3 * k) / 2048;
        int y = (((groundItemY[i] * magicLoc + 64) - localPlayer.currentY) * 3 * k) / 2048;
        int i6 = y * k4 + x * i5 >> 18;
        y = y * i5 - x * k4 >> 18;
        x = i6;
        drawMinimapEntity(uiX + uiWidth / 2 + x, (uiY + uiHeight / 2) - y, 0xff0000);
        entities[entityIndex++] = new MinimapEntity(uiX + uiWidth / 2 + x,
                                                    (uiY + uiHeight / 2) - y,
                                                    i,
                                                    MinimapEntityType.GROUNDITEM
        );
      }

      for (int i = 0; i < npcCount; i++) {
        GameCharacter character = npcs[i];
        int x = ((character.currentX - localPlayer.currentX) * 3 * k) / 2048;
        int y = ((character.currentY - localPlayer.currentY) * 3 * k) / 2048;
        int j6 = y * k4 + x * i5 >> 18;
        y = y * i5 - x * k4 >> 18;
        x = j6;
        drawMinimapEntity(uiX + uiWidth / 2 + x, (uiY + uiHeight / 2) - y, 0xffff00);
        entities[entityIndex++] = new MinimapEntity(uiX + uiWidth / 2 + x,
                                                    (uiY + uiHeight / 2) - y,
                                                    i,
                                                    MinimapEntityType.NPC
        );
      }

      for (int i = 0; i < playerCount; i++) {
        GameCharacter character_1 = players[i];
        int x = ((character_1.currentX - localPlayer.currentX) * 3 * k) / 2048;
        int y = ((character_1.currentY - localPlayer.currentY) * 3 * k) / 2048;
        int k6 = y * k4 + x * i5 >> 18;
        y = y * i5 - x * k4 >> 18;
        x = k6;
        int j8 = 0xffffff;
        for (int k8 = 0; k8 < super.friendListCount; k8++) {
          if (character_1.hash != super.friendListHashes[k8] || super.friendListOnline[k8] != 255) {
            continue;
          }
          j8 = 65280;
          break;
        }

        drawMinimapEntity(uiX + uiWidth / 2 + x, (uiY + uiHeight / 2) - y, j8);
        entities[entityIndex++] = new MinimapEntity(uiX + uiWidth / 2 + x,
                                                    (uiY + uiHeight / 2) - y,
                                                    i,
                                                    MinimapEntityType.PLAYER
        );
      }
    }

    surface.drawCircle(uiX + uiWidth / 2, uiY + uiHeight / 2, 2, 0xffffff, 255);
    surface.drawMinimapSprite(uiX + 19, 55, spriteMedia + 24, cameraRotation + 128 & 255, 128);// compass
    surface.setBounds(0, 0, gameWidth, gameHeight + 12);
    return entities;
  }

  private void drawUiTabMinimap(boolean nomenus) {
    int uiX = surface.width2 - 199;
    int uiY = 36;
    int uiWidth = 156;// '\234';
    int uiHeight = 152;// '\230';
    surface.drawSprite(uiX - 49, 3, spriteMedia + 2);
    drawMinimap(uiX + 40, uiY, uiWidth, uiHeight, 192, true);
    if (!nomenus) {
      return;
    }
    int mouseX = super.mouseX - (surface.width2 - 199);
    int mouseY = super.mouseY - uiY;
    if (mouseX >= 40 && mouseY >= 0 && mouseX < 196 && mouseY < 152) {
      int c1 = 156;// '\234';
      int c3 = 152;// '\230';
      int l = 192 + minimapRandom_2;
      int j1 = cameraRotation + minimapRandom_1 & 255;// 0xff
      int j = surface.width2 - 199;
      j += 40;
      int dx = ((super.mouseX - (j + c1 / 2)) * 16384) / (3 * l);
      int dy = ((super.mouseY - (uiY + c3 / 2)) * 16384) / (3 * l);
      int l4 = Scene.sin2048Cache[1024 - j1 * 4 & 1023];// 0x3ff
      int j5 = Scene.sin2048Cache[(1024 - j1 * 4 & 1023) + 1024];// 0x3ff
      int l6 = dy * l4 + dx * j5 >> 15;
      dy = dy * j5 - dx * l4 >> 15;
      dx = l6;
      dx += localPlayer.currentX;
      dy = localPlayer.currentY - dy;
      if (mouseButtonClick == 1) {
        walkToActionSource(localRegionX, localRegionY, dx / 128, dy / 128, false);
      }
      mouseButtonClick = 0;
    }
  }

  private void drawDialogTradeConfirm() {
    int dialogX = gameWidth / 2 - 234; // 22
    int dialogY = gameHeight / 2 - 131; // 36
    surface.drawBox(dialogX, dialogY, 468, 16, 192);
    surface.drawBoxAlpha(dialogX, dialogY + 16, 468, 246, 0x989898, 160);
    String name = Version.CLIENT > 204 ? tradeRecipientConfirmName : Utility.hash2username(tradeRecipientConfirmHash);
    surface.drawStringCenter("Please confirm your trade with @yel@" + name, dialogX + 234, dialogY + 12, 1, 0xffffff);
    surface.drawStringCenter("You are about to give:", dialogX + 117, dialogY + 30, 1, 0xffff00);
    for (int j = 0; j < tradeConfirmItemsCount; j++) {
      String s = GameData.itemName[tradeConfirmItems[j]];
      if (GameData.itemStackable[tradeConfirmItems[j]] == 0) {
        s = s + " x " + formatNumber(tradeConfirmItemCount[j]);
      }
      surface.drawStringCenter(s, dialogX + 117, dialogY + 42 + j * 12, 1, 0xffffff);
    }

    if (tradeConfirmItemsCount == 0) {
      surface.drawStringCenter("Nothing!", dialogX + 117, dialogY + 42, 1, 0xffffff);
    }
    surface.drawStringCenter("In return you will receive:", dialogX + 351, dialogY + 30, 1, 0xffff00);
    for (int k = 0; k < tradeRecipientConfirmItemsCount; k++) {
      String s1 = GameData.itemName[tradeRecipientConfirmItems[k]];
      if (GameData.itemStackable[tradeRecipientConfirmItems[k]] == 0) {
        s1 = s1 + " x " + formatNumber(tradeRecipientConfirmItemCount[k]);
      }
      surface.drawStringCenter(s1, dialogX + 351, dialogY + 42 + k * 12, 1, 0xffffff);
    }

    if (tradeRecipientConfirmItemsCount == 0) {
      surface.drawStringCenter("Nothing!", dialogX + 351, dialogY + 42, 1, 0xffffff);
    }
    surface.drawStringCenter("Are you sure you want to do this?", dialogX + 234, dialogY + 200, 4, 65535);
    surface.drawStringCenter("There is NO WAY to reverse a trade if you change your mind.",
                             dialogX + 234,
                             dialogY + 215,
                             1,
                             0xffffff
    );
    surface.drawStringCenter("Remember that not all players are trustworthy",
                             dialogX + 234,
                             dialogY + 230,
                             1,
                             0xffffff
    );
    if (!tradeConfirmAccepted) {
      surface.drawSprite((dialogX + 118) - 35, dialogY + 238, spriteMedia + 25);
      surface.drawSprite((dialogX + 352) - 35, dialogY + 238, spriteMedia + 26);
    } else {
      surface.drawStringCenter("Waiting for other player...", dialogX + 234, dialogY + 250, 1, 0xffff00);
    }
    if (mouseButtonClick == 1) {
      if (super.mouseX < dialogX ||
          super.mouseY < dialogY ||
          super.mouseX > dialogX + 468 ||
          super.mouseY > dialogY + 262) {
        showDialogTradeConfirm = false;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_DECLINE));
        super.clientStream.sendPacket();
      }
      if (super.mouseX >= (dialogX + 118) - 35 &&
          super.mouseX <= dialogX + 118 + 70 &&
          super.mouseY >= dialogY + 238 &&
          super.mouseY <= dialogY + 238 + 21) {
        tradeConfirmAccepted = true;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_CONFIRM_ACCEPT));
        super.clientStream.sendPacket();
      }
      if (super.mouseX >= (dialogX + 352) - 35 &&
          super.mouseX <= dialogX + 353 + 70 &&
          super.mouseY >= dialogY + 238 &&
          super.mouseY <= dialogY + 238 + 21) {
        showDialogTradeConfirm = false;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_DECLINE));
        super.clientStream.sendPacket();
      }
      mouseButtonClick = 0;
    }
  }

  private void setActiveUiTab() {
    // TODO maek work
    if (showUiTab == 0 &&
        super.mouseX >= surface.width2 - 35 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 &&
        super.mouseY < 35) {
      showUiTab = 1;
    }
    if (showUiTab == 0 &&
        super.mouseX >= surface.width2 - 35 - 33 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 33 &&
        super.mouseY < 35) {
      showUiTab = 2;
      minimapRandom_1 = (int) (Math.random() * 13D) - 6;
      minimapRandom_2 = (int) (Math.random() * 23D) - 11;
    }
    if (showUiTab == 0 &&
        super.mouseX >= surface.width2 - 35 - 66 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 66 &&
        super.mouseY < 35) {
      showUiTab = 3;
    }
    if (showUiTab == 0 &&
        super.mouseX >= surface.width2 - 35 - 99 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 99 &&
        super.mouseY < 35) {
      showUiTab = 4;
    }
    if (showUiTab == 0 &&
        super.mouseX >= surface.width2 - 35 - 132 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 132 &&
        super.mouseY < 35) {
      showUiTab = 5;
    }
    if (showUiTab == 0 &&
        super.mouseX >= surface.width2 - 35 - 165 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 165 &&
        super.mouseY < 35) {
      showUiTab = 6;
    }
    if (showUiTab != 0 &&
        super.mouseX >= surface.width2 - 35 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 &&
        super.mouseY < 26) {
      showUiTab = 1;
    }
    if (showUiTab != 0 &&
        showUiTab != 2 &&
        super.mouseX >= surface.width2 - 35 - 33 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 33 &&
        super.mouseY < 26) {
      showUiTab = 2;
      minimapRandom_1 = (int) (Math.random() * 13D) - 6;
      minimapRandom_2 = (int) (Math.random() * 23D) - 11;
    }
    if (showUiTab != 0 &&
        super.mouseX >= surface.width2 - 35 - 66 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 66 &&
        super.mouseY < 26) {
      showUiTab = 3;
    }
    if (showUiTab != 0 &&
        super.mouseX >= surface.width2 - 35 - 99 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 99 &&
        super.mouseY < 26) {
      showUiTab = 4;
    }
    if (showUiTab != 0 &&
        super.mouseX >= surface.width2 - 35 - 132 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 132 &&
        super.mouseY < 26) {
      showUiTab = 5;
    }
    if (showUiTab != 0 &&
        super.mouseX >= surface.width2 - 35 - 165 &&
        super.mouseY >= 3 &&
        super.mouseX < surface.width2 - 3 - 165 &&
        super.mouseY < 26) {
      showUiTab = 6;
    }
    if (showUiTab == 1 &&
        (super.mouseX < surface.width2 - 248 || super.mouseY > 36 + (inventoryMaxItemCount / 5) * 34)) {
      showUiTab = 0;
    }
    if (showUiTab == 3 && (super.mouseX < surface.width2 - 199 || super.mouseY > 316)) {
      showUiTab = 0;
    }
    if ((showUiTab == 2 || showUiTab == 4 || showUiTab == 5) &&
        (super.mouseX < surface.width2 - 199 || super.mouseY > 240)) {
      showUiTab = 0;
    }
    if (showUiTab == 6 && (super.mouseX < surface.width2 - 199 || super.mouseY > 315)) {
      showUiTab = 0;
    }
  }

  private void drawOptionMenu() {
    if (mouseButtonClick != 0) {
      for (int i = 0; i < optionMenuCount; i++) {
        if (super.mouseX >= surface.textWidth(optionMenuEntry[i], 1) ||
            super.mouseY <= i * 12 ||
            super.mouseY >= 12 + i * 12) {
          continue;
        }
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CHOOSE_OPTION));
        super.clientStream.putByte(i);
        super.clientStream.sendPacket();
        break;
      }

      mouseButtonClick = 0;
      showOptionMenu = false;
      return;
    }
    for (int j = 0; j < optionMenuCount; j++) {
      int k = 65535;
      if (super.mouseX < surface.textWidth(optionMenuEntry[j], 1) &&
          super.mouseY > j * 12 &&
          super.mouseY < 12 + j * 12) {
        k = 0xff0000;
      }
      surface.drawstring((j + 1) + ") - " + optionMenuEntry[j], 6, 12 + j * 12, 1, k);
    }

  }

  void drawNpc(int x, int y, int w, int h, int id, int tx, int ty) {
    GameCharacter character = npcs[id];
    int cameraAnim = character.animationCurrent + (cameraRotation + 16) / 32 & 7;
    boolean flag = false;
    int anim = cameraAnim;
    if (anim == 5) {
      anim = 3;
      flag = true;
    } else if (anim == 6) {
      anim = 2;
      flag = true;
    } else if (anim == 7) {
      anim = 1;
      flag = true;
    }
    int j2 = anim * 3 + npcWalkModel[(character.stepCount / GameData.npcWalkModel[character.npcId]) % 4];
    if (character.animationCurrent == 8) {
      anim = 5;
      cameraAnim = 2;
      flag = false;
      x -= (GameData.npcCombatAnimation[character.npcId] * ty) / 100;
      j2 = anim * 3 + npcCombatModelArray1[(loginTimer / (GameData.npcCombatModel[character.npcId] - 1)) % 8];
    } else if (character.animationCurrent == 9) {
      anim = 5;
      cameraAnim = 2;
      flag = true;
      x += (GameData.npcCombatAnimation[character.npcId] * ty) / 100;
      j2 = anim * 3 + npcCombatModelArray2[(loginTimer / GameData.npcCombatModel[character.npcId]) % 8];
    }
    for (int k2 = 0; k2 < 12; k2++) {
      int l2 = npcAnimationArray[cameraAnim][k2];
      int k3 = GameData.npcSprite[character.npcId][l2];
      if (k3 >= 0) {
        int i4 = 0;
        int j4 = 0;
        int k4 = j2;
        if (flag && anim >= 1 && anim <= 3 && GameData.animationHasF[k3] == 1) {
          k4 += 15;
        }
        if (anim != 5 || GameData.animationHasA[k3] == 1) {
          int l4 = k4 + GameData.animationNumber[k3];
          i4 = (i4 * w) / surface.spriteWidthFull[l4];
          j4 = (j4 * h) / surface.spriteHeightFull[l4];
          int i5 = (w * surface.spriteWidthFull[l4]) / surface.spriteWidthFull[GameData.animationNumber[k3]];
          i4 -= (i5 - w) / 2;
          int col = GameData.animationCharacterColour[k3];
          int skincol = 0;
          if (col == 1) {
            col = GameData.npcColourHair[character.npcId];
            skincol = GameData.npcColourSkin[character.npcId];
          } else if (col == 2) {
            col = GameData.npcColourTop[character.npcId];
            skincol = GameData.npcColourSkin[character.npcId];
          } else if (col == 3) {
            col = GameData.npcColorBottom[character.npcId];
            skincol = GameData.npcColourSkin[character.npcId];
          }
          surface.spriteClipping(x + i4, y + j4, i5, h, l4, col, skincol, tx, flag);
        }
      }
    }

    if (character.messageTimeout > 0) {
      addReceivedMessage(x, y, w, character.message);
    }

    if (character.animationCurrent == 8 || character.animationCurrent == 9 || character.combatTimer != 0) {
      if (character.combatTimer > 0) {
        int i3 = x;
        if (character.animationCurrent == 8) {
          i3 -= (20 * ty) / 100;
        } else if (character.animationCurrent == 9) {
          i3 += (20 * ty) / 100;
        }
        int l3 = (character.healthCurrent * 30) / character.healthMax;
        healthBarX[healthBarCount] = i3 + w / 2;
        healthBarY[healthBarCount] = y;
        healthBarMissing[healthBarCount++] = l3;
      }
      if (character.combatTimer > 150) {
        int j3 = x;
        if (character.animationCurrent == 8) {
          j3 -= (10 * ty) / 100;
        } else if (character.animationCurrent == 9) {
          j3 += (10 * ty) / 100;
        }
        surface.drawSprite((j3 + w / 2) - 12, (y + h / 2) - 12, spriteMedia + 12);
        surface.drawStringCenter(String.valueOf(character.damageTaken), (j3 + w / 2) - 1, y + h / 2 + 5, 3, 0xffffff);
      }
    }

    if (debugHud != DEBUG_HUD_NONE) {
      int dist = distance(localRegionX, localRegionY, characterLocalX(character), characterLocalY(character));
      if ((debugHud == DEBUG_HUD_NAMES || debugHud == DEBUG_HUD_ALL) &&
          character != null &&
          character.npcId >= 0 &&
          character.npcId < GameData.npcName.length) {
        String cmd = GameData.npcCommand[character.npcId];
        if (!cmd.equalsIgnoreCase("")) {
          addReceivedMessage(x, y, w, alphaize(0xdddddd, dist) + cmd);
        }
        id = character.npcId;
        int npclevel = (GameData.npcAttack[id] +
                        GameData.npcDefense[id] +
                        GameData.npcStrength[id] +
                        GameData.npcHits[id]) / 4;
        int playerlevel = (playerStatBase[0] + playerStatBase[1] + playerStatBase[2] + playerStatBase[3] + 27) / 4;
        int leveldiff = playerlevel - npclevel;
        int i1 = surface.getColor("yel");
        if (leveldiff < 0) {
          i1 = surface.getColor("or1");
        }
        if (leveldiff < -3) {
          i1 = surface.getColor("or2");
        }
        if (leveldiff < -6) {
          i1 = surface.getColor("or3");
        }
        if (leveldiff < -9) {
          i1 = surface.getColor("red");
        }
        if (leveldiff > 0) {
          i1 = surface.getColor("gr1");
        }
        if (leveldiff > 3) {
          i1 = surface.getColor("gr2");
        }
        if (leveldiff > 6) {
          i1 = surface.getColor("gr3");
        }
        if (leveldiff > 9) {
          i1 = surface.getColor("gre");
        }
        String s1 = " " + alphaize(i1, dist) + "(lvl-" + npclevel + ")";
        if (GameData.npcAttackable[id] <= 0) {
          s1 = "";
        }
        addReceivedMessage(x,
                           y,
                           w,
                           alphaize(0xaaaa00, dist) +
                           GameData.npcName[id] +
                           " " +
                           alphaize(0x919191, dist) +
                           "(" +
                           id +
                           ")" +
                           s1
        );
      }
      if (debugHud == DEBUG_HUD_HITBOXES || debugHud == DEBUG_HUD_ALL) {
        surface.drawBoxAlpha(x, y, w, h, 0xffff00, 60);
      }
    }
  }

  public int characterLocalX(GameCharacter c) {
    return (c.currentX - 64) / magicLoc;
  }

  public int characterLocalY(GameCharacter c) {
    return (c.currentY - 64) / magicLoc;
  }

  private void walkToWallObject(int i, int j, int k) {
    if (k == 0) {
      walkToActionSource(localRegionX, localRegionY, i, j - 1, i, j, false, true);
      return;
    }
    if (k == 1) {
      walkToActionSource(localRegionX, localRegionY, i - 1, j, i, j, false, true);
      return;
    } else {
      walkToActionSource(localRegionX, localRegionY, i, j, i, j, true, true);
      return;
    }
  }

  private GameCharacter addNpc(int serverIndex, int x, int y, int sprite, int type) {
    if (npcsServer[serverIndex] == null) {
      npcsServer[serverIndex] = new GameCharacter();
      npcsServer[serverIndex].serverIndex = serverIndex;
    }
    GameCharacter character = npcsServer[serverIndex];
    boolean foundNpc = false;
    for (int i = 0; i < npcCacheCount; i++) {
      if (npcsCache[i].serverIndex != serverIndex) {
        continue;
      }
      foundNpc = true;
      break;
    }

    if (foundNpc) {
      character.npcId = type;
      character.animationNext = sprite;
      int waypointIdx = character.waypointCurrent;
      if (x != character.waypointsX[waypointIdx] || y != character.waypointsY[waypointIdx]) {
        character.waypointCurrent = waypointIdx = (waypointIdx + 1) % 10;
        character.waypointsX[waypointIdx] = x;
        character.waypointsY[waypointIdx] = y;
      }
    } else {
      character.serverIndex = serverIndex;
      character.movingStep = 0;
      character.waypointCurrent = 0;
      character.waypointsX[0] = character.currentX = x;
      character.waypointsY[0] = character.currentY = y;
      character.npcId = type;
      character.animationNext = character.animationCurrent = sprite;
      character.stepCount = 0;
    }
    npcs[npcCount++] = character;
    return character;
  }

  private void drawDialogBank() {
    int dialogWidth = 408;// '\u0198';
    int dialogHeight = 334;// '\u014E';
    if (bankActivePage > 0 && bankItemCount <= 48) {
      bankActivePage = 0;
    }
    if (bankActivePage > 1 && bankItemCount <= 96) {
      bankActivePage = 1;
    }
    if (bankActivePage > 2 && bankItemCount <= 144) {
      bankActivePage = 2;
    }
    if (bankSelectedItemSlot >= bankItemCount || bankSelectedItemSlot < 0) {
      bankSelectedItemSlot = -1;
    }
    if (bankSelectedItemSlot != -1 && bankItems[bankSelectedItemSlot] != bankSelectedItem) {
      bankSelectedItemSlot = -1;
      bankSelectedItem = -2;
    }
    if (mouseButtonClick != 0 && inputPopupType == 0) {
      mouseButtonClick = 0;
      int mouseX = super.mouseX - (gameWidth / 2 - dialogWidth / 2);
      int mouseY = super.mouseY - (gameHeight / 2 + 3 - dialogHeight / 2);
      if (mouseX >= 0 && mouseY >= 12 && mouseX < 408 && mouseY < 280) {
        int bankPageStartSlot = bankActivePage * 48;
        for (int row = 0; row < 6; row++) {
          for (int col = 0; col < 8; col++) {
            int x = 7 + col * 49;
            int y = 28 + row * 34;
            if (mouseX > x &&
                mouseX < x + 49 &&
                mouseY > y &&
                mouseY < y + 34 &&
                bankPageStartSlot < bankItemCount &&
                bankItems[bankPageStartSlot] != -1) {
              bankSelectedItem = bankItems[bankPageStartSlot];
              bankSelectedItemSlot = bankPageStartSlot;
            }
            bankPageStartSlot++;
          }

        }

        mouseX = gameWidth / 2 - dialogWidth / 2;
        mouseY = gameHeight / 2 + 3 - dialogHeight / 2;
        int slot;
        if (bankSelectedItemSlot < 0) {
          slot = -1;
        } else {
          slot = bankItems[bankSelectedItemSlot];
        }
        if (slot != -1) {
          int bankCount = bankItemsCount[bankSelectedItemSlot];
          if (GameData.itemStackable[slot] == 1 && bankCount > 1) {
            bankCount = 1;
          }
          if (bankCount >= 1 &&
              super.mouseX >= mouseX + 220 &&
              super.mouseY >= mouseY + 238 &&
              super.mouseX < mouseX + 250 &&
              super.mouseY <= mouseY + 249) {
            super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
            super.clientStream.putShort(slot);
            if (Version.CLIENT > 204) {
              super.clientStream.putInt(1);
            } else {
              super.clientStream.putShort(1);
            }
            super.clientStream.putInt(0x12345678);
            super.clientStream.sendPacket();
          }
          if (bankCount >= 5 &&
              super.mouseX >= mouseX + 250 &&
              super.mouseY >= mouseY + 238 &&
              super.mouseX < mouseX + 280 &&
              super.mouseY <= mouseY + 249) {
            super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
            super.clientStream.putShort(slot);
            if (Version.CLIENT > 204) {
              super.clientStream.putInt(5);
            } else {
              super.clientStream.putShort(5);
            }
            super.clientStream.putInt(0x12345678);
            super.clientStream.sendPacket();
          }
          if (Version.CLIENT > 204) {
            if (bankCount >= 10 &&
                super.mouseX >= mouseX + 280 &&
                super.mouseY >= mouseY + 238 &&
                super.mouseX < mouseX + 305 &&
                super.mouseY <= mouseY + 249) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
              super.clientStream.putShort(slot);
              super.clientStream.putInt(10);
              super.clientStream.putInt(0x12345678);
              super.clientStream.sendPacket();
            }
            if (bankCount >= 50 &&
                super.mouseX >= mouseX + 305 &&
                super.mouseY >= mouseY + 238 &&
                super.mouseX < mouseX + 335 &&
                super.mouseY <= mouseY + 249) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
              super.clientStream.putShort(slot);
              super.clientStream.putInt(50);
              super.clientStream.putInt(0x12345678);
              super.clientStream.sendPacket();
            }
            if (super.mouseX >= mouseX + 335 &&
                super.mouseY >= mouseY + 238 &&
                super.mouseX < mouseX + 368 &&
                super.mouseY <= mouseY + 249) {
              showInputPopup(3, new String[] {
                "Please enter the number of items to withdraw", "and press enter"
              }, true);
            }
            if (super.mouseX >= mouseX + 370 &&
                super.mouseY >= mouseY + 238 &&
                super.mouseX < mouseX + 400 &&
                super.mouseY <= mouseY + 249) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
              super.clientStream.putShort(slot);
              super.clientStream.putInt(bankCount);
              super.clientStream.putInt(0x12345678);
              super.clientStream.sendPacket();
            }
          } else {
            if (bankCount >= 25 &&
                super.mouseX >= mouseX + 280 &&
                super.mouseY >= mouseY + 238 &&
                super.mouseX < mouseX + 305 &&
                super.mouseY <= mouseY + 249) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
              super.clientStream.putShort(slot);
              if (Version.CLIENT > 204) {
                super.clientStream.putInt(25);
              } else {
                super.clientStream.putShort(25);
              }
              super.clientStream.putInt(0x12345678);
              super.clientStream.sendPacket();
            }
            if (bankCount >= 100 &&
                super.mouseX >= mouseX + 305 &&
                super.mouseY >= mouseY + 238 &&
                super.mouseX < mouseX + 335 &&
                super.mouseY <= mouseY + 249) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
              super.clientStream.putShort(slot);
              if (Version.CLIENT > 204) {
                super.clientStream.putInt(100);
              } else {
                super.clientStream.putShort(100);
              }
              super.clientStream.putInt(0x12345678);
              super.clientStream.sendPacket();
            }
            if (bankCount >= 500 &&
                super.mouseX >= mouseX + 335 &&
                super.mouseY >= mouseY + 238 &&
                super.mouseX < mouseX + 368 &&
                super.mouseY <= mouseY + 249) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
              super.clientStream.putShort(slot);
              if (Version.CLIENT > 204) {
                super.clientStream.putInt(500);
              } else {
                super.clientStream.putShort(500);
              }
              super.clientStream.putInt(0x12345678);
              super.clientStream.sendPacket();
            }
            if (bankCount >= 2500 &&
                super.mouseX >= mouseX + 370 &&
                super.mouseY >= mouseY + 238 &&
                super.mouseX < mouseX + 400 &&
                super.mouseY <= mouseY + 249) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_WITHDRAW));
              super.clientStream.putShort(slot);
              if (Version.CLIENT > 204) {
                super.clientStream.putInt(2500);
              } else {
                super.clientStream.putShort(2500);
              }
              super.clientStream.putInt(0x12345678);
              super.clientStream.sendPacket();
            }
          }
          if (getInventoryCount(slot) >= 1 &&
              super.mouseX >= mouseX + 220 &&
              super.mouseY >= mouseY + 263 &&
              super.mouseX < mouseX + 250 &&
              super.mouseY <= mouseY + 274) {
            super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
            super.clientStream.putShort(slot);
            if (Version.CLIENT > 204) {
              super.clientStream.putInt(1);
            } else {
              super.clientStream.putShort(1);
            }
            super.clientStream.putInt(-0x789abcdf);
            super.clientStream.sendPacket();
          }
          if (getInventoryCount(slot) >= 5 &&
              super.mouseX >= mouseX + 250 &&
              super.mouseY >= mouseY + 263 &&
              super.mouseX < mouseX + 280 &&
              super.mouseY <= mouseY + 274) {
            super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
            super.clientStream.putShort(slot);
            if (Version.CLIENT > 204) {
              super.clientStream.putInt(5);
            } else {
              super.clientStream.putShort(5);
            }
            super.clientStream.putInt(-0x789abcdf);
            super.clientStream.sendPacket();
          }
          if (Version.CLIENT > 204) {
            if (getInventoryCount(slot) >= 10 &&
                super.mouseX >= mouseX + 280 &&
                super.mouseY >= mouseY + 263 &&
                super.mouseX < mouseX + 305 &&
                super.mouseY <= mouseY + 274) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
              super.clientStream.putShort(slot);
              super.clientStream.putInt(10);
              super.clientStream.putInt(-0x789abcdf);
              super.clientStream.sendPacket();
            }
            if (getInventoryCount(slot) >= 50 &&
                super.mouseX >= mouseX + 305 &&
                super.mouseY >= mouseY + 263 &&
                super.mouseX < mouseX + 335 &&
                super.mouseY <= mouseY + 274) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
              super.clientStream.putShort(slot);
              super.clientStream.putInt(50);
              super.clientStream.putInt(-0x789abcdf);
              super.clientStream.sendPacket();
            }
            if (super.mouseX >= mouseX + 335 &&
                super.mouseY >= mouseY + 263 &&
                super.mouseX < mouseX + 368 &&
                super.mouseY <= mouseY + 274) {
              showInputPopup(4, new String[] {
                "Please enter the number of items to deposit", "and press enter"
              }, true);
            }
            if (super.mouseX >= mouseX + 370 &&
                super.mouseY >= mouseY + 263 &&
                super.mouseX < mouseX + 400 &&
                super.mouseY <= mouseY + 274) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
              super.clientStream.putShort(slot);
              super.clientStream.putInt(getInventoryCount(slot));
              super.clientStream.putInt(-0x789abcdf);
              super.clientStream.sendPacket();
            }
          } else {
            if (getInventoryCount(slot) >= 25 &&
                super.mouseX >= mouseX + 280 &&
                super.mouseY >= mouseY + 263 &&
                super.mouseX < mouseX + 305 &&
                super.mouseY <= mouseY + 274) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
              super.clientStream.putShort(slot);
              if (Version.CLIENT > 204) {
                super.clientStream.putInt(25);
              } else {
                super.clientStream.putShort(25);
              }
              super.clientStream.putInt(-0x789abcdf);
              super.clientStream.sendPacket();
            }
            if (getInventoryCount(slot) >= 100 &&
                super.mouseX >= mouseX + 305 &&
                super.mouseY >= mouseY + 263 &&
                super.mouseX < mouseX + 335 &&
                super.mouseY <= mouseY + 274) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
              super.clientStream.putShort(slot);
              if (Version.CLIENT > 204) {
                super.clientStream.putInt(100);
              } else {
                super.clientStream.putShort(100);
              }
              super.clientStream.putInt(-0x789abcdf);
              super.clientStream.sendPacket();
            }
            if (getInventoryCount(slot) >= 500 &&
                super.mouseX >= mouseX + 335 &&
                super.mouseY >= mouseY + 263 &&
                super.mouseX < mouseX + 368 &&
                super.mouseY <= mouseY + 274) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
              super.clientStream.putShort(slot);
              if (Version.CLIENT > 204) {
                super.clientStream.putInt(500);
              } else {
                super.clientStream.putShort(500);
              }
              super.clientStream.putInt(-0x789abcdf);
              super.clientStream.sendPacket();
            }
            if (getInventoryCount(slot) >= 2500 &&
                super.mouseX >= mouseX + 370 &&
                super.mouseY >= mouseY + 263 &&
                super.mouseX < mouseX + 400 &&
                super.mouseY <= mouseY + 274) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_DEPOSIT));
              super.clientStream.putShort(slot);
              if (Version.CLIENT > 204) {
                super.clientStream.putInt(2500);
              } else {
                super.clientStream.putShort(2500);
              }
              super.clientStream.putInt(-0x789abcdf);
              super.clientStream.sendPacket();
            }
          }
        }
      } else if (bankItemCount > 48 && mouseX >= 50 && mouseX <= 115 && mouseY <= 12) {
        bankActivePage = 0;
      } else if (bankItemCount > 48 && mouseX >= 115 && mouseX <= 180 && mouseY <= 12) {
        bankActivePage = 1;
      } else if (bankItemCount > 96 && mouseX >= 180 && mouseX <= 245 && mouseY <= 12) {
        bankActivePage = 2;
      } else if (bankItemCount > 144 && mouseX >= 245 && mouseX <= 310 && mouseY <= 12) {
        bankActivePage = 3;
      } else {
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_BANK_CLOSE));
        super.clientStream.sendPacket();
        showDialogBank = false;
        return;
      }
    }
    int dialogX = gameWidth / 2 - dialogWidth / 2;
    int dialogY = gameHeight / 2 + 3 - dialogHeight / 2;
    surface.drawBox(dialogX, dialogY, 408, 12, 192);
    surface.drawBoxAlpha(dialogX, dialogY + 12, 408, 17, 0x989898, 160);
    surface.drawBoxAlpha(dialogX, dialogY + 29, 8, 204, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 399, dialogY + 29, 9, 204, 0x989898, 160);
    surface.drawBoxAlpha(dialogX, dialogY + 233, 408, 47, 0x989898, 160);
    surface.drawstring("Bank", dialogX + 1, dialogY + 10, 1, 0xffffff);
    int xOff = 50;
    if (bankItemCount > 48) {
      int colour = 0xffffff;
      if (bankActivePage == 0) {
        colour = 0xff0000;
      } else if (super.mouseX > dialogX + xOff &&
                 super.mouseY >= dialogY &&
                 super.mouseX < dialogX + xOff + 65 &&
                 super.mouseY < dialogY + 12) {
        colour = 0xffff00;
      }
      surface.drawstring("<page 1>", dialogX + xOff, dialogY + 10, 1, colour);
      xOff += 65;
      colour = 0xffffff;
      if (bankActivePage == 1) {
        colour = 0xff0000;
      } else if (super.mouseX > dialogX + xOff &&
                 super.mouseY >= dialogY &&
                 super.mouseX < dialogX + xOff + 65 &&
                 super.mouseY < dialogY + 12) {
        colour = 0xffff00;
      }
      surface.drawstring("<page 2>", dialogX + xOff, dialogY + 10, 1, colour);
      xOff += 65;
    }
    if (bankItemCount > 96) {
      int colour = 0xffffff;
      if (bankActivePage == 2) {
        colour = 0xff0000;
      } else if (super.mouseX > dialogX + xOff &&
                 super.mouseY >= dialogY &&
                 super.mouseX < dialogX + xOff + 65 &&
                 super.mouseY < dialogY + 12) {
        colour = 0xffff00;
      }
      surface.drawstring("<page 3>", dialogX + xOff, dialogY + 10, 1, colour);
      xOff += 65;
    }
    if (bankItemCount > 144) {
      int colour = 0xffffff;
      if (bankActivePage == 3) {
        colour = 0xff0000;
      } else if (super.mouseX > dialogX + xOff &&
                 super.mouseY >= dialogY &&
                 super.mouseX < dialogX + xOff + 65 &&
                 super.mouseY < dialogY + 12) {
        colour = 0xffff00;
      }
      surface.drawstring("<page 4>", dialogX + xOff, dialogY + 10, 1, colour);
      xOff += 65;
    }
    int colour = 0xffffff;
    if (super.mouseX > dialogX + 320 &&
        super.mouseY >= dialogY &&
        super.mouseX < dialogX + 408 &&
        super.mouseY < dialogY + 12) {
      colour = 0xff0000;
    }
    surface.drawstringRight("Close window", dialogX + 406, dialogY + 10, 1, colour);
    surface.drawstring("Number in bank in green", dialogX + 7, dialogY + 24, 1, 65280);
    surface.drawstring("Number held in blue", dialogX + 289, dialogY + 24, 1, 65535);
    int bankPageStartSlot = bankActivePage * 48;
    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < 8; col++) {
        int x = dialogX + 7 + col * 49;
        int y = dialogY + 28 + row * 34;
        if (bankSelectedItemSlot == bankPageStartSlot) {
          surface.drawBoxAlpha(x, y, 49, 34, 0xff0000, 160);
        } else {
          surface.drawBoxAlpha(x, y, 49, 34, 0xd0d0d0, 160);
        }
        surface.drawBoxEdge(x, y, 50, 35, 0);
        if (bankPageStartSlot < bankItemCount && bankItems[bankPageStartSlot] != -1) {
          surface.spriteClipping(x,
                                 y,
                                 48,
                                 32,
                                 spriteItem + GameData.itemPicture[bankItems[bankPageStartSlot]],
                                 GameData.itemMask[bankItems[bankPageStartSlot]],
                                 0,
                                 0,
                                 false
          );
          surface.drawstring(String.valueOf(bankItemsCount[bankPageStartSlot]), x + 1, y + 10, 1, 65280);
          surface.drawstringRight(String.valueOf(getInventoryCount(bankItems[bankPageStartSlot])),
                                  x + 47,
                                  y + 29,
                                  1,
                                  65535
          );
        }
        bankPageStartSlot++;
      }

    }

    surface.drawLineHoriz(dialogX + 5, dialogY + 256, 398, 0);
    if (bankSelectedItemSlot == -1) {
      surface.drawStringCenter("Select an object to withdraw or deposit", dialogX + 204, dialogY + 248, 3, 0xffff00);
      return;
    }
    int itemType;
    if (bankSelectedItemSlot < 0) {
      itemType = -1;
    } else {
      itemType = bankItems[bankSelectedItemSlot];
    }
    if (itemType != -1) {
      int itemCount = bankItemsCount[bankSelectedItemSlot];
      if (GameData.itemStackable[itemType] == 1 && itemCount > 1) {
        itemCount = 1;
      }
      if (itemCount > 0) {
        surface.drawstring("Withdraw " + GameData.itemName[itemType], dialogX + 2, dialogY + 248, 1, 0xffffff);
        colour = 0xffffff;
        if (super.mouseX >= dialogX + 220 &&
            super.mouseY >= dialogY + 238 &&
            super.mouseX < dialogX + 250 &&
            super.mouseY <= dialogY + 249) {
          colour = 0xff0000;
        }
        surface.drawstring("One", dialogX + 222, dialogY + 248, 1, colour);
        if (itemCount >= 5) {
          colour = 0xffffff;
          if (super.mouseX >= dialogX + 250 &&
              super.mouseY >= dialogY + 238 &&
              super.mouseX < dialogX + 280 &&
              super.mouseY <= dialogY + 249) {
            colour = 0xff0000;
          }
          surface.drawstring("Five", dialogX + 252, dialogY + 248, 1, colour);
        }
        if (Version.CLIENT > 204) {
          if (itemCount >= 10) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 280 &&
                super.mouseY >= dialogY + 238 &&
                super.mouseX < dialogX + 305 &&
                super.mouseY <= dialogY + 249) {
              colour = 0xff0000;
            }
            surface.drawstring("10", dialogX + 282, dialogY + 248, 1, colour);
          }
          if (itemCount >= 50) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 305 &&
                super.mouseY >= dialogY + 238 &&
                super.mouseX < dialogX + 335 &&
                super.mouseY <= dialogY + 249) {
              colour = 0xff0000;
            }
            surface.drawstring("50", dialogX + 307, dialogY + 248, 1, colour);
          }

          colour = 0xffffff;
          if (super.mouseX >= dialogX + 335 &&
              super.mouseY >= dialogY + 238 &&
              super.mouseX < dialogX + 368 &&
              super.mouseY <= dialogY + 249) {
            colour = 0xff0000;
          }
          surface.drawstring("X", dialogX + 337, dialogY + 248, 1, colour);

          colour = 0xffffff;
          if (super.mouseX >= dialogX + 370 &&
              super.mouseY >= dialogY + 238 &&
              super.mouseX < dialogX + 400 &&
              super.mouseY <= dialogY + 249) {
            colour = 0xff0000;
          }
          surface.drawstring("All", dialogX + 370, dialogY + 248, 1, colour);
        } else {
          if (itemCount >= 25) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 280 &&
                super.mouseY >= dialogY + 238 &&
                super.mouseX < dialogX + 305 &&
                super.mouseY <= dialogY + 249) {
              colour = 0xff0000;
            }
            surface.drawstring("25", dialogX + 282, dialogY + 248, 1, colour);
          }
          if (itemCount >= 100) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 305 &&
                super.mouseY >= dialogY + 238 &&
                super.mouseX < dialogX + 335 &&
                super.mouseY <= dialogY + 249) {
              colour = 0xff0000;
            }
            surface.drawstring("100", dialogX + 307, dialogY + 248, 1, colour);
          }
          if (itemCount >= 500) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 335 &&
                super.mouseY >= dialogY + 238 &&
                super.mouseX < dialogX + 368 &&
                super.mouseY <= dialogY + 249) {
              colour = 0xff0000;
            }
            surface.drawstring("500", dialogX + 337, dialogY + 248, 1, colour);
          }
          if (itemCount >= 2500) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 370 &&
                super.mouseY >= dialogY + 238 &&
                super.mouseX < dialogX + 400 &&
                super.mouseY <= dialogY + 249) {
              colour = 0xff0000;
            }
            surface.drawstring("2500", dialogX + 370, dialogY + 248, 1, colour);
          }
        }
      }
      if (getInventoryCount(itemType) > 0) {
        surface.drawstring("Deposit " + GameData.itemName[itemType], dialogX + 2, dialogY + 273, 1, 0xffffff);
        colour = 0xffffff;
        if (super.mouseX >= dialogX + 220 &&
            super.mouseY >= dialogY + 263 &&
            super.mouseX < dialogX + 250 &&
            super.mouseY <= dialogY + 274) {
          colour = 0xff0000;
        }
        surface.drawstring("One", dialogX + 222, dialogY + 273, 1, colour);
        if (getInventoryCount(itemType) >= 5) {
          colour = 0xffffff;
          if (super.mouseX >= dialogX + 250 &&
              super.mouseY >= dialogY + 263 &&
              super.mouseX < dialogX + 280 &&
              super.mouseY <= dialogY + 274) {
            colour = 0xff0000;
          }
          surface.drawstring("Five", dialogX + 252, dialogY + 273, 1, colour);
        }
        if (Version.CLIENT > 204) {
          if (getInventoryCount(itemType) >= 10) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 280 &&
                super.mouseY >= dialogY + 263 &&
                super.mouseX < dialogX + 305 &&
                super.mouseY <= dialogY + 274) {
              colour = 0xff0000;
            }
            surface.drawstring("10", dialogX + 282, dialogY + 273, 1, colour);
          }
          if (getInventoryCount(itemType) >= 50) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 305 &&
                super.mouseY >= dialogY + 263 &&
                super.mouseX < dialogX + 335 &&
                super.mouseY <= dialogY + 274) {
              colour = 0xff0000;
            }
            surface.drawstring("50", dialogX + 307, dialogY + 273, 1, colour);
          }

          colour = 0xffffff;
          if (super.mouseX >= dialogX + 335 &&
              super.mouseY >= dialogY + 263 &&
              super.mouseX < dialogX + 368 &&
              super.mouseY <= dialogY + 274) {
            colour = 0xff0000;
          }
          surface.drawstring("X", dialogX + 337, dialogY + 273, 1, colour);

          colour = 0xffffff;
          if (super.mouseX >= dialogX + 370 &&
              super.mouseY >= dialogY + 263 &&
              super.mouseX < dialogX + 400 &&
              super.mouseY <= dialogY + 274) {
            colour = 0xff0000;
          }
          surface.drawstring("All", dialogX + 370, dialogY + 273, 1, colour);
        } else {
          if (getInventoryCount(itemType) >= 25) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 280 &&
                super.mouseY >= dialogY + 263 &&
                super.mouseX < dialogX + 305 &&
                super.mouseY <= dialogY + 274) {
              colour = 0xff0000;
            }
            surface.drawstring("25", dialogX + 282, dialogY + 273, 1, colour);
          }
          if (getInventoryCount(itemType) >= 100) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 305 &&
                super.mouseY >= dialogY + 263 &&
                super.mouseX < dialogX + 335 &&
                super.mouseY <= dialogY + 274) {
              colour = 0xff0000;
            }
            surface.drawstring("100", dialogX + 307, dialogY + 273, 1, colour);
          }
          if (getInventoryCount(itemType) >= 500) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 335 &&
                super.mouseY >= dialogY + 263 &&
                super.mouseX < dialogX + 368 &&
                super.mouseY <= dialogY + 274) {
              colour = 0xff0000;
            }
            surface.drawstring("500", dialogX + 337, dialogY + 273, 1, colour);
          }
          if (getInventoryCount(itemType) >= 2500) {
            colour = 0xffffff;
            if (super.mouseX >= dialogX + 370 &&
                super.mouseY >= dialogY + 263 &&
                super.mouseX < dialogX + 400 &&
                super.mouseY <= dialogY + 274) {
              colour = 0xff0000;
            }
            surface.drawstring("2500", dialogX + 370, dialogY + 273, 1, colour);
          }
        }
      }
    }
  }

  private void drawDialogDuel() {
    if (mouseButtonClick != 0 && mouseButtonItemCountIncrement == 0) {
      mouseButtonItemCountIncrement = 1;
    }
    if (mouseButtonItemCountIncrement > 0) {
      int mouseX = super.mouseX - (gameWidth / 2 - 468 / 2);
      int mouseY = super.mouseY - (gameHeight / 2 - 262 / 2);
      //int mouseX = super.mouseX - 22;
      //int mouseY = super.mouseY - 36;
      if (mouseX >= 0 && mouseY >= 0 && mouseX < 468 && mouseY < 262) {
        if (mouseX > 216 && mouseY > 30 && mouseX < 462 && mouseY < 235) {
          int slot = (mouseX - 217) / 49 + ((mouseY - 31) / 34) * 5;
          if (slot >= 0 && slot < inventoryItemsCount) {
            boolean sendUpdate = false;
            int l1 = 0;
            int item = inventoryItemId[slot];
            for (int k3 = 0; k3 < duelOfferItemCount; k3++) {
              if (duelOfferItemId[k3] == item) {
                if (GameData.itemStackable[item] == 0) {
                  for (int i4 = 0; i4 < mouseButtonItemCountIncrement; i4++) {
                    if (duelOfferItemStack[k3] < inventoryItemStackCount[slot]) {
                      duelOfferItemStack[k3]++;
                    }
                    sendUpdate = true;
                  }

                } else {
                  l1++;
                }
              }
            }

            if (getInventoryCount(item) <= l1) {
              sendUpdate = true;
            }
            if (GameData.itemSpecial[item] == 1) {
              showMessage("This object cannot be added to a duel offer", 3);
              sendUpdate = true;
            }
            if (!sendUpdate && duelOfferItemCount < 8) {
              duelOfferItemId[duelOfferItemCount] = item;
              duelOfferItemStack[duelOfferItemCount] = 1;
              duelOfferItemCount++;
              sendUpdate = true;
            }
            if (sendUpdate) {
              super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_DUEL_ITEM_UPDATE));
              super.clientStream.putByte(duelOfferItemCount);
              for (int j4 = 0; j4 < duelOfferItemCount; j4++) {
                super.clientStream.putShort(duelOfferItemId[j4]);
                super.clientStream.putInt(duelOfferItemStack[j4]);
              }

              super.clientStream.sendPacket();
              duelOfferOpponentAccepted = false;
              duelOfferAccepted = false;
            }
          }
        }
        if (mouseX > 8 && mouseY > 30 && mouseX < 205 && mouseY < 129) {
          int slot = (mouseX - 9) / 49 + ((mouseY - 31) / 34) * 4;
          if (slot >= 0 && slot < duelOfferItemCount) {
            int j1 = duelOfferItemId[slot];
            for (int i2 = 0; i2 < mouseButtonItemCountIncrement; i2++) {
              if (GameData.itemStackable[j1] == 0 && duelOfferItemStack[slot] > 1) {
                duelOfferItemStack[slot]--;
                continue;
              }
              duelOfferItemCount--;
              mouseButtonDownTime = 0;
              for (int l2 = slot; l2 < duelOfferItemCount; l2++) {
                duelOfferItemId[l2] = duelOfferItemId[l2 + 1];
                duelOfferItemStack[l2] = duelOfferItemStack[l2 + 1];
              }

              break;
            }

            super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_DUEL_ITEM_UPDATE));
            super.clientStream.putByte(duelOfferItemCount);
            for (int i3 = 0; i3 < duelOfferItemCount; i3++) {
              super.clientStream.putShort(duelOfferItemId[i3]);
              super.clientStream.putInt(duelOfferItemStack[i3]);
            }

            super.clientStream.sendPacket();
            duelOfferOpponentAccepted = false;
            duelOfferAccepted = false;
          }
        }
        boolean flag = false;
        if (mouseX >= 93 && mouseY >= 221 && mouseX <= 104 && mouseY <= 232) {
          duelSettingsRetreat = !duelSettingsRetreat;
          flag = true;
        }
        if (mouseX >= 93 && mouseY >= 240 && mouseX <= 104 && mouseY <= 251) {
          duelSettingsMagic = !duelSettingsMagic;
          flag = true;
        }
        if (mouseX >= 191 && mouseY >= 221 && mouseX <= 202 && mouseY <= 232) {
          duelSettingsPrayer = !duelSettingsPrayer;
          flag = true;
        }
        if (mouseX >= 191 && mouseY >= 240 && mouseX <= 202 && mouseY <= 251) {
          duelSettingsWeapons = !duelSettingsWeapons;
          flag = true;
        }
        if (flag) {
          super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_DUEL_SETTINGS));
          super.clientStream.putByte(duelSettingsRetreat ? 1 : 0);
          super.clientStream.putByte(duelSettingsMagic ? 1 : 0);
          super.clientStream.putByte(duelSettingsPrayer ? 1 : 0);
          super.clientStream.putByte(duelSettingsWeapons ? 1 : 0);
          super.clientStream.sendPacket();
          duelOfferOpponentAccepted = false;
          duelOfferAccepted = false;
        }
        if (mouseX >= 217 && mouseY >= 238 && mouseX <= 286 && mouseY <= 259) {
          duelOfferAccepted = true;
          super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_DUEL_ACCEPT));
          super.clientStream.sendPacket();
        }
        if (mouseX >= 394 && mouseY >= 238 && mouseX < 463 && mouseY < 259) {
          showDialogDuel = false;
          super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_DUEL_DECLINE));
          super.clientStream.sendPacket();
        }
      } else if (mouseButtonClick != 0) {
        showDialogDuel = false;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_DUEL_DECLINE));
        super.clientStream.sendPacket();
      }
      mouseButtonClick = 0;
      mouseButtonItemCountIncrement = 0;
    }
    if (!showDialogDuel) {
      return;
    }
    int dialogX = gameWidth / 2 - 468 / 2 + 22;
    int dialogY = gameHeight / 2 - 262 / 2 + 22;
    //int dialogX = 22;
    //int dialogY = 36;
    surface.drawBox(dialogX, dialogY, 468, 12, 0xc90b1d);
    surface.drawBoxAlpha(dialogX, dialogY + 12, 468, 18, 0x989898, 160);
    surface.drawBoxAlpha(dialogX, dialogY + 30, 8, 248, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 205, dialogY + 30, 11, 248, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 462, dialogY + 30, 6, 248, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 99, 197, 24, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 192, 197, 23, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 258, 197, 20, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 216, dialogY + 235, 246, 43, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 30, 197, 69, 0xd0d0d0, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 123, 197, 69, 0xd0d0d0, 160);
    surface.drawBoxAlpha(dialogX + 8, dialogY + 215, 197, 43, 0xd0d0d0, 160);
    surface.drawBoxAlpha(dialogX + 216, dialogY + 30, 246, 205, 0xd0d0d0, 160);
    for (int j2 = 0; j2 < 3; j2++) {
      surface.drawLineHoriz(dialogX + 8, dialogY + 30 + j2 * 34, 197, 0);
    }

    for (int j3 = 0; j3 < 3; j3++) {
      surface.drawLineHoriz(dialogX + 8, dialogY + 123 + j3 * 34, 197, 0);
    }

    for (int l3 = 0; l3 < 7; l3++) {
      surface.drawLineHoriz(dialogX + 216, dialogY + 30 + l3 * 34, 246, 0);
    }

    for (int k4 = 0; k4 < 6; k4++) {
      if (k4 < 5) {
        surface.drawLineVert(dialogX + 8 + k4 * 49, dialogY + 30, 69, 0);
      }
      if (k4 < 5) {
        surface.drawLineVert(dialogX + 8 + k4 * 49, dialogY + 123, 69, 0);
      }
      surface.drawLineVert(dialogX + 216 + k4 * 49, dialogY + 30, 205, 0);
    }

    surface.drawLineHoriz(dialogX + 8, dialogY + 215, 197, 0);
    surface.drawLineHoriz(dialogX + 8, dialogY + 257, 197, 0);
    surface.drawLineVert(dialogX + 8, dialogY + 215, 43, 0);
    surface.drawLineVert(dialogX + 204, dialogY + 215, 43, 0);
    surface.drawstring("Preparing to duel with: " + duelOpponentName, dialogX + 1, dialogY + 10, 1, 0xffffff);
    surface.drawstring("Your Stake", dialogX + 9, dialogY + 27, 4, 0xffffff);
    surface.drawstring("Opponent's Stake", dialogX + 9, dialogY + 120, 4, 0xffffff);
    surface.drawstring("Duel Options", dialogX + 9, dialogY + 212, 4, 0xffffff);
    surface.drawstring("Your Inventory", dialogX + 216, dialogY + 27, 4, 0xffffff);
    surface.drawstring("No retreating", dialogX + 8 + 1, dialogY + 215 + 16, 3, 0xffff00);
    surface.drawstring("No magic", dialogX + 8 + 1, dialogY + 215 + 35, 3, 0xffff00);
    surface.drawstring("No prayer", dialogX + 8 + 102, dialogY + 215 + 16, 3, 0xffff00);
    surface.drawstring("No weapons", dialogX + 8 + 102, dialogY + 215 + 35, 3, 0xffff00);
    surface.drawBoxEdge(dialogX + 93, dialogY + 215 + 6, 11, 11, 0xffff00);
    if (duelSettingsRetreat) {
      surface.drawBox(dialogX + 95, dialogY + 215 + 8, 7, 7, 0xffff00);
    }
    surface.drawBoxEdge(dialogX + 93, dialogY + 215 + 25, 11, 11, 0xffff00);
    if (duelSettingsMagic) {
      surface.drawBox(dialogX + 95, dialogY + 215 + 27, 7, 7, 0xffff00);
    }
    surface.drawBoxEdge(dialogX + 191, dialogY + 215 + 6, 11, 11, 0xffff00);
    if (duelSettingsPrayer) {
      surface.drawBox(dialogX + 193, dialogY + 215 + 8, 7, 7, 0xffff00);
    }
    surface.drawBoxEdge(dialogX + 191, dialogY + 215 + 25, 11, 11, 0xffff00);
    if (duelSettingsWeapons) {
      surface.drawBox(dialogX + 193, dialogY + 215 + 27, 7, 7, 0xffff00);
    }
    if (!duelOfferAccepted) {
      surface.drawSprite(dialogX + 217, dialogY + 238, spriteMedia + 25);
    }
    surface.drawSprite(dialogX + 394, dialogY + 238, spriteMedia + 26);
    if (duelOfferOpponentAccepted) {
      surface.drawStringCenter("Other player", dialogX + 341, dialogY + 246, 1, 0xffffff);
      surface.drawStringCenter("has accepted", dialogX + 341, dialogY + 256, 1, 0xffffff);
    }
    if (duelOfferAccepted) {
      surface.drawStringCenter("Waiting for", dialogX + 217 + 35, dialogY + 246, 1, 0xffffff);
      surface.drawStringCenter("other player", dialogX + 217 + 35, dialogY + 256, 1, 0xffffff);
    }
    for (int i = 0; i < inventoryItemsCount; i++) {
      int x = 217 + dialogX + (i % 5) * 49;
      int y = 31 + dialogY + (i / 5) * 34;
      surface.spriteClipping(x,
                             y,
                             48,
                             32,
                             spriteItem + GameData.itemPicture[inventoryItemId[i]],
                             GameData.itemMask[inventoryItemId[i]],
                             0,
                             0,
                             false
      );
      if (GameData.itemStackable[inventoryItemId[i]] == 0) {
        surface.drawstring(String.valueOf(inventoryItemStackCount[i]), x + 1, y + 10, 1, 0xffff00);
      }
    }

    for (int i = 0; i < duelOfferItemCount; i++) {
      int x = 9 + dialogX + (i % 4) * 49;
      int y = 31 + dialogY + (i / 4) * 34;
      surface.spriteClipping(x,
                             y,
                             48,
                             32,
                             spriteItem + GameData.itemPicture[duelOfferItemId[i]],
                             GameData.itemMask[duelOfferItemId[i]],
                             0,
                             0,
                             false
      );
      if (GameData.itemStackable[duelOfferItemId[i]] == 0) {
        surface.drawstring(String.valueOf(duelOfferItemStack[i]), x + 1, y + 10, 1, 0xffff00);
      }
      if (super.mouseX > x && super.mouseX < x + 48 && super.mouseY > y && super.mouseY < y + 32) {
        surface.drawstring(GameData.itemName[duelOfferItemId[i]] +
                           ": @whi@" +
                           GameData.itemDescription[duelOfferItemId[i]], dialogX + 8, dialogY + 273, 1, 0xffff00);
      }
    }

    for (int i = 0; i < duelOfferOpponentItemCount; i++) {
      int x = 9 + dialogX + (i % 4) * 49;
      int y = 124 + dialogY + (i / 4) * 34;
      surface.spriteClipping(x,
                             y,
                             48,
                             32,
                             spriteItem + GameData.itemPicture[duelOfferOpponentItemId[i]],
                             GameData.itemMask[duelOfferOpponentItemId[i]],
                             0,
                             0,
                             false
      );
      if (GameData.itemStackable[duelOfferOpponentItemId[i]] == 0) {
        surface.drawstring(String.valueOf(duelOfferOpponentItemStack[i]), x + 1, y + 10, 1, 0xffff00);
      }
      if (super.mouseX > x && super.mouseX < x + 48 && super.mouseY > y && super.mouseY < y + 32) {
        surface.drawstring(GameData.itemName[duelOfferOpponentItemId[i]] +
                           ": @whi@" +
                           GameData.itemDescription[duelOfferOpponentItemId[i]],
                           dialogX + 8,
                           dialogY + 273,
                           1,
                           0xffff00
        );
      }
    }

  }

  private boolean loadNextRegion(int lx, int ly) {
    if (deathScreenTimeout != 0) {
      world.playerAlive = false;
      return false;
    }
    loadingArea = false;
    lx += planeWidth;
    ly += planeHeight;
    if (lastHeightOffset == planeIndex &&
        lx > localLowerX &&
        lx < localUpperX &&
        ly > localLowerY &&
        ly < localUpperY) {
      world.playerAlive = true;
      return false;
    }
    surface.drawStringCenter("Loading... Please wait", gameWidth / 2, gameHeight / 2 + 25, 1, 0xffffff);
    drawChatMessageTabs();
    surface.draw(graphics, 0, 0);
    int ax = regionX;
    int ay = regionY;
    int sectionX = (lx + 24) / 48;
    int sectionY = (ly + 24) / 48;
    lastHeightOffset = planeIndex;
    regionX = sectionX * 48 - 48;
    regionY = sectionY * 48 - 48;
    localLowerX = sectionX * 48 - 32;
    localLowerY = sectionY * 48 - 32;
    localUpperX = sectionX * 48 + 32;
    localUpperY = sectionY * 48 + 32;
    world.loadSection(lx, ly, lastHeightOffset);
    regionX -= planeWidth;
    regionY -= planeHeight;
    int offsetx = regionX - ax;
    int offsety = regionY - ay;
    for (int objidx = 0; objidx < objectCount; objidx++) {
      objectX[objidx] -= offsetx;
      objectY[objidx] -= offsety;
      int objx = objectX[objidx];
      int objy = objectY[objidx];
      int objid = objectId[objidx];
      GameModel gameModel = objectModel[objidx];
      try {
        int objtype = objectDirection[objidx];
        int objw;
        int objh;
        if (objtype == 0 || objtype == 4) {
          objw = GameData.objectWidth[objid];
          objh = GameData.objectHeight[objid];
        } else {
          objh = GameData.objectWidth[objid];
          objw = GameData.objectHeight[objid];
        }
        int j6 = ((objx + objx + objw) * magicLoc) / 2;
        int k6 = ((objy + objy + objh) * magicLoc) / 2;
        if (objx >= 0 && objy >= 0 && objx < 96 && objy < 96) {
          scene.addModel(gameModel);
          gameModel.place(j6, -world.getElevation(j6, k6), k6);
          world.removeObject2(objx, objy, objid);
          if (objid == 74) {
            gameModel.translate(0, -480, 0);
          }
        }
      } catch (RuntimeException runtimeexception) {
        System.out.println("Loc Error: " + runtimeexception.getMessage());
        System.out.println("i:" + objidx + " obj:" + gameModel);
        runtimeexception.printStackTrace();
      }
    }

    for (int k2 = 0; k2 < wallObjectCount; k2++) {
      wallObjectX[k2] -= offsetx;
      wallObjectY[k2] -= offsety;
      int i3 = wallObjectX[k2];
      int l3 = wallObjectY[k2];
      int j4 = wallObjectId[k2];
      int i5 = wallObjectDirection[k2];
      try {
        world.setObjectAdjacency(i3, l3, i5, j4);
        GameModel gameModel_1 = createModel(i3, l3, i5, j4, k2);
        wallObjectModel[k2] = gameModel_1;
      } catch (RuntimeException runtimeexception1) {
        System.out.println("Bound Error: " + runtimeexception1.getMessage());
        runtimeexception1.printStackTrace();
      }
    }

    for (int j3 = 0; j3 < groundItemCount; j3++) {
      groundItemX[j3] -= offsetx;
      groundItemY[j3] -= offsety;
    }

    for (int i4 = 0; i4 < playerCount; i4++) {
      GameCharacter character = players[i4];
      character.currentX -= offsetx * magicLoc;
      character.currentY -= offsety * magicLoc;
      for (int j5 = 0; j5 <= character.waypointCurrent; j5++) {
        character.waypointsX[j5] -= offsetx * magicLoc;
        character.waypointsY[j5] -= offsety * magicLoc;
      }

    }

    for (int k4 = 0; k4 < npcCount; k4++) {
      GameCharacter character_1 = npcs[k4];
      character_1.currentX -= offsetx * magicLoc;
      character_1.currentY -= offsety * magicLoc;
      for (int l5 = 0; l5 <= character_1.waypointCurrent; l5++) {
        character_1.waypointsX[l5] -= offsetx * magicLoc;
        character_1.waypointsY[l5] -= offsety * magicLoc;
      }

    }

    world.playerAlive = true;
    return true;
  }

  void drawPlayer(int x, int y, int w, int h, int id, int tx, int ty) {
    GameCharacter character = players[id];
    if (character.colourBottom == 255) // this means the character is invisible! MOD!!!
    {
      return;
    }
    int l1 = character.animationCurrent + (cameraRotation + 16) / 32 & 7;
    boolean flag = false;
    int i2 = l1;
    if (i2 == 5) {
      i2 = 3;
      flag = true;
    } else if (i2 == 6) {
      i2 = 2;
      flag = true;
    } else if (i2 == 7) {
      i2 = 1;
      flag = true;
    }
    int j2 = i2 * 3 + npcWalkModel[(character.stepCount / 6) % 4];
    if (character.animationCurrent == 8) {
      i2 = 5;
      l1 = 2;
      flag = false;
      x -= (5 * ty) / 100;
      j2 = i2 * 3 + npcCombatModelArray1[(loginTimer / 5) % 8];
    } else if (character.animationCurrent == 9) {
      i2 = 5;
      l1 = 2;
      flag = true;
      x += (5 * ty) / 100;
      j2 = i2 * 3 + npcCombatModelArray2[(loginTimer / 6) % 8];
    }
    for (int k2 = 0; k2 < 12; k2++) {
      int l2 = npcAnimationArray[l1][k2];
      int l3 = character.equippedItem[l2] - 1;
      if (l3 >= 0) {
        int k4 = 0;
        int i5 = 0;
        int j5 = j2;
        if (flag && i2 >= 1 && i2 <= 3) {
          if (GameData.animationHasF[l3] == 1) {
            j5 += 15;
          } else if (l2 == 4 && i2 == 1) {
            k4 = -22;
            i5 = -3;
            j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
          } else if (l2 == 4 && i2 == 2) {
            k4 = 0;
            i5 = -8;
            j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
          } else if (l2 == 4 && i2 == 3) {
            k4 = 26;
            i5 = -5;
            j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
          } else if (l2 == 3 && i2 == 1) {
            k4 = 22;
            i5 = 3;
            j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
          } else if (l2 == 3 && i2 == 2) {
            k4 = 0;
            i5 = 8;
            j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
          } else if (l2 == 3 && i2 == 3) {
            k4 = -26;
            i5 = 5;
            j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
          }
        }
        if (i2 != 5 || GameData.animationHasA[l3] == 1) {
          int k5 = j5 + GameData.animationNumber[l3];
          k4 = (k4 * w) / surface.spriteWidthFull[k5];
          i5 = (i5 * h) / surface.spriteHeightFull[k5];
          int l5 = (w * surface.spriteWidthFull[k5]) / surface.spriteWidthFull[GameData.animationNumber[l3]];
          k4 -= (l5 - w) / 2;
          int i6 = GameData.animationCharacterColour[l3];
          int j6 = characterSkinColours[character.colourSkin];
          if (i6 == 1) {
            i6 = characterHairColours[character.colourHair];
          } else if (i6 == 2) {
            i6 = characterTopBottomColours[character.colourTop];
          } else if (i6 == 3) {
            i6 = characterTopBottomColours[character.colourBottom];
          }
          surface.spriteClipping(x + k4, y + i5, l5, h, k5, i6, j6, tx, flag);
        }
      }
    }

    if (character.messageTimeout > 0) {
      addReceivedMessage(x, y, w, character.message);
    }
    if (character.bubbleTimeout > 0) {
      actionBubbleX[itemsAboveHeadCount] = x + w / 2;
      actionBubbleY[itemsAboveHeadCount] = y;
      actionBubbleScale[itemsAboveHeadCount] = ty;
      actionBubbleItem[itemsAboveHeadCount++] = character.bubbleItem;
    }
    if (character.animationCurrent == 8 || character.animationCurrent == 9 || character.combatTimer != 0) {
      if (character.combatTimer > 0) {
        int i3 = x;
        if (character.animationCurrent == 8) {
          i3 -= (20 * ty) / 100;
        } else if (character.animationCurrent == 9) {
          i3 += (20 * ty) / 100;
        }
        int i4 = (character.healthCurrent * 30) / character.healthMax;
        healthBarX[healthBarCount] = i3 + w / 2;
        healthBarY[healthBarCount] = y;
        healthBarMissing[healthBarCount++] = i4;
      }
      if (character.combatTimer > 150) {
        int j3 = x;
        if (character.animationCurrent == 8) {
          j3 -= (10 * ty) / 100;
        } else if (character.animationCurrent == 9) {
          j3 += (10 * ty) / 100;
        }
        surface.drawSprite((j3 + w / 2) - 12, (y + h / 2) - 12, spriteMedia + 11);
        surface.drawStringCenter(String.valueOf(character.damageTaken), (j3 + w / 2) - 1, y + h / 2 + 5, 3, 0xffffff);
      }
    }
    if (character.skullVisible == 1 && character.bubbleTimeout == 0) {
      int k3 = tx + x + w / 2;
      if (character.animationCurrent == 8) {
        k3 -= (20 * ty) / 100;
      } else if (character.animationCurrent == 9) {
        k3 += (20 * ty) / 100;
      }
      int j4 = (16 * ty) / 100;
      int l4 = (16 * ty) / 100;
      surface.spriteClipping(k3 - j4 / 2, y - l4 / 2 - (10 * ty) / 100, j4, l4, spriteMedia + 13);
    }

    if (debugHud != DEBUG_HUD_NONE) {
      int dist = distance(localRegionX, localRegionY, characterLocalX(character), characterLocalY(character));
      if ((debugHud == DEBUG_HUD_NAMES || debugHud == DEBUG_HUD_ALL) &&
          character != null &&
          character.name != null &&
          character.serverIndex != localPlayer.serverIndex) {
        int leveldiff = 0;
        if (localPlayer.level > 0 && character.level > 0) {
          leveldiff = localPlayer.level - character.level;
        }
        int i1 = surface.getColor("whi");
        if (leveldiff < 0) {
          i1 = surface.getColor("or1");
        }
        if (leveldiff < -3) {
          i1 = surface.getColor("or2");
        }
        if (leveldiff < -6) {
          i1 = surface.getColor("or3");
        }
        if (leveldiff < -9) {
          i1 = surface.getColor("red");
        }
        if (leveldiff > 0) {
          i1 = surface.getColor("gr1");
        }
        if (leveldiff > 3) {
          i1 = surface.getColor("gr2");
        }
        if (leveldiff > 6) {
          i1 = surface.getColor("gr3");
        }
        if (leveldiff > 9) {
          i1 = surface.getColor("gre");
        }
        String s1 = " " + alphaize(i1, dist) + "(lvl-" + character.level + ")";
        addReceivedMessage(x, y, w, alphaize(0x00aa00, dist) + character.name + s1);
      }
      if (debugHud == DEBUG_HUD_HITBOXES || debugHud == DEBUG_HUD_ALL) {
        surface.drawBoxAlpha(x, y, w, h, 0x00ff00, 60);
      }
    }
  }

  private void drawChatMessageTabs() {
    if (gameWidth > surface.spriteWidth[spriteMedia + 22]) {
      surface.drawSprite(0, gameHeight, spriteMedia + 22);
      int x = surface.spriteWidth[spriteMedia + 22];
      while (x < gameWidth) {
        surface.drawSprite(x, gameHeight, spriteMedia + 22);
        x += x;
      }
    }
    surface.drawSprite(gameWidth / 2 - surface.spriteWidth[spriteMedia + 23] / 2, gameHeight - 4, spriteMedia + 23);
    int col = Utility.rgb2long(200, 200, 255);
    if (messageTabSelected == 0) {
      col = Utility.rgb2long(255, 200, 50);
    }
    if (messageTabFlashAll % 30 > 15) {
      col = Utility.rgb2long(255, 50, 50);
    }
    surface.drawStringCenter("All messages", gameWidth / 2 - 202, gameHeight + 6, 0, col);
    col = Utility.rgb2long(200, 200, 255);
    if (messageTabSelected == 1) {
      col = Utility.rgb2long(255, 200, 50);
    }
    if (messageTabFlashHistory % 30 > 15) {
      col = Utility.rgb2long(255, 50, 50);
    }
    surface.drawStringCenter("Chat history", gameWidth / 2 - 101, gameHeight + 6, 0, col);
    col = Utility.rgb2long(200, 200, 255);
    if (messageTabSelected == 2) {
      col = Utility.rgb2long(255, 200, 50);
    }
    if (messtageTabFlashQuest % 30 > 15) {
      col = Utility.rgb2long(255, 50, 50);
    }
    surface.drawStringCenter("Quest history", gameWidth / 2 - 1, gameHeight + 6, 0, col);
    col = Utility.rgb2long(200, 200, 255);
    if (messageTabSelected == 3) {
      col = Utility.rgb2long(255, 200, 50);
    }
    if (messageTabFlashPrivate % 30 > 15) {
      col = Utility.rgb2long(255, 50, 50);
    }
    surface.drawStringCenter("Private history", gameWidth / 2 + 99, gameHeight + 6, 0, col);
    surface.drawStringCenter("Report abuse", gameWidth / 2 + 201, gameHeight + 6, 0, 0xffffff);
  }

  private void drawUiTabMagic(boolean nomenus) {
    int uiX = surface.width2 - 199;
    int uiY = 36;
    surface.drawSprite(uiX - 49, 3, spriteMedia + 4);
    int uiWidth = 196;// '\304';
    int uiHeight = 182;// '\266';
    int l;
    int k = l = Utility.rgb2long(160, 160, 160);
    if (tabMagicPrayer == 0) {
      k = Utility.rgb2long(220, 220, 220);
    } else {
      l = Utility.rgb2long(220, 220, 220);
    }
    surface.drawBoxAlpha(uiX, uiY, uiWidth / 2, 24, k, 128);
    surface.drawBoxAlpha(uiX + uiWidth / 2, uiY, uiWidth / 2, 24, l, 128);
    surface.drawBoxAlpha(uiX, uiY + 24, uiWidth, 90, Utility.rgb2long(220, 220, 220), 128);
    surface.drawBoxAlpha(uiX, uiY + 24 + 90, uiWidth, uiHeight - 90 - 24, Utility.rgb2long(160, 160, 160), 128);
    surface.drawLineHoriz(uiX, uiY + 24, uiWidth, 0);
    surface.drawLineVert(uiX + uiWidth / 2, uiY, 24, 0);
    surface.drawLineHoriz(uiX, uiY + 113, uiWidth, 0);
    surface.drawStringCenter("Magic", uiX + uiWidth / 4, uiY + 16, 4, 0);
    surface.drawStringCenter("Prayers", uiX + uiWidth / 4 + uiWidth / 2, uiY + 16, 4, 0);
    if (tabMagicPrayer == 0) {
      panelMagic.clearList(controlListMagic);
      int i1 = 0;
      for (int spell = 0; spell < GameData.spellCount; spell++) {
        String s = "@yel@";
        for (int rune = 0; rune < GameData.spellRunesRequired[spell]; rune++) {
          int k4 = GameData.spellRunesId[spell][rune];
          if (hasInventoryItems(k4, GameData.spellRunesCount[spell][rune])) {
            continue;
          }
          s = "@whi@";
          break;
        }

        int l4 = playerStatCurrent[6];
        if (GameData.spellLevel[spell] > l4) {
          s = "@bla@";
        }
        panelMagic.addListEntry(controlListMagic,
                                i1++,
                                s + "Level " + GameData.spellLevel[spell] + ": " + GameData.spellName[spell]
        );
      }

      panelMagic.drawPanel();
      int i3 = panelMagic.getListEntryIndex(controlListMagic);
      if (i3 != -1) {
        surface.drawstring("Level " + GameData.spellLevel[i3] + ": " + GameData.spellName[i3],
                           uiX + 2,
                           uiY + 124,
                           1,
                           0xffff00
        );
        surface.drawstring(GameData.spellDescription[i3], uiX + 2, uiY + 136, 0, 0xffffff);
        for (int i4 = 0; i4 < GameData.spellRunesRequired[i3]; i4++) {
          int i5 = GameData.spellRunesId[i3][i4];
          surface.drawSprite(uiX + 2 + i4 * 44, uiY + 150, spriteItem + GameData.itemPicture[i5]);
          int j5 = getInventoryCount(i5);
          int k5 = GameData.spellRunesCount[i3][i4];
          String s2 = "@red@";
          if (hasInventoryItems(i5, k5)) {
            s2 = "@gre@";
          }
          surface.drawstring(s2 + j5 + "/" + k5, uiX + 2 + i4 * 44, uiY + 150, 1, 0xffffff);
        }

      } else {
        surface.drawstring("Point at a spell for a description", uiX + 2, uiY + 124, 1, 0);
      }
    }
    if (tabMagicPrayer == 1) {
      panelMagic.clearList(controlListMagic);
      int j1 = 0;
      for (int j2 = 0; j2 < GameData.prayerCount; j2++) {
        String s1 = "@whi@";
        if (GameData.prayerLevel[j2] > playerStatBase[5]) {
          s1 = "@bla@";
        }
        if (prayerOn[j2]) {
          s1 = "@gre@";
        }
        panelMagic.addListEntry(controlListMagic,
                                j1++,
                                s1 + "Level " + GameData.prayerLevel[j2] + ": " + GameData.prayerName[j2]
        );
      }

      panelMagic.drawPanel();
      int j3 = panelMagic.getListEntryIndex(controlListMagic);
      if (j3 != -1) {
        surface.drawStringCenter("Level " + GameData.prayerLevel[j3] + ": " + GameData.prayerName[j3],
                                 uiX + uiWidth / 2,
                                 uiY + 130,
                                 1,
                                 0xffff00
        );
        surface.drawStringCenter(GameData.prayerDescription[j3], uiX + uiWidth / 2, uiY + 145, 0, 0xffffff);
        surface.drawStringCenter("Drain rate: " + GameData.prayerDrain[j3], uiX + uiWidth / 2, uiY + 160, 1, 0);
      } else {
        surface.drawstring("Point at a prayer for a description", uiX + 2, uiY + 124, 1, 0);
      }
    }
    if (!nomenus) {
      return;
    }
    int mouseX = super.mouseX - (surface.width2 - 199);
    int mouseY = super.mouseY - 36;
    if (mouseX >= 0 && mouseY >= 0 && mouseX < 196 && mouseY < 182) {
      panelMagic.handleMouse(mouseX + (surface.width2 - 199),
                             mouseY + 36,
                             super.lastMouseButtonDown,
                             super.mouseButtonDown
      );
      if (mouseY <= 24 && mouseButtonClick == 1) {
        if (mouseX < 98 && tabMagicPrayer == 1) {
          tabMagicPrayer = 0;
          panelMagic.resetListProps(controlListMagic);
        } else if (mouseX > 98 && tabMagicPrayer == 0) {
          tabMagicPrayer = 1;
          panelMagic.resetListProps(controlListMagic);
        }
      }
      if (mouseButtonClick == 1 && tabMagicPrayer == 0) {
        int idx = panelMagic.getListEntryIndex(controlListMagic);
        if (idx != -1) {
          int k2 = playerStatCurrent[6];
          if (GameData.spellLevel[idx] > k2) {
            showMessage("Your magic ability is not high enough for this spell", 3);
          } else {
            int k3;
            for (k3 = 0; k3 < GameData.spellRunesRequired[idx]; k3++) {
              int j4 = GameData.spellRunesId[idx][k3];
              if (hasInventoryItems(j4, GameData.spellRunesCount[idx][k3])) {
                continue;
              }
              showMessage("You don't have all the reagents you need for this spell", 3);
              k3 = -1;
              break;
            }

            if (k3 == GameData.spellRunesRequired[idx]) {
              selectedSpell = idx;
              selectedItemInventoryIndex = -1;
            }
          }
        }
      }
      if (mouseButtonClick == 1 && tabMagicPrayer == 1) {
        int l1 = panelMagic.getListEntryIndex(controlListMagic);
        if (l1 != -1) {
          int l2 = playerStatBase[5];
          if (GameData.prayerLevel[l1] > l2) {
            showMessage("Your prayer ability is not high enough for this prayer", 3);
          } else if (playerStatCurrent[5] == 0) {
            showMessage("You have run out of prayer points. Return to a church to recharge", 3);
          } else if (prayerOn[l1]) {
            super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PRAYER_OFF));
            super.clientStream.putByte(l1);
            super.clientStream.sendPacket();
            prayerOn[l1] = false;
            playSoundFile("prayeroff");
          } else {
            super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PRAYER_ON));
            super.clientStream.putByte(l1);
            super.clientStream.sendPacket();
            prayerOn[l1] = true;
            playSoundFile("prayeron");
          }
        }
      }
      mouseButtonClick = 0;
    }
  }

  private void drawDialogShop() {
    int dialogX = gameWidth / 2 - 204; // 52
    int dialogY = gameHeight / 2 - 123; // 44
    if (mouseButtonClick != 0 && inputPopupType == 0) {
      mouseButtonClick = 0;
      int mouseX = super.mouseX - dialogX;
      int mouseY = super.mouseY - dialogY;
      if (mouseX >= 0 && mouseY >= 12 && mouseX < 408 && mouseY < 246) {
        int itemIndex = 0;
        for (int row = 0; row < 5; row++) {
          for (int col = 0; col < 8; col++) {
            int slotX = 7 + col * 49;
            int slotY = 28 + row * 34;
            if (mouseX > slotX &&
                mouseX < slotX + 49 &&
                mouseY > slotY &&
                mouseY < slotY + 34 &&
                shopItem[itemIndex] != -1) {
              shopSelectedItemIndex = itemIndex;
              shopSelectedItemType = shopItem[itemIndex];
            }
            itemIndex++;
          }

        }

        if (shopSelectedItemIndex >= 0) {
          int itemType = shopItem[shopSelectedItemIndex];
          if (itemType != -1) {
            if (Version.CLIENT > 204) {
              int shopCount = shopItemCount[shopSelectedItemIndex];
              if (shopCount > 0 && mouseY >= 204 && mouseY <= 215) {
                // buy item
                byte count = 0;
                if (mouseX > 318 && mouseX < 330) {
                  count = 1;
                }
                if (shopCount >= 5 && mouseX > 333 && mouseX < 345) {
                  count = 5;
                }
                if (shopCount >= 10 && mouseX > 348 && mouseX < 365) {
                  count = 10;
                }
                if (shopCount >= 50 && mouseX > 368 && mouseX < 385) {
                  count = 50;
                }
                if (mouseX > 388 && mouseX < 400) {
                  showInputPopup(5, new String[] { "Type the number of items to buy and press enter" }, true);
                }
                if (count > 0) {
                  super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SHOP_BUY));
                  super.clientStream.putShort(shopItem[shopSelectedItemIndex]);
                  super.clientStream.putShort(shopCount);
                  super.clientStream.putShort(count);
                  super.clientStream.sendPacket();
                }
              }

              int invCount = getInventoryCount(itemType);
              if (invCount > 0 && mouseY >= 229 && mouseY <= 240) {
                // sell item
                byte count = 0;
                if (mouseX > 318 && mouseX < 330) {
                  count = 1;
                }

                if (invCount >= 5 && mouseX > 333 && mouseX < 345) {
                  count = 5;
                }

                if (invCount >= 10 && mouseX > 348 && mouseX < 365) {
                  count = 10;
                }

                if (mouseX > 388 && mouseX < 400) {
                  showInputPopup(6, new String[] { "Type the number of items to sell and press enter" }, true);
                }

                if (invCount >= 50 && mouseX > 368 && mouseX < 385) {
                  count = 50;
                }

                if (count > 0) {
                  super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SHOP_SELL));
                  super.clientStream.putShort(shopItem[shopSelectedItemIndex]);
                  super.clientStream.putShort(shopCount);
                  super.clientStream.putShort(count);
                  super.clientStream.sendPacket();
                }
              }
            } else {
              if (shopItemCount[shopSelectedItemIndex] > 0 &&
                  mouseX > 298 &&
                  mouseY >= 204 &&
                  mouseX < 408 &&
                  mouseY <= 215) {
                int priceMod = shopBuyPriceMod + shopItemPrice[shopSelectedItemIndex];
                if (priceMod < 10) {
                  priceMod = 10;
                }
                int itemPrice = (priceMod * GameData.itemBasePrice[itemType]) / 100;
                super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SHOP_BUY));
                super.clientStream.putShort(shopItem[shopSelectedItemIndex]);
                super.clientStream.putInt(itemPrice);
                super.clientStream.sendPacket();
              }
              if (getInventoryCount(itemType) > 0 && mouseX > 2 && mouseY >= 229 && mouseX < 112 && mouseY <= 240) {
                int priceMod = shopSellPriceMod + shopItemPrice[shopSelectedItemIndex];
                if (priceMod < 10) {
                  priceMod = 10;
                }
                int itemPrice = (priceMod * GameData.itemBasePrice[itemType]) / 100;
                super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SHOP_SELL));
                super.clientStream.putShort(shopItem[shopSelectedItemIndex]);
                super.clientStream.putInt(itemPrice);
                super.clientStream.sendPacket();
              }
            }
          }
        }
      } else {
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SHOP_CLOSE));
        super.clientStream.sendPacket();
        showDialogShop = false;
        return;
      }
    }
    surface.drawBox(dialogX, dialogY, 408, 12, 192);
    surface.drawBoxAlpha(dialogX, dialogY + 12, 408, 17, 0x989898, 160);
    surface.drawBoxAlpha(dialogX, dialogY + 29, 8, 170, 0x989898, 160);
    surface.drawBoxAlpha(dialogX + 399, dialogY + 29, 9, 170, 0x989898, 160);
    surface.drawBoxAlpha(dialogX, dialogY + 199, 408, 47, 0x989898, 160);
    surface.drawstring("Buying and selling items", dialogX + 1, dialogY + 10, 1, 0xffffff);
    int colour = 0xffffff;
    if (super.mouseX > dialogX + 320 &&
        super.mouseY >= dialogY &&
        super.mouseX < dialogX + 408 &&
        super.mouseY < dialogY + 12) {
      colour = 0xff0000;
    }
    surface.drawstringRight("Close window", dialogX + 406, dialogY + 10, 1, colour);
    surface.drawstring("Shops stock in green", dialogX + 2, dialogY + 24, 1, 65280);
    surface.drawstring("Number you own in blue", dialogX + 135, dialogY + 24, 1, 65535);
    surface.drawstring("Your money: " + getInventoryCount(10) + "gp", dialogX + 280, dialogY + 24, 1, 0xffff00);
    int itemIndex = 0;
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 8; col++) {
        int slotX = dialogX + 7 + col * 49;
        int slotY = dialogY + 28 + row * 34;
        if (shopSelectedItemIndex == itemIndex) {
          surface.drawBoxAlpha(slotX, slotY, 49, 34, 0xff0000, 160);
        } else {
          surface.drawBoxAlpha(slotX, slotY, 49, 34, 0xd0d0d0, 160);
        }
        surface.drawBoxEdge(slotX, slotY, 50, 35, 0);
        if (shopItem[itemIndex] != -1) {
          surface.spriteClipping(slotX,
                                 slotY,
                                 48,
                                 32,
                                 spriteItem + GameData.itemPicture[shopItem[itemIndex]],
                                 GameData.itemMask[shopItem[itemIndex]],
                                 0,
                                 0,
                                 false
          );
          surface.drawstring(String.valueOf(shopItemCount[itemIndex]), slotX + 1, slotY + 10, 1, 65280);
          surface.drawstringRight(String.valueOf(getInventoryCount(shopItem[itemIndex])),
                                  slotX + 47,
                                  slotY + 10,
                                  1,
                                  65535
          );
        }
        itemIndex++;
      }

    }

    surface.drawLineHoriz(dialogX + 5, dialogY + 222, 398, 0);
    if (shopSelectedItemIndex == -1) {
      surface.drawStringCenter("Select an object to buy or sell", dialogX + 204, dialogY + 214, 3, 0xffff00);
      return;
    }
    int selectedItemType = shopItem[shopSelectedItemIndex];
    if (selectedItemType != -1) {
      int count = shopItemCount[shopSelectedItemIndex];
      if (count > 0) {
        if (Version.CLIENT > 204) {
          int itemPrice = Utility.calculateShopItemPrice(GameData.itemBasePrice[selectedItemType],
                                                         count,
                                                         true,
                                                         this.shopBuyPriceMod,
                                                         1,
                                                         this.shopItemPrice[this.shopSelectedItemIndex],
                                                         this.shopPriceMultiplier
          );
          surface.drawstring(GameData.itemName[selectedItemType] + ": buy for " + itemPrice + " gp each",
                             dialogX + 2,
                             dialogY + 214,
                             1,
                             0xffff00
          );
          boolean yvalid = super.mouseY >= dialogY + 204 && super.mouseY <= (215 + dialogY);
          colour = 0xffffff;
          surface.drawstring("Buy:", dialogX + 285, dialogY + 214, 3, 0xffffff);
          if (yvalid && dialogX + 318 < super.mouseX && super.mouseX < (330 + dialogX)) {
            colour = 0xff0000;
          }

          surface.drawstring("1", dialogX + 320, dialogY + 214, 3, colour);
          if (count >= 5) {
            colour = 0xffffff;
            if (yvalid && 333 + dialogX < super.mouseX && super.mouseX < dialogX + 345) {
              colour = 0xff0000;
            }

            surface.drawstring("5", dialogX + 335, dialogY + 214, 3, colour);
          }

          if (count >= 10) {
            colour = 0xffffff;
            if (yvalid && super.mouseX > 348 + dialogX && super.mouseX < (dialogX + 365)) {
              colour = 0xff0000;
            }

            surface.drawstring("10", dialogX + 350, dialogY + 214, 3, colour);
          }

          if (count >= 50) {
            colour = 0xffffff;
            if (yvalid && super.mouseX > (dialogX + 368) && super.mouseX < dialogX + 385) {
              colour = 0xff0000;
            }

            surface.drawstring("50", dialogX + 370, dialogY + 214, 3, colour);
          }

          colour = 0xffffff;
          if (yvalid && (388 + dialogX) < super.mouseX && dialogX + 400 > super.mouseX) {
            colour = 0xff0000;
          }

          surface.drawstring("X", dialogX + 390, dialogY + 214, 3, colour);
        } else {
          int priceMod = shopBuyPriceMod + shopItemPrice[shopSelectedItemIndex];
          if (priceMod < 10) {
            priceMod = 10;
          }
          int itemPrice = (priceMod * GameData.itemBasePrice[selectedItemType]) / 100;
          surface.drawstring("Buy a new " + GameData.itemName[selectedItemType] + " for " + itemPrice + "gp",
                             dialogX + 2,
                             dialogY + 214,
                             1,
                             0xffff00
          );
          colour = 0xffffff;
          if (super.mouseX > dialogX + 298 &&
              super.mouseY >= dialogY + 204 &&
              super.mouseX < dialogX + 408 &&
              super.mouseY <= dialogY + 215) {
            colour = 0xff0000;
          }
          surface.drawstringRight("Click here to buy", dialogX + 405, dialogY + 214, 3, colour);
        }
      } else {
        surface.drawStringCenter("This item is not currently available to buy",
                                 dialogX + 204,
                                 dialogY + 214,
                                 3,
                                 0xffff00
        );
      }
      if (getInventoryCount(selectedItemType) > 0) {
        if (Version.CLIENT > 204) {
          int itemPrice = Utility.calculateShopItemPrice(GameData.itemBasePrice[selectedItemType],
                                                         count,
                                                         false,
                                                         this.shopSellPriceMod,
                                                         1,
                                                         this.shopItemPrice[this.shopSelectedItemIndex],
                                                         this.shopPriceMultiplier
          );
          surface.drawstring(GameData.itemName[selectedItemType] + ": sell for " + itemPrice + " gp each",
                             dialogX + 2,
                             dialogY + 239,
                             1,
                             0xffff00
          );
          boolean yvalid = (dialogY + 229) <= super.mouseY && super.mouseY <= (240 + dialogY);
          colour = 0xffffff;
          surface.drawstring("Sell:", dialogX + 285, dialogY + 239, 3, 0xffffff);
          if (yvalid && dialogX - -318 < super.mouseX && super.mouseX < (330 + dialogX)) {
            colour = 0xff0000;
          }

          surface.drawstring("1", dialogX + 320, dialogY + 239, 3, colour);
          if (count >= 5) {
            colour = 0xffffff;
            if (yvalid && 333 + dialogX < super.mouseX && super.mouseX < dialogX + 345) {
              colour = 0xff0000;
            }

            surface.drawstring("5", dialogX + 335, dialogY + 239, 3, colour);
          }

          if (count >= 10) {
            colour = 0xffffff;
            if (yvalid && super.mouseX > 348 + dialogX && super.mouseX < (dialogX + 365)) {
              colour = 0xff0000;
            }

            surface.drawstring("10", dialogX + 350, dialogY + 239, 3, colour);
          }

          if (count >= 50) {
            colour = 0xffffff;
            if (yvalid && super.mouseX > (dialogX - -368) && super.mouseX < dialogX - -385) {
              colour = 0xff0000;
            }

            surface.drawstring("50", dialogX + 370, dialogY + 239, 3, colour);
          }

          colour = 0xffffff;
          if (yvalid && (388 + dialogX) < super.mouseX && dialogX - -400 > super.mouseX) {
            colour = 0xff0000;
          }

          surface.drawstring("X", dialogX + 390, dialogY + 239, 3, colour);
        } else {
          int priceMod = shopSellPriceMod + shopItemPrice[shopSelectedItemIndex];
          if (priceMod < 10) {
            priceMod = 10;
          }
          int itemPrice = (priceMod * GameData.itemBasePrice[selectedItemType]) / 100;
          surface.drawstringRight("Sell your " + GameData.itemName[selectedItemType] + " for " + itemPrice + "gp",
                                  dialogX + 405,
                                  dialogY + 239,
                                  1,
                                  0xffff00
          );
          colour = 0xffffff;
          if (super.mouseX > dialogX + 2 &&
              super.mouseY >= dialogY + 229 &&
              super.mouseX < dialogX + 112 &&
              super.mouseY <= dialogY + 240) {
            colour = 0xff0000;
          }
          surface.drawstring("Click here to sell", dialogX + 2, dialogY + 239, 3, colour);
        }
      } else {
        surface.drawStringCenter("You do not have any of this item to sell", dialogX + 204, dialogY + 239, 3, 0xffff00);
      }
    }
  }

  private boolean hasInventoryItems(int id, int mincount) {
    if (id == 31 && (isItemEquipped(197) || isItemEquipped(615) || isItemEquipped(682))) {
      return true;
    }
    if (id == 32 && (isItemEquipped(102) || isItemEquipped(616) || isItemEquipped(683))) {
      return true;
    }
    if (id == 33 && (isItemEquipped(101) || isItemEquipped(617) || isItemEquipped(684))) {
      return true;
    }
    if (id == 34 && (isItemEquipped(103) || isItemEquipped(618) || isItemEquipped(685))) {
      return true;
    }
    return getInventoryCount(id) >= mincount;
  }

  private void drawGame() {
    if (deathScreenTimeout != 0) {
      surface.fade2black();
      surface.drawStringCenter("Oh dear! You are dead...", gameWidth / 2, gameHeight / 2, 7, 0xff0000);
      drawChatMessageTabs();
      surface.draw(graphics, 0, 0);
      return;
    }
    if (showAppearanceChange) {
      drawAppearancePanelCharacterSprites();
      return;
    }
    if (isSleeping) {
      surface.fade2black();
      if (Math.random() < 0.14999999999999999D) {
        surface.drawStringCenter("ZZZ",
                                 (int) (Math.random() * 80D),
                                 (int) (Math.random() * (double) gameHeight),
                                 5,
                                 (int) (Math.random() * 16777215D)
        );
      }
      if (Math.random() < 0.14999999999999999D) {
        surface.drawStringCenter("ZZZ",
                                 gameWidth - (int) (Math.random() * 80D),
                                 (int) (Math.random() * (double) gameHeight),
                                 5,
                                 (int) (Math.random() * 16777215D)
        );
      }
      surface.drawBox(gameWidth / 2 - 100, gameHeight / 2 - 7, 200, 40, 0);
      surface.drawStringCenter("You are sleeping", gameWidth / 2, gameHeight / 2 - 117, 7, 0xffff00);
      surface.drawStringCenter("Fatigue: " + (fatigueSleeping * 100) / 750 + "%",
                               gameWidth / 2,
                               gameHeight / 2 - 77,
                               7,
                               0xffff00
      );
      surface.drawStringCenter("When you want to wake up just use your",
                               gameWidth / 2,
                               gameHeight / 2 - 27,
                               5,
                               0xffffff
      );
      surface.drawStringCenter("keyboard to type the word in the box below",
                               gameWidth / 2,
                               gameHeight / 2 - 7,
                               5,
                               0xffffff
      );
      surface.drawStringCenter(super.inputTextCurrent + "*", gameWidth / 2, gameHeight / 2 + 13, 5, 65535);
      if (sleepingStatusText == null) {
        surface.drawSprite(gameWidth / 2 - 127, gameHeight / 2 + 63, spriteTexture + 1);
      } else {
        surface.drawStringCenter(sleepingStatusText, gameWidth / 2, gameHeight / 2 + 93, 5, 0xff0000);
      }
      surface.drawBoxEdge(gameWidth / 2 - 128, gameHeight / 2 + 62, 257, 42, 0xffffff);
      drawChatMessageTabs();
      surface.drawStringCenter("If you can't read the word", gameWidth / 2, gameHeight / 2 + 123, 1, 0xffffff);
      surface.drawStringCenter("@yel@click here@whi@ to get a different one",
                               gameWidth / 2,
                               gameHeight / 2 + 138,
                               1,
                               0xffffff
      );
      surface.draw(graphics, 0, 0);
      return;
    }
    if (!world.playerAlive) {
      return;
    }
    for (int i = 0; i < 64; i++) {
      scene.removeModel(world.roofModels[lastHeightOffset][i]);
      if (lastHeightOffset == 0) {
        scene.removeModel(world.wallModels[1][i]);
        scene.removeModel(world.roofModels[1][i]);
        scene.removeModel(world.wallModels[2][i]);
        scene.removeModel(world.roofModels[2][i]);
      }
      if (!zoomControls) {
        fogOfWar = true;
      }
      if (lastHeightOffset == 0 &&
          (world.objectAdjacency[localPlayer.currentX / 128][localPlayer.currentY / 128] & 128) == 0) {// 0x80
        scene.addModel(world.roofModels[lastHeightOffset][i]);
        if (lastHeightOffset == 0) {
          scene.addModel(world.wallModels[1][i]);
          scene.addModel(world.roofModels[1][i]);
          scene.addModel(world.wallModels[2][i]);
          scene.addModel(world.roofModels[2][i]);
        }
        if (!zoomControls) {
          fogOfWar = false;
        }
      }
    }

    if (objectAnimationNumberFireLightningSpell != lastObjectAnimationNumberFireLightningSpell) {
      lastObjectAnimationNumberFireLightningSpell = objectAnimationNumberFireLightningSpell;
      for (int j = 0; j < objectCount; j++) {
        if (objectId[j] == 97) {
          updateObjectAnimation(j, "firea" + (objectAnimationNumberFireLightningSpell + 1));
        }
        if (objectId[j] == 274) {
          updateObjectAnimation(j, "fireplacea" + (objectAnimationNumberFireLightningSpell + 1));
        }
        if (objectId[j] == 1031) {
          updateObjectAnimation(j, "lightning" + (objectAnimationNumberFireLightningSpell + 1));
        }
        if (objectId[j] == 1036) {
          updateObjectAnimation(j, "firespell" + (objectAnimationNumberFireLightningSpell + 1));
        }
        if (objectId[j] == 1147) {
          updateObjectAnimation(j, "spellcharge" + (objectAnimationNumberFireLightningSpell + 1));
        }
      }

    }
    if (objectAnimationNumberTorch != lastObjectAnimationNumberTorch) {
      lastObjectAnimationNumberTorch = objectAnimationNumberTorch;
      for (int k = 0; k < objectCount; k++) {
        if (objectId[k] == 51) {
          updateObjectAnimation(k, "torcha" + (objectAnimationNumberTorch + 1));
        }
        if (objectId[k] == 143) {
          updateObjectAnimation(k, "skulltorcha" + (objectAnimationNumberTorch + 1));
        }
      }

    }
    if (objectAnimationNumberClaw != lastOjectAnimationNumberClaw) {
      lastOjectAnimationNumberClaw = objectAnimationNumberClaw;
      for (int l = 0; l < objectCount; l++) {
        if (objectId[l] == 1142) {
          updateObjectAnimation(l, "clawspell" + (objectAnimationNumberClaw + 1));
        }
      }

    }
    scene.reduceSprites(spriteCount);
    spriteCount = 0;
    for (int i = 0; i < playerCount; i++) {
      GameCharacter character = players[i];
      if (character.colourBottom != 255) {
        int x = character.currentX;
        int y = character.currentY;
        int elev = -world.getElevation(x, y);
        int id = scene.addSprite(5000 + i, x, elev, y, 145, 220, i + 10000);
        spriteCount++;
        if (character == localPlayer) {
          scene.setLocalPlayer(id);
        }
        if (character.animationCurrent == 8) {
          scene.setSpriteTranslateX(id, -30);
        }
        if (character.animationCurrent == 9) {
          scene.setSpriteTranslateX(id, 30);
        }
      }
    }

    for (int i = 0; i < playerCount; i++) {
      GameCharacter player = players[i];
      if (player.projectileRange > 0) {
        GameCharacter character = null;
        if (player.attackingNpcServerIndex != -1) {
          character = npcsServer[player.attackingNpcServerIndex];
        } else if (player.attackingPlayerServerIndex != -1) {
          character = playerServer[player.attackingPlayerServerIndex];
        }
        if (character != null) {
          int sx = player.currentX;
          int sy = player.currentY;
          int selev = -world.getElevation(sx, sy) - 110;
          int dx = character.currentX;
          int dy = character.currentY;
          int delev = -world.getElevation(dx, dy) - GameData.npcHeight[character.npcId] / 2;
          int rx = (sx * player.projectileRange + dx * (projectileMaxRange - player.projectileRange)) /
                   projectileMaxRange;
          int rz = (selev * player.projectileRange + delev * (projectileMaxRange - player.projectileRange)) /
                   projectileMaxRange;
          int ry = (sy * player.projectileRange + dy * (projectileMaxRange - player.projectileRange)) /
                   projectileMaxRange;
          scene.addSprite(spriteProjectile + player.incomingProjectileSprite, rx, rz, ry, 32, 32, 0);
          spriteCount++;
        }
      }
    }

    for (int i = 0; i < npcCount; i++) {
      GameCharacter character_3 = npcs[i];
      int i3 = character_3.currentX;
      int j4 = character_3.currentY;
      int i7 = -world.getElevation(i3, j4);
      int i9 = scene.addSprite(20000 + i,
                               i3,
                               i7,
                               j4,
                               GameData.npcWidth[character_3.npcId],
                               GameData.npcHeight[character_3.npcId],
                               i + 30000
      );
      spriteCount++;
      if (character_3.animationCurrent == 8) {
        scene.setSpriteTranslateX(i9, -30);
      }
      if (character_3.animationCurrent == 9) {
        scene.setSpriteTranslateX(i9, 30);
      }
    }

    for (int i = 0; i < groundItemCount; i++) {
      int x = groundItemX[i] * magicLoc + 64;
      int y = groundItemY[i] * magicLoc + 64;
      scene.addSprite(40000 + groundItemId[i], x, -world.getElevation(x, y) - groundItemZ[i], y, 96, 64, i + 20000);
      spriteCount++;
    }

    for (int i = 0; i < teleportBubbleCount; i++) {
      int l4 = teleportBubbleX[i] * magicLoc + 64;
      int j7 = teleportBubbleY[i] * magicLoc + 64;
      int j9 = teleportBubbleType[i];
      if (j9 == 0) {
        scene.addSprite(50000 + i, l4, -world.getElevation(l4, j7), j7, 128, 256, i + 50000);
        spriteCount++;
      }
      if (j9 == 1) {
        scene.addSprite(50000 + i, l4, -world.getElevation(l4, j7), j7, 128, 64, i + 50000);
        spriteCount++;
      }
    }

    surface.interlace = false;
    surface.blackScreen();
    surface.interlace = super.interlace;
    if (lastHeightOffset == 3) {
      int i5 = 40 + (int) (Math.random() * 3D);
      int k7 = 40 + (int) (Math.random() * 7D);
      scene.setLight(i5, k7, -50, -10, -50);
    }
    itemsAboveHeadCount = 0;
    receivedMessagesCount = 0;
    healthBarCount = 0;
    if (cameraAutoAngleDebug) {
      if (optionCameraModeAuto) { // && !fogOfWar) {
        int j5 = cameraAngle;
        autorotateCamera();
        if (cameraAngle != j5) {
          cameraAutoRotatePlayerX = localPlayer.currentX;
          cameraAutoRotatePlayerY = localPlayer.currentY;
        }
      }
      scene.clipFar3d = zoomControls ? 19999 : 3000;
      scene.clipFar2d = zoomControls ? 19999 : 3000;
      scene.fogZFalloff = 1;
      scene.fogZDistance = zoomControls ? 19999 : 2800;
      cameraRotation = cameraAngle * 32;
      int x = cameraAutoRotatePlayerX + cameraRotationX;
      int y = cameraAutoRotatePlayerY + cameraRotationY;
      scene.setCamera(x, -world.getElevation(x, y), y, axisRotation, cameraRotation * 4, 0, 2000);
    } else {
      if (optionCameraModeAuto) // && !fogOfWar)
      {
        autorotateCamera();
      }
      if (!super.interlace) {
        scene.clipFar3d = zoomControls ? 19999 : 2400;
        scene.clipFar2d = zoomControls ? 19999 : 2400;
        scene.fogZFalloff = 1;
        scene.fogZDistance = zoomControls ? 19999 : 2300;
      } else {
        scene.clipFar3d = zoomControls ? 19999 : 2200;
        scene.clipFar2d = zoomControls ? 19999 : 2200;
        scene.fogZFalloff = 1;
        scene.fogZDistance = zoomControls ? 19999 : 2100;
      }
      int x = cameraAutoRotatePlayerX + cameraRotationX;
      int y = cameraAutoRotatePlayerY + cameraRotationY;
      scene.setCamera(x, -world.getElevation(x, y), y, axisRotation, cameraRotation * 4, 0, cameraZoom * 2);
    }
    scene.render();

    Manager.run("drawUi");

    if (mouseClickXStep > 0) {
      surface.drawSprite(mouseClickXX - 8, mouseClickXY - 8, spriteMedia + 14 + (24 - mouseClickXStep) / 6);
    }
    if (mouseClickXStep < 0) {
      surface.drawSprite(mouseClickXX - 8, mouseClickXY - 8, spriteMedia + 18 + (24 + mouseClickXStep) / 6);
    }
    if (systemUpdate != 0) {
      int i6 = systemUpdate / 50;
      int j8 = i6 / 60;
      i6 %= 60;
      if (i6 < 10) {
        surface.drawStringCenter("System update in: " + j8 + ":0" + i6, gameWidth / 2, gameHeight - 7, 1, 0xffff00);
      } else {
        surface.drawStringCenter("System update in: " + j8 + ":" + i6, gameWidth / 2, gameHeight - 7, 1, 0xffff00);
      }
    }
    if (!loadingArea) {
      int j6 = 2203 - (localRegionY + planeHeight + regionY);
      if (localRegionX + planeWidth + regionX >= 2640) {
        j6 = -50;
      }
      if (j6 > 0) {
        int wildlvl = 1 + j6 / 6;
        surface.drawSprite(gameWidth / 2 + 197, gameHeight - 56, spriteMedia + 13);
        surface.drawStringCenter("Wilderness", gameWidth / 2 + 209, gameHeight - 20, 1, 0xffff00);
        surface.drawStringCenter("Level: " + wildlvl, gameWidth / 2 + 209, gameHeight - 7, 1, 0xffff00);
        if (showUiWildWarn == 0) {
          showUiWildWarn = 2;
        }
      }
      if (showUiWildWarn == 0 && j6 > -10 && j6 <= 0) {
        showUiWildWarn = 1;
      }
    }
    if (messageTabSelected == 0) {
      for (int index = 0; index < messageShitSize; index++) {
        if (messageHistoryTimeout[index] > 0) {
          if (Version.CLIENT > 204) {
            String s = messageColor[index] +
                       formatMessage(messageMessages[index], messageSenders[index], messageTypes[index]);
            surface.drawstring(s, 7, gameHeight - 18 - index * 12, 1, 0xffff00, messageCrowns[index]);
          } else {
            String s = messageHistory[index];
            surface.drawstring(s, 7, gameHeight - 18 - index * 12, 1, 0xffff00);
          }
        }
      }

    }
    panelMessageTabs.hide(controlTextListChat);
    panelMessageTabs.hide(controlTextListQuest);
    panelMessageTabs.hide(controlTextListPrivate);
    if (messageTabSelected == 1) {
      panelMessageTabs.show(controlTextListChat);
    } else if (messageTabSelected == 2) {
      panelMessageTabs.show(controlTextListQuest);
    } else if (messageTabSelected == 3) {
      panelMessageTabs.show(controlTextListPrivate);
    }
    Panel.textListEntryHeightMod = 2;
    panelMessageTabs.drawPanel();
    Panel.textListEntryHeightMod = 0;
    //    if (debugHud == DEBUG_HUD_NONE) {
    //      surface.drawSpriteAlpha(surface.width2 - 3 - 197, 3, spriteMedia, 128);
    //    }
    surface.drawSpriteAlpha(surface.width2 - 3 - 197, 3, spriteMedia, 128);
    drawUi();
    surface.loggedIn = false;
    drawChatMessageTabs();
    surface.draw(graphics, 0, 0);
  }

  private boolean isItemEquipped(int i) {
    for (int j = 0; j < inventoryItemsCount; j++) {
      if (inventoryItemId[j] == i && inventoryEquipped[j] == 1) {
        return true;
      }
    }

    return false;
  }

  private void drawDialogDuelConfirm() {
    int dialogX = gameWidth / 2 - 468 / 2 + 22;
    int dialogY = gameHeight / 2 - 262 / 2 + 36;
    //byte dialogX = 22;
    //byte dialogY = 36;
    surface.drawBox(dialogX, dialogY, 468, 16, 192);
    surface.drawBoxAlpha(dialogX, dialogY + 16, 468, 246, 0x989898, 160);
    String name = Version.CLIENT > 204 ? duelOpponentName : Utility.hash2username(duelOpponentNameHash);
    surface.drawStringCenter("Please confirm your duel with @yel@" + name, dialogX + 234, dialogY + 12, 1, 0xffffff);
    surface.drawStringCenter("Your stake:", dialogX + 117, dialogY + 30, 1, 0xffff00);
    for (int itemIndex = 0; itemIndex < duelItemsCount; itemIndex++) {
      String s = GameData.itemName[duelItems[itemIndex]];
      if (GameData.itemStackable[duelItems[itemIndex]] == 0) {
        s = s + " x " + formatNumber(duelItemCount[itemIndex]);
      }
      surface.drawStringCenter(s, dialogX + 117, dialogY + 42 + itemIndex * 12, 1, 0xffffff);
    }

    if (duelItemsCount == 0) {
      surface.drawStringCenter("Nothing!", dialogX + 117, dialogY + 42, 1, 0xffffff);
    }
    surface.drawStringCenter("Your opponent's stake:", dialogX + 351, dialogY + 30, 1, 0xffff00);
    for (int itemIndex = 0; itemIndex < duelOpponentItemsCount; itemIndex++) {
      String s1 = GameData.itemName[duelOpponentItems[itemIndex]];
      if (GameData.itemStackable[duelOpponentItems[itemIndex]] == 0) {
        s1 = s1 + " x " + formatNumber(duelOpponentItemCount[itemIndex]);
      }
      surface.drawStringCenter(s1, dialogX + 351, dialogY + 42 + itemIndex * 12, 1, 0xffffff);
    }

    if (duelOpponentItemsCount == 0) {
      surface.drawStringCenter("Nothing!", dialogX + 351, dialogY + 42, 1, 0xffffff);
    }
    if (duelOptionRetreat == 0) {
      surface.drawStringCenter("You can retreat from this duel", dialogX + 234, dialogY + 180, 1, 65280);
    } else {
      surface.drawStringCenter("No retreat is possible!", dialogX + 234, dialogY + 180, 1, 0xff0000);
    }
    if (duelOptionMagic == 0) {
      surface.drawStringCenter("Magic may be used", dialogX + 234, dialogY + 192, 1, 65280);
    } else {
      surface.drawStringCenter("Magic cannot be used", dialogX + 234, dialogY + 192, 1, 0xff0000);
    }
    if (duelOptionPrayer == 0) {
      surface.drawStringCenter("Prayer may be used", dialogX + 234, dialogY + 204, 1, 65280);
    } else {
      surface.drawStringCenter("Prayer cannot be used", dialogX + 234, dialogY + 204, 1, 0xff0000);
    }
    if (duelOptionWeapons == 0) {
      surface.drawStringCenter("Weapons may be used", dialogX + 234, dialogY + 216, 1, 65280);
    } else {
      surface.drawStringCenter("Weapons cannot be used", dialogX + 234, dialogY + 216, 1, 0xff0000);
    }
    surface.drawStringCenter("If you are sure click 'Accept' to begin the duel",
                             dialogX + 234,
                             dialogY + 230,
                             1,
                             0xffffff
    );
    if (!duelAccepted) {
      surface.drawSprite((dialogX + 118) - 35, dialogY + 238, spriteMedia + 25);
      surface.drawSprite((dialogX + 352) - 35, dialogY + 238, spriteMedia + 26);
    } else {
      surface.drawStringCenter("Waiting for other player...", dialogX + 234, dialogY + 250, 1, 0xffff00);
    }
    if (mouseButtonClick == 1) {
      if (super.mouseX < dialogX ||
          super.mouseY < dialogY ||
          super.mouseX > dialogX + 468 ||
          super.mouseY > dialogY + 262) {
        showDialogDuelConfirm = false;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_TRADE_DECLINE));
        super.clientStream.sendPacket();
      }
      if (super.mouseX >= (dialogX + 118) - 35 &&
          super.mouseX <= dialogX + 118 + 70 &&
          super.mouseY >= dialogY + 238 &&
          super.mouseY <= dialogY + 238 + 21) {
        duelAccepted = true;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_DUEL_CONFIRM_ACCEPT));
        super.clientStream.sendPacket();
      }
      if (super.mouseX >= (dialogX + 352) - 35 &&
          super.mouseX <= dialogX + 353 + 70 &&
          super.mouseY >= dialogY + 238 &&
          super.mouseY <= dialogY + 238 + 21) {
        showDialogDuelConfirm = false;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_DUEL_DECLINE));
        super.clientStream.sendPacket();
      }
      mouseButtonClick = 0;
    }
  }

  private void walkToGroundItem(int i, int j, int k, int l, boolean walkToAction) {
    if (walkTo(i, j, k, l, k, l, false, walkToAction)) {
      return;
    } else {
      walkToActionSource(i, j, k, l, k, l, true, walkToAction);
      return;
    }
  }

  private void drawDialogServermessage() {
    int width = gameWidth / 2 + 144;// 400
    int height = gameHeight / 2 - 67;// 100
    if (serverMessageBoxTop) {
      height = gameHeight / 2 + 133;// 300
    }
    surface.drawBox(gameWidth / 2 - width / 2, gameHeight / 2 - height / 2, width, height, 0);
    surface.drawBoxEdge(gameWidth / 2 - width / 2, gameHeight / 2 - height / 2, width, height, 0xffffff);
    surface.centrepara(serverMessage, gameWidth / 2, (gameHeight / 2 - height / 2) + 20, 1, 0xffffff, width - 40);
    int i = gameHeight / 2 - 10 + height / 2;
    int j = 0xffffff;
    if (super.mouseY > i - 12 && super.mouseY <= i && super.mouseX > width - 294 && super.mouseX < width + 6) {
      j = 0xff0000;
    }
    surface.drawStringCenter("Click here to close window", gameWidth / 2, i, 1, j);
    if (mouseButtonClick == 1) {
      if (j == 0xff0000) {
        showDialogServermessage = false;
      }
      if ((super.mouseX < gameWidth / 2 - width / 2 || super.mouseX > gameWidth / 2 + width / 2) &&
          (super.mouseY < gameHeight / 2 - height / 2 || super.mouseY > gameHeight / 2 + height / 2)) {
        showDialogServermessage = false;
      }
    }
    mouseButtonClick = 0;
  }

  private void drawDialogReportAbuseInput() {
    // TODO make it look and act like the new window
    int dialogX = gameWidth / 2 - 200; // 56
    int dialogY = gameHeight / 2 - 37; // 130
    if (super.inputTextFinal.length() > 0) {
      String s = super.inputTextFinal.trim();
      super.inputTextCurrent = "";
      super.inputTextFinal = "";
      if (s.length() > 0) {
        long l = Utility.username2hash(s);
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_REPORT_ABUSE));
        if (Version.CLIENT > 204) {
          super.clientStream.pjstr2(s);
        } else {
          super.clientStream.putLong(l);
        }
        super.clientStream.putByte(reportAbuseOffence);
        super.clientStream.putByte(reportAbuseMute ? 1 : 0);
        super.clientStream.sendPacket();
      }
      showDialogReportAbuseStep = 0;
      return;
    }
    surface.drawBox(dialogX, dialogY, 400, 100, 0);
    surface.drawBoxEdge(dialogX, dialogY, 400, 100, 0xffffff);
    int y = dialogY + 30;
    surface.drawStringCenter("Now type the name of the offending player, and press enter",
                             gameWidth / 2,
                             y,
                             1,
                             0xffff00
    );
    y += 18;
    surface.drawStringCenter("Name: " + super.inputTextCurrent + "*", gameWidth / 2, y, 4, 0xffffff);
    if (super.moderatorLevel1 > 0) {
      y = dialogY + 77;
      if (reportAbuseMute) {
        surface.drawStringCenter("Moderator option: Mute player for 48 hours: <ON>", gameWidth / 2, y, 1, 0xff8000);
      } else {
        surface.drawStringCenter("Moderator option: Mute player for 48 hours: <OFF>", gameWidth / 2, y, 1, 0xffffff);
      }
      if (super.mouseX > dialogX + 50 &&
          super.mouseX < dialogX + 350 &&
          super.mouseY > y - 13 &&
          super.mouseY < y + 2 &&
          mouseButtonClick == 1) {
        mouseButtonClick = 0;
        reportAbuseMute = !reportAbuseMute;
      }
    }
    y = dialogY + 92;
    int color = 0xffffff;
    if (super.mouseX > dialogX + 140 && super.mouseX < dialogX + 260 && super.mouseY > y - 13 && super.mouseY < y + 2) {
      color = 0xffff00;
      if (mouseButtonClick == 1) {
        mouseButtonClick = 0;
        showDialogReportAbuseStep = 0;
      }
    }
    surface.drawStringCenter("Click here to cancel", gameWidth / 2, y, 1, color);
    if (mouseButtonClick == 1 &&
        (super.mouseX < dialogX ||
         super.mouseX > dialogX + 400 ||
         super.mouseY < dialogY ||
         super.mouseY > dialogY + 100)) {
      mouseButtonClick = 0;
      showDialogReportAbuseStep = 0;
    }
  }

  public Image createImage(int i, int j) {
    if (gameFrame != null) {
      return gameFrame.createImage(i, j);
    }
    return super.createImage(i, j);
  }

  private void walkToObject(int x, int y, int id, int index) {
    int w;
    int h;
    if (id == 0 || id == 4) {
      w = GameData.objectWidth[index];
      h = GameData.objectHeight[index];
    } else {
      h = GameData.objectWidth[index];
      w = GameData.objectHeight[index];
    }
    if (GameData.objectType[index] == 2 || GameData.objectType[index] == 3) {
      if (id == 0) {
        x--;
        w++;
      }
      if (id == 2) {
        h++;
      }
      if (id == 4) {
        w++;
      }
      if (id == 6) {
        y--;
        h++;
      }
      walkToActionSource(localRegionX, localRegionY, x, y, (x + w) - 1, (y + h) - 1, false, true);
      return;
    } else {
      walkToActionSource(localRegionX, localRegionY, x, y, (x + w) - 1, (y + h) - 1, true, true);
      return;
    }
  }

  private int getInventoryCount(int id) {
    int count = 0;
    for (int k = 0; k < inventoryItemsCount; k++) {
      if (inventoryItemId[k] == id) {
        if (GameData.itemStackable[id] == 1) {
          count++;
        } else {
          count += inventoryItemStackCount[k];
        }
      }
    }

    return count;
  }

  private void drawUiTabOptions(boolean flag) {
    int uiX = surface.width2 - 199;
    int uiY = 36;
    surface.drawSprite(uiX - 49, 3, spriteMedia + 6);
    int uiWidth = 196;// '\304';
    surface.drawBoxAlpha(uiX, 36, uiWidth, 65, Utility.rgb2long(181, 181, 181), 160);
    surface.drawBoxAlpha(uiX, 101, uiWidth, 65, Utility.rgb2long(201, 201, 201), 160);
    surface.drawBoxAlpha(uiX, 166, uiWidth, 95, Utility.rgb2long(181, 181, 181), 160);
    surface.drawBoxAlpha(uiX, 261, uiWidth, inTutorial ? 55 : 40, Utility.rgb2long(201, 201, 201), 160);
    int x = uiX + 3;
    int y = uiY + 15;
    surface.drawstring("Game options - click to toggle", x, y, 1, 0);
    y += 15;
    if (optionCameraModeAuto) {
      surface.drawstring("Camera angle mode - @gre@Auto", x, y, 1, 0xffffff);
    } else {
      surface.drawstring("Camera angle mode - @red@Manual", x, y, 1, 0xffffff);
    }
    y += 15;
    if (optionMouseButtonOne) {
      surface.drawstring("Mouse buttons - @red@One", x, y, 1, 0xffffff);
    } else {
      surface.drawstring("Mouse buttons - @gre@Two", x, y, 1, 0xffffff);
    }
    y += 15;
    if (members) {
      if (optionSoundDisabled) {
        surface.drawstring("Sound effects - @red@off", x, y, 1, 0xffffff);
      } else {
        surface.drawstring("Sound effects - @gre@on", x, y, 1, 0xffffff);
      }
    }
    y += 15;
    surface.drawstring("To change your contact details,", x, y, 0, 0xffffff);
    y += 15;
    surface.drawstring("password, recovery questions, etc..", x, y, 0, 0xffffff);
    y += 15;
    surface.drawstring("please select 'account management'", x, y, 0, 0xffffff);
    y += 15;
    if (referid == 0) {
      surface.drawstring("from the runescape.com front page", x, y, 0, 0xffffff);
    } else if (referid == 1) {
      surface.drawstring("from the link below the gamewindow", x, y, 0, 0xffffff);
    } else {
      surface.drawstring("from the runescape front webpage", x, y, 0, 0xffffff);
    }
    y += 15;
    y += 5;
    surface.drawstring("Privacy settings. Will be applied to", uiX + 3, y, 1, 0);
    y += 15;
    surface.drawstring("all people not on your friends list", uiX + 3, y, 1, 0);
    y += 15;
    if (super.settingsBlockChat == 0) {
      surface.drawstring("Block chat messages: @red@<off>", uiX + 3, y, 1, 0xffffff);
    } else {
      surface.drawstring("Block chat messages: @gre@<on>", uiX + 3, y, 1, 0xffffff);
    }
    y += 15;
    if (super.settingsBlockPrivate == 0) {
      surface.drawstring("Block private messages: @red@<off>", uiX + 3, y, 1, 0xffffff);
    } else {
      surface.drawstring("Block private messages: @gre@<on>", uiX + 3, y, 1, 0xffffff);
    }
    y += 15;
    if (super.settingsBlockTrade == 0) {
      surface.drawstring("Block trade requests: @red@<off>", uiX + 3, y, 1, 0xffffff);
    } else {
      surface.drawstring("Block trade requests: @gre@<on>", uiX + 3, y, 1, 0xffffff);
    }
    y += 15;
    if (members) {
      if (super.settingsBlockDuel == 0) {
        surface.drawstring("Block duel requests: @red@<off>", uiX + 3, y, 1, 0xffffff);
      } else {
        surface.drawstring("Block duel requests: @gre@<on>", uiX + 3, y, 1, 0xffffff);
      }
    }
    y += 15;
    int color;
    if (inTutorial) {
      y += 5;
      color = 0xffffff;
      if (super.mouseX > x && uiWidth + x > super.mouseX && y - 12 < super.mouseY && super.mouseY < 4 + y) {
        color = 0xffff00;
      }
      surface.drawstring("Skip the tutorial", x, y, 1, color);
      y += 15;
    }
    y += 5;
    surface.drawstring("Always logout when you finish", x, y, 1, 0);
    y += 15;
    color = 0xffffff;
    if (super.mouseX > x && super.mouseX < x + uiWidth && super.mouseY > y - 12 && super.mouseY < y + 4) {
      color = 0xffff00;
    }
    surface.drawstring("Click here to logout", uiX + 3, y, 1, color);
    if (!flag) {
      return;
    }
    int mouseX = super.mouseX - (surface.width2 - 199);
    int mouseY = super.mouseY - 36;
    if (mouseX >= 0 && mouseY >= 0 && mouseX < 196 && mouseY < 280) {
      uiX = surface.width2 - 199;
      uiY = 36;
      uiWidth = 196;// '\304';
      x = uiX + 3;
      y = uiY + 30;
      if (super.mouseX > x &&
          super.mouseX < x + uiWidth &&
          super.mouseY > y - 12 &&
          super.mouseY < y + 4 &&
          mouseButtonClick == 1) {
        optionCameraModeAuto = !optionCameraModeAuto;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SETTINGS_GAME));
        super.clientStream.putByte(0);
        super.clientStream.putByte(optionCameraModeAuto ? 1 : 0);
        super.clientStream.sendPacket();
      }
      y += 15;
      if (super.mouseX > x &&
          super.mouseX < x + uiWidth &&
          super.mouseY > y - 12 &&
          super.mouseY < y + 4 &&
          mouseButtonClick == 1) {
        optionMouseButtonOne = !optionMouseButtonOne;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SETTINGS_GAME));
        super.clientStream.putByte(2);
        super.clientStream.putByte(optionMouseButtonOne ? 1 : 0);
        super.clientStream.sendPacket();
      }
      y += 15;
      if (members &&
          super.mouseX > x &&
          super.mouseX < x + uiWidth &&
          super.mouseY > y - 12 &&
          super.mouseY < y + 4 &&
          mouseButtonClick == 1) {
        optionSoundDisabled = !optionSoundDisabled;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SETTINGS_GAME));
        super.clientStream.putByte(3);
        super.clientStream.putByte(optionSoundDisabled ? 1 : 0);
        super.clientStream.sendPacket();
      }
      y += 15;
      y += 15;
      y += 15;
      y += 15;
      y += 15;
      boolean flag1 = false;
      y += 35;
      if (super.mouseX > x &&
          super.mouseX < x + uiWidth &&
          super.mouseY > y - 12 &&
          super.mouseY < y + 4 &&
          mouseButtonClick == 1) {
        super.settingsBlockChat = 1 - super.settingsBlockChat;
        flag1 = true;
      }
      y += 15;
      if (super.mouseX > x &&
          super.mouseX < x + uiWidth &&
          super.mouseY > y - 12 &&
          super.mouseY < y + 4 &&
          mouseButtonClick == 1) {
        super.settingsBlockPrivate = 1 - super.settingsBlockPrivate;
        flag1 = true;
      }
      y += 15;
      if (super.mouseX > x &&
          super.mouseX < x + uiWidth &&
          super.mouseY > y - 12 &&
          super.mouseY < y + 4 &&
          mouseButtonClick == 1) {
        super.settingsBlockTrade = 1 - super.settingsBlockTrade;
        flag1 = true;
      }
      y += 15;
      if (members &&
          super.mouseX > x &&
          super.mouseX < x + uiWidth &&
          super.mouseY > y - 12 &&
          super.mouseY < y + 4 &&
          mouseButtonClick == 1) {
        super.settingsBlockDuel = 1 - super.settingsBlockDuel;
        flag1 = true;
      }
      y += 15;
      if (flag1) {
        sendPrivacySettings(super.settingsBlockChat,
                            super.settingsBlockPrivate,
                            super.settingsBlockTrade,
                            super.settingsBlockDuel
        );
      }
      if (inTutorial) {
        y += 5;
        if (x < super.mouseX &&
            super.mouseX < uiWidth + x &&
            y - 12 < super.mouseY &&
            (y + 4) > super.mouseY &&
            this.mouseButtonClick == 1) {
          showInputPopup(9, new String[] {
            "Are you sure you wish to skip the tutorial", "and teleport to Lumbridge?"
          }, false);
          this.showUiTab = 0;
        }

        y += 15;
      }
      y += 20;
      if (super.mouseX > x &&
          super.mouseX < x + uiWidth &&
          super.mouseY > y - 12 &&
          super.mouseY < y + 4 &&
          mouseButtonClick == 1) {
        sendLogout();
      }
      mouseButtonClick = 0;
    }
  }

  void drawTeleportBubble(int x, int y, int w, int h, int id, int tx, int ty) {
    int type = teleportBubbleType[id];
    int time = teleportBubbleTime[id];
    if (type == 0) {
      int j2 = 255 + time * 5 * 256;
      surface.drawCircle(x + w / 2, y + h / 2, 20 + time * 2, j2, 255 - time * 5);
    }
    if (type == 1) {
      int k2 = 0xff0000 + time * 5 * 256;
      surface.drawCircle(x + w / 2, y + h / 2, 10 + time, k2, 255 - time * 5);
    }
  }

  private void updateObjectAnimation(int i, String s) { // looks like it just updates objects like torches etc to flip between the different models and appear "animated"
    int j = objectX[i];
    int k = objectY[i];
    int l = j - localPlayer.currentX / 128;
    int i1 = k - localPlayer.currentY / 128;
    byte byte0 = 7;
    if (j >= 0 && k >= 0 && j < 96 && k < 96 && l > -byte0 && l < byte0 && i1 > -byte0 && i1 < byte0) {
      scene.removeModel(objectModel[i]);
      int j1 = GameData.getModelIndex(s);
      GameModel gameModel = gameModels[j1].copy();
      scene.addModel(gameModel);
      gameModel.setLight(true, 48, 48, -50, -10, -50);
      gameModel.copyPosition(objectModel[i]);
      gameModel.key = i;
      objectModel[i] = gameModel;
    }
  }

  private void createTopMouseMenu() {
    if (selectedSpell >= 0 || selectedItemInventoryIndex >= 0) {
      menuItemText1[menuItemsCount] = "Cancel";
      menuItemText2[menuItemsCount] = "";
      menuItemID[menuItemsCount] = 4000;
      menuItemsCount++;
    }
    for (int i = 0; i < menuItemsCount; i++) {
      menuIndices[i] = i;
    }

    for (boolean flag = false; !flag; ) {
      flag = true;
      for (int j = 0; j < menuItemsCount - 1; j++) {
        int l = menuIndices[j];
        int j1 = menuIndices[j + 1];
        if (menuItemID[l] > menuItemID[j1]) {
          menuIndices[j] = j1;
          menuIndices[j + 1] = l;
          flag = false;
        }
      }

    }

    if (menuItemsCount > 20) {
      menuItemsCount = 20;
    }
    if (menuItemsCount > 0) {
      int k = -1;
      for (int i1 = 0; i1 < menuItemsCount; i1++) {
        if (menuItemText2[menuIndices[i1]] == null || menuItemText2[menuIndices[i1]].length() <= 0) {
          continue;
        }
        k = i1;
        break;
      }

      String s = null;
      if ((selectedItemInventoryIndex >= 0 || selectedSpell >= 0) && menuItemsCount == 1) {
        s = "Choose a target";
      } else if ((selectedItemInventoryIndex >= 0 || selectedSpell >= 0) && menuItemsCount > 1) {
        s = "@whi@" + menuItemText1[menuIndices[0]] + " " + menuItemText2[menuIndices[0]];
      } else if (k != -1) {
        s = menuItemText2[menuIndices[k]] + ": @whi@" + menuItemText1[menuIndices[0]];
      }
      if (menuItemsCount == 2 && s != null) {
        s = s + "@whi@ / 1 more option";
      }
      if (menuItemsCount > 2 && s != null) {
        s = s + "@whi@ / " + (menuItemsCount - 1) + " more options";
      }
      if (s != null) {
        surface.drawstring(s, 6, 14, 1, 0xffff00);
      }
      if (!optionMouseButtonOne && mouseButtonClick == 1 ||
          optionMouseButtonOne && mouseButtonClick == 1 && menuItemsCount == 1) {
        // TODO packet 59 from 233
        menuItemClick(menuIndices[0]);
        mouseButtonClick = 0;
        return;
      }
      if (!optionMouseButtonOne && mouseButtonClick == 2 || optionMouseButtonOne && mouseButtonClick == 1) {
        menuHeight = (menuItemsCount + 1) * 15;
        menuWidth = surface.textWidth("Choose option", 1) + 5;
        for (int k1 = 0; k1 < menuItemsCount; k1++) {
          int l1 = surface.textWidth(menuItemText1[k1] + " " + menuItemText2[k1], 1) + 5;
          if (l1 > menuWidth) {
            menuWidth = l1;
          }
        }

        menuX = super.mouseX - menuWidth / 2;
        menuY = super.mouseY - 7;
        showRightClickMenu = true;
        if (menuX < 0) {
          menuX = 0;
        }
        if (menuY < 0) {
          menuY = 0;
        }
        if (menuX + menuWidth > gameWidth - 2) {
          menuX = gameWidth - 2 - menuWidth;
        }
        if (menuY + menuHeight > gameHeight - 19) {
          menuY = gameHeight - 19 - menuHeight;
        }
        mouseButtonClick = 0;
      }
    }
  }

  private void drawDialogLogout() {
    surface.drawBox(gameWidth / 2 - 130, gameHeight / 2 - 30, 260, 60, 0);
    surface.drawBoxEdge(gameWidth / 2 - 130, gameHeight / 2 - 30, 260, 60, 0xffffff);
    surface.drawStringCenter("Logging out...", gameWidth / 2, gameHeight / 2 + 6, 5, 0xffffff);
  }

  public void drawDialogCombatStyle() {
    byte byte0 = 7;
    byte byte1 = 15 + 12 * 4;
    int width = 175;// '\257';
    if (mouseButtonClick != 0) {
      for (int i = 0; i < 5; i++) {
        if (i <= 0 ||
            super.mouseX <= byte0 ||
            super.mouseX >= byte0 + width ||
            super.mouseY <= byte1 + i * 20 ||
            super.mouseY >= byte1 + i * 20 + 20) {
          continue;
        }
        combatStyle = i - 1;
        mouseButtonClick = 0;
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_COMBAT_STYLE));
        super.clientStream.putByte(combatStyle);
        super.clientStream.sendPacket();
        break;
      }

    }
    for (int j = 0; j < 5; j++) {
      if (j == combatStyle + 1) {
        surface.drawBoxAlpha(byte0, byte1 + j * 20, width, 20, Utility.rgb2long(255, 0, 0), 128);
      } else {
        surface.drawBoxAlpha(byte0, byte1 + j * 20, width, 20, Utility.rgb2long(190, 190, 190), 128);
      }
      surface.drawLineHoriz(byte0, byte1 + j * 20, width, 0);
      surface.drawLineHoriz(byte0, byte1 + j * 20 + 20, width, 0);
    }

    surface.drawStringCenter("Select combat style", byte0 + width / 2, byte1 + 16, 3, 0xffffff);
    surface.drawStringCenter("Controlled (+1 of each)", byte0 + width / 2, byte1 + 36, 3, 0);
    surface.drawStringCenter("Aggressive (+3 strength)", byte0 + width / 2, byte1 + 56, 3, 0);
    surface.drawStringCenter("Accurate   (+3 attack)", byte0 + width / 2, byte1 + 76, 3, 0);
    surface.drawStringCenter("Defensive  (+3 defense)", byte0 + width / 2, byte1 + 96, 3, 0);
  }

  private void menuItemClick(int i) {
    int mx = menuItemX[i];
    int my = menuItemY[i];
    int midx = menuSourceType[i];
    int msrcidx = menuSourceIndex[i];
    int mtargetindex = menuTargetIndex[i];
    int mitemid = menuItemID[i];
    String mtext = menuTextVar[i];
    if (mitemid == 200) {
      walkToGroundItem(localRegionX, localRegionY, mx, my, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_GROUNDITEM));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putShort(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedSpell = -1;
    }
    if (mitemid == 210) {
      walkToGroundItem(localRegionX, localRegionY, mx, my, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_USEWITH_GROUNDITEM));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putShort(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedItemInventoryIndex = -1;
    }
    if (mitemid == 220) {
      walkToGroundItem(localRegionX, localRegionY, mx, my, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_GROUNDITEM_TAKE));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putShort(midx);
      //super.clientStream.putShort(msrcidx); // looks like it was removed because it's never set in the menu for this menu item (Take item)
      super.clientStream.sendPacket();
    }
    if (mitemid == 3200) {
      showMessage(GameData.itemDescription[midx], 3);
    }
    if (mitemid == 300) {
      walkToWallObject(mx, my, midx);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_WALLOBJECT));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putByte(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedSpell = -1;
    }
    if (mitemid == 310) {
      walkToWallObject(mx, my, midx);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_USEWITH_WALLOBJECT));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putByte(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedItemInventoryIndex = -1;
    }
    if (mitemid == 320) {
      walkToWallObject(mx, my, midx);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_WALL_OBJECT_COMMAND1));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putByte(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 2300) {
      walkToWallObject(mx, my, midx);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_WALL_OBJECT_COMMAND2));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putByte(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 3300) {
      showMessage(GameData.wallObjectDescription[midx], 3);
    }
    if (mitemid == 400) {
      walkToObject(mx, my, midx, msrcidx);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_OBJECT));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putShort(mtargetindex);
      super.clientStream.sendPacket();
      selectedSpell = -1;
    }
    if (mitemid == 410) {
      walkToObject(mx, my, midx, msrcidx);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_USEWITH_OBJECT));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putShort(mtargetindex);
      super.clientStream.sendPacket();
      selectedItemInventoryIndex = -1;
    }
    if (mitemid == 420) {
      walkToObject(mx, my, midx, msrcidx);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_OBJECT_CMD1));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.sendPacket();
    }
    if (mitemid == 2400) {
      walkToObject(mx, my, midx, msrcidx);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_OBJECT_CMD2));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.sendPacket();
    }
    if (mitemid == 3400) {
      showMessage(GameData.objectDescription[midx], 3);
    }
    if (mitemid == 600) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_INVITEM));
      super.clientStream.putShort(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedSpell = -1;
    }
    if (mitemid == 610) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_USEWITH_INVITEM));
      super.clientStream.putShort(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedItemInventoryIndex = -1;
    }
    if (mitemid == 620) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_INV_UNEQUIP));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 630) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_INV_WEAR));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 640) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_INV_CMD));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 650) {
      selectedItemInventoryIndex = midx;
      showUiTab = 0;
      selectedItemName = GameData.itemName[inventoryItemId[selectedItemInventoryIndex]];
    }
    if (mitemid == 660) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_INV_DROP));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
      selectedItemInventoryIndex = -1;
      showUiTab = 0;
      showMessage("Dropping " + GameData.itemName[inventoryItemId[midx]], 4);
    }
    //    if (mitemid == 3600) {
    //      showMessage(GameData.itemDescription[midx], 3);
    //    }
    if (mitemid == 700) {
      int l1 = (mx - 64) / magicLoc;
      int l3 = (my - 64) / magicLoc;
      walkToActionSource(localRegionX, localRegionY, l1, l3, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_NPC));
      super.clientStream.putShort(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedSpell = -1;
    }
    if (mitemid == 710) {
      int i2 = (mx - 64) / magicLoc;
      int i4 = (my - 64) / magicLoc;
      walkToActionSource(localRegionX, localRegionY, i2, i4, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_USEWITH_NPC));
      super.clientStream.putShort(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedItemInventoryIndex = -1;
    }
    if (mitemid == 720) {
      int j2 = (mx - 64) / magicLoc;
      int j4 = (my - 64) / magicLoc;
      walkToActionSource(localRegionX, localRegionY, j2, j4, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_NPC_TALK));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 725) {
      int k2 = (mx - 64) / magicLoc;
      int k4 = (my - 64) / magicLoc;
      walkToActionSource(localRegionX, localRegionY, k2, k4, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_NPC_CMD));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 715 || mitemid == 2715) {
      int l2 = (mx - 64) / magicLoc;
      int l4 = (my - 64) / magicLoc;
      walkToActionSource(localRegionX, localRegionY, l2, l4, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_NPC_ATTACK));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 3700) {
      showMessage(GameData.npcDescription[midx], 3);
    }
    if (mitemid == 800) {
      int i3 = (mx - 64) / magicLoc;
      int i5 = (my - 64) / magicLoc;
      walkToActionSource(localRegionX, localRegionY, i3, i5, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_PLAYER));
      super.clientStream.putShort(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedSpell = -1;
    }
    if (mitemid == 810) {
      int j3 = (mx - 64) / magicLoc;
      int j5 = (my - 64) / magicLoc;
      walkToActionSource(localRegionX, localRegionY, j3, j5, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_USEWITH_PLAYER));
      super.clientStream.putShort(midx);
      super.clientStream.putShort(msrcidx);
      super.clientStream.sendPacket();
      selectedItemInventoryIndex = -1;
    }
    if (mitemid == 805 || mitemid == 2805) {
      int k3 = (mx - 64) / magicLoc;
      int k5 = (my - 64) / magicLoc;
      walkToActionSource(localRegionX, localRegionY, k3, k5, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PLAYER_ATTACK));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 2806) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PLAYER_DUEL));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 2810) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PLAYER_TRADE));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }
    if (mitemid == 2820) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PLAYER_FOLLOW));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
    }

    if (Version.CLIENT > 204) {
      if (mitemid == 2830) {
        super.inputPmFinal = "";
        super.inputPmCurrent = "";
        privateMessageTarget = mtext;
        showDialogSocialInput = 2;
      }
      if (mitemid == 2831) {
        friendAdd(mtext);
      }
      if (mitemid == 2832) {
        ignoreAdd(mtext);
      }
      if (mitemid == 2833) {
        super.inputTextCurrent = mtext;
        super.inputTextFinal = "";
        showDialogReportAbuseStep = 1;
      }
    }

    if (mitemid == 900) {
      walkToActionSource(localRegionX, localRegionY, mx, my, true);
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_GROUND));
      super.clientStream.putShort(mx + regionX);
      super.clientStream.putShort(my + regionY);
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
      selectedSpell = -1;
    }
    if (mitemid == 920) {
      walkToActionSource(localRegionX, localRegionY, mx, my, false);
      if (mouseClickXStep == -24) {
        mouseClickXStep = 24;
      }
    }
    if (mitemid == 1000) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_SELF));
      super.clientStream.putShort(midx);
      super.clientStream.sendPacket();
      selectedSpell = -1;
    }
    if (mitemid == 4000) {
      selectedItemInventoryIndex = -1;
      selectedSpell = -1;
    }
  }

  private void drawUiTabPlayerInfo(boolean nomenus) {
    int uiX = surface.width2 - 199;
    int uiY = 36;
    surface.drawSprite(uiX - 49, 3, spriteMedia + 3);
    int uiWidth = 196;// '\304';
    int uiHeight = 275;// '\u0113';
    int l;
    int k = l = Utility.rgb2long(160, 160, 160);
    if (uiTabPlayerInfoSubTab == 0) {
      k = Utility.rgb2long(220, 220, 220);
    } else {
      l = Utility.rgb2long(220, 220, 220);
    }
    surface.drawBoxAlpha(uiX, uiY, uiWidth / 2, 24, k, 128);
    surface.drawBoxAlpha(uiX + uiWidth / 2, uiY, uiWidth / 2, 24, l, 128);
    surface.drawBoxAlpha(uiX, uiY + 24, uiWidth, uiHeight - 24, Utility.rgb2long(220, 220, 220), 128);
    surface.drawLineHoriz(uiX, uiY + 24, uiWidth, 0);
    surface.drawLineVert(uiX + uiWidth / 2, uiY, 24, 0);
    surface.drawStringCenter("Stats", uiX + uiWidth / 4, uiY + 16, 4, 0);
    surface.drawStringCenter("Quests", uiX + uiWidth / 4 + uiWidth / 2, uiY + 16, 4, 0);
    if (uiTabPlayerInfoSubTab == 0) {
      int i1 = 72;
      int k1 = -1;
      surface.drawstring("Skills", uiX + 5, i1, 3, 0xffff00);
      i1 += 13;
      for (int l1 = 0; l1 < 9; l1++) {
        int i2 = 0xffffff;
        if (super.mouseX > uiX + 3 && super.mouseY >= i1 - 11 && super.mouseY < i1 + 2 && super.mouseX < uiX + 90) {
          i2 = 0xff0000;
          k1 = l1;
        }
        surface.drawstring(skillNameShort[l1] + ":@yel@" + playerStatCurrent[l1] + "/" + playerStatBase[l1],
                           uiX + 5,
                           i1,
                           1,
                           i2
        );
        i2 = 0xffffff;
        if (super.mouseX >= uiX + 90 &&
            super.mouseY >= i1 - 13 - 11 &&
            super.mouseY < (i1 - 13) + 2 &&
            super.mouseX < uiX + 196) {
          i2 = 0xff0000;
          k1 = l1 + 9;
        }
        surface.drawstring(skillNameShort[l1 + 9] + ":@yel@" + playerStatCurrent[l1 + 9] + "/" + playerStatBase[l1 + 9],
                           (uiX + uiWidth / 2) - 5,
                           i1 - 13,
                           1,
                           i2
        );
        i1 += 13;
      }

      surface.drawstring("Quest Points:@yel@" + playerQuestPoints, (uiX + uiWidth / 2) - 5, i1 - 13, 1, 0xffffff);
      i1 += 12;
      surface.drawstring("Fatigue: @yel@" + (statFatigue * 100) / 750 + "%", uiX + 5, i1 - 13, 1, 0xffffff);
      i1 += 8;
      surface.drawstring("Equipment Status", uiX + 5, i1, 3, 0xffff00);
      i1 += 12;
      for (int j2 = 0; j2 < 3; j2++) {
        surface.drawstring(equipmentStatNames[j2] + ":@yel@" + playerStatEquipment[j2], uiX + 5, i1, 1, 0xffffff);
        if (j2 < 2) {
          surface.drawstring(equipmentStatNames[j2 + 3] + ":@yel@" + playerStatEquipment[j2 + 3],
                             uiX + uiWidth / 2 + 25,
                             i1,
                             1,
                             0xffffff
          );
        }
        i1 += 13;
      }

      i1 += 6;
      surface.drawLineHoriz(uiX, i1 - 15, uiWidth, 0);
      if (k1 != -1) {
        surface.drawstring(skillNameLong[k1] + " skill", uiX + 5, i1, 1, 0xffff00);
        i1 += 12;
        int k2 = experienceArray[0];
        for (int i3 = 0; i3 < 98; i3++) {
          if (playerExperience[k1] >= experienceArray[i3]) {
            k2 = experienceArray[i3 + 1];
          }
        }

        surface.drawstring("Total xp: " + playerExperience[k1] / 4, uiX + 5, i1, 1, 0xffffff);
        i1 += 12;
        surface.drawstring("Next level at: " + k2 / 4, uiX + 5, i1, 1, 0xffffff);
      } else {
        surface.drawstring("Overall levels", uiX + 5, i1, 1, 0xffff00);
        i1 += 12;
        int l2 = 0;
        for (int j3 = 0; j3 < playerStatCount; j3++) {
          l2 += playerStatBase[j3];
        }

        surface.drawstring("Skill total: " + l2, uiX + 5, i1, 1, 0xffffff);
        i1 += 12;
        surface.drawstring("Combat level: " + localPlayer.level, uiX + 5, i1, 1, 0xffffff);
        i1 += 12;
      }
    }
    if (uiTabPlayerInfoSubTab == 1) {
      panelQuestList.clearList(controlListQuest);
      panelQuestList.addListEntry(controlListQuest, 0, "@whi@Quest-list (green=completed)");
      for (int j1 = 0; j1 < questCount; j1++) {
        panelQuestList.addListEntry(controlListQuest, j1 + 1, (questComplete[j1] ? "@gre@" : "@red@") + questName[j1]);
      }

      panelQuestList.drawPanel();
    }
    if (!nomenus) {
      return;
    }
    int mouseX = super.mouseX - (surface.width2 - 199);
    int mouseY = super.mouseY - 36;
    if (mouseX >= 0 && mouseY >= 0 && mouseX < uiWidth && mouseY < uiHeight) {
      if (uiTabPlayerInfoSubTab == 1) {
        panelQuestList.handleMouse(mouseX + (surface.width2 - 199),
                                   mouseY + 36,
                                   super.lastMouseButtonDown,
                                   super.mouseButtonDown
        );
      }
      if (mouseY <= 24 && mouseButtonClick == 1) {
        if (mouseX < 98) {
          uiTabPlayerInfoSubTab = 0;
          return;
        }
        if (mouseX > 98) {
          uiTabPlayerInfoSubTab = 1;
        }
      }
    }
  }

  private void createRightClickMenu() {
        /*if(messageTabSelected == 1 && panelMessageTabs.isClicked(controlTextListChat)
                || messageTabSelected == 3 && panelMessageTabs.isClicked(controlTextListPrivate)) {
            int control = messageTabSelected == 1 ? controlTextListChat : controlTextListPrivate;
            int mouseThing = panelMessageTabs.isMouseButtonDown(control);
            if(mouseThing >> 16 == 2 || optionMouseButtonOne && (mouseThing >> 16) == 1) {
                mouseThing = mouseThing & 0xffff;
                String extra1 = panelMessageTabs.getListEntryExtra1(control, mouseThing);
                String extra2 = panelMessageTabs.getListEntryExtra2(control, mouseThing);
                // if(createRightClickMenusSocial(extra1, extra2) {
                return;
                // }
            }
        }

        if(messageTabSelected == 0) {
            for(int i = 0; i < messageShitSize; i++) {
                if(messageHistoryTimeout[i] > 0 && (messageTypes[i] == 4 || messageTypes[i] == 1 || messageTypes[i] == 5 || messageTypes[i] == 6)) {
                    String fullMessage = messageColor[i] + formatMessage(messageMessages[i], messageSenders[i], messageTypes[i]);
                    // createRightClickMenusSocial
                    // bla bla bla no one gives a shit
                }
            }
        }*/

    int i = 2203 - (localRegionY + planeHeight + regionY);
    if (localRegionX + planeWidth + regionX >= 2640) {
      i = -50;
    }
    int j = -1;
    for (int k = 0; k < objectCount; k++) {
      objectAlreadyInMenu[k] = false;
    }

    for (int l = 0; l < wallObjectCount; l++) {
      wallObjectAlreadyInMenu[l] = false;
    }

    int i1 = scene.getMousePickedCount();
    GameModel objs[] = scene.getMousePickedModels();
    int plyrs[] = scene.getMousePickedFaces();
    for (int menuidx = 0; menuidx < i1; menuidx++) {
      if (menuItemsCount > 200) {
        break;
      }
      int pid = plyrs[menuidx];
      GameModel gameModel = objs[menuidx];
      if (gameModel.faceTag[pid] <= 65535 ||
          gameModel.faceTag[pid] >= 200000 && gameModel.faceTag[pid] <= 300000)// 0x30d40    0x493e0
      {
        if (gameModel == scene.view) {
          int idx = gameModel.faceTag[pid] % 10000;
          int type = gameModel.faceTag[pid] / 10000;
          if (type == 1) { // player
            String s = "";
            int k3 = 0;
            if (localPlayer.level > 0 && players[idx].level > 0) {
              k3 = localPlayer.level - players[idx].level;
            }
            if (k3 < 0) {
              s = "@or1@";
            }
            if (k3 < -3) {
              s = "@or2@";
            }
            if (k3 < -6) {
              s = "@or3@";
            }
            if (k3 < -9) {
              s = "@red@";
            }
            if (k3 > 0) {
              s = "@gr1@";
            }
            if (k3 > 3) {
              s = "@gr2@";
            }
            if (k3 > 6) {
              s = "@gr3@";
            }
            if (k3 > 9) {
              s = "@gre@";
            }
            s = " " + s + "(level-" + players[idx].level + ")";
            if (selectedSpell >= 0) {
              if (GameData.spellType[selectedSpell] == 1 || GameData.spellType[selectedSpell] == 2) {
                menuItemText1[menuItemsCount] = "Cast " +
                                                GameData.spellName[selectedSpell] +
                                                " @0x919191@(" +
                                                selectedSpell +
                                                ") @whi@on";
                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                menuItemID[menuItemsCount] = 800;
                menuItemX[menuItemsCount] = players[idx].currentX;
                menuItemY[menuItemsCount] = players[idx].currentY;
                menuSourceType[menuItemsCount] = players[idx].serverIndex;
                menuSourceIndex[menuItemsCount] = selectedSpell;
                menuItemsCount++;
              }
            } else if (selectedItemInventoryIndex >= 0) {
              menuItemText1[menuItemsCount] = "Use " +
                                              selectedItemName +
                                              " @0x919191@(" +
                                              inventoryItemId[selectedItemInventoryIndex] +
                                              ") @whi@with";
              menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
              menuItemID[menuItemsCount] = 810;
              menuItemX[menuItemsCount] = players[idx].currentX;
              menuItemY[menuItemsCount] = players[idx].currentY;
              menuSourceType[menuItemsCount] = players[idx].serverIndex;
              menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
              menuItemsCount++;
            } else {
              if (i > 0 && (players[idx].currentY - 64) / magicLoc + planeHeight + regionY < 2203) {
                menuItemText1[menuItemsCount] = "Attack";
                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                if (k3 >= 0 && k3 < 5) {
                  menuItemID[menuItemsCount] = 805;
                } else {
                  menuItemID[menuItemsCount] = 2805;
                }
                menuItemX[menuItemsCount] = players[idx].currentX;
                menuItemY[menuItemsCount] = players[idx].currentY;
                menuSourceType[menuItemsCount] = players[idx].serverIndex;
                menuItemsCount++;
              } else if (members) {
                menuItemText1[menuItemsCount] = "Duel with";
                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                menuItemX[menuItemsCount] = players[idx].currentX;
                menuItemY[menuItemsCount] = players[idx].currentY;
                menuItemID[menuItemsCount] = 2806;
                menuSourceType[menuItemsCount] = players[idx].serverIndex;
                menuItemsCount++;
              }
              menuItemText1[menuItemsCount] = "Trade with";
              menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
              menuItemID[menuItemsCount] = 2810;
              menuSourceType[menuItemsCount] = players[idx].serverIndex;
              menuItemsCount++;
              menuItemText1[menuItemsCount] = "Follow";
              menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
              menuItemID[menuItemsCount] = 2820;
              menuSourceType[menuItemsCount] = players[idx].serverIndex;
              menuItemsCount++;

              if (Version.CLIENT > 204) {
                boolean isFriend = false, isOnline = false;
                for (int fidx = 0; fidx < friendListCount; fidx++) {
                  if (players[idx].name.equals(friendListNames[fidx])) {
                    isFriend = true;
                    if ((friendListOnline[fidx] & 4) != 0) {
                      isOnline = true;
                    }
                    break;
                  }
                }
                boolean isIgnored = false;
                for (int iidx = 0; iidx < ignoreListCount; iidx++) {
                  if (players[idx].name.equals(ignoreListNames[iidx])) {
                    isIgnored = true;
                    break;
                  }
                }

                if (isFriend && isOnline) {
                  menuItemText1[menuItemsCount] = "Message";
                  menuItemText2[menuItemsCount] = "@whi@" + players[idx].name;
                  menuItemID[menuItemsCount] = 2830;
                  menuTextVar[menuItemsCount] = players[idx].name;
                  menuItemsCount++;
                } else if (!isFriend && !isIgnored) {
                  menuItemText1[menuItemsCount] = "Add friend";
                  menuItemText2[menuItemsCount] = "@whi@" + players[idx].name;
                  menuItemID[menuItemsCount] = 2831;
                  menuTextVar[menuItemsCount] = players[idx].name;
                  menuItemsCount++;
                  menuItemText1[menuItemsCount] = "Add ignore";
                  menuItemText2[menuItemsCount] = "@whi@" + players[idx].name;
                  menuItemID[menuItemsCount] = 2832;
                  menuTextVar[menuItemsCount] = players[idx].name;
                  menuItemsCount++;
                }

                menuItemText1[menuItemsCount] = "Report abuse";
                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name;
                menuItemID[menuItemsCount] = 2833;
                menuTextVar[menuItemsCount] = players[idx].accountName;
                menuItemsCount++;
              }
            }
          } else if (type == 2) { // ground item
            if (selectedSpell >= 0) {
              if (GameData.spellType[selectedSpell] == 3) {
                menuItemText1[menuItemsCount] = "Cast " +
                                                GameData.spellName[selectedSpell] +
                                                " @0x919191@(" +
                                                selectedSpell +
                                                ") @whi@on";
                menuItemText2[menuItemsCount] = "@lre@" +
                                                GameData.itemName[groundItemId[idx]] +
                                                " @0x919191@(" +
                                                groundItemId[idx] +
                                                ")";
                menuItemID[menuItemsCount] = 200;
                menuItemX[menuItemsCount] = groundItemX[idx];
                menuItemY[menuItemsCount] = groundItemY[idx];
                menuSourceType[menuItemsCount] = groundItemId[idx];
                menuSourceIndex[menuItemsCount] = selectedSpell;
                menuItemsCount++;
              }
            } else if (selectedItemInventoryIndex >= 0) {
              menuItemText1[menuItemsCount] = "Use " +
                                              selectedItemName +
                                              " @0x919191@(" +
                                              inventoryItemId[selectedItemInventoryIndex] +
                                              ") @whi@with";
              menuItemText2[menuItemsCount] = "@lre@" +
                                              GameData.itemName[groundItemId[idx]] +
                                              " @0x919191@(" +
                                              groundItemId[idx] +
                                              ")";
              menuItemID[menuItemsCount] = 210;
              menuItemX[menuItemsCount] = groundItemX[idx];
              menuItemY[menuItemsCount] = groundItemY[idx];
              menuSourceType[menuItemsCount] = groundItemId[idx];
              menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
              menuItemsCount++;
            } else {
              menuItemText1[menuItemsCount] = "Take";
              menuItemText2[menuItemsCount] = "@lre@" +
                                              GameData.itemName[groundItemId[idx]] +
                                              " @0x919191@(" +
                                              groundItemId[idx] +
                                              ")";
              menuItemID[menuItemsCount] = 220;
              menuItemX[menuItemsCount] = groundItemX[idx];
              menuItemY[menuItemsCount] = groundItemY[idx];
              menuSourceType[menuItemsCount] = groundItemId[idx];
              menuItemsCount++;
              menuItemText1[menuItemsCount] = "Examine";
              menuItemText2[menuItemsCount] = "@lre@" + GameData.itemName[groundItemId[idx]];
              menuItemID[menuItemsCount] = 3200;
              menuSourceType[menuItemsCount] = groundItemId[idx];
              menuItemsCount++;
            }
          } else if (type == 3) { // npc
            String s1 = "";
            int leveldiff = -1;
            int id = npcs[idx].npcId;
            if (GameData.npcAttackable[id] > 0) {
              int npclevel = (GameData.npcAttack[id] +
                              GameData.npcDefense[id] +
                              GameData.npcStrength[id] +
                              GameData.npcHits[id]) / 4;
              int playerlevel = (playerStatBase[0] + playerStatBase[1] + playerStatBase[2] + playerStatBase[3] + 27) /
                                4;
              leveldiff = playerlevel - npclevel;
              s1 = "@yel@";
              if (leveldiff < 0) {
                s1 = "@or1@";
              }
              if (leveldiff < -3) {
                s1 = "@or2@";
              }
              if (leveldiff < -6) {
                s1 = "@or3@";
              }
              if (leveldiff < -9) {
                s1 = "@red@";
              }
              if (leveldiff > 0) {
                s1 = "@gr1@";
              }
              if (leveldiff > 3) {
                s1 = "@gr2@";
              }
              if (leveldiff > 6) {
                s1 = "@gr3@";
              }
              if (leveldiff > 9) {
                s1 = "@gre@";
              }
              s1 = " " + s1 + "(level-" + npclevel + ")";
            }
            if (selectedSpell >= 0) {
              if (GameData.spellType[selectedSpell] == 2) {
                menuItemText1[menuItemsCount] = "Cast " +
                                                GameData.spellName[selectedSpell] +
                                                " @0x919191@(" +
                                                selectedSpell +
                                                ") @whi@on";
                menuItemText2[menuItemsCount] = "@yel@" +
                                                GameData.npcName[npcs[idx].npcId] +
                                                " @0x919191@(" +
                                                npcs[idx].npcId +
                                                ")";
                menuItemID[menuItemsCount] = 700;
                menuItemX[menuItemsCount] = npcs[idx].currentX;
                menuItemY[menuItemsCount] = npcs[idx].currentY;
                menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
                menuSourceIndex[menuItemsCount] = selectedSpell;
                menuItemsCount++;
              }
            } else if (selectedItemInventoryIndex >= 0) {
              menuItemText1[menuItemsCount] = "Use " +
                                              selectedItemName +
                                              " @0x919191@(" +
                                              inventoryItemId[selectedItemInventoryIndex] +
                                              ") @whi@with";
              menuItemText2[menuItemsCount] = "@yel@" +
                                              GameData.npcName[npcs[idx].npcId] +
                                              " @0x919191@(" +
                                              npcs[idx].npcId +
                                              ")";
              menuItemID[menuItemsCount] = 710;
              menuItemX[menuItemsCount] = npcs[idx].currentX;
              menuItemY[menuItemsCount] = npcs[idx].currentY;
              menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
              menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
              menuItemsCount++;
            } else {
              if (GameData.npcAttackable[id] > 0) {
                menuItemText1[menuItemsCount] = "Attack";
                menuItemText2[menuItemsCount] = "@yel@" +
                                                GameData.npcName[npcs[idx].npcId] +
                                                " @0x919191@(" +
                                                npcs[idx].npcId +
                                                ")" +
                                                s1;
                if (leveldiff >= 0) {
                  menuItemID[menuItemsCount] = 715;
                } else {
                  menuItemID[menuItemsCount] = 2715;
                }
                menuItemX[menuItemsCount] = npcs[idx].currentX;
                menuItemY[menuItemsCount] = npcs[idx].currentY;
                menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
                menuItemsCount++;
              }
              menuItemText1[menuItemsCount] = "Talk-to";
              menuItemText2[menuItemsCount] = "@yel@" + GameData.npcName[npcs[idx].npcId];
              if (GameData.npcAttackable[id] <= 0) {
                menuItemText2[menuItemsCount] += " @0x919191@(" + npcs[idx].npcId + ")";
              }
              menuItemID[menuItemsCount] = 720;
              menuItemX[menuItemsCount] = npcs[idx].currentX;
              menuItemY[menuItemsCount] = npcs[idx].currentY;
              menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
              menuItemsCount++;
              if (!GameData.npcCommand[id].equals("")) {
                menuItemText1[menuItemsCount] = GameData.npcCommand[id];
                menuItemText2[menuItemsCount] = "@yel@" + GameData.npcName[npcs[idx].npcId];
                menuItemID[menuItemsCount] = 725;
                menuItemX[menuItemsCount] = npcs[idx].currentX;
                menuItemY[menuItemsCount] = npcs[idx].currentY;
                menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
                menuItemsCount++;
              }
              menuItemText1[menuItemsCount] = "Examine";
              menuItemText2[menuItemsCount] = "@yel@" + GameData.npcName[npcs[idx].npcId];
              menuItemID[menuItemsCount] = 3700;
              menuSourceType[menuItemsCount] = npcs[idx].npcId;
              menuItemsCount++;
            }
          }
        } else if (gameModel != null && gameModel.key >= 10000) {
          int idx = gameModel.key - 10000;
          int id = wallObjectId[idx];
          if (!wallObjectAlreadyInMenu[idx]) {
            if (selectedSpell >= 0) {
              if (GameData.spellType[selectedSpell] == 4) {
                menuItemText1[menuItemsCount] = "Cast " +
                                                GameData.spellName[selectedSpell] +
                                                " @0x919191@(" +
                                                selectedSpell +
                                                ") @whi@on";
                menuItemText2[menuItemsCount] = "@cya@" + GameData.wallObjectName[id] + " @0x919191@(" + id + ")";
                menuItemID[menuItemsCount] = 300;
                menuItemX[menuItemsCount] = wallObjectX[idx];
                menuItemY[menuItemsCount] = wallObjectY[idx];
                menuSourceType[menuItemsCount] = wallObjectDirection[idx];
                menuSourceIndex[menuItemsCount] = selectedSpell;
                menuItemsCount++;
              }
            } else if (selectedItemInventoryIndex >= 0) {
              menuItemText1[menuItemsCount] = "Use " +
                                              selectedItemName +
                                              " @0x919191@(" +
                                              inventoryItemId[selectedItemInventoryIndex] +
                                              ") @whi@with";
              menuItemText2[menuItemsCount] = "@cya@" + GameData.wallObjectName[id] + " @0x919191@(" + id + ")";
              menuItemID[menuItemsCount] = 310;
              menuItemX[menuItemsCount] = wallObjectX[idx];
              menuItemY[menuItemsCount] = wallObjectY[idx];
              menuSourceType[menuItemsCount] = wallObjectDirection[idx];
              menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
              menuItemsCount++;
            } else {
              if (!GameData.wallObjectCommand1[id].equalsIgnoreCase("WalkTo")) {
                menuItemText1[menuItemsCount] = GameData.wallObjectCommand1[id];
                menuItemText2[menuItemsCount] = "@cya@" + GameData.wallObjectName[id] + " @0x919191@(" + id + ")";
                menuItemID[menuItemsCount] = 320;
                menuItemX[menuItemsCount] = wallObjectX[idx];
                menuItemY[menuItemsCount] = wallObjectY[idx];
                menuSourceType[menuItemsCount] = wallObjectDirection[idx];
                menuItemsCount++;
              }
              if (!GameData.wallObjectCommand2[id].equalsIgnoreCase("Examine")) {
                menuItemText1[menuItemsCount] = GameData.wallObjectCommand2[id];
                menuItemText2[menuItemsCount] = "@cya@" + GameData.wallObjectName[id];
                if (GameData.wallObjectCommand1[id].equalsIgnoreCase("WalkTo")) {
                  menuItemText2[menuItemsCount] += " @0x919191@(" + id + ")";
                }
                menuItemID[menuItemsCount] = 2300;
                menuItemX[menuItemsCount] = wallObjectX[idx];
                menuItemY[menuItemsCount] = wallObjectY[idx];
                menuSourceType[menuItemsCount] = wallObjectDirection[idx];
                menuItemsCount++;
              }
              menuItemText1[menuItemsCount] = "Examine";
              menuItemText2[menuItemsCount] = "@cya@" + GameData.wallObjectName[id];
              if (GameData.wallObjectCommand1[id].equalsIgnoreCase("WalkTo") &&
                  GameData.wallObjectCommand2[id].equalsIgnoreCase("Examine")) {
                menuItemText2[menuItemsCount] += " @0x919191@(" + id + ")";
              }
              menuItemID[menuItemsCount] = 3300;
              menuSourceType[menuItemsCount] = id;
              menuItemsCount++;
            }
            wallObjectAlreadyInMenu[idx] = true;
          }
        } else if (gameModel != null && gameModel.key >= 0) {
          int idx = gameModel.key;
          int id = objectId[idx];
          if (!objectAlreadyInMenu[idx]) {
            if (selectedSpell >= 0) {
              if (GameData.spellType[selectedSpell] == 5) {
                menuItemText1[menuItemsCount] = "Cast " +
                                                GameData.spellName[selectedSpell] +
                                                " @0x919191@(" +
                                                selectedSpell +
                                                ") @whi@on";
                menuItemText2[menuItemsCount] = "@cya@" + GameData.objectName[id] + " @0x919191@(" + id + ")";
                menuItemID[menuItemsCount] = 400;
                menuItemX[menuItemsCount] = objectX[idx];
                menuItemY[menuItemsCount] = objectY[idx];
                menuSourceType[menuItemsCount] = objectDirection[idx];
                menuSourceIndex[menuItemsCount] = objectId[idx];
                menuTargetIndex[menuItemsCount] = selectedSpell;
                menuItemsCount++;
              }
            } else if (selectedItemInventoryIndex >= 0) {
              menuItemText1[menuItemsCount] = "Use " +
                                              selectedItemName +
                                              " @0x919191@(" +
                                              inventoryItemId[selectedItemInventoryIndex] +
                                              ") @whi@with";
              menuItemText2[menuItemsCount] = "@cya@" + GameData.objectName[id] + " @0x919191@(" + id + ")";
              menuItemID[menuItemsCount] = 410;
              menuItemX[menuItemsCount] = objectX[idx];
              menuItemY[menuItemsCount] = objectY[idx];
              menuSourceType[menuItemsCount] = objectDirection[idx];
              menuSourceIndex[menuItemsCount] = objectId[idx];
              menuTargetIndex[menuItemsCount] = selectedItemInventoryIndex;
              menuItemsCount++;
            } else {
              if (!GameData.objectCommand1[id].equalsIgnoreCase("WalkTo")) {
                menuItemText1[menuItemsCount] = GameData.objectCommand1[id];
                menuItemText2[menuItemsCount] = "@cya@" + GameData.objectName[id] + " @0x919191@(" + id + ")";
                menuItemID[menuItemsCount] = 420;
                menuItemX[menuItemsCount] = objectX[idx];
                menuItemY[menuItemsCount] = objectY[idx];
                menuSourceType[menuItemsCount] = objectDirection[idx];
                menuSourceIndex[menuItemsCount] = objectId[idx];
                menuItemsCount++;
              }
              if (!GameData.objectCommand2[id].equalsIgnoreCase("Examine")) {
                menuItemText1[menuItemsCount] = GameData.objectCommand2[id];
                menuItemText2[menuItemsCount] = "@cya@" + GameData.objectName[id];
                if (GameData.objectCommand1[id].equalsIgnoreCase("WalkTo")) {
                  menuItemText2[menuItemsCount] += " @0x919191@(" + id + ")";
                }
                menuItemID[menuItemsCount] = 2400;
                menuItemX[menuItemsCount] = objectX[idx];
                menuItemY[menuItemsCount] = objectY[idx];
                menuSourceType[menuItemsCount] = objectDirection[idx];
                menuSourceIndex[menuItemsCount] = objectId[idx];
                menuItemsCount++;
              }
              menuItemText1[menuItemsCount] = "Examine";
              menuItemText2[menuItemsCount] = "@cya@" + GameData.objectName[id];
              if (GameData.objectCommand1[id].equalsIgnoreCase("WalkTo") &&
                  GameData.objectCommand2[id].equalsIgnoreCase("Examine")) {
                menuItemText2[menuItemsCount] += " @0x919191@(" + id + ")";
              }
              menuItemID[menuItemsCount] = 3400;
              menuSourceType[menuItemsCount] = id;
              menuItemsCount++;
            }
            objectAlreadyInMenu[idx] = true;
          }
        } else {
          if (pid >= 0) {
            pid = gameModel.faceTag[pid] - 200000;
          }
          if (pid >= 0) {
            j = pid;
          }
        }
      }
    }

    if (selectedSpell >= 0 && GameData.spellType[selectedSpell] <= 1) {
      menuItemText1[menuItemsCount] = "Cast " +
                                      GameData.spellName[selectedSpell] +
                                      " @0x919191@(" +
                                      selectedSpell +
                                      ") @whi@on self";
      menuItemText2[menuItemsCount] = "";
      menuItemID[menuItemsCount] = 1000;
      menuSourceType[menuItemsCount] = selectedSpell;
      menuItemsCount++;
    }
    if (j != -1) {
      if (selectedSpell >= 0) {
        if (GameData.spellType[selectedSpell] == 6) {
          menuItemText1[menuItemsCount] = "Cast " +
                                          GameData.spellName[selectedSpell] +
                                          " @0x919191@(" +
                                          selectedSpell +
                                          ") @whi@on ground";
          menuItemText2[menuItemsCount] = "";
          menuItemID[menuItemsCount] = 900;
          menuItemX[menuItemsCount] = world.localX[j];
          menuItemY[menuItemsCount] = world.localY[j];
          menuSourceType[menuItemsCount] = selectedSpell;
          menuItemsCount++;
          return;
        }
      } else if (selectedItemInventoryIndex < 0) {
        menuItemText1[menuItemsCount] = "Walk here @0x919191@(" +
                                        (regionX + world.localX[j]) +
                                        ", " +
                                        (regionY + world.localY[j]) +
                                        ")";
        menuItemText2[menuItemsCount] = "";
        menuItemID[menuItemsCount] = 920;
        menuItemX[menuItemsCount] = world.localX[j];
        menuItemY[menuItemsCount] = world.localY[j];
        menuItemsCount++;
      }
    }
  }

  protected void startThread(Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.setDaemon(true);
    thread.start();
  }

  private GameModel createModel(int x, int y, int direction, int id, int count) {
    int x1 = x;
    int y1 = y;
    int x2 = x;
    int y2 = y;
    int j2 = GameData.wallObjectTextureFront[id];
    int k2 = GameData.wallObjectTextureBack[id];
    int l2 = GameData.wallObjectHeight[id];
    GameModel gameModel = new GameModel(4, 1);
    if (direction == 0) {
      x2 = x + 1;
    }
    if (direction == 1) {
      y2 = y + 1;
    }
    if (direction == 2) {
      x1 = x + 1;
      y2 = y + 1;
    }
    if (direction == 3) {
      x2 = x + 1;
      y2 = y + 1;
    }
    x1 *= magicLoc;
    y1 *= magicLoc;
    x2 *= magicLoc;
    y2 *= magicLoc;
    int i3 = gameModel.vertexAt(x1, -world.getElevation(x1, y1), y1);
    int j3 = gameModel.vertexAt(x1, -world.getElevation(x1, y1) - l2, y1);
    int k3 = gameModel.vertexAt(x2, -world.getElevation(x2, y2) - l2, y2);
    int l3 = gameModel.vertexAt(x2, -world.getElevation(x2, y2), y2);
    int ai[] = {
      i3, j3, k3, l3
    };
    gameModel.createFace(4, ai, j2, k2);
    gameModel.setLight(false, 60, 24, -50, -10, -50);
    if (x >= 0 && y >= 0 && x < 96 && y < 96) {
      scene.addModel(gameModel);
    }
    gameModel.key = count + 10000;
    return gameModel;
  }

  protected void startGame() {
    if (appletMode) {
      String s = getDocumentBase().getHost().toLowerCase();
      if (!s.endsWith("jagex.com") &&
          !s.endsWith("jagex.co.uk") &&
          !s.endsWith("runescape.com") &&
          !s.endsWith("runescape.co.uk") &&
          !s.endsWith("runescape.net") &&
          !s.endsWith("runescape.org") &&
          !s.endsWith("penguin") &&
          !s.endsWith("puffin")) {
        errorLoadingCodebase = true;
        return;
      }
    }
    int total_exp = 0;
    for (int level = 0; level < 99; level++) {
      int level_1 = level + 1;
      int exp = (int) ((double) level_1 + 300D * Math.pow(2D, (double) level_1 / 7D));
      total_exp += exp;
      experienceArray[level] = total_exp & 0xffffffc;
    }

    try {
      String s1 = getParameter("referid");
      referid = Integer.parseInt(s1);
    } catch (Exception Ex) {
    }
    try {
      String s2 = getParameter("member");
      int j1 = Integer.parseInt(s2);
      if (j1 == 1) {
        members = true;
      }
    } catch (Exception Ex) {
    }
    if (appletMode) {
      super.port = 43594;
    }
    maxReadTries = 1000;
    clientVersion = Version.CLIENT;
    try {
      String s3 = getParameter("poff");
      int k1 = Integer.parseInt(s3);
      super.port += k1;
      System.out.println("Offset: " + k1);
    } catch (Exception Ex) {
    }
    loadGameConfig();
    if (errorLoadingData) {
      return;
    }
    spriteMedia = 2000;
    spriteUtil = spriteMedia + 100;
    spriteItem = spriteUtil + 50;
    spriteLogo = spriteItem + 1000;
    spriteProjectile = spriteLogo + 10;
    spriteTexture = spriteProjectile + 50;
    if (Version.CLIENT > 204) {
      spriteCrowns = spriteTexture + 10;
      spriteTextureWorld = spriteCrowns + 5;
    } else {
      spriteTextureWorld = spriteTexture + 10;
      spriteCrowns = spriteTextureWorld + 5;
    }
    graphics = getGraphics();
    setTargetFps(50);
    surface = new SurfaceSprite(gameWidth, gameHeight + 12, 4000, this);
    surface.mudclientref = this;
    surface.setBounds(0, 0, gameWidth, gameHeight + 12);
    Panel.drawBackgroundArrow = false;
    Panel.baseSpriteStart = spriteUtil;
    panelMagic = new Panel(surface, 5);
    int x = surface.width2 - 199;
    byte y = 36;
    controlListMagic = panelMagic.addTextListInteractive(x, y + 24, 196, 90, 1, 500, true);
    panelSocialList = new Panel(surface, 5);
    controlListSocialPlayers = panelSocialList.addTextListInteractive(x, y + 40, 196, 126, 1, 500, true);
    panelQuestList = new Panel(surface, 5);
    controlListQuest = panelQuestList.addTextListInteractive(x, y + 24, 196, 251, 1, 500, true);
    loadMedia();
    if (errorLoadingData) {
      return;
    }
    loadEntities();
    if (errorLoadingData) {
      return;
    }
    scene = new Scene(surface, 15000, 15000, 1000);
    scene.setBounds(gameWidth / 2, gameHeight / 2, gameWidth / 2, gameHeight / 2, gameWidth, const_9);
    scene.clipFar3d = zoomControls ? 19999 : 2400;
    scene.clipFar2d = zoomControls ? 19999 : 2400;
    scene.fogZFalloff = 1;
    scene.fogZDistance = zoomControls ? 19999 : 2300;
    scene.setLight(-50, -10, -50);
    world = new World(scene, surface);
    world.baseMediaSprite = spriteMedia;
    loadTextures();
    if (errorLoadingData) {
      return;
    }
    loadModels();
    if (errorLoadingData) {
      return;
    }
    loadMaps();
    if (errorLoadingData) {
      return;
    }
    if (members) {
      loadSounds();
    }
    if (!errorLoadingData) {
      showLoadingProgress(100, "Starting game...");
      createMessageTabPanel();
      createLoginPanels();
      createAppearancePanel();
      resetLoginScreenVariables();
      renderLoginScreenViewports();
    }
  }

  public URL getDocumentBase() {
    return super.getDocumentBase();
  }

  public String getParameter(String s) {
    return super.getParameter(s);
  }

  private void loadGameConfig() {
    byte buff[] = readDataFile("config" + Version.CONFIG + ".jag", "Configuration", 10);
    if (buff == null) {
      errorLoadingData = true;
      return;
    }
    GameData.loadData(buff, members);
    if (Version.CLIENT <= 204) {
      buff = readDataFile("filter" + Version.FILTER + ".jag", "Chat system", 15);
      if (buff == null) {
        errorLoadingData = true;
        return;
      } else {
        byte buffragments[] = Utility.loadData("fragmentsenc.txt", 0, buff);
        byte buffbandenc[] = Utility.loadData("badenc.txt", 0, buff);
        byte buffhostenc[] = Utility.loadData("hostenc.txt", 0, buff);
        byte bufftldlist[] = Utility.loadData("tldlist.txt", 0, buff);
        WordFilter.loadFilters(new Buffer(buffragments),
                               new Buffer(buffbandenc),
                               new Buffer(buffhostenc),
                               new Buffer(bufftldlist)
        );
        return;
      }
    }
  }

  public Graphics getGraphics() {
    if (gameFrame != null) {
      return gameFrame.getGraphics();
    } else {
      return super.getGraphics();
    }
  }

  private void loadMedia() {
    byte media[] = readDataFile("media" + Version.MEDIA + ".jag", "2d graphics", 20);
    if (media == null) {
      errorLoadingData = true;
      return;
    }
    byte buff[] = Utility.loadData("index.dat", 0, media);
    surface.parseSprite(spriteMedia, Utility.loadData("inv1.dat", 0, media), buff, 1);
    surface.parseSprite(spriteMedia + 1, Utility.loadData("inv2.dat", 0, media), buff, 6);
    surface.parseSprite(spriteMedia + 9, Utility.loadData("bubble.dat", 0, media), buff, 1);
    surface.parseSprite(spriteMedia + 10, Utility.loadData("runescape.dat", 0, media), buff, 1);
    surface.parseSprite(spriteMedia + 11, Utility.loadData("splat.dat", 0, media), buff, 3);
    surface.parseSprite(spriteMedia + 14, Utility.loadData("icon.dat", 0, media), buff, 8);
    surface.parseSprite(spriteMedia + 22, Utility.loadData("hbar.dat", 0, media), buff, 1);
    surface.parseSprite(spriteMedia + 23, Utility.loadData("hbar2.dat", 0, media), buff, 1);
    surface.parseSprite(spriteMedia + 24, Utility.loadData("compass.dat", 0, media), buff, 1);
    surface.parseSprite(spriteMedia + 25, Utility.loadData("buttons.dat", 0, media), buff, 2);
    surface.parseSprite(spriteUtil, Utility.loadData("scrollbar.dat", 0, media), buff, 2);
    surface.parseSprite(spriteUtil + 2, Utility.loadData("corners.dat", 0, media), buff, 4);
    surface.parseSprite(spriteUtil + 6, Utility.loadData("arrows.dat", 0, media), buff, 2);
    surface.parseSprite(spriteProjectile,
                        Utility.loadData("projectile.dat", 0, media),
                        buff,
                        GameData.projectileSprite
    );
    if (Version.CLIENT > 204) {
      surface.parseSprite(spriteCrowns, Utility.loadData("crowns.dat", 0, media), buff, 2);
      surface.setCrownStartId(spriteCrowns);
    }
    int i = GameData.itemSpriteCount;
    for (int j = 1; i > 0; j++) {
      int k = i;
      i -= 30;
      if (k > 30) {
        k = 30;
      }
      surface.parseSprite(spriteItem + (j - 1) * 30, Utility.loadData("objects" + j + ".dat", 0, media), buff, k);
    }

    surface.loadSprite(spriteMedia);
    surface.loadSprite(spriteMedia + 9);
    for (int l = 11; l <= 26; l++) {
      surface.loadSprite(spriteMedia + l);
    }

    for (int i1 = 0; i1 < GameData.projectileSprite; i1++) {
      surface.loadSprite(spriteProjectile + i1);
    }

    for (int j1 = 0; j1 < GameData.itemSpriteCount; j1++) {
      surface.loadSprite(spriteItem + j1);
    }

  }

  private void loadEntities() {
    byte entityBuff[] = null;
    byte indexDat[] = null;
    entityBuff = readDataFile("entity" + Version.ENTITY + ".jag", "people and monsters", 30);
    if (entityBuff == null) {
      errorLoadingData = true;
      return;
    }
    indexDat = Utility.loadData("index.dat", 0, entityBuff);
    byte entityBuffMem[] = null;
    byte indexDatMem[] = null;
    if (members) {
      entityBuffMem = readDataFile("entity" + Version.ENTITY + ".mem", "member graphics", 45);
      if (entityBuffMem == null) {
        errorLoadingData = true;
        return;
      }
      indexDatMem = Utility.loadData("index.dat", 0, entityBuffMem);
    }
    int frameCount = 0;
    anInt659 = 0;
    anInt660 = anInt659;
    label0:
    for (int j = 0; j < GameData.animationCount; j++) {
      String animName = GameData.animationName[j];
      for (int k = 0; k < j; k++) {
        if (!GameData.animationName[k].equalsIgnoreCase(animName)) {
          continue;
        }
        GameData.animationNumber[j] = GameData.animationNumber[k];
        continue label0;
      }

      byte dat[] = Utility.loadData(animName + ".dat", 0, entityBuff);
      byte indexData[] = indexDat;
      if (dat == null && members) {
        dat = Utility.loadData(animName + ".dat", 0, entityBuffMem);
        indexData = indexDatMem;
      }
      if (dat != null) {
        // TODO something fishy going on here in 233
        surface.parseSprite(anInt660, dat, indexData, 15);
        frameCount += 15;
        if (GameData.animationHasA[j] == 1) {
          byte datA[] = Utility.loadData(animName + "a.dat", 0, entityBuff);
          byte aIndexDat[] = indexDat;
          if (datA == null && members) {
            datA = Utility.loadData(animName + "a.dat", 0, entityBuffMem);
            aIndexDat = indexDatMem;
          }
          surface.parseSprite(anInt660 + 15, datA, aIndexDat, 3);
          frameCount += 3;
        }
        if (GameData.animationHasF[j] == 1) {
          byte datF[] = Utility.loadData(animName + "f.dat", 0, entityBuff);
          byte fDatIndex[] = indexDat;
          if (datF == null && members) {
            datF = Utility.loadData(animName + "f.dat", 0, entityBuffMem);
            fDatIndex = indexDatMem;
          }
          surface.parseSprite(anInt660 + 18, datF, fDatIndex, 9);
          frameCount += 9;
        }
        if (GameData.animationSomething[j] != 0) {
          for (int l = anInt660; l < anInt660 + 27; l++) {
            surface.loadSprite(l);
          }

        }
      }
      GameData.animationNumber[j] = anInt660;
      anInt660 += 27;
    }

    //System.out.println("Loaded: " + frameCount + " frames of animation");
  }

  private void loadTextures() {
    byte buffTextures[] = readDataFile("textures" + Version.TEXTURES + ".jag", "Textures", 50);
    if (buffTextures == null) {
      errorLoadingData = true;
      return;
    }
    byte buffIndex[] = Utility.loadData("index.dat", 0, buffTextures);
    scene.allocateTextures(GameData.textureCount, 7, 11);
    for (int i = 0; i < GameData.textureCount; i++) {
      String name = GameData.textureName[i];
      byte buff1[] = Utility.loadData(name + ".dat", 0, buffTextures);
      surface.parseSprite(spriteTexture, buff1, buffIndex, 1);
      surface.drawBox(0, 0, 128, 128, 0xff00ff);
      surface.drawSprite(0, 0, spriteTexture);
      int wh = surface.spriteWidthFull[spriteTexture];
      String nameSub = GameData.textureSubtypeName[i];
      if (nameSub != null && nameSub.length() > 0) {
        byte buff2[] = Utility.loadData(nameSub + ".dat", 0, buffTextures);
        surface.parseSprite(spriteTexture, buff2, buffIndex, 1);
        surface.drawSprite(0, 0, spriteTexture);
      }
      surface.drawSprite(spriteTextureWorld + i, 0, 0, wh, wh);
      int area = wh * wh;
      for (int j = 0; j < area; j++) {
        if (surface.spritePixels[spriteTextureWorld + i][j] == 65280) {
          surface.spritePixels[spriteTextureWorld + i][j] = 0xff00ff;
        }
      }

      surface.drawWorld(spriteTextureWorld + i);
      scene.defineTexture(i,
                          surface.spriteColoursUsed[spriteTextureWorld + i],
                          surface.spriteColourList[spriteTextureWorld + i],
                          wh / 64 - 1
      );
    }

  }

  private void loadModels() {
    GameData.getModelIndex("torcha2");
    GameData.getModelIndex("torcha3");
    GameData.getModelIndex("torcha4");
    GameData.getModelIndex("skulltorcha2");
    GameData.getModelIndex("skulltorcha3");
    GameData.getModelIndex("skulltorcha4");
    GameData.getModelIndex("firea2");
    GameData.getModelIndex("firea3");
    GameData.getModelIndex("fireplacea2");
    GameData.getModelIndex("fireplacea3");
    GameData.getModelIndex("firespell2");
    GameData.getModelIndex("firespell3");
    GameData.getModelIndex("lightning2");
    GameData.getModelIndex("lightning3");
    GameData.getModelIndex("clawspell2");
    GameData.getModelIndex("clawspell3");
    GameData.getModelIndex("clawspell4");
    GameData.getModelIndex("clawspell5");
    GameData.getModelIndex("spellcharge2");
    GameData.getModelIndex("spellcharge3");
    //if (getStartedAsApplet()) { // always show models on loading screen viewports
    if (true) {
      byte abyte0[] = readDataFile("models" + Version.MODELS + ".jag", "3d models", 60);
      if (abyte0 == null) {
        errorLoadingData = true;
        return;
      }
      for (int j = 0; j < GameData.modelCount; j++) {
        int k = Utility.getDataFileOffset(GameData.modelName[j] + ".ob3", abyte0);
        if (k != 0) {
          gameModels[j] = new GameModel(abyte0, k, true);
        } else {
          gameModels[j] = new GameModel(1, 1);
        }
        if (GameData.modelName[j].equals("giantcrystal")) {
          gameModels[j].transparent = true;
        }
      }

      return;
    }
    showLoadingProgress(70, "Loading 3d models");
    for (int i = 0; i < GameData.modelCount; i++) {
      gameModels[i] = new GameModel("../gamedata/models/" + GameData.modelName[i] + ".ob2");
      if (GameData.modelName[i].equals("giantcrystal")) {
        gameModels[i].transparent = true;
      }
    }

  }

  private void loadMaps() {
    if (!useJmFormat) {
      world.mapPack = readDataFile("maps" + Version.MAPS + ".jag", "map", 70);
      if (members) {
        world.memberMapPack = readDataFile("maps" + Version.MAPS + ".mem", "members map", 75);
      }
      world.landscapePack = readDataFile("land" + Version.MAPS + ".jag", "landscape", 80);
      if (members) {
        world.memberLandscapePack = readDataFile("land" + Version.MAPS + ".mem", "members landscape", 85);
      }
    }
  }

  private void loadSounds() {
    try {
      soundData = readDataFile("sounds" + Version.SOUNDS + ".mem", "Sound effects", 90);
      audioPlayer = new StreamAudioPlayer();
      return;
    } catch (Throwable throwable) {
      System.out.println("Unable to init sounds:" + throwable);
    }
  }

  private void createMessageTabPanel() {
    panelMessageTabs = new Panel(surface, 10);
    controlTextListChat = panelMessageTabs.addTextList(5, gameHeight - 121, gameWidth - 10, 112, 1, 20, true);
    controlTextListAll = panelMessageTabs.addTextListInput(7, gameHeight - 10, gameWidth - 14, 14, 1, 80, false, true);
    controlTextListQuest = panelMessageTabs.addTextList(5, gameHeight - 121, gameWidth - 10, 112, 1, 20, true);
    controlTextListPrivate = panelMessageTabs.addTextList(5, gameHeight - 121, gameWidth - 10, 112, 1, 20, true);
    panelMessageTabs.setFocus(controlTextListAll);
  }

  private void createLoginPanels() {
    panelLoginWelcome = new Panel(surface, 50);
    int y = gameHeight / 2 - 127;
    int x = gameWidth / 2;
    if (!members) {
      panelLoginWelcome.addText(x, 200 + y, "Click on an option", 5, true);
      panelLoginWelcome.addButtonBackground(x - 100, 240 + y, 120, 35);
      panelLoginWelcome.addButtonBackground(x + 100, 240 + y, 120, 35);
      panelLoginWelcome.addText(x - 100, 240 + y, "New User", 5, false);
      panelLoginWelcome.addText(x + 100, 240 + y, "Existing User", 5, false);
      controlWelcomeNewuser = panelLoginWelcome.addButton(x - 100, 240 + y, 120, 35);
      controlWelcomeExistinguser = panelLoginWelcome.addButton(x + 100, 240 + y, 120, 35);
      panelLoginWelcome.setFocus(controlWelcomeExistinguser);
    } else {
      panelLoginWelcome.addText(x, 200 + y, "Welcome to RuneScape", 4, true);
      panelLoginWelcome.addText(x, 215 + y, "You need a member account to use this server", 4, true);
      panelLoginWelcome.addButtonBackground(x, 250 + y, 200, 35);
      panelLoginWelcome.addText(x, 250 + y, "Click here to login", 5, false);
      controlWelcomeExistinguser = panelLoginWelcome.addButton(x, 250 + y, 200, 35);
      panelLoginWelcome.setFocus(controlWelcomeExistinguser);
    }
    panelLoginNewuser = new Panel(surface, 50);
    y = 230;
    if (referid == 0) {
      panelLoginNewuser.addText(x, y + 8, "To create an account please go back to the", 4, true);
      y += 20;
      panelLoginNewuser.addText(x, y + 8, "www.runescape.com front page, and choose 'create account'", 4, true);
    } else if (referid == 1) {
      panelLoginNewuser.addText(x, y + 8, "To create an account please click on the", 4, true);
      y += 20;
      panelLoginNewuser.addText(x, y + 8, "'create account' link below the game window", 4, true);
    } else {
      panelLoginNewuser.addText(x, y + 8, "To create an account please go back to the", 4, true);
      y += 20;
      panelLoginNewuser.addText(x, y + 8, "runescape front webpage and choose 'create account'", 4, true);
    }
    y += 30;
    panelLoginNewuser.addButtonBackground(x, y + 17, 150, 34);
    panelLoginNewuser.addText(x, y + 17, "Ok", 5, false);
    controlLoginNewOk = panelLoginNewuser.addButton(x, y + 17, 150, 34);
    panelLoginNewuser.setFocus(controlLoginNewOk);
    panelLoginExistinguser = new Panel(surface, 50);
    y = gameHeight / 2 + 63;
    controlLoginStatus = panelLoginExistinguser.addText(x, y - 10, "Please enter your username and password", 4, true);
    y += 28;
    panelLoginExistinguser.addButtonBackground(x - 116, y, 200, 40);
    panelLoginExistinguser.addText(x - 116, y - 10, "Username:", 4, false);
    controlLoginUser = panelLoginExistinguser.addTextInput(x - 116, y + 10, 200, 40, 4, 255, false, false);
    y += 47;
    panelLoginExistinguser.addButtonBackground(x - 66, y, 200, 40);
    panelLoginExistinguser.addText(x - 66, y - 10, "Password:", 4, false);
    controlLoginPass = panelLoginExistinguser.addTextInput(x - 66, y + 10, 200, 40, 4, 20, true, false);
    y -= 55;
    panelLoginExistinguser.addButtonBackground(x + 154, y, 120, 25);
    panelLoginExistinguser.addText(x + 154, y, "Ok", 4, false);
    controlLoginOk = panelLoginExistinguser.addButton(x + 154, y, 120, 25);
    y += 30;
    panelLoginExistinguser.addButtonBackground(x + 154, y, 120, 25);
    panelLoginExistinguser.addText(x + 154, y, "Cancel", 4, false);
    controlLoginCancel = panelLoginExistinguser.addButton(x + 154, y, 120, 25);
    y += 30;
    panelLoginExistinguser.setFocus(controlLoginUser);
  }

  private void createAppearancePanel() {
    panelAppearance = new Panel(surface, 100);
    panelAppearance.addText(256, 10, "Please design Your Character", 4, true);
    int x = 140;
    int y = 34;
    x += 116;
    y -= 10;
    panelAppearance.addText(x - 55, y + 110, "Front", 3, true);
    panelAppearance.addText(x, y + 110, "Side", 3, true);
    panelAppearance.addText(x + 55, y + 110, "Back", 3, true);
    byte xoff = 54;
    y += 145;
    panelAppearance.addBoxRounded(x - xoff, y, 53, 41);
    panelAppearance.addText(x - xoff, y - 8, "Head", 1, true);
    panelAppearance.addText(x - xoff, y + 8, "Type", 1, true);
    panelAppearance.addSprite(x - xoff - 40, y, Panel.baseSpriteStart + 7);
    controlButtonAppearanceHead1 = panelAppearance.addButton(x - xoff - 40, y, 20, 20);
    panelAppearance.addSprite((x - xoff) + 40, y, Panel.baseSpriteStart + 6);
    controlButtonAppearanceHead2 = panelAppearance.addButton((x - xoff) + 40, y, 20, 20);
    panelAppearance.addBoxRounded(x + xoff, y, 53, 41);
    panelAppearance.addText(x + xoff, y - 8, "Hair", 1, true);
    panelAppearance.addText(x + xoff, y + 8, "Color", 1, true);
    panelAppearance.addSprite((x + xoff) - 40, y, Panel.baseSpriteStart + 7);
    controlButtonAppearanceHair1 = panelAppearance.addButton((x + xoff) - 40, y, 20, 20);
    panelAppearance.addSprite(x + xoff + 40, y, Panel.baseSpriteStart + 6);
    controlButtonAppearanceHair2 = panelAppearance.addButton(x + xoff + 40, y, 20, 20);
    y += 50;
    panelAppearance.addBoxRounded(x - xoff, y, 53, 41);
    panelAppearance.addText(x - xoff, y, "Gender", 1, true);
    panelAppearance.addSprite(x - xoff - 40, y, Panel.baseSpriteStart + 7);
    controlButtonAppearanceGender1 = panelAppearance.addButton(x - xoff - 40, y, 20, 20);
    panelAppearance.addSprite((x - xoff) + 40, y, Panel.baseSpriteStart + 6);
    controlButtonAppearanceGender2 = panelAppearance.addButton((x - xoff) + 40, y, 20, 20);
    panelAppearance.addBoxRounded(x + xoff, y, 53, 41);
    panelAppearance.addText(x + xoff, y - 8, "Top", 1, true);
    panelAppearance.addText(x + xoff, y + 8, "Color", 1, true);
    panelAppearance.addSprite((x + xoff) - 40, y, Panel.baseSpriteStart + 7);
    controlButtonAppearanceTop1 = panelAppearance.addButton((x + xoff) - 40, y, 20, 20);
    panelAppearance.addSprite(x + xoff + 40, y, Panel.baseSpriteStart + 6);
    controlButtonAppearanceTop2 = panelAppearance.addButton(x + xoff + 40, y, 20, 20);
    y += 50;
    panelAppearance.addBoxRounded(x - xoff, y, 53, 41);
    panelAppearance.addText(x - xoff, y - 8, "Skin", 1, true);
    panelAppearance.addText(x - xoff, y + 8, "Color", 1, true);
    panelAppearance.addSprite(x - xoff - 40, y, Panel.baseSpriteStart + 7);
    controlButtonAppearanceSkin1 = panelAppearance.addButton(x - xoff - 40, y, 20, 20);
    panelAppearance.addSprite((x - xoff) + 40, y, Panel.baseSpriteStart + 6);
    controlButtonAppearanceSkin2 = panelAppearance.addButton((x - xoff) + 40, y, 20, 20);
    panelAppearance.addBoxRounded(x + xoff, y, 53, 41);
    panelAppearance.addText(x + xoff, y - 8, "Bottom", 1, true);
    panelAppearance.addText(x + xoff, y + 8, "Color", 1, true);
    panelAppearance.addSprite((x + xoff) - 40, y, Panel.baseSpriteStart + 7);
    controlButtonAppearanceBottom1 = panelAppearance.addButton((x + xoff) - 40, y, 20, 20);
    panelAppearance.addSprite(x + xoff + 40, y, Panel.baseSpriteStart + 6);
    controlButtonAppearanceBottom2 = panelAppearance.addButton(x + xoff + 40, y, 20, 20);
    y += 82;
    y -= 35;
    panelAppearance.addButtonBackground(x, y, 200, 30);
    panelAppearance.addText(x, y, "Accept", 4, false);
    controlButtonAppearanceAccept = panelAppearance.addButton(x, y, 200, 30);
  }

  private void resetLoginScreenVariables() {
    loggedIn = 0;
    loginScreen = 0;
    loginUser = "";
    loginPass = "";
    loginUserDesc = "Please enter a username:";
    loginUserDisp = "*" + loginUser + "*";
    playerCount = 0;
    npcCount = 0;
  }

  private void renderLoginScreenViewports() {
    int rh = 0;
    byte rx = 50;//49;
    byte ry = 50;//47;
    world.loadSection(rx * 48 + 23, ry * 48 + 23, rh);
    world.addModels(gameModels);
    int x = 9728;// '\u2600'
    int y = 6400;// '\u1900'
    int zoom = 1100;// '\u044C'
    int rotation = 888;// '\u0378'
    scene.clipFar3d = 4100;
    scene.clipFar2d = 4100;
    scene.fogZFalloff = 1;
    scene.fogZDistance = 4000;
    surface.blackScreen();
    scene.setCamera(x, -world.getElevation(x, y), y, 912, rotation, 0, zoom * 2);
    scene.render();
    surface.fade2black();
    surface.fade2black();
        /*surface.drawBox(0, 0, gameWidth, 6, 0);
        for (int j = 6; j >= 1; j--)
            surface.drawLineAlpha(0, j, 0, j, gameWidth, 8);

        surface.drawBox(0, 194, 512, 20, 0);
        for (int k = 6; k >= 1; k--)
            surface.drawLineAlpha(0, k, 0, 194 - k, gameWidth, 8); */

    surface.drawSprite(gameWidth / 2 - surface.spriteWidth[spriteMedia + 10] / 2,
                       gameHeight / 2 - 152,
                       spriteMedia + 10
    ); // runescape logo
    surface.drawSprite(spriteLogo, 0, 0, gameWidth, gameHeight);
    surface.drawWorld(spriteLogo);
    x = 9216;// '\u2400';
    y = 9216;// '\u2400';
    zoom = 1100;// '\u044C';
    rotation = 888;// '\u0378';
    scene.clipFar3d = 4100;
    scene.clipFar2d = 4100;
    scene.fogZFalloff = 1;
    scene.fogZDistance = 4000;
    surface.blackScreen();
    scene.setCamera(x, -world.getElevation(x, y), y, 912, rotation, 0, zoom * 2);
    scene.render();
    surface.fade2black();
    surface.fade2black();
        /*surface.drawBox(0, 0, gameWidth, 6, 0);
        for (int l = 6; l >= 1; l--)
            surface.drawLineAlpha(0, l, 0, l, gameWidth, 8);

        surface.drawBox(0, 194, gameWidth, 20, 0);
        for (int i1 = 6; i1 >= 1; i1--)
            surface.drawLineAlpha(0, i1, 0, 194 - i1, gameWidth, 8);*/

    surface.drawSprite(gameWidth / 2 - surface.spriteWidth[spriteMedia + 10] / 2,
                       gameHeight / 2 - 152,
                       spriteMedia + 10
    );
    surface.drawSprite(spriteLogo + 1, 0, 0, gameWidth, gameHeight);  // h was 200
    surface.drawWorld(spriteLogo + 1);

    for (int j1 = 0; j1 < 64; j1++) {
      scene.removeModel(world.roofModels[0][j1]);
      scene.removeModel(world.wallModels[1][j1]);
      scene.removeModel(world.roofModels[1][j1]);
      scene.removeModel(world.wallModels[2][j1]);
      scene.removeModel(world.roofModels[2][j1]);
    }

    x = 11136;// '\u2B80';
    y = 10368;// '\u2880';
    zoom = 500;// '\u01F4';
    rotation = 376;// '\u0178';
    scene.clipFar3d = 4100;
    scene.clipFar2d = 4100;
    scene.fogZFalloff = 1;
    scene.fogZDistance = 4000;
    surface.blackScreen();
    scene.setCamera(x, -world.getElevation(x, y), y, 912, rotation, 0, zoom * 2);
    scene.render();
    surface.fade2black();
    surface.fade2black();
        /*surface.drawBox(0, 0, gameWidth, 6, 0);
        for (int k1 = 6; k1 >= 1; k1--)
            surface.drawLineAlpha(0, k1, 0, k1, gameWidth, 8);

        surface.drawBox(0, 194, gameWidth, 20, 0);
        for (int l1 = 6; l1 >= 1; l1--)
            surface.drawLineAlpha(0, l1, 0, 194, gameWidth, 8);  */

    surface.drawSprite(gameWidth / 2 - surface.spriteWidth[spriteMedia + 10] / 2,
                       gameHeight / 2 - 152,
                       spriteMedia + 10
    );
    surface.drawSprite(spriteMedia + 10, 0, 0, gameWidth, gameHeight);
    surface.drawWorld(spriteMedia + 10);
  }

  protected void cantLogout() {
    logoutTimeout = 0;
    showMessage("Sorry, you can't logout at the moment", 3, "@cya@");
  }

  private void showMessage(String message, int type, String color) {
    showMessage(type, message, null, null, 0, color, false);
  }

  protected void draw() {
    Manager.run("draw");

    if (errorLoadingData) {
      Graphics g = getGraphics();
      g.setColor(Color.black);
      g.fillRect(0, 0, 512, 356);
      g.setFont(new Font("Helvetica", 1, 16));
      g.setColor(Color.yellow);
      int i = 35;
      g.drawString("Sorry, an error has occured whilst loading RuneScape", 30, i);
      i += 50;
      g.setColor(Color.white);
      g.drawString("To fix this try the following (in order):", 30, i);
      i += 50;
      g.setColor(Color.white);
      g.setFont(new Font("Helvetica", 1, 12));
      g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, i);
      i += 30;
      g.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, i);
      i += 30;
      g.drawString("3: Try using a different game-world", 30, i);
      i += 30;
      g.drawString("4: Try rebooting your computer", 30, i);
      i += 30;
      g.drawString("5: Try selecting a different version of Java from the play-game menu", 30, i);
      setTargetFps(1);
      return;
    }
    if (errorLoadingCodebase) {
      Graphics g1 = getGraphics();
      g1.setColor(Color.black);
      g1.fillRect(0, 0, 512, 356);
      g1.setFont(new Font("Helvetica", 1, 20));
      g1.setColor(Color.white);
      g1.drawString("Error - unable to load game!", 50, 50);
      g1.drawString("To play RuneScape make sure you play from", 50, 100);
      g1.drawString("http://www.runescape.com", 50, 150);
      setTargetFps(1);
      return;
    }
    if (errorLoadingMemory) {
      Graphics g2 = getGraphics();
      g2.setColor(Color.black);
      g2.fillRect(0, 0, 512, 356);
      g2.setFont(new Font("Helvetica", 1, 20));
      g2.setColor(Color.white);
      g2.drawString("Error - out of memory!", 50, 50);
      g2.drawString("Close ALL unnecessary programs", 50, 100);
      g2.drawString("and windows before loading the game", 50, 150);
      g2.drawString("RuneScape needs about 48meg of spare RAM", 50, 200);
      setTargetFps(1);
      return;
    }
    try {
      if (loggedIn == 0) {
        surface.loggedIn = false;
        loginScreen = 2;
        drawLoginScreens();
      }
      if (loggedIn == 1) {
        surface.loggedIn = true;
        drawGame();
        return;
      }
    } catch (OutOfMemoryError Ex) {
      disposeAndCollect();
      errorLoadingMemory = true;
    }
  }

  protected void onClosing() {
    closeConnection(false);// TODO UNSURE IF SHOULD BE FALSE
    disposeAndCollect();
    if (audioPlayer != null) {
      audioPlayer.stopPlayer();
    }
  }

  private void disposeAndCollect() {
    try {
      if (surface != null) {
        surface.clear();
        surface.pixels = null;
        surface = null;
      }
      if (scene != null) {
        scene.dispose();
        scene = null;
      }
      gameModels = null;
      objectModel = null;
      wallObjectModel = null;
      playerServer = null;
      players = null;
      npcsServer = null;
      npcs = null;
      localPlayer = null;
      if (world != null) {
        world.terrainModels = null;
        world.wallModels = null;
        world.roofModels = null;
        world.parentModel = null;
        world = null;
      }
      System.gc();
    } catch (Exception Ex) {
    }
  }

  protected void handleMouseDown(int i, int j, int k) {
    mouseClickXHistory[mouseClickCount] = j;
    mouseClickYHistory[mouseClickCount] = k;
    mouseClickCount = mouseClickCount + 1 & 8191;// 0x1fff
    for (int l = 10; l < 4000; l++) {
      int i1 = mouseClickCount - l & 8191;// 0x1fff
      if (mouseClickXHistory[i1] == j && mouseClickYHistory[i1] == k) {
        boolean flag = false;
        for (int j1 = 1; j1 < l; j1++) {
          int k1 = mouseClickCount - j1 & 8191;// 0x1fff
          int l1 = i1 - j1 & 8191;// 0x1fff
          if (mouseClickXHistory[l1] != j || mouseClickYHistory[l1] != k) {
            flag = true;
          }
          if (mouseClickXHistory[k1] != mouseClickXHistory[l1] || mouseClickYHistory[k1] != mouseClickYHistory[l1]) {
            break;
          }
          if (j1 == l - 1 && flag && combatTimeout == 0 && logoutTimeout == 0) {
            //sendLogout();
            return;
          }
        }

      }
    }

  }

  protected void showServerMessage(String s) {
    if (s.startsWith("@bor@")) {
      showMessage(s, 4);
      return;
    }
    if (s.startsWith("@que@")) {
      showMessage("@whi@" + s, 5);
      return;
    }
    if (s.startsWith("@pri@")) {
      showMessage(s, 6);
      return;
    } else {
      showMessage(s, 3);
      return;
    }
  }

  protected void showLoginScreenStatus(String s, String s1) {
    if (loginScreen == 1) {
      panelLoginNewuser.updateText(anInt827, s + " " + s1);
    }
    if (loginScreen == 2) {
      panelLoginExistinguser.updateText(controlLoginStatus, s + " " + s1);
    }
    loginUserDisp = s1;
    drawLoginScreens();
    resetTimings();
  }

  private void drawLoginScreens() {
    welcomScreenAlreadyShown = false;
    surface.interlace = false;
    surface.blackScreen();
    if (loginScreen == 0 || loginScreen == 1 || loginScreen == 2 || loginScreen == 3) {
      int i = (loginTimer * 2) % 3072;
      if (i < 1024) {
        surface.drawSprite(0, 10, spriteLogo);
        if (i > 768) {
          surface.drawSpriteAlpha(0, 10, spriteLogo + 1, i - 768);
        }
      } else if (i < 2048) {
        surface.drawSprite(0, 10, spriteLogo + 1);
        if (i > 1792) {
          surface.drawSpriteAlpha(0, 10, spriteMedia + 10, i - 1792);
        }
      } else {
        surface.drawSprite(0, 10, spriteMedia + 10);
        if (i > 2816) {
          surface.drawSpriteAlpha(0, 10, spriteLogo, i - 2816);
        }
      }
    }
    if (loginScreen == 0) {
      panelLoginWelcome.drawPanel();
    }
    if (loginScreen == 1) {
      panelLoginNewuser.drawPanel();
    }
    if (loginScreen == 2) {
      panelLoginExistinguser.drawPanel();
    }
    surface.drawSprite(0, gameHeight, spriteMedia + 22);
    if (gameWidth > surface.spriteWidth[spriteMedia + 22]) {
      int x = surface.spriteWidth[spriteMedia + 22];
      while (x < gameWidth) {
        surface.drawSprite(x, gameHeight, spriteMedia + 22);
        x += x;
      }
    }
    //surface.drawLineAlpha(50, 50, 150, 150, 100, 100);
    //drawUiTabMinimap(true);
    surface.draw(graphics, 0, 0);
  }

  protected void handleIncomingPacket(Command.Server opcode, int ptype, int psize, byte pdata[]) {
    try {
      int offset = 1;
      if (opcode == Command.Server.SV_REGION_PLAYERS) {
        knownPlayerCount = this.playerCount;
        for (int k = 0; k < knownPlayerCount; k++) {
          knownPlayers[k] = players[k];
        }

        int bitmaskOffset = 8 * offset;
        localRegionX = Utility.getBitMask(pdata, bitmaskOffset, 11);
        bitmaskOffset += 11;
        localRegionY = Utility.getBitMask(pdata, bitmaskOffset, 13);
        bitmaskOffset += 13;
        int anim = Utility.getBitMask(pdata, bitmaskOffset, 4);
        bitmaskOffset += 4;
        boolean flag1 = loadNextRegion(localRegionX, localRegionY);
        localRegionX -= regionX;
        localRegionY -= regionY;
        int wx = localRegionX * magicLoc + 64;
        int wy = localRegionY * magicLoc + 64;
        if (flag1) {
          localPlayer.waypointCurrent = 0;
          localPlayer.movingStep = 0;
          localPlayer.currentX = localPlayer.waypointsX[0] = wx;
          localPlayer.currentY = localPlayer.waypointsY[0] = wy;
        }
        this.playerCount = 0;
        localPlayer = createPlayer(localPlayerServerIndex, wx, wy, anim);
        int playerCount = Utility.getBitMask(pdata, bitmaskOffset, 8);
        bitmaskOffset += 8;
        for (int index = 0; index < playerCount; index++) {
          GameCharacter player = null;
          int playerServerIndex = Utility.getBitMask(pdata, bitmaskOffset, 16);
          bitmaskOffset += 16;
          for (int idx = 0; index < knownPlayers.length; index++) {

            for (GameCharacter n : knownPlayers) {
              if (n.serverIndex == playerServerIndex) {
                player = n;
                break;
              }
            }
          }
          int reqUpdate = Utility.getBitMask(pdata, bitmaskOffset++, 1);
          if (reqUpdate != 0) {
            int updateType = Utility.getBitMask(pdata, bitmaskOffset++, 1);
            if (updateType == 0) {
              int nextAnim = Utility.getBitMask(pdata, bitmaskOffset, 3);
              bitmaskOffset += 3;
              if (player == null) {
                continue;
              } else {
                int wp = player.waypointCurrent;
                int x = player.waypointsX[wp];
                int y = player.waypointsY[wp];
                if (nextAnim == 2 || nextAnim == 1 || nextAnim == 3) {
                  x += magicLoc;
                }
                if (nextAnim == 6 || nextAnim == 5 || nextAnim == 7) {
                  x -= magicLoc;
                }
                if (nextAnim == 4 || nextAnim == 3 || nextAnim == 5) {
                  y += magicLoc;
                }
                if (nextAnim == 0 || nextAnim == 1 || nextAnim == 7) {
                  y -= magicLoc;
                }
                player.animationNext = nextAnim;
                player.waypointCurrent = wp = (wp + 1) % 10;
                player.waypointsX[wp] = x;
                player.waypointsY[wp] = y;
              }
            } else {
              if (Version.CLIENT > 204) {
                int i43 = Utility.getBitMask(pdata, bitmaskOffset, 2);
                bitmaskOffset += 2;
                if (i43 == 3) {
                  continue;
                }
                player.animationNext = Utility.getBitMask(pdata, bitmaskOffset, 2) + (i43 << 2);
                bitmaskOffset += 2;
              } else {
                int i43 = Utility.getBitMask(pdata, bitmaskOffset, 4);
                bitmaskOffset += 4;
                if ((i43 & 12) == 12) {// 0xc
                  continue;
                }
                player.animationNext = i43;
              }
            }
          }
          players[this.playerCount++] = player;
        }

        int count = 0;
        while (bitmaskOffset + 24 < psize * 8) {
          int serverIndex = Utility.getBitMask(pdata, bitmaskOffset, 16);
          bitmaskOffset += 16;
          int areaX = Utility.getBitMask(pdata, bitmaskOffset, 5);
          bitmaskOffset += 5;
          if (areaX > 15) {
            areaX -= 32;
          }
          int areaY = Utility.getBitMask(pdata, bitmaskOffset, 5);
          bitmaskOffset += 5;
          if (areaY > 15) {
            areaY -= 32;
          }
          int animation = Utility.getBitMask(pdata, bitmaskOffset, 4);
          bitmaskOffset += 4;
          int x = (localRegionX + areaX) * magicLoc + 64;
          int y = (localRegionY + areaY) * magicLoc + 64;
          createPlayer(serverIndex, x, y, animation);
          if (Version.CLIENT <= 204) {
            int i44 = Utility.getBitMask(pdata, bitmaskOffset++, 1);
            if (i44 == 0) {
              playerServerIndexes[count++] = serverIndex;
            }
          }
        }
        if (Version.CLIENT <= 204 && count > 0) {
          super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_KNOWN_PLAYERS));
          super.clientStream.putShort(count);
          for (int i = 0; i < count; i++) {
            GameCharacter c = playerServer[playerServerIndexes[i]];
            super.clientStream.putShort(c.serverIndex);
            super.clientStream.putShort(c.serverId);
          }

          super.clientStream.sendPacket();
          count = 0;
        }
        if (Version.CLIENT > 204) {
          offset = (bitmaskOffset + 7) / 8;
        }
        return;
      }
      if (opcode == Command.Server.SV_REGION_GROUND_ITEMS) {
        for (; offset < psize; ) {
          if (Utility.getUnsignedByte(pdata[offset++]) == 255) {
            int newIndex = 0;
            int x = localRegionX + pdata[offset++] >> 3;
            int y = localRegionY + pdata[offset++] >> 3;
            for (int oldIndex = 0; oldIndex < groundItemCount; oldIndex++) {
              int j26 = (groundItemX[oldIndex] >> 3) - x;
              int j29 = (groundItemY[oldIndex] >> 3) - y;
              if (j26 != 0 || j29 != 0) {
                if (oldIndex != newIndex) {
                  groundItemX[newIndex] = groundItemX[oldIndex];
                  groundItemY[newIndex] = groundItemY[oldIndex];
                  groundItemId[newIndex] = groundItemId[oldIndex];
                  groundItemZ[newIndex] = groundItemZ[oldIndex];
                }
                newIndex++;
              }
            }

            groundItemCount = newIndex;
          } else {
            offset--;
            int mod = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            int x = localRegionX + pdata[offset++];
            int y = localRegionY + pdata[offset++];
            if ((mod & 32768) == 0) {// 0x8000
              groundItemX[groundItemCount] = x;
              groundItemY[groundItemCount] = y;
              groundItemId[groundItemCount] = mod;
              groundItemZ[groundItemCount] = 0;
              for (int index = 0; index < objectCount; index++) {
                if (objectX[index] != x || objectY[index] != y) {
                  continue;
                }
                groundItemZ[groundItemCount] = GameData.objectElevation[objectId[index]];
                break;
              }

              groundItemCount++;
            } else {
              mod &= 32767;// 0x7fff
              int newIndex = 0;
              for (int oldIndex = 0; oldIndex < groundItemCount; oldIndex++) {
                if (groundItemX[oldIndex] != x || groundItemY[oldIndex] != y || groundItemId[oldIndex] != mod) {
                  if (oldIndex != newIndex) {
                    groundItemX[newIndex] = groundItemX[oldIndex];
                    groundItemY[newIndex] = groundItemY[oldIndex];
                    groundItemId[newIndex] = groundItemId[oldIndex];
                    groundItemZ[newIndex] = groundItemZ[oldIndex];
                  }
                  newIndex++;
                } else {
                  mod = -123;
                }
              }

              groundItemCount = newIndex;
            }
          }
        }

        return;
      }
      if (opcode == Command.Server.SV_REGION_OBJECTS) {
        for (; offset < psize; ) {
          if (Utility.getUnsignedByte(pdata[offset++]) == 255) {
            int newIndex = 0;
            int x = localRegionX + pdata[offset++] >> 3;
            int y = localRegionY + pdata[offset++] >> 3;
            int direction = pdata[offset++];
            if (Version.CLIENT <= 204) {
              offset += 3;
            }
            for (int oldIndex = 0; oldIndex < objectCount; oldIndex++) {
              int l26 = (objectX[oldIndex] >> 3) - x;
              int k29 = (objectY[oldIndex] >> 3) - y;
              if (l26 != 0 || k29 != 0) {
                if (oldIndex != newIndex) {
                  objectModel[newIndex] = objectModel[oldIndex];
                  objectModel[newIndex].key = newIndex;
                  objectX[newIndex] = objectX[oldIndex];
                  objectY[newIndex] = objectY[oldIndex];
                  objectId[newIndex] = objectId[oldIndex];
                  objectDirection[newIndex] = objectDirection[oldIndex];
                }
                newIndex++;
              } else {
                scene.removeModel(objectModel[oldIndex]);
                world.removeObject(objectX[oldIndex], objectY[oldIndex], objectId[oldIndex]);
              }
            }

            objectCount = newIndex;
          } else {
            int id1 = Utility.getUnsignedShort(pdata, offset);
            offset--;
            int id = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            int lX = localRegionX + pdata[offset++];
            int lY = localRegionY + pdata[offset++];
            int newDirection = pdata[offset++];
            int oldIndex = 0;
            for (int newIndex = 0; newIndex < objectCount; newIndex++) {
              if (objectX[newIndex] != lX || objectY[newIndex] != lY) {
                if (newIndex != oldIndex) {
                  objectModel[oldIndex] = objectModel[newIndex];
                  objectModel[oldIndex].key = oldIndex;
                  objectX[oldIndex] = objectX[newIndex];
                  objectY[oldIndex] = objectY[newIndex];
                  objectId[oldIndex] = objectId[newIndex];
                  objectDirection[oldIndex] = objectDirection[newIndex];
                }
                oldIndex++;
              } else {
                scene.removeModel(objectModel[newIndex]);
                world.removeObject(objectX[newIndex], objectY[newIndex], objectId[newIndex]);
              }
            }

            objectCount = oldIndex;
            if (id != 60000) {
              int direction = newDirection;
              int width;
              int height;
              if (direction == 0 || direction == 4) {
                width = GameData.objectWidth[id];
                height = GameData.objectHeight[id];
              } else {
                height = GameData.objectWidth[id];
                width = GameData.objectHeight[id];
              }
              int worldX = ((lX + lX + width) * magicLoc) / 2;
              int worldY = ((lY + lY + height) * magicLoc) / 2;
              int modelIdx = GameData.objectModelIndex[id];
              GameModel model = gameModels[modelIdx].copy();
              scene.addModel(model);
              model.key = objectCount;
              model.rotate(0, direction * 32, 0);
              model.translate(worldX, -world.getElevation(worldX, worldY), worldY);
              model.setLight(true, 48, 48, -50, -10, -50);
              world.removeObject2(lX, lY, id);
              if (id == 74) {
                model.translate(0, -480, 0);
              }
              objectX[objectCount] = lX;
              objectY[objectCount] = lY;
              objectId[objectCount] = id;
              objectDirection[objectCount] = direction;
              objectModel[objectCount++] = model;
            }
          }
        }

        return;
      }
      if (opcode == Command.Server.SV_IN_TUTORIAL) {
        inTutorial = Utility.getUnsignedByte(pdata[offset++]) != 0;
        return;
      }
      if (opcode == Command.Server.SV_INVENTORY_ITEMS) {
        inventoryItemsCount = Utility.getUnsignedByte(pdata[offset++]);

        for (int i = 0; i < inventoryItemsCount; i++) {
          int mod = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          inventoryItemId[i] = (mod & 0x7fff);// 0x7fff
          inventoryEquipped[i] = mod / 32768;

          if (GameData.itemStackable[mod & 0x7fff] == 0) {// 0x7fff
            if (Version.CLIENT > 204) {
              Utility.UInt3 u = Utility.getUnsignedInt3(pdata, offset);
              offset = u.newOffset;
              inventoryItemStackCount[i] = u.result;
            } else {
              inventoryItemStackCount[i] = Utility.getUnsignedInt(pdata, offset);
              if (inventoryItemStackCount[i] >= 128) {
                offset += 4;
              } else {
                offset++;
              }
            }
          } else {
            inventoryItemStackCount[i] = 1;
          }
        }

        return;
      }
      if (opcode == Command.Server.SV_REGION_PLAYER_UPDATE) {
        int playerCount = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        for (int _i = 0; _i < playerCount; _i++) {
          int playerServerIndex = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          GameCharacter player = playerServer[playerServerIndex];
          byte updateType = pdata[offset++];
          if (updateType == 0) { // speech bubble with an item in it
            int id = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            if (player != null) {
              player.bubbleTimeout = 150;
              player.bubbleItem = id;
            }
            // TODO messy as fuck, refactor i guess but needs to reach the last else block if player = null i think?
          } else if (Version.CLIENT > 204 && player != null && updateType == 1) { // chat
            int modStatus = Utility.getUnsignedByte(pdata[offset++]);

            Utility.Cabbage message = Utility.cabbage(pdata, offset);
            offset = message.newOffset;
            boolean ignored = false;
            String name = Utility.formatName(player.accountName);
            if (name != null) {
              for (int index = 0; index < ignoreListCount; index++) {
                if (name.equals(Utility.formatName(ignoreListAccNames[index]))) {
                  ignored = true;
                  break;
                }
              }
            }
            if (!ignored) {
              player.messageTimeout = 150;
              player.message = message.result;
              showMessage(4, player.message, player.name, player.accountName, modStatus, null, modStatus == 2);
              showMessage("[" + modStatus + "," + player.accountName + "]" + player.name + ": " + message, 2);
            }
          } else if (Version.CLIENT <= 204 && updateType == 1) {
            byte messageLength = pdata[offset++];
            if (player != null) {
              String filtered = WordFilter.filter(Utility.byteToString(pdata, offset, messageLength));
              boolean ignored = false;
              for (int i = 0; i < super.ignoreListCount; i++) {
                if (super.ignoreListHashes[i] == player.hash) {
                  ignored = true;
                  break;
                }
              }

              if (!ignored) {
                player.messageTimeout = 150;
                player.message = filtered;
                showMessage(player.name + ": " + player.message, 2);
              }
            }
            offset += messageLength;
          } else if (updateType == 2) { // combat damage and hp
            int damage = Utility.getUnsignedByte(pdata[offset++]);
            int current = Utility.getUnsignedByte(pdata[offset++]);
            int max = Utility.getUnsignedByte(pdata[offset++]);
            if (player != null) {
              player.damageTaken = damage;
              player.healthCurrent = current;
              player.healthMax = max;
              player.combatTimer = 200;
              if (player == localPlayer) {
                playerStatCurrent[3] = current;
                playerStatBase[3] = max;
                showDialogWelcome = false;
                showDialogServermessage = false;
              }
            }
          } else if (updateType == 3) { // new incoming projectile from npc?
            int projectileSprite = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            int npcIdx = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            if (player != null) {
              player.incomingProjectileSprite = projectileSprite;
              player.attackingNpcServerIndex = npcIdx;
              player.attackingPlayerServerIndex = -1;
              player.projectileRange = projectileMaxRange;
            }
          } else if (updateType == 4) { // new incoming projectile from player
            int projectileSprite = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            int playerIdx = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            if (player != null) {
              player.incomingProjectileSprite = projectileSprite;
              player.attackingPlayerServerIndex = playerIdx;
              player.attackingNpcServerIndex = -1;
              player.projectileRange = projectileMaxRange;
            }
            // TODO same shit as above?
          } else if (Version.CLIENT > 204 && player != null && updateType == 5) {
            Utility.getUnsignedShort(pdata, offset); // ignore
            offset += 2;
            Utility.Gjstr2 displayName = Utility.gjstr2(pdata, offset);
            offset = displayName.newOffset;
            Utility.Gjstr2 accountName = Utility.gjstr2(pdata, offset);
            offset = accountName.newOffset;
            player.name = displayName.result;
            player.accountName = accountName.result;

            System.out.println("player.name=" + player.name + " player.accountName=" + player.accountName);

            int equippedCount = Utility.getUnsignedByte(pdata[offset++]);
            for (int i = 0; i < equippedCount; i++) {
              player.equippedItem[i] = Utility.getUnsignedByte(pdata[offset++]);
            }
            for (int i = equippedCount; i < 12; i++) {
              player.equippedItem[i] = 0;
            }

            player.colourHair = Utility.getUnsignedByte(pdata[offset++]);
            player.colourTop = Utility.getUnsignedByte(pdata[offset++]);
            player.colourBottom = Utility.getUnsignedByte(pdata[offset++]);
            player.colourSkin = Utility.getUnsignedByte(pdata[offset++]);
            player.level = Utility.getUnsignedByte(pdata[offset++]);
            player.skullVisible = Utility.getUnsignedByte(pdata[offset++]);
          } else if (Version.CLIENT <= 204 && updateType == 5) {
            if (player != null) {
              player.serverId = Utility.getUnsignedShort(pdata, offset);
              offset += 2;
              player.hash = Utility.getUnsignedLong(pdata, offset);
              offset += 8;
              player.name = Utility.hash2username(player.hash);

              int equippedCount = Utility.getUnsignedByte(pdata[offset]);
              offset++;
              for (int i = 0; i < equippedCount; i++) {
                player.equippedItem[i] = Utility.getUnsignedByte(pdata[offset]);
                offset++;
              }

              for (int i = equippedCount; i < 12; i++) {
                player.equippedItem[i] = 0;
              }

              player.colourHair = pdata[offset++] & 0xff;
              player.colourTop = pdata[offset++] & 0xff;
              player.colourBottom = pdata[offset++] & 0xff;
              player.colourSkin = pdata[offset++] & 0xff;
              player.level = pdata[offset++] & 0xff;
              player.skullVisible = pdata[offset++] & 0xff;
              offset++; //server sends mod rank here
            } else {
              offset += 14;
              int unused = Utility.getUnsignedByte(pdata[offset]);
              offset += unused + 1;
            }
          } else if (Version.CLIENT > 204 && player != null && updateType == 6) {
            Utility.Cabbage message = Utility.cabbage(pdata, offset);
            offset = message.newOffset;
            player.message = message.result;
            player.messageTimeout = 150;
            if (player == localPlayer) {
              showMessage(3, player.message, player.name, player.accountName, 0, null, false);
            }
          } else if (Version.CLIENT <= 204 && updateType == 6) {
            byte mLen = pdata[offset];
            offset++;
            if (player != null) {
              String msg = Utility.byteToString(pdata, offset, mLen);
              player.messageTimeout = 150;
              player.message = msg;
              if (player == localPlayer) {
                showMessage(player.name + ": " + player.message, 5);
              }
            }
            offset += mLen;
          } else if (Version.CLIENT > 204) {
            int i1 = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            Utility.Gjstr2 g1 = Utility.gjstr2(pdata, offset);
            offset = g1.newOffset;
            Utility.Gjstr2 g2 = Utility.gjstr2(pdata, offset);
            offset = g2.newOffset;
            int i2 = Utility.getUnsignedByte(pdata[offset++]);
            offset += 6 + i2;
            // TODO what the fuck is this
            System.out.println(String.format("i1=%d g1=%s g2=%s i2=%d", i1, g1.result, g2.result, i2));
          }
        }

        System.out.println("WAT");
        return;
      }
      if (opcode == Command.Server.SV_REGION_WALL_OBJECTS) {
        for (; offset < psize; ) {
          if (Utility.getUnsignedByte(pdata[offset++]) == 255) {
            int count = 0;
            int lX = localRegionX + pdata[offset++] >> 3;
            int lY = localRegionY + pdata[offset++] >> 3;

            for (int i = 0; i < wallObjectCount; i++) {
              int sX = (wallObjectX[i] >> 3) - lX;
              int sY = (wallObjectY[i] >> 3) - lY;
              if (sX != 0 || sY != 0) {
                if (i != count) {
                  wallObjectModel[count] = wallObjectModel[i];
                  wallObjectModel[count].key = count + 10000;
                  wallObjectX[count] = wallObjectX[i];
                  wallObjectY[count] = wallObjectY[i];
                  wallObjectDirection[count] = wallObjectDirection[i];
                  wallObjectId[count] = wallObjectId[i];
                }
                count++;
              } else {
                scene.removeModel(wallObjectModel[i]);
                world.removeWallObject(wallObjectX[i], wallObjectY[i], wallObjectDirection[i], wallObjectId[i]);
              }
            }

            wallObjectCount = count;
          } else {
            offset--;
            int id = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            int lX = localRegionX + pdata[offset++];
            int lY = localRegionY + pdata[offset++];
            byte direction = pdata[offset++];
            int count = 0;
            for (int i = 0; i < wallObjectCount; i++) {
              if (wallObjectX[i] != lX || wallObjectY[i] != lY || wallObjectDirection[i] != direction) {
                if (i != count) {
                  wallObjectModel[count] = wallObjectModel[i];
                  wallObjectModel[count].key = count + 10000;
                  wallObjectX[count] = wallObjectX[i];
                  wallObjectY[count] = wallObjectY[i];
                  wallObjectDirection[count] = wallObjectDirection[i];
                  wallObjectId[count] = wallObjectId[i];
                }
                count++;
              } else {
                scene.removeModel(wallObjectModel[i]);
                world.removeWallObject(wallObjectX[i], wallObjectY[i], wallObjectDirection[i], wallObjectId[i]);
              }
            }

            wallObjectCount = count;
            if (id != 60000) {
              world.setObjectAdjacency(lX, lY, direction, id);
              GameModel model = createModel(lX, lY, direction, id, wallObjectCount);
              wallObjectModel[wallObjectCount] = model;
              wallObjectX[wallObjectCount] = lX;
              wallObjectY[wallObjectCount] = lY;
              wallObjectId[wallObjectCount] = id;
              wallObjectDirection[wallObjectCount++] = direction;
            }
          }
        }

        return;
      }
      if (opcode == Command.Server.SV_REGION_NPCS) {
        npcCacheCount = this.npcCount;
        this.npcCount = 0;
        for (int i2 = 0; i2 < npcCacheCount; i2++) {
          npcsCache[i2] = npcs[i2];
        }

        int bitmaskOffset = 8 * offset;
        int npcCount = Utility.getBitMask(pdata, bitmaskOffset, 8);
        bitmaskOffset += 8;
        for (int index = 0; index < npcCount; index++) {
          int npcCacheID = Utility.getBitMask(pdata, bitmaskOffset, 16);
          GameCharacter npc = null;
          for (GameCharacter n : npcsCache) {
            if (n.serverIndex == npcCacheID) {
              npc = n;
              break;
            }
          }

          bitmaskOffset += 16;

          int reqUpdate = Utility.getBitMask(pdata, bitmaskOffset++, 1);
          if (reqUpdate != 0) {
            int updateType = Utility.getBitMask(pdata, bitmaskOffset++, 1);
            if (updateType == 0) {
              int nextAnim = Utility.getBitMask(pdata, bitmaskOffset, 3);
              bitmaskOffset += 3;
              if (npc == null) {
                bitmaskOffset += 6;
                continue;
              } else {
                int wp = npc.waypointCurrent;
                int x = npc.waypointsX[wp];
                int y = npc.waypointsY[wp];
                if (nextAnim == 2 || nextAnim == 1 || nextAnim == 3) {
                  x += magicLoc;
                }
                if (nextAnim == 6 || nextAnim == 5 || nextAnim == 7) {
                  x -= magicLoc;
                }
                if (nextAnim == 4 || nextAnim == 3 || nextAnim == 5) {
                  y += magicLoc;
                }
                if (nextAnim == 0 || nextAnim == 1 || nextAnim == 7) {
                  y -= magicLoc;
                }
                npc.animationNext = nextAnim;
                npc.waypointCurrent = wp = (wp + 1) % 10;
                npc.waypointsX[wp] = x;
                npc.waypointsY[wp] = y;
              }
            } else {
              if (Version.CLIENT > 204) {
                int k35 = Utility.getBitMask(pdata, bitmaskOffset, 2);
                bitmaskOffset += 2;
                if (k35 == 3) {
                  continue;
                }
                npc.animationNext = Utility.getBitMask(pdata, bitmaskOffset, 2) + (k35 << 2);
                bitmaskOffset += 2;
              } else {
                int k35 = Utility.getBitMask(pdata, bitmaskOffset, 4);
                bitmaskOffset += 4;
                if ((k35 & 12) == 12) {// 0xc
                  continue;
                }
                npc.animationNext = k35;
              }
            }
          }
          if (npc != null) {
            npcs[this.npcCount++] = npc;
          } else {
            this.npcCount++;
          }
        }

        while (bitmaskOffset + 34 < psize * 8) {
          int serverIndex = Utility.getBitMask(pdata, bitmaskOffset, 16);
          bitmaskOffset += 16;
          int areaX = Utility.getBitMask(pdata, bitmaskOffset, 5);
          bitmaskOffset += 5;
          if (areaX > 15) {
            areaX -= 32;
          }
          int areaY = Utility.getBitMask(pdata, bitmaskOffset, 5);
          bitmaskOffset += 5;
          if (areaY > 15) {
            areaY -= 32;
          }
          int sprite = Utility.getBitMask(pdata, bitmaskOffset, 4);
          bitmaskOffset += 4;
          int x = (localRegionX + areaX) * magicLoc + 64;
          int y = (localRegionY + areaY) * magicLoc + 64;
          int type = Utility.getBitMask(pdata, bitmaskOffset, 10);
          bitmaskOffset += 10;
          if (type >= GameData.npcCount) {
            type = 24;
          }
          addNpc(serverIndex, x, y, sprite, type);
        }

        return;
      }
      if (opcode == Command.Server.SV_REGION_NPC_UPDATE) {
        int npcCount = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        for (int index = 0; index < npcCount; index++) {
          int serverIndex = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          GameCharacter npc = npcsServer[serverIndex];
          int updateType = Utility.getUnsignedByte(pdata[offset++]);
          if (updateType == 1) {
            int playerServerIndex = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
            if (Version.CLIENT > 204) {
              if (npc != null) {
                Utility.Cabbage message = Utility.cabbage(pdata, offset);
                offset = message.newOffset;
                npc.message = message.result;
                npc.messageTimeout = 150;
                if (playerServerIndex == localPlayer.serverIndex) {
                  showMessage(GameData.npcName[npc.npcId] + ": " + npc.message, 3, "@yel@");
                }
              }
            } else {
              byte messageLength = pdata[offset];
              offset++;
              if (npc != null) {
                String s4 = Utility.byteToString(pdata, offset, messageLength);
                npc.messageTimeout = 150;
                npc.message = s4;
                if (playerServerIndex == localPlayer.serverIndex) {
                  showMessage("@yel@" + GameData.npcName[npc.npcId] + ": " + npc.message, 5);
                }
              }
              offset += messageLength;
            }
          } else if (updateType == 2) {
            int damage = Utility.getUnsignedByte(pdata[offset++]);
            int hpcur = Utility.getUnsignedByte(pdata[offset++]);
            int hpmax = Utility.getUnsignedByte(pdata[offset++]);
            if (npc != null) {
              npc.damageTaken = damage;
              npc.healthCurrent = hpcur;
              npc.healthMax = hpmax;
              npc.combatTimer = 200;
            }
          }
        }

        return;
      }
      if (opcode == Command.Server.SV_OPTION_LIST) {
        showOptionMenu = true;
        int count = Utility.getUnsignedByte(pdata[offset++]);
        optionMenuCount = count;
        if (Version.CLIENT > 204) {
          for (int i = 0; i < count; i++) {
            Utility.Gjstr2 g1 = Utility.gjstr2(pdata, offset);
            offset = g1.newOffset;
            optionMenuEntry[i] = g1.result;
          }
        } else {
          for (int i = 0; i < count; i++) {
            int length = Utility.getUnsignedByte(pdata[offset++]);
            optionMenuEntry[i] = Utility.byteToString(pdata, offset, length);
            offset += length;
          }
        }

        return;
      }
      if (opcode == Command.Server.SV_OPTION_LIST_CLOSE) {
        showOptionMenu = false;
        return;
      }

      if (opcode == Command.Server.SV_CLOSE_CONNECTION) {
        closeConnection(false);
        resetGame();
        resetLoginScreenVariables();
        resetLoginVars();
        surface.loggedIn = false;
        loginScreen = 2;
        drawLoginScreens();
        showLoginScreenStatus("", "");
        return;
      }

      if (opcode == Command.Server.SV_WORLD_INFO) {
        loadingArea = true;
        localPlayerServerIndex = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        planeWidth = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        planeHeight = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        planeIndex = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        planeMultiplier = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        planeHeight -= planeIndex * planeMultiplier;
        return;
      }
      if (opcode == Command.Server.SV_PLAYER_STAT_LIST) {
        for (int i = 0; i < playerStatCount; i++) {
          playerStatCurrent[i] = Utility.getUnsignedByte(pdata[offset++]);
        }

        for (int i = 0; i < playerStatCount; i++) {
          playerStatBase[i] = Utility.getUnsignedByte(pdata[offset++]);
        }

        for (int i = 0; i < playerStatCount; i++) {
          playerExperience[i] = Utility.getUnsignedInt(pdata, offset);
          offset += 4;
        }

        playerQuestPoints = Utility.getUnsignedByte(pdata[offset++]);
        return;
      }
      if (opcode == Command.Server.SV_PLAYER_STAT_EQUIPMENT_BONUS) {
        for (int i3 = 0; i3 < playerStatEquipmentCount; i3++) {
          playerStatEquipment[i3] = (byte) Utility.getUnsignedShort(pdata, offset);
          offset += 2;
        }

        return;
      }
      if (opcode == Command.Server.SV_PLAYER_DIED) {
        deathScreenTimeout = 250;
        return;
      }
      if (opcode == Command.Server.SV_REGION_ENTITY_UPDATE) {
        int entityCount = (psize - 1) / 4;
        for (int index = 0; index < entityCount; index++) {
          int x = localRegionX + Utility.getSignedShort(pdata, offset) >> 3;
          offset += 2;
          int y = localRegionY + Utility.getSignedShort(pdata, offset) >> 3;
          offset += 2;
          int newIndex = 0;
          for (int gIndex = 0; gIndex < groundItemCount; gIndex++) {
            int gx = (groundItemX[gIndex] >> 3) - x;
            int gy = (groundItemY[gIndex] >> 3) - y;
            if (gx != 0 || gy != 0) {
              if (gIndex != newIndex) {
                groundItemX[newIndex] = groundItemX[gIndex];
                groundItemY[newIndex] = groundItemY[gIndex];
                groundItemId[newIndex] = groundItemId[gIndex];
                groundItemZ[newIndex] = groundItemZ[gIndex];
              }
              newIndex++;
            }
          }

          groundItemCount = newIndex;
          newIndex = 0;
          for (int oIndex = 0; oIndex < objectCount; oIndex++) {
            int ox = (objectX[oIndex] >> 3) - x;
            int oy = (objectY[oIndex] >> 3) - y;
            if (ox != 0 || oy != 0) {
              if (oIndex != newIndex) {
                objectModel[newIndex] = objectModel[oIndex];
                objectModel[newIndex].key = newIndex;
                objectX[newIndex] = objectX[oIndex];
                objectY[newIndex] = objectY[oIndex];
                objectId[newIndex] = objectId[oIndex];
                objectDirection[newIndex] = objectDirection[oIndex];
              }
              newIndex++;
            } else {
              scene.removeModel(objectModel[oIndex]);
              world.removeObject(objectX[oIndex], objectY[oIndex], objectId[oIndex]);
            }
          }

          objectCount = newIndex;
          newIndex = 0;
          for (int wIndex = 0; wIndex < wallObjectCount; wIndex++) {
            int wx = (wallObjectX[wIndex] >> 3) - x;
            int wy = (wallObjectY[wIndex] >> 3) - y;
            if (wx != 0 || wy != 0) {
              if (wIndex != newIndex) {
                wallObjectModel[newIndex] = wallObjectModel[wIndex];
                wallObjectModel[newIndex].key = newIndex + 10000;
                wallObjectX[newIndex] = wallObjectX[wIndex];
                wallObjectY[newIndex] = wallObjectY[wIndex];
                wallObjectDirection[newIndex] = wallObjectDirection[wIndex];
                wallObjectId[newIndex] = wallObjectId[wIndex];
              }
              newIndex++;
            } else {
              scene.removeModel(wallObjectModel[wIndex]);
              world.removeWallObject(wallObjectX[wIndex],
                                     wallObjectY[wIndex],
                                     wallObjectDirection[wIndex],
                                     wallObjectId[wIndex]
              );
            }
          }

          wallObjectCount = newIndex;
        }

        return;
      }
      if (opcode == Command.Server.SV_APPEARANCE) {
        showAppearanceChange = true;
        return;
      }
      if (opcode == Command.Server.SV_TRADE_OPEN) {
        int serverIndex = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        if (playerServer[serverIndex] != null) {
          tradeRecipientName = playerServer[serverIndex].name;
        }
        showDialogTrade = true;
        tradeRecipientAccepted = false;
        tradeAccepted = false;
        tradeItemsCount = 0;
        tradeRecipientItemsCount = 0;
        return;
      }
      if (opcode == Command.Server.SV_TRADE_CLOSE) {
        showDialogTrade = false;
        showDialogTradeConfirm = false;
        return;
      }
      if (opcode == Command.Server.SV_TRADE_ITEMS) {
        tradeRecipientItemsCount = Utility.getUnsignedByte(pdata[offset++]);
        for (int index = 0; index < tradeRecipientItemsCount; index++) {
          tradeRecipientItems[index] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          tradeRecipientItemCount[index] = Utility.getUnsignedInt(pdata, offset);
          offset += 4;
        }

        tradeRecipientAccepted = false;
        tradeAccepted = false;
        return;
      }
      if (opcode == Command.Server.SV_TRADE_RECIPIENT_STATUS) {
        int accepted = Version.CLIENT > 204 ? Utility.getUnsignedByte(pdata[offset++]) : pdata[offset++];
        if (accepted == 1) {
          tradeRecipientAccepted = true;
          return;
        } else {
          tradeRecipientAccepted = false;
          return;
        }
      }
      if (opcode == Command.Server.SV_SHOP_OPEN) {
        showDialogShop = true;
        int shopItemCount = Utility.getUnsignedByte(pdata[offset++]);
        byte shopType = pdata[offset++];
        shopSellPriceMod = Utility.getUnsignedByte(pdata[offset++]);
        shopBuyPriceMod = Utility.getUnsignedByte(pdata[offset++]);
        if (Version.CLIENT > 204) {
          shopPriceMultiplier = Utility.getUnsignedByte(pdata[offset++]);
        }
        for (int itemIndex = 0; itemIndex < 40; itemIndex++) {
          shopItem[itemIndex] = -1;
        }

        for (int itemIndex = 0; itemIndex < shopItemCount; itemIndex++) {
          shopItem[itemIndex] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          this.shopItemCount[itemIndex] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          if (Version.CLIENT > 204) {
            shopItemPrice[itemIndex] = Utility.getUnsignedShort(pdata, offset);
            offset += 2;
          } else {
            shopItemPrice[itemIndex] = pdata[offset++];
          }
        }

        if (shopType == 1) {// shopType == 1 -> is a general shop
          int slot = 39;
          for (int index = 0; index < inventoryItemsCount; index++) {
            if (slot < shopItemCount) {
              break;
            }
            boolean flag2 = false;
            for (int j39 = 0; j39 < 40; j39++) {
              if (shopItem[j39] != inventoryItemId[index]) {
                continue;
              }
              flag2 = true;
              break;
            }

            if (inventoryItemId[index] == 10) {
              flag2 = true;
            }
            if (!flag2) {
              shopItem[slot] = inventoryItemId[index] & 32767;// 0x7fff
              this.shopItemCount[slot] = 0;
              shopItemPrice[slot] = 0;
              slot--;
            }
          }

        }
        if (shopSelectedItemIndex >= 0 &&
            shopSelectedItemIndex < 40 &&
            shopItem[shopSelectedItemIndex] != shopSelectedItemType) {
          shopSelectedItemIndex = -1;
          shopSelectedItemType = -2;
        }
        return;
      }
      if (opcode == Command.Server.SV_SHOP_CLOSE) {
        showDialogShop = false;
        return;
      }
      if (opcode == Command.Server.SV_TRADE_STATUS) {
        byte accepted = pdata[1];
        if (accepted == 1) {
          tradeAccepted = true;
          return;
        } else {
          tradeAccepted = false;
          return;
        }
      }
      if (opcode == Command.Server.SV_GAME_SETTINGS) {
        optionCameraModeAuto = Utility.getUnsignedByte(pdata[offset++]) == 1;
        optionMouseButtonOne = Utility.getUnsignedByte(pdata[offset++]) == 1;
        optionSoundDisabled = Utility.getUnsignedByte(pdata[offset++]) == 1;
        return;
      }
      if (opcode == Command.Server.SV_PRAYER_STATUS) {
        for (int index = 0; index < psize - 1; index++) {
          boolean on = pdata[offset++] == 1;
          if (!prayerOn[index] && on) {
            playSoundFile("prayeron");
          }
          if (prayerOn[index] && !on) {
            playSoundFile("prayeroff");
          }
          prayerOn[index] = on;
        }

        return;
      }
      if (opcode == Command.Server.SV_PLAYER_QUEST_LIST) {
        for (int index = 0; index < questCount; index++) {
          questComplete[index] = pdata[offset++] == 1;
        }

        return;
      }
      if (opcode == Command.Server.SV_BANK_OPEN) {
        showDialogBank = true;
        newBankItemCount = Utility.getUnsignedByte(pdata[offset++]);
        bankItemsMax = Utility.getUnsignedByte(pdata[offset++]);
        for (int index = 0; index < newBankItemCount; index++) {
          newBankItems[index] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          if (Version.CLIENT > 204) {
            Utility.UInt3 u = Utility.getUnsignedInt3(pdata, offset);
            offset = u.newOffset;
            newBankItemsCount[index] = u.result;
          } else {
            newBankItemsCount[index] = Utility.getUnsignedInt(pdata, offset);
            if (Version.CLIENT <= 204) {
              if (newBankItemsCount[index] >= 128) {
                offset += 4;
              } else {
                offset++;
              }
            }
          }
        }

        updateBankItems();
        return;
      }
      if (opcode == Command.Server.SV_BANK_CLOSE) {
        showDialogBank = false;
        return;
      }
      if (opcode == Command.Server.SV_PLAYER_STAT_EXPERIENCE_UPDATE) {
        int skill = Utility.getUnsignedByte(pdata[offset++]);
        playerExperience[skill] = Utility.getUnsignedInt(pdata, offset);
        offset += 4;
        return;
      }
      if (opcode == Command.Server.SV_DUEL_OPEN) {
        int serverIndex = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        if (playerServer[serverIndex] != null) {
          duelOpponentName = playerServer[serverIndex].name;
        }
        showDialogDuel = true;
        duelOfferItemCount = 0;
        duelOfferOpponentItemCount = 0;
        duelOfferOpponentAccepted = false;
        duelOfferAccepted = false;
        duelSettingsRetreat = false;
        duelSettingsMagic = false;
        duelSettingsPrayer = false;
        duelSettingsWeapons = false;
        return;
      }
      if (opcode == Command.Server.SV_DUEL_CLOSE) {
        showDialogDuel = false;
        showDialogDuelConfirm = false;
        return;
      }
      if (opcode == Command.Server.SV_TRADE_CONFIRM_OPEN) {
        showDialogTradeConfirm = true;
        tradeConfirmAccepted = false;
        showDialogTrade = false;
        if (Version.CLIENT > 204) {
          Utility.Gjstr2 g1 = Utility.gjstr2(pdata, offset);
          offset = g1.newOffset;
          tradeRecipientConfirmName = g1.result;
        } else {
          tradeRecipientConfirmHash = Utility.getUnsignedLong(pdata, offset);
          offset += 8;
        }
        tradeRecipientConfirmItemsCount = Utility.getUnsignedByte(pdata[offset++]);
        for (int index = 0; index < tradeRecipientConfirmItemsCount; index++) {
          tradeRecipientConfirmItems[index] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          tradeRecipientConfirmItemCount[index] = Utility.getUnsignedInt(pdata, offset);
          offset += 4;
        }

        tradeConfirmItemsCount = Utility.getUnsignedByte(pdata[offset++]);
        for (int index = 0; index < tradeConfirmItemsCount; index++) {
          tradeConfirmItems[index] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          tradeConfirmItemCount[index] = Utility.getUnsignedInt(pdata, offset);
          offset += 4;
        }

        return;
      }
      if (opcode == Command.Server.SV_DUEL_UPDATE) {
        duelOfferOpponentItemCount = Utility.getUnsignedByte(pdata[offset++]);
        for (int index = 0; index < duelOfferOpponentItemCount; index++) {
          duelOfferOpponentItemId[index] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          duelOfferOpponentItemStack[index] = Utility.getUnsignedInt(pdata, offset);
          offset += 4;
        }

        duelOfferOpponentAccepted = false;
        duelOfferAccepted = false;
        return;
      }
      if (opcode == Command.Server.SV_DUEL_SETTINGS) {
        duelSettingsRetreat = (Version.CLIENT > 204 ? Utility.getUnsignedByte(pdata[offset++]) : pdata[offset++]) == 1;
        duelSettingsMagic = (Version.CLIENT > 204 ? Utility.getUnsignedByte(pdata[offset++]) : pdata[offset++]) == 1;
        duelSettingsPrayer = (Version.CLIENT > 204 ? Utility.getUnsignedByte(pdata[offset++]) : pdata[offset++]) == 1;
        duelSettingsWeapons = (Version.CLIENT > 204 ? Utility.getUnsignedByte(pdata[offset++]) : pdata[offset++]) == 1;
        duelOfferOpponentAccepted = false;
        duelOfferAccepted = false;
        return;
      }
      if (opcode == Command.Server.SV_BANK_UPDATE) {
        int itemsCountOld = Utility.getUnsignedByte(pdata[offset++]);
        int item = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        int itemCount;
        if (Version.CLIENT > 204) {
          Utility.UInt3 u = Utility.getUnsignedInt3(pdata, offset);
          offset = u.newOffset;
          itemCount = u.result;
        } else {
          itemCount = Utility.getUnsignedInt(pdata, offset);
          if (itemCount >= 128 || Version.CLIENT > 204) {
            offset += 4;
          } else {
            offset++;
          }
        }
        if (itemCount == 0) {
          newBankItemCount--;
          for (int index = itemsCountOld; index < newBankItemCount; index++) {
            newBankItems[index] = newBankItems[index + 1];
            newBankItemsCount[index] = newBankItemsCount[index + 1];
          }

        } else {
          newBankItems[itemsCountOld] = item;
          newBankItemsCount[itemsCountOld] = itemCount;
          if (itemsCountOld >= newBankItemCount) {
            newBankItemCount = itemsCountOld + 1;
          }
        }
        updateBankItems();
        return;
      }
      if (opcode == Command.Server.SV_INVENTORY_ITEM_UPDATE) {
        int stackSize = 1;
        int slot = Utility.getUnsignedByte(pdata[offset++]);
        int mod = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        if (GameData.itemStackable[mod & 32767] == 0) {// 0x7fff
          if (Version.CLIENT > 204) {
            Utility.UInt3 u = Utility.getUnsignedInt3(pdata, offset);
            offset = u.newOffset;
            stackSize = u.result;
          } else {
            stackSize = Utility.getUnsignedInt(pdata, offset);
            if (stackSize >= 128) {
              offset += 4;
            } else {
              offset++;
            }
          }
        }
        inventoryItemId[slot] = mod & 32767;// 0x7fff
        inventoryEquipped[slot] = mod / 32768;
        inventoryItemStackCount[slot] = stackSize;
        if (slot >= inventoryItemsCount) {
          inventoryItemsCount = slot + 1;
        }
        return;
      }
      if (opcode == Command.Server.SV_INVENTORY_ITEM_REMOVE) {
        int slot = Utility.getUnsignedByte(pdata[offset++]);
        inventoryItemsCount--;
        for (int index = slot; index < inventoryItemsCount; index++) {
          inventoryItemId[index] = inventoryItemId[index + 1];
          inventoryItemStackCount[index] = inventoryItemStackCount[index + 1];
          inventoryEquipped[index] = inventoryEquipped[index + 1];
        }

        return;
      }
      if (opcode == Command.Server.SV_PLAYER_STAT_UPDATE) {
        int stat = Utility.getUnsignedByte(pdata[offset++]);
        playerStatCurrent[stat] = Utility.getUnsignedByte(pdata[offset++]);
        playerStatBase[stat] = Utility.getUnsignedByte(pdata[offset++]);
        playerExperience[stat] = Utility.getUnsignedInt(pdata, offset);
        offset += 4;
        return;
      }
      if (opcode == Command.Server.SV_DUEL_OPPONENT_ACCEPTED) {
        byte accepted = pdata[offset++];
        if (accepted == 1) {
          duelOfferOpponentAccepted = true;
          return;
        } else {
          duelOfferOpponentAccepted = false;
          return;
        }
      }
      if (opcode == Command.Server.SV_DUEL_ACCEPTED) {
        byte accepted = pdata[offset++];
        if (accepted == 1) {
          duelOfferAccepted = true;
          return;
        } else {
          duelOfferAccepted = false;
          return;
        }
      }
      if (opcode == Command.Server.SV_DUEL_CONFIRM_OPEN) {
        showDialogDuelConfirm = true;
        duelAccepted = false;
        showDialogDuel = false;
        if (Version.CLIENT > 204) {
          Utility.Gjstr2 g1 = Utility.gjstr2(pdata, offset);
          offset = g1.newOffset;
          duelOpponentName = g1.result;
        } else {
          duelOpponentNameHash = Utility.getUnsignedLong(pdata, offset);
        }
        offset += 8;
        duelOpponentItemsCount = Utility.getUnsignedByte(pdata[offset++]);
        for (int index = 0; index < duelOpponentItemsCount; index++) {
          duelOpponentItems[index] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          duelOpponentItemCount[index] = Utility.getUnsignedInt(pdata, offset);
          offset += 4;
        }

        duelItemsCount = Utility.getUnsignedByte(pdata[offset++]);
        for (int index = 0; index < duelItemsCount; index++) {
          duelItems[index] = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          duelItemCount[index] = Utility.getUnsignedInt(pdata, offset);
          offset += 4;
        }

        duelOptionRetreat = Utility.getUnsignedByte(pdata[offset++]);
        duelOptionMagic = Utility.getUnsignedByte(pdata[offset++]);
        duelOptionPrayer = Utility.getUnsignedByte(pdata[offset++]);
        duelOptionWeapons = Utility.getUnsignedByte(pdata[offset++]);
        return;
      }
      if (opcode == Command.Server.SV_SOUND) {
        String s;
        if (Version.CLIENT > 204) {
          Utility.Gjstr2 g1 = Utility.gjstr2(pdata, offset);
          offset = g1.newOffset;
          s = g1.result;
        } else {
          s = Utility.byteToString(pdata, offset, psize - 1);
        }
        playSoundFile(s);
        return;
      }
      if (opcode == Command.Server.SV_TELEPORT_BUBBLE) {
        if (teleportBubbleCount < 50) {
          int type = Utility.getUnsignedByte(pdata[offset++]);
          int x = pdata[offset++] + localRegionX;
          int y = pdata[offset++] + localRegionY;
          teleportBubbleType[teleportBubbleCount] = type;
          teleportBubbleTime[teleportBubbleCount] = 0;
          teleportBubbleX[teleportBubbleCount] = x;
          teleportBubbleY[teleportBubbleCount] = y;
          teleportBubbleCount++;
        }
        return;
      }
      if (opcode == Command.Server.SV_WELCOME) {
        if (!welcomScreenAlreadyShown) {
          welcomeLastLoggedInIP = Utility.getUnsignedInt(pdata, offset);
          offset += 4;
          welcomeLastLoggedInDays = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          welcomeRecoverySetDays = Utility.getUnsignedByte(pdata[offset++]);
          welcomeUnreadMessages = Utility.getUnsignedShort(pdata, offset);
          offset += 2;
          showDialogWelcome = true;
          welcomScreenAlreadyShown = true;
          welcomeLastLoggedInHost = null;
        }
        return;
      }
      if (opcode == Command.Server.SV_SERVER_MESSAGE) {
        if (Version.CLIENT > 204) {
          Utility.Gjstr2 g1 = Utility.gjstr2(pdata, offset);
          offset = g1.newOffset;
          serverMessage = g1.result;
        } else {
          serverMessage = Utility.byteToString(pdata, offset, psize - 1);
        }
        showDialogServermessage = true;
        serverMessageBoxTop = false;
        return;
      }
      if (opcode == Command.Server.SV_SERVER_MESSAGE_ONTOP) {
        if (Version.CLIENT > 204) {
          Utility.Gjstr2 g1 = Utility.gjstr2(pdata, offset);
          offset = g1.newOffset;
          serverMessage = g1.result;
        } else {
          serverMessage = Utility.byteToString(pdata, offset, psize - 1);
        }
        showDialogServermessage = true;
        serverMessageBoxTop = true;
        return;
      }
      if (opcode == Command.Server.SV_PLAYER_STAT_FATIGUE) {
        statFatigue = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        return;
      }
      if (opcode == Command.Server.SV_SLEEP_OPEN) {
        if (!isSleeping) {
          fatigueSleeping = statFatigue;
        }
        isSleeping = true;
        super.inputTextCurrent = "";
        super.inputTextFinal = "";
        surface.readSleepWord(spriteTexture + 1, pdata);
        sleepingStatusText = null;
        return;
      }
      if (opcode == Command.Server.SV_PLAYER_STAT_FATIGUE_ASLEEP) {
        fatigueSleeping = Utility.getUnsignedShort(pdata, offset);
        offset += 2;
        return;
      }
      if (opcode == Command.Server.SV_SLEEP_CLOSE) {
        isSleeping = false;
        return;
      }
      if (opcode == Command.Server.SV_SLEEP_INCORRECT) {
        sleepingStatusText = "Incorrect - Please wait...";
        return;
      }
      if (opcode == Command.Server.SV_SYSTEM_UPDATE) {
        systemUpdate = Utility.getUnsignedShort(pdata, offset) * 32;
        offset += 2;
        return;
      }
      if (Version.CLIENT > 204 && opcode == Command.Server.SV_UNKNOWN_213) {
        System.out.println("!! opcode 213");
        return;
      }
    } catch (RuntimeException runtimeexception) {
      runtimeexception.printStackTrace();
      if (Version.CLIENT <= 204) {
        if (packetErrorCount < 3) {
          String s1 = runtimeexception.toString();
          int slen = s1.length();
          super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PACKET_EXCEPTION));
          super.clientStream.putShort(slen);
          super.clientStream.putString(s1);
          super.clientStream.putShort(slen = (s1 = "p-type: " + opcode + "(" + ptype + ") p-size:" + psize).length());
          super.clientStream.putString(s1);
          super.clientStream.putShort(slen = (s1 = "rx:" +
                                                   localRegionX +
                                                   " ry:" +
                                                   localRegionY +
                                                   " num3l:" +
                                                   objectCount).length());
          super.clientStream.putString(s1);
          s1 = "";
          for (int l18 = 0; l18 < 80 && l18 < psize; l18++) {
            s1 = s1 + pdata[l18] + " ";
          }
          super.clientStream.putShort(s1.length());
          super.clientStream.putString(s1);
          //super.clientStream.sendPacket();
          packetErrorCount++;
        }
        super.clientStream.closeStream();
        resetLoginVars();
      }
    }
  }

  protected void handleInputs() {
    if (errorLoadingCodebase) {
      return;
    }
    if (errorLoadingMemory) {
      return;
    }
    if (errorLoadingData) {
      return;
    }
    try {
      loginTimer++;
      if (loggedIn == 0) {
        super.mouseActionTimeout = 0;
        handleLoginScreenInput();
      }
      if (loggedIn == 1) {
        super.mouseActionTimeout++;
        handleGameInput();
      }
      super.lastMouseButtonDown = 0;
      //super.unusedKeyCode2 = 0;
      cameraRotationTime++;
      if (cameraRotationTime > 500) {
        cameraRotationTime = 0;
        int i = (int) (Math.random() * 4D);
        if ((i & 1) == 1) {
          cameraRotationX += cameraRotationXIncrement;
        }
        if ((i & 2) == 2) {
          cameraRotationY += cameraRotationYIncrement;
        }
      }
      if (cameraRotationX < -50) {
        cameraRotationXIncrement = 2;
      }
      if (cameraRotationX > 50) {
        cameraRotationXIncrement = -2;
      }
      if (cameraRotationY < -50) {
        cameraRotationYIncrement = 2;
      }
      if (cameraRotationY > 50) {
        cameraRotationYIncrement = -2;
      }
      if (messageTabFlashAll > 0) {
        messageTabFlashAll--;
      }
      if (messageTabFlashHistory > 0) {
        messageTabFlashHistory--;
      }
      if (messtageTabFlashQuest > 0) {
        messtageTabFlashQuest--;
      }
      if (messageTabFlashPrivate > 0) {
        messageTabFlashPrivate--;
        return;
      }
    } catch (OutOfMemoryError Ex) {
      disposeAndCollect();
      errorLoadingMemory = true;
    }
  }

  private void handleLoginScreenInput() {
    if (super.worldFullTimeout > 0) {
      super.worldFullTimeout--;
    }
    if (loginScreen == 0) {
      panelLoginWelcome.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
      if (panelLoginWelcome.isClicked(controlWelcomeNewuser)) {
        loginScreen = 1;
      }
      if (panelLoginWelcome.isClicked(controlWelcomeExistinguser)) {
        loginScreen = 2;
        panelLoginExistinguser.updateText(controlLoginStatus, "Please enter your username and password");
        panelLoginExistinguser.updateText(controlLoginUser, username);
        panelLoginExistinguser.updateText(controlLoginPass, password);
        panelLoginExistinguser.setFocus(controlLoginUser);
        return;
      }
    } else if (loginScreen == 1) {
      panelLoginNewuser.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
      if (panelLoginNewuser.isClicked(controlLoginNewOk)) {
        loginScreen = 0;
        return;
      }
    } else if (loginScreen == 2) {
      panelLoginExistinguser.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
      if (panelLoginExistinguser.isClicked(controlLoginCancel)) {
        loginScreen = 0;
      }
      if (panelLoginExistinguser.isClicked(controlLoginUser)) {
        panelLoginExistinguser.setFocus(controlLoginPass);
      }
      if (panelLoginExistinguser.isClicked(controlLoginPass) || panelLoginExistinguser.isClicked(controlLoginOk)) {
        loginUser = panelLoginExistinguser.getText(controlLoginUser);
        loginPass = panelLoginExistinguser.getText(controlLoginPass);
        login(loginUser, loginPass, false);
      }
    }
  }

  private void handleGameInput() {
    if (systemUpdate > 1) {
      systemUpdate--;
    }
    checkConnection();
    if (logoutTimeout > 0) {
      logoutTimeout--;
    }
        /*if (super.mouseActionTimeout > 4500 && combatTimeout == 0 && logoutTimeout == 0) {
            super.mouseActionTimeout -= 500;
            sendLogout();
            return;
        }*/ // idle timeout
    if (localPlayer.animationCurrent == 8 || localPlayer.animationCurrent == 9) {
      combatTimeout = 500;
    }
    if (combatTimeout > 0) {
      combatTimeout--;
    }
    if (showAppearanceChange) {
      handleAppearancePanelControls();
      return;
    }
    for (int i = 0; i < playerCount; i++) {
      GameCharacter character = players[i];
      int k = (character.waypointCurrent + 1) % 10;
      if (character.movingStep != k) {
        int i1 = -1;
        int l2 = character.movingStep;
        int j4;
        if (l2 < k) {
          j4 = k - l2;
        } else {
          j4 = (10 + k) - l2;
        }
        int j5 = 4;
        if (j4 > 2) {
          j5 = (j4 - 1) * 4;
        }
        if (character.waypointsX[l2] - character.currentX > magicLoc * 3 ||
            character.waypointsY[l2] - character.currentY > magicLoc * 3 ||
            character.waypointsX[l2] - character.currentX < -magicLoc * 3 ||
            character.waypointsY[l2] - character.currentY < -magicLoc * 3 ||
            j4 > 8) {
          character.currentX = character.waypointsX[l2];
          character.currentY = character.waypointsY[l2];
        } else {
          if (character.currentX < character.waypointsX[l2]) {
            character.currentX += j5;
            character.stepCount++;
            i1 = 2;
          } else if (character.currentX > character.waypointsX[l2]) {
            character.currentX -= j5;
            character.stepCount++;
            i1 = 6;
          }
          if (character.currentX - character.waypointsX[l2] < j5 &&
              character.currentX - character.waypointsX[l2] > -j5) {
            character.currentX = character.waypointsX[l2];
          }
          if (character.currentY < character.waypointsY[l2]) {
            character.currentY += j5;
            character.stepCount++;
            if (i1 == -1) {
              i1 = 4;
            } else if (i1 == 2) {
              i1 = 3;
            } else {
              i1 = 5;
            }
          } else if (character.currentY > character.waypointsY[l2]) {
            character.currentY -= j5;
            character.stepCount++;
            if (i1 == -1) {
              i1 = 0;
            } else if (i1 == 2) {
              i1 = 1;
            } else {
              i1 = 7;
            }
          }
          if (character.currentY - character.waypointsY[l2] < j5 &&
              character.currentY - character.waypointsY[l2] > -j5) {
            character.currentY = character.waypointsY[l2];
          }
        }
        if (i1 != -1) {
          character.animationCurrent = i1;
        }
        if (character.currentX == character.waypointsX[l2] && character.currentY == character.waypointsY[l2]) {
          character.movingStep = (l2 + 1) % 10;
        }
      } else {
        character.animationCurrent = character.animationNext;
      }
      if (character.messageTimeout > 0) {
        character.messageTimeout--;
      }
      if (character.bubbleTimeout > 0) {
        character.bubbleTimeout--;
      }
      if (character.combatTimer > 0) {
        character.combatTimer--;
      }
      if (deathScreenTimeout > 0) {
        deathScreenTimeout--;
        if (deathScreenTimeout == 0) {
          showMessage("You have been granted another life. Be more careful this time!", 3);
        }
        if (deathScreenTimeout == 0) {
          showMessage("You retain your skills. Your objects land where you died", 3);
        }
      }
    }

    for (int j = 0; j < npcCount; j++) {
      GameCharacter character_1 = npcs[j];
      if (character_1 == null) {
        continue;
      }
      int j1 = (character_1.waypointCurrent + 1) % 10;
      if (character_1.movingStep != j1) {
        int i3 = -1;
        int k4 = character_1.movingStep;
        int k5;
        if (k4 < j1) {
          k5 = j1 - k4;
        } else {
          k5 = (10 + j1) - k4;
        }
        int l5 = 4;
        if (k5 > 2) {
          l5 = (k5 - 1) * 4;
        }
        if (character_1.waypointsX[k4] - character_1.currentX > magicLoc * 3 ||
            character_1.waypointsY[k4] - character_1.currentY > magicLoc * 3 ||
            character_1.waypointsX[k4] - character_1.currentX < -magicLoc * 3 ||
            character_1.waypointsY[k4] - character_1.currentY < -magicLoc * 3 ||
            k5 > 8) {
          character_1.currentX = character_1.waypointsX[k4];
          character_1.currentY = character_1.waypointsY[k4];
        } else {
          if (character_1.currentX < character_1.waypointsX[k4]) {
            character_1.currentX += l5;
            character_1.stepCount++;
            i3 = 2;
          } else if (character_1.currentX > character_1.waypointsX[k4]) {
            character_1.currentX -= l5;
            character_1.stepCount++;
            i3 = 6;
          }
          if (character_1.currentX - character_1.waypointsX[k4] < l5 &&
              character_1.currentX - character_1.waypointsX[k4] > -l5) {
            character_1.currentX = character_1.waypointsX[k4];
          }
          if (character_1.currentY < character_1.waypointsY[k4]) {
            character_1.currentY += l5;
            character_1.stepCount++;
            if (i3 == -1) {
              i3 = 4;
            } else if (i3 == 2) {
              i3 = 3;
            } else {
              i3 = 5;
            }
          } else if (character_1.currentY > character_1.waypointsY[k4]) {
            character_1.currentY -= l5;
            character_1.stepCount++;
            if (i3 == -1) {
              i3 = 0;
            } else if (i3 == 2) {
              i3 = 1;
            } else {
              i3 = 7;
            }
          }
          if (character_1.currentY - character_1.waypointsY[k4] < l5 &&
              character_1.currentY - character_1.waypointsY[k4] > -l5) {
            character_1.currentY = character_1.waypointsY[k4];
          }
        }
        if (i3 != -1) {
          character_1.animationCurrent = i3;
        }
        if (character_1.currentX == character_1.waypointsX[k4] && character_1.currentY == character_1.waypointsY[k4]) {
          character_1.movingStep = (k4 + 1) % 10;
        }
      } else {
        character_1.animationCurrent = character_1.animationNext;
        if (character_1.npcId == 43) {
          character_1.stepCount++;
        }
      }
      if (character_1.messageTimeout > 0) {
        character_1.messageTimeout--;
      }
      if (character_1.bubbleTimeout > 0) {
        character_1.bubbleTimeout--;
      }
      if (character_1.combatTimer > 0) {
        character_1.combatTimer--;
      }
    }

    if (showUiTab != 2) {
      if (Surface.anInt346 > 0) {
        sleepWordDelayTimer++;
      }
      if (Surface.anInt347 > 0) {
        sleepWordDelayTimer = 0;
      }
      Surface.anInt346 = 0;
      Surface.anInt347 = 0;
    }
    for (int l = 0; l < playerCount; l++) {
      GameCharacter character = players[l];
      if (character.projectileRange > 0) {
        character.projectileRange--;
      }
    }

    if (cameraAutoAngleDebug) {
      if (cameraAutoRotatePlayerX - localPlayer.currentX < -500 ||
          cameraAutoRotatePlayerX - localPlayer.currentX > 500 ||
          cameraAutoRotatePlayerY - localPlayer.currentY < -500 ||
          cameraAutoRotatePlayerY - localPlayer.currentY > 500) {
        cameraAutoRotatePlayerX = localPlayer.currentX;
        cameraAutoRotatePlayerY = localPlayer.currentY;
      }
    } else {
      if (cameraAutoRotatePlayerX - localPlayer.currentX < -500 ||
          cameraAutoRotatePlayerX - localPlayer.currentX > 500 ||
          cameraAutoRotatePlayerY - localPlayer.currentY < -500 ||
          cameraAutoRotatePlayerY - localPlayer.currentY > 500) {
        cameraAutoRotatePlayerX = localPlayer.currentX;
        cameraAutoRotatePlayerY = localPlayer.currentY;
      }
      if (cameraAutoRotatePlayerX != localPlayer.currentX) {
        cameraAutoRotatePlayerX += (localPlayer.currentX - cameraAutoRotatePlayerX) / (16 + (cameraZoom - 500) / 15);
      }
      if (cameraAutoRotatePlayerY != localPlayer.currentY) {
        cameraAutoRotatePlayerY += (localPlayer.currentY - cameraAutoRotatePlayerY) / (16 + (cameraZoom - 500) / 15);
      }
      if (optionCameraModeAuto) {
        int k1 = cameraAngle * 32;
        int j3 = k1 - cameraRotation;
        byte byte0 = 1;
        if (j3 != 0) {
          anInt707++;
          if (j3 > 128) {
            byte0 = -1;
            j3 = 256 - j3;
          } else if (j3 > 0) {
            byte0 = 1;
          } else if (j3 < -128) {
            byte0 = 1;
            j3 = 256 + j3;
          } else if (j3 < 0) {
            byte0 = -1;
            j3 = -j3;
          }
          cameraRotation += ((anInt707 * j3 + 255) / 256) * byte0;
          cameraRotation &= 255;// 0xff;
        } else {
          anInt707 = 0;
        }
      }
    }
    if (sleepWordDelayTimer > 20) {
      sleepWordDelay = false;
      sleepWordDelayTimer = 0;
    }
    if (isSleeping) {
      if (super.inputTextFinal.length() > 0) {
        if (super.inputTextFinal.equalsIgnoreCase("::lostcon") && !appletMode) {
          super.clientStream.closeStream();
        } else if (super.inputTextFinal.equalsIgnoreCase("::closecon") && !appletMode) {
          closeConnection(true);
        } else {
          super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SLEEP_WORD));
          if (Version.CLIENT > 204) {
            if (!sleepWordDelay) {
              super.clientStream.putByte(0);
              sleepWordDelay = true;
            } else {
              super.clientStream.putByte(1);
            }
            super.clientStream.pjstr2(super.inputTextFinal);
          } else {
            super.clientStream.putString(super.inputTextFinal);
            if (!sleepWordDelay) {
              super.clientStream.putByte(0);
              sleepWordDelay = true;
            }
          }
          super.clientStream.sendPacket();
          super.inputTextCurrent = "";
          super.inputTextFinal = "";
          sleepingStatusText = "Please wait...";
        }
      }
      if (super.lastMouseButtonDown == 1 &&
          super.mouseY > gameHeight / 2 + 108 &&
          super.mouseY < gameHeight / 2 + 143 &&
          super.mouseX > gameWidth / 2 - 200 &&
          super.mouseX < gameWidth / 2 + 200) {
        super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SLEEP_WORD));
        if (Version.CLIENT > 204) {
          if (!sleepWordDelay) {
            super.clientStream.putByte(0);
            sleepWordDelay = true;
          } else {
            super.clientStream.putByte(1);
          }
          super.clientStream.pjstr2("-null-");
        } else {
          super.clientStream.putString("-null-");
          if (!sleepWordDelay) {
            super.clientStream.putByte(0);
            sleepWordDelay = true;
          }
        }
        super.clientStream.sendPacket();
        super.inputTextCurrent = "";
        super.inputTextFinal = "";
        sleepingStatusText = "Please wait...";
      }
      super.lastMouseButtonDown = 0;
      return;
    }
    if (super.mouseY > gameHeight - 4) {
      if (super.mouseX > gameWidth / 2 - 241 && super.mouseX < gameWidth / 2 - 160 && super.lastMouseButtonDown == 1) {
        messageTabSelected = 0;
      }
      if (super.mouseX > gameWidth / 2 - 136 && super.mouseX < gameWidth / 2 - 62 && super.lastMouseButtonDown == 1) {
        messageTabSelected = 1;
        panelMessageTabs.controlFlashText[controlTextListChat] = 999999;//0xf423f;
      }
      if (super.mouseX > gameWidth / 2 - 41 && super.mouseX < gameWidth / 2 + 39 && super.lastMouseButtonDown == 1) {
        messageTabSelected = 2;
        panelMessageTabs.controlFlashText[controlTextListQuest] = 999999;//0xf423f;
      }
      if (super.mouseX > gameWidth / 2 + 59 && super.mouseX < gameWidth / 2 + 139 && super.lastMouseButtonDown == 1) {
        messageTabSelected = 3;
        panelMessageTabs.controlFlashText[controlTextListPrivate] = 999999;//0xf423f;
      }
      if (super.mouseX > gameWidth / 2 + 161 && super.mouseX < gameWidth / 2 + 241 && super.lastMouseButtonDown == 1) {
        showDialogReportAbuseStep = 1;
        reportAbuseOffence = 0;
        super.inputTextCurrent = "";
        super.inputTextFinal = "";
      }
      super.lastMouseButtonDown = 0;
      super.mouseButtonDown = 0;
    }
    panelMessageTabs.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
    if (messageTabSelected > 0 && super.mouseX >= gameWidth / 2 + 238 && super.mouseY >= gameHeight - 66) {
      super.lastMouseButtonDown = 0;
    }
    if (panelMessageTabs.isClicked(controlTextListAll)) {
      String s = panelMessageTabs.getText(controlTextListAll);
      panelMessageTabs.updateText(controlTextListAll, "");
      if (s.startsWith("::")) {
        s = s.substring(2);
        if (s.equalsIgnoreCase("closecon") && !appletMode) {
          super.clientStream.closeStream();
        } else if (s.equalsIgnoreCase("logout") && !appletMode) {
          closeConnection(true);
        } else if (s.equalsIgnoreCase("lostcon") && !appletMode) {
          lostConnection();
        } else {
          java.util.List<Object> kek = Manager.run("handleCommand", s);
          if (!kek.contains(true) && !kek.contains(Exception.class)) {
            sendCommandString(s);
          }
        }
      } else {
        if (Version.CLIENT > 204) {
          sendChatMessage(s);
        } else {
          byte[] msg = Utility.stringToByteArray(s);
          sendChatMessage(msg, msg.length);
          s = WordFilter.filter(s);
          localPlayer.messageTimeout = 150;
          localPlayer.message = s;
          showMessage(localPlayer.name + ": " + s, 2);
        }
      }
    }
    if (messageTabSelected == 0) {
      for (int l1 = 0; l1 < messageShitSize; l1++) {
        if (messageHistoryTimeout[l1] > 0) {
          messageHistoryTimeout[l1]--;
        }
      }

    }
    if (deathScreenTimeout != 0) {
      super.lastMouseButtonDown = 0;
    }
    if (showDialogTrade || showDialogDuel) {
      if (super.mouseButtonDown != 0) {
        mouseButtonDownTime++;
      } else {
        mouseButtonDownTime = 0;
      }
      if (mouseButtonDownTime > 600) {
        mouseButtonItemCountIncrement += 5000;
      } else if (mouseButtonDownTime > 450) {
        mouseButtonItemCountIncrement += 500;
      } else if (mouseButtonDownTime > 300) {
        mouseButtonItemCountIncrement += 50;
      } else if (mouseButtonDownTime > 150) {
        mouseButtonItemCountIncrement += 5;
      } else if (mouseButtonDownTime > 50) {
        mouseButtonItemCountIncrement++;
      } else if (mouseButtonDownTime > 20 && (mouseButtonDownTime & 5) == 0) {
        mouseButtonItemCountIncrement++;
      }
    } else {
      mouseButtonDownTime = 0;
      mouseButtonItemCountIncrement = 0;
    }
    if (super.lastMouseButtonDown == 1) {
      mouseButtonClick = 1;
    } else if (super.lastMouseButtonDown == 2) {
      mouseButtonClick = 2;
    }
    scene.setMouseLoc(super.mouseX, super.mouseY);
    super.lastMouseButtonDown = 0;
    if (optionCameraModeAuto) {
      if (anInt707 == 0 || cameraAutoAngleDebug) {
        if (super.keyLeft) {
          cameraAngle = cameraAngle + 1 & 7;
          super.keyLeft = false;
          if (zoomControls || !fogOfWar) {
            if ((cameraAngle & 1) == 0) {
              cameraAngle = cameraAngle + 1 & 7;
            }
            for (int i2 = 0; i2 < 8; i2++) {
              if (isValidCameraAngle(cameraAngle)) {
                break;
              }
              cameraAngle = cameraAngle + 1 & 7;
            }

          }
        }
        if (super.keyRight) {
          cameraAngle = cameraAngle + 7 & 7;
          super.keyRight = false;
          if (zoomControls || !fogOfWar) {
            if ((cameraAngle & 1) == 0) {
              cameraAngle = cameraAngle + 7 & 7;
            }
            for (int j2 = 0; j2 < 8; j2++) {
              if (isValidCameraAngle(cameraAngle)) {
                break;
              }
              cameraAngle = cameraAngle + 7 & 7;
            }

          }
        }
      }
    } else if (super.keyLeft) {
      cameraRotation = cameraRotation + 2 & 255;// 0xff;
    } else if (super.keyRight) {
      cameraRotation = cameraRotation - 2 & 255;// 0xff;
    }
    if (!zoomControls && (fogOfWar && cameraZoom > 550)) {
      cameraZoom -= 4;
    } else if (!zoomControls && (!fogOfWar && cameraZoom < 750)) {
      cameraZoom += 4;
    }
    if (mouseClickXStep > 0) {
      mouseClickXStep--;
    } else if (mouseClickXStep < 0) {
      mouseClickXStep++;
    }
    scene.doSOemthingWithTheFuckinFountainFuck(17);// 17 is fountain
    objectAnimationCount++;
    if (objectAnimationCount > 5) {
      objectAnimationCount = 0;
      objectAnimationNumberFireLightningSpell = (objectAnimationNumberFireLightningSpell + 1) % 3;
      objectAnimationNumberTorch = (objectAnimationNumberTorch + 1) % 4;
      objectAnimationNumberClaw = (objectAnimationNumberClaw + 1) % 5;
    }
    for (int k2 = 0; k2 < objectCount; k2++) {
      int l3 = objectX[k2];
      int l4 = objectY[k2];
      if (l3 >= 0 && l4 >= 0 && l3 < 96 && l4 < 96 && objectId[k2] == 74) {
        objectModel[k2].rotate(1, 0, 0);
      }
    }

    for (int i4 = 0; i4 < teleportBubbleCount; i4++) {
      teleportBubbleTime[i4]++;
      if (teleportBubbleTime[i4] > 50) {
        teleportBubbleCount--;
        for (int i5 = i4; i5 < teleportBubbleCount; i5++) {
          teleportBubbleX[i5] = teleportBubbleX[i5 + 1];
          teleportBubbleY[i5] = teleportBubbleY[i5 + 1];
          teleportBubbleTime[i5] = teleportBubbleTime[i5 + 1];
          teleportBubbleType[i5] = teleportBubbleType[i5 + 1];
        }

      }
    }

  }

  private void handleAppearancePanelControls() {
    panelAppearance.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
    if (panelAppearance.isClicked(controlButtonAppearanceHead1)) {
      do {
        appearanceHeadType = ((appearanceHeadType - 1) + GameData.animationCount) % GameData.animationCount;
      } while ((GameData.animationSomething[appearanceHeadType] & 3) != 1 ||
               (GameData.animationSomething[appearanceHeadType] & 4 * appearanceHeadGender) == 0);
    }
    if (panelAppearance.isClicked(controlButtonAppearanceHead2)) {
      do {
        appearanceHeadType = (appearanceHeadType + 1) % GameData.animationCount;
      } while ((GameData.animationSomething[appearanceHeadType] & 3) != 1 ||
               (GameData.animationSomething[appearanceHeadType] & 4 * appearanceHeadGender) == 0);
    }
    if (panelAppearance.isClicked(controlButtonAppearanceHair1)) {
      appearanceHairColour = ((appearanceHairColour - 1) + characterHairColours.length) % characterHairColours.length;
    }
    if (panelAppearance.isClicked(controlButtonAppearanceHair2)) {
      appearanceHairColour = (appearanceHairColour + 1) % characterHairColours.length;
    }
    if (panelAppearance.isClicked(controlButtonAppearanceGender1) ||
        panelAppearance.isClicked(controlButtonAppearanceGender2)) {
      for (appearanceHeadGender = 3 - appearanceHeadGender; (GameData.animationSomething[appearanceHeadType] & 3) !=
                                                            1 ||
                                                            (GameData.animationSomething[appearanceHeadType] &
                                                             4 * appearanceHeadGender) ==
                                                            0; appearanceHeadType = (appearanceHeadType + 1) %
                                                                                    GameData.animationCount) {
      }
      for (; (GameData.animationSomething[appearanceBodyGender] & 3) != 2 ||
             (GameData.animationSomething[appearanceBodyGender] & 4 * appearanceHeadGender) == 0; appearanceBodyGender =
             (appearanceBodyGender + 1) %
             GameData.animationCount) {
      }
    }
    if (panelAppearance.isClicked(controlButtonAppearanceTop1)) {
      appearanceTopColour = ((appearanceTopColour - 1) + characterTopBottomColours.length) %
                            characterTopBottomColours.length;
    }
    if (panelAppearance.isClicked(controlButtonAppearanceTop2)) {
      appearanceTopColour = (appearanceTopColour + 1) % characterTopBottomColours.length;
    }
    if (panelAppearance.isClicked(controlButtonAppearanceSkin1)) {
      appearanceSkinColour = ((appearanceSkinColour - 1) + characterSkinColours.length) % characterSkinColours.length;
    }
    if (panelAppearance.isClicked(controlButtonAppearanceSkin2)) {
      appearanceSkinColour = (appearanceSkinColour + 1) % characterSkinColours.length;
    }
    if (panelAppearance.isClicked(controlButtonAppearanceBottom1)) {
      appearanceBottomColour = ((appearanceBottomColour - 1) + characterTopBottomColours.length) %
                               characterTopBottomColours.length;
    }
    if (panelAppearance.isClicked(controlButtonAppearanceBottom2)) {
      appearanceBottomColour = (appearanceBottomColour + 1) % characterTopBottomColours.length;
    }
    if (panelAppearance.isClicked(controlButtonAppearanceAccept)) {
      super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_APPEARANCE));
      super.clientStream.putByte(appearanceHeadGender);
      super.clientStream.putByte(appearanceHeadType);
      super.clientStream.putByte(appearanceBodyGender);
      super.clientStream.putByte(appearance2Colour);
      super.clientStream.putByte(appearanceHairColour);
      super.clientStream.putByte(appearanceTopColour);
      super.clientStream.putByte(appearanceBottomColour);
      super.clientStream.putByte(appearanceSkinColour);
      super.clientStream.sendPacket();
      surface.blackScreen();
      showAppearanceChange = false;
    }
  }

  protected void lostConnection() {
    systemUpdate = 0;
    if (logoutTimeout != 0) {
      resetLoginVars();
      return;
    } else {
      super.lostConnection();
      return;
    }
  }

  private boolean isValidCameraAngle(int i) {
    int j = localPlayer.currentX / 128;
    int k = localPlayer.currentY / 128;
    for (int l = 2; l >= 1; l--) {
      if (i == 1 &&
          ((world.objectAdjacency[j][k - l] & 128) == 128 ||
           (world.objectAdjacency[j - l][k] & 128) == 128 ||
           (world.objectAdjacency[j - l][k - l] & 128) == 128))// 0x80
      {
        return false;
      }
      if (i == 3 &&
          ((world.objectAdjacency[j][k + l] & 128) == 128 ||
           (world.objectAdjacency[j - l][k] & 128) == 128 ||
           (world.objectAdjacency[j - l][k + l] & 128) == 128))// 0x80
      {
        return false;
      }
      if (i == 5 &&
          ((world.objectAdjacency[j][k + l] & 128) == 128 ||
           (world.objectAdjacency[j + l][k] & 128) == 128 ||
           (world.objectAdjacency[j + l][k + l] & 128) == 128))// 0x80
      {
        return false;
      }
      if (i == 7 &&
          ((world.objectAdjacency[j][k - l] & 128) == 128 ||
           (world.objectAdjacency[j + l][k] & 128) == 128 ||
           (world.objectAdjacency[j + l][k - l] & 128) == 128))// 0x80
      {
        return false;
      }
      if (i == 0 && (world.objectAdjacency[j][k - l] & 128) == 128)// 0x80
      {
        return false;
      }
      if (i == 2 && (world.objectAdjacency[j - l][k] & 128) == 128)// 0x80
      {
        return false;
      }
      if (i == 4 && (world.objectAdjacency[j][k + l] & 128) == 128)// 0x80
      {
        return false;
      }
      if (i == 6 && (world.objectAdjacency[j + l][k] & 128) == 128)// 0x80
      {
        return false;
      }
    }

    return true;
  }

  protected void resetLoginVars() {
    systemUpdate = 0;
    loginScreen = 2;
    loggedIn = 0;
    logoutTimeout = 0;
  }

  public enum MinimapEntityType {
    OBJECT, GROUNDITEM, NPC, PLAYER
  }

  public static class MinimapEntity {
    public MinimapEntityType type;
    public int x;
    public int y;
    public int index;

    MinimapEntity(int x, int y, int index, MinimapEntityType type) {
      this.x = x;
      this.y = y;
      this.index = index;
      this.type = type;
    }
  }


    /* unused
    private String recoveryQuestions[] = {
            "Where were you born?", "What was your first teachers name?", "What is your fathers middle name?", "Who was your first best friend?", "What is your favourite vacation spot?", "What is your mothers middle name?", "What was your first pets name?", "What was the name of your first school?", "What is your mothers maiden name?", "Who was your first boyfriend/girlfriend?",
            "What was the first computer game you purchased?", "Who is your favourite actor/actress?", "Who is your favourite author?", "Who is your favourite musician?", "Who is your favourite cartoon character?", "What is your favourite book?", "What is your favourite food?", "What is your favourite movie?"
    };*/
}
