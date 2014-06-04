package BoRBrick;


/** Base class for musicians. Implements Musician. <br>
 * Provides debug information for Musician methods. <br>
 * Provides some utility methods (in the future).
 * @author Aswin
 *
 */
public class BaseMusician implements Musician {
  

  public BaseMusician() {
  System.out.println("Musician "+this.getClass().getName()+ " loaded.");
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
  
  
}
