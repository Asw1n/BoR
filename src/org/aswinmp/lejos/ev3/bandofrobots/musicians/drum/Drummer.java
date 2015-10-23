package org.aswinmp.lejos.ev3.bandofrobots.musicians.drum;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.CircularLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.DrumArm;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.ManualCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleTouchSensorCalibration;

public class Drummer {
  static int SHOULDERRANGE=8;
  Limb leftShoulder, rightShoulder, leftArm, rightArm;
  Limb[] limbs;
  
  public Drummer() {
    leftShoulder  = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.A), true,  new SingleBoundaryCalibration(true,20,210,20));
    rightShoulder = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.D), false, new SingleBoundaryCalibration(false,20,210,20));

    leftArm = new DrumArm(new EV3MediumRegulatedMotor(MotorPort.B),false, new ManualCalibration( 0, 60));
    rightArm = new DrumArm(new EV3MediumRegulatedMotor(MotorPort.C),true, new ManualCalibration( 0, 60));

    limbs = new Limb[] {leftShoulder, rightShoulder, leftArm, rightArm};
    
    leftArm.setSpeed(1);
    rightArm.setSpeed(1);
    leftShoulder.setSpeed(1);
    rightShoulder.setSpeed(1);
    leftShoulder.setRange(0, SHOULDERRANGE);
    rightShoulder.setRange(0, SHOULDERRANGE);
    leftArm.setRange(0, 1);
    rightArm.setRange(0, 1);
  }
  
  public void calibrateLimbs() {
//    leftShoulder.calibrate();
//    leftShoulder.moveToMax(false);
//    leftShoulder.moveToMin(false);
//    rightShoulder.calibrate();
//    rightShoulder.moveToMax(false);
//    rightShoulder.moveToMin(false);
    leftArm.calibrate();
    rightArm.calibrate();
  }

  public void restLimbs() {
    for (Limb limb : limbs) {
      limb.moveToMin(false);
      limb.rest();
    }
  }


  public void playHighTom() {
    leftShoulder.moveTo(0, true);
    leftArm.moveTo(1, true);
  }
  
  public void playFloorTom() {
    rightShoulder.moveTo(1, true);
    rightArm.moveTo(1, true);
  }

  public void playMidTom() {
    rightShoulder.moveTo(2, true);
    rightArm.moveTo(1, true);
  }

  public void playSnare() {
    rightShoulder.moveTo(0, true);
    rightArm.moveTo(1, true);
  }


  public void playHiHat() {
    leftShoulder.moveTo(1, true);
    leftArm.moveTo(1, true);
  }

  public void playBase() {
  }



  public void playSymbal() {
    leftShoulder.moveTo(2, true);
    leftArm.moveTo(1, true);
  }
  
  public void testShoulders() {
    for (int i =0;i<=SHOULDERRANGE;i++) {
      leftShoulder.moveTo(i, false);
      Delay.msDelay(500);
    }
    leftShoulder.moveToCenter(false);
    for (int i =0;i<=SHOULDERRANGE;i++) {
      rightShoulder.moveTo(i, false);
      Delay.msDelay(500);
    }
    rightShoulder.moveToCenter(false);
    
  }

  public void testHands() {
    for (int i=0; i<10;i++) {
      leftArm.moveToMax(false);
      Delay.msDelay(100);
//      leftArm.moveToMin(false);
    }
    
  }

}
