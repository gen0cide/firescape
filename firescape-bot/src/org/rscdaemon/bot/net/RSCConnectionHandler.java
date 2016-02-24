package org.rscdaemon.bot.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.rscdaemon.bot.GameConnector;
import org.rscdaemon.bot.codec.RSCCodecFactory;
import org.rscdaemon.bot.util.Logger;

public class RSCConnectionHandler implements IoHandler {

  public GameConnector gc = null;

  public RSCConnectionHandler(GameConnector gc) {
    this.gc = gc;
  }

  @Override
  public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    System.out.println("Exception in session!");
    cause.printStackTrace();
  }

  @Override
  public void messageReceived(IoSession session, Object message) throws Exception {
    if (session.isClosing()) {
      Logger.net("Message received, but session is closing!");
      return;
    }
    RSCPacket p = (RSCPacket) message;
    if (p == null) {
      return;
    } else {
      gc.incomingPacketQueue.add(p);
    }
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
    session.getFilterChain().addFirst("protocolFilter", new ProtocolCodecFilter(new RSCCodecFactory()));
    Logger.net("Session created!");
  }

  @Override
  public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    Logger.net("Session idle.");
  }

  @Override
  public void sessionOpened(IoSession session) throws Exception {
    Logger.net("Session opened!");
    session.setAttribute("player", gc.player);
  }

  @Override
  public void inputClosed(IoSession arg0) throws Exception {
    // TODO Auto-generated method stub
  }

}
