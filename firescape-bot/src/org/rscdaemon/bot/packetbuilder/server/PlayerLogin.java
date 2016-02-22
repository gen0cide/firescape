package org.rscdaemon.bot.packetbuilder.server;

import org.rscdaemon.bot.model.Player;
import org.rscdaemon.bot.net.EncryptedPayload;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.util.Config;
import org.rscdaemon.bot.util.DataConversions;

public class PlayerLogin extends ServerPacket {

  public PlayerLogin(Player p) {
    super(p);
    // TODO Auto-generated constructor stub
  }

  @Override
  public RSCPacket getPacket() {
    // TODO Auto-generated method stub
    if (player.sessionID == 0) {
      return null;
    } else {
      EncryptedPayload ep = new EncryptedPayload(new byte[500]);
      ep.offset = 0;
      int sessionRotationKeys[] = new int[4];
      sessionRotationKeys[0] = (int) (Math.random() * 99999999D);
      sessionRotationKeys[1] = (int) (Math.random() * 99999999D);
      sessionRotationKeys[2] = (int) (player.sessionID >> 32);
      sessionRotationKeys[3] = (int) player.sessionID;
      for (int i : sessionRotationKeys) {
        ep.add4ByteInt(i);
      }
      ep.add4ByteInt(0);
      String u = DataConversions.addCharacters(player.username, 20);
      ep.addString(u);
      String pw = DataConversions.addCharacters(player.password, 20);
      ep.addString(pw);
      ep.encryptPacketWithKeys(DataConversions.key, DataConversions.modulus);
      if (player.isReconnecting) {
        packet.addByte(1);
      } else {
        packet.addByte(0);
      }
      packet.addShort(Config.VERSION);
      packet.addBytes(ep.packet, 0, ep.offset);
      return packet.toPacket();
    }
  }

  @Override
  public int getPacketID() {
    return 0;
  }

}
