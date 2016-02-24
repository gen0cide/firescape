package org.rscdaemon.bot.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class RSCCodecFactory implements ProtocolCodecFactory {
  /**
   * The protocol encoder in use
   */
  private static ProtocolEncoder encoder = new RSCProtocolEncoder();
  /**
   * The protocol decoder in use
   */
  private static ProtocolDecoder decoder = new RSCProtocolDecoder();

  /**
   * Provides the encoder to use to parse incoming data.
   *
   * @return A protocol encoder
   */
  public ProtocolEncoder getEncoder() {
    return encoder;
  }

  /**
   * Provides the decoder to use to format outgoing data.
   *
   * @return A protocol decoder
   */
  public ProtocolDecoder getDecoder() {
    return decoder;
  }

  @Override
  public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
    return decoder;
  }

  @Override
  public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
    return encoder;
  }
}
