package org.rscdaemon.bot.packethandler.server;

import org.apache.mina.core.session.IoSession;
import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.Packet;
import org.rscdaemon.bot.packethandler.PacketHandler;
import org.rscdaemon.bot.util.Logger;

public class SessionResponse implements PacketHandler {

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Logger.net("Got session response packet");
    Player player = (Player) session.getAttribute("player");
    player.sessionID = p.readLong();
  }

}
