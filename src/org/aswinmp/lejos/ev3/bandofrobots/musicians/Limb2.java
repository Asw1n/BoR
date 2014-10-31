package org.aswinmp.lejos.ev3.bandofrobots.musicians;

import lejos.robotics.RegulatedMotor;

/**
 * The Limb class represents a limb of a humanoid robot that is powered by a
 * single regulated motor
 * 
 * @author Aswin Bouwmeester, Matthias Paul Scholz
 * 
 */
public class Limb2 {

	RegulatedMotor motor;
	private final LimbRange logicalLimbRange;
	private double tickFactor;

	/**
	 * Constructor. The physical range is assumed to be the same as the logical
	 * one as specified. It can be calibrated to the actual physical setup by
	 * calibrating the {@link Limb}.
	 * 
	 * @param logicalMin
	 *            the logical minimum value of the range the {@link Limb} is
	 *            able to move in.
	 * @param logicalMax
	 *            the logical maximum value of the range the {@link Limb} is
	 *            able to move in.
	 */
	public Limb2(final RegulatedMotor motor, final int logicalMin,
			final int logicalMax) {
		this.motor = motor;
		this.logicalLimbRange = new LimbRange(logicalMin, logicalMax);
		/*
		 * In case no physical limb range is provided, we assume it to be the
		 * same as the logical one. This can be reset by calibrating the limb.
		 */
		recomputeTickFactor(logicalLimbRange);
	}

	/**
	 * Constructor.
	 * 
	 * @param motor
	 *            the {@link RegulatedMotor} that steers the {@link Limb}.
	 * @param logicalMin
	 *            the logical minimum value of the range the {@link Limb} is
	 *            able to move in.
	 * @param logicalMax
	 *            the logical maximum value of the range the {@link Limb} is
	 *            able to move in.
	 * @param physicalRange
	 *            the physical range that the {@link Limb} is able to move.
	 * @param physicalMax
	 *            the physical maximum value of the range the {@link Limb} is
	 *            able to move in.
	 */
	public Limb2(final RegulatedMotor motor, final int logicalMin,
			final int logicalMax, final int physicalRange) {
		this.motor = motor;
		this.logicalLimbRange = new LimbRange(logicalMin, logicalMax);
		recomputeTickFactor(new LimbRange(0, physicalRange));
	}

	/**
	 * Sets the speed of the motor as a fraction (0-1) of the maximum speed
	 * 
	 * @param speedFactor
	 */
	public void setSpeed(final float speedFactor) {
		motor.setSpeed((int) (motor.getMaxSpeed() * speedFactor));
	}

	/**
	 * Calibrates the logical range of the {@link Limb} against the actual
	 * physical setup by moving the {@link Limb} forward and backward to its
	 * limits. When the calibration routine has completed, the {@link Limb} will
	 * be positioned in the middle of its range.
	 */
	public void calibrate() {
		final int originalSpeed = motor.getSpeed();
		// TODO what is the best speed here?
		motor.setSpeed(50);
		// determine physical max range
		motor.forward();
		while (!motor.isStalled()) {
			// wait for stall
		}
		motor.stop();
		final int physicalMax = motor.getTachoCount();
		// determine physical min range
		motor.backward();
		while (!motor.isStalled()) {
			// wait for stall
		}
		motor.stop();
		final int physicalMin = motor.getTachoCount();
		// set physical limb range
		final LimbRange physicalLimbRange = new LimbRange(0, physicalMax
				- physicalMin);
		// recompute tick factor
		recomputeTickFactor(physicalLimbRange);
		// position limb in the logical middle
		motor.resetTachoCount();
		moveTo(logicalLimbRange.getMiddle(), false);
		// reset speed
		motor.setSpeed(originalSpeed);
	}

	/**
	 * Rotates the limb to its minimum position
	 * 
	 */
	public void moveToMin(final boolean immediateReturn) {
		moveTo(logicalLimbRange.getMin(), immediateReturn);
	}

	/**
	 * Rotates the limb to its maximum position
	 * 
	 */
	public void moveToMax(final boolean immediateReturn) {
		moveTo(logicalLimbRange.getMax(), immediateReturn);
	}

	/**
	 * @return the position of the limb.
	 */
	public int getPosition() {
		return (int) (logicalLimbRange.getMin() + motor.getTachoCount()
				/ tickFactor);
	}

	/**
	 * Moves the limb to a position. If the target value is not contained in the
	 * logical range specified by the user, it will be cut appropriately.
	 * 
	 * @param value
	 *            the target value.
	 */
	public void moveTo(final float value, final boolean immediateReturn) {
		int targetValue = (int) value;
		if (targetValue > logicalLimbRange.getMax()) {
			targetValue = logicalLimbRange.getMax();
		} else if (targetValue < logicalLimbRange.getMin()) {
			targetValue = logicalLimbRange.getMin();
		}
		motor.rotateTo(toTick(targetValue), immediateReturn);
	}

	/**
	 * Rests the limb and puts the motor into float.
	 */
	public void rest() {
		// TODO why is this required instead of simply calling stop()?
		while (motor.isMoving())
			Thread.yield();
		motor.flt();
	}

	private void recomputeTickFactor(final LimbRange physicalLimbRange) {
		tickFactor = physicalLimbRange.getRange()
				/ (double) logicalLimbRange.getRange();
	}

	private int toTick(final float value) {
		return (int) ((value - logicalLimbRange.getMin()) * tickFactor);
	}

	private class LimbRange {
		int min;
		int max;

		public LimbRange(final int min, final int max) {
			this.min = min;
			this.max = max;
		}

		public int getMin() {
			return min;
		}

		public int getMax() {
			return max;
		}

		public int getRange() {
			return max - min;
		}

		public int getMiddle() {
			return min + (max - min) / 2;
		}

	}
}
