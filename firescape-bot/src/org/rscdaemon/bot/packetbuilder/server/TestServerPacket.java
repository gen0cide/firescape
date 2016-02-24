package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.RSCPacket;

public class TestServerPacket extends ServerPacket {

  public TestServerPacket(Player p) {
    super(p);
  }

  /**
   * Returns an RSCPacket by calling packet.toPacket(); The packet object is
   * inherited from ServerPacket. This allows you to do things like
   * packet.addShort(foo); Define all of your packets here!
   */
  public RSCPacket getPacket() {
    return null;
  }

  @Override
  public int getPacketID() {
    return 1776;
  }

}
