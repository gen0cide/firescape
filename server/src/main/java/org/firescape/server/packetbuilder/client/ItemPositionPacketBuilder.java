package org.firescape.server.packetbuilder.client;

import org.firescape.server.model.Item;
import org.firescape.server.model.Player;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.packetbuilder.RSCPacketBuilder;
import org.firescape.server.util.DataConversions;
import org.firescape.server.util.StatefulEntityCollection;

import java.util.Collection;

public class ItemPositionPacketBuilder {

  private Player playerToUpdate;

  /**
   * Sets the player to update
   */
  public void setPlayer(Player p) {
    playerToUpdate = p;
  }

  public RSCPacket getPacket() {
    StatefulEntityCollection<Item> watchedItems = playerToUpdate.getWatchedItems();
    if (watchedItems.changed()) {
      Collection<Item> newItems = watchedItems.getNewEntities();
      Collection<Item> knownItems = watchedItems.getKnownEntities();
      RSCPacketBuilder packet = new RSCPacketBuilder();
      packet.setID(99);
      for (Item i : knownItems) {
        if (watchedItems.isRemoving(i)) {
          byte[] offsets = DataConversions.getObjectPositionOffsets(i.getLocation(), playerToUpdate.getLocation());
          // if(it's miles away) {
          // packet.addByte((byte)255);
          // packet.addByte((byte)sectionX);
          // packet.addByte((byte)sectionY);
          // }
          // else {
          packet.addShort(i.getID() + 32768);
          packet.addByte(offsets[0]);
          packet.addByte(offsets[1]);
          // }
        }
      }
      for (Item i : newItems) {
        byte[] offsets = DataConversions.getObjectPositionOffsets(i.getLocation(), playerToUpdate.getLocation());
        packet.addShort(i.getID());
        packet.addByte(offsets[0]);
        packet.addByte(offsets[1]);
      }
      return packet.toPacket();
    }
    return null;
  }
}
