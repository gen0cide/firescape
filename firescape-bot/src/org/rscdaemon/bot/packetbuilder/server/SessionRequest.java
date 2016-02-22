package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.util.DataConversions;

public class SessionRequest extends ServerPacket {

  public static final String GAME_CLASS = "ORG.RSCDAEMON.BOT.BOT";

  public SessionRequest(Player p) {
    super(p);
  }

  @Override
  public RSCPacket getPacket() {
    packet.setID(getPacketID());
    byte[] randBytes = new byte[1];
    DataConversions.rand.nextBytes(randBytes);
    packet.addBytes(randBytes);
    packet.addString(GAME_CLASS);
    return packet.toPacket();
  }

  @Override
  public int getPacketID() {
    // TODO Auto-generated method stub
    return 32;
  }

}
