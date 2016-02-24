package org.rscdaemon.bot.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.rscdaemon.bot.net.RSCPacket;
import org.rscdaemon.bot.util.LoginHelper;

public class RSCProtocolDecoder extends CumulativeProtocolDecoder {
  /**
   * Parses the data in the provided byte buffer and writes it to
   * <code>out</code> as a <code>RSCPacket</code>.
   *
   * @param session
   *          The IoSession the data was read from
   * @param in
   *          The buffer
   * @param out
   *          The decoder output stream to which to write the
   *          <code>RSCPacket</code>
   * @return Whether enough data was available to create a packet
   */
  // protected boolean doDecode(IoSession session, ByteBuffer in,
  // ProtocolDecoderOutput out) {
  //
  // return false;
  // }

  /**
   * Releases the buffer used by the given session.
   *
   * @param session
   *          The session for which to release the buffer
   * @throws Exception
   *           if failed to dispose all resources
   */
  public void dispose(IoSession session) throws Exception {
    super.dispose(session);
  }

  @Override
  protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
    if (LoginHelper.awaitingSession) {
      byte[] payload = new byte[8];
      for (int i = 0; i < payload.length; i++) {
        payload[i] = in.get();
      }
      RSCPacket pkt = new RSCPacket(session, 1776, payload, true);
      out.write(pkt);
      return true;
    } else if (LoginHelper.awaitingLogin) {
      byte[] payload = new byte[1];
      for (int i = 0; i < payload.length; i++) {
        payload[i] = in.get();
      }
      out.write(new RSCPacket(session, 1777, payload, true));
      LoginHelper.awaitingLogin = false;
      return true;
    }
    if (in.remaining() >= 2) {
      int length = in.get();
      if (length - 1 < 0) {
        return false;
      }
      if (length >= 160) {
        length = (length - 160) * 256 + in.get();
      }
      if (length <= in.remaining()) {
        if (length - 1 < 0) {
          return false;
        }
        byte[] payload = new byte[length - 1];
        int id;
        if (length < 160) {
          if (length > 1) {
            payload[length - 2] = in.get();
            id = in.getUnsigned();
            if (length - 2 > 0) {
              in.get(payload, 0, length - 2);
            }
          } else {
            id = in.getUnsigned();
          }
        } else {
          id = in.getUnsigned();
          in.get(payload);
        }
        out.write(new RSCPacket(session, id, payload));
        return true;
      } else {
        in.rewind();
        return false;
      }
    }
    return false;
  }
}
