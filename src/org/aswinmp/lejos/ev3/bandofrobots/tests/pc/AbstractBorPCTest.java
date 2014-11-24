package org.aswinmp.lejos.ev3.bandofrobots.tests.pc;

import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.player.BorPlayer;

public abstract class AbstractBorPCTest {

  protected void stopPlayerAfterPeriod(final BorPlayer borPlayer, final long periodInMilliseconds) {
    System.out
        .println(String
            .format("Will stop BoR player after %d milliseconds", periodInMilliseconds));
    final Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println(String
            .format("%d milliseconds have elapsed => stopping BoR player", periodInMilliseconds));
        borPlayer.shutDown();
        timer.cancel();
      }
    }, periodInMilliseconds);
  }

  // TODO this should be performed by a factory
  protected BoRController createBoRController() throws MidiUnavailableException {
    final BoRController borController = new BoRController();
    // set default sequencer
    borController.setSequencer(MidiSystem.getSequencer().getDeviceInfo());
    // set default synthesizer
    borController.setSynthesizer(MidiSystem.getSynthesizer().getDeviceInfo());
    return borController;
  }
}
