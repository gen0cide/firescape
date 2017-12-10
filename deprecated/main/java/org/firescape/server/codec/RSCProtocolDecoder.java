package org.firescape.server.codec;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.firescape.server.net.RSCPacket;
import org.firescape.server.opcode.Opcode;
import org.firescape.server.util.DataConversions;

/**
 * A decoder for the RSC protocol. Parses the incoming data from an IoSession and outputs it as a <code>RSCPacket</code>
 * object.
 */
public class RSCProtocolDecoder extends CumulativeProtocolDecoder {

  /**
   * Parses the data in the provided byte buffer and writes it to <code>out</code> as a <code>RSCPacket</code>.
   *
   * @param session
   *   The IoSession the data was read from
   * @param in
   *   The buffer
   * @param out
   *   The decoder output stream to which to write the <code>RSCPacket</code>
   *
   * @return Whether enough data was available to create a packet
   */
  protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) {
    if (in.remaining() >= 2) {
      int length = in.get();
      if (length >= 160) {
        length = (length - 160) * 256 + in.get();
      }
      if (length <= in.remaining()) {
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
        RSCPacket outboundPacket = new RSCPacket(session, id, payload);
        byte[] debugData = new byte[payload.length + 1];
        debugData[0] = (byte) id;
        for (int i = 1; i < debugData.length; ++i) {
          debugData[i] = payload[i - 1];
        }
        System.out.println(String.format(
          "[Packet] <<< %s(id=%d) size=%d\n%s",
          Opcode.getClient(204, id),
          id,
          debugData.length,
          DataConversions.bytesToHex(debugData)
        ));
        out.write(outboundPacket);
        return true;
      } else {
        in.rewind();
        return false;
      }
    }
    return false;
  }

  /**
   * Releases the buffer used by the given session.
   *
   * @param session
   *   The session for which to release the buffer
   *
   * @throws Exception
   *   if failed to dispose all resources
   */
  public void dispose(IoSession session) throws Exception {
    super.dispose(session);
  }
}
