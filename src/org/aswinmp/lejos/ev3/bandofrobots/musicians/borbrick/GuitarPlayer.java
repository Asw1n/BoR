package org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick;

import java.rmi.RemoteException;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.LED;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class GuitarPlayer extends BaseMusician {
  Limb       rightHand, leftHand, foot, head;
  boolean    isUp   = true;
  boolean    isSet  = false;
  static int DIV    = 2;
  int countDown=0;

  LED        led    = LocalEV3.get().getLED();
  boolean    footup = false;
  int        pos    = 1;
  private Heartbeat heartbeat;

  public static void main(String[] args) {
    GuitarPlayer jimi = new GuitarPlayer();
  }

  public GuitarPlayer() {
    super();
    rightHand = new Limb(new EV3MediumRegulatedMotor(MotorPort.A), -50);
    leftHand = new Limb(new EV3MediumRegulatedMotor(MotorPort.B), -150);
    foot = new Limb(new EV3MediumRegulatedMotor(MotorPort.C), -30);
    head = new Limb(new EV3MediumRegulatedMotor(MotorPort.D), -45);
    head.setLogicRange(0, 1);
    rightHand.setLogicRange(-1, 1);
    setBeatPulseDevider(DIV);
    LCD.clear();


    head.calibrate(5, 1, -15, 0.2f);
    rightHand.calibrate(5, 1, -10, 0.2f);
    leftHand.calibrate(10, -1, 170, 0.2f);
    foot.calibrate(7, 1, -60, 0.2f);

    head.testRange();
    foot.testRange();
    leftHand.testRange();
    rightHand.testRange();
    
    heartbeat = new Heartbeat();
    heartbeat.setDaemon(true);
    heartbeat.start();
  }

  @Override
  public void setDynamicRange(int lowestNote, int highestNote)
      throws RemoteException {
    leftHand.setLogicRange(lowestNote, highestNote);
  }

  @Override
  public void noteOn(int tone) {
    // super.noteOn(tone);
    if (!isSet) {
      rightHand.setSpeed(0.5f);
      if (isUp) {
        rightHand.moveToMin();
      } else {
        rightHand.moveToMax();
      }
      isUp = !isUp;
      isSet = true;
    }
    leftHand.setSpeed(1);
    leftHand.moveToPosition(tone);
  }

  @Override
  public void noteOff(int tone) {
    // super.noteOff(tone);
    // rightHand.setSpeed(.1f);
    // rightHand.min();
    // leftHand.setSpeed(0.1f);
    // leftHand.min();
  }

  public void stop() {
    super.stop();
    head.setSpeed(0.2f);
    head.moveToMax();
    led.setPattern(0);
    leftHand.setSpeed(0.2f);
    leftHand.moveToMax();
    rightHand.setSpeed(0.2f);
    rightHand.moveToMin();
    foot.setSpeed(0.1f);
    foot.moveToMin();
    Delay.msDelay(1200);
    head.moveToMin();
    leftHand.rest();
    rightHand.rest();
    head.rest();
    foot.rest();
    countDown=0;
  }

  @Override
  protected void beatPulse(int beatNo) {
    if (countDown< 4 *DIV+1) {
      if (beatNo * 2 % DIV == 0) {
        if (beatNo  % DIV == 0) {
          head.moveToMax();
        } else {
          head.moveToMin();
        }
      }
      countDown++;
    }
    
    
    
    // The left arm may change direction every DIV * quarter note
    isSet = false;

    // Leds go on on 1 and 3 and off at 2 and 4
    if (beatNo % DIV == 0) {
      if (beatNo / DIV % 2 == 0) {
        led.setPattern(3);
      } else {
        led.setPattern(0);
      }
    }

    // The leg goes up and down every quarter note
    if (beatNo * 2 % DIV == 0) {
      if (beatNo  % DIV == 0) {
        foot.moveToMax();
      } else {
        foot.moveToMin();
      }
    }
  }

  private class Limb {
    RegulatedMotor motor;
    int            range;
    private int    min;
    private int    max;
    private double tickFactor = 0;

    protected Limb(RegulatedMotor motor, int range) {
      this.motor = motor;
      this.range = range;
    }

    public void setSpeed(float speedFactor) {
      motor.setSpeed((int) (motor.getMaxSpeed() * speedFactor));

    }

    /**
     * @param stallDetectionSpeed
     * @param stallDirection
     * @param stallToRest
     * @param speedFactor
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
      Sound.beep();
      motor.stop();
      motor.resetTachoCount();
      motor.setSpeed((int) (motor.getMaxSpeed() * speedFactor));
      motor.rotateTo(stallToRest);
      motor.resetTachoCount();

    }

    public void moveToMin() {
      motor.rotateTo(0, true);
    }

    public void moveToMax() {
      motor.rotateTo(range, true);
    }
    
    public void testRange() {
      moveToMin();
      while(motor.isMoving());
      moveToMax();
      while(motor.isMoving());
      moveToMin();
      while(motor.isMoving());
      moveToMax();
      while(motor.isMoving());
      moveToMin();
      while(motor.isMoving());
    }

    public void setLogicRange(int min, int max) {
      this.min = min;
      this.max = max;
      this.tickFactor = range / (double) (max - min);
    }

    private int toTick(float value) {
      return (int) ((value - min) * tickFactor);
    }

    private int getLogicPosition() {
      return (int) (min + motor.getTachoCount() / tickFactor);
    }

    public void moveToPosition(float value) {
      if (tickFactor != 0)
        motor.rotateTo(toTick(value), true);
    }

    public void rest() {
      while (motor.isMoving())
        Thread.yield();
      motor.flt();
    }

  }
  
  private class Heartbeat extends Thread {

    
    public void run() {
      while(true) {
        head.setSpeed(0.1f);
        head.moveToPosition(0.5f);
        Delay.msDelay(800);
        head.setSpeed(0.1f);
        head.moveToPosition(0f);
        Delay.msDelay(1700);
      }
      
      
    }
    
  }

}
