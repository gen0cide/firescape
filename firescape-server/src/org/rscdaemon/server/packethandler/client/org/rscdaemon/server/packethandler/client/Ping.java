package org.rscdaemon.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.rscdaemon.server.net.Packet;
import org.rscdaemon.server.packethandler.PacketHandler;

public class Ping implements PacketHandler {
  public void handlePacket(Packet p, IoSession session) throws Exception {
    // do nothing, simply receiving the packet triggers a ping
  }
}
