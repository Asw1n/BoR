package org.aswinmp.lejos.ev3.bandofrobots.musicians.drum;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.CircularLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleTouchSensorCalibration;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Limb left = new CircularLimb(new EV3MediumRegulatedMotor(MotorPort.C),false, new SingleTouchSensorCalibration(SensorPort.S1,false,20,0,0));
		left.setRange(0, 3);
		for (int i=0;i<40;i++) {
			left.moveTo(i % 4, false);
			Sound.beep();
		}
	}

}
