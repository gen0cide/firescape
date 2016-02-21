/**
 * 
 */
package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.packetbuilder.RSCPacketBuilder;

/**
 * @author alexl
 *
 */
public abstract class ServerPacket {

  public RSCPacketBuilder packet = new RSCPacketBuilder();

  public ServerPacket() {

  }

  public abstract RSCPacket getPacket();
}
