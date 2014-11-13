package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import lejos.hardware.Button;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3rHead;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.RandomlyDelayedActionExecutor;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.RobotAction;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class Drumm3rHeadTest {

	public static void main(final String[] args) {
		final Drumm3rHead drumm3rHead = new Drumm3rHead();
		BrickLogger.info("Moving head");
		drumm3rHead.openEyes();
		drumm3rHead.enableLEDPattern(true);
		final RandomlyDelayedActionExecutor nod = new RandomlyDelayedActionExecutor(
				2000, new RobotAction() {
					@Override
					public void execute() {
						drumm3rHead.nod();
					}
				});
		new Thread(nod).start();
		Button.waitForAnyPress();
		nod.stop();
		drumm3rHead.reset();
	}
}
