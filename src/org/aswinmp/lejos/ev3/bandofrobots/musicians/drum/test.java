package org.aswinmp.lejos.ev3.bandofrobots.musicians.drum;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.CircularLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleTouchSensorCalibration;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    Limb leftShoulder = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.A), true, new SingleBoundaryCalibration(true,20,90,10));
	    Limb rightShoulder = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.D), false, new SingleBoundaryCalibration(false,20,90,10));

		Limb leftArm = new CircularLimb(new EV3MediumRegulatedMotor(MotorPort.B),false, new SingleTouchSensorCalibration(SensorPort.S1,false,20,0,0),180);
		Limb rightArm = new CircularLimb(new EV3MediumRegulatedMotor(MotorPort.C),true, new SingleTouchSensorCalibration(SensorPort.S2,true,20,0,0),180);

		
		leftArm.setSpeed(1);
		rightArm.setSpeed(1);
		leftShoulder.setSpeed(1);
		rightShoulder.setSpeed(1);
		leftShoulder.calibrate();
		rightShoulder.calibrate();
		leftShoulder.setRange(1, 3);
		rightShoulder.setRange(1, 3);
		for (int j=1;j<4;j++) {
			leftShoulder.moveTo(j, true);
			rightShoulder.moveTo(j, true);
			for (int i=0;i<10;i++) {
				leftArm.moveTo(1, true);
				rightArm.moveTo(1, true);
				Delay.msDelay(200*j);
		}
		}
	leftShoulder.moveTo(1, true);
	rightShoulder.moveTo(1, false);
	}

}
