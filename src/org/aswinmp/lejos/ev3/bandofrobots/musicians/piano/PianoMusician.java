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
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.Musician;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s.Blu3sMusician;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.DualBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleBoundaryCalibration;

public class PianoMusician extends AbstractMusician {
  final Piano piano = new Piano();

  public static void main(String[] args) {
    final PianoMusician pianoMusician = new PianoMusician();
    pianoMusician.piano.calibrate();
    pianoMusician.piano.restLimbs();

    try {
      // register
      pianoMusician.register();
    } catch ( RemoteException | AlreadyBoundException exc) {
      System.err.println(exc.getMessage());
      exc.printStackTrace();
    }
  }


  @Override
  public void start() {
    piano.centerHands();
  }

  @Override
  public void stop() {
    piano.spreadHands();
    piano.restLimbs();
  }

  @Override
  public void noteOn(int tone, int intensity) {
    piano.playWithAnyHand(tone);
  }
  


}
