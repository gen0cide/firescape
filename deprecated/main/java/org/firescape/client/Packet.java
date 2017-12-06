package org.firescape.client;

import org.firescape.client.opcode.Opcode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Packet {

  //static char charMap[];
  public static int anIntArray537[] = new int[256];
  public static int anIntArray541[] = new int[256];
  public int readTries;
  public int maxReadTries;
  public int packetStart;
  public byte packetData[];
  /*private static int anIntArray521[] = {
      0, 1, 3, 7, 15, 31, 63, 127, 255, 511,
      1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff,
      0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff,
      0x3fffffff, 0x7fffffff, -1
  };
  int anInt522 = 61;
  int anInt523 = 59;
  int anInt524 = 42;
  int anInt525 = 43;
  int anInt526 = 44; // index list for charMap
  int anInt527 = 45;
  int anInt528 = 46;
  int anInt529 = 47;
  int anInt530 = 92;
  int anInt531 = 32;
  int anInt532 = 124;
  int anInt533 = 34;    */
  public ISAAC isaacIncoming;
  public ISAAC isaacOutgoing;
  public int packetEnd;
  protected int length;
  protected int packetMaxLength;
  protected boolean socketException;
  protected String socketExceptionMessage;
  protected int delay;
  private int packet8Check;

  public Packet() {
    packetEnd = 3;
    packet8Check = 8;
    packetMaxLength = 5000;
    socketException = false;
    socketExceptionMessage = "";
  }

  public void seedIsaac(int seed[]) {
    isaacIncoming = new ISAAC(seed);
    isaacOutgoing = new ISAAC(seed);
  }

  public void closeStream() {
  }

  public int readPacket(byte buff[]) {
    try {
      readTries++;
      if (maxReadTries > 0 && readTries > maxReadTries) {
        socketException = true;
        socketExceptionMessage = "time-out";
        maxReadTries += maxReadTries;
        return 0;
      }
      if (length == 0 && availableStream() >= 2) {
        length = readStream();
        if (length >= 160) {
          length = (length - 160) * 256 + readStream();
        }
      }
      if (length > 0 && availableStream() >= length) {
        if (length >= 160) {
          readBytes(length, buff);
        } else {
          buff[length - 1] = (byte) readStream();
          if (length > 1) {
            readBytes(length - 1, buff);
          }
        }
        int i = length;
        length = 0;
        readTries = 0;
        return i;
      }
    } catch (IOException ioexception) {
      socketException = true;
      socketExceptionMessage = ioexception.getMessage();
    }
    return 0;
  }

  public int availableStream() throws IOException {
    return 0;
  }

  public int readStream() throws IOException {
    return 0;
  }

  public void readBytes(int len, byte buff[]) throws IOException {
    readStreamBytes(len, 0, buff);
  }

  public void readStreamBytes(int i, int j, byte abyte0[]) throws IOException {
  }

  public boolean hasPacket() {
    return packetStart > 0;
  }

  public void putLong(long l) {
    putInt((int) (l >> 32));
    putInt((int) (l & -1L));
  }

  public void putInt(int i) {
    packetData[packetEnd++] = (byte) (i >> 24);
    packetData[packetEnd++] = (byte) (i >> 16);
    packetData[packetEnd++] = (byte) (i >> 8);
    packetData[packetEnd++] = (byte) i;
  }

  public void newPacket(int i) {
    if (packetStart > (packetMaxLength * 4) / 5) {
      try {
        writePacket(0);
      } catch (IOException ioexception) {
        socketException = true;
        socketExceptionMessage = ioexception.getMessage();
      }
    }
    if (packetData == null) {
      packetData = new byte[packetMaxLength];
    }
    packetData[packetStart + 2] = (byte) i;
    packetData[packetStart + 3] = 0;
    packetEnd = packetStart + 3;
    packet8Check = 8;
  }

  public void writePacket(int i) throws IOException {
    if (socketException) {
      packetStart = 0;
      packetEnd = 3;
      socketException = false;
      throw new IOException(socketExceptionMessage);
    }
    delay++;
    if (delay < i) {
      return;
    }
    if (packetStart > 0) {
      delay = 0;
      writeStreamBytes(packetData, 0, packetStart);
    }
    packetStart = 0;
    packetEnd = 3;
  }

  public void writeStreamBytes(byte abyte0[], int i, int j) throws IOException {
  }

  public long getLong() throws IOException {
    long l = getShort();
    long l1 = getShort();
    long l2 = getShort();
    long l3 = getShort();
    return (l << 48) + (l1 << 32) + (l2 << 16) + l3;
  }

  public int getShort() throws IOException {
    int i = getByte();
    int j = getByte();
    return i * 256 + j;
  }

  public int getByte() throws IOException {
    return readStream();
  }

  public void putString(String s) {
    try {
      byte[] msg = s.getBytes("UTF-8");
      for (int i = 0; i < msg.length; i++) {
        putByte(msg[i]);
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void putByte(int i) {
    packetData[packetEnd++] = (byte) i;
  }

  public int isaacCommand(int i) {
    return i - isaacIncoming.getNextValue() & 0xff;
  }

  public void flushPacket() throws IOException {
    sendPacket();
    writePacket(0);
  }

  public void sendPacket() {
    byte[] packetDebug = Arrays.copyOfRange(packetData, packetStart + 2, packetEnd);
    System.out.println(String.format(
      "[Packet] >>> %s(id=%d) size=%d\n%s",
      Opcode.getClient(Version.CLIENT, packetData[packetStart + 2] & 0xff),
      packetData[packetStart + 2] & 0xff,
      packetDebug.length,
      Utility.bytesToHex(packetDebug)
    ));
    if (isaacOutgoing != null) {
      int i = packetData[packetStart + 2] & 0xff;
      packetData[packetStart + 2] = (byte) (i + isaacOutgoing.getNextValue());
    }
    if (packet8Check != 8) // what the fuck is this even for? legacy?
    {
      packetEnd++;
    }
    int j = packetEnd - packetStart - 2;
    if (j >= 160) {
      packetData[packetStart] = (byte) (160 + j / 256);
      packetData[packetStart + 1] = (byte) (j & 0xff);
    } else {
      packetData[packetStart] = (byte) j;
      packetEnd--;
      packetData[packetStart + 1] = packetData[packetEnd];
    }
    if (packetMaxLength <= 10000) // this seems largely useless and doesn't appear to do anything
    {
      int k = packetData[packetStart + 2] & 0xff;
      anIntArray537[k]++;
      anIntArray541[k] += packetEnd - packetStart;
    }
    packetStart = packetEnd;
  }

  public void pjstr(String string) {
    int nul = string.indexOf('\0');
    if (nul >= 0) {
      throw new IllegalArgumentException("NUL character at " + nul + " - cannot pjstr");
    } else {
      packetEnd += Utility.writeUnicodeString(string, 0, string.length(), packetData, packetEnd);
      packetData[packetEnd++] = 0;
    }
  }

  public void pjstr2(String string) {
    int nul = string.indexOf('\0');
    if (nul >= 0) {
      throw new IllegalArgumentException("NUL character at " + nul + " - cannot pjstr2");
    } else {
      packetData[packetEnd++] = 0;
      packetEnd += Utility.writeUnicodeString(string, 0, string.length(), packetData, packetEnd);
      packetData[packetEnd++] = 0;
    }
  }

  public void xteaEncrypt(int start, int[] isaackeys, int off) {
    int var5 = packetEnd;
    packetEnd = start;
    int var6 = (-start + off) / 8;

    for (int var7 = 0; var7 < var6; ++var7) {
      int var8 = Utility.getUnsignedInt(packetData, packetEnd);
      packetEnd += 4; // TODO when 233 calls getUnsignedInt it actually advances the offset by 4 as far as i could tell
      int var9 = Utility.getUnsignedInt(packetData, packetEnd);
      packetEnd += 4; // TODO when 233 calls getUnsignedInt it actually advances the offset by 4 as far as i could tell
      int var10 = 0;
      int var11 = -1640531527;

      for (int var12 = 32; var12-- > 0; var9 += (var8 >>> 5 ^ var8 << 4) + var8 ^
                                                var10 - -isaackeys[var10 >>> 11 & 1356857347]) {
        var8 += var9 + (var9 << 4 ^ var9 >>> 5) ^ var10 - -isaackeys[3 & var10];
        var10 += var11;
      }

      packetEnd -= 8;
      putInt(var8);
      putInt(var9);
    }

    packetEnd = var5;
  }

  public void putLengthShort(int offset, int length) {
    packetData[-length + packetEnd - offset] = (byte) (length >> 8);
    packetData[-1 + (packetEnd - length)] = (byte) length;
  }

  public void putRandom() {
    byte[] randomdat = new byte[24];
    // TODO UNSAFE - add that random file shit here
    putBytes(randomdat, 0, 24);
  }

  public void putBytes(byte src[], int srcPos, int len) {
    System.arraycopy(src, srcPos, packetData, packetEnd, len);
    packetEnd += len;
  }

  public int putUnicodeString(String s) {
    int off = packetEnd;
    byte[] unicode = Utility.stringToUnicode(s);
    putShort2(unicode.length);
    packetEnd += Class11.instance.method241(0, unicode.length, packetData, unicode, packetEnd);
    return packetEnd - off;
  }

  public void putShort2(int var2) {
    if (var2 >= 0 && var2 < 128) {
      putByte(var2);
    } else if (var2 >= 0 && var2 < 0x8000) {
      putShort(var2 + 0x8000);
    } else {
      throw new IllegalArgumentException();
    }
  }

  public void putShort(int i) {
    packetData[packetEnd++] = (byte) (i >> 8);
    packetData[packetEnd++] = (byte) i;
  }

  // public static int anInt543;

    /*static
    {
        charMap = new char[256];
        for(int i = 0; i < 256; i++)
            charMap[i] = (char)i;

        charMap[61] = '=';
        charMap[59] = ';';
        charMap[42] = '*';
        charMap[43] = '+';
        charMap[44] = ',';
        charMap[45] = '-';
        charMap[46] = '.';
        charMap[47] = '/';
        charMap[92] = '\\';
        charMap[124] = '|';
        charMap[33] = '!';
        charMap[34] = '"';
    }            */
}
