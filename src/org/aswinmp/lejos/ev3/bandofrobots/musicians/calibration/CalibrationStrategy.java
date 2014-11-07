package org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration;

import lejos.robotics.RegulatedMotor;

/** Calibrates the position of a motor. After calibration the range of the motor is known. 
 * The range is expressed as a minimum and maximum encoder value.  
 * @author Aswin Bouwmeester
 *
 */
public interface CalibrationStrategy {

  /**
   * Calibrates the motor
   */
  public LimbRange calibrate(RegulatedMotor motor);

  
  public class LimbRange {
    int min;
    int max;

    public LimbRange(final int min, final int max) {
      this.min = min;
      this.max = max;
    }

    public int getMin() {
      return min;
    }

    public int getMax() {
      return max;
    }

    public int getRange() {
      return max - min;
    }

    public int getMiddle() {
      return min + (max - min) / 2;
    }

  }
}

