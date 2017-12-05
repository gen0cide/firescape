package org.firescape.server.opcode;

import org.firescape.server.opcode.version.v202;
import org.firescape.server.opcode.version.v204;
import org.firescape.server.opcode.version.v234;

import java.util.Map;

public class Opcode {

  public static Command.Server getServer(int version, int id) {
    try {
      Map<Command.Server, Integer> m;
      if (version == 202) {
        m = v202.server;
      } else if (version == 234) {
        m = v234.server;
      } else {
        m = v204.server;
      }
      return m.entrySet().stream().filter(e -> e.getValue() == id).findFirst().get().getKey();
    } catch (Exception ignored) {
    }
    return Command.Server.UNKNOWN;
  }

  public static int getServer(int version, Command.Server opcode) {
    try {
      Map<Command.Server, Integer> m;
      if (version == 202) {
        m = v202.server;
      } else if (version == 234) {
        m = v234.server;
      } else {
        m = v204.server;
      }
      return m.entrySet().stream().filter(e -> e.getKey().equals(opcode)).findFirst().get().getValue();
    } catch (Exception ignored) {
    }
    return -1;
  }

  public static int getClient(int version, Command.Client opcode) {
    try {
      Map<Command.Client, Integer> m;
      if (version == 202) {
        m = v202.client;
      } else if (version == 234) {
        m = v234.client;
      } else {
        m = v204.client;
      }
      return m.entrySet().stream().filter(e -> e.getKey().equals(opcode)).findFirst().get().getValue();
    } catch (Exception ignored) {
    }
    return -1;
  }

  public static Command.Client getClient(int version, int ptype) {
    try {
      Map<Command.Client, Integer> m;
      if (version == 202) {
        m = v202.client;
      } else if (version == 234) {
        m = v234.client;
      } else {
        m = v204.client;
      }
      return m.entrySet().stream().filter(e -> e.getValue() == ptype).findFirst().get().getKey();
    } catch (Exception ignored) {
    }
    return Command.Client.UNKNOWN;
  }

}
