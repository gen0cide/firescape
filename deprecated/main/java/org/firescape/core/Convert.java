package org.firescape.core;

public class Convert {

  private static final char[] characters = {
    ' ',
    'e',
    't',
    'a',
    'o',
    'i',
    'h',
    'n',
    's',
    'r',
    'd',
    'l',
    'u',
    'm',
    'w',
    'c',
    'y',
    'f',
    'g',
    'p',
    'b',
    'v',
    'k',
    'x',
    'j',
    'q',
    'z',
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
    ' ',
    '!',
    '?',
    '.',
    ',',
    ':',
    ';',
    '(',
    ')',
    '-',
    '&',
    '*',
    '\\',
    '\'',
    '@',
    '#',
    '+',
    '=',
    '\243',
    '$',
    '%',
    '"',
    '[',
    ']'
  };

  public static String bytesToHex(byte[] in) {
    final StringBuilder builder = new StringBuilder();
    for (byte b : in) {
      builder.append(String.format("%02x ", b));
    }
    return builder.toString();
  }

  public static long getUnsignedLong(byte buff[], int off) {
    return (((long) getUnsignedInt(buff, off) & 0xffffffffL) << 32) +
           ((long) getUnsignedInt(buff, off + 4) & 0xffffffffL);
  }

  public static int getUnsignedInt(byte abyte0[], int i) {
    return ((abyte0[i] & 0xff) << 24) +
           ((abyte0[i + 1] & 0xff) << 16) +
           ((abyte0[i + 2] & 0xff) << 8) +
           (abyte0[i + 3] & 0xff);
  }

  public static int getSignedShort(byte abyte0[], int i) {
    int j = getUnsignedByte(abyte0[i]) * 256 + getUnsignedByte(abyte0[i + 1]);
    if (j > 32767) {
      j -= 0x10000;
    }
    return j;
  }

  public static int getUnsignedByte(byte byte0) {
    return byte0 & 0xff;
  }

  public static int getUnsignedShort(byte abyte0[], int i) {
    return ((abyte0[i] & 0xff) << 8) + (abyte0[i + 1] & 0xff);
  }

  public static String formatAuthString(String s, int maxlen) {
    String s1 = "";
    for (int j = 0; j < maxlen; j++) {
      if (j >= s.length()) {
        s1 = s1 + " ";
      } else {
        char c = s.charAt(j);
        if (c >= 'a' && c <= 'z') {
          s1 = s1 + c;
        } else if (c >= 'A' && c <= 'Z') {
          s1 = s1 + c;
        } else if (c >= '0' && c <= '9') {
          s1 = s1 + c;
        } else {
          s1 = s1 + '_';
        }
      }
    }

    return s1;
  }

  public static String ipAddrIntToString(long i) {
    return (i >> 24 & 0xff) + "." + (i >> 16 & 0xff) + "." + (i >> 8 & 0xff) + "." + (i & 0xff);
  }

  public static long ipAddrStringToLong(String ip) {
    String[] octets = ip.split("\\.");
    long result = 0L;
    for (int x = 0; x < 4; x++) {
      result += Integer.parseInt(octets[x]) * Math.pow(256, 3 - x);
    }
    return result;
  }

  public static long usernameToHash(String s) {
    String s1 = "";
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c >= 'a' && c <= 'z') {
        s1 = s1 + c;
      } else if (c >= 'A' && c <= 'Z') {
        s1 = s1 + (char) ((c + 97) - 65);
      } else if (c >= '0' && c <= '9') {
        s1 = s1 + c;
      } else {
        s1 = s1 + ' ';
      }
    }

    s1 = s1.trim();
    if (s1.length() > 12) {
      s1 = s1.substring(0, 12);
    }
    long hash = 0L;
    for (int j = 0; j < s1.length(); j++) {
      char c1 = s1.charAt(j);
      hash *= 37L;
      if (c1 >= 'a' && c1 <= 'z') {
        hash += (1 + c1) - 97;
      } else if (c1 >= '0' && c1 <= '9') {
        hash += (27 + c1) - 48;
      }
    }

    return hash;
  }

  public static String hashToUsername(long hash) {
    if (hash < 0L) {
      return "invalidName";
    }
    String s = "";
    while (hash != 0L) {
      int i = (int) (hash % 37L);
      hash /= 37L;
      if (i == 0) {
        s = " " + s;
      } else if (i < 27) {
        if (hash % 37L == 0L) {
          s = (char) ((i + 65) - 1) + s;
        } else {
          s = (char) ((i + 97) - 1) + s;
        }
      } else {
        s = (char) ((i + 48) - 27) + s;
      }
    }
    return s;
  }

  public static byte[] stringToByteArray(String message) {
    byte[] buffer = new byte[100];
    if (message.length() > 80) {
      message = message.substring(0, 80);
    }
    message = message.toLowerCase();
    int length = 0;
    int j = -1;
    for (int k = 0; k < message.length(); k++) {
      int code = getCharCode(message.charAt(k));
      if (code > 12) {
        code += 195;
      }
      if (j == -1) {
        if (code < 13) {
          j = code;
        } else {
          buffer[length++] = (byte) code;
        }
      } else if (code < 13) {
        buffer[length++] = (byte) ((j << 4) + code);
        j = -1;
      } else {
        buffer[length++] = (byte) ((j << 4) + (code >> 4));
        j = code & 0xf;
      }
    }
    if (j != -1) {
      buffer[length++] = (byte) (j << 4);
    }
    byte[] string = new byte[length];
    System.arraycopy(buffer, 0, string, 0, length);
    return string;
  }

  private static int getCharCode(char c) {
    for (int x = 0; x < characters.length; x++) {
      if (c == characters[x]) {
        return x;
      }
    }
    return 0;
  }

  public static String byteArrayToString(byte[] data, int offset, int length) {
    char[] buffer = new char[100];
    try {
      int k = 0;
      int l = -1;
      for (int i1 = 0; i1 < length; i1++) {
        int j1 = data[offset++] & 0xff;
        int k1 = j1 >> 4 & 0xf;
        if (l == -1) {
          if (k1 < 13) {
            buffer[k++] = characters[k1];
          } else {
            l = k1;
          }
        } else {
          buffer[k++] = characters[((l << 4) + k1) - 195];
          l = -1;
        }
        k1 = j1 & 0xf;
        if (l == -1) {
          if (k1 < 13) {
            buffer[k++] = characters[k1];
          } else {
            l = k1;
          }
        } else {
          buffer[k++] = characters[((l << 4) + k1) - 195];
          l = -1;
        }
      }
      boolean flag = true;
      for (int l1 = 0; l1 < k; l1++) {
        char c = buffer[l1];
        if (l1 > 4 && c == '@') {
          buffer[l1] = ' ';
        }
        if (c == '%') {
          buffer[l1] = ' ';
        }
        if (flag && c >= 'a' && c <= 'z') {
          buffer[l1] += '\uFFE0';
          flag = false;
        }
        if (c == '.' || c == '!' || c == ':') {
          flag = true;
        }
      }
      return new String(buffer, 0, k);
    } catch (Exception e) {
      return ".";
    }
  }

  public static int rgbToInt(int red, int green, int blue) {
    return (red << 16) + (green << 8) + blue;
  }
}
