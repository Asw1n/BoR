package PC;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Song {
  private List<InstrumentMusicianMap> instruments;
  private File songFile;
  
  public Song () {
    instruments = new ArrayList<InstrumentMusicianMap>();
  }
  
  public void setSong(File song) {
    this.songFile=song;
    instruments = new ArrayList<InstrumentMusicianMap>();
    scan();
  }
  
  public File getSong() {
    return songFile;
  }
  
  public String getFileName() {
    return songFile.getAbsolutePath();
  }
  
  protected InstrumentMusicianMap findInstrument(int channel, int instrument) {
    for (InstrumentMusicianMap myInstrument : instruments) {
      if (myInstrument.getInstrument()==instrument && myInstrument.getChannel()==channel)
        return myInstrument;
    }
    return null;
  }
  
  public int getNumberOfInstruments() {
    return instruments.size();
  }
  
  public InstrumentMusicianMap getInstrument(int index) {
    return instruments.get(index);
  }
  
  /**
   * This method scans the song for instruments that are used and determines the dynamic range of each instrument.
   */
  private void scan() {
    InstrumentMusicianMap thisInstrument;
    InstrumentMusicianMap[] channel = new InstrumentMusicianMap[16];
    try {
      Sequence seq = MidiSystem.getSequence(songFile);
      Track[] tracks=seq.getTracks();
      for (Track track : tracks) {
        for (int i=0; i<track.size();i++) {
          MidiEvent event=track.get(i);
          if (event.getMessage() instanceof ShortMessage) {
            ShortMessage message=(ShortMessage)event.getMessage();
            if (message.getCommand()==ShortMessage.PROGRAM_CHANGE) {
              // a change of instrument is detected, if it new add it to the instrument list
              thisInstrument=findInstrument(message.getChannel(),message.getData1());
              if (thisInstrument==null) {
                thisInstrument=new InstrumentMusicianMap( message.getChannel(),message.getData1());
                instruments.add(thisInstrument);
              }
              // make the instrument the current instrument  on the channel
              channel[thisInstrument.getChannel()]=thisInstrument;
            }
            if (message.getCommand()==ShortMessage.NOTE_ON) {
              // A note is detected, update the dynamic range for this instrument
              channel[message.getChannel()].updateDynamicRange(message.getData1());;
            }
          }
        }
      }
    }
    catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void dump() {
    System.out.println(getFileName());
    for (InstrumentMusicianMap thisInstrument : instruments) {
      thisInstrument.dump();
    }
  }

  public List<InstrumentMusicianMap> getMapping() {
    return instruments;
  }

  public File getFile() {
    // TODO Auto-generated method stub
    return songFile;
  }

}
