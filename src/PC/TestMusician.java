package PC;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import lejos.hardware.BrickFinder;
import lejos.utility.Delay;


public class TestMusician {
  ArrayList<BrickHub> bricks;
  BrickHub brick ;
  

  public TestMusician() {
    scan();
    brick = bricks.get(0);
    brick.connect();
    Musician hub=brick.getHub();
    for (int i=0;i<10;i++) {
      try {
        hub.noteOn(0);
        Delay.msDelay(500);
        hub.noteOff(0);
        Delay.msDelay(500);
      }
      catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      
    }
 }
  
  
  public void scan() {
    try {
      bricks=new ArrayList<BrickHub>();
      for(lejos.hardware.BrickInfo info : BrickFinder.discover()) {
        bricks.add(new BrickHub(info));
      }
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  

  public static void main(String[] args) {
    TestMusician foobar=new TestMusician();

  }

}
