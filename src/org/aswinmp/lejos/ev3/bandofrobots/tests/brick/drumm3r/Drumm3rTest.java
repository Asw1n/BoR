package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import lejos.hardware.Button;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.RandomlyDelayedActionExecutor;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.RobotAction;

public class Drumm3rTest {

	public static void main(final String[] args) {
		final Drumm3r drumm3r = new Drumm3r();
		System.out.println("Calibrating");
		drumm3r.calibrate();
		System.out.println("Drumming");
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
		drumm3r.openEyes();
		new Thread(drum).start();
		new Thread(tap).start();
		new Thread(moveTorso).start();
		Button.waitForAnyPress();
		drum.stop();
		tap.stop();
		moveTorso.stop();
		drumm3r.reset();
	}

}
