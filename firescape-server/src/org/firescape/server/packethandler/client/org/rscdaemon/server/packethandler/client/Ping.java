package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;

public class Ping implements PacketHandler {
  public void handlePacket(Packet p, IoSession session) throws Exception {
    // do nothing, simply receiving the packet triggers a ping
  }
}
