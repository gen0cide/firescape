package org.firescape.server.packetbuilder.client;

import org.firescape.server.model.GameObject;
import org.firescape.server.model.Player;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.packetbuilder.RSCPacketBuilder;
import org.firescape.server.util.DataConversions;
import org.firescape.server.util.StatefulEntityCollection;

import java.util.Arrays;
import java.util.Collection;

public class GameObjectPositionPacketBuilder {
  private Player playerToUpdate;

  /**
   * Sets the player to update
   */
  public void setPlayer( Player p ) {
    playerToUpdate = p;
  }

  public RSCPacket getPacket() {
    StatefulEntityCollection<GameObject> watchedObjects = playerToUpdate.getWatchedObjects();
    if (watchedObjects.changed()) {
      Collection<GameObject> newObjects = watchedObjects.getNewEntities();
      Collection<GameObject> knownObjets = watchedObjects.getKnownEntities();
      RSCPacketBuilder packet = new RSCPacketBuilder();
      packet.setID(48);
      for (GameObject o : knownObjets) {
        if (o.getType() != 0) {
          continue;
        }
        // We should remove ones miles away differently I think
        if (watchedObjects.isRemoving(o)) {
          byte[] offsets = DataConversions.getObjectPositionOffsets(o.getLocation(), playerToUpdate.getLocation());
          packet.addShort(60000);
          packet.addByte(offsets[0]);
          packet.addByte(offsets[1]);
          packet.addByte((byte) o.getDirection());
          System.out.printf("SENDING REMOVE OBJECT id=%d name=%s coords=%s\n", o.getID(), o.getGameObjectDef().name,
            Arrays.toString(offsets));

        }
      }
      for (GameObject o : newObjects) {
        if (o.getType() != 0) {
          continue;
        }
        byte[] offsets = DataConversions.getObjectPositionOffsets(o.getLocation(), playerToUpdate.getLocation());
        packet.addShort(o.getID());
        packet.addByte(offsets[0]);
        packet.addByte(offsets[1]);
        packet.addByte((byte) o.getDirection());
        System.out.printf("SENDING NEW OBJECT id=%d name=%s coords=%s\n", o.getID(), o.getGameObjectDef().name,
          Arrays.toString(offsets));
      }
      return packet.toPacket();
    }
    return null;
  }
}
