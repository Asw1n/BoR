package org.aswinmp.lejos.ev3.bandofrobots.musicians.k3ys;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class K3ysMusician extends AbstractMusician {
	final K3ys piano = new K3ys();

	public static void main(final String[] args) {
		final K3ysMusician pianoMusician = new K3ysMusician();
		pianoMusician.piano.calibrate();
//		pianoMusician.piano.testBack();
//    pianoMusician.piano.test();
		pianoMusician.piano.spreadHands(false);
		pianoMusician.piano.rest();

		try {
			// register
			pianoMusician.register();
		} catch (RemoteException | AlreadyBoundException exc) {
			BrickLogger.error("An exception occurred", exc);
			exc.printStackTrace();
		}
	}

	@Override
	public void start() {
		piano.openEyes();
		piano.centerHands(true);
	}

	@Override
	public void stop() {
		piano.spreadHands(false);
		piano.closeEyes();
		piano.rest();
	}

	@Override
	public void noteOn(final int tone, final int intensity) {
		piano.play(tone);
	}
	
	 @Override
	  public void voiceOn(final int tone, final int intensity) {
	   piano.openMouth(intensity);
	  }

	  @Override
	  public void voiceOff(final int tone) {
	    piano.closeMouth();
	  }

	@Override
	public String toString() {
		return "PianoMusician";
	}

}
