package org.rscdaemon.bot.net;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.rscdaemon.bot.GameConnector;
import org.rscdaemon.bot.util.Logger;

public class RSCConnectionHandler implements IoHandler {

  public GameConnector gameConnector = null;

  public PacketQueue<RSCPacket> incomingPackets;
  public PacketQueue<RSCPacket> outgoingPackets;

  public RSCConnectionHandler(GameConnector gc) {
    gameConnector = gc;
    incomingPackets = gc.incomingPacketQueue;
    outgoingPackets = gc.outgoingPacketQueue;
  }

  @Override
  public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void messageReceived(IoSession session, Object message) throws Exception {
    if (session.isClosing()) {
      Logger.net("Message received, but session is closing!");
      return;
    }
    RSCPacket p = (RSCPacket) message;
    incomingPackets.add(p);
  }

  @Override
  public void messageSent(IoSession session, Object message) throws Exception {
    Logger.net("Message sent: " + message.toString());

  }

  @Override
  public void sessionClosed(IoSession session) throws Exception {
    Logger.net("Session closed!");

  }

  @Override
  public void sessionCreated(IoSession session) throws Exception {
    Logger.net("Session created!");

  }

  @Override
  public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    Logger.net("Session idle.");

  }

  @Override
  public void sessionOpened(IoSession session) throws Exception {
    // TODO Auto-generated method stub
    Logger.net("Session opened!");

  }

}
