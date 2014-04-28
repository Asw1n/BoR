package PC;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

public class BoRController {
Sequencer sequencer;
Synthesizer synthesizer;
File midiFile;
IMMap[] map;
BoRConductor conductor=new BoRConductor();

public Sequencer getSequencer() {
  return sequencer;
}
public void setSequencer(Sequencer sequencer) {
  this.sequencer = sequencer;
}

public void setSequencer(MidiDevice.Info sequencer) {
  try {
    this.sequencer = (Sequencer) MidiSystem.getMidiDevice(sequencer);
  }
  catch (MidiUnavailableException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
}


public Synthesizer getSynthesizer() {
  return synthesizer;
}

public void setSynthesizer(Synthesizer synthesizer) {
  this.synthesizer = synthesizer;
}

public void setSynthesizer(MidiDevice.Info synthesizer) {
  try {
    this.synthesizer = (Synthesizer) MidiSystem.getMidiDevice(synthesizer);
  }
  catch (MidiUnavailableException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
}



public File getMidiFile() {
  return midiFile;
}
public void setMidiFile(File midiFile) {
  this.midiFile = midiFile;
}
public IMMap[] getMap() {
  return map;
}
public void setMap(IMMap[] map) {
  this.map = map;
}

public void open() {
  try {
    sequencer.open();
    synthesizer.open();
    conductor.setMap(map);
    conductor.open();
    sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
    sequencer.getTransmitter().setReceiver(conductor);
    try {
      sequencer.setSequence(MidiSystem.getSequence(midiFile));
    }
    catch (InvalidMidiDataException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  catch (MidiUnavailableException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  
}

public void close() {
  sequencer.close();
  synthesizer.close();
  conductor.close();
}

public void play() {
  sequencer.setTickPosition(0);
  sequencer.start();
  
}
 
public void pause() {
  sequencer.start();

}

public void stop() {
  sequencer.stop();
}

public boolean isPlaying() {
  return sequencer.isRunning();
}

}
