package org.aswinmp.lejos.ev3.bandofrobots.musicians.sing3r;

import java.util.Random;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
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

public class Sing3r {

	private static int MOVE_RANGE = 90;

	private static Port LEFT_LEG_MOTOR_PORT = MotorPort.D;
	private static Port RIGHT_LEG_MOTOR_PORT = MotorPort.A;
	private static Port ARMS_MOTOR_PORT = MotorPort.B;
	private static Port MOUTH_MOTOR_PORT = MotorPort.C;
	private static Port EYES_PORT = SensorPort.S3;
	private static Port ARMS_MIN_TOUCH_SENSOR = SensorPort.S2;
	private static Port ARMS_MAX_TOUCH_SENSOR = SensorPort.S4;

	private ArmsState armsState = ArmsState.UNKNOWN;

	private final EV3LargeRegulatedMotor leftLeg;
	private final EV3LargeRegulatedMotor rightLeg;
	private final Limb arms;
	private final Limb mouth;
	private final EV3UltrasonicSensor eyes;
	private final RangeFinderAdaptor ultrasonicRangeFinder;
	private final MoveDirection[] availableMoveDirections = MoveDirection
			.values();

	public Sing3r() {
		// create and configure limbs
		leftLeg = new EV3LargeRegulatedMotor(LEFT_LEG_MOTOR_PORT);
		leftLeg.setSpeed(leftLeg.getMaxSpeed() / 2);
		rightLeg = new EV3LargeRegulatedMotor(RIGHT_LEG_MOTOR_PORT);
		rightLeg.setSpeed(rightLeg.getMaxSpeed() / 2);
		arms = new LinearLimb(new EV3LargeRegulatedMotor(ARMS_MOTOR_PORT),
				false, new DualTouchSensorCalibration(ARMS_MIN_TOUCH_SENSOR,
						ARMS_MAX_TOUCH_SENSOR, 100));
		mouth = new LinearLimb(new EV3MediumRegulatedMotor(MOUTH_MOTOR_PORT),
				false, new DualBoundaryCalibration(50, 0, 30), 0, 100);
		eyes = new EV3UltrasonicSensor(EYES_PORT);
		final SampleProvider eyesSampleProvider = eyes.getDistanceMode();
		ultrasonicRangeFinder = new RangeFinderAdaptor(eyesSampleProvider);
	}

	public void calibrate() {
		mouth.calibrate();
		arms.calibrate();
	}

	public void openEyes() {
		eyes.enable();
	}

	public void closeEyes() {
		eyes.disable();
	}

	public void swingArms(final boolean immediateReturn) {
		switch (armsState) {
		case LEFT_UP:
			arms.moveToMax(immediateReturn);
			armsState = ArmsState.LEFT_DOWN;
			break;
		default:
			arms.moveToMin(immediateReturn);
			armsState = ArmsState.LEFT_UP;
		}
	}

	public void openMouth(final float intensity, final boolean immediateReturn) {
		mouth.moveToMax(immediateReturn);
	}

	public void closeMouth() {
		mouth.moveToMin(true);
	}

	public void sing() {
		openMouth(1, false);
		closeMouth();
	}

	public void reset() {
		arms.moveToCenter(false);
		mouth.moveToCenter(false);
		enableLEDPattern(false);
		closeEyes();
	}

	public void enableLEDPattern(final boolean enabled) {
		final int pattern = enabled ? 3 : 0;
		LocalEV3.get().getLED().setPattern(pattern);
	}

	public float getDistanceToEyes() {
		return ultrasonicRangeFinder.getRange();
	}

	public void move(final MoveDirection moveDirection,
			final boolean immediateReturn) {
		switch (moveDirection) {
		case FORWARD:
			leftLeg.rotate(MOVE_RANGE, true);
			rightLeg.rotate(MOVE_RANGE, immediateReturn);
			break;
		case LEFT:
			leftLeg.rotate(-MOVE_RANGE, true);
			rightLeg.rotate(MOVE_RANGE, immediateReturn);
			break;
		case RIGHT:
			leftLeg.rotate(MOVE_RANGE, true);
			rightLeg.rotate(-MOVE_RANGE, immediateReturn);
			break;
		default:
			leftLeg.rotate(-MOVE_RANGE, true);
			rightLeg.rotate(-MOVE_RANGE, immediateReturn);
		}
	}

	public void moveRandomly(final boolean immediateReturn) {
		// create random move direction
		final int randomEnumIndex = new Random()
				.nextInt(availableMoveDirections.length);
		final MoveDirection randomMoveDirection = availableMoveDirections[randomEnumIndex];
		// move
		move(randomMoveDirection, immediateReturn);
	}

	enum ArmsState {
		LEFT_UP, LEFT_DOWN, LEFT_MIDDLE, UNKNOWN
	}

	public enum MoveDirection {
		FORWARD, BACKWARD, LEFT, RIGHT
	}

}
