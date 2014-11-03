package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.DualBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;

public class Drumm3r {

	private static Port LEFT_HAND_MOTOR_PORT = MotorPort.C;
	private static Port RIGHT_HAND_MOTOR_PORT = MotorPort.B;
	private static Port RIGHT_FOOT_MOTOR_PORT = MotorPort.A;
	private static Port TORSO_MOTOR_PORT = MotorPort.D;

	private final Limb leftHand;
	private final Limb rightHand;
	// TODO this should be a Limb also once it supports circular movements
	private final EV3MediumRegulatedMotor rightFoot;
	private final Limb torso;

	public Drumm3r() {
		// create and configure limbs
		leftHand = new LinearLimb(
		    new EV3LargeRegulatedMotor(LEFT_HAND_MOTOR_PORT), 
		    false, 
		    new DualBoundaryCalibration(5),0, 100);
		leftHand.setSpeed(1.0f);
		rightHand = new LinearLimb(
				new EV3LargeRegulatedMotor(RIGHT_HAND_MOTOR_PORT),
				false,
				new DualBoundaryCalibration(5), 0, 100);
		rightHand.setSpeed(1.0f);
		torso = new LinearLimb(
		    new EV3LargeRegulatedMotor(TORSO_MOTOR_PORT), 
		    false,
		    new DualBoundaryCalibration(5),-100, 100);
		torso.setSpeed(1.0f);
		rightFoot = new EV3MediumRegulatedMotor(RIGHT_FOOT_MOTOR_PORT);
		rightFoot.setSpeed(rightFoot.getMaxSpeed());

	}

	public void calibrate() {
		rightHand.calibrate();
		leftHand.calibrate();
	}

	public void drumLeft() {
		leftHand.moveToMax(false);
		leftHand.moveToMin(false);
	}

	public void drumRight() {
		rightHand.moveToMax(false);
		rightHand.moveToMin(false);
	}

	public void tap() {
		rightFoot.rotate(360);
	}

	public void moveTorsoTo(final TorsoLocation location) {
		switch (location) {
		case LEFT:
			torso.moveToMin(false);
			break;
		case RIGHT:
			torso.moveToMax(false);
			break;
		default:
			torso.moveTo(0, false);
		}

	}

	public enum TorsoLocation {
		LEFT, RIGHT, MIDDLE;
	}
}
