package org.aswinmp.lejos.ev3.bandofrobots.musicians.guitarist;

import java.rmi.RemoteException;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;

import lejos.hardware.motor.Motor;

public class Guitarist extends AbstractMusician {

	private int lowestNote = 0;
	private int toneRange = 0;
	private float ticksPerTone = 0;

	public Guitarist() {
		super();
		Motor.B.setSpeed(Motor.B.getMaxSpeed());
		Motor.A.setSpeed(Motor.A.getMaxSpeed());
	}

	@Override
	public void noteOn(final int tone, final int intensity) {
		moveRightHand(computeRightHandPosition(tone));
		pick();
	}

	@Override
	public void noteOff(final int tone) {
		moveRightHand(lowestNote);
	}

	@Override
	public void setDynamicRange(final int lowestNote, final int highestNote)
			throws RemoteException {
		super.setDynamicRange(lowestNote, highestNote);
		this.lowestNote = lowestNote;
		toneRange = highestNote - lowestNote;
		ticksPerTone = 360 / toneRange;
	}

	@Override
	protected void beatPulse(int BeatNo, int pulseNO) {
	}

	private void pick() {
		Motor.B.rotate(-360, true);
	}

	private void moveRightHand(final int position) {
		Motor.A.rotateTo(position);
	}

	private int computeRightHandPosition(final int tone) {
		return (int) ((tone - lowestNote) * ticksPerTone);
	}
}
