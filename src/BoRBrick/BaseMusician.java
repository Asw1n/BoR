package BoRBrick;

import java.rmi.RemoteException;




/** Base class for musicians. Implements Musician. <br>
 * Provides debug information for Musician methods. <br>
 * Provides some utility methods (in the future).
 * @author Aswin
 *
 */
public class BaseMusician implements Musician {
    protected int tempo =500;
    protected boolean running=false;
    private long nextBeat=0;
    private boolean generateBeatPulse=true;
    Runner runner;
    
 

  public BaseMusician() {
  System.out.println("Musician "+this.getClass().getName()+ " loaded.");
  runner = new Runner();
  runner.setDaemon(true);
  runner.start();
}

  @Override
  public void start() {
    System.out.println("Start song");
    nextBeat=System.currentTimeMillis();
    running=true;
  }

  @Override
  public void stop() {
    System.out.println("End song");
    running=false;
  }

  @Override
  public void setTempo(int tempo) {
    System.out.println("Set tempo: " + tempo);
  }

  @Override
  public void noteOn(int tone) {
    System.out.println("Tone on: " + tone);
  }

  @Override
  public void noteOff(int tone) {
    System.out.println("Tone off: " + tone);
  }

  @Override
  public void setDynamicRange(int lowestNote, int highestNote) throws RemoteException {
    System.out.println("Dynamic range: " + lowestNote + " to " + highestNote );
  }
  
  public void generateBeatPulse(boolean v) {
      generateBeatPulse =v;
  }
  
  protected void beatPulse() {
      System.out.println("Beat" );
  }
  
  private class Runner extends Thread {
      @Override
      public void run() {
        while (true) {
          long time=System.currentTimeMillis();
          if (running) {
              if (time>=nextBeat) {
                  if (generateBeatPulse)
                      beatPulse();
                  nextBeat+=tempo;
              }
          }
          Thread.yield();
          

        }
      }

    }
}
  
 

