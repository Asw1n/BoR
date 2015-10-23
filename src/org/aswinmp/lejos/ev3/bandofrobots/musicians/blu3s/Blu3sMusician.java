package org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class Blu3sMusician extends AbstractMusician {
	final Blu3s blu3s = new Blu3s();

	public static void main(final String[] args) {
    final Blu3sMusician blu3sMusician = new Blu3sMusician();
    blu3sMusician.blu3s.openEyes();
    blu3sMusician.blu3s.calibrateLimbs();
    blu3sMusician.blu3s.closeEyes();
    blu3sMusician.setBeatPulseDevider(4);
		try {
			// register
      blu3sMusician.register();
      blu3sMusician.blu3s.switchAmpOn();
		} catch (RemoteException | AlreadyBoundException exc) {
			BrickLogger.error("An exception occurred", exc);
			exc.printStackTrace();
		}
	}

	@Override
	public void start() {
		super.start();
		blu3s.openEyes();
    blu3s.closeEyes();
		blu3s.switchAmpOn();
	}

	@Override
	public void stop() {
		super.stop();
		blu3s.restLimbs();
		blu3s.closeEyes();
		blu3s.switchAmpOff();
	}

	@Override
	public void noteOn(final int tone, final int intensity) {
		blu3s.play(tone, intensity);
	}

	@Override
	public void voiceOn(final int tone, final int intensity) {
		blu3s.openMouth(intensity);
	}

	@Override
	public void voiceOff(final int tone) {
		blu3s.closeMouth();
	}

	@Override
	public void setDynamicRange(final int lowestNote, final int highestNote) {
		blu3s.setGuitarRange(lowestNote, highestNote);
	}

	@Override
	protected void beatPulse(final int beatNo, final int pulseNo) {
		blu3s.prepareRightHand();

		// The leg goes up and down every quarter note
		if (pulseNo % 4 == 3) {
			blu3s.tapFoot();
		} else {
			blu3s.raiseFoot();
		}
	}

	@Override
	public String toString() {
		return "Blu3sMusician";
	}

}
