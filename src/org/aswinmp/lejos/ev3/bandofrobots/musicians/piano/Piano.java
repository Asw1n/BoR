package org.aswinmp.lejos.ev3.bandofrobots.musicians.piano;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.DualBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleTouchSensorCalibration;

public class Piano {
  static LED led = LocalEV3.get().getLED();
  Limb       leftHand;
  Limb       rightHand;
  Limb       back;
  Limb head;
  double handWidth = 2.5 / 16;
  int lowestNote = 21;
  int highestNote = 108;
  int rightRest=70;
  int leftRest=60;
  Limb[] limbs;
  Mean statistic=new Mean();
  
  public Piano() {
    leftHand = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.A), false, new SingleBoundaryCalibration(false,20,720,15));
    leftHand.setSpeed(1);
    leftHand.setRange(lowestNote, (int) (highestNote * (1 - handWidth)));
    
    rightHand = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.B), false, new SingleBoundaryCalibration(true,20,720,15));
    rightHand.setSpeed(1);
    rightHand.setRange((int) (lowestNote * (1 + handWidth)), highestNote);
    
    back = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.C), true, new SingleTouchSensorCalibration(SensorPort.S1, true, 10, 50));
    back.setSpeed(0.1f);
    back.setRange(lowestNote * 2f ,highestNote/2f);
    
    head = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.D), false, new SingleBoundaryCalibration(false, 10, 75, 65));
    head.setRange(0, 127);
    head.setSpeed(0.2f);

    

    limbs = new Limb[]{leftHand, rightHand, back};
  }
  
  public void calibrate() {
    rightHand.calibrate();
    leftHand.calibrate();
    centerHands();
    back.calibrate();
    back.moveToCenter(false);
    head.calibrate();
  }
  
  public void centerHands() {
    rightHand.moveTo(rightRest,true);
    leftHand.moveTo(leftRest,true);
  }
  
  public void spreadHands() {
    leftHand.moveToMin(false);
    rightHand.moveToMax(false);
  }
  
  public void playWithRightHand(int tone) {
    rightHand.moveTo(tone, true);
  }

  public void playWithLeftHand(int tone) {
    leftHand.moveTo(tone, true);
  }
  
  public void playWithAnyHand(int tone) {
    Limb hand;
    int middle =statistic.getNewStatistic(tone);
   if (tone >= leftHand.getMaximum()) {
      hand = rightHand;
      }
    else if (tone <= rightHand.getMinimum()) {
      hand = leftHand;
    }
    else if (tone >= rightHand.getTarget()) {
      hand = rightHand;
    } 
    else if (tone <= leftHand.getTarget()) {
      hand = leftHand;
    }
    else if (tone<middle) {
      hand=leftHand;
    }
    else {
      hand=rightHand;
    }
    hand.moveTo(tone,true);
    
    setBack();
  }
  
  public void setBack() {
    back.moveTo((rightHand.getTarget()+leftHand.getTarget())/2, true);
  }
  
  private class Median {
    private int size=10;
    private List<Integer> lastNotes= new ArrayList<Integer>();
    
    public int getNewStatistic(int note) {
      lastNotes.add(note);
      if (lastNotes.size()>size)
        lastNotes.remove(0);
      int[] notes = new int[lastNotes.size()];
      Arrays.sort(notes);
      return notes[lastNotes.size()];
    }
  }
  
  private class Mean {
    private int size=10;
    private List<Integer> lastNotes= new ArrayList<Integer>();
    private int sum;
    
    public int getNewStatistic(int note) {
      lastNotes.add(note);
      sum+=note;
      if (lastNotes.size()>size) {
        sum -=lastNotes.remove(0);
      }
      return sum / lastNotes.size();
    }
  }

  public void restLimbs() {
    for (Limb limb : limbs) {
      limb.rest();
    }
    
  }
  
  public void test() {
    leftHand.moveTo(lowestNote, true);
    rightHand.moveTo(highestNote, true);
    setBack();
    Delay.msDelay(5000);
    leftHand.moveTo(highestNote *16/19, true);
    setBack();
    Delay.msDelay(5000);
    leftHand.moveTo(lowestNote, true);
    setBack();
    Delay.msDelay(5000);
    rightHand.moveTo(lowestNote*22/19, true);
    setBack();
    Delay.msDelay(5000);
    spreadHands();
    setBack();
    Delay.msDelay(5000);
    
    
  }
  
  public void testHead() {
    head.moveTo(127, false);
    head.moveTo(0, false);
    Delay.msDelay(5000);
    head.moveToMax(false);
    head.moveToMin(false);
    Delay.msDelay(5000);
  }

}
