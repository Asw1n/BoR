package org.aswinmp.lejos.ev3.bandofrobots.musicians;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/** Calibrates the position limb by trying to detect a single physical boundary that prevents the limb to turn any further.
 * The second boundary is calculated by using the specified range
 *  
 * @author Aswin Bouwmeester
 *
 */




/**
 * @author Aswin Bouwmeester
 *
 */
public class SingleBoundaryCalibration implements CalibrationStrategy {
  private int range=0;
  private boolean forward;
  private int strechZone=0;
  private int speed;
  
  
  
  /** Main method is used for testing purposes only. It uses motor on port A
   * @param args
   */
  public static void main(final String[] args) {
    CalibrationStrategy test = new SingleBoundaryCalibration(true, 10, 50);
    RegulatedMotor m=new EV3LargeRegulatedMotor(MotorPort.A);
    LimbRange range=test.calibrate(m);
    System.out.println(String.format("Minimum tacho pos: %d", range.getMin()));
    System.out.println(String.format("Maximum tacho pos: %d", range.getMax()));
    m.setSpeed(100);
    m.rotateTo(range.getMin(), false);
    m.rotateTo(range.getMax(), false);
    m.close();
  }

  
  /** Constructor
   * @param direction
   * the direction the motor must move to find the boundary, true means forward, false means backward.
   * @param speed
   * The motor speed (expressed in degrees / second) to use when finding the boundary
   * @param range
   * The tacho range that the motor can freely travel
   * @param strechZone
   * The amount of tachoticks that seperates the intended boundary and the physical boundary. 
   * In this zone the construction is under strain that must be avoided during operation
   */
  public SingleBoundaryCalibration(boolean forward, int speed, int range, int strechZone) {
    this.forward=forward;
    this.speed=speed;
    this.range=range;
    this.strechZone=strechZone;
  }
  
  /** Constructor
   * @param direction
   * the direction the motor must move to find the boundary, true means forward, false means backward.
   * @param speed
   * The motor speed (expressed in degrees / second) to use when finding the boundary
   * @param range
   * The tacho range that the motor can freely travel
   */
  public SingleBoundaryCalibration(boolean forward, int speed,  int range) {
    this.forward=forward;
    this.speed=speed;
    this.range=range;
  }

  @Override
  public LimbRange calibrate(RegulatedMotor motor) {
    int minimum;
    int maximum;

    motor.setSpeed(speed);
    if (forward) {
      motor.forward();
    }
    else {
      motor.backward();
    }
    waitForStall(motor);
    motor.stop();
    if (forward) {
      maximum=motor.getTachoCount()-strechZone;
      minimum = maximum - range;
      if (strechZone !=0) {
        motor.rotateTo(maximum);
      }
    }
    else {
      minimum=motor.getTachoCount()+strechZone;
      maximum = minimum + range;
      if (strechZone !=0) {
        motor.rotateTo(maximum);
      }
    }
    return new LimbRange(minimum, maximum);
  }
  
  /** Detects a stall position. A stall is defined as not being able to move for 1 second. 
   * @param motor
   */
  protected void waitForStall(RegulatedMotor motor) {
    boolean stalled=false;
    int pos=0, newPos;
    long start= System.currentTimeMillis();
    while (!stalled) {
      newPos = motor.getTachoCount();
      if (pos != newPos) {
        start = System.currentTimeMillis();
        pos = newPos;
      } 
      else {
        if (System.currentTimeMillis() - start > 1000)
          stalled = true;
      }
      Delay.msDelay(10);
    }
  }


}
