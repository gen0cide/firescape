package org.firescape.client.assets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

public class Sprite {

  public static Gson gson = new GsonBuilder().setPrettyPrinting()
                                             .serializeNulls()
                                             .excludeFieldsWithModifiers(Modifier.PRIVATE)
                                             .create();
  public int id;
  public int offset;
  public int width;
  public int height;
  public int fullWidth;
  public int fullHeight;
  public int colorCount;
  public int[] colors;
  public int translateX;
  public int translateY;
  public boolean translate;
  private int[] pixels;

  public Sprite(
    int id, int offset, int width, int height, int fullWidth, int fullHeight, int colorCount, int[] colors, int translateX, int translateY, boolean translate, int[] pixels
  ) {
    this.id = id;
    this.offset = offset;
    this.width = width;
    this.height = height;
    this.fullWidth = fullWidth;
    this.fullHeight = fullHeight;
    this.colorCount = colorCount;
    this.colors = colors;
    this.translateX = translateX;
    this.translateY = translateY;
    this.translate = translate;
    this.pixels = pixels;
  }

  public void saveSprite() {
    saveImage();
    saveMetadata();
  }

  public void saveImage() {
    BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        image.setRGB(x, y, this.pixels[x + y * width]);
      }
    }
    try {
      File outputfile = new File("conf/sprites/" + id + ".png");
      ImageIO.write(image, "png", outputfile);
    } catch (IOException e) {
      System.out.println("ERROR WRITING SPRITE: " + id);
      e.printStackTrace();
    }
  }

  public void saveMetadata() {
    try (PrintWriter out = new PrintWriter("conf/json/sprites/" + id + ".json")) {
      out.println(gson.toJson(this));
    } catch (FileNotFoundException e) {
      System.out.println("Nope!");
    }
  }
}
