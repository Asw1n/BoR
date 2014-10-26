package org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick;

import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;



/** The Limb class represents a limb of a humanoid robot that is powered by a single regulated motor
 * 
 * @author Aswin Bouwmeester
 *
 */
public class Limb {

  RegulatedMotor motor;
  int            range;
  private int    min;
  private double tickFactor = 0;
  private int    max;
  private int    target=0;

  /** Constructor of the limb class 
   * @param motor
   * A regulated motor object
   * @param range
   * The maximum range (in encoder ticks) that the motor is supposed to make
   */
  public Limb(RegulatedMotor motor, int range) {
    this.motor = motor;
    this.range = range;
  }

  /** Sets the speed of the motor as a fraction (0-1) of the maximum speed
   * @param speedFactor
   */
  public void setSpeed(float speedFactor) {
    motor.setSpeed((int) (motor.getMaxSpeed() * speedFactor));

  }

  /** Calibrates the position of the motor by slowly moving it to an end point. Also sets the tacho counter to zero after calibrating.
   * @param stallDetectionSpeed
   * The speed of the motor (in degrees/second) that must by used while calibrating
   * @param stallDirection
   * The direction the motor must rotate to while calibrating (1=forward, -1=backward) 
   * @param stallToRest
   * The number of ticks the motor must rotate back from the detected stall position. 
   * It is assumed that the stall position over stretches the robot. 
   * Rotating a bit back from the stall position releases tension of the robot.
   * The tacho is reset after rotating back from the stall position
   * @param speedFactor
   * Sets the speed after calibrationg to this value. Expressed a a fraction of the maximum speed.
   */
  public void calibrate(int stallDetectionSpeed, int stallDirection,
      int stallToRest, float speedFactor) {
    boolean stalled = false;
    int pos = motor.getTachoCount();
    int newPos;
    long start = System.currentTimeMillis();
    motor.setSpeed(stallDetectionSpeed);
    if (stallDirection > 0)
      motor.forward();
    else
      motor.backward();
    while (!stalled) {
      newPos = motor.getTachoCount();
      if (pos != newPos) {
        start = System.currentTimeMillis();
        pos = newPos;
      } else {
        if (System.currentTimeMillis() - start > 1000)
          stalled = true;
      }
      Delay.msDelay(10);
    }
    motor.stop();
    motor.resetTachoCount();
    motor.setSpeed((int) (motor.getMaxSpeed() * speedFactor));
    motor.rotateTo(stallToRest);
    motor.resetTachoCount();

  }

  /** Rotates the limb to its minimum position
   * 
   */
  public void moveToMin() {
    moveTo(min);
  }

  /** Rotates the limb to its maximum position
   * 
   */
  public void moveToMax() {
    moveTo(max);
  }

  /** Moves the limb from minimum to maximum and back twice
   * 
   */
  public void testRange() {
    motor.rotateTo(0, false);
    motor.rotateTo(range, false);
    motor.rotateTo(0, false);
    motor.rotateTo(range, false);
  }

  /** Sets a logical range for the limb. The logical range is used to hide the tacho range of the motor.   
   * @param min
   * The minimum value of the logical range
   * @param max
   * The maximum value of the logical range
   */
  public void setLogicRange(int min, int max) {
    this.min = min;
    this.max = max;
    this.tickFactor = range / (double) (max - min);
  }

  /** Convert a logical value to a tacho position
   * @param value
   * @return
   */
  private int toTick(float value) {
    return (int) ((value - min) * tickFactor);
  }

  /** Converts a tacho position to a logical value
   * @return
   */
  public int getPosition() {
    return (int) (min + motor.getTachoCount() / tickFactor);
  }

  /** Converts a linear value to a circular value.
   * @param value
   * @return
   */
  @SuppressWarnings("unused")
  private float toCircular(float value) {
    // TODO: finish developing this method
    value = (value - min) / (max - min) / 2 - 1; // normalize to -1 to 1
    value = (float) Math.toDegrees(Math.acos(1 / value));
    return value;
  }

  /** <oves the limb to a logical position
   * @param value
   * Value is not tested for out of range condition
   */
  public void moveTo(float value) {
    if (tickFactor != 0) {
      target=(int) value;
      motor.rotateTo(toTick(target), true);
    }
  }

  /**
   * Rests the limb and puts it motor into float
   */
  public void rest() {
    while (motor.isMoving())
      Thread.yield();
    motor.flt();
  }

  public int getMaximum() {
    return max;
  }

  public int getMinimum() {
    return min;
  }

  public int getTarget() {
    return target;
  }

}
