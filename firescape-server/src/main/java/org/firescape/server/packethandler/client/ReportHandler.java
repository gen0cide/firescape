package org.firescape.server.packethandler.client;
        /*
         * package org.firescape.server.packethandler.client; import
         * org.firescape.server.packethandler.PacketHandler; import
         * org.firescape.server.model.*; import org.firescape.server.net.Packet; import
         * org.apache.mina.common.IoSession; public class ReportHandler implements
         * PacketHandler { public static final World world = World.getWorld(); public
         * void handlePacket(Packet p, IoSession session) throws Exception { Player
         * player = (Player)session.getAttachment(); if(!player.canReport()) {
         * player.getActionSender().sendMessage(
         * "You may only send one abuse report per minute."); return; }
         * world.getServer().getLoginConnector().getActionSender().reportUser(player.
         * getUsernameHash(), p.readLong(), p.readByte()); player.setLastReport();
         * player.getActionSender().sendMessage(
         * "Your report has been received, thank you."); } }
         */