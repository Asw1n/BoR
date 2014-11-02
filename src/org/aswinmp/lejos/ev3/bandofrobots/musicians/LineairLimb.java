package org.aswinmp.lejos.ev3.bandofrobots.musicians;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.CalibrationStrategy.LimbRange;

import lejos.robotics.RegulatedMotor;

/**
 * Represents a limb where a limb is driven by a rod. In other words, the motor
 * movement is transferred into a liner motion before moving the limb.
 * 
 * @author Aswin Bouwmeester, Matthias Paul Scholz
 *
 */
public class LineairLimb implements Limb {

  private float                logicalMaximum = 1;
  private float                logicalMinimum = 0;
  private final RegulatedMotor motor;
  private CalibrationStrategy  calibrater;
  private LimbRange            range;

  /** Constructor. Logical range defaults to 0-1 and can be modified later.
   * @param motor
   * @param calibrater
   */
  public LineairLimb(final RegulatedMotor motor, CalibrationStrategy calibrater) {
    this.motor = motor;
    this.calibrater = calibrater;
  }

  /** Constructor that sets logical range of the limb.
   * @param motor
   * @param calibrater
   * @param logicalMin
   * @param logicalMax
   */
  public LineairLimb(final RegulatedMotor motor,
      CalibrationStrategy calibrater, final int logicalMin, final int logicalMax) {
    this.motor = motor;
    this.calibrater = calibrater;
    this.setMinimum(logicalMin);
    this.setMaximum(logicalMax);
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


  /** gives the scaling factor between logical range and tacho range 
   * @return
   */
  protected float getFactor() {
    if (range==null) throw new NullPointerException("Range not specified");
    return (range.getMax() - range.getMin()) / (logicalMaximum - logicalMinimum);
  }

  protected int toEncoder(float position) {
    if (range==null) throw new NullPointerException("Range not specified");
    return (int) (range.getMin() + (position - logicalMinimum) * getFactor());
  }

  protected float toPosition(int tacho) {
    return logicalMinimum + (tacho - range.getMin()) / getFactor();
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
