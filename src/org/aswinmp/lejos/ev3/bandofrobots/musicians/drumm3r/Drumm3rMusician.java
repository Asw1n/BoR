package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import lejos.hardware.Sound;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Musician;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

/**
 * An implementation of a {@link Musician} that assigns the {@link Drumm3r} to
 * MIDI events.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class Drumm3rMusician extends AbstractMusician {

	private final Drumm3r drumm3r;
	private final RandomlyDelayedMoveTorsoExecutor moveTorso;

	public static void main(final String[] args) {
		try {
			final Drumm3rMusician drumm3rMusician = new Drumm3rMusician();
			// register
			drumm3rMusician.register();
			// finally play sound to signal that setting up the
			// Drumm3r is complete
			Sound.beepSequenceUp();
		} catch (RemoteException | AlreadyBoundException exc) {
			BrickLogger.error(exc.getMessage(), exc);
		}
	}

	public Drumm3rMusician() {
		super();
		drumm3r = new Drumm3r();
		moveTorso = new RandomlyDelayedMoveTorsoExecutor(2000, drumm3r);
		drumm3r.calibrate();
		drumm3r.reset();
	}

	@Override
	public void start() {
		super.start();
		drumm3r.openEyes();
		new Thread(moveTorso).start();
		drumm3r.enableLEDPattern(true);
	}

	@Override
	public void noteOn(final int tone, final int intensity) {
		super.noteOn(tone, intensity);
		drumm3r.drum(true);
	}

	@Override
	public void stop() {
		super.stop();
		if (moveTorso != null) {
			moveTorso.stop();
		}
		drumm3r.reset();
	}

	@Override
	protected void beatPulse(final int beatNo, final int pulseNo) {
		super.beatPulse(beatNo, pulseNo);
		if (beatNo % 2 == 0) {
			drumm3r.nod();
		}
	}

	@Override
	public String toString() {
		return "Drumm3rMusician";
	}

}
