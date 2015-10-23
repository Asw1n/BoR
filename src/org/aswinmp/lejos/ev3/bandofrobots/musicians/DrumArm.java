package org.aswinmp.lejos.ev3.bandofrobots.musicians;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.CalibrationStrategy;

public class DrumArm extends LinearLimb {
  private Monitor arm;
  

  public DrumArm(RegulatedMotor motor, boolean reverse, CalibrationStrategy calibrater) {
    super(motor, reverse, calibrater);
    arm=new Monitor();
    arm.start();
  }

  public DrumArm(RegulatedMotor motor, boolean reverse, CalibrationStrategy calibrater, int logicalMin, int logicalMax) {
    super(motor, reverse, calibrater, logicalMin, logicalMax);
    arm=new Monitor();
    arm.start();
  }

  public DrumArm(RegulatedMotor motor, boolean reverse, CalibrationStrategy calibrater, int logicalMin, int logicalMax, int tachoMin,
      int tachoMax) {
    super(motor, reverse, calibrater, logicalMin, logicalMax, tachoMin, tachoMax);
    arm=new Monitor();
    arm.start();
  }
  
  private class Monitor extends Thread {
    public boolean more = true;

    public Monitor() {
      setDaemon(true);
    }

    public synchronized void run() {
      while (more) {
        if (getPosition()>0.95 && !motor.isMoving()) 
          moveToMin(false);
        Delay.msDelay(20);
      }
    }
  }
  
  
  

}
