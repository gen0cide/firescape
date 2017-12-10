package org.firescape.server.packethandler.client;

import org.apache.mina.common.IoSession;
import org.firescape.server.model.Player;
import org.firescape.server.model.World;
import org.firescape.server.net.Packet;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.opcode.Command;
import org.firescape.server.opcode.Opcode;
import org.firescape.server.packethandler.PacketHandler;
import org.firescape.server.util.DataConversions;

public class FriendHandler implements PacketHandler {

  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    int pID = ((RSCPacket) p).getID();
    long f = p.readLong();
    boolean isOnline = world.getPlayers().contains(world.getPlayer(f));
    String friend = DataConversions.hashToUsername(f);
    if (pID == Opcode.getClient(204, Command.Client.CL_FRIEND_ADD)) { // Add friend
      if (player.friendCount() >= 50) {
        player.getActionSender().sendMessage("Your friend list is too full");
        return;
      }
      for (int i = 0; i < player.friendCount(); i++) {
        String s = player.getFriendList().get(i);
        if (s.equals(friend)) {
          player.getActionSender().sendMessage("This person is already in your friends list");
          return;
        }
      }
      if (isOnline) {
        player.getActionSender().sendFriendUpdate(f, 1);
      } else {
        player.getActionSender().sendFriendUpdate(f, 0);
      }
      player.addFriend(friend);
    } else if (pID == Opcode.getClient(204, Command.Client.CL_FRIEND_REMOVE)) { // Remove friend
      player.removeFriend(friend);
    } else if (pID == Opcode.getClient(204, Command.Client.CL_IGNORE_ADD)) { // Add ignore
      if (player.ignoreCount() >= 50) {
        player.getActionSender().sendMessage("Your ignore list is too full");
        return;
      }
      player.addIgnore(friend);
    } else if (pID == Opcode.getClient(204, Command.Client.CL_IGNORE_REMOVE)) { // Remove ignore
      player.removeIgnore(friend);
    } else if (pID == Opcode.getClient(204, Command.Client.CL_PM)) { // Send PM
      Player pa = world.getPlayer(f);
      byte[] remaining = p.getRemainingData();
      if (player.isMuted() && pa.isAdmin() || pa.isMod() || pa.isPMod()) {
        pa.getActionSender().sendPrivateMessage(player.getUsernameHash(), remaining);
      } else if (player.isAdmin() || player.isMod() || player.isPMod() && pa.isMuted()) {
        pa.getActionSender().sendPrivateMessage(player.getUsernameHash(), remaining);
      } else if (pa.isMuted()) {
        player.getActionSender().sendMessage("You can't communicate with a @red@MUTED@whi@ player.");
      } else if (player.isMuted()) {
        player.getActionSender().sendMessage("You can't communicate with anybody while @red@MUTED@whi@.");
        return;
      }
      if (player.getFriendList().contains(friend) && !player.getIgnoreList().contains(friend) && isOnline) {
        Player pe = world.getPlayer(f);
        pa.getActionSender().sendPrivateMessage(player.getUsernameHash(), remaining);
      } else {
        player.getActionSender().sendMessage("the target is not online.");
      }
    }
  }

}
