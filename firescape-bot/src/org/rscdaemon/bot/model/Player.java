package org.rscdaemon.bot.model;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.packetbuilder.server.ChatMessage;
import org.rscdaemon.bot.packetbuilder.server.PlayerLogin;
import org.rscdaemon.bot.packetbuilder.server.SessionRequest;
import org.rscdaemon.bot.util.Config;
import org.rscdaemon.bot.util.DataConversions;
import org.rscdaemon.bot.util.Logger;
import org.rscdaemon.bot.util.LoginHelper;

public class Player {

  public String username;

  public String password;

  public long sessionID = 0;

  public IoSession session;

  public boolean isReconnecting = false;

  public boolean isLoggedIn = false;

  public Player() {
    this.username = Config.USERNAME;
    this.password = Config.PASSWORD;
  }

  public Player(IoSession session) {
    this.session = session;
    this.username = Config.USERNAME;
    this.password = Config.PASSWORD;
  }

  public Player(String username, String password, IoSession session) {
    this.session = session;
    this.username = username;
    this.password = password;
  }

  public void setIoSession(IoSession session) {
    this.session = session;
  }

  public void sendPacket(RSCPacket p, boolean wait) {
    // System.out.println("Session: " + this.session.toString());
    if (wait) {
      WriteFuture future = this.session.write(p);
      future.awaitUninterruptibly();
      if (future.isWritten()) {
        return;
      } else {
        Logger.net("Packet was NOT written");
        return;
      }
    } else {
      this.session.write(p);
    }
  }

  public void sendChatMessage(String m) {
    ChatMessage cm = new ChatMessage(this);
    cm.packet.addBytes(DataConversions.stringToByteArray(m));
    sendPacket(cm.getPacket(), true);
  }

  public void setupSession() {
    LoginHelper.awaitingSession = true;
    sendPacket(new SessionRequest(this).getPacket(), true);
  }

  public void login() {
    LoginHelper.awaitingSession = false;
    LoginHelper.awaitingLogin = true;
    if (sessionID != 0) {
      sendPacket(new PlayerLogin(this).getPacket(), true);
    } else {
      Logger.info("No session ID found!");
    }
  }
}
