package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.DualBoundaryCalibration;

public class Drumm3r {

	private static Port LEFT_HAND_MOTOR_PORT = MotorPort.C;
	private static Port RIGHT_HAND_MOTOR_PORT = MotorPort.B;
	private static Port RIGHT_FOOT_MOTOR_PORT = MotorPort.A;
	private static Port TORSO_MOTOR_PORT = MotorPort.D;

	private static final int TORSO_MIN = -2880;
	private static final int TORSO_MAX = 2880;

	private final Limb leftHand;
	private final Limb rightHand;
	// TODO this should be a Limb also once it supports circular movements
	private final EV3MediumRegulatedMotor rightFoot;
	// TODO this should be a Limb once un-calibrated limbs are supported
	private final EV3LargeRegulatedMotor torso;

	public Drumm3r() {
		// create and configure limbs
		leftHand = new LinearLimb(new EV3LargeRegulatedMotor(
				LEFT_HAND_MOTOR_PORT), false, new DualBoundaryCalibration(50),
				0, 100);
		leftHand.setSpeed(1.0f);
		rightHand = new LinearLimb(new EV3LargeRegulatedMotor(
				RIGHT_HAND_MOTOR_PORT), false, new DualBoundaryCalibration(50),
				0, 100);
		rightHand.setSpeed(1.0f);
		torso = new EV3LargeRegulatedMotor(TORSO_MOTOR_PORT);
		torso.setSpeed(torso.getMaxSpeed());
		rightFoot = new EV3MediumRegulatedMotor(RIGHT_FOOT_MOTOR_PORT);
		rightFoot.setSpeed(rightFoot.getMaxSpeed());

	}

	public void calibrate() {
		rightHand.calibrate();
		leftHand.calibrate();
	}

	public void drumLeft() {
		leftHand.moveToCenter(false);
		leftHand.moveToMax(true);
	}

	public void drumRight() {
		rightHand.moveToCenter(false);
		rightHand.moveToMax(true);
	}

	public void tap() {
		rightFoot.rotate(360);
	}

	public void moveTorsoTo(final TorsoLocation location) {
		switch (location) {
		case LEFT:
			torso.rotateTo(TORSO_MIN);
			break;
		case RIGHT:
			torso.rotateTo(TORSO_MAX);
			break;
		default:
			torso.rotateTo(0);
		}

	}

	public void reset() {
		rightHand.moveToCenter(false);
		leftHand.moveToCenter(false);
	}

	public enum TorsoLocation {
		LEFT, RIGHT, MIDDLE;
	}
}
