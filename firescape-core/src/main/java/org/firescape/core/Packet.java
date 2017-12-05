package org.firescape.core;

import java.io.UnsupportedEncodingException;

public class Packet {

  protected static final int DEFAULT_SIZE = 32;
  protected static int bitmasks[] = {
    0,
    0x1,
    0x3,
    0x7,
    0xf,
    0x1f,
    0x3f,
    0x7f,
    0xff,
    0x1ff,
    0x3ff,
    0x7ff,
    0xfff,
    0x1fff,
    0x3fff,
    0x7fff,
    0xffff,
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

  protected byte[] payload;
  protected int curLength;
  protected int bitPosition;
  protected boolean bare;
  protected int pID;

  public Packet() {
    this(DEFAULT_SIZE);
  }

  public Packet(int capacity) {
    payload = new byte[capacity];
  }

  public Packet setBare(boolean bare) {
    this.bare = bare;
    return this;
  }

  public Packet setID(int pID) {
    this.pID = pID;
    return this;
  }

  public Packet addBits(int value, int numBits) {
    int bytePos = bitPosition >> 3;
    int bitOffset = 8 - (bitPosition & 7);
    bitPosition += numBits;
    curLength = (bitPosition + 7) / 8;
    ensureCapacity(curLength);
    for (; numBits > bitOffset; bitOffset = 8) {
      payload[bytePos] &= ~bitmasks[bitOffset]; // mask out the desired area
      payload[bytePos++] |= (value >> (numBits - bitOffset)) & bitmasks[bitOffset];
      numBits -= bitOffset;
    }
    if (numBits == bitOffset) {
      payload[bytePos] &= ~bitmasks[bitOffset];
      payload[bytePos] |= value & bitmasks[bitOffset];
    } else {
      payload[bytePos] &= ~(bitmasks[numBits] << (bitOffset - numBits));
      payload[bytePos] |= (value & bitmasks[numBits]) << (bitOffset - numBits);
    }
    return this;
  }

  private void ensureCapacity(int minimumCapacity) {
    if (minimumCapacity >= payload.length) {
      expandCapacity(minimumCapacity);
    }
  }

  private void expandCapacity(int minimumCapacity) {
    int newCapacity = (payload.length + 1) * 2;
    if (newCapacity < 0) {
      newCapacity = Integer.MAX_VALUE;
    } else if (minimumCapacity > newCapacity) {
      newCapacity = minimumCapacity;
    }
    int oldLength = curLength;
    if (oldLength > payload.length) {
      oldLength = payload.length;
    }
    byte[] newPayload = new byte[newCapacity];
    System.arraycopy(payload, 0, newPayload, 0, oldLength);
    payload = newPayload;
  }

  public Packet addBytes(byte[] data) {
    return addBytes(data, 0, data.length);
  }

  public Packet addString(String s) {
    try {
      byte[] msg = s.getBytes("UTF-8");
      for (int i = 0; i < msg.length; i++) {
        addByte(msg[i]);
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return this;
  }

  public Packet addBytes(byte[] data, int offset, int len) {
    int newLength = curLength + len;
    ensureCapacity(newLength);
    System.arraycopy(data, offset, payload, curLength, len);
    curLength = newLength;
    return this;
  }

  public Packet addByte(byte val) {
    return addByte(val, true);
  }

  private Packet addByte(byte val, boolean checkCapacity) {
    if (checkCapacity) {
      ensureCapacity(curLength + 1);
    }
    payload[curLength++] = val;
    return this;
  }

  public Packet addShort(int val) {
    ensureCapacity(curLength + 2);
    addByte((byte) (val >> 8), false);
    addByte((byte) val, false);
    return this;
  }

  public Packet addLong(long val) {
    addInt((int) (val >> 32));
    addInt((int) (val & -1L));
    return this;
  }

  public Packet addInt(int val) {
    ensureCapacity(curLength + 4);
    addByte((byte) (val >> 24), false);
    addByte((byte) (val >> 16), false);
    addByte((byte) (val >> 8), false);
    addByte((byte) val, false);
    return this;
  }

  public byte[] toPacket() {
    byte[] data = new byte[curLength];
    System.arraycopy(payload, 0, data, 0, curLength);
    return data;
  }
}
