package applet;

import java.applet.Applet;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * rsc 31.12.2013
 */
public class Launcher extends Applet {

  private Map<String, String> appletParams;
  private JFrame frame;
  private Applet gameApplet;
  private String server = "classic2";
  private String client = "client233.client";

  public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    Launcher launcher = new Launcher();
    launcher.appletParams = launcher.getAppletParams();
    launcher.load();
    launcher.launch();
  }

  private Map<String, String> getAppletParams() throws IOException {
    Map<String, String> params = new HashMap<String, String>();
    String uri = "http://" + server + ".runescape.com/j0,a1";// unsigned applet url cus no cabbase
    BufferedReader in = new BufferedReader(new InputStreamReader(new URL(uri).openStream()));
    String line = null;
    while ((line = in.readLine()) != null) {
      if (line.startsWith("<param name=")) {
        line = line.substring("<param name=".length());
        line = line.substring(0, line.length() - 1);
        if (line.contains("\"")) {
          line = line.replaceAll("\"", "");
        }
        String name = line.substring(0, line.indexOf(' '));
        String value = line.substring(line.indexOf("value=") + "value=".length());
        System.out.println("PARAM PUT \"" + name + "\" = \"" + value + "\"");
        params.put(name, value);
      }
    }
    return params;
  }

  private void load() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    Class clazz = Class.forName(client);
    gameApplet = (Applet) clazz.newInstance();
    Method method = clazz.getMethod("provideLoaderApplet", Class.forName("java.applet.Applet"));
    method.invoke(null, this);
  }

  private void launch() {
    System.out.println("SERVER " + server);
    System.out.println("CLIENT " + client);

    frame = new JFrame("RuneScape Classic");
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.out.println("CLOSING");
                /*gameApplet.stop();    unsafe?
                gameApplet.destroy();
                try {
                    Thread.sleep(1000L);
                } catch (Exception ex) {
                }*/
        System.exit(0);
      }
    });
    gameApplet.setBounds(0, 0, 640, 480);
    Container container = frame.getContentPane();
    container.add(this);

    frame.setVisible(true);
    frame.setSize(640, 480);

    gameApplet.init();
    gameApplet.start();
  }

  public String getParameter(String name) {
    System.out.println("PARAM GET " + name);
    if (appletParams.containsKey(name)) {
      return appletParams.get(name);
    }
    return "";
  }

  public URL getDocumentBase() {
    try {
      return new URL("http://" + server + ".runescape.com/");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public URL getCodeBase() {
    try {
      return new URL("http://" + server + ".runescape.com/");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void destroy() {
    if (gameApplet != null) {
      gameApplet.destroy();
    }
  }

  public void paint(Graphics g) {
    if (gameApplet != null) {
      gameApplet.paint(g);
    }
  }

  public void update(Graphics g) {
    if (gameApplet != null) {
      gameApplet.paint(g);
    }
  }
}
