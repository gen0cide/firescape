package org.firescape.client;

import org.firescape.client.opcode.Command;
import org.firescape.client.opcode.Keys;
import org.firescape.client.opcode.Opcode;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class GameConnection extends GameShell {

  public static int clientVersion = 1;
  public static int maxReadTries;
  private final int maxSocialListSize = 100;
  public String server;
  public int port;
  public ClientStream clientStream;
  public int friendListCount;
  public long friendListHashes[];
  public String friendListNames[];
  public String friendListOldNames[];
  public String friendListServer[];
  public int friendListOnline[];
  public int ignoreListCount;
  public long ignoreListHashes[];
  public String ignoreListNames[];
  public String ignoreListAccNames[];
  public String ignoreListOldNames[];
  public String ignoreListServers[];
  public int settingsBlockChat;
  public int settingsBlockPrivate;
  public int settingsBlockTrade;
  public int settingsBlockDuel;
  public long sessionID;
  public int worldFullTimeout;
  public int moderatorLevel1;
  public int moderatorLevel2;
  public String the_username = "";
  public String the_password = "";
  protected boolean limit30;
  protected GameCharacter localPlayer;
  String username;
  String password;
  byte incomingPacket[];
  int autoLoginTimeout;
  long packetLastRead;
  private long friendListUnknown[];
  private int friendListUnknown2;

  public GameConnection() {
    server = "127.0.0.1";
    port = 43594;
    username = "";
    password = "";
    incomingPacket = new byte[5000];
    friendListHashes = new long[200];
    friendListNames = new String[200];
    friendListOldNames = new String[200];
    friendListServer = new String[200];
    friendListOnline = new int[200];
    ignoreListHashes = new long[maxSocialListSize];
    ignoreListNames = new String[maxSocialListSize];
    ignoreListAccNames = new String[maxSocialListSize];
    ignoreListOldNames = new String[maxSocialListSize];
    ignoreListServers = new String[maxSocialListSize];
    friendListUnknown = new long[maxSocialListSize];
  }

  public void login(String user, String pass, boolean reconnecting) {
    if (worldFullTimeout > 0) {
      showLoginScreenStatus("Please wait...", "Connecting to server");
      try {
        Thread.sleep(2000L);
      } catch (Exception Ex) {
      }
      showLoginScreenStatus("Sorry! The server is currently full.", "Please try again later");
      return;
    }
    try {
      username = user;
      String u = Utility.formatAuthString(username, 20);
      password = pass;
      pass = Utility.formatAuthString(pass, 20);
      if (user.trim().length() == 0) {
        showLoginScreenStatus("You must enter both a username", "and a password - Please try again");
        return;
      }
      if (reconnecting) {
        drawTextBox("Connection lost! Please wait...", "Attempting to re-establish");
      } else {
        showLoginScreenStatus("Please wait...", "Connecting to server");
      }
      clientStream = new ClientStream(createSocket(server, port), this);
      clientStream.maxReadTries = maxReadTries;
      long sessid = 0L;
      long l = Utility.username2hash(u);
      clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SESSION));
      clientStream.putByte((int) (l >> 16 & 31L));
      clientStream.putString(this.getClass().getName().toUpperCase());
      clientStream.flushPacket();
      sessid = clientStream.getLong();
      sessionID = sessid;
      if (sessid == 0L) {
        showLoginScreenStatus("Login server offline.", "Please try again in a few mins");
        return;
      }
      int limit30 = 0;
      try {
        if (getStartedAsApplet()) {
          String s2 = getParameter("limit30");
          if (s2.equals("1")) {
            limit30 = 1;
          }
        }
      } catch (Exception Ex) {
      }
      if (this.limit30) {
        limit30 = 1;
      }

      int ai[] = new int[4];
      ai[0] = (int) (Math.random() * 99999999D);
      ai[1] = (int) (Math.random() * 99999999D);
      Buffer loginBlock = new Buffer(500);
      ai[2] = (int) (sessid >> 32);
      ai[3] = (int) sessid;
      loginBlock.putInt(ai[0]);
      loginBlock.putInt(ai[1]);
      loginBlock.putInt(ai[2]);
      loginBlock.putInt(ai[3]);
      loginBlock.putInt(getLinkUID());
      loginBlock.putString(u);
      loginBlock.putString(pass);
      loginBlock.encrypt(Keys.getExponent(Version.CLIENT), Keys.getModulus(Version.CLIENT));

      clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_LOGIN));
      if (reconnecting) {
        clientStream.putByte(1);
      } else {
        clientStream.putByte(0);
      }
      clientStream.putShort(Version.FIRESCAPE_VERSION);
      clientStream.putBytes(loginBlock.buffer, 0, loginBlock.offset);
      clientStream.flushPacket();
      int resp = clientStream.readStream();

      System.out.println("[LOGIN] Response Code: " + resp);
      // TODO login responses and pretty much everything else as well

      if (resp == 25) {
        moderatorLevel1 = 1;
        moderatorLevel2 = 0;
        autoLoginTimeout = 0;
        resetGame();
        return;
      }
      if (resp == 0) {
        moderatorLevel1 = 0;
        moderatorLevel2 = 0;
        autoLoginTimeout = 0;
        resetGame();
        return;
      }
      if (resp == 1) {
        autoLoginTimeout = 0;
        method37();
        return;
      }
      if (reconnecting) {
        username = "";
        pass = "";
        resetLoginVars();
        return;
      }
      if (resp == -1) {
        showLoginScreenStatus("Error unable to login.", "Server timed out");
        return;
      }
      if (resp == 2) {
        showLoginScreenStatus("Invalid username or password.", "Try again, or create a new account");
        return;
      }
      if (resp == 3) {
        showLoginScreenStatus("That username is already logged in.", "Wait 60 seconds then retry");
        return;
      }
      if (resp == 4) {
        showLoginScreenStatus("The client has been updated.",
                              "Download the latest at github" + ".com/gen0cide/firescape/releases"
        );
        return;
      }
      if (resp == 6) {
        showLoginScreenStatus("Account permanently disabled.", "#Banned");
        return;
      }
      if (resp == 7) {
        showLoginScreenStatus("Login attempts exceeded!", "Please try again in 5 minutes");
        return;
      }
      if (resp == 5) {
        showLoginScreenStatus("Error unable to login.", "Server rejected session");
        return;
      }
      if (resp == 9) {
        showLoginScreenStatus("Error unable to login.", "Loginserver rejected session");
        return;
      }
      if (resp == 11) {
        showLoginScreenStatus("Account temporarily disabled.", "Check your message inbox for details");
        return;
      }
      if (resp == 12) {
        showLoginScreenStatus("Account permanently disabled.", "Check your message inbox for details");
        return;
      }
      if (resp == 10) {
        showLoginScreenStatus("Sorry! This world is currently full.", "Please try a different world");
        worldFullTimeout = 1500;
        return;
      }
      if (resp == 15) {
        showLoginScreenStatus("You need a members account", "to login to this world");
        return;
      }
      if (resp == 16) {
        showLoginScreenStatus("Error - no reply from loginserver.", "Please try again");
        return;
      }
      if (resp == 17) {
        showLoginScreenStatus("Error - failed to decode profile.", "Contact customer support");
        return;
      }
      if (resp == 18) {
        showLoginScreenStatus("Account suspected stolen.", "Press 'recover a locked account' on front page.");
        return;
      }
      if (resp == 20) {
        showLoginScreenStatus("Error - loginserver mismatch", "Please try a different world");
        return;
      }
      if (resp == 21) {
        showLoginScreenStatus("Unable to login.", "That is not an RS-Classic account");
        return;
      }
      if (resp == 22) {
        showLoginScreenStatus("Username and password must be at least 3 characters.", "Please try again.");
        return;
      }
      if (resp == 23) {
        this.showLoginScreenStatus("You need to set your display name.",
                                   "Please go to the Account Management page to do this."
        );
        return;
      }
      if (resp == 24) {
        this.showLoginScreenStatus("This world does not accept new players.", "Please see the launch page for help");
        return;
      }
      if (resp == 25) {
        this.showLoginScreenStatus("None of your characters can log in.", "Contact customer support");
        return;
      } else {
        showLoginScreenStatus("Error unable to login.", "Unrecognised response code");
        return;
      }
    } catch (Exception exception) {
      System.out.println(String.valueOf(exception));
    }
    if (autoLoginTimeout > 0) {
      try {
        Thread.sleep(5000L);
      } catch (Exception Ex) {
      }
      autoLoginTimeout--;
      login(username, password, reconnecting);
    }
    if (reconnecting) {
      username = "";
      password = "";
      resetLoginVars();
    } else {
      showLoginScreenStatus("Sorry! Unable to connect.", "Check internet settings or try another world");
    }
  }

  protected void closeConnection(boolean notifyServer) {
    if (notifyServer && clientStream != null) {
      try {
        clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CLOSE_CONNECTION));
        clientStream.flushPacket();
      } catch (IOException Ex) {
      }
    }
    username = "";
    password = "";
    resetLoginVars();
  }

  protected void resetLoginVars() {
  }

  protected void lostConnection() {
    try {
      throw new Exception("");
    } catch (Exception ex) {
      System.out.println("lost connection: ");
      ex.printStackTrace();
    }
    System.out.println("Lost connection");
    autoLoginTimeout = 10;
    login(username, password, true);
  }

  protected void drawTextBox(String s, String s1) {
    Graphics g = getGraphics();
    Font font = new Font("Helvetica", 1, 15);
    char c = '\u0200';
    char c1 = '\u0158';
    g.setColor(Color.black);
    g.fillRect(c / 2 - 140, c1 / 2 - 25, 280, 50);
    g.setColor(Color.white);
    g.drawRect(c / 2 - 140, c1 / 2 - 25, 280, 50);
    drawString(g, s, font, c / 2, c1 / 2 - 10);
    drawString(g, s1, font, c / 2, c1 / 2 + 10);
  }

  protected void checkConnection() {
    long l = System.currentTimeMillis();
    if (clientStream.hasPacket()) {
      packetLastRead = l;
    }
    if (l - packetLastRead > 5000L) {
      packetLastRead = l;
      clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PING));
      clientStream.sendPacket();
    }
    try {
      clientStream.writePacket(20);
    } catch (IOException Ex) {
      lostConnection();
      return;
    }
    if (!method43()) {
      return;
    }
    int psize = clientStream.readPacket(incomingPacket);
    if (psize > 0) {
      int ptype = 0;
      ptype = incomingPacket[0] & 0xff;
      handlePacket(Opcode.getServer(Version.CLIENT, ptype), ptype, psize);
    }
  }

  private void handlePacket(Command.Server opcode, int ptype, int psize) {
    System.out.println(String.format("[Packet] <<< %s(id=%d) size=%d\n%s",
                                     opcode.name(),
                                     ptype,
                                     psize,
                                     Utility.bytesToHex(Arrays.copyOfRange(incomingPacket, 0, psize))
    ));
    int offset = 1;
    if (opcode == Command.Server.SV_MESSAGE) {
      String s = new String(Arrays.copyOfRange(incomingPacket, 1, 1 + (psize - 1)));
      showServerMessage(s);
      return;
    }
    if (opcode == Command.Server.SV_LOGOUT_DENY) {
      cantLogout();
      return;
    }
    if (opcode == Command.Server.SV_FRIEND_LIST) {
      friendListCount = incomingPacket[1] & 0xff;
      int friendOffset = 0;
      for (int k = 0; k < friendListCount; k++) {
        friendListHashes[k] = Utility.getUnsignedLong(incomingPacket, 2 + friendOffset);
        friendListOnline[k] = Utility.getUnsignedByte(incomingPacket[10 + friendOffset]);
        friendOffset += 9;
      }

      sortFriendsList();
      return;
    }
    if (opcode == Command.Server.SV_FRIEND_STATUS_CHANGE) {
      long hash = Utility.getUnsignedLong(incomingPacket, 1);
      for (int i2 = 0; i2 < friendListCount; i2++) {
        if (friendListHashes[i2] == hash) {
          int friendStatus = Utility.getUnsignedByte(incomingPacket[9]);
          if (friendListOnline[i2] != friendStatus) {
            friendListOnline[i2] = friendStatus;
            if (friendListOnline[i2] == 1) {
              showServerMessage("@pri@" + Utility.hash2username(hash) + " has logged in");
            }
            if (friendListOnline[i2] == 0) {
              showServerMessage("@pri@" + Utility.hash2username(hash) + " has logged out");
            }
            sortFriendsList();
            return;
          }
        }
      }
      return;
    }
    if (opcode == Command.Server.SV_IGNORE_LIST) {
      ignoreListCount = Utility.getUnsignedByte(incomingPacket[offset++]);
      for (int index = 0; index < ignoreListCount; index++) {
        ignoreListHashes[index] = Utility.getUnsignedLong(incomingPacket, 2 + index * 8);
      }
      return;
    }
    if (opcode == Command.Server.SV_PRIVACY_SETTINGS) {
      settingsBlockChat = Utility.getUnsignedByte(incomingPacket[offset++]);
      settingsBlockPrivate = Utility.getUnsignedByte(incomingPacket[offset++]);
      settingsBlockTrade = Utility.getUnsignedByte(incomingPacket[offset++]);
      settingsBlockDuel = Utility.getUnsignedByte(incomingPacket[offset++]);
      return;
    }
    if (opcode == Command.Server.SV_FRIEND_MESSAGE) {
      long from = Utility.getUnsignedLong(incomingPacket, 1);
      String rawMessage = new String(Arrays.copyOfRange(incomingPacket, 9, psize));
      showServerMessage("@pri@" + Utility.hash2username(from) + ": tells you " + rawMessage);
      return;
    }
    handleIncomingPacket(opcode, ptype, psize, incomingPacket);
    return;
  }

  private void sortFriendsList() {
    boolean go = true;
    while (go) {
      go = false;
      for (int index = 0; index < friendListCount - 1; index++) {
        if (Version.CLIENT > 204 &&
            ((friendListOnline[index] & 2) == 0 && (friendListOnline[index + 1] & 2) != 0 ||
             (friendListOnline[index] & 4) == 0 && (friendListOnline[index + 1] & 4) != 0) ||
            Version.CLIENT <= 204 &&
            (friendListOnline[index] != 255 && friendListOnline[index + 1] == 255 ||
             friendListOnline[index] == 0 && friendListOnline[index + 1] != 0)) {
          String server = friendListServer[index];
          friendListServer[index] = friendListServer[index + 1];
          friendListServer[index + 1] = server;
          String name = friendListNames[index];
          friendListNames[index] = friendListNames[index + 1];
          friendListNames[index + 1] = name;
          String oldName = friendListOldNames[index];
          friendListOldNames[index] = friendListOldNames[index + 1];
          friendListOldNames[index + 1] = oldName;
          int online = friendListOnline[index];
          friendListOnline[index] = friendListOnline[index + 1];
          friendListOnline[index + 1] = online;
          go = true;
        }
      }

    }
  }

  protected void sendPrivacySettings(int chat, int priv, int trade, int duel) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_SETTINGS_PRIVACY));
    clientStream.putByte(chat);
    clientStream.putByte(priv);
    clientStream.putByte(trade);
    clientStream.putByte(duel);
    clientStream.sendPacket();
  }

  protected void ignoreAdd(String name) {
    if (ignoreListCount >= 100) { // 200 if members else 100
      showMessage(0, "Ignore list full", null, null, 0, null, false);
      return;
    }
    String formattedName = Utility.formatName(name);
    if (formattedName == null) {
      return;
    }
    for (int i = 0; i < ignoreListCount; i++) {
      if (formattedName.equals(Utility.formatName(ignoreListNames[i])) ||
          ignoreListOldNames[i] != null && formattedName.equals(Utility.formatName(ignoreListOldNames[i]))) {
        showMessage(0, name + " is already on your ignore list", null, null, 0, null, false);
        return;
      }
    }
    for (int i = 0; i < friendListCount; i++) {
      if (formattedName.equals(Utility.formatName(friendListNames[i])) ||
          friendListOldNames[i] != null && formattedName.equals(Utility.formatName(friendListOldNames[i]))) {
        showMessage(0, "Please remove " + name + " from your friends list first", null, null, 0, null, false);
        return;
      }
    }
    if (formattedName.equals(Utility.formatName(localPlayer.accountName))) {
      showMessage(0, "You can't add yourself to your ignore list", null, null, 0, null, false);
      return;
    }
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_IGNORE_ADD));
    clientStream.pjstr2(name);
    clientStream.sendPacket();
  }

  protected void showMessage(int messageType, String message, String sender, String senderClan, int crownId, String colorOverride, boolean forceShow) {
  }

  protected void ignoreAdd(long l) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_IGNORE_ADD));
    clientStream.putLong(l);
    clientStream.sendPacket();
    for (int i = 0; i < ignoreListCount; i++) {
      if (ignoreListHashes[i] == l) {
        return;
      }
    }

    if (ignoreListCount >= maxSocialListSize) {
      return;
    } else {
      ignoreListHashes[ignoreListCount++] = l;
      return;
    }
  }

  protected void ignoreRemove(String name) {
    String formattedName = Utility.formatName(name);
    if (formattedName == null) {
      return;
    }
    for (int i = 0; i < ignoreListCount; i++) {
      if (formattedName.equals(Utility.formatName(ignoreListAccNames[i]))) {
        ignoreListCount--;
        for (int j = i; j < ignoreListCount; j++) {
          ignoreListNames[j] = ignoreListNames[j + 1];
          ignoreListAccNames[j] = ignoreListAccNames[j + 1];
          ignoreListOldNames[j] = ignoreListOldNames[j + 1];
          ignoreListServers[j] = ignoreListServers[j + 1];
        }
        clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_IGNORE_REMOVE));
        clientStream.pjstr2(name);
        clientStream.sendPacket();
        return;
      }
    }
  }

  protected void ignoreRemove(long l) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_IGNORE_REMOVE));
    clientStream.putLong(l);
    clientStream.sendPacket();
    for (int i = 0; i < ignoreListCount; i++) {
      if (ignoreListHashes[i] == l) {
        ignoreListCount--;
        for (int j = i; j < ignoreListCount; j++) {
          ignoreListHashes[j] = ignoreListHashes[j + 1];
        }

        return;
      }
    }

  }

  protected void friendAdd(String name) {
    if (friendListCount >= 200) { // 200 if members else 100
      showMessage(0, "Friend list is full", null, null, 0, null, false);
      return;
    }
    String formattedName = Utility.formatName(name);
    if (formattedName == null) {
      return;
    }
    for (int i = 0; i < friendListCount; i++) {
      if (formattedName.equals(Utility.formatName(friendListNames[i])) ||
          friendListOldNames[i] != null && formattedName.equals(Utility.formatName(friendListOldNames[i]))) {
        showMessage(0, name + " is already on your friend list.", null, null, 0, null, false);
        return;
      }
    }
    for (int i = 0; i < ignoreListCount; i++) {
      if (formattedName.equals(Utility.formatName(ignoreListNames[i])) ||
          ignoreListOldNames[i] != null && formattedName.equals(Utility.formatName(ignoreListOldNames[i]))) {
        showMessage(0, "Please remove " + name + " from your ignore list first.", null, null, 0, null, false);
        return;
      }
    }
    if (formattedName.equals(Utility.formatName(localPlayer.accountName))) {
      showMessage(0, "You can't add yourself to your own friend list.", null, null, 0, null, false);
      return;
    }
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_FRIEND_ADD));
    clientStream.pjstr2(name);
    clientStream.sendPacket();
  }

  protected void friendAdd(long l) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_FRIEND_ADD));
    clientStream.putLong(l);
    clientStream.sendPacket();
    for (int i = 0; i < friendListCount; i++) {
      if (friendListHashes[i] == l) {
        return;
      }
    }

    if (friendListCount >= maxSocialListSize) {
      return;
    } else {
      friendListHashes[friendListCount] = l;
      friendListOnline[friendListCount] = 0;
      friendListCount++;
      return;
    }
  }

  protected void friendRemove(String name) {
    String formattedName = Utility.formatName(name);
    if (formattedName == null) {
      return;
    }
    for (int i = 0; friendListCount > i; ++i) {
      if (formattedName.equals(Utility.formatName(friendListNames[i]))) {
        friendListCount--;

        for (int j = i; j < friendListCount; ++j) {
          friendListNames[j] = friendListNames[1 + j];
          friendListOldNames[j] = friendListOldNames[j - -1];
          friendListServer[j] = friendListServer[j + 1];
          friendListOnline[j] = friendListOnline[1 + j];
        }

        clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_FRIEND_REMOVE));
        clientStream.pjstr2(name);
        clientStream.sendPacket();
        break;
      }
    }
  }

  protected void friendRemove(long l) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_FRIEND_REMOVE));
    clientStream.putLong(l);
    clientStream.sendPacket();
    for (int i = 0; i < friendListCount; i++) {
      if (friendListHashes[i] != l) {
        continue;
      }
      friendListCount--;
      for (int j = i; j < friendListCount; j++) {
        friendListHashes[j] = friendListHashes[j + 1];
        friendListOnline[j] = friendListOnline[j + 1];
      }

      break;
    }

    showServerMessage("@pri@" + Utility.hash2username(l) + " has been removed from your friends list");
  }

  protected void showServerMessage(String s) {
  }

  protected void sendPrivateMessage(long u, byte buff[], int len) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PM));
    clientStream.putLong(u);
    clientStream.putBytes(buff, 0, len);
    clientStream.sendPacket();
  }

  protected void sendPrivateMessage(String target, String message) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_PM));
    clientStream.pjstr2(target);
    clientStream.putUnicodeString(message);
    clientStream.sendPacket();
  }

  protected void sendChatMessage(byte buff[], int len) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CHAT));
    clientStream.putBytes(buff, 0, len);
    clientStream.sendPacket();
  }

  protected void sendChatMessage(String message) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CHAT));
    clientStream.putString(message);
    clientStream.sendPacket();
  }

  protected void sendCommandString(String s) {
    clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_COMMAND));
    clientStream.putString(s);
    clientStream.sendPacket();
  }

  protected void showLoginScreenStatus(String s, String s1) {
  }

  protected void method37() {
  }

  protected void resetGame() {
  }

  protected void cantLogout() {
  }

  protected void handleIncomingPacket(Command.Server opcode, int ptype, int len, byte data[]) {
  }

  protected boolean method43() {
    return true;
  }

  protected int getLinkUID() {
    return 0;
  }
}
