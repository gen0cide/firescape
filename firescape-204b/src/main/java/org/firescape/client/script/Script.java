package org.firescape.client.script;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.file.Files;
import java.nio.file.Path;

public class Script {

  private ScriptEngine engine;
  private Invocable invocable;
  private Path path;

  public Script(Path path, ScriptEngineManager manager) throws Exception {
    String filename = path.getFileName().toString();
    int i = filename.lastIndexOf('.');
    if (i < 0) {
      throw new Exception("Unable to determine file extension for: " + path);
    }
    String extension = filename.substring(i + 1);
    engine = manager.getEngineByExtension(extension);
    if (engine == null) {
      throw new Exception("No engine for: " + path);
    }
    invocable = (Invocable) engine;
    this.path = path;
  }

  public Path getPath() {
    return path;
  }

  public void load() throws Exception {
    engine.eval(Files.newBufferedReader(path));
  }

  public Object run(String name, Object... params) throws Exception {
    return invocable.invokeFunction(name, params);
  }

}
