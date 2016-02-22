package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.RSCPacket;

public class ChatMessage extends ServerPacket {

  public ChatMessage(Player p) {
    super(p);
    // TODO Auto-generated constructor stub
  }

  @Override
  public RSCPacket getPacket() {
    // TODO Auto-generated method stub
    return packet.toPacket();
  }

  @Override
  public int getPacketID() {
    // TODO Auto-generated method stub
    return 145;
  }

}
