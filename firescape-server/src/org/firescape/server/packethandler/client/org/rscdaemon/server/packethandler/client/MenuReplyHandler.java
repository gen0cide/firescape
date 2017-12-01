package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.model.MenuHandler;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;

public class MenuReplyHandler implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    MenuHandler menuHandler = player.getMenuHandler();
    if (menuHandler == null) {
      player.setSuspiciousPlayer(true);
      return;
    }
    int option = (int) p.readByte();
    String reply = menuHandler.getOption(option);
    player.resetMenuHandler();
    if (reply == null) {
      player.setSuspiciousPlayer(true);
      return;
    }
    menuHandler.handleReply(option, reply);
  }
}
