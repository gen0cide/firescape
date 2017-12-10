package org.firescape.server.model;

import java.io.UnsupportedEncodingException;

public class ChatMessage {

  /**
   * Who sent the message
   */
  private final Mob sender;
  /**
   * The message it self, in byte format
   */
  private byte[] message;
  /**
   * Who the message is for
   */
  private Mob recipient;

  public ChatMessage(Mob sender, byte[] message) {
    this.sender = sender;
    this.message = message;
  }

  public ChatMessage(Mob sender, String message, Mob recipient) {
    this.sender = sender;
    this.message = new byte[] {};
    try {
      this.message = message.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
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
