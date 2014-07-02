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
Song song;
//File midiFile;
//List<InstrumentMusicianMap> map;
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
    e.printStackTrace();
  }
}


public void open() {
  try {
    System.out.println("Binding resources.");
    sequencer.open();
    synthesizer.open();
    conductor.setMap(song.getMapping());
    connect();
    conductor.open();
    sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
    sequencer.getTransmitter().setReceiver(conductor);
    sequencer.setSequence(MidiSystem.getSequence(song.getFile()));
    sequencer.addMetaEventListener(conductor);
  }
    catch (Exception e) {
      e.printStackTrace();
    }
}

private void connect() {
  System.out.println("Connecting to musicians.");
  for (InstrumentMusicianMap mapping : song.getMapping()) {
    if (mapping.getBrick() !=null)
    mapping.getBrick().connect();
  }
  
}

private void disconnect() {
  System.out.println("Disconnecting from musicians.");
  for (InstrumentMusicianMap mapping : song.getMapping()) {
    if (mapping.getBrick() !=null)
    mapping.getBrick().disconnect();
  }
}


public void close() {
  System.out.println("Releasing resources.");
  sequencer.close();
  synthesizer.close();
  conductor.close();
  disconnect();
}

public void play() {
  System.out.println("Start song.");
  sequencer.setTickPosition(0);
  conductor.start();
  sequencer.start();
}
 
public void pause() {
  sequencer.start();

}

public void stop() {
  System.out.println("Stop song.");
  sequencer.stop();
}

public boolean isPlaying() {
  return sequencer.isRunning();
}
public void setSong(Song song) {
  this.song=song;
}

}
