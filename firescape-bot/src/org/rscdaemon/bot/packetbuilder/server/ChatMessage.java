package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.RSCPacket;

public class ChatMessage extends ServerPacket {

  public ChatMessage(Player p) {
    super(p);
  }

  @Override
  public RSCPacket getPacket() {
    return packet.toPacket();
  }

  @Override
  public int getPacketID() {
    return 145;
  }

}
