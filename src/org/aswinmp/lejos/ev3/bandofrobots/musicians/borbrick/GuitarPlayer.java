package org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import lejos.hardware.BrickFinder;
import lejos.hardware.LED;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class GuitarPlayer extends BaseMusician {
  Limb       rightHand, leftHand, foot, head;
  boolean    isUp   = true;
  boolean    isSet  = false;
  static int DIV    = 2;
  int countDown=0;

  static LED        led    = LocalEV3.get().getLED();
  boolean    footup = false;
  int        pos    = 1;
  private EV3UltrasonicSensor eyes;

  public static void main(String[] args) {
    Registry registry;
    final List<String> ips = getIPAddresses();
    // Use last IP address, which will be Wifi, it it exists
    String lastIp = null;
    for (final String ip : ips) {
      lastIp = ip;
//      System.out.println(ip);
    }
//    System.out.println("Setting java.rmi.server.hostname to " + lastIp);
//    System.setProperty("java.rmi.server.hostname", lastIp);
//
//    System.out.println("Starting RMI registry using port 1098");
    try {
      // Musician obj = new BaseMusician();
      final Musician obj = new GuitarPlayer();
      final Musician stub = (Musician) UnicastRemoteObject.exportObject(
          obj, 0);

      // Bind the remote object's stub in the registry
      registry = LocateRegistry.createRegistry(1098);
      registry.bind("Musician", stub);
      final TextLCD lcd = LocalEV3.get().getTextLCD();
      lcd.clear();
//      lcd.drawString("Musician waiting for conductor to connect", 0, 0);
//      System.err.println("Musician ready");
      led.setPattern(1);
    } catch (final Exception e) {
//      System.err.println("Musician exception: " + e.toString());
      e.printStackTrace();
    }   
  }
  

  public GuitarPlayer() {
    super();
    rightHand = new Limb(new EV3MediumRegulatedMotor(MotorPort.A), -40);
    leftHand = new Limb(new EV3MediumRegulatedMotor(MotorPort.B), -150);
    foot = new Limb(new EV3MediumRegulatedMotor(MotorPort.C), -30);
    head = new Limb(new EV3MediumRegulatedMotor(MotorPort.D), -45);
    head.setLogicRange(0, 127);
    rightHand.setLogicRange(-1, 1);
    setBeatPulseDevider(DIV);
    LCD.clear();


    head.calibrate(5, 1, -15, 0.2f);
    rightHand.calibrate(5, 1, -5, 0.2f);
    leftHand.calibrate(10, -1, 170, 0.2f);
    foot.calibrate(7, 1, -60, 0.2f);

    head.testRange();
    foot.testRange();
    leftHand.testRange();
    rightHand.testRange();
    
    eyes = new EV3UltrasonicSensor(BrickFinder.getDefault().getPort("S2"));
    eyes.disable();
    

  }
  

  @Override
  public void setDynamicRange(int lowestNote, int highestNote)
      throws RemoteException {
    leftHand.setLogicRange(lowestNote, highestNote);
  }

  @Override
  public void noteOn(int tone, int intensity) {
     super.noteOn(tone, intensity);
    if (!isSet) {
      rightHand.setSpeed(127f/intensity);
      // rightHand.setSpeed(1);
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
  public void voiceOn(int tone, int intensity) {
     super.voiceOn(tone, intensity);
     head.moveToPosition(intensity);

  }
  
  @Override
  public void voiceOff(int tone) {
     super.voiceOff(tone);
     head.moveToMin();
  }
  


  
public void start() {
  super.start();
  eyes.enable();
  led.setPattern(3);
}


  public void stop() {
    super.stop();
    head.setSpeed(0.2f);
    head.moveToMax();
    led.setPattern(1);
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
    eyes.disable();
    countDown=0;
  }

  @Override
  protected void beatPulse(int beatNo) {
    isSet=false;
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
        led.setPattern(2);
      } else {
        led.setPattern(0);
      }
    }

    // The leg goes up and down every quarter note
    if (beatNo * 2 % DIV == 0) {
      if (beatNo  % DIV == 0) {
        foot.moveToMin();
      } else {
        foot.moveToMax();
      }
    }
  }

  private class Limb {
    RegulatedMotor motor;
    int            range;
    private int    min;
    private double tickFactor = 0;
    private int max;

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
    
    private float toCircular(float value) {
      value = (value-min) / (max-min) / 2 -1;  // normalize to -1 to 1 
      value = (float) Math.toDegrees(Math.acos(1/value) ) ;
      return value;
    }

    public void moveToPosition(float value) {
      if (tickFactor != 0)
        motor.rotateTo(toTick(value), true);
        LCD.drawInt((int) value,8, 0, 0);
        LCD.drawInt((int) toCircular(value),8, 0, 1);
    }

    public void rest() {
      while (motor.isMoving())
        Thread.yield();
      motor.flt();
    }

  }
  
 
}
