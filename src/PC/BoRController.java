package PC;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

/** The BoRController controls the PC side of the BoR system. It instructs the sequencer to play a song and links a 
 * synthesizer and a BoR Conductor to the sequencer.
 * @author Aswin
 *
 */
public class BoRController {
Sequencer sequencer;
Synthesizer synthesizer;
File midiFile;
List<IMMap> map;
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
public List<IMMap> getMap() {
  return map;
}
public void setMap(List<IMMap> list) {
  this.map = list;
}

public void open() {
  try {
    sequencer.open();
    synthesizer.open();
    conductor.setMap(map);
    connect();
    conductor.open();
    sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
    sequencer.getTransmitter().setReceiver(conductor);
    sequencer.setSequence(MidiSystem.getSequence(midiFile));
  }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
}

private void connect() {
  for (IMMap oneMap : map) {
    if (oneMap.brickHub !=null)
    oneMap.brickHub.connect();
  }
  
}

private void disconnect() {
  for (IMMap oneMap : map) {
    if (oneMap.brickHub !=null)
    oneMap.brickHub.disconnect();
  }
  
}


public void close() {
  sequencer.close();
  synthesizer.close();
  conductor.close();
  disconnect();
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
