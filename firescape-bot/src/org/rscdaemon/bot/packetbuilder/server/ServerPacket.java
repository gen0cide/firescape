package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.packetbuilder.RSCPacketBuilder;

public abstract class ServerPacket {

  public RSCPacketBuilder packet = new RSCPacketBuilder();

  public Player player;

  public ServerPacket(Player p) {
    this.player = p;
    packet.setID(getPacketID());
  }

  public abstract RSCPacket getPacket();

  public abstract int getPacketID();
}
