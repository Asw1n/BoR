package org.aswinmp.lejos.ev3.bandofrobots.pc.borserver;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * The conductor receives midi events from a sequencer and passes these on to
 * the appropriate bricks.
 * @author Aswin
 */
public class Conductor implements Receiver, MetaEventListener {
  private Channels channels;
  private final BoRController boRController;
  private Song song;

  public void setSong(final Song song) {
    channels = song.getChannels();
    this.song = song;
  }

  public Conductor(final BoRController controller) {
    boRController = controller;
  }

  /*
   * We are intereseted in tempo changes. This method receives all meta events,
   * setting tempo is one of them
   */
  @Override
  public void meta(final MetaMessage metaMessage) {
    switch (metaMessage.getMessage()[1]) {
    case 81:
      // calculate tempo from meta message
      int tempo = 0;
      for (int p = 2; p >= 0; p--) {
        tempo += (metaMessage.getMessage()[5 - p] & 0xFF) * Math.pow(255, p);
      }
      final byte[] b = metaMessage.getMessage();
      tempo = (((b[3] & 0xFF) << 16) | ((b[4] & 0xFF) << 8) | (b[5] & 0xFF)) / 1000;
      // System.out.println("Set tempo: " + tempo);

      // Send tempo to all musicians and singers
      for (final Brick brick : channels.getAllBricks()) {
        brick.setTempo(tempo);
      }

      break;

    case 47:
      // TODO: What happens if a tracks ends before the others?
      boRController.stop();
      break;
    }

  }

  @Override
  public void close() {
  }

  @Override
  public void send(final MidiMessage arg0, final long arg1) {
    if (arg0 instanceof ShortMessage) {
      final ShortMessage message = (ShortMessage) arg0;
      final int channel = message.getChannel();
      final int command = message.getCommand();
      if (song.metronomeIsSet && channel == song.metronomeChannel
          && command == ShortMessage.NOTE_ON) {
        for (final Brick brick : channels.getAllBricks()) {
          brick.sendBeat(message);
        }

      }
      if (command == ShortMessage.NOTE_ON /* || command == ShortMessage.NOTE_OFF */) {
        for (final Brick brick : channels.getInstrumentBricks(channel)) {
          brick.sendInstrumentEvent(message);
        }
      }
      if (command == ShortMessage.NOTE_ON || command == ShortMessage.NOTE_OFF) {
        for (final Brick brick : channels.getVoiceBricks(channel)) {
          brick.sendVoiceEvent(message);
        }
      }
    }
  }

}
