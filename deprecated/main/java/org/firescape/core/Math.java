package org.firescape.core;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Math {

  private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yy");
  private static final Random rand = new Random();

  public static Random getRandom() {
    return rand;
  }

  public static boolean inArray(int[] haystack, int needle) {
    for (int option : haystack) {
      if (needle == option) {
        return true;
      }
    }
    return false;
  }

  public static int max(int i1, int i2) {
    return i1 > i2 ? i1 : i2;
  }

  public static int roundUp(double val) {
    return (int) java.lang.Math.round(val + 0.5D);
  }

  public static double round(double value, int decimalPlace) {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    return (bd.doubleValue());
  }

  public static int random(int low, int high) {
    return low + rand.nextInt(high - low + 1);
  }

  public static boolean percentChance(int percent) {
    return random(1, 100) <= percent;
  }

  public static String timeFormat(long l) {
    return formatter.format(l);
  }

  public static int randomWeighted(int low, int dip, int peak, int max) {
    int total = 0;
    int probability = 100;
    int[] probArray = new int[max + 1];
    for (int x = 0; x < probArray.length; x++) {
      total += probArray[x] = probability;
      if (x < dip || x > peak) {
        probability -= 3;
      } else {
        probability += 3;
      }
    }
    int hit = random(0, total);
    total = 0;
    for (int x = 0; x < probArray.length; x++) {
      if (hit >= total && hit < (total + probArray[x])) {
        return x;
      }
      total += probArray[x];
    }
    return 0;
  }

  public static int average(int[] values) {
    int total = 0;
    for (int value : values) {
      total += value;
    }
    return total / values.length;
  }
}
