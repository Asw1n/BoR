package BoRBrick;

import java.rmi.RemoteException;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class GuitarPlayer extends BaseMusician {
  RegulatedMotor rightHand;
  RegulatedMotor leftHand;
  private static int rightHandRange=15;
  private static int leftHandRange=160;
  private static int high=90;
  private static int low=55;
  private static int toneRange=high-low;
  private float ticksPerTone=(float)leftHandRange/(float)toneRange;

  
  
  
  public static void main(String[] args) {
    GuitarPlayer jimi = new GuitarPlayer();
    for (int i=low;i<high;i+=3) {
    jimi.noteOn(i);
    Delay.msDelay(200);
    jimi.noteOff(i);
    Delay.msDelay(100);
    }
    jimi.noteOn(low);
    jimi.noteOff(low);
    Delay.msDelay(500);
    
  }
  
  public GuitarPlayer() {
    rightHand=new EV3LargeRegulatedMotor(MotorPort.A);
    rightHand.setSpeed(600);
    leftHand=new EV3LargeRegulatedMotor(MotorPort.B);
    leftHand.setSpeed(600);
  }
  
  
  
  @Override
  public void sendDynamicRange(int lowestNote, int highestNote) throws RemoteException {
    super.sendDynamicRange(lowestNote, highestNote);
    high=highestNote;
    low=lowestNote;
  }

  @Override
  public void noteOn(int tone) {
    //super.noteOn(tone);
    rightHand.rotateTo(rightHandRange,true);
    int leftHandPos=(int) ((tone-low)*ticksPerTone);
    leftHand.setSpeed(600);
    leftHand.rotateTo(-leftHandPos, true);
  }
  
  @Override
  public void noteOff(int tone) {
    super.noteOff(tone);
    rightHand.rotateTo(0,true);
    leftHand.setSpeed(60);
    leftHand.rotateTo(0, true);
  }

  
  
}
