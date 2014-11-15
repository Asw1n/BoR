package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import lejos.hardware.Button;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.RandomlyDelayedMoveTorsoExecutor;
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
		final RandomlyDelayedActionExecutor nod = new RandomlyDelayedActionExecutor(
				2000, new RobotAction() {
					@Override
					public void execute() {
						drumm3r.nod();
					}
				});
		final RandomlyDelayedMoveTorsoExecutor moveTorso = new RandomlyDelayedMoveTorsoExecutor(
				2000, drumm3r);
		drumm3r.enableLEDPattern(true);
		drumm3r.openEyes();
		new Thread(drum).start();
		new Thread(nod).start();
		new Thread(moveTorso).start();
		Button.waitForAnyPress();
		drum.stop();
		nod.stop();
		moveTorso.stop();
		drumm3r.reset();
	}
}
