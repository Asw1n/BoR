package BoRServer;

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
public class Controller {
    Sequencer   sequencer;
    Synthesizer synthesizer;
    Song        song      = new Song();
    Conductor   conductor = new Conductor();
    
    protected Song getSong() {
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

    public void setFile(String pathName) {
        song.setSongFile(pathName);
    }

    public String getFile() {
        return song.getSongFile();
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
    
    private void release() {
        System.out.println("Releasing resources.");
        sequencer.close();
        synthesizer.close();
        conductor.close();
    }
    
    
    private void connect() {
        System.out.println("Connecting to bricks.");
        for (int channel = 0; channel < Channels.CHANNELCOUNT; channel++) {
            for (Brick brick : song.getChannels().getBricks(channel)) {
                brick.connect();
            }
        }
    }
    
    private void disconnect() {
        System.out.println("Disconnecting from bricks.");
        for (int channel = 0; channel < Channels.CHANNELCOUNT; channel++) {
            for (Brick brick : song.getChannels().getBricks(channel)) {
                brick.disconnect();
            }
        }
    }

    
    public void play() {
        bind();
        conductor.setSong(song);
        connect();
        System.out.println("Start song.");
        sequencer.setTickPosition(0);
        sequencer.start();
    }
    
    public void stop() {
        System.out.println("Stop song.");
        sequencer.stop();
        disconnect();
      }
    
    public boolean isPlaying() {
        return sequencer.isRunning();
      }
}
