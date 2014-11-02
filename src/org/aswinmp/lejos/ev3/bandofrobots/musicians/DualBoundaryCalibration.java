package org.aswinmp.lejos.ev3.bandofrobots.musicians;


import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/** Calibrates the position of a limb by trying to detect upper and lower physical boundaries that prevent the limb to move any further.
 *  
 * @author Aswin Bouwmeester
 *
 */
public class DualBoundaryCalibration implements CalibrationStrategy {
  private int speed;
  private int lowerStrechZone;
  private int upperStrechZone;
  
  
  /** Main method is used for testing purposes only. It uses motor on port A
   * @param args
   */
  public static void main(final String[] args) {
    CalibrationStrategy test = new DualBoundaryCalibration( 10);
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
   * the direction the motor must rotate to find the boundary, true means forward, false means backward.
   * @param speed
   * The motor speed (expressed in degrees / second) to use when finding the boundary
   * @param range
   * The tacho range that the motor can freely travel
   * @param LowerstrechZone
   * The amount of tacho ticks that separates the intended lower boundary and the physical boundary. 
   * In this zone the construction is under strain that must be avoided during operation
   * @param UpperstrechZone
   * The amount of tacho ticks that separates the intended upper boundary and the physical boundary. 
   * In this zone the construction is under strain that must be avoided during operation
   */
  public DualBoundaryCalibration(int speed, int lowerStrechZone, int upperStrechZone) {
    this.speed=speed;
    this.lowerStrechZone=lowerStrechZone;
    this.upperStrechZone=upperStrechZone;
  }
  
  /** Constructor
   * @param direction
   * the direction the motor must move to find the boundary, true means forward, false means backward.
   * @param speed
   * The motor speed (expressed in degrees / second) to use when finding the boundary
   * @param range
   * The tacho range that the motor can freely travel
   */
  public DualBoundaryCalibration( int speed) {
    this.speed=speed;
  }

  @Override
  public LimbRange calibrate(RegulatedMotor motor) {
    int minimum;
    int maximum;

    motor.setSpeed(speed);
    motor.forward();
    waitForStall(motor);
    motor.stop();
    maximum=motor.getTachoCount()-upperStrechZone;
    motor.backward();
    Delay.msDelay(1000);
    waitForStall(motor);
    motor.stop();
    minimum=motor.getTachoCount()+lowerStrechZone;
    if (lowerStrechZone !=0) {
        motor.rotateTo(minimum);
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
      Delay.msDelay(100);
    }
  }
}
