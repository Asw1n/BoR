package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import lejos.hardware.Sound;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;

public class Drumm3rMusician extends AbstractMusician {

	private final Drumm3r drumm3r;

	public static void main(final String[] args) {
		try {
			final Drumm3rMusician drumm3rMusician = new Drumm3rMusician();
			// register
			drumm3rMusician.register();
			// play sound to signal that setting up the Drumm3r is complete
			Sound.beepSequenceUp();
		} catch (RemoteException | AlreadyBoundException exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
	}

	public Drumm3rMusician() {
		super();
		drumm3r = new Drumm3r();
		drumm3r.calibrate();
		drumm3r.reset();
	}

	@Override
	public void start() {
		super.start();
		drumm3r.enableLEDPattern(true);
		drumm3r.openEyes();
	}

	@Override
	public void noteOn(final int tone, final int intensity) {
		super.noteOn(tone, intensity);
		drumm3r.drum(true);
	}

	@Override
	public void stop() {
		super.stop();
		drumm3r.reset();
	}

	@Override
	protected void beatPulse(final int beatNo, final int pulseNo) {
		super.beatPulse(beatNo, pulseNo);
		if (beatNo % 2 == 0) {
			drumm3r.tap();
		}
	}

}
