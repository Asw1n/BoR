package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.sing3r;

import lejos.hardware.Button;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.sing3r.Sing3r;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.RandomlyDelayedActionExecutor;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.RobotAction;
import org.aswinmp.lejos.ev3.bandofrobots.utils.AbstractRandomlyDelayedExecutor;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class Sing3rTest {

	public static void main(final String[] args) {
		final Sing3r sing3r = new Sing3r();
		BrickLogger.info("Calibrating");
		sing3r.calibrate();
		sing3r.reset();
		BrickLogger.info("Singing");
		final RandomlyDelayedActionExecutor sing = new RandomlyDelayedActionExecutor(
				1000, new RobotAction() {
					@Override
					public void execute() {
						sing3r.sing();
					}
				});
		final RandomlyDelayedActionExecutor swingArms = new RandomlyDelayedActionExecutor(
				2000, new RobotAction() {
					@Override
					public void execute() {
						sing3r.swingArms(false);
					}
				});
		final RandomlyDelayedMoveExecutor move = new RandomlyDelayedMoveExecutor(
				2000, sing3r);
		sing3r.enableLEDPattern(true);
		sing3r.openEyes();
		new Thread(sing).start();
		new Thread(swingArms).start();
		new Thread(move).start();
		Button.waitForAnyPress();
		move.stop();
		sing.stop();
		swingArms.stop();
		sing3r.reset();
	}

	static class RandomlyDelayedMoveExecutor extends
			AbstractRandomlyDelayedExecutor {

		private final Sing3r sing3r;

		public RandomlyDelayedMoveExecutor(final int randomMax,
				final Sing3r sing3r) {
			super(randomMax);
			this.sing3r = sing3r;
		}

		@Override
		protected void execute() {
			// move randomly
			sing3r.moveRandomly(false);
		}
	}
}
