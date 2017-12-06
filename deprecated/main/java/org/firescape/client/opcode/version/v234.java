/**
 * rsc 10-02-2016
 */

package org.firescape.client.opcode.version;

import org.firescape.client.opcode.Command;

import java.util.HashMap;

public class v234 {

  public static HashMap<Command.Client, Integer> client = new HashMap<Command.Client, Integer>() {{
    putAll(v204.client);
    put(Command.Client.CL_SKIP_TUTORIAL, 84);
    // TODO
  }};

  public static HashMap<Command.Server, Integer> server = new HashMap<Command.Server, Integer>() {{
    putAll(v204.server);
    put(Command.Server.SV_CLOSE_CONNECTION_NOTIFY, 165);
    put(Command.Server.SV_IGNORE_STATUS_CHANGE, 237);
    put(Command.Server.SV_UNKNOWN_87, 87);
    put(Command.Server.SV_UNKNOWN_189, 189);
    put(Command.Server.SV_IN_TUTORIAL, 111);
    put(Command.Server.SV_UNKNOWN_213, 213);
    // TODO
  }};
}
