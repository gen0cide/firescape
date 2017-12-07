package org.firescape.client.script;

import javax.script.ScriptEngineManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Manager {

  public static final String[] scriptIncludes = {
    "helper.js", "hud.js", "login.js"
  };

  private static ScriptEngineManager manager = new ScriptEngineManager();
  private static List<Script> scripts = new ArrayList<>();

  public static void addBinding(String name, Object binding) {
    manager.put(name, binding);
  }

  public static void clear() {
    scripts.clear();
  }

  public static void loadScripts() {
    for (int i = 0; i < scriptIncludes.length; i++) {
      try {
        Script s = new Script(scriptIncludes[i], manager);
        s.load();
        scripts.add(s);
        p("Loaded Script: " + scriptIncludes[i]);
      } catch (Exception e) {
        p("Error loading Script: " + scriptIncludes[i]);
        e.printStackTrace();
      }
    }
  }

  private static void p(String s) {
    System.out.println(s);
  }

  public static List<Object> run(String func, Object... params) {
    List<Object> I_OBJECT = new ArrayList<>();
    for (Script s : scripts) {
      try {
        I_OBJECT.add(s.run(func, params));
      } catch (NoSuchMethodException ey) {
        // ignore
        I_OBJECT.add(Exception.class);
      } catch (Exception e) {
        p("Error running script " + s.getPath() + " - " + e.getMessage());
        I_OBJECT.add(Exception.class);
      }
    }
    return I_OBJECT;
  }

}
