package org.firescape.server.packetbuilder.client;

import org.firescape.server.model.Bank;
import org.firescape.server.model.InvItem;
import org.firescape.server.model.Player;
import org.firescape.server.model.Shop;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.opcode.Command;
import org.firescape.server.opcode.Opcode;
import org.firescape.server.packetbuilder.RSCPacketBuilder;
import org.firescape.server.util.Formulae;
import org.firescape.server.util.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MiscPacketBuilder {

  /**
   * The player we are creating packets for
   */
  private final Player player;
  /**
   * List of packets waiting to be sent to the user
   */
  private final List<RSCPacket> packets = new ArrayList<RSCPacket>();

  /**
   * Constructs a new MiscPacketBuilder
   */
  public MiscPacketBuilder(Player player) {
    this.player = player;
  }

  /**
   * Gets a List of new packets since the last update
   */
  public List<RSCPacket> getPackets() {
    return this.packets;
  }

  /**
   * Clears old packets that have already been sent
   */
  public void clearPackets() {
    this.packets.clear();
  }

  /**
   * Tells the client to save a screenshot
   */
  public void sendScreenshot() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(181);
    //    packets.add(s.toPacket());
  }

  /**
   * Sends the players combat style
   */
  public void sendCombatStyle() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(129);
    //    s.addByte((byte) player.getCombatStyle());
    //    packets.add(s.toPacket());
  }

  /**
   * Updates the fatigue percentage
   */
  public void sendFatigue() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_PLAYER_STAT_FATIGUE));
    s.addShort(this.player.getFatigue());
    this.packets.add(s.toPacket());
  }

  /**
   * Druidic Ritual @author Yong Min
   */
  public void sendDruidicRitualComplete() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(137);
    //    if (player.getDruidicRitualStatus() == 4) {
    //      s.addShort(1);
    //    } else {
    //      s.addShort(0);
    //    }
    //    packets.add(s.toPacket());
  }

  /**
   * Romeo & Juliet @author Yong Min
   */
  public void sendRomeoJulietComplete() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(140);
    //    if (player.getRomeoJulietStatus() == 3) {
    //      s.addShort(1);
    //    } else {
    //      s.addShort(0);
    //    }
    //    packets.add(s.toPacket());
  }

  /**
   * Sheep Shearer @author Yong Min
   */
  public void sendSheepShearerComplete() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(141);
    //    if (player.getSheepShearerStatus() == 2) {
    //      s.addShort(1);
    //    } else {
    //      s.addShort(0);
    //    }
    //    packets.add(s.toPacket());
  }

  /**
   * Imp Catcher @author Yong Min
   */
  public void sendImpCatcherComplete() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(138);
    //    if (player.getImpCatcherStatus() == 2) {
    //      s.addShort(1);
    //    } else {
    //      s.addShort(0);
    //    }
    //    packets.add(s.toPacket());
  }

  /**
   * Witches Potion @author Yong Min
   */
  public void sendWitchPotionComplete() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(142);
    //    if (player.getWitchPotionStatus() == 3) {
    //      s.addShort(1);
    //    } else {
    //      s.addShort(0);
    //    }
    //    packets.add(s.toPacket());
  }

  /**
   * Doric's Quest @author Yong Min
   */
  public void sendDoricsQuestComplete() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(143);
    //    if (player.getDoricsQuestStatus() == 2) {
    //      s.addShort(1);
    //    } else {
    //      s.addShort(0);
    //    }
    //    packets.add(s.toPacket());
  }

  /**
   * Cook's Assistant @author Yong Min
   */
  public void sendCooksAssistantComplete() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(144);
    //    if (player.getCooksAssistantStatus() == 2) {
    //      s.addShort(1);
    //    } else {
    //      s.addShort(0);
    //    }
    //    packets.add(s.toPacket());
  }

  /**
   * Mute System
   */
  public void sendMute() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(109);
    //    s.addShort(player.getMute());
    //    packets.add(s.toPacket());
  }

  /**
   * Updates the quest points @author Yong Min
   */
  public void sendQuestPoints() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(128);
    //    s.addShort(player.getQuestPoints());
    //    packets.add(s.toPacket());
  }

  /**
   * Updates the kills @author Yong Min
   */
  public void sendKills() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(132);
    //    s.addShort(player.getKills());
    //    packets.add(s.toPacket());
  }

  /**
   * Updates the deaths @author Yong Min
   */
  public void sendDeaths() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(134);
    //    s.addShort(player.getDeaths());
    //    packets.add(s.toPacket());
  }

  /**
   * Updates the killing spree @author Yong Min
   */
  public void sendKillingSpree() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(206);
    //    s.addShort(player.getKillingSpree());
    //    packets.add(s.toPacket());
  }

  /**
   * Updates the guthix casts @author Yong Min
   */
  public void sendGuthixSpellCast() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(210);
    //    s.addShort(player.getGuthixSpellCast());
    //    packets.add(s.toPacket());
  }

  /**
   * Updates the saradomin casts @author Yong Min
   */
  public void sendSaradominSpellCast() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(213);
    //    s.addShort(player.getSaradominSpellCast());
    //    packets.add(s.toPacket());
  }

  /**
   * Updates the zamorak casts @author Yong Min
   */
  public void sendZamorakSpellCast() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(212);
    //    s.addShort(player.getZamorakSpellCast());
    //    packets.add(s.toPacket());
  }

  /**
   * Hides a question menu
   */
  public void hideMenu() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_OPTION_LIST_CLOSE));
    this.packets.add(s.toPacket());
  }

  /**
   * Shows a question menu
   */
  public void sendMenu(String[] options) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_OPTION_LIST));
    s.addByte((byte) options.length);
    for (String option : options) {
      s.addInt(option.length());
      try {
        s.addBytes(option.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    this.packets.add(s.toPacket());
  }

  /**
   * Show the bank window
   */
  public void showBank() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_BANK_OPEN));
    s.addByte((byte) this.player.getBank().size());
    s.addByte((byte) Bank.MAX_SIZE);
    for (InvItem i : this.player.getBank().getItems()) {
      s.addShort(i.getID());
      s.addInt(i.getAmount());
    }
    this.packets.add(s.toPacket());
  }

  /**
   * Hides the bank windows
   */
  public void hideBank() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_BANK_CLOSE));
    this.packets.add(s.toPacket());
  }

  /**
   * Updates the id and amount of an item in the bank
   */
  public void updateBankItem(int slot, int newId, int amount) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_BANK_UPDATE));
    s.addByte((byte) slot);
    s.addShort(newId);
    s.addInt(amount);
    this.packets.add(s.toPacket());
  }

  /**
   * Show the bank window
   */
  public void showShop(Shop shop) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_SHOP_OPEN));
    s.addByte((byte) shop.size());
    s.addByte((byte) (shop.isGeneral() ? 1 : 0));
    s.addByte((byte) shop.getSellModifier());
    s.addByte((byte) shop.getBuyModifier());
    for (InvItem i : shop.getItems()) {
      s.addShort(i.getID());
      s.addShort(i.getAmount());
      s.addShort(i.getDef().getBasePrice());
    }
    this.packets.add(s.toPacket());
  }

  /**
   * Hides the shop window
   */
  public void hideShop() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_SHOP_CLOSE));
    this.packets.add(s.toPacket());
  }

  /**
   * Sends a system update message
   */
  public void startShutdown(int seconds) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_SYSTEM_UPDATE));
    s.addShort((int) (((double) seconds / 32D) * 50));
    this.packets.add(s.toPacket());
  }

  /**
   * PvP tournament timer.
   */
  public void startPvp(int seconds) {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(173);
    //    s.addShort((int) (((double) seconds / 32D) * 50));
    //    System.out.println("Seconds:  " + seconds + "\tpID:  555\tData sent:  " + (int) (((double) seconds / 32D) *
    // 50)
    //            + "\tData Recieved:  " + (int) (((double) seconds / 32D) * 50) * 32);
    //    packets.add(s.toPacket());
  }

  /**
   * Sends a message box
   */
  public void sendAlert(String message, boolean big) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(big
            ? Opcode.getServer(204, Command.Server.SV_SERVER_MESSAGE)
            : Opcode.getServer(204, Command.Server.SV_SERVER_MESSAGE_ONTOP));
    try {
      s.addBytes(message.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    this.packets.add(s.toPacket());
  }

  /**
   * Sends a sound effect
   */
  public void sendSound(String soundName) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_SOUND));
    try {
      s.addBytes(soundName.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    this.packets.add(s.toPacket());
  }

  /**
   * Alert the client that they just died
   */
  public void sendDied() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_PLAYER_DIED));
    this.packets.add(s.toPacket());
  }

  /**
   * Send a private message
   */
  public void sendPrivateMessage(long usernameHash, byte[] message) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_FRIEND_MESSAGE));
    s.addLong(usernameHash);
    s.addInt(0);
    s.addBytes(message);
    this.packets.add(s.toPacket());
  }

  /**
   * Updates a friends login status
   */
  public void sendFriendUpdate(long usernameHash, int world) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_FRIEND_STATUS_CHANGE));
    s.addLong(usernameHash);
    this.packets.add(s.toPacket());
  }

  /**
   * Sends the whole friendlist
   */
  public void sendFriendList() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_FRIEND_LIST));
    s.addByte((byte) this.player.getFriendList().size());
    for (String friend : this.player.getFriendList()) {
      s.addLong(org.firescape.server.util.DataConversions.usernameToHash(friend));
      s.addByte((byte) 1);
    }
    this.packets.add(s.toPacket());
  }

  /**
   * Sends the whole ignore list
   */
  public void sendIgnoreList() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_IGNORE_LIST));
    s.addByte((byte) this.player.getIgnoreList().size());
    for (String user : this.player.getIgnoreList()) {
      s.addLong(org.firescape.server.util.DataConversions.usernameToHash(user));
    }
    this.packets.add(s.toPacket());
  }

  public void sendTradeAccept() {
    Player with = this.player.getWishToTrade();
    if (with == null) { // This shouldn't happen
      return;
    }
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(20);
    s.addLong(with.getUsernameHash());
    s.addByte((byte) with.getTradeOffer().size());
    for (InvItem item : with.getTradeOffer()) {
      s.addShort(item.getID());
      s.addInt(item.getAmount());
    }
    s.addByte((byte) this.player.getTradeOffer().size());
    for (InvItem item : this.player.getTradeOffer()) {
      s.addShort(item.getID());
      s.addInt(item.getAmount());
    }
    this.packets.add(s.toPacket());
  }

  public void sendDuelAccept() {
    Player with = this.player.getWishToDuel();
    if (with == null) { // This shouldn't happen
      return;
    }
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(172);
    s.addLong(with.getUsernameHash());
    s.addByte((byte) with.getDuelOffer().size());
    for (InvItem item : with.getDuelOffer()) {
      s.addShort(item.getID());
      s.addInt(item.getAmount());
    }
    s.addByte((byte) this.player.getDuelOffer().size());
    for (InvItem item : this.player.getDuelOffer()) {
      s.addShort(item.getID());
      s.addInt(item.getAmount());
    }
    s.addByte((byte) (this.player.getDuelSetting(0) ? 1 : 0)); // duelCantRetreat =
    // data[i7++] & 0xff;
    s.addByte((byte) (this.player.getDuelSetting(1) ? 1 : 0)); // duelUseMagic =
    // data[i7++] & 0xff;
    s.addByte((byte) (this.player.getDuelSetting(2) ? 1 : 0)); // duelUsePrayer =
    // data[i7++] & 0xff;
    s.addByte((byte) (this.player.getDuelSetting(3) ? 1 : 0)); // duelUseWeapons =
    // data[i7++] & 0xff;
    this.packets.add(s.toPacket());
  }

  public void sendTradeAcceptUpdate() {
    Player with = this.player.getWishToTrade();
    if (with == null) { // This shouldn't happen
      return;
    }
    RSCPacketBuilder s1 = new RSCPacketBuilder();
    s1.setID(15);
    s1.addByte((byte) (this.player.isTradeOfferAccepted() ? 1 : 0));
    this.packets.add(s1.toPacket());
    RSCPacketBuilder s2 = new RSCPacketBuilder();
    s2.setID(162);
    s2.addByte((byte) (with.isTradeOfferAccepted() ? 1 : 0));
    this.packets.add(s2.toPacket());
  }

  public void sendDuelAcceptUpdate() {
    Player with = this.player.getWishToDuel();
    if (with == null) { // This shouldn't happen
      return;
    }
    RSCPacketBuilder s1 = new RSCPacketBuilder();
    s1.setID(210);
    s1.addByte((byte) (this.player.isDuelOfferAccepted() ? 1 : 0));
    this.packets.add(s1.toPacket());
    RSCPacketBuilder s2 = new RSCPacketBuilder();
    s2.setID(253);
    s2.addByte((byte) (with.isDuelOfferAccepted() ? 1 : 0));
    this.packets.add(s2.toPacket());
  }

  public void sendDuelSettingUpdate() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(30);
    s.addByte((byte) (this.player.getDuelSetting(0) ? 1 : 0));
    s.addByte((byte) (this.player.getDuelSetting(1) ? 1 : 0));
    s.addByte((byte) (this.player.getDuelSetting(2) ? 1 : 0));
    s.addByte((byte) (this.player.getDuelSetting(3) ? 1 : 0));
    this.packets.add(s.toPacket());
  }

  public void sendTradeItems() {
    Player with = this.player.getWishToTrade();
    if (with == null) { // This shouldn't happen
      return;
    }
    ArrayList<InvItem> items = with.getTradeOffer();
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(97);
    s.addByte((byte) items.size());
    for (InvItem item : items) {
      s.addShort(item.getID());
      s.addInt(item.getAmount());
    }
    this.packets.add(s.toPacket());
  }

  public void sendDuelItems() {
    Player with = this.player.getWishToDuel();
    if (with == null) { // This shouldn't happen
      return;
    }
    ArrayList<InvItem> items = with.getDuelOffer();
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(6);
    s.addByte((byte) items.size());
    for (InvItem item : items) {
      s.addShort(item.getID());
      s.addInt(item.getAmount());
    }
    this.packets.add(s.toPacket());
  }

  public void sendTradeWindowOpen() {
    Player with = this.player.getWishToTrade();
    if (with == null) { // This shouldn't happen
      return;
    }
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(92);
    s.addShort(with.getIndex());
    this.packets.add(s.toPacket());
  }

  public void sendDuelWindowOpen() {
    Player with = this.player.getWishToDuel();
    if (with == null) { // This shouldn't happen
      return;
    }
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(176);
    s.addShort(with.getIndex());
    this.packets.add(s.toPacket());
  }

  public void sendTradeWindowClose() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(128);
    this.packets.add(s.toPacket());
  }

  public void sendDuelWindowClose() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(225);
    this.packets.add(s.toPacket());
  }

  public void sendAppearanceScreen() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_APPEARANCE));
    this.packets.add(s.toPacket());
  }

  public void sendServerInfo() {
    String packetMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
    String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
    Logger.error(String.format("Invalid Server Packet packetMethod=%s callerMethod=%s", packetMethod, callerMethod));
    return;
    //    RSCPacketBuilder s = new RSCPacketBuilder();
    //    s.setID(25);
    //    s.addLong(Config.START_TIME);
    //    s.addBytes(GameVars.serverLocation.getBytes());
    //    packets.add(s.toPacket());
  }

  public void sendTeleBubble(int x, int y, boolean grab) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_TELEPORT_BUBBLE));
    s.addByte((byte) (grab ? 1 : 0));
    s.addByte((byte) (x - this.player.getX()));
    s.addByte((byte) (y - this.player.getY()));
    this.packets.add(s.toPacket());
  }

  public void sendMessage(String message) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_MESSAGE));
    try {
      s.addBytes(message.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    this.packets.add(s.toPacket());
  }

  public void sendRemoveItem(int slot) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_INVENTORY_ITEM_REMOVE));
    s.addByte((byte) slot);
    this.packets.add(s.toPacket());
  }

  public void sendUpdateItem(int slot) {
    InvItem item = this.player.getInventory().get(slot);
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_INVENTORY_ITEM_UPDATE));
    s.addByte((byte) slot);
    s.addShort(item.getID() + (item.isWielded() ? 32768 : 0));
    if (item.getDef().isStackable()) {
      s.addInt(item.getAmount());
    }
    this.packets.add(s.toPacket());
  }

  public void sendInventory() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_INVENTORY_ITEMS));
    s.addByte((byte) this.player.getInventory().size());
    for (InvItem item : this.player.getInventory().getItems()) {
      s.addShort(item.getID() + (item.isWielded() ? 32768 : 0));
      if (item.getDef().isStackable()) {
        s.addInt(item.getAmount());
      }
    }
    this.packets.add(s.toPacket());
  }

  /**
   * Updates the equipment status
   */
  public void sendEquipmentStats() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_PLAYER_STAT_EQUIPMENT_BONUS));
    s.addShort(this.player.getArmourPoints());
    s.addShort(this.player.getWeaponAimPoints());
    s.addShort(this.player.getWeaponPowerPoints());
    s.addShort(this.player.getMagicPoints());
    s.addShort(this.player.getPrayerPoints());
    s.addShort(this.player.getRangePoints());
    this.packets.add(s.toPacket());
  }

  /**
   * Updates just one stat
   */
  public void sendStat(int stat) {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_PLAYER_STAT_UPDATE));
    s.addByte((byte) stat);
    s.addByte((byte) this.player.getCurStat(stat));
    s.addByte((byte) this.player.getMaxStat(stat));
    s.addInt(this.player.getExp(stat));
    this.packets.add(s.toPacket());
  }

  /**
   * Updates the users stats
   */
  public void sendStats() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_PLAYER_STAT_LIST));
    for (int lvl : this.player.getCurStats()) {
      s.addByte((byte) lvl);
    }
    for (int lvl : this.player.getMaxStats()) {
      s.addByte((byte) lvl);
    }
    for (int exp : this.player.getExps()) {
      s.addInt(exp);
    }
    s.addByte((byte) (this.player.getQuestPoints() & 0xff));
    this.packets.add(s.toPacket());
  }

  /**
   * Sent when the user changes coords incase they moved up/down a level
   */
  public void sendWorldInfo() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_WORLD_INFO));
    s.addShort(this.player.getIndex());
    s.addShort(2304);
    s.addShort(1776);
    s.addShort(Formulae.getHeight(this.player.getLocation()));
    s.addShort(944);
    this.packets.add(s.toPacket());
  }

  /**
   * Sends the prayer list of activated/deactivated prayers
   */
  public void sendPrayers() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_PRAYER_STATUS));
    for (int x = 0; x < 14; x++) { // was 14
      s.addByte((byte) (this.player.isPrayerActivated(x) ? 1 : 0));
    }
    this.packets.add(s.toPacket());
  }

  /**
   * Updates game settings, ie sound effects etc
   */
  public void sendGameSettings() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_GAME_SETTINGS));
    s.addByte((byte) (this.player.getGameSetting(0) ? 1 : 0));
    s.addByte((byte) (this.player.getGameSetting(2) ? 1 : 0));
    s.addByte((byte) (this.player.getGameSetting(3) ? 1 : 0));
    //    s.addByte((byte) (player.getGameSetting(4) ? 1 : 0));
    //    s.addByte((byte) (player.getGameSetting(5) ? 1 : 0));
    //    s.addByte((byte) (player.getGameSetting(6) ? 1 : 0));
    this.packets.add(s.toPacket());
  }

  /**
   * Updates privacy settings, ie pm block etc
   */
  public void sendPrivacySettings() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_PRIVACY_SETTINGS));
    s.addByte((byte) (this.player.getPrivacySetting(0) ? 1 : 0));
    s.addByte((byte) (this.player.getPrivacySetting(1) ? 1 : 0));
    s.addByte((byte) (this.player.getPrivacySetting(2) ? 1 : 0));
    s.addByte((byte) (this.player.getPrivacySetting(3) ? 1 : 0));
    this.packets.add(s.toPacket());
  }

  /**
   * Confirm logout allowed
   */
  public RSCPacket sendLogout() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_CLOSE_CONNECTION));
    RSCPacket packet = s.toPacket();
    this.packets.add(packet);
    return packet;
  }

  /**
   * Deny logging out
   */
  public void sendCantLogout() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_LOGOUT_DENY));
    this.packets.add(s.toPacket());
  }

  /**
   * Displays the login box and last IP and login date
   */
  public void sendLoginBox() {
    RSCPacketBuilder s = new RSCPacketBuilder();
    s.setID(Opcode.getServer(204, Command.Server.SV_WELCOME));
    s.addInt(2130706433);
    s.addShort(this.player.getDaysSinceLastLogin());
    s.addByte((byte) 0);
    s.addShort(0);
    this.packets.add(s.toPacket());
  }
}
