package BoRBrick;

import java.rmi.RemoteException;



/** Base class for musicians. Implements Musician. <br>
 * Provides debug information for Musician methods. <br>
 * Provides some utility methods (in the future).
 * @author Aswin
 *
 */
public class BaseMusician implements Musician {
    protected int tempo =125;
    private boolean running=false;
  

  public BaseMusician() {
  System.out.println("Musician "+this.getClass().getName()+ " loaded.");
  Runner runner = new Runner();
  runner.setDaemon(true);
  runner.start();
}

  @Override
  public void start() {
    System.out.println("Start song");
  }

  @Override
  public void stop() {
    System.out.println("End song");
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
  
  public void enableBeat() {
      running =true;
  }
  
  private class Runner extends Thread {
      @Override
      public void run() {
        long nextTime=System.currentTimeMillis();
        long currentTime;
        while (true) {
          if (running) {
          }
        }
      }

    }
}
  
 

