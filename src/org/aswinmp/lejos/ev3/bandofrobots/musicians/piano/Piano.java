package org.aswinmp.lejos.ev3.bandofrobots.musicians.piano;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.DualBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LineairLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Musician;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.SingleBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s.Blu3sMusician;

public class Piano extends AbstractMusician {
  static LED led = LocalEV3.get().getLED();
  Limb       leftHand;
  Limb       rightHand;
  double handWidth = 3 / 19;
  int lowestNote = 21;
  int highestNote = 108;



  public static void main(String[] args) {
    try {
      final Piano piano = new Piano();
      // register
      piano.register();
    } catch (RemoteException | AlreadyBoundException exc) {
      System.err.println(exc.getMessage());
      exc.printStackTrace();
    }
  }

  private Piano() {
    leftHand = new LineairLimb(new EV3MediumRegulatedMotor(MotorPort.A), new DualBoundaryCalibration(20,10,10));
    leftHand.calibrate();
    leftHand.setSpeed(1);
    leftHand.moveToMin(true);
    rightHand = new LineairLimb(new EV3MediumRegulatedMotor(MotorPort.B), new DualBoundaryCalibration(20,10,10));
    rightHand.calibrate();
    rightHand.setSpeed(1);
    rightHand.moveToMax(false);
    leftHand.calibrate();
    leftHand.setSpeed(1);
    leftHand.moveToMin(true);
    leftHand.setRange(lowestNote, (int) (highestNote * (1 - handWidth)));
    rightHand.setRange((int) (lowestNote * (1 + handWidth)), highestNote);
    rightHand.moveToCenter(true);
    leftHand.moveToCenter(true);
  }


  @Override
  public void start() {
    super.start();
    led.setPattern(3);
    leftHand.setSpeed(1);
    rightHand.setSpeed(1);
    
  }

  @Override
  public void stop() {
    // Let every limb go to default position and put to rest;
    super.stop();
    led.setPattern(1);
    leftHand.setSpeed(0.2f);
    leftHand.moveToMin(true);
    rightHand.setSpeed(0.2f);
    rightHand.moveToMax(true);
    rightHand.moveTo(70,true);
    leftHand.moveTo(60,true);
  }

  @Override
  public void noteOn(int tone, int intensity) {
    Limb hand;
    // System.out.println(String.format("Tone %d", tone));
    // System.out.println(String.format("Left  %d, %d", leftHand.getTarget(), leftHand.getPosition()));
    // System.out.println(String.format("Right %d, %d", rightHand.getTarget(), rightHand.getPosition()));
    super.noteOn(tone, intensity);
    // decide which hand plays the note;
    // Out of range for one of the hands?
    if (tone >= leftHand.getMaximum()) {
      hand = rightHand;
      // System.out.println("Out of reach for left hand");
      }
    else if (tone <= rightHand.getMinimum()) {
      hand = leftHand;
      // System.out.println("Out of reach for right hand");
    }
    // Would it collide ?
    else if (tone >= rightHand.getTarget()) {
      hand = rightHand;
      // System.out.println("Left hand would collide");
    } 
    else if (tone <= leftHand.getTarget()) {
      hand = leftHand;
      // System.out.println("Right hand would collide");
    }
    // Which hand is closest?
    else if (Math.abs(leftHand.getPosition() - tone) >= Math.abs(rightHand.getPosition() - tone)) {
      hand = rightHand;
      // System.out.println("Right hand is closer");
    } 
    else {
      hand = leftHand;
      // System.out.println("Left hand is closer");
    }
    hand.moveTo(tone,true);
  }

}
