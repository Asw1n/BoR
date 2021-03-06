package org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s;


import lejos.hardware.BrickFinder;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleBoundaryCalibration;

public class Blu3s  {
  static int[] strings = new int[]{40, 45, 50, 55 , 59, 64};
	Limb rightHand, leftHand, foot, head;
	Limb[] limbs ;
	boolean isUp = true;
	boolean isSet = false;

	static LED led = LocalEV3.get().getLED();
	private final EV3UltrasonicSensor eyes;



	public Blu3s() {
		super();
		rightHand = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.A), true, new SingleBoundaryCalibration(true, 15, 20, 30));
    rightHand.setRange(-1, 1);
    rightHand.setSpeed(1);

    leftHand = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.B), true, new SingleBoundaryCalibration(false, 15, 140, 20));
    leftHand.setRange(0, 1);
    leftHand.setSpeed(1);

    foot = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.C), false, new SingleBoundaryCalibration(true, 10, 15, 50));
    foot.setRange(0, 1);
    foot.setSpeed(1);

		head = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.D), true, new SingleBoundaryCalibration(true, 10, 40, 15));
		head.setRange(0, 127);
		head.setSpeed(1);



    limbs = new Limb[]{rightHand, leftHand, head, foot};

		eyes = new EV3UltrasonicSensor(BrickFinder.getDefault().getPort("S2"));

	}
	
	private int toFret(int tone) {
	  for (int x=strings.length-1; x>=0; x--) {
	    if (tone >= strings[x]) return tone - strings[x];
	  }
    return 0;
	}

	 private int toGuitarString(int tone) {
	    for (int x=strings.length-1; x>=0; x--) {
	      if (tone >= strings[x]) return x;
	    }
	    return 0;
	  }

	

	public void setGuitarRange(final int lowestNote, final int highestNote){
		leftHand.setRange(lowestNote, highestNote);
		leftHand.setRange(40,82);
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
		//leftHand.setSpeed(1);
		leftHand.moveTo(tone, true);
	}
	

	public void openMouth(final float f) {
		// The mouth opens when singing according to intensity of the note.
	  head.setSpeed(0.3f);
		head.moveTo(f, true);
	}
	
	public void closeMouth() {
    head.setSpeed(0.1f);
	  head.moveTo(0, true);
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
    leftHand.moveToMax(false);
    leftHand.rest();
  }

  public void closeEyes() {
    eyes.disable();
    
  }

  public void switchAmpOff() {
    led.setPattern(0);
  }

  public void tapFoot() {
    foot.setSpeed(0.3f);
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
      limb.moveToMin(false);
    }
  }
  
  public void testLeftHand() {
    leftHand.setSpeed(1);
    leftHand.moveToMin(false);
    leftHand.moveToMax(false);
    leftHand.moveToMin(false);
    leftHand.moveToMax(false);
    leftHand.moveToMin(false);
    leftHand.moveToMax(false);
    leftHand.moveToMin(false);
    leftHand.moveToMax(false);
    leftHand.moveToMin(false);
    leftHand.moveToMax(false);
  }

}
