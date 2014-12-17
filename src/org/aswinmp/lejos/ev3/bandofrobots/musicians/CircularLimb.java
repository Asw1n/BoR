package org.aswinmp.lejos.ev3.bandofrobots.musicians;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.CalibrationStrategy;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.CalibrationStrategy.LimbRange;

import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;

/**
 * Represents a limb where a limb is driven by a rod. In other words, the motor
 * movement is transferred into a linear motion before moving the limb.
 * 
 * @author Aswin Bouwmeester, Matthias Paul Scholz
 *
 */
public class CircularLimb implements Limb {

  private float                logicalMaximum = 1;
  private float                logicalMinimum = 0;
  private final RegulatedMotor motor;
  private CalibrationStrategy  calibrater;
  private float				   gearRatio = 360;
  

  /** Constructor. Logical range defaults to 0-1 and can be modified later.
  * @param motor
   * A regulated motor
   * @param reverse
   * Set to true if motor is reversed to limb direction
   * @param calibrater
   * Calibration strategy to align limb
   */
  public CircularLimb(final RegulatedMotor motor, boolean reverse,  CalibrationStrategy calibrater) {
	  if (reverse) 
		  this.motor=MirrorMotor.invertMotor(motor);
	  else 
		  this.motor = motor;
    this.calibrater = calibrater;
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
      return gearRatio / (logicalMaximum - logicalMinimum);
  }

  protected int toEncoder(float position) {
      return (int) ((int)(motor.getTachoCount()/getFactor()) + position * getFactor());
  }

  protected float toPosition(int tacho) {
      return (motor.getTachoCount() % getFactor()) / getFactor();
  }

  @Override
  public void calibrate() {
    calibrater.calibrate(motor);
    motor.resetTachoCount();
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
    moveTo(logicalMinimum+(logicalMaximum - logicalMinimum) / 2f, immediateReturn);
  }

  @Override
  public void moveTo(float position, boolean immediateReturn) {
    float target;
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
