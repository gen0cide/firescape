package org.firescape.server.packetbuilder.client;

import org.firescape.server.model.Player;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.packetbuilder.RSCPacketBuilder;
import org.firescape.server.util.DataConversions;
import org.firescape.server.util.StatefulEntityCollection;

import java.util.Collection;

public class PlayerPositionPacketBuilder {
  private Player playerToUpdate;

  /**
   * Sets the player to update
   */
  public void setPlayer(Player p) {
    playerToUpdate = p;
  }

  public RSCPacket getPacket() {
    StatefulEntityCollection<Player> watchedPlayers = playerToUpdate.getWatchedPlayers();
    Collection<Player> newPlayers = watchedPlayers.getNewEntities();
    Collection<Player> knownPlayers = watchedPlayers.getKnownEntities();
    RSCPacketBuilder packet = new RSCPacketBuilder();
    packet.setID(191);
    packet.addBits(playerToUpdate.getX(), 11);
    packet.addBits(playerToUpdate.getY(), 13);
    packet.addBits(playerToUpdate.getSprite(), 4);
    packet.addBits(knownPlayers.size(), 8);
    for (Player p : knownPlayers) {
      if (playerToUpdate.getIndex() != p.getIndex()) {
        packet.addBits(p.getIndex(), 16);
        if (watchedPlayers.isRemoving(p)) {
          packet.addBits(1, 1);
          packet.addBits(1, 1);
          packet.addBits(12, 4);
        } else if (p.hasMoved()) {
          packet.addBits(1, 1);
          packet.addBits(0, 1);
          packet.addBits(p.getSprite(), 3);
        } else if (p.spriteChanged()) {
          packet.addBits(1, 1);
          packet.addBits(1, 1);
          packet.addBits(p.getSprite(), 4);
        } else {
          packet.addBits(0, 1);
        }
      }
    }
    for (Player p : newPlayers) {
      byte[] offsets = DataConversions.getMobPositionOffsets(p.getLocation(), playerToUpdate.getLocation());
      packet.addBits(p.getIndex(), 16);
      packet.addBits(offsets[0], 5);
      packet.addBits(offsets[1], 5);
      packet.addBits(p.getSprite(), 4);
      packet.addBits(0, 1);
    }
    return packet.toPacket();
  }
}
