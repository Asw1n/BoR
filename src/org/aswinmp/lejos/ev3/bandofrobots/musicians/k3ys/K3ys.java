package org.aswinmp.lejos.ev3.bandofrobots.musicians.k3ys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class K3ys {
  static LED led = LocalEV3.get().getLED();
  
  private static Port LEFT_HAND_MOTOR_PORT = MotorPort.A;
  private static Port RIGHT_HAND_MOTOR_PORT = MotorPort.B;
  private static Port TORSO_MOTOR_PORT = MotorPort.C;
  private static Port HEAD_MOTOR_PORT = MotorPort.D;
  private static Port EYES_PORT = SensorPort.S1;

  
  Limb       leftHand;
  Limb       rightHand;
  Limb       back;
  Limb head;
  static float lowestNote = 21;                                                        // in notes
  static float highestNote = 108;                                                      // in notes

  static float keyboardWidth = 21-1;                                                     // in Studs
  static float handWidth = 4.5f;                                                          // in Studs
  static float handWidthInNotes=  (highestNote-lowestNote) * handWidth / keyboardWidth ;//in notes

  static float handRange = 10;                                                         // in studs
  static float handRangeInNotes = (highestNote-lowestNote) * handRange /keyboardWidth;
  static float gearRatio = 60;                                                         // in degrees / stud
  static int limbRange= (int) (handRange * gearRatio);                                 // in degrees
  
  static float leftLowest=lowestNote;
  static float leftHighest=leftLowest + handRangeInNotes;
  static float rightHighest=highestNote;
  static float rightLowest=highestNote - handRangeInNotes;
  
  
  int rightRest=80;
  int leftRest=50;
  Limb[] limbs;
  Mean statistic=new Mean();
  
  public K3ys() {
    leftHand = new LinearLimb(new EV3MediumRegulatedMotor(LEFT_HAND_MOTOR_PORT), false, new SingleBoundaryCalibration(false,20,limbRange,20));
    leftHand.setSpeed(1);
    leftHand.setRange(leftLowest,leftHighest );
    
    rightHand = new LinearLimb(new EV3MediumRegulatedMotor(RIGHT_HAND_MOTOR_PORT), false, new SingleBoundaryCalibration(true,20,limbRange,20));
    rightHand.setSpeed(1);
    rightHand.setRange(rightLowest ,rightHighest);
    
    back = new LinearLimb(new EV3MediumRegulatedMotor(TORSO_MOTOR_PORT), true, new SingleBoundaryCalibration(false, 10, 90, 5));
    back.setSpeed(0.1f);
    back.setRange(leftLowest,rightHighest);
    
    head = new LinearLimb(new EV3MediumRegulatedMotor(HEAD_MOTOR_PORT), false, new SingleBoundaryCalibration(false, 5, 75, 10));
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
    //int middle =statistic.getNewStatistic(note);
    // Test if note is outside the range of one of the hands, then the other hand should play it
   if (note >= leftHighest) {
      hand = rightHand;
      }
    else if (note <= rightLowest) {
      hand = leftHand;
    }
    else if (Math.abs(note-leftHand.getTarget()) < Math.abs(note-rightHand.getTarget()) ) {
      hand=leftHand;
    }
    else {
      hand=rightHand;
    }
   
    if (hand==leftHand) {
      if (rightHand.getTarget()-handWidthInNotes<=note) {
        rightHand.moveTo(note+handWidthInNotes, true);
      }
    }
    else {
      if (leftHand.getTarget()+handWidthInNotes<=note) {
        leftHand.moveTo(note-handWidthInNotes, true);  
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
    leftHand.moveTo(leftHand.getMinimum(), true);
    rightHand.moveTo(rightHand.getMaximum(), true);
    setBack();
    Delay.msDelay(5000);
    leftHand.moveTo(leftHand.getMaximum(),true);
    setBack();
    Delay.msDelay(5000);
    leftHand.moveTo(leftHand.getMinimum(), true);
    setBack();
    Delay.msDelay(5000);
    rightHand.moveTo(rightHand.getMinimum(), true);
    setBack();
    Delay.msDelay(5000);
    for ( float p=lowestNote;p<=highestNote-this.handWidthInNotes;p+=1) {
      moveTo(p);
    }
    spreadHands();
    setBack();
    Delay.msDelay(5000);
  }
  
  private void moveTo(float position) {
    leftHand.moveTo(position, true);
    rightHand.moveTo(position+this.handWidthInNotes, true);
    setBack();
    Delay.msDelay(1000);
    
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
