package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.net.RSCPacket;

public class TestServerPacket extends ServerPacket {

  public static final int PACKET_ID = 1776;

  public TestServerPacket() {
    packet.setID(PACKET_ID);
  }

  /**
   * Returns an RSCPacket by calling packet.toPacket(); The packet object is
   * inherited from ServerPacket. This allows you to do things like
   * packet.addShort(foo); Define all of your packets here!
   */
  public RSCPacket getPacket() {
    // TODO Auto-generated method stub
    return null;
  }

}
