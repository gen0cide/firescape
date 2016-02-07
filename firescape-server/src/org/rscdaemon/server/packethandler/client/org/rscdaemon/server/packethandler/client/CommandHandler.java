package org.rscdaemon.server.packethandler.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.mina.common.IoSession;
import org.rscdaemon.server.Server;
import org.rscdaemon.server.clan.Party;
import org.rscdaemon.server.entityhandling.EntityHandler;
import org.rscdaemon.server.event.SingleEvent;
import org.rscdaemon.server.model.InvItem;
import org.rscdaemon.server.model.Mob;
import org.rscdaemon.server.model.Npc;
import org.rscdaemon.server.model.Player;
import org.rscdaemon.server.model.World;
import org.rscdaemon.server.net.Packet;
import org.rscdaemon.server.packethandler.PacketHandler;
import org.rscdaemon.server.states.CombatState;
import org.rscdaemon.server.util.DataConversions;
import org.rscdaemon.server.util.Formulae;
import org.rscdaemon.server.util.Logger;

public class CommandHandler implements PacketHandler {
  /**
   * World instance
   */
  public static final World world = World.getWorld();

  public long lasttime;
  String lastplayer;
  public long lastmessage;

  public static String[] cussingWords = { "fuck" }; // Add words to be filtered
                                                    // here

  public void handlePacket(Packet p, IoSession session) throws Exception {
    Player player = (Player) session.getAttachment();
    if (player.isBusy()) {
      player.resetPath();
      return;
    }
    player.resetAll();
    String s = new String(p.getData()).trim();
    int firstSpace = s.indexOf(" ");
    String cmd = s;
    String[] args = new String[0];
    if (firstSpace != -1) {
      cmd = s.substring(0, firstSpace).trim();
      args = s.substring(firstSpace + 1).trim().split(" ");
    }
    try {
      handleCommand(cmd.toLowerCase(), args, player, s);
    }
    catch (Exception e) {
    }
  }

  public void handleCommand(String cmd, String[] args, Player player, String raw) throws Exception {
    // if(args[0] != null)
    // args[0] = args[0].replace("_", "");
    /**
     * Cody Clan System Commands
     */
    if (cmd.equals("c")) {
      Party party = new Party(player);
      String newStr = "";
      for (int i = 0; i < args.length; i++) {
        newStr = newStr + args[i] + " ";
      }
      party.sendPartyMessageUser(newStr);
      return;
    } else if (cmd.equals("go")) {
      Party party = new Party(player);
      party.acceptSummon();
      return;
    } else if (cmd.equals("leave")) {
      Party party = new Party(player);
      party.removePlayer();
      return;
    } else if (cmd.equals("invite")) {
      Party party = new Party(player);
      args[0] = args[0].replace("_", " ");
      party.invitePlayer(args[0]);
      return;
    } else if (cmd.equals("summonparty")) {
      Party party = new Party(player);
      party.summonParty();
      return;
    } else if (cmd.equals("clan")) {
      Party party = new Party(player);
      player.getActionSender().sendMessage("@cya@[@whi@Clan Members:@cya@] @whi@" + party.partyMembersString());
      return;
    } else if (cmd.equals("accept")) {
      Party party = new Party(player);
      party.acceptPlayer();
      return;
    } else if (cmd.equals("array")) {
      player.getActionSender().sendMessage(player.myParty.toString());
      return;

    } else if (cmd.equals("stuck")) {
      if (player.getLocation().inModRoom() && !player.isMod()) {
        player.getActionSender().sendMessage("You cannot use ::stuck here");
      } else if (player.getLocation().inWilderness()) {
        player.getActionSender().sendMessage("Cannot use stuck in wilderness");
      } else if (System.currentTimeMillis() - player.getLastMoved() < 10000
          || System.currentTimeMillis() - player.getCastTimer() < 10000) {
        player.getActionSender()
            .sendMessage("There is a 10 second delay on using ::stuck, please stand still for 10 secs");
      } else if (!player.inCombat() && System.currentTimeMillis() - player.getCombatTimer() > 10000) {
        player.setCastTimer();
        player.teleport(220, 440, true);
      } else {
        player.getActionSender().sendMessage("You cannot use ::stuck for 10 seconds after combat");
      }
      return;
    }
    if (cmd.equals("online")) {
      int playerscounter = 0;
      for (Player p : world.getPlayers()) {
        playerscounter++;
      }
      player.getActionSender().sendMessage("@cya@[@red@FireScape@cya@] @whi@There are currently @or1@" + playerscounter
          + " @whi@players playing FireScape.");
      return;
    }
    if (cmd.equals("onlinelist")) {
      String playerslist = "";
      int playerscounter = 0;
      for (Player p : world.getPlayers()) {
        if (p.isAdmin()) {
          playerslist = "#adm# @yel@" + p.getUsername() + ", " + playerslist;
        } else if (p.isMod()) {
          playerslist = "#mod# @gry@" + p.getUsername() + ", " + playerslist;
        } else if (p.isPMod()) {
          playerslist = "#pmd# @gre@" + p.getUsername() + ", " + playerslist;
        } else if (p.isEvent()) {
          playerslist = "#evt# @blu@" + p.getUsername() + ", " + playerslist;
        } else if (p.isMuted()) {
          playerslist = "@red@[m] @whi@" + p.getUsername() + ", " + playerslist;
        } else {
          playerslist = "@whi@" + p.getUsername() + ", " + playerslist;
        }
        playerscounter++;
      }
      player.getActionSender().sendAlert("There are @or1@" + playerscounter + " @whi@players online: " + playerslist,
          true);
      return;
    }
    if (cmd.equals("wipebank")) {
      player.getBank().getItems().clear();
      player.getActionSender().sendMessage("@cya@[@red@FireScape@cya@] @whi@Your bank has been cleared.");
      return;
    }
    if (cmd.equals("skull")) {
      player.addSkull(1200000);
      player.getActionSender().sendMessage("@cya@[@red@FireScape@cya@] @whi@You are now skulled for 20 minutes!");
      return;
    }
    if (cmd.equals("fatigue")) {
      player.setFatigue(100);
      player.getActionSender().sendFatigue();
      player.getActionSender().sendMessage("@cya@[@red@FireScape@cya@] @whi@Your fatigue has been set to 100%");
      return;
    }
    if (cmd.equals("spree")) {
      long PlayerHash = DataConversions.usernameToHash(args[0]);
      Player p = world.getPlayer(PlayerHash);
      if (p != null) {
        player.getActionSender()
            .sendMessage("[@red@FireScape@whi@] " + p.getUsername() + "'s killing spree is: " + p.getKillingSpree());
      } else {
        player.getActionSender().sendMessage("[@red@FireScape@whi@] Invalid player. Please make sure they are online!");
      }
      return;
    }
    if (cmd.equals("wild")) {
      int wildcount = 0;
      for (Player p : world.getPlayers()) {
        if (p.getLocation().inWilderness()) {
          wildcount++;
        }
      }
      if (wildcount == 0) {
        player.getActionSender().sendMessage(
            "[@red@FireScape@whi@] There is currently @or1@" + wildcount + " @whi@players in the wilderness.");
        return;
      }
      if (wildcount == 1) {
        player.getActionSender().sendMessage(
            "[@red@FireScape@whi@] There is currently @or1@" + wildcount + " @whi@player in the wilderness.");
        return;
      } else {
        player.getActionSender().sendMessage(
            "[@red@FireScape@whi@] There is currently @or1@" + wildcount + " @whi@players in the wilderness.");
        return;
      }
    }
    if (cmd.equals("say")) {
      boolean waittime = false;
      if (lasttime == 0L) {
        lasttime = System.currentTimeMillis();
      }
      ArrayList informOfChatMessage = new ArrayList();
      Player p;
      for (Iterator i$ = world.getPlayers().iterator(); i$.hasNext(); informOfChatMessage.add(p)) {
        p = (Player) i$.next();
      }
      String newStr = "@gre@";
      for (int i = 0; i < args.length; i++) {
        newStr = (new StringBuilder()).append(newStr).append(args[i]).append(" ").toString();
      }
      for (int yong = 0; yong < cussingWords.length; yong++) {
        String filter = "";
        for (int min = 0; min < cussingWords[yong].length(); min++) {
          filter += "*";
        }
        newStr = newStr.replaceAll("(?i)" + cussingWords[yong], filter);
      }
      if (player.isMuted()) {
        player.getActionSender().sendMessage("[@red@FireScape@whi@] You can't use this command while you're muted!");
        return;
      }
      if (player.isAdmin()) {
        newStr = (new StringBuilder()).append("@say@@adm@").append("@yel@     " + player.getUsername()).append(": ")
            .append(newStr).toString();
      } else if (player.isMod()) {
        newStr = (new StringBuilder()).append("@say@@mod@").append("@gry@     " + player.getUsername()).append(": ")
            .append(newStr).toString();
      } else if (player.isPMod()) {
        newStr = (new StringBuilder()).append("@say@@pmd@").append("@gre@     " + player.getUsername()).append(": ")
            .append(newStr).toString();
      } else if (player.isEvent()) {
        newStr = (new StringBuilder()).append("@say@@evt@").append("@blu@     " + player.getUsername()).append(": ")
            .append(newStr).toString();
      } else if (System.currentTimeMillis() - lasttime > 20000L || lastplayer != player.getUsername()) {
        newStr = (new StringBuilder()).append("@say@@whi@[Member] ").append(player.getUsername()).append(": ")
            .append(newStr).toString();
      } else {
        long timeremaining = 10L - (System.currentTimeMillis() - lasttime) / 1000L;
        player.getActionSender().sendMessage((new StringBuilder()).append("You need to wait ").append(timeremaining)
            .append(" seconds before using ::say again.").toString());
        waittime = true;
      }
      if (!waittime) {
        lasttime = System.currentTimeMillis();
        lastplayer = player.getUsername();
        Player pl;
        for (Iterator i$ = informOfChatMessage.iterator(); i$.hasNext(); pl.getActionSender().sendMessage(newStr)) {
          pl = (Player) i$.next();
        }

      }
      return;
    }

    if (!player.isEvent()) {
      return;
    }
    if (cmd.equals("modroom")) {
      Logger.mod(player.getUsername() + " teleported to the mod room.");
      player.teleport(70, 1640, true);
      return;
    }
    if (cmd.equals("global")) {
      String globalMsg = "";
      for (int i = 0; i < args.length; i++) {
        globalMsg = globalMsg + args[i] + " ";
      }
      for (Player p : world.getPlayers()) {
        p.getActionSender().sendAlert("#evt#" + player.getUsername() + ": " + globalMsg, false);
      }
      return;
    }

    if (!player.isPMod()) {
      return;
    }
    if (cmd.equals("modroom")) {
      Logger.mod(player.getUsername() + " teleported to the mod room.");
      player.teleport(70, 1640, true);
      return;
    }
    if (cmd.equals("global")) {
      String globalMsg = "";
      for (int i = 0; i < args.length; i++) {
        globalMsg = globalMsg + args[i] + " ";
      }
      for (Player p : world.getPlayers()) {
        p.getActionSender().sendAlert("#pmd#" + player.getUsername() + ": " + globalMsg, false);
      }
      return;
    }
    if (cmd.equals("goto") || cmd.equals("summon")) {
      boolean summon = cmd.equals("summon");
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: " + (summon ? "SUMMON" : "GOTO") + " name");
        return;
      }
      long usernameHash = DataConversions.usernameToHash(args[0]);
      Player affectedPlayer = world.getPlayer(usernameHash);
      if (affectedPlayer != null) {
        if (summon) {
          Logger.mod(player.getUsername() + " summoned " + affectedPlayer.getUsername() + " from "
              + affectedPlayer.getLocation().toString() + " to " + player.getLocation().toString());
          affectedPlayer.teleport(player.getX(), player.getY(), true);
          affectedPlayer.getActionSender().sendMessage("You have been summoned by " + player.getUsername());
        } else {
          Logger.mod(player.getUsername() + " went from " + player.getLocation() + " to " + affectedPlayer.getUsername()
              + " at " + affectedPlayer.getLocation().toString());
          player.teleport(affectedPlayer.getX(), affectedPlayer.getY(), true);
        }
      } else {
        player.getActionSender().sendMessage("Invalid player, maybe they aren't currently on this server?");
      }
      return;
    }
    if (cmd.equals("take") || cmd.equals("put")) {
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: TAKE name");
        return;
      }
      Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
      if (affectedPlayer == null) {
        player.getActionSender().sendMessage("Invalid player, maybe they aren't currently online?");
        return;
      }
      Logger.mod(player.getUsername() + " took " + affectedPlayer.getUsername() + " from "
          + affectedPlayer.getLocation().toString() + " to admin room");
      affectedPlayer.teleport(78, 1642, true);
      if (cmd.equals("take")) {
        player.teleport(76, 1642, true);
      }
      return;
    }

    if (!player.isMod()) {
      return;
    }
    if (cmd.equals("modroom")) {
      Logger.mod(player.getUsername() + " teleported to the mod room.");
      player.teleport(70, 1640, true);
      return;
    }
    if (cmd.equals("global")) {
      String globalMsg = "";
      for (int i = 0; i < args.length; i++) {
        globalMsg = globalMsg + args[i] + " ";
      }
      for (Player p : world.getPlayers()) {
        p.getActionSender().sendAlert("#mod#" + player.getUsername() + ": " + globalMsg, false);
      }
      return;
    }
    if (cmd.equals("goto") || cmd.equals("summon")) {
      boolean summon = cmd.equals("summon");
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: " + (summon ? "SUMMON" : "GOTO") + " name");
        return;
      }
      long usernameHash = DataConversions.usernameToHash(args[0]);
      Player affectedPlayer = world.getPlayer(usernameHash);
      if (affectedPlayer != null) {
        if (summon) {
          Logger.mod(player.getUsername() + " summoned " + affectedPlayer.getUsername() + " from "
              + affectedPlayer.getLocation().toString() + " to " + player.getLocation().toString());
          affectedPlayer.teleport(player.getX(), player.getY(), true);
          affectedPlayer.getActionSender().sendMessage("You have been summoned by " + player.getUsername());
        } else {
          Logger.mod(player.getUsername() + " went from " + player.getLocation() + " to " + affectedPlayer.getUsername()
              + " at " + affectedPlayer.getLocation().toString());
          player.teleport(affectedPlayer.getX(), affectedPlayer.getY(), true);
        }
      } else {
        player.getActionSender().sendMessage("Invalid player, maybe they aren't currently on this server?");
      }
      return;
    }
    if (cmd.equals("take") || cmd.equals("put")) {
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: TAKE name");
        return;
      }
      Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
      if (affectedPlayer == null) {
        player.getActionSender().sendMessage("Invalid player, maybe they aren't currently online?");
        return;
      }
      Logger.mod(player.getUsername() + " took " + affectedPlayer.getUsername() + " from "
          + affectedPlayer.getLocation().toString() + " to admin room");
      affectedPlayer.teleport(78, 1642, true);
      if (cmd.equals("take")) {
        player.teleport(76, 1642, true);
      }
      return;
    }
    if (cmd.equals("kick")) {
      long PlayerHash = DataConversions.usernameToHash(args[0]);
      Player p = world.getPlayer(PlayerHash);
      if (p != null) {
        p.getActionSender().sendLogout();
        p.destroy(true);
        player.getActionSender().sendMessage(p.getUsername() + " has been kicked from the server.");
        Logger.mod(player.getUsername() + " kicked " + p.getUsername() + " from the server.");
      } else {
        player.getActionSender().sendMessage("Invalid username or the player is currently offline.");
      }
      return;
    }
    if (cmd.equals("teleport")) {
      if (args.length != 2) {
        player.getActionSender().sendMessage("Invalid args. Syntax: TELEPORT x y");
        return;
      }
      int x = Integer.parseInt(args[0]);
      int y = Integer.parseInt(args[1]);
      if (world.withinWorld(x, y)) {
        Logger.mod(player.getUsername() + " teleported from " + player.getLocation().toString() + " to (" + x + ", " + y
            + ")");
        player.teleport(x, y, true);
      } else {
        player.getActionSender().sendMessage("Invalid coordinates!");
      }
      return;
    }
    if (cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("unban")) {
      boolean banned = cmd.equalsIgnoreCase("ban");
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: " + (banned ? "BAN" : "UNBAN") + " name");
        return;
      }
      if (banned) {
        if (Integer.valueOf(Server.readValue(args[0], "rank")) == 6) {
          player.getActionSender().sendMessage("Target is already banned");
          return;
        } else {
          world.banPlayer(args[0]);
          Logger.mod(player.getUsername() + " has banned " + args[0]);
        }
      } else {
        if (Integer.valueOf(Server.readValue(args[0], "rank")) == 6) {
          world.unbanPlayer(args[0]);
          Logger.mod(player.getUsername() + " has unbanned " + args[0]);
        } else {
          player.getActionSender().sendMessage("Target is not banned");
        }
      }
      return;
    }

    if (!player.isAdmin()) {
      return;
    }
    if (cmd.equals("shutdown")) {
      Logger.mod(player.getUsername() + " shut down the server!");
      world.getServer().kill();
      return;
    }
    if (cmd.equals("update")) {
      String reason = "";
      if (args.length > 0) {
        for (String s : args) {
          reason += (s + " ");
        }
        reason = reason.substring(0, reason.length() - 1);
      }
      if (world.getServer().shutdownForUpdate()) {
        Logger.mod(player.getUsername() + " updated the server: " + reason);
        for (Player p : world.getPlayers()) {
          p.getActionSender().sendAlert("The server will be shutting down in 60 seconds: " + reason, false);
          p.getActionSender().startShutdown(60);
        }
      }
      return;
    }
    if (cmd.equals("appearance")) {
      player.setChangingAppearance(true);
      player.getActionSender().sendAppearanceScreen();
      return;
    }
    if (cmd.equals("invisible")) {
      player.goInvisible();
    }
    if (cmd.equals("promote")) {
      long PlayerHash = DataConversions.usernameToHash(args[0]);
      Player p = world.getPlayer(PlayerHash);
      Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
      int rank = Integer.parseInt(args[1]);
      if (affectedPlayer == null) {
        player.getActionSender().sendMessage("Invalid player or maybe they aren't online?");
        return;
      }
      if (rank == 4) { // Administrator
        world.promoteAdmin(args[0]);
        p.getActionSender().sendMessage(player.getUsername() + " has promoted you to @yel@Administrator.");
        p.getActionSender().sendMessage("Please logout and back in for the effects to take place.");
        player.getActionSender()
            .sendMessage("You have promoted " + affectedPlayer.getUsername() + " to @yel@Administrator.");
      } else if (rank == 3) { // Moderator
        world.promoteMod(args[0]);
        p.getActionSender().sendMessage(player.getUsername() + " has promoted you to @gry@Moderator.");
        p.getActionSender().sendMessage("Please logout and back in for the effects to take place.");
        player.getActionSender()
            .sendMessage("You have promoted " + affectedPlayer.getUsername() + " to @gry@Moderator.");
      } else if (rank == 2) { // Player Moderator
        world.promotePMod(args[0]);
        p.getActionSender().sendMessage(player.getUsername() + " has promoted you to @gre@Player Moderator.");
        p.getActionSender().sendMessage("Please logout and back in for the effects to take place.");
        player.getActionSender()
            .sendMessage("You have promoted " + affectedPlayer.getUsername() + " to @gre@Player Moderator.");
      } else if (rank == 7) { // Event Staff
        world.promoteEvent(args[0]);
        p.getActionSender().sendMessage(player.getUsername() + " has promoted you to @blu@Event Staff.");
        p.getActionSender().sendMessage("Please logout and back in for the effects to take place.");
        player.getActionSender()
            .sendMessage("You have promoted " + affectedPlayer.getUsername() + " to @blu@Event Staff.");
      }
      return;
    }
    if (cmd.equals("item")) {
      if (args.length < 1 || args.length > 2) {
        player.getActionSender().sendMessage("Invalid args. Syntax: ITEM id [amount]");
        return;
      }
      int id = Integer.parseInt(args[0]);
      if (EntityHandler.getItemDef(id) != null) {
        int amount = 1;
        if (args.length == 2 && EntityHandler.getItemDef(id).isStackable()) {
          amount = Integer.parseInt(args[1]);
        }
        InvItem item = new InvItem(id, amount);
        player.getInventory().add(item);
        player.getActionSender().sendInventory();
        Logger.mod(player.getUsername() + " spawned themself " + amount + " " + item.getDef().getName() + "(s)");
      } else {
        player.getActionSender().sendMessage("Invalid id");
      }
      return;
    }
    if (cmd.equals("reset")) {
      long PlayerHash = DataConversions.usernameToHash(args[0]);
      Player p = world.getPlayer(PlayerHash);
      int stat = Formulae.getStatIndex(args[1]);
      int level = Integer.parseInt(args[2]);

      if (level < 1 || level > 40) {
        player.getActionSender().sendMessage("Invalid " + Formulae.statArray[stat] + " level.");
        return;
      }
      if (p.getMaxStat(stat) > 39) {
        player.getActionSender()
            .sendMessage("That player's " + Formulae.statArray[stat] + " level is too high to be reset.");
        return;
      }
      if (stat > 6) {
        player.getActionSender().sendMessage("You can only change another players combat stats.");
        return;
      }
      if (stat == 3) {
        player.getActionSender().sendMessage("You can not alter another players hits level.");
        return;
      }
      p.setCurStat(stat, level);
      p.setMaxStat(stat, level);
      p.setExp(stat, Formulae.lvlToXp(level));

      if (stat == 0 || stat == 1 || stat == 3) {
        int hitpointsexp = Formulae.lvlToXp(p.getMaxStat(0)) + Formulae.lvlToXp(p.getMaxStat(1))
            + Formulae.lvlToXp(p.getMaxStat(2)) + 1154;
        int hitpointslevel = Formulae.experienceToLevel(hitpointsexp / 3);
        if (hitpointslevel < 10) {
          hitpointslevel = 10;
          p.setCurStat(3, 10);
          p.setMaxStat(3, 10);
          p.setExp(3, 1155);
        } else {
          p.setCurStat(3, hitpointslevel);
          p.setMaxStat(3, hitpointslevel);
          p.setExp(3, Formulae.lvlToXp(hitpointslevel));
        }
      }

      int combat = Formulae.getCombatlevel(p.getMaxStats());
      if (combat != p.getCombatLevel()) {
        p.setCombatLevel(combat);
      }
      p.getActionSender().sendStats();
      if (p.getUsername() == player.getUsername()) {
        player.getActionSender()
            .sendMessage("You have updated your " + Formulae.statArray[stat] + " to level " + args[2] + ".");
      } else {
        p.getActionSender().sendMessage(
            player.getUsername() + " has updated your " + Formulae.statArray[stat] + " to level " + args[2] + ".");
        player.getActionSender().sendMessage(
            "You have updated " + p.getUsername() + "'s " + Formulae.statArray[stat] + " to level " + args[2] + ".");
      }
      return;
    }
    if (cmd.equals("setstat")) {
      if (args.length < 2) {
        player.getActionSender().sendMessage("INVALID USE - EXAMPLE setstat attack 99");
        return;
      }

      int stat = Formulae.getStatIndex(args[0]);
      int lvl = Integer.parseInt(args[1]);

      if (lvl < 0 || lvl > 99) {
        player.getActionSender().sendMessage("Invalid " + Formulae.statArray[stat] + " level.");
        return;
      }

      player.setCurStat(stat, lvl);
      player.setMaxStat(stat, lvl);
      player.setExp(stat, Formulae.lvlToXp(lvl));

      if (stat == 0 || stat == 1 || stat == 2) {
        int hitpointsXp = Formulae.lvlToXp(player.getMaxStat(0)) + Formulae.lvlToXp(player.getMaxStat(1))
            + Formulae.lvlToXp(player.getMaxStat(2)) + 1154;
        int hitpointsLVL = Formulae.experienceToLevel(hitpointsXp / 3);
        if (hitpointsLVL < 10) {
          hitpointsLVL = 10;
          player.setCurStat(3, 10);
          player.setMaxStat(3, 10);
          player.setExp(3, 1155);
        } else {
          player.setCurStat(3, hitpointsLVL);
          player.setMaxStat(3, hitpointsLVL);
          player.setExp(3, Formulae.lvlToXp(hitpointsLVL));
        }
      }

      int comb = Formulae.getCombatlevel(player.getMaxStats());
      if (comb != player.getCombatLevel())
        player.setCombatLevel(comb);

      player.getActionSender().sendInventory();
      player.getActionSender().sendStats();
      player.getActionSender().sendMessage("Your " + Formulae.statArray[stat] + " has been set to level " + lvl);
    }
    if (cmd.equals("npc")) {
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: NPC id");
        return;
      }
      int id = Integer.parseInt(args[0]);
      if (EntityHandler.getNpcDef(id) != null) {
        final Npc n = new Npc(id, player.getX(), player.getY(), player.getX() - 2, player.getX() + 2, player.getY() - 2,
            player.getY() + 2);
        n.setRespawn(false);
        world.registerNpc(n);
        world.getDelayedEventHandler().add(new SingleEvent(null, 60000) {
          public void action() {
            Mob opponent = n.getOpponent();
            if (opponent != null) {
              opponent.resetCombat(CombatState.ERROR);
            }
            n.resetCombat(CombatState.ERROR);
            world.unregisterNpc(n);
            n.remove();
          }
        });
        Logger.mod(
            player.getUsername() + " spawned a " + n.getDef().getName() + " at " + player.getLocation().toString());
      } else {
        player.getActionSender().sendMessage("Invalid id");
      }
      return;
    }
    if (cmd.equals("mute")) {
      boolean mute = cmd.equalsIgnoreCase("mute");
      if (args.length != 1) {
        player.getActionSender().sendMessage("[@red@FireScape@whi@] Invalid args. Syntax: MUTE name!");
        return;
      }
      if (mute) {
        if (Integer.valueOf(Server.readValue(args[0], "mute")) == 1) {
          player.getActionSender().sendMessage("[@red@FireScape@whi@] That player is already @red@muted@whi@!");
          return;
        } else {
          world.mutePlayer(args[0]);
          Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
          player.getActionSender()
              .sendMessage("[@red@FireScape@whi@] You have @red@muted @whi@" + affectedPlayer.getUsername() + "!");
          affectedPlayer.getActionSender()
              .sendMessage("[@red@FireScape@whi@] You have been @red@muted @whi@by: " + player.getUsername());
          affectedPlayer.getActionSender().sendLogout();
          affectedPlayer.destroy(true);
        }
        Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
        if (affectedPlayer == null) {
          player.getActionSender()
              .sendMessage("[@red@FireScape@whi@] Invalid player. Please make sure they are online!");
          return;
        }
        Logger.mod(player.getUsername() + " muted " + affectedPlayer.getUsername() + "");
        affectedPlayer.getActionSender().sendMute();
        return;
      }
    }
    if (cmd.equals("unmute")) {
      boolean unmute = cmd.equalsIgnoreCase("unmute");
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. SYNTAX: UNMUTE name");
        return;
      }
      if (unmute) {
        if (Integer.valueOf(Server.readValue(args[0], "mute")) == 0) {
          player.getActionSender().sendMessage("[@red@FireScape@whi@] That player is not even muted!");
          return;
        } else {
          if (Integer.valueOf(Server.readValue(args[0], "mute")) == 1) {
            world.unMutePlayer(args[0]);
            Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
            player.getActionSender()
                .sendMessage("[@red@FireScape@whi@] You have @gre@unmuted @whi@" + affectedPlayer.getUsername() + "");
            affectedPlayer.getActionSender().sendMessage("[@red@FireScape@whi@] You have been @gre@unmuted@whi@!");
            affectedPlayer.getActionSender().sendLogout();
            affectedPlayer.destroy(true);
          }
          Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
          if (affectedPlayer == null) {
            player.getActionSender()
                .sendMessage("[@red@FireScape@whi@] Invalid player. Please make sure they are online!");
            return;
          }
          Logger.mod(player.getUsername() + " unmuted " + affectedPlayer.getUsername() + "");
          affectedPlayer.getActionSender().sendMute();
          return;
        }
      }
    }
    if (cmd.equals("kick")) {
      long PlayerHash = DataConversions.usernameToHash(args[0]);
      Player p = world.getPlayer(PlayerHash);
      if (p != null) {
        p.getActionSender().sendLogout();
        p.destroy(true);
        player.getActionSender().sendMessage(p.getUsername() + " has been kicked from the server.");
        Logger.mod(player.getUsername() + " kicked " + p.getUsername() + " from the server.");
      } else {
        player.getActionSender().sendMessage("Invalid username or the player is currently offline.");
      }
      return;
    }
    if (cmd.equals("ipban")) {
      String ipban = raw.substring(6);
      long usernameHash = DataConversions.usernameToHash(raw.substring(6));
      Player affectedPlayer = world.getPlayer(usernameHash);
      System.out.println("IP JUST BANNED: " + affectedPlayer.getCurrentIP());
      player.getActionSender().sendMessage(
          "Successfully banned " + affectedPlayer.getUsername() + "'s IP:  " + affectedPlayer.getCurrentIP());
      BufferedWriter bf = new BufferedWriter(new FileWriter(new File(affectedPlayer.getCurrentIP() + ".bat")));
      bf.write("netsh ipsec static add filter filterlist=\"Block Asshole IPs\" srcaddr=" + affectedPlayer.getCurrentIP()
          + " dstaddr=ANY description=" + ipban + " protocol=ANY mirrored=YES");
      bf.newLine();
      bf.write("exit");
      bf.close();

      try {
        Runtime.getRuntime().exec(affectedPlayer.getCurrentIP() + ".bat");
      }
      catch (Exception err) {
        System.out.println(err);
      }
    }
    if (cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("unban")) {
      boolean banned = cmd.equalsIgnoreCase("ban");
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: " + (banned ? "BAN" : "UNBAN") + " name");
        return;
      }
      if (banned) {
        if (Integer.valueOf(Server.readValue(args[0], "rank")) == 6) {
          player.getActionSender().sendMessage("Target is already banned");
          return;
        } else {
          world.banPlayer(args[0]);
          Logger.mod(player.getUsername() + " has banned " + args[0]);
        }
      } else {
        if (Integer.valueOf(Server.readValue(args[0], "rank")) == 6) {
          world.unbanPlayer(args[0]);
          Logger.mod(player.getUsername() + " has unbanned " + args[0]);
        } else {
          player.getActionSender().sendMessage("Target is not banned");
        }
      }
      return;
    }
    if (cmd.equals("teleport")) {
      if (args.length != 2) {
        player.getActionSender().sendMessage("Invalid args. Syntax: TELEPORT x y");
        return;
      }
      int x = Integer.parseInt(args[0]);
      int y = Integer.parseInt(args[1]);
      if (world.withinWorld(x, y)) {
        Logger.mod(player.getUsername() + " teleported from " + player.getLocation().toString() + " to (" + x + ", " + y
            + ")");
        player.teleport(x, y, true);
      } else {
        player.getActionSender().sendMessage("Invalid coordinates!");
      }
      return;
    }
    if (cmd.equals("goto") || cmd.equals("summon")) {
      boolean summon = cmd.equals("summon");
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: " + (summon ? "SUMMON" : "GOTO") + " name");
        return;
      }
      long usernameHash = DataConversions.usernameToHash(args[0]);
      Player affectedPlayer = world.getPlayer(usernameHash);
      if (affectedPlayer != null) {
        if (summon) {
          Logger.mod(player.getUsername() + " summoned " + affectedPlayer.getUsername() + " from "
              + affectedPlayer.getLocation().toString() + " to " + player.getLocation().toString());
          affectedPlayer.teleport(player.getX(), player.getY(), true);
          affectedPlayer.getActionSender().sendMessage("You have been summoned by " + player.getUsername());
        } else {
          Logger.mod(player.getUsername() + " went from " + player.getLocation() + " to " + affectedPlayer.getUsername()
              + " at " + affectedPlayer.getLocation().toString());
          player.teleport(affectedPlayer.getX(), affectedPlayer.getY(), true);
        }
      } else {
        player.getActionSender().sendMessage("Invalid player, maybe they aren't currently on this server?");
      }
      return;
    }
    if (cmd.equals("take") || cmd.equals("put")) {
      if (args.length != 1) {
        player.getActionSender().sendMessage("Invalid args. Syntax: TAKE name");
        return;
      }
      Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
      if (affectedPlayer == null) {
        player.getActionSender().sendMessage("Invalid player, maybe they aren't currently online?");
        return;
      }
      Logger.mod(player.getUsername() + " took " + affectedPlayer.getUsername() + " from "
          + affectedPlayer.getLocation().toString() + " to admin room");
      affectedPlayer.teleport(78, 1642, true);
      if (cmd.equals("take")) {
        player.teleport(76, 1642, true);
      }
      return;
    }
    if (cmd.equals("global")) {
      String globalMsg = "";
      for (int i = 0; i < args.length; i++) {
        globalMsg = globalMsg + args[i] + " ";
      }
      for (Player p : world.getPlayers()) {
        p.getActionSender().sendAlert("#adm#" + player.getUsername() + ": " + globalMsg, false);
      }
      return;
    }
    if (cmd.equals("dropall")) {
      player.getInventory().getItems().clear();
      player.getActionSender().sendInventory();
    }
  }

}