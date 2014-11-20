package org.aswinmp.lejos.ev3.bandofrobots.musicians.sing3r;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import lejos.hardware.Sound;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class Sing3rMusician extends AbstractMusician {

	private final Sing3r sing3r;
	private final RandomlyDelayedMicroExecutor micro;

	public static void main(final String[] args) {
		try {
			final Sing3rMusician sing3rMusician = new Sing3rMusician();
			// register
			sing3rMusician.register();
			// finally play sound to signal that setting up the
			// Sing3r final is complete
			Sound.beepSequenceUp();
		} catch (RemoteException | AlreadyBoundException exc) {
			BrickLogger.error(exc.getMessage(), exc);
		}
	}

	public Sing3rMusician() {
		super();
		sing3r = new Sing3r();
		micro = new RandomlyDelayedMicroExecutor(2000, sing3r);
		sing3r.calibrate();
		sing3r.reset();
	}

	@Override
	public void start() {
		super.start();
		sing3r.openEyes();
		new Thread(micro).start();
		sing3r.enableLEDPattern(true);
	}

	@Override
	public void voiceOn(final int tone, final int intensity) {
		// BrickLogger.info("voiceOn(%d)", intensity);
		sing3r.openMouth(intensity, true);
	}

	@Override
	public void voiceOff(final int tone) {
		// BrickLogger.info("voiceOff");
		sing3r.closeMouth();
	}

	@Override
	public void stop() {
		super.stop();
		if (micro != null) {
			micro.stop();
		}
		sing3r.reset();
	}

	@Override
	protected void beatPulse(final int beatNo, final int pulseNo) {
		super.beatPulse(beatNo, pulseNo);
		if (beatNo == 0) {
			sing3r.moveRandomly(true);
		}
	}

	@Override
	public String toString() {
		return "Sing3rMusician";
	}

}
