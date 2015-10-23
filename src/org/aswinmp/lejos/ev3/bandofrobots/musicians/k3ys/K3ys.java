package org.aswinmp.lejos.ev3.bandofrobots.musicians.k3ys;


import lejos.hardware.BrickFinder;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Limb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.LinearLimb;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.DualBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration.SingleBoundaryCalibration;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class K3ys {
  static LED led = LocalEV3.get().getLED();
  
  private static Port LEFT_HAND_MOTOR_PORT = MotorPort.B;
  private static Port RIGHT_HAND_MOTOR_PORT = MotorPort.A;
  private static Port TORSO_MOTOR_PORT = MotorPort.C;
  private static Port HEAD_MOTOR_PORT = MotorPort.D;
  private static Port EYES_PORT = SensorPort.S2;
  private final EV3UltrasonicSensor eyes;


  
  Limb       leftHand;
  Limb       rightHand;
  Limb       back;
  Limb       head;
  static float lowestNote = 21;                                                        // in notes
  static float highestNote = 108;                                                      // in notes
//  static float lowestNote = 29;                                                        // in notes
//  static float highestNote = 83;                                                      // in notes
  static float toneRange = highestNote-lowestNote;

  static float keyboardWidth = 21-1;                                                   // in Studs
  static float handWidth = 4.5f;                                                       // in Studs
  static float handWidthInNotes=  toTone(handWidth);              //in notes

  static float handRange = 10;                                                         // in studs
  static float handRangeInNotes = toTone(handRange);
  static float gearRatio = 60;                                                         // in degrees / stud
  static int limbRange= (int) (handRange * gearRatio);                                 // in degrees
  
  static float leftLowest=lowestNote;
  static float leftHighest=leftLowest + handRangeInNotes;
  static float rightHighest=highestNote;
  static float rightLowest=highestNote - handRangeInNotes;

  static float bendStart = 6;                                                          // in studs  
  static float bendFactor = 1 / (toneRange*(handRange-bendStart)/keyboardWidth);             //
  static float leftBend = studToTone(bendStart);
  static float rightBend = studToTone(keyboardWidth-bendStart);

  
  
  static int rightRest=80;
  static int leftRest=50;
  Limb[] limbs;
  private float rhmean=rightRest;
  private float lhmean=leftRest;
  private float f=0.3f;
  
  public K3ys() {
    leftHand = new LinearLimb(new EV3MediumRegulatedMotor(LEFT_HAND_MOTOR_PORT), false, new SingleBoundaryCalibration(false,20,limbRange,20));
    leftHand.setSpeed(1);
    leftHand.setRange(leftLowest,leftHighest );
    
    rightHand = new LinearLimb(new EV3MediumRegulatedMotor(RIGHT_HAND_MOTOR_PORT), false, new SingleBoundaryCalibration(true,20,limbRange,30));
    rightHand.setSpeed(1);
    rightHand.setRange(rightLowest ,rightHighest);
    
    back = new LinearLimb(new EV3MediumRegulatedMotor(TORSO_MOTOR_PORT), true, new SingleBoundaryCalibration(false, 10, 100, 5));
    //back = new LinearLimb(new EV3MediumRegulatedMotor(TORSO_MOTOR_PORT), true, new DualBoundaryCalibration(5,5,5));
    back.setSpeed(0.1f);
    back.setRange(-1,1);
    
	head = new LinearLimb(new EV3MediumRegulatedMotor(MotorPort.D), true, new SingleBoundaryCalibration(true, 10, 40, 15));
	head.setRange(0, 127);
	head.setSpeed(1);
    limbs = new Limb[]{leftHand, rightHand, back, head};
    
	eyes = new EV3UltrasonicSensor(BrickFinder.getDefault().getPort("S2"));

  }
  
  
  static float toTone(float stud) {
    return (toneRange / keyboardWidth)  * stud;
  }

  static float toStud(float tone) {
    return tone * keyboardWidth/toneRange;
  }
  
  static float studToTone(float stud) {
    return lowestNote +  toTone(stud);
  }
  
  static float toneToStud(float tone) {
    return toStud(tone-lowestNote);
  }
  
  public void calibrate() {
    rightHand.calibrate();
    leftHand.calibrate();
    centerHands(true);
    back.calibrate();
    back.moveToCenter(false);
    head.calibrate();
  }
  
  public void openEyes() {
	    eyes.enable();
	  }

  public void closeEyes() {
	    eyes.disable();
	  }

  
  
  public void openMouth(final float f) {
    // The mouth opens when singing according to intensity of the note.
    head.setSpeed(0.1f);
    head.moveTo(f, true);
  }
  
  public void closeMouth() {
    head.setSpeed(0.03f);
    head.moveTo(0, true);
  }
  
  public void centerHands(boolean immediateReturn) {
    play(rightHand,rightRest,immediateReturn);
    play(leftHand,leftRest,immediateReturn);
  }
  
  public void spreadHands(boolean immediateReturn) {
    play(rightHand,highestNote,immediateReturn);
    play(leftHand,lowestNote,immediateReturn);
  }
  
  public void rest() {
    for (Limb limb : limbs) limb.rest();
    rhmean=rightRest;
    lhmean=leftRest;
  }
  
  /** Play a tone with one hand and makes sure the other gets out of the way. Also move the back.
   * @param hand
   * @param note
   */
  private void play(Limb hand, float note, boolean immediateReturn) {
    if (hand==leftHand) {
      if (note + handWidthInNotes > rightHand.getTarget()) {
        rightHand.moveTo(note+handWidthInNotes, true);
      }
    }
    else {
      if (note - handWidthInNotes < leftHand.getTarget()) {
        leftHand.moveTo(note-handWidthInNotes, true);  
      }
    }
    hand.moveTo(note,immediateReturn);
    setBack();
  }
  
  public void play(int note) {
    Limb hand;
    //BrickLogger.info("note: %f %f %d ", lhmean, rhmean , note);
   if (note >= leftHighest) {
      hand = rightHand;
      }
    else if (note <= rightLowest) {
      hand = leftHand;
    }
    else if (Math.abs(note-rhmean)<Math.abs(note-lhmean)) {
      hand=rightHand;
    }
    else {
      hand=leftHand;
    }
    if (hand==rightHand){
      rhmean=(1-f)*rhmean+f*note;
    }
    else {
      lhmean=(1-f)*lhmean+f*note;
    }

    play(hand, note, true);
  }
  
  public void setBack() {
    float leftFactor=0;
    float rightFactor=0;
    if (leftHand.getTarget()>leftBend) {
      leftFactor=(leftHand.getTarget()-leftBend) * bendFactor;
    }
    if (rightHand.getTarget()<rightBend) {
      rightFactor=(rightHand.getTarget()-rightBend) * bendFactor;
    }
    //BrickLogger.info("Bend: %f %f ", leftFactor , rightFactor);
    back.moveTo(leftFactor + rightFactor, true);
  }
  



  public void test() {
    play(rightHand,rightLowest, false);
    for (float note=leftLowest;note<=leftHighest; note++) {
      play(leftHand,note,false);
    }
    play(rightHand,rightHighest, true);
    play(leftHand,leftHighest, false);
    for (float note=rightHighest;note>=rightLowest; note--) {
      play(rightHand,note,false);
    }
  }
  
  private void moveTo(float position) {
    leftHand.moveTo(position, true);
    rightHand.moveTo(position+handWidthInNotes, true);
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
  
  public void testBack() {
    for (float i=-1;i<=1;i+=0.05) {
      back.moveTo(i,false); 
      Delay.msDelay(100);
    }
    back.moveToMax(false);
    back.moveToMin(false);
    back.moveToMax(false);
    back.moveToMin(false);
    back.moveToMax(false);
    back.moveToMin(false);
    back.moveTo(0,false);
  }

}
