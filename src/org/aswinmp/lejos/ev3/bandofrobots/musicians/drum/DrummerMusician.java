package org.aswinmp.lejos.ev3.bandofrobots.musicians.drum;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Percussion;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class DrummerMusician extends AbstractMusician implements Percussion {
  final Drummer drummer = new Drummer();

  public static void main(final String[] args) {
    final DrummerMusician drummerMusician = new DrummerMusician();
      drummerMusician.drummer.calibrateLimbs();    
//      drummerMusician.drummer.testShoulders();
      drummerMusician.drummer.testHands();
//    drummerMusician.drummer.restLimbs();
//    try {
//      // register
//      drummerMusician.register();
//    } catch (RemoteException | AlreadyBoundException exc) {
//      BrickLogger.error("An exception occurred", exc);
//      exc.printStackTrace();
//    }
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void stop() {
    super.stop();
    drummer.restLimbs();
  }

  @Override
  public void noteOn(final int tone, final int intensity) {
    switch (tone) {
    case Percussion.LowMidTom: 
    case Percussion.LowTom: 
      drummer.playMidTom();
      break;
    case Percussion.LowFloorTom: 
    case Percussion.BassDrum:
    case Percussion.BassDrum1:
      drummer.playFloorTom();
      break;
    case Percussion.CrashCymbal1: 
    case Percussion.CrashCymbal2: 
    case Percussion.RideCymbal:
      drummer.playSymbal();
      break;
    case Percussion.HighTom: 
      drummer.playHighTom();
      break;
    case Percussion.ElectricSnare: 
    case Percussion.AcousticSnare: 
    case Percussion.HandClap: 
      drummer.playSnare();
      break;
    case Percussion.ClosedHiHat: 
    case Percussion.OpenHiHat:
    case Percussion.PedalHiHat:
      drummer.playHiHat();
      break;
    default: 
      BrickLogger.info("No definition for percussion: %d", tone);
      break;
    }

  }


  @Override
  public String toString() {
    return "DrummerMusician";
  }

}
