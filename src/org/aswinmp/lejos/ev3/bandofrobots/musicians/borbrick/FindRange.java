package org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import lejos.utility.TextMenu;

public class FindRange {
  int[] lowest = new int[4];
  int[] highest = new int[4];

  public static void main(String[] args) {
    FindRange foo = new FindRange();
    foo.runMenu();
    foo.dumpRanges();
    Button.waitForAnyPress();
  }

  private void dumpRanges() {
    LCD.clear();
    for (int i=0;i<4;i++)
      showRange(i);
  }

  private void runMenu() {
    TextMenu menu = new TextMenu(new String[] {"Motor1","Motor2","Motor3","Motor4","Quit"}, 1, "Range finder");
    while(true) {
      switch (menu.select()) {
      case 0: 
        findRange(0);
        break;
      case 1: 
        findRange(1);
        break;
      case 2: 
        findRange(2);
        break;
      case 3: 
        findRange(3);
        break;
      case 4: 
        return;
      }
    }
    
    
  }

  private void findRange(int i) {
    RegulatedMotor motor=null;
    int speed=0;
    
    switch (i) {
      case 0:
        motor = new EV3MediumRegulatedMotor(MotorPort.A);
        break;
      case 1:
        motor = new EV3MediumRegulatedMotor(MotorPort.B);
        break;
      case 2:
        motor = new EV3MediumRegulatedMotor(MotorPort.C);
        break;
      case 3:
        motor = new EV3MediumRegulatedMotor(MotorPort.D);
        break;
    }
    
    LCD.clear();
    while (true) {
      switch(Button.getButtons()) {
      case Button.ID_DOWN:
        lowest[i]=motor.getTachoCount();
        showRange(i);
        speed=0;
        setSpeed(motor,speed);
        break;
      case Button.ID_UP:
        highest[i]=motor.getTachoCount();
        showRange(i);
        speed=0;
        setSpeed(motor,speed);
        break;
      case Button.ID_LEFT:
        speed-=1;
        setSpeed(motor,speed);
        break;
      case Button.ID_RIGHT:
        speed+=1;
        setSpeed(motor,speed);
        break;
      case Button.ID_ESCAPE:
        motor.resetTachoCount();
        lowest[i]=0;
        highest[i]=0;
        showRange(i);
        break;
      case Button.ID_ENTER:
        speed=0;
        setSpeed(motor,speed);
        motor.close();
        return;
      }
      LCD.drawString(String.format("%d", motor.getTachoCount()), 0, 6);
      Delay.msDelay(25);
      LCD.refresh();
    }
  }

  private void showRange(int i) {
    LCD.drawString(String.format("%d  %d  %d",i, lowest[i], highest[i]), 0, i+1);
    LCD.refresh();
  }

  private void setSpeed(RegulatedMotor motor, int speed) {
    motor.setSpeed(Math.abs(speed));
    if (speed == 0) {
      motor.stop();
      motor.flt();
    }
    if (speed<0) {
      motor.backward();
    }
    if (speed>0) {
      motor.forward();
    }
  }

}
