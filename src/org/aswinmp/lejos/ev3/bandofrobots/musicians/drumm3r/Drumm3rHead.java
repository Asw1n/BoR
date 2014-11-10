package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Drumm3rHead {

	private static Port HEAD_MOTOR_PORT = MotorPort.A;
	private static Port EYES_PORT = SensorPort.S1;

	private static final int HEAD_MIN = -120;
	private static final int HEAD_MAX = 120;

	// TODO this should be a Limb once un-calibrated limbs are supported
	private final EV3MediumRegulatedMotor head;
	private final EV3UltrasonicSensor eyes;

	public Drumm3rHead() {
		// create and configure limbs
		head = new EV3MediumRegulatedMotor(HEAD_MOTOR_PORT);
		head.setSpeed(head.getMaxSpeed() / 2);
		eyes = new EV3UltrasonicSensor(EYES_PORT);
	}

	public void openEyes() {
		eyes.enable();
	}

	public void closeEyes() {
		eyes.disable();
	}

	public void moveHeadTo(final HeadLocation location) {
		switch (location) {
		case LEFT:
			head.rotateTo(HEAD_MIN);
			break;
		case RIGHT:
			head.rotateTo(HEAD_MAX);
			break;
		default:
			head.rotateTo(0);
		}

	}

	public void reset() {
		head.rotateTo(0);
		closeEyes();
		enableLEDPattern(false);
	}

	public void enableLEDPattern(final boolean enabled) {
		final int pattern = enabled ? 3 : 0;
		LocalEV3.get().getLED().setPattern(pattern);
	}

	public enum HeadLocation {
		LEFT, RIGHT, MIDDLE;
	}

}
