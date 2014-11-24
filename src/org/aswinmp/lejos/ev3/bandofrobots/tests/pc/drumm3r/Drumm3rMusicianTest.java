package org.aswinmp.lejos.ev3.bandofrobots.tests.pc.drumm3r;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.AssignmentType;
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
	private static final long TEST_DURATION = 30000;

	public static void main(final String[] args) {
		try {
			new Drumm3rMusicianTest().runTest(createMidiFile(MIDI_FILE_NAME),
					new MusicianConfiguration("Drumm3r", 9,
							AssignmentType.MUSICIAN), TEST_DURATION);
		} catch (final IOException | InvalidMidiDataException
				| BoRCommandException | MidiUnavailableException exc) {
			System.err.println(exc);
		}
	}
}
