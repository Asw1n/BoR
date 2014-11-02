package org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s;


import lejos.hardware.BrickFinder;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LineairLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.SingleBoundaryCalibration;

public class Blu3s  {
	Limb rightHand, leftHand, foot, head;
	Limb[] limbs ;
	boolean isUp = true;
	boolean isSet = false;

	static LED led = LocalEV3.get().getLED();
	private final EV3UltrasonicSensor eyes;



	public Blu3s() {
		super();
		rightHand = new LineairLimb(new EV3MediumRegulatedMotor(MotorPort.A), new SingleBoundaryCalibration(true, 5, 50, 10));
		leftHand = new LineairLimb(new EV3MediumRegulatedMotor(MotorPort.B), new SingleBoundaryCalibration(true, 5, 170, 10));
		foot = new LineairLimb(new EV3MediumRegulatedMotor(MotorPort.C), new SingleBoundaryCalibration(true, 5, 60, 10));
		head = new LineairLimb(new EV3MediumRegulatedMotor(MotorPort.D), new SingleBoundaryCalibration(true, 5, 45, 10));
		limbs = new Limb[]{rightHand, leftHand, head, foot};
		head.setRange(0, 127);
		rightHand.setRange(-1, 1);
		LCD.clear();
		head.calibrate();
		rightHand.calibrate();
		leftHand.calibrate();
		foot.calibrate();

		// head.calibrate(5, 1, -15, 0.2f);
		// rightHand.calibrate(5, 1, -5, 0.2f);
		// leftHand.calibrate(10, -1, 170, 0.2f);
		// foot.calibrate(7, 1, -60, 0.2f);

		leftHand.setSpeed(1);
		rightHand.setSpeed(1);


		eyes = new EV3UltrasonicSensor(BrickFinder.getDefault().getPort("S2"));
		eyes.disable();

	}


	public void setGuitarRange(final int lowestNote, final int highestNote){
		leftHand.setRange(lowestNote, highestNote);
	}

	public void play(final int tone, final int intensity) {
		// the right hand moves up or down on a note. It changes direction every
		// quarter note.
		if (!isSet) {
			rightHand.setSpeed(127f / intensity);
			if (isUp) {
				rightHand.moveToMin(true);
			} else {
				rightHand.moveToMax(true);
			}
			isUp = !isUp;
			isSet = true;
		}
		// The left hand moves full speed to the position
		leftHand.setSpeed(1);
		leftHand.moveTo(tone, true);
	}

	public void openMouth(final float f) {
		// The mouth opens when singing according to intensity of the note.
		head.moveTo(f, true);
	}
	
	public void closeMouth() {
	  head.moveTo(0, true);
	}

	public void voiceOff(final int tone) {
		head.moveToMin(true);
	}


  public void openEyes() {
    eyes.enable();
  }

  public void switchAmpOn() {
    led.setPattern(3);
    
  }

  public void restLimbs() {
    for (Limb limb : limbs) {
      limb.moveToMin(false);
      limb.rest();
    }
  }

  public void closeEyes() {
    eyes.disable();
    
  }

  public void switchAmpOff() {
    led.setPattern(0);
  }

  public void tapFoot() {
    foot.setSpeed(1);
    foot.moveToMin(true);
    
  }

  public void raiseFoot() {
    foot.setSpeed(0.1f);
    foot.moveToMax(true);
    
  }

  public void prepareRightHand() {
    isSet=false;
  }


  public void calibrateLimbs() {
    for (Limb limb : limbs) {
      limb.calibrate();
      limb.moveToMin(true);
    }
  }

}
