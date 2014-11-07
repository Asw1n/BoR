package org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
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
public class TouchSensorCalibration implements CalibrationStrategy {
  private int range=0;
  private boolean forward;
  private int strechZone=0;
  private int speed;
  private Port port;
  
  
  
  /** Main method is used for testing purposes only. It uses motor on port A
   * @param args
   */
  public static void main(final String[] args) {
    CalibrationStrategy test = new TouchSensorCalibration(SensorPort.S1, true, 10, 70);
    RegulatedMotor m=new EV3LargeRegulatedMotor(MotorPort.A);
    LimbRange range=test.calibrate(m);
    System.out.println(String.format("Minimum tacho pos: %d", range.getMin()));
    System.out.println(String.format("Maximum tacho pos: %d", range.getMax()));
    m.setSpeed(100);
    m.rotateTo(range.getMin(), false);
    m.rotateTo(range.getMax(), false);
    m.close();
  }

  

  public TouchSensorCalibration(Port port, boolean forward, int speed, int range, int strechZone) {
    this.forward=forward;
    this.speed=speed;
    this.range=range;
    this.strechZone=strechZone;
  }
  
  /** Constructor

   */
  public TouchSensorCalibration(Port port, boolean forward, int speed,  int range) {
    this.forward=forward;
    this.speed=speed;
    this.range=range;
    this.port=port;
  }

  @Override
  public LimbRange calibrate(RegulatedMotor motor) {
    int minimum;
    int maximum;
    int oldSpeed=motor.getSpeed();
    
    EV3TouchSensor pb = new EV3TouchSensor(port);
    SampleProvider sp = pb.getTouchMode();
    float[] sample=new float[sp.sampleSize()];

    motor.setSpeed(speed);
    if (forward) {
      motor.forward();
    }
    else {
      motor.backward();
    }
    do {
      Delay.msDelay(20);
      sp.fetchSample(sample, 0);
    } while (sample[0]==0);
    motor.stop();
    if (forward) {
      maximum=motor.getTachoCount()-strechZone;
      motor.rotateTo(maximum);
      minimum = maximum - range;
    }
    else {
      minimum=motor.getTachoCount()+strechZone;
      motor.rotateTo(minimum);
      maximum = minimum + range;
    }
    motor.setSpeed(oldSpeed);
    pb.close();
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
