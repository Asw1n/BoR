package PC;


import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import lejos.utility.Delay;


public class TestController {
  
  private BoRController myController;
  private Sequencer mySequencer;
  private Synthesizer mySynthesizer;
  private static File testMidiFile= new File("MIDI/spain.mid"); 
  

  public TestController() {
    System.out.println("Playing " + testMidiFile.getAbsolutePath());
    myController= new BoRController();
    try {
      mySequencer=MidiSystem.getSequencer(false);
      mySynthesizer=MidiSystem.getSynthesizer();
    }
    catch (MidiUnavailableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void test() {
    myController.setSequencer(mySequencer);
    myController.setSynthesizer(mySynthesizer);
    myController.setMidiFile(testMidiFile);
    myController.open();
    myController.play();
    Delay.msDelay(40000);
    myController.stop();
    myController.close();
    
  }

  public static void main(String[] args) {
    TestController foobar = new TestController();
    foobar.test();

  }
  

}
