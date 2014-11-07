package org.aswinmp.lejos.ev3.bandofrobots.musicians;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.CalibrationStrategy;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.CalibrationStrategy.LimbRange;

import lejos.robotics.RegulatedMotor;

/**
 * Represents a limb where a limb is driven by a rod. In other words, the motor
 * movement is transferred into a linear motion before moving the limb.
 * 
 * @author Aswin Bouwmeester, Matthias Paul Scholz
 *
 */
public class LinearLimb implements Limb {

  private float                logicalMaximum = 1;
  private float                logicalMinimum = 0;
  private final RegulatedMotor motor;
  private CalibrationStrategy  calibrater;
  private LimbRange            range = new LimbRange(0,1);
  private Boolean              reverse = false;
  

  /** Constructor. Logical range defaults to 0-1 and can be modified later.
  * @param motor
   * A regulated motor
   * @param reverse
   * Set to true if motor is reversed to limb direction
   * @param calibrater
   * Calibration strategy to align limb
   */
  public LinearLimb(final RegulatedMotor motor, boolean reverse,  CalibrationStrategy calibrater) {
    this.motor = motor;
    this.calibrater = calibrater;
    this.reverse=reverse;
  }

  /** Constructor that sets dynamic range of the limb.
   * @param motor
   * A regulated motor
   * @param reverse
   * Set to true if motor is reversed to limb direction
   * @param calibrater
   * Calibration strategy to align limb
   * @param logicalMin
   * User assigned minimum value of a limbs dynamic range
   * @param logicalMax
   * User assigned maximum value of a limbs dynamic range
   */
  public LinearLimb(final RegulatedMotor motor,  boolean reverse,
      CalibrationStrategy calibrater, final int logicalMin, final int logicalMax) {
    this(motor, reverse, calibrater);
    this.setMinimum(logicalMin);
    this.setMaximum(logicalMax);
  }
  
  
  /** Constructor that sets dynamic range of the limb.
   * @param motor
   * A regulated motor
   * @param reverse
   * Set to true if motor is reversed to limb direction
   * @param calibrater
   * Calibration strategy to align limb
   * @param logicalMin
   * User assigned minimum value of a limbs dynamic range
   * @param logicalMax
   * User assigned maximum value of a limbs dynamic range
   * @param tachoMin
   * Minimum value for the motor tacho to prevent over stretching of the limb
   * @param tachoMax
   * Maximum value for the motor tacho to prevent over stretching of the limb
   */
  public LinearLimb(final RegulatedMotor motor,  boolean reverse,
      CalibrationStrategy calibrater, final int logicalMin, final int logicalMax, final int tachoMin, final int tachoMax) {
    this(motor, reverse, calibrater, logicalMin, logicalMax);
    range = new LimbRange(tachoMin,tachoMax);
  }


  @Override
  public void setMinimum(float logicalMin) {
    this.logicalMinimum = logicalMin;
  }

  @Override
  public void setMaximum(float logicalMax) {
    this.logicalMaximum = logicalMax;
  }
  
  public float getMaximum() {
    return logicalMaximum;
  }

  public float getMinimum() {
    return logicalMinimum;
  }


  /** gives the scaling factor between limbs dynamic range and tacho's range 
   * @return
   */
  protected float getFactor() {
    if (!reverse)
      return ((float)range.getRange()) / (logicalMaximum - logicalMinimum);
    else 
      return -((float)range.getRange()) / (logicalMaximum - logicalMinimum);
  }

  protected int toEncoder(float position) {
    if (!reverse)
    return (int) ((float)range.getMin() + (position - logicalMinimum) * getFactor());
    else
      return (int) ((float)range.getMin() + (position - logicalMaximum) * getFactor());
  }

  protected float toPosition(int tacho) {
    if (!reverse)
      return logicalMinimum + (tacho - range.getMin()) / getFactor();
    else 
      return logicalMaximum + (tacho - range.getMin()) / getFactor();
  }

  @Override
  public void calibrate() {
    range=calibrater.calibrate(motor);
  }

  @Override
  public void setSpeed(float speed) {
    motor.setSpeed((int) (speed * motor.getMaxSpeed()));
  }

  @Override
  public void moveToMin(boolean immediateReturn) {
    moveTo(logicalMinimum, immediateReturn);
  }

  @Override
  public void moveToMax(boolean immediateReturn) {
    moveTo(logicalMaximum, immediateReturn);

  }

  @Override
  public void moveToCenter(boolean immediateReturn) {
    moveTo((logicalMaximum - logicalMinimum) / 2f, immediateReturn);
  }

  @Override
  public void moveTo(float position, boolean immediateReturn) {
    float target;
    if (range==null) throw new NullPointerException("Range not specified");
      if (position < logicalMinimum)
        target = logicalMinimum;
      else if (position > logicalMaximum)
        target = logicalMaximum;
      else
        target = position;
      motor.rotateTo(toEncoder(target), immediateReturn);
  }

  @Override
  public void rest() {
    motor.flt();
  }

  @Override
  public void setCalibrationStrategy(CalibrationStrategy calibrater) {
    this.calibrater = calibrater;
  }

  @Override
  public float getPosition() {
    if (range==null) throw new NullPointerException("Range not specified");
      return toPosition(motor.getTachoCount()) ;
  }

  @Override
  public float getTarget() {
    return toPosition(motor.getLimitAngle());
  }

  @Override
  public void setRange(float minimum, float maximum) {
    logicalMinimum=minimum;
    logicalMaximum=maximum;
  }

}
