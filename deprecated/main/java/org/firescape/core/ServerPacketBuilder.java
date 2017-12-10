package org.firescape.core;

import java.io.UnsupportedEncodingException;

public class ServerPacketBuilder {

  public static Packet sendFatigue(int val) {
    Packet p = new Packet();
    p.setID(Opcode.getServer(Command.Server.SV_PLAYER_STAT_FATIGUE));
    p.addShort(val);
    return p;
  }

  public static Packet hideMenu() {
    Packet p = new Packet();
    p.setID(Opcode.getServer(Command.Server.SV_OPTION_LIST_CLOSE));
    return p;
  }

  public static Packet sendMenu(String[] options) {
    Packet p = new Packet();
    p.setID(Opcode.getServer(Command.Server.SV_OPTION_LIST));
    p.addByte((byte) options.length);
    for (String option : options) {
      p.addInt(option.length());
      try {
        p.addBytes(option.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        for (int i = 0; i < option.length(); i++) {
          p.addByte((byte) 0);
        }
      }
    }
    return p;
  }
}
