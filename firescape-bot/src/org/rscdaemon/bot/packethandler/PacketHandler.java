package org.rscdaemon.bot.packethandler;

import org.apache.mina.common.IoSession;
import org.rscdaemon.bot.net.Packet;

public interface PacketHandler {
  public void handlePacket(Packet p, IoSession session) throws Exception;
}
