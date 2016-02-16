package org.rscdaemon.server.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.rscdaemon.server.util.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author Ent Loads Player details up.
 */
public class PlayerLoader {

  public static JedisPool redis = new JedisPool(new JedisPoolConfig(), "localhost");

  static Properties props = new Properties();
  static Properties baseProps = new Properties();

  public static int getLogin(String user, String pass) {
    try {

      // user = user.replaceAll("_", " ");
      InputStream ios = null;
      String username = user.replaceAll(" ", "_").toLowerCase();
      String redis_key = "players_" + username.toLowerCase();
      try (Jedis jedis = redis.getResource()) {
        if (jedis.exists(redis_key)) {
          ios = new ByteArrayInputStream(jedis.get(redis_key).getBytes(StandardCharsets.UTF_8));
          Logger.print("Loaded players_" + username.toLowerCase() + " from redis.", 3);
          props.load(ios);
          if (Integer.valueOf(props.getProperty("rank")) == 6) {
            ios.close();
            return 6; // Banned.
          }
          if (props.getProperty("pass").equalsIgnoreCase(pass)) {
            if (props.getProperty("loggedin").equalsIgnoreCase("true")) {
              ios.close();
              return 2; // Already logged in.
            }
            ios.close();
            return 1; // Correct, Log in.

          } else { // Bad password
            ios.close();
            return 0;
          }
        } else {
          Properties pr = new Properties();
          pr.load(new FileInputStream(new File("players/Template")));
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          pr.setProperty("pass", pass);
          pr.store(bos, "Redis backed character data");
          jedis.set(redis_key, bos.toString());
          Logger.print("Saved " + redis_key + " data to redis.", 3);
          // Server.writeValue(user, "pass", pass);
          Logger.print("Account Created: " + user, 3);
          return 1;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      org.rscdaemon.server.util.Logger.print(e.toString(), 1);
      return 0;
    }
  }

  static void copy(File src, File dst) throws IOException {
    try {
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dst);

      byte[] buffer = new byte[1024];
      int len;
      while ((len = in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      in.close();
      out.close();
    }
    catch (Exception e) {
      Logger.print(e, 1);
    }
  }
}
