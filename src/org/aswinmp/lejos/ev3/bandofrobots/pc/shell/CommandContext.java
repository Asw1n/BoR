package org.aswinmp.lejos.ev3.bandofrobots.pc.shell;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.player.BorPlayer;

public class CommandContext {

  private BoRController borController;
  private BorPlayer borPlayer;

  public BoRController getBoRController() throws MidiUnavailableException {
    if (borController == null) {
      // TODO this should be performed by a factory
      // lazily create and configure BoRController
      borController = new BoRController();
      // set default sequencer
      borController.setSequencer(MidiSystem.getSequencer().getDeviceInfo());
      // set default synthesizer
      borController.setSynthesizer(MidiSystem.getSynthesizer().getDeviceInfo());
    }
    return borController;
  }

  public BorPlayer getBorPlayer() throws MidiUnavailableException {
    if (borPlayer == null) {
      // lazily create and configure BorPlayer
      borPlayer = new BorPlayer(getBoRController());
    }
    return borPlayer;
  }

}
