package PC;


import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;


public class TestController {
  
  private BoRController myController;
  private Sequencer mySequencer;
  private Synthesizer mySynthesizer;
  private static File testMidiFile= new File("/BoR/MIDI/7steps2h.mid"); 
  

  public TestController() {
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

  public static void main(String[] args) {
    TestController foobar = new TestController();

  }
  

}
