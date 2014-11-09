package org.aswinmp.lejos.ev3.bandofrobots.musicians.piano;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class PianoMusician extends AbstractMusician {
	final Piano piano = new Piano();

	public static void main(final String[] args) {
		final PianoMusician pianoMusician = new PianoMusician();
		pianoMusician.piano.calibrate();
		pianoMusician.piano.restLimbs();

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
		piano.centerHands();
	}

	@Override
	public void stop() {
		piano.spreadHands();
		piano.restLimbs();
	}

	@Override
	public void noteOn(final int tone, final int intensity) {
		piano.playWithAnyHand(tone);
	}

	@Override
	public String toString() {
		return "PianoMusician";
	}

}
