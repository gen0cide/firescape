package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;

public class ChatHandler implements PacketHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player sender = (Player) session.getAttachment();
    if (sender.isMuted()) {
      sender.getActionSender().sendMessage("You are @red@MUTED@whi@. Nobody will see your messages.");
      return;
    }
    // add to redis
    sender.addMessageToChatQueue(p.getData());
  }

}