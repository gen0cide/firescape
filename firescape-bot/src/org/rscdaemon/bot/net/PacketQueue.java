package org.rscdaemon.bot.net;

import java.util.ArrayList;
import java.util.List;

public class PacketQueue<T extends Packet> {

  public ArrayList<T> packets = new ArrayList<T>();

  public void add(T p) {
    synchronized (packets) {
      packets.add(p);
    }
  }

  public boolean hasPackets() {
    return !packets.isEmpty();
  }

  public List<T> getPackets() {
    List<T> tmpList;
    synchronized (packets) {
      tmpList = (List<T>) packets.clone();
      packets.clear();
    }
    return tmpList;
  }
}
