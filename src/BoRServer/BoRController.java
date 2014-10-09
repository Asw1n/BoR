package BoRServer;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

/** The controller manages all resources of BoR. Loads sequencer, synthesizer and conductor. Connects to bricks and disconnects.
 * Starts and stop playing a song.
 * @author Aswin
 *
 */
public class BoRController {
    Sequencer   sequencer;
    Synthesizer synthesizer;
    Song        song      ;
    Conductor   conductor;
    
    public BoRController () {
      song      = new Song();
      conductor = new Conductor(this);
    }
    
    public Song getSong() {
        return song;
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    public void setSequencer(MidiDevice.Info sequencer) {
        try {
            this.sequencer = (Sequencer) MidiSystem.getMidiDevice(sequencer)  ;
        }
        catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public Synthesizer getSynthesizer() {
        return synthesizer;
    }

    public void setSynthesizer(MidiDevice.Info synthesizer) {
        try {
            this.synthesizer = (Synthesizer) MidiSystem.getMidiDevice(synthesizer)  ;
        }
        catch (MidiUnavailableException e) {
            e.printStackTrace();
        }    }

    public void setFile(String pathName) throws InvalidMidiDataException, IOException {
        song.setFilePath(pathName);
    }

    public String getFile() {
        return song.getFilePath();
    }
    
    public boolean songIsSet() {
      return song.isSet();
    }

    private void bind() {
        try {
            System.out.println("Binding Midi resources.");
            sequencer.open();
            synthesizer.open();
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
            sequencer.getTransmitter().setReceiver(conductor);
            sequencer.setSequence(MidiSystem.getSequence(song.getSong()));
            sequencer.addMetaEventListener(conductor);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        System.out.println("Releasing resources.");
        sequencer.close();
        synthesizer.close();
        conductor.close();
    }
    
    
    private void connect() {
        System.out.println("Connecting to bricks.");
        for (Brick brick : song.getChannels().getBricks()) {
          brick.connect();
        }
        //TODO: aggregate dynamic range per brick (instead of per channel)
        for (int channel = 0; channel < Channels.CHANNELCOUNT; channel++) {
            for (Brick brick : song.getChannels().getBricks(channel)) {
                brick.sendDynamicRange(song.getChannels().getLowestNote(channel), song.getChannels().getHighestNote(channel));
            }
        }
    }
    
    private void disconnect() {
        System.out.println("Disconnecting from bricks.");
            for (Brick brick : song.getChannels().getBricks()) {
                brick.disconnect();
            }
    }

    
    public void play() {
        bind();
        conductor.setSong(song);
        connect();
        System.out.println("Start song.");
        sequencer.setTickPosition(0);
        for (Brick brick : song.getChannels().getBricks()) {
          brick.sendStart();
        }
        sequencer.start();
    }
    
    public void stop() {
        System.out.println("Stop song.");
        sequencer.stop();
        for (Brick brick : song.getChannels().getBricks()) {
          brick.sendStop();
        }
        disconnect();
      }
    
    public boolean isPlaying() {
        return sequencer.isRunning();
      }
    
    public void dumpChannels() {
      song.dumpChannels();
    }
}
