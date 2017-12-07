package org.firescape.client.script;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class Script {

  private ScriptEngine engine;
  private Invocable invocable;
  private String path;

  public Script(String filename, ScriptEngineManager manager) throws Exception {
    int i = filename.lastIndexOf('.');
    if (i < 0) {
      throw new Exception("Unable to determine file extension for: " + filename);
    }
    String extension = filename.substring(i + 1);
    engine = manager.getEngineByExtension(extension);
    if (engine == null) {
      throw new Exception("No engine for: " + filename);
    }
    invocable = (Invocable) engine;
    this.path = filename;
  }

  public String getPath() {
    return path;
  }

  public void load() throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass()
                                                                     .getResourceAsStream(
                                                                       "/org/firescape/client/conf/scripts/" + path)));
    engine.eval(br);
  }

  public Object run(String name, Object... params) throws Exception {
    return invocable.invokeFunction(name, params);
  }
}
