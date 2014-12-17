package org.aswinmp.lejos.ev3.bandofrobots.musicians;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.CalibrationStrategy;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.CalibrationStrategy.LimbRange;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleTouchSensorCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
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
  private int 				   currentTarget;
  private boolean              adaptToRhythm=true;
  private long                 lastTime=Long.MIN_VALUE;
  

  public boolean isAdaptToRhythm() {
	return adaptToRhythm;
}

public void setAdaptToRhythm(boolean adaptToRhythm) {
	this.adaptToRhythm = adaptToRhythm;
}

public float getGearRatio() {
	return gearRatio;
}

public void setGearRatio(float gearRatio) {
	this.gearRatio = gearRatio;
}

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

  public CircularLimb(final RegulatedMotor motor, boolean reverse,  CalibrationStrategy calibrater, int gearRatio) {
	this(motor,reverse,calibrater);
	this.gearRatio=gearRatio;
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
      return gearRatio ;
  }

  protected int toEncoder(float position) {
	  currentTarget += position * gearRatio;
	  return currentTarget;
  }

  protected float toPosition(int tacho) {
      return (motor.getTachoCount() % gearRatio) / gearRatio;
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
    moveTo(0, immediateReturn);
  }

  @Override
  public void moveToMax(boolean immediateReturn) {
    moveTo(0.5f, immediateReturn);

  }

  @Override
  public void moveToCenter(boolean immediateReturn) {
    moveTo(0.25f, immediateReturn);
  }

  @Override
  public void moveTo(float position, boolean immediateReturn) {
    int aim = (int) (currentTarget  + position * gearRatio);
    if (aim < currentTarget) aim+=gearRatio;
    if (aim> currentTarget+gearRatio) aim-=gearRatio;
    currentTarget=aim;
    motor.rotateTo(currentTarget, immediateReturn);
    if (adaptToRhythm) {
    	long current=System.currentTimeMillis();
    	if (current-lastTime<2000) {
    		motor.setSpeed((int) (1000*gearRatio/(current-lastTime)));
    	}
    	else motor.setSpeed((int) motor.getMaxSpeed());
    	lastTime=current;
    }
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
	  BrickLogger.error("The logical range of the circular limb is always 0 - 1");
  }

}
