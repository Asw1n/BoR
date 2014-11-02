package org.aswinmp.lejos.ev3.bandofrobots.musicians;

/** Represents a limb with a single motor driven joint. 
 * 
 * @author Aswin Bouwmeester
 *
 */
public interface Limb {
  
 
  
  /** Set the calibration strategy to be used for obtaining the right position of the limb.
   * @param calibrater
   */
  public void setCalibrationStrategy(CalibrationStrategy calibrater);
  
  /**
   * Positions the limb and sets the zero position of the motor encoder
   */
  public void calibrate();
  
  /**
   * Sets the motor speed of the limb as a fraction of the maximum motor speed
   */
  public void setSpeed(float speed);
  
  /**
   * Moves the limb to its minimum extreme position
   */
  public void moveToMin(boolean immediateReturn);
  
  /**
   * Moves the limb to its maximum extreme position
   */
  public void moveToMax(boolean immediateReturn);
  
  /**
   * Moves the limb to its center position
   * @param immideateReturn
   */
  public void moveToCenter(boolean immediateReturn);
  
  /** Moves the limb to a logical position
   * @param position
   * The desired position to the limb  expressed in the same range as the limbs minimum and maximum positions
   */
  public void moveTo(float position, boolean immediateReturn);
  
  /** Stops any movement of the limb and sets its motor into float
   */
  public void rest();
  
  /** Defines the logical minimum position of a limb
   * @param logicalMin
   */
  public void setMinimum(float Minimum);

  /** Defines the maximum position of a limb
   * @param maximum
   */
  public void setMaximum(float maximum);
  
  /** Returns the minimum position of a limb
   */
  public float getMinimum( );

  /** Returns the maximum position of a limb
   * @param maximum
   */
  public float getMaximum( );

  
  
  /** Defines the range of a limb
   * @param minimum
   * @param maximum
   */
  public void setRange(float minimum, float maximum);
  

/** Gives the current position of the limb
 * @return
 */
public float getPosition();

/** Gives the target position of the limb
 * @return
 */
public float getTarget();
  

}
