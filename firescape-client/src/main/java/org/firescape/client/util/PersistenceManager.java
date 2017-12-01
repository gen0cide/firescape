package org.firescape.client.util;

import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PersistenceManager {
  private static final XStream xstream = new XStream();

  static {
    addAlias("NPCDef", "org.firescape.client.entityhandling.defs.NPCDef");
    addAlias("ItemDef", "org.firescape.client.entityhandling.defs.ItemDef");
    addAlias("TextureDef", "org.firescape.client.entityhandling.defs.extras.TextureDef");
    addAlias("AnimationDef", "org.firescape.client.entityhandling.defs.extras.AnimationDef");
    addAlias("ItemDropDef", "org.firescape.client.entityhandling.defs.extras.ItemDropDef");
    addAlias("SpellDef", "org.firescape.client.entityhandling.defs.SpellDef");
    addAlias("PrayerDef", "org.firescape.client.entityhandling.defs.PrayerDef");
    addAlias("TileDef", "org.firescape.client.entityhandling.defs.TileDef");
    addAlias("DoorDef", "org.firescape.client.entityhandling.defs.DoorDef");
    addAlias("ElevationDef", "org.firescape.client.entityhandling.defs.ElevationDef");
    addAlias("GameObjectDef", "org.firescape.client.entityhandling.defs.GameObjectDef");
    addAlias("Sprite", "org.firescape.client.model.Sprite");
  }

  private static void addAlias(String name, String className) {
    try {
      xstream.alias(name, Class.forName(className));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static Object load(File file) {
    try {
      InputStream is = new GZIPInputStream(new FileInputStream(file));
      Object rv = xstream.fromXML(is);
      return rv;
    } catch (IOException ioe) {
      System.err.println(ioe.getMessage());
    }
    return null;
  }

  public static void write(File file, Object o) {
    try {
      OutputStream os = new GZIPOutputStream(new FileOutputStream(file));
      xstream.toXML(o, os);
    } catch (IOException ioe) {
      System.err.println(ioe.getMessage());
    }
  }
}
