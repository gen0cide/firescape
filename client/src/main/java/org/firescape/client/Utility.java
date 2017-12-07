package org.firescape.client;

import java.io.*;
import java.net.URL;

public class Utility {

  //public static String lastFile;

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
  public static URL appletCodeBase = null;
  public static boolean aBoolean546;
  public static char[] unicodeChars = {
    '\u20AC',
    '\0'
    /**/,
    '\u201A',
    '\u0192',
    '\u201E',
    '\u2026',
    '\u2020',
    '\u2021',
    '\u02C6',
    '\u2030',
    '\u0160',
    '\u2039',
    '\u0152',
    '\0'
    /**/,
    '\u017D',
    '\0'
    /**/,
    '\0'
    /**/,
    '\u2018',
    '\u2019',
    '\u201C',
    '\u201D',
    '\u2022',
    '\u2013',
    '\u2014',
    '\u02DC',
    '\u2122',
    '\u0161',
    '\u203A',
    '\u0153',
    '\0'
    /**/,
    '\u017E',
    '\u0178'
  };
  private static int bitmask[] = {
    0,
    1,
    3,
    7,
    15,
    31,
    63,
    127,
    255,
    511,
    1023,
    2047,
    4095,
    8191,
    16383,
    32767,
    65535,
    0x1ffff,
    0x3ffff,
    0x7ffff,
    0xfffff,
    0x1fffff,
    0x3fffff,
    0x7fffff,
    0xffffff,
    0x1ffffff,
    0x3ffffff,
    0x7ffffff,
    0xfffffff,
    0x1fffffff,
    0x3fffffff,
    0x7fffffff,
    -1
  };
  private static int[] unknown = new int[256];
  // more weird shit mANE I DONT LIKE THE FUTURE
  private static char[] validUnicodeChars = new char[] {
    ' '
    /*32*/, 160
    /*160*/, '_'
    /*95*/, '-'
    /*45*/, 'à'
    /*224*/, 'á'
    /*225*/, 'â'
    /*226*/, 'ä'
    /*228*/, 'ã'
    /*227*/, 'À'
    /*192*/, 'Á'
    /*193*/, 'Â'
    /*194*/, 'Ä'
    /*196*/, 'Ã'
    /*195*/, 'è'
    /*232*/, 'é'
    /*233*/, 'ê'
    /*234*/, 'ë'
    /*235*/, 'È'
    /*200*/, 'É'
    /*201*/, 'Ê'
    /*202*/, 'Ë'
    /*203*/, 'í'
    /*237*/, 'î'
    /*238*/, 'ï'
    /*239*/, 'Í'
    /*205*/, 'Î'
    /*206*/, 'Ï'
    /*207*/, 'ò'
    /*242*/, 'ó'
    /*243*/, 'ô'
    /*244*/, 'ö'
    /*246*/, 'õ'
    /*245*/, 'Ò'
    /*210*/, 'Ó'
    /*211*/, 'Ô'
    /*212*/, 'Ö'
    /*214*/, 'Õ'
    /*213*/, 'ù'
    /*249*/, 'ú'
    /*250*/, 'û'
    /*251*/, 'ü'
    /*252*/, 'Ù'
    /*217*/, 'Ú'
    /*218*/, 'Û'
    /*219*/, 'Ü'
    /*220*/, 'ç'
    /*231*/, 'Ç'
    /*199*/, 'ÿ'
    /*255*/, 'Ÿ'
    /*376*/, 'ñ'
    /*241*/, 'Ñ'
    /*209*/, 'ß'
    /*223*/
  };
  private static char[] validDelimiters = new char[] {
    '[', ']', '#'
  };

  static {
    for (int var0 = 0; var0 < 256; ++var0) {
      int var1 = var0;

      for (int var2 = 0; var2 < 8; ++var2) {
        if ((var1 & 1) == 1) {
          var1 = -306674912 ^ var1 >>> 1;
        } else {
          var1 >>>= 1;
        }
      }

      unknown[var0] = var1;
    }
  }

  public static void readFully(String s, byte abyte0[], int i) throws IOException {
    InputStream inputstream = openFile(s);
    DataInputStream datainputstream = new DataInputStream(inputstream);
    try {
      datainputstream.readFully(abyte0, 0, i);
    } catch (EOFException Ex) {
    }
    datainputstream.close();
  }

  public static InputStream openFile(String s) throws IOException {
    //lastFile = s;
    Object obj;
    if (appletCodeBase == null) {
      obj = new BufferedInputStream(new FileInputStream(s));
    } else {
      URL url = new URL(appletCodeBase, s);
      obj = url.openStream();
    }
    return ((InputStream) (obj));
  }

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

  public static int getUnsignedInt2(byte abyte0[], int i) {
    if ((abyte0[i] & 0xff) < 128) {
      return abyte0[i];
    } else {
      return ((abyte0[i] & 0xff) - 128 << 24) +
             ((abyte0[i + 1] & 0xff) << 16) +
             ((abyte0[i + 2] & 0xff) << 8) +
             (abyte0[i + 3] & 0xff);
    }
  }

  public static int getBitMask(byte buff[], int off, int len) {
    int k = off >> 3;
    int l = 8 - (off & 7);
    int i1 = 0;
    for (; len > l; l = 8) {
      i1 += (buff[k++] & bitmask[l]) << len - l;
      len -= l;
    }

    if (len == l) {
      i1 += buff[k] & bitmask[l];
    } else {
      i1 += buff[k] >> l - len & bitmask[len];
    }
    return i1;
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

  public static String ip2string(int i) {
    return (i >> 24 & 0xff) + "." + (i >> 16 & 0xff) + "." + (i >> 8 & 0xff) + "." + (i & 0xff);
  }

  public static long username2hash(String s) {
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

  public static String hash2username(long hash) {
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

  public static int getDataFileOffset(String filename, byte data[]) {
    int numEntries = getUnsignedShort(data, 0);
    int wantedHash = 0;
    filename = filename.toUpperCase();
    for (int k = 0; k < filename.length(); k++) {
      wantedHash = (wantedHash * 61 + filename.charAt(k)) - 32;
    }

    int offset = 2 + numEntries * 10;
    for (int entry = 0; entry < numEntries; entry++) {
      int fileHash = (data[entry * 10 + 2] & 0xff) * 0x1000000 +
                     (data[entry * 10 + 3] & 0xff) * 0x10000 +
                     (data[entry * 10 + 4] & 0xff) * 256 +
                     (data[entry * 10 + 5] & 0xff);
      int fileSize = (data[entry * 10 + 9] & 0xff) * 0x10000 +
                     (data[entry * 10 + 10] & 0xff) * 256 +
                     (data[entry * 10 + 11] & 0xff);
      if (fileHash == wantedHash) {
        return offset;
      }
      offset += fileSize;
    }

    return 0;
  }

  public static int getUnsignedShort(byte abyte0[], int i) {
    return ((abyte0[i] & 0xff) << 8) + (abyte0[i + 1] & 0xff);
  }

  public static int getDataFileLength(String filename, byte data[]) {
    int numEntries = getUnsignedShort(data, 0);
    int wantedHash = 0;
    filename = filename.toUpperCase();
    for (int k = 0; k < filename.length(); k++) {
      wantedHash = (wantedHash * 61 + filename.charAt(k)) - 32;
    }

    int offset = 2 + numEntries * 10;
    for (int i1 = 0; i1 < numEntries; i1++) {
      int fileHash = (data[i1 * 10 + 2] & 0xff) * 0x1000000 +
                     (data[i1 * 10 + 3] & 0xff) * 0x10000 +
                     (data[i1 * 10 + 4] & 0xff) * 256 +
                     (data[i1 * 10 + 5] & 0xff);
      int fileSize = (data[i1 * 10 + 6] & 0xff) * 0x10000 +
                     (data[i1 * 10 + 7] & 0xff) * 256 +
                     (data[i1 * 10 + 8] & 0xff);
      int fileSizeCompressed = (data[i1 * 10 + 9] & 0xff) * 0x10000 +
                               (data[i1 * 10 + 10] & 0xff) * 256 +
                               (data[i1 * 10 + 11] & 0xff);
      if (fileHash == wantedHash) {
        return fileSize;
      }
      offset += fileSizeCompressed;
    }

    return 0;
  }

  public static byte[] loadData(String s, int i, byte abyte0[]) {
    byte[] b = unpackData(s, i, abyte0, null);
        /*try {
            String parent = "./unpacked/" + lastFile + "/";
            System.out.println("Dumping " + parent + s);
            File f = new File(parent);
            f.mkdirs();
            f = new File(f.getPath(), s);
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } */
    return b;
  }

  public static byte[] unpackData(String filename, int i, byte archiveData[], byte fileData[]) {
    int numEntries = (archiveData[0] & 0xff) * 256 + (archiveData[1] & 0xff);
    int wantedHash = 0;
    filename = filename.toUpperCase();
    for (int l = 0; l < filename.length(); l++) {
      wantedHash = (wantedHash * 61 + filename.charAt(l)) - 32;
    }

    int offset = 2 + numEntries * 10;
    for (int entry = 0; entry < numEntries; entry++) {
      int fileHash = (archiveData[entry * 10 + 2] & 0xff) * 0x1000000 +
                     (archiveData[entry * 10 + 3] & 0xff) * 0x10000 +
                     (archiveData[entry * 10 + 4] & 0xff) * 256 +
                     (archiveData[entry * 10 + 5] & 0xff);
      int fileSize = (archiveData[entry * 10 + 6] & 0xff) * 0x10000 +
                     (archiveData[entry * 10 + 7] & 0xff) * 256 +
                     (archiveData[entry * 10 + 8] & 0xff);
      int fileSizeCompressed = (archiveData[entry * 10 + 9] & 0xff) * 0x10000 +
                               (archiveData[entry * 10 + 10] & 0xff) * 256 +
                               (archiveData[entry * 10 + 11] & 0xff);
      if (fileHash == wantedHash) {
        if (fileData == null) {
          fileData = new byte[fileSize + i];
        }
        if (fileSize != fileSizeCompressed) {
          BZLib.decompress(fileData, fileSize, archiveData, fileSizeCompressed, offset);
        } else {
          for (int j = 0; j < fileSize; j++) {
            fileData[j] = archiveData[offset + j];
          }

        }
        return fileData;
      }
      offset += fileSizeCompressed;
    }

    return null;
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

  /**
   * returns the code used to represent the given character in our byte array encoding methods
   */
  private static int getCharCode(char c) {
    for (int x = 0; x < characters.length; x++) {
      if (c == characters[x]) {
        return x;
      }
    }
    return 0;
  }

  public static String byteToString(byte[] data, int offset, int length) {
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

  public static byte[] stringToUnicode(String string) {
    byte[] buffer = new byte[string.length()];
    writeUnicodeString(string, 0, string.length(), buffer, 0);
    return buffer;
  }

  public static int writeUnicodeString(CharSequence charseq, int seqoff, int seqlen, byte[] buf, int bufoff) {
    int strlen = seqlen - seqoff;
    for (int i = 0; i < strlen; i++) {
      char c = charseq.charAt(seqoff + i);
      if (c > 0 && c < '\200' || c >= '\240' && c <= '\377') {
        buf[bufoff + i] = (byte) c;
        continue;
      }
      if (c == '\u20AC') {
        buf[bufoff + i] = -128;
        continue;
      }
      if (c == '\u201A') {
        buf[bufoff + i] = -126;
        continue;
      }
      if (c == '\u0192') {
        buf[bufoff + i] = -125;
        continue;
      }
      if (c == '\u201E') {
        buf[bufoff + i] = -124;
        continue;
      }
      if (c == '\u2026') {
        buf[bufoff + i] = -123;
        continue;
      }
      if (c == '\u2020') {
        buf[bufoff + i] = -122;
        continue;
      }
      if (c == '\u2021') {
        buf[bufoff + i] = -121;
        continue;
      }
      if (c == '\u02C6') {
        buf[bufoff + i] = -120;
        continue;
      }
      if (c == '\u2030') {
        buf[bufoff + i] = -119;
        continue;
      }
      if (c == '\u0160') {
        buf[bufoff + i] = -118;
        continue;
      }
      if (c == '\u2039') {
        buf[bufoff + i] = -117;
        continue;
      }
      if (c == '\u0152') {
        buf[bufoff + i] = -116;
        continue;
      }
      if (c == '\u017D') {
        buf[bufoff + i] = -114;
        continue;
      }
      if (c == '\u2018') {
        buf[bufoff + i] = -111;
        continue;
      }
      if (c == '\u2019') {
        buf[bufoff + i] = -110;
        continue;
      }
      if (c == '\u201C') {
        buf[bufoff + i] = -109;
        continue;
      }
      if (c == '\u201D') {
        buf[bufoff + i] = -108;
        continue;
      }
      if (c == '\u2022') {
        buf[bufoff + i] = -107;
        continue;
      }
      if (c == '\u2013') {
        buf[bufoff + i] = -106;
        continue;
      }
      if (c == '\u2014') {
        buf[bufoff + i] = -105;
        continue;
      }
      if (c == '\u02DC') {
        buf[bufoff + i] = -104;
        continue;
      }
      if (c == '\u2122') {
        buf[bufoff + i] = -103;
        continue;
      }
      if (c == '\u0161') {
        buf[bufoff + i] = -102;
        continue;
      }
      if (c == '\u203A') {
        buf[bufoff + i] = -101;
        continue;
      }
      if (c == '\u0153') {
        buf[bufoff + i] = -100;
        continue;
      }
      if (c == '\u017E') {
        buf[bufoff + i] = -98;
        continue;
      }
      if (c == '\u0178') {
        buf[bufoff + i] = -97;
      } else {
        buf[bufoff + i] = '?';
      }
    }

    return strlen;
  }

  public static int rgb2long(int red, int green, int blue) {
    return (red << 16) + (green << 8) + blue;
  }

  public static Gjstr2 gjstr2(byte[] buffer, int offset) {
    byte nul = buffer[offset++];
    if (nul != 0) {
      throw new IllegalStateException("Bad version number in gjstr2");
    } else {
      int off = offset;
      while (buffer[offset++] != 0) {
      }
      int len = offset - off - 1;
      String result = "";
      if (len > 0) {
        result = Utility.readUnicodeString(buffer, off, len);
      }
      return new Gjstr2(result, offset);
    }
  }

  public static String readUnicodeString(byte[] buf, int bufoff, int len) {
    char[] chars = new char[len];
    int off = 0;
    for (int j = 0; j < len; j++) {
      int uchar = buf[bufoff + j] & 0xff;
      if (uchar == 0) {
        continue;
      }
      if (uchar >= 128 && uchar < 160) {
        char c = unicodeChars[uchar - 128];
        if (c == '\0') {
          c = '?';
        }
        uchar = c;
      }
      chars[off++] = (char) uchar;
    }
    return new String(chars, 0, off);
  }

  public static void sleep(long var0) {
    try {
      Thread.sleep(var0);
    } catch (InterruptedException var4) {
    }
  }

  public static Cabbage cabbage(byte[] buffer, int offset) {
    return cabbage(buffer, offset, 32767);
  }

  public static Cabbage cabbage(byte[] buffer, int offset, int limit) {
    try {
      int i1;
      if (getUnsignedByte(buffer[offset]) < 128) {
        i1 = getUnsignedByte(buffer[offset]);
        offset++;
      } else {
        i1 = getUnsignedShort(buffer, offset) - 32768;
        offset += 2;
      }

      if (i1 > limit) {
        i1 = limit;
      }

      byte[] data = new byte[i1];
      offset += Class11.instance.method240(buffer, 0, data, offset, i1);
      String s1 = Utility.readUnicodeString(data, 0, i1);
      return new Cabbage(s1, offset);
    } catch (Exception exception) {
      return new Cabbage("Cabbage", offset);
    }
  }

  public static boolean valueSame(byte[] buffer, int offset) {
    offset -= 4;
    int var2 = Utility.getnum(offset, 0, buffer);
    int var3 = getUnsignedInt(buffer, offset);
    return var3 == var2;
  }

  public static int getnum(int length, int offset, byte[] buffer) {
    int var4 = -1;

    for (int var5 = offset; var5 < length; ++var5) {
      var4 = var4 >>> 8 ^ unknown[(buffer[var5] ^ var4) & 255];
    }

    var4 = ~var4;

    return var4;
  }

  public static UInt3 getUnsignedInt3(byte[] buffer, int offset) {
    int val;
    if (buffer[offset] >= 0) {
      val = getUnsignedShort(buffer, offset);
      offset += 2;
    } else {
      val = Integer.MAX_VALUE & getUsignedInt9001(buffer, offset);
      offset += 4;
    }
    return new UInt3(val, offset); // TODO lol fuck man
  }

  public static int getUsignedInt9001(byte buffer[], int offset) {
    offset += 4;
    return (buffer[offset - 1] & 255) + ((255 & buffer[-4 + offset]) << 24) -
           (-(buffer[-3 + offset] << 16 & 16711680) - (buffer[-2 + offset] << 8 & '\uff00'));
  }

  public static String formatName(String name) {
    if (name == null) {
      return null;
    } else {
      int startIndex = 0;

      int endIndex;
      for (endIndex = name.length(); startIndex < endIndex && shouldTrim(name.charAt(startIndex)); ++startIndex) {
      }

      while (startIndex < endIndex && shouldTrim(name.charAt(endIndex - 1))) {
        --endIndex;
      }

      int nameLength = endIndex - startIndex;
      if (nameLength >= 1 && nameLength <= 12) {
        StringBuffer formattedName = new StringBuffer(nameLength);

        for (int index = startIndex; index < endIndex; ++index) {
          char c = name.charAt(index);
          if (isValidChar(c, 0)) {
            char sanitizedChar = sanitize(c);
            if (sanitizedChar != 0) {
              formattedName.append(sanitizedChar);
            }
          }
        }

        if (formattedName.length() == 0) {
          return null;
        } else {
          return formattedName.toString();
        }
      } else {
        return null;
      }
    }
  }

  private static boolean shouldTrim(char c) {
    return c == 160 || c == ' ' || c == '_' || c == '-';
  }

  private static boolean isValidChar(char c, int startIndex) {
    if (java.lang.Character.isISOControl(c)) {
      return false;
    } else if (isAscii(c)) {
      return true;
    } else {
      // is unicode
      char[] validChars = validUnicodeChars;

      for (int index = startIndex; index < validChars.length; ++index) {
        char validChar = validChars[index];
        if (validChar == c) {
          return true;
        }
      }

      // like clan tags etc i guess, [FAG] Pewpew, Pewpew #FAG
      char[] validDelims = validDelimiters;

      for (int index = 0; validDelims.length > index; ++index) {
        char validChar = validDelims[index];
        if (validChar == c) {
          return true;
        }
      }

      return false;
    }
  }

  private static char sanitize(char c) {
    if (c == ' ' || c == 160 || c == '_' || c == '-') {
      return '_';
    }
    if (c == '[' || c == ']' || c == '#') {
      return c;
    }
    if ((c >= 'à' && c <= 'ä') || (c >= 'À' && c <= 'Ä')) {
      return 'a';
    }
    if ((c >= 'è' && c <= 'ë') || (c >= 'É' && c <= 'Ë')) { // HA! THATS A FUCKING BUG M8, MISSING 'È'
      return 'e';
    }
    if ((c >= 'í' && c <= 'ï') || (c >= 'Í' && c <= 'Ï')) {
      return 'i';
    }
    if ((c >= 'ò' && c <= 'ö') || (c >= 'Ò' && c <= 'Ö')) {
      return 'o';
    }
    if ((c >= 'ù' && c <= 'ü') || (c >= 'Ù' && c <= 'Ü')) {
      return 'u';
    }
    if (c == 'ç' || c == 'Ç') {
      return 'c';
    }
    if (c == 'ÿ' || c == 'Ÿ') {
      return 'y';
    }
    if (c == 'ñ' || c == 'Ñ') {
      return 'n';
    }
    if (c == 'ß') {
      return 'b';
    }
    return java.lang.Character.toLowerCase(c);
  }

  private static boolean isAscii(char c) {
    return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
  }

  public static void insertBytes(byte[] src, int srcPos, byte[] dst, int dstPos, int len) {
    if (src == dst) {
      if (srcPos == dstPos) {
        return;
      }

      if (dstPos > srcPos && dstPos < srcPos + len) {
        --len;
        srcPos += len;
        dstPos += len;
        len = srcPos - len;

        for (len += 7; srcPos >= len; dst[dstPos--] = src[srcPos--]) {
          dst[dstPos--] = src[srcPos--];
          dst[dstPos--] = src[srcPos--];
          dst[dstPos--] = src[srcPos--];
          dst[dstPos--] = src[srcPos--];
          dst[dstPos--] = src[srcPos--];
          dst[dstPos--] = src[srcPos--];
          dst[dstPos--] = src[srcPos--];
        }

        for (len -= 7; srcPos >= len; dst[dstPos--] = src[srcPos--]) {
        }

        return;
      }
    }

    len += srcPos;

    for (len -= 7; srcPos < len; dst[dstPos++] = src[srcPos++]) {
      dst[dstPos++] = src[srcPos++];
      dst[dstPos++] = src[srcPos++];
      dst[dstPos++] = src[srcPos++];
      dst[dstPos++] = src[srcPos++];
      dst[dstPos++] = src[srcPos++];
      dst[dstPos++] = src[srcPos++];
      dst[dstPos++] = src[srcPos++];
    }

    for (len += 7; srcPos < len; dst[dstPos++] = src[srcPos++]) {
    }

  }

  public static int calculateShopItemPrice(int basePrice, int itemCount, boolean buying, int priceModifier, int loopCycles, int itemPrice, int priceMultiplier) {
    int price = 0;

    for (int var9 = 0; loopCycles > var9; ++var9) {
      int var10 = priceMultiplier * (-itemCount + (buying ? var9 : -var9) + itemPrice);
      if (var10 < -100) {
        var10 = -100;
      } else if (var10 > 100) {
        var10 = 100;
      }

      int var11 = priceModifier + var10;
      if (var11 < 10) {
        var11 = 10;
      }

      price += basePrice * var11 / 100;
    }

    return price;
  }

  public static class Gjstr2 {

    public String result;
    public int newOffset;

    public Gjstr2(String result, int read) {
      this.result = result;
      this.newOffset = read;
    }
  }

  public static class Cabbage extends Gjstr2 {

    public Cabbage(String s, int i) {
      super(s, i);
    }
  }

  public static class UInt3 {

    public int result;
    public int newOffset;

    public UInt3(int result, int newOffset) {
      this.result = result;
      this.newOffset = newOffset;
    }
  }
}
