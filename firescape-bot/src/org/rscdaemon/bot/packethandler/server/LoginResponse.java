package org.rscdaemon.bot.packethandler.server;

import org.apache.mina.core.session.IoSession;
import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.Packet;
import org.rscdaemon.bot.packethandler.PacketHandler;
import org.rscdaemon.bot.util.LoginHelper;

public class LoginResponse implements PacketHandler {

  @Override
  public void handlePacket(Packet p, IoSession session) throws Exception {
    byte loginCode = p.readByte();
    LoginHelper.awaitingSession = false;
    LoginHelper.awaitingLogin = false;
    Player player = (Player) session.getAttribute("player");
    player.isLoggedIn = true;
  }

}
