package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinderAdaptor;
import lejos.robotics.SampleProvider;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.DualBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.DualTouchSensorCalibration;

/**
 * A drumming robot.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class Drumm3r {

	private static Port LEFT_HAND_MOTOR_PORT = MotorPort.C;
	private static Port RIGHT_HAND_MOTOR_PORT = MotorPort.B;
	// private static Port RIGHT_FOOT_MOTOR_PORT = MotorPort.A;
	private static Port TORSO_MOTOR_PORT = MotorPort.A;
	private static Port HEAD_MOTOR_PORT = MotorPort.D;
	private static Port TORSO_MIN_TOUCH_SENSOR = SensorPort.S4;
	private static Port TORSO_MAX_TOUCH_SENSOR = SensorPort.S3;
	private static Port EYES_PORT = SensorPort.S1;

	private DrumsLocation drumsLocation = DrumsLocation.NONE;

	private final Limb leftHand;
	private final Limb rightHand;
	private final Limb torso;
	// private final EV3MediumRegulatedMotor rightFoot;
	// TODO this should be a Limb once un-calibrated limbs are supported
	private final EV3LargeRegulatedMotor head;
	private final EV3UltrasonicSensor eyes;
	private final RangeFinderAdaptor ultrasonicRangeFinder;

	public Drumm3r() {
		// create and configure limbs
		leftHand = new LinearLimb(new EV3LargeRegulatedMotor(
				LEFT_HAND_MOTOR_PORT), false, new DualBoundaryCalibration(50,
				0, 30), 0, 100);
		leftHand.setSpeed(1.0f);
		rightHand = new LinearLimb(new EV3LargeRegulatedMotor(
				RIGHT_HAND_MOTOR_PORT), false, new DualBoundaryCalibration(50,
				0, 30), 0, 100);
		rightHand.setSpeed(1.0f);
		final EV3LargeRegulatedMotor torsoMotor = new EV3LargeRegulatedMotor(
				TORSO_MOTOR_PORT);
		torso = new LinearLimb(torsoMotor, false,
				new DualTouchSensorCalibration(TORSO_MIN_TOUCH_SENSOR,
						TORSO_MAX_TOUCH_SENSOR, (int) torsoMotor.getMaxSpeed()));
		torso.setSpeed(1.0f);
		// rightFoot = new EV3MediumRegulatedMotor(RIGHT_FOOT_MOTOR_PORT);
		// rightFoot.setSpeed(rightFoot.getMaxSpeed());
		head = new EV3LargeRegulatedMotor(HEAD_MOTOR_PORT);
		head.setSpeed(head.getMaxSpeed());
		eyes = new EV3UltrasonicSensor(EYES_PORT);
		final SampleProvider eyesSampleProvider = eyes.getDistanceMode();
		ultrasonicRangeFinder = new RangeFinderAdaptor(eyesSampleProvider);

	}

	public void calibrate() {
		rightHand.calibrate();
		leftHand.calibrate();
		torso.calibrate();
	}

	public void openEyes() {
		eyes.enable();
	}

	public void closeEyes() {
		eyes.disable();
	}

	public void drum(final boolean immediateReturn) {
		switch (drumsLocation) {
		case LEFT:
		case NONE:
			leftHand.moveToCenter(true);
			rightHand.moveToMax(immediateReturn);
			drumsLocation = DrumsLocation.RIGHT;
			break;
		case RIGHT:
			rightHand.moveToCenter(true);
			leftHand.moveToMax(immediateReturn);
			drumsLocation = DrumsLocation.LEFT;
			break;
		default:
			// nothing to do
		}
	}

	// public void tap() {
	// rightFoot.rotate(360);
	// }

	public void moveTorsoTo(final TorsoLocation location) {
		switch (location) {
		case LEFT:
			torso.moveToMin(false);
			break;
		case RIGHT:
			torso.moveToMax(false);
			break;
		default:
			torso.moveToCenter(false);
		}

	}

	public void reset() {
		rightHand.moveToCenter(false);
		leftHand.moveToCenter(false);
		torso.moveToCenter(false);
		enableLEDPattern(false);
		closeEyes();
	}

	public void enableLEDPattern(final boolean enabled) {
		final int pattern = enabled ? 3 : 0;
		LocalEV3.get().getLED().setPattern(pattern);
	}

	public void nod() {
		head.rotate(360);
	}

	public float getDistanceToEyes() {
		return ultrasonicRangeFinder.getRange();
	}

	public enum TorsoLocation {
		LEFT, RIGHT, MIDDLE;
	}

	enum DrumsLocation {
		LEFT, RIGHT, NONE, BOTH
	}
}
