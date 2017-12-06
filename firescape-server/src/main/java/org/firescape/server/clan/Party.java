package org.firescape.server.clan;

import org.firescape.server.model.Player;
import org.firescape.server.model.World;

import java.util.ArrayList;

/**
 * @author Cody The Clan's main class.
 */

public class Party {

  public static final World world = World.getWorld();
  public int MaxPartySize = 100; // Maximum
  // size of
  // Players
  // in
  // Party.
  public Player player;
  public ArrayList<String> playersToGiveTo = new ArrayList<String>();

  public Party(Player p) {
    player = p;
  }

  public void invitePlayer(String playername) {
    try {
      Player target = world.getPlayer(playername);
      if (playername.equals(player.getUsername())) {
        player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@You cannot invite your self to a Clan");
        return;
      }
      if (player.inParty && !player.myParty.get(0).equals(player.getUsername())) {
        player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@Sorry, you are not the Clan leader.");
        return;
      }
      if (this.isPartyFull()) {
        player.getActionSender()
              .sendMessage("@cya@[@whi@Clan@cya@]@whi@You cannot fit anymore players into your" + "" + " Clan");
        return;
      }
      if (target.inParty) {
        player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@Sorry, they are already in a Clan");
        return;
      }
      if (target.invitedPlayers.contains(player.getUsername())) {
        player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@You have already invited this player");
        return;
      }
      target.getActionSender()
            .sendMessage("@cya@[@whi@Clan@cya@]@whi@" +
                         player.getUsername() +
                         " @whi@has " +
                         "Invited you to join there Clan, type ::accept to join.");
      player.getActionSender()
            .sendMessage("@cya@[@whi@Clan@cya@]@whi@You have invited @whi@" +
                         target.getUsername() +
                         " @whi@to join your Clan");
      target.lastPartyInvite = player.getUsername();
      player.invitedPlayers.add(target.getUsername());

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  public boolean isPartyFull() {
    return player.myParty.size() >= MaxPartySize;
  }

  public void acceptPlayer() {
    try {
      Player leader = world.getPlayer(player.lastPartyInvite);
      if (player.lastPartyInvite == null) {
        player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@Sorry, nobody has invited you to a Clan");
      }
      if (this.invitedPlayersContains(player.getUsername(), leader)) {
        if (leader.myParty.size() > MaxPartySize) {
          player.getActionSender()
                .sendMessage("@cya@[@whi@Clan@cya@]@whi@Sorry, the Clan you are trying to join" +
                             "" +
                             " is currently Full.");
          return;
        }
        if (!leader.inParty) {
          leader.myParty.add(leader.getUsername());
        }
        leader.myParty.add(player.getUsername());
        player.myParty = leader.myParty;
        player.inParty = true;
        leader.inParty = true;
        player.lastPartyInvite = null;
        sendPartyMessage("@cya@[@whi@Clan@cya@]@whi@" + player.getUsername() + "@whi@ has joined the Clan");
        player.getActionSender()
              .sendMessage("@cya@[@whi@Clan@cya@]@whi@Clan Members: @yel@" + this.partyMembersString());

      } else {
        player.getActionSender()
              .sendMessage("@cya@[@whi@Clan@cya@]@whi@" +
                           leader.getUsername() +
                           " has not " +
                           "invited you to join there Clan");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public boolean invitedPlayersContains(String name, Player leader) {
    try {
      for (int i = 0; i < leader.invitedPlayers.size(); i++) {
        if (leader.invitedPlayers.get(i).equals(name)) {
          return true;
        }
      }
      return false;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public void sendPartyMessage(String message) {
    try {
      for (int i = 0; i < player.myParty.size(); i++) {
        Player member = world.getPlayer(player.myParty.get(i));
        member.getActionSender().sendMessage("@cya@[@whi@Clan Chat@cya@] @whi@" + message);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public String partyMembersString() {
    try {
      if (!player.inParty) {
        return "@cya@[@whi@Clan@cya@]@whi@You are not in a Clan";
      }
      String str = player.myParty.get(0) + "@whi@(@yel@L@whi@), ";
      for (int i = 1; i < player.myParty.size(); i++) {
        str = str + player.myParty.get(i) + ", ";
      }
      return str;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "error?";
    }
  }

  public void sendPartyMessageUser(String message) {
    try {
      if (!player.inParty) {
        player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@You are not in a Clan");
        return;
      }
      for (int i = 0; i < player.myParty.size(); i++) {
        Player member = world.getPlayer(player.myParty.get(i));
        member.getActionSender()
              .sendMessage("@say@@cya@[@whi@Clan Chat@cya@] @cya@[@whi@" +
                           player.getUsername() +
                           "@cya@]@whi@: " +
                           message);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void updatePartyMembers() {
    try {
      for (int i = 0; i < player.myParty.size(); i++) {
        Player member = world.getPlayer(player.myParty.get(i));
        member.myParty = player.myParty;
        member.myParty.clear();
        member.myParty.addAll(player.myParty);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void removePlayer() {
    try {
      ArrayList<String> temparray = new ArrayList<String>();
      temparray.addAll(player.myParty);
      for (int i = 0; i < temparray.size(); i++) {
        Player member = world.getPlayer(temparray.get(i));
        if (member.myParty.get(i).isEmpty()) {
        } else {
          removePlayerParty(getIndex());
        }
        member.getActionSender()
              .sendMessage("@cya@[@whi@Clan@cya@]@whi@(@yel@" +
                           player.getUsername() +
                           "@whi@)" +
                           "" +
                           " @whi@Has left the Clan");
        if (temparray.get(i).isEmpty()) {
          break;
        }
      }
      player.clearMyParty();
      player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@You have left the Clan");

    } catch (Exception e) {
      System.out.println(e.getMessage() + " \nStack: " + e.getStackTrace().toString());
    }
  }

  public void removePlayerParty(int id) {
    player.myParty.remove(id);
  }

  public int getIndex() {
    try {
      for (int i = 0; i < player.myParty.size(); i++) {
        if (player.myParty.get(i).equals(player.getUsername())) {
          return i;
        }
      }
      return 1;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return 1;
    }
  }

  public int getPartySize() {
    return player.myParty.size() + 1;
  }

  public void partyMembersInView() // Decided to use a void for this instead,
  // to get the party members in your view,
  // its under the public ArrayList
  // playersToGiveTo
  {
    try {
      for (Player p : player.getViewArea().getPlayersInView()) {
        if (partyContains(p.getUsername())) {
          playersToGiveTo.add(p.getUsername());
        }
      }
    } catch (Exception e) {
      System.out.println("partyMembersInView : " + e.getMessage());
    }
  }

  public boolean partyContains(String name) {
    try {
      for (int i = 0; i < player.myParty.size(); i++) {
        if (player.myParty.get(i).equals(name)) {
          return true;
        }
      }
      return false;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public void summonParty() {
    if (!isPartyLeader()) {
      player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@You are not the Clan leader");
      return;
    }
    for (int i = 0; i < player.myParty.size(); i++) {
      if (player.myParty.get(i).equals(player.getUsername())) {
        player.getActionSender()
              .sendMessage("@cya@[@whi@Clan@cya@]@whi@" +
                           player.getUsername() +
                           " Has " +
                           "Wished " +
                           "To Summon The Clan");

      } else {
        Player member = world.getPlayer(player.myParty.get(i));
        member.getActionSender()
              .sendMessage("@cya@[@whi@Clan@cya@]@whi@" +
                           player.getUsername() +
                           " @whi@wishes" +
                           "" +
                           " to Summon you. Write ::go  (to be summoned)");
        member.summonLeader = player.getUsername();
      }
    }

  }

  public boolean isPartyLeader() {
    return player.myParty.get(0).equals(player.getUsername());
  }

  public void acceptSummon() {
    Player leader = world.getPlayer(player.myParty.get(0));
    if (player.inParty) {
      if (player.isSkulled()) {
        player.getActionSender()
              .sendMessage("@cya@[@whi@Clan@cya@]@whi@Your leader wanted to summon you, but he" +
                           "" +
                           " cannot when you are skulled.");
        player.summonLeader = null;
        return;
      }
      if (player.summonLeader.equals(player.myParty.get(0))) {
        player.teleport(leader.getLocation().x, leader.getLocation().y, true);
        player.summonLeader = null;
      } else {
        player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@Nobody has tried to summon you");
      }
    } else {
      player.getActionSender().sendMessage("@cya@[@whi@Clan@cya@]@whi@Sorry, you are not in a Clan");
    }
  }
}