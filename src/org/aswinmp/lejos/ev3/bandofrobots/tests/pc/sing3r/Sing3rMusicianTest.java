package org.aswinmp.lejos.ev3.bandofrobots.tests.pc.sing3r;

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
public class Sing3rMusicianTest extends AbstractMusicianTest {

	private static final String MIDI_FILE_NAME = "dire_straits-sultans_of_swing.mid";
	private static final long TEST_DURATION = 30000;

	public Sing3rMusicianTest(final MusicianConfiguration musicianConfiguration) {
		super(musicianConfiguration);
	}

	public static void main(final String[] args) {
		try {
			new Sing3rMusicianTest(new MusicianConfiguration(
					createMidiFile(MIDI_FILE_NAME), "Sing3r", 5)).runTest(
					TEST_DURATION, false, true);
		} catch (final IOException | InvalidMidiDataException
				| BoRCommandException | MidiUnavailableException exc) {
			exc.printStackTrace();
		}
	}

}
