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
public class Drumm3rHeadMusicianTest extends AbstractMusicianTest {

	private static final String MIDI_FILE_NAME = "dire_straits-sultans_of_swing.mid";
	private static final long SERVER_DELAY = 900;
	private static final long TEST_DURATION = 10000;

	public Drumm3rHeadMusicianTest(
			final MusicianConfiguration musicianConfiguration) {
		super(musicianConfiguration, SERVER_DELAY);
	}

	public static void main(final String[] args) {
		try {
			new Drumm3rHeadMusicianTest(new MusicianConfiguration(
					createMidiFile(MIDI_FILE_NAME), "Drumm3rHead", 9))
					.runTest(TEST_DURATION);
		} catch (final IOException | InvalidMidiDataException
				| BoRCommandException | MidiUnavailableException exc) {
			exc.printStackTrace();
		}
	}

}
