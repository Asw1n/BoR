package org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration;

import lejos.robotics.RegulatedMotor;

public class ManualCalibration implements CalibrationStrategy {

  private int min;
  private int max;

  public ManualCalibration( int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public LimbRange calibrate(RegulatedMotor motor) {
    // TODO Auto-generated method stub
    return new LimbRange(min,max);
  }

}
