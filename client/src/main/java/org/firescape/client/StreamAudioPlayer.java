package org.firescape.client;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class StreamAudioPlayer {

  // from sun.audio.AudioDevice.openChannel
  private static final AudioFormat pcmFormat = new AudioFormat(
    AudioFormat.Encoding.ULAW,
    8000, 8,
    1, 1,
    8000, true);
  // from com.sun.media.sound.Toolkit.getPCMConvertedAudioInputStream
  private static final AudioFormat lineFormat = new AudioFormat(
    AudioFormat.Encoding.PCM_SIGNED, 8000,
    16, 1, 2,
    8000, ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
  private Clip clip;

  public StreamAudioPlayer() throws LineUnavailableException {
    clip = AudioSystem.getClip();
  }

  public void stopPlayer() {
    clip.stop();
    clip.flush();
  }

  public void writeStream(byte buf[], int off, int len) throws IOException, LineUnavailableException {
    stopPlayer();
    clip.close();
    ByteArrayInputStream buffer = new ByteArrayInputStream(buf, off, len);
    AudioInputStream audio = new AudioInputStream(buffer, pcmFormat, -1);
    audio = AudioSystem.getAudioInputStream(lineFormat, audio);
    clip.open(audio);
    clip.start();
  }
}