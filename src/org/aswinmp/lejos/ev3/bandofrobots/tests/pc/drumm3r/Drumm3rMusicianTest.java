package org.aswinmp.lejos.ev3.bandofrobots.tests.pc.drumm3r;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.MusicianConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.tests.pc.AbstractMusicianTest;

/**
 * 
 * @author mpscholz
 * @todo Convert this to a JUnit test
 */
public class Drumm3rMusicianTest extends AbstractMusicianTest {

	private static final String MIDI_FILE_NAME = "dire_straits-sultans_of_swing.mid";

	public Drumm3rMusicianTest(final MusicianConfiguration musicianConfiguration) {
		super(musicianConfiguration);
	}

	public static void main(final String[] args) {
		try {
			new Drumm3rMusicianTest(new MusicianConfiguration(
					createMidiFile(MIDI_FILE_NAME), "Drumm3r", 9))
					.runTest(10000);
		} catch (final IOException | InvalidMidiDataException
				| BoRCommandException | MidiUnavailableException exc) {
			exc.printStackTrace();
		}
	}

}
