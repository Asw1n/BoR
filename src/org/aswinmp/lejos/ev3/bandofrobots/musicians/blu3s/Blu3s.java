package org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick.BaseMusician;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick.Musician;

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

public class Blu3s extends BaseMusician {
  Limb                        rightHand, leftHand, foot, head;
  boolean                     isUp      = true;
  boolean                     isSet     = false;
  int                         countDown = 0;

  static LED                  led       = LocalEV3.get().getLED();
  boolean                     footup    = false;
  int                         pos       = 1;
  private EV3UltrasonicSensor eyes;

  public static void main(String[] args) {
    Registry registry;
    final List<String> ips = getIPAddresses();
    // Use last IP address, which will be Wifi, it it exists
    String lastIp = null;
    for (final String ip : ips) {
      lastIp = ip;
      // System.out.println(ip);
    }
    // System.out.println("Setting java.rmi.server.hostname to " + lastIp);
    System.setProperty("java.rmi.server.hostname", lastIp);
    //
    // System.out.println("Starting RMI registry using port 1098");
    try {
      // Musician obj = new BaseMusician();
      final Musician obj = new Blu3s();
      final Musician stub = (Musician) UnicastRemoteObject.exportObject(obj, 0);

      // Bind the remote object's stub in the registry
      registry = LocateRegistry.createRegistry(1098);
      registry.bind("Musician", stub);
      final TextLCD lcd = LocalEV3.get().getTextLCD();
      lcd.clear();
      // lcd.drawString("Musician waiting for conductor to connect", 0, 0);
      // System.err.println("Musician ready");
      led.setPattern(1);
    } catch (final Exception e) {
      // System.err.println("Musician exception: " + e.toString());
      e.printStackTrace();
    }
  }

  public Blu3s() {
    super();
    setBeatPulseDevider(2);
    rightHand = new Limb(new EV3MediumRegulatedMotor(MotorPort.A), -20);
    leftHand = new Limb(new EV3MediumRegulatedMotor(MotorPort.B), -150);
    foot = new Limb(new EV3MediumRegulatedMotor(MotorPort.C), -15);
    head = new Limb(new EV3MediumRegulatedMotor(MotorPort.D), -45);
    head.setLogicRange(0, 127);
    rightHand.setLogicRange(-1, 1);
    LCD.clear();

//    head.calibrate(5, 1, -15, 0.2f);
//    rightHand.calibrate(5, 1, -5, 0.2f);
//    leftHand.calibrate(10, -1, 170, 0.2f);
//    foot.calibrate(7, 1, -60, 0.2f);

    leftHand.setSpeed(1);
    rightHand.setSpeed(1);

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
    super.setDynamicRange(lowestNote, highestNote);
    leftHand.setLogicRange(lowestNote, highestNote);
  }

  @Override
  public void noteOn(int tone, int intensity) {
    super.noteOn(tone, intensity);
    // the right hand moves up or down on a note. It changes direction every quarter note.
    if (!isSet) {
      rightHand.setSpeed(127f / intensity);
      if (isUp) {
        rightHand.moveToMin();
      } else {
        rightHand.moveToMax();
      }
      isUp = !isUp;
      isSet = true;
    }
    // The left hand moves full speed to the position 
    leftHand.setSpeed(1);
    leftHand.moveTo(tone);
  }

  @Override
  public void voiceOn(int tone, int intensity) {
    super.voiceOn(tone, intensity);
    // The mouth opens when singing according to intensity of the note.
    head.moveTo(intensity);
  }

  @Override
  public void voiceOff(int tone) {
    super.voiceOff(tone);
    head.moveToMin();
  }

  @Override
  public void start() {
    super.start();
    eyes.enable();
    led.setPattern(3);
  }

  @Override
  public void stop() {
    // Let every limb go to default position and put to rest;
    super.stop();
    head.setSpeed(0.2f);
    led.setPattern(1);
    leftHand.setSpeed(0.2f);
    leftHand.moveToMin();
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
    countDown = 0;
  }

  @Override
  public void beatPulse(int beatNo, int pulseNo) {
    super.beatPulse(beatNo, pulseNo);

    // The left arm may change direction every DIV * quarter note
    isSet = false;

    // Have some LEDs at the start to synchronize video shots
    if (countDown < 8) {
      if (pulseNo % 2 == 0) {
        led.setPattern(2);
      } else {
        led.setPattern(0);
      }
      countDown++;
    }

    // The leg goes up and down every quarter note
    if (pulseNo % 2 == 0) {
      foot.setSpeed(1);
      foot.moveToMin();
    } else {
      foot.setSpeed(0.1f);
      foot.moveToMax();
    }
  }


}
