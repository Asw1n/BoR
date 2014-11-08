package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import java.util.Random;

import lejos.hardware.Button;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3rHead;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3rHead.HeadLocation;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.AbstractRandomlyDelayedExecutor;

public class Drumm3rHeadTest {

	public static void main(final String[] args) {
		final Drumm3rHead drumm3rHead = new Drumm3rHead();
		System.out.println("Moving head");
		final RandomlyDelayedMoveHeadExecutor moveHead = new RandomlyDelayedMoveHeadExecutor(
				2000, drumm3rHead);
		drumm3rHead.openEyes();
		drumm3rHead.enableLEDPattern(true);
		new Thread(moveHead).start();
		Button.waitForAnyPress();
		moveHead.stop();
		drumm3rHead.reset();
	}

	static class RandomlyDelayedMoveHeadExecutor extends
			AbstractRandomlyDelayedExecutor {

		private final Drumm3rHead drumm3rHead;
		private final HeadLocation[] availableHeadLocations = HeadLocation
				.values();

		public RandomlyDelayedMoveHeadExecutor(final int randomMax,
				final Drumm3rHead drumm3rHead) {
			super(randomMax);
			this.drumm3rHead = drumm3rHead;
		}

		@Override
		protected void execute() {
			// create random head location
			final int randomEnumIndex = new Random()
					.nextInt(availableHeadLocations.length);
			final HeadLocation randomHeadLocation = availableHeadLocations[randomEnumIndex];
			// move head
			drumm3rHead.moveHeadTo(randomHeadLocation);
		}
	}
}
