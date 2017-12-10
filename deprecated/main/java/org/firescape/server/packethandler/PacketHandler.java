package org.firescape.server.packethandler;

import org.apache.mina.common.IoSession;
import org.firescape.server.net.Packet;

public interface PacketHandler {

  void handlePacket(Packet p, IoSession session) throws Exception;
}
