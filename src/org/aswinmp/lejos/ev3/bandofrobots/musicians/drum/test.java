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
	    Limb leftShoulder = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.A), true, new SingleBoundaryCalibration(true,20,120,10));
	    Limb rightShoulder = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.D), false, new SingleBoundaryCalibration(false,20,120,10));

	    CircularLimb leftArm = new CircularLimb(new EV3MediumRegulatedMotor(MotorPort.B),false, new SingleTouchSensorCalibration(SensorPort.S1, true, 60, 0, 30),180, true);
	    CircularLimb rightArm = new CircularLimb(new EV3MediumRegulatedMotor(MotorPort.C),true, new SingleTouchSensorCalibration(SensorPort.S4,true,60, 0, 30),180, true);

    leftShoulder.calibrate();
    leftShoulder.moveToMax(false);
    rightShoulder.calibrate();
    rightShoulder.moveToMax(false);

    
    
    
    
    leftArm.adaptToRhythm(true);
    rightArm.adaptToRhythm(true);
		
		
		leftArm.setSpeed(1);
		rightArm.setSpeed(1);
		leftShoulder.setSpeed(1);
		rightShoulder.setSpeed(1);
		leftShoulder.setRange(0, 3);
		rightShoulder.setRange(0, 3);
		
		
		for (int j=0;j<3;j++) {
			leftShoulder.moveTo(j, true);
			rightShoulder.moveTo(j, true);
			for (int i=0;i<10;i++) {
				leftArm.moveTo(0, true);
				rightArm.moveTo(0, true);
				Delay.msDelay(200*j);
		}
		}
  leftArm.moveToMax(true);
  rightArm.moveToMax(true);
	leftShoulder.moveTo(0, true);
	rightShoulder.moveTo(0, false);
  Delay.msDelay(2000);
	}

}
