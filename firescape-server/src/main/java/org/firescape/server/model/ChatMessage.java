package org.firescape.server.model;

import org.firescape.server.util.DataConversions;

public class ChatMessage {
  /**
   * Who sent the message
   */
  private final Mob sender;
  /**
   * The message it self, in byte format
   */
  private final byte[] message;
  /**
   * Who the message is for
   */
  private Mob recipient;

  public ChatMessage( Mob sender, byte[] message ) {
    this.sender = sender;
    this.message = message;
  }

  public ChatMessage( Mob sender, String message, Mob recipient ) {
    this.sender = sender;
    this.message = DataConversions.stringToByteArray(message);
    this.recipient = recipient;
  }

  public Mob getRecipient() {
    return recipient;
  }

  public Mob getSender() {
    return sender;
  }

  public byte[] getMessage() {
    return message;
  }

  public int getLength() {
    return message.length;
  }

}
