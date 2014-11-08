package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import lejos.hardware.Button;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r;

public class Drumm3rTest {

	public static void main(final String[] args) {
		final Drumm3r drumm3r = new Drumm3r();
		System.out.println("Calibrating");
		drumm3r.calibrate();
		System.out.println("Drumming");
		final RandomlyDelayedActionExecutor drumLeft = new RandomlyDelayedActionExecutor(
				new Drumm3rAction() {
					@Override
					public void execute() {
						drumm3r.drumLeft();
					}
				});
		final RandomlyDelayedActionExecutor drumRight = new RandomlyDelayedActionExecutor(
				new Drumm3rAction() {
					@Override
					public void execute() {
						drumm3r.drumRight();
					}
				});
		final RandomlyDelayedActionExecutor tap = new RandomlyDelayedActionExecutor(
				new Drumm3rAction() {
					@Override
					public void execute() {
						drumm3r.tap();
					}
				});
		final RandomlyDelayedMoveTorsoExecutor moveTorso = new RandomlyDelayedMoveTorsoExecutor(
				drumm3r);
		new Thread(drumLeft).start();
		new Thread(drumRight).start();
		new Thread(tap).start();
		new Thread(moveTorso).start();
		Button.waitForAnyPress();
		drumLeft.stop();
		drumRight.stop();
		tap.stop();
		moveTorso.stop();
	}

}
