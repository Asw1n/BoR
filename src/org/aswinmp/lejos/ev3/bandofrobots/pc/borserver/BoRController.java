package org.aswinmp.lejos.ev3.bandofrobots.pc.borserver;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

/**
 * The controller manages all resources of BoR. Loads sequencer, synthesizer and
 * conductor. Connects to bricks and disconnects. Starts and stop playing a
 * song.
 * 
 * @author Aswin
 * 
 */
public class BoRController {
	Sequencer sequencer;
	Synthesizer synthesizer;
	Song song;
	Conductor conductor;
	TimeBuffer timeBuffer ;

	public BoRController() {
		song = new Song();
		conductor = new Conductor(this);
		timeBuffer = new TimeBuffer(100);
    timeBuffer.setDaemon(true);
    timeBuffer.start();
	}

	public Song getSong() {
		return song;
	}

	public Sequencer getSequencer() {
		return sequencer;
	}

	public void setSequencer(final MidiDevice.Info sequencer) {
		try {
			this.sequencer = (Sequencer) MidiSystem.getMidiDevice(sequencer);
		} catch (final MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	public Synthesizer getSynthesizer() {
		return synthesizer;
	}

	public void setSynthesizer(final MidiDevice.Info synthesizer) {
		try {
			this.synthesizer = (Synthesizer) MidiSystem
					.getMidiDevice(synthesizer);
		} catch (final MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void setFile(final String pathName) throws InvalidMidiDataException,
			IOException {
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
			
	    timeBuffer.setReceiver(synthesizer.getReceiver());
	    sequencer.getTransmitter().setReceiver(timeBuffer);
	    sequencer.getTransmitter().setReceiver(conductor);
	 
//			sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
//			sequencer.getTransmitter().setReceiver(conductor);
			
			sequencer.setSequence(MidiSystem.getSequence(song.getSong()));
			// TODO: Meta events arenot delayed when sent to the Brick. Is this what is best or do they need a delay?
			sequencer.addMetaEventListener(conductor);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		System.out.println("Closing controller");
		sequencer.close();
		synthesizer.close();
		conductor.close();
		System.out.println("Controller closed");
	}

	private void connect() {
		System.out.println("Connecting to bricks.");
		for (final Brick brick : song.getChannels().getAllBricks()) {
			brick.connect();
		}
		// TODO: aggregate dynamic range per brick (instead of per channel)
		for (int channel = 0; channel < Channels.CHANNELCOUNT; channel++) {
			for (final Brick brick : song.getChannels().getInstrumentBricks(
					channel)) {
				brick.sendDynamicRange(song.getChannels()
						.getLowestNote(channel), song.getChannels()
						.getHighestNote(channel));
			}
		}
	}

	private void disconnect() {
		System.out.println("Disconnecting from bricks.");
		for (final Brick brick : song.getChannels().getAllBricks()) {
			brick.disconnect();
		}
	}

	public void play() {
		bind();
		conductor.setSong(song);
		connect();
		System.out.println("Start song.");
		sequencer.setTickPosition(0);
		for (final Brick brick : song.getChannels().getAllBricks()) {
			brick.sendStart();
		}
		sequencer.start();
	}

	public void stop() {
		System.out.println("Stopping controller");
		sequencer.stop();
		for (final Brick brick : song.getChannels().getAllBricks()) {
			brick.sendStop();
		}
		disconnect();
		System.out.println("Controller stopped");
	}

	public boolean isPlaying() {
		return sequencer.isRunning();
	}

	public void dumpChannels() {
		song.dumpChannels();
	}
	
	/** Sets the delay of the audible music. The delay gives the musicians time to anticipate on notes.
	 * @param delay
	 * in milliseconds
	 */
	public void setSoundDelay(long delay) {
	  timeBuffer.setDelay(delay);
	}

  public long getSoundDelay() {
    return timeBuffer.getDelay();
  }
}
