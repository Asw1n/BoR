package org.aswinmp.lejos.ev3.bandofrobots.musicians.k3ys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleBoundaryCalibration;

public class K3ys {
  static LED led = LocalEV3.get().getLED();
  Limb       leftHand;
  Limb       rightHand;
  Limb       back;
  Limb head;
  static int keyboardWidth=21;
  static int  handWidth = 3;
  static int gearRatio=60;  // degrees / stud;
  static int limbRange= (keyboardWidth - 2 * handWidth) * gearRatio;
  static int lowestNote = 21;
  static int highestNote = 108;
  static int handWidthInNotes= (highestNote-lowestNote) * handWidth / keyboardWidth + 1;
  
  
  int rightRest=70;
  int leftRest=60;
  Limb[] limbs;
  Mean statistic=new Mean();
  
  public K3ys() {
    leftHand = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.A), false, new SingleBoundaryCalibration(false,20,limbRange,15));
    leftHand.setSpeed(1);
    leftHand.setRange(lowestNote, highestNote - handWidthInNotes);
    
    rightHand = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.B), false, new SingleBoundaryCalibration(true,20,limbRange,15));
    rightHand.setSpeed(1);
    rightHand.setRange( lowestNote + handWidthInNotes, highestNote);
    
    back = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.C), true, new SingleBoundaryCalibration(false, 10, 90, 5));
    back.setSpeed(0.1f);
    back.setRange(lowestNote + handWidthInNotes / 2 ,highestNote - handWidthInNotes / 2);
    
    head = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.D), false, new SingleBoundaryCalibration(false, 5, 75, 10));
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
    //head.calibrate();
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
  
  public void playWithAnyHand(int note) {
    Limb hand;
    int middle =statistic.getNewStatistic(note);
    // Test if note is outside the range of one of the hands, then the other hand should play it
   if (note >= leftHand.getMaximum()) {
      hand = rightHand;
      }
    else if (note <= rightHand.getMinimum()) {
      hand = leftHand;
    }
    else if (note<middle) {
      hand=leftHand;
    }
    else {
      hand=rightHand;
    }
   
    if (hand==leftHand) {
      if (rightHand.getTarget()-handWidthInNotes<=note) {
        rightHand.moveTo(note+handWidthInNotes+2, true);
      }
    }
    else {
      if (leftHand.getTarget()+handWidthInNotes<=note) {
        leftHand.moveTo(note-handWidthInNotes-2, true);  
      }
    }
    hand.moveTo(note,true);
    
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
