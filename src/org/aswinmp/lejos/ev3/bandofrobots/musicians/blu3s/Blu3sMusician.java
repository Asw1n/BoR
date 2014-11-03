package org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;

public class Blu3sMusician extends AbstractMusician {
  final Blu3s blu3s =new Blu3s();
  
  public static void main(final String[] args) {
    try {
      final Blu3sMusician blu3sMusician = new Blu3sMusician();
      // register
      blu3sMusician.register();
      blu3sMusician.blu3s.openEyes();
      blu3sMusician.blu3s.calibrateLimbs();
      blu3sMusician.blu3s.closeEyes();
      blu3sMusician.setBeatPulseDevider(2);
    } catch (RemoteException | AlreadyBoundException exc) {
      System.err.println(exc.getMessage());
      exc.printStackTrace();
    }
  }

  @Override
  public void start() {
    super.start();
    blu3s.openEyes();
    blu3s.switchAmpOn();
  }

  @Override
  public void stop() {
    super.stop();
    blu3s.restLimbs();
    blu3s.closeEyes();
    blu3s.switchAmpOff();
  }

  @Override
  public void noteOn(int tone, int intensity) {
    blu3s.play(tone, intensity);
  }

  @Override
  public void voiceOn(int tone, int intensity) {
    blu3s.openMouth(intensity);
  }

  @Override
  public void voiceOff(int tone) {
    blu3s.closeMouth();
  }

  @Override
  public void setDynamicRange(int lowestNote, int highestNote) {
    blu3s.setGuitarRange(lowestNote, highestNote);
  }

  @Override
  protected void beatPulse(int beatNo, int pulseNo) {
    blu3s.prepareRightHand();

    // The leg goes up and down every quarter note
    if (pulseNo % 4 == 3) {
      blu3s.tapFoot();
    } else {
      blu3s.raiseFoot();
    }
  } 
  
  

}
