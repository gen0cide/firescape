package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.util.DataConversions;

public class SessionRequest extends ServerPacket {

  public static final String GAME_CLASS = "ORG.RSCDAEMON.BOT.BOT";

  public SessionRequest(Player p) {
    super(p);
  }

  public RSCPacket getPacket() {
    byte[] randBytes = new byte[1];
    DataConversions.rand.nextBytes(randBytes);
    packet.addBytes(randBytes);
    packet.addString(GAME_CLASS);
    return packet.toPacket();
  }

  public int getPacketID() {
    return 32;
  }

}
