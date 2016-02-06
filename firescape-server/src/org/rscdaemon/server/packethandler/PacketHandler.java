package org.rscdaemon.server.packethandler;

import org.apache.mina.common.IoSession;
import org.rscdaemon.server.net.Packet;

public interface PacketHandler {
  public void handlePacket(Packet p, IoSession session) throws Exception;
}
