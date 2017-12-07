package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.packethandler.PacketHandler;

public class GameSettingHandler implements PacketHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();
  // private GameSettingUpdatePacketBuilder builder = new
  // GameSettingUpdatePacketBuilder();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    int idx = (int) p.readByte();
    if (idx < 0 || idx > 6) {
      player.setSuspiciousPlayer(true);
      return;
    }
    boolean on = p.readByte() == 1;
    player.setGameSetting(idx, on);
    // builder.setPlayer(player);
    // builder.setIndex(idx);
    // builder.setOn(on);
  }

}
