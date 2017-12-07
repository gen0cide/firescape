package org.firescape.client;

import javax.sound.sampled.*;
import java.io.InputStream;

public class StreamAudioPlayer extends InputStream {

  byte buffer[];
  int start;
  int end;
  Clip clip;
  AudioInputStream audioIn;

  public StreamAudioPlayer() {
    return;
    //    InputStream bio = null;
    //    bio = new BufferedInputStream(this);
    //    audioIn = null;
    //    try {
    //      audioIn = AudioSystem.getAudioInputStream(bio);
    //      BufferedInputStream bufferedInputStream = new BufferedInputStream(audioIn);
    //      audioIn = new AudioInputStream(bufferedInputStream, audioIn.getFormat(), audioIn.getFrameLength());
    //      AudioFormat format = audioIn.getFormat();
    //      DataLine.Info info = new DataLine.Info(Clip.class, format);
    //      Clip clip = (Clip) AudioSystem.getLine(info);
    //      clip.open(audioIn);
    //      clip.start();
    //    } catch (UnsupportedAudioFileException e) {
    //      e.printStackTrace();
    //    } catch (IOException e) {
    //      e.printStackTrace();
    //    } catch (LineUnavailableException e) {
    //      e.printStackTrace();
    //    }
  }

  public void stopPlayer() {
    return;
    //    if (clip.isRunning()) {
    //      clip.stop();
    //    }
  }

  public void writeStream(byte buf[], int off, int len) {
    buffer = buf;
    start = off;
    end = off + len;
  }

  public int read() {
    byte abyte0[] = new byte[1];
    read(abyte0, 0, 1);
    return abyte0[0];
  }

  public int read(byte abyte0[], int i, int j) {
    for (int k = 0; k < j; k++) {
      if (start < end) {
        abyte0[i + k] = buffer[start++];
      } else {
        abyte0[i + k] = -1;
      }
    }

    return j;
  }
}
