package BoRBrick;

import lejos.hardware.ev3.LocalEV3;
import lejos.utility.Delay;

public class BaseMusician implements Musician {
  
  private Heartbeat heartbeat;

  public BaseMusician() {
  heartbeat = new Heartbeat();
  heartbeat.setDaemon(true);
  //heartbeat.start();
  System.out.println("2");
}

  @Override
  public void start() {
  }

  @Override
  public void stop() {
  }

  @Override
  public void setTempo(int tempo) {
  }

  @Override
  public void noteOn(int tone) {
    //System.out.println("+");
    LocalEV3.get().getLED().setPattern(1);
    heartbeat.setLastTime(tone);
  }

  @Override
  public void noteOff(int tone) {
    //System.out.println("-");
    LocalEV3.get().getLED().setPattern(0);
  }
  
  private class Heartbeat extends Thread {
    long lastTime=0;
    int tone;
    
   public void run() {
//     while(true) {
//     long now=System.currentTimeMillis();
//     if (lastTime !=0 ) {
//       if (now-lastTime >50) {
//         noteOff(tone);
//         lastTime=0;
//       }
//     }
//     Delay.msDelay(50);
//     }
   }
     public void setLastTime(int tone) {
       this.tone=tone;
       lastTime=System.currentTimeMillis();
     }
      
  }
  
}
