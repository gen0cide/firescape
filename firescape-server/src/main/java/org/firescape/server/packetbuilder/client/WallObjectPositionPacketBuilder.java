package org.firescape.server.packetbuilder.client;

import org.firescape.server.model.GameObject;
import org.firescape.server.model.Player;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.util.DataConversions;
import org.firescape.server.util.StatefulEntityCollection;
import org.firescape.server.packetbuilder.RSCPacketBuilder;

import java.util.Collection;

public class WallObjectPositionPacketBuilder {
  private Player playerToUpdate;

  /**
   * Sets the player to update
   */
  public void setPlayer(Player p) {
    playerToUpdate = p;
  }

  public RSCPacket getPacket() {
    StatefulEntityCollection<GameObject> watchedObjects = playerToUpdate.getWatchedObjects();
    if (watchedObjects.changed()) {
      Collection<GameObject> newObjects = watchedObjects.getNewEntities();
      Collection<GameObject> knownObjets = watchedObjects.getKnownEntities();
      RSCPacketBuilder packet = new RSCPacketBuilder();
      packet.setID(95);
      for (GameObject o : knownObjets) {
        if (o.getType() != 1) {
          continue;
        }
        // We should remove ones miles away differently I think
        if (watchedObjects.isRemoving(o)) {
          byte[] offsets = DataConversions.getObjectPositionOffsets(o.getLocation(), playerToUpdate.getLocation());
          packet.addShort(60000);
          packet.addByte(offsets[0]);
          packet.addByte(offsets[1]);
          packet.addByte((byte) o.getDirection());
        }
      }
      for (GameObject o : newObjects) {
        if (o.getType() != 1) {
          continue;
        }
        byte[] offsets = DataConversions.getObjectPositionOffsets(o.getLocation(), playerToUpdate.getLocation());
        packet.addShort(o.getID());
        packet.addByte(offsets[0]);
        packet.addByte(offsets[1]);
        packet.addByte((byte) o.getDirection());
      }
      return packet.toPacket();
    }
    return null;
  }
}
