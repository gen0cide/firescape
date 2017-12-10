package org.firescape.core;

import org.firescape.core.version.V204;

import java.util.Map;

public class Opcode {

  public static Command.Server getServer(int id) {
    try {
      Map<Command.Server, Integer> m;
      m = V204.server;
      return m.entrySet().stream().filter(e -> e.getValue() == id).findFirst().get().getKey();
    } catch (Exception ignored) {
    }
    return Command.Server.UNKNOWN;
  }

  public static int getServer(Command.Server opcode) {
    try {
      Map<Command.Server, Integer> m;
      m = V204.server;
      return m.entrySet().stream().filter(e -> e.getKey().equals(opcode)).findFirst().get().getValue();
    } catch (Exception ignored) {
    }
    return -1;
  }

  public static int getClient(Command.Client opcode) {
    try {
      Map<Command.Client, Integer> m;
      m = V204.client;
      return m.entrySet().stream().filter(e -> e.getKey().equals(opcode)).findFirst().get().getValue();
    } catch (Exception ignored) {
    }
    return -1;
  }

  public static Command.Client getClient(int ptype) {
    try {
      Map<Command.Client, Integer> m;
      m = V204.client;
      return m.entrySet().stream().filter(e -> e.getValue() == ptype).findFirst().get().getKey();
    } catch (Exception ignored) {
    }
    return Command.Client.UNKNOWN;
  }
}
