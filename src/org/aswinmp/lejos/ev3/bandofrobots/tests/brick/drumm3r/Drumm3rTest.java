package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import java.util.Random;

import lejos.hardware.Button;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r.TorsoLocation;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.AbstractRandomlyDelayedExecutor;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.RandomlyDelayedActionExecutor;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.RobotAction;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class Drumm3rTest {

	public static void main(final String[] args) {
		final Drumm3r drumm3r = new Drumm3r();
		BrickLogger.info("Calibrating");
		drumm3r.calibrate();
		drumm3r.reset();
		BrickLogger.info("Drumming");
		final RandomlyDelayedActionExecutor drum = new RandomlyDelayedActionExecutor(
				1000, new RobotAction() {
					@Override
					public void execute() {
						drumm3r.drum(false);
					}
				});
		final RandomlyDelayedActionExecutor tap = new RandomlyDelayedActionExecutor(
				2000, new RobotAction() {
					@Override
					public void execute() {
						drumm3r.tap();
					}
				});
		final RandomlyDelayedMoveTorsoExecutor moveTorso = new RandomlyDelayedMoveTorsoExecutor(
				2000, drumm3r);
		drumm3r.enableLEDPattern(true);
		new Thread(drum).start();
		new Thread(tap).start();
		new Thread(moveTorso).start();
		Button.waitForAnyPress();
		drum.stop();
		tap.stop();
		moveTorso.stop();
		drumm3r.reset();
	}

	static class RandomlyDelayedMoveTorsoExecutor extends
			AbstractRandomlyDelayedExecutor {

		private final Drumm3r drumm3r;
		private final TorsoLocation[] availableTorsoLocations = TorsoLocation
				.values();

		public RandomlyDelayedMoveTorsoExecutor(final int randomMax,
				final Drumm3r drumm3r) {
			super(randomMax);
			this.drumm3r = drumm3r;
		}

		@Override
		protected void execute() {
			// create random torso location
			final int randomEnumIndex = new Random()
					.nextInt(availableTorsoLocations.length);
			final TorsoLocation randomTorsoLocation = availableTorsoLocations[randomEnumIndex];
			// move torso
			drumm3r.moveTorsoTo(randomTorsoLocation);
		}
	}
}
