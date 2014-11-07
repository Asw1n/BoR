package org.aswinmp.lejos.ev3.bandofrobots.tests.drumm3r;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.tests.AbstractMusicianTest;
import org.aswinmp.lejos.ev3.bandofrobots.tests.BrickChannelAssignment;

/**
 * 
 * @author mpscholz
 * @todo Convert this to a JUnit test
 */
public class Drumm3rMusicianTest extends AbstractMusicianTest {

	public Drumm3rMusicianTest(final String testMIDIFile,
			final BrickChannelAssignment... brickChannelAssignments) {
		super(testMIDIFile, brickChannelAssignments);
	}

	public static void main(final String[] args) {
		try {
			new Drumm3rMusicianTest(createTestFilePath(),
					new BrickChannelAssignment("Drumm3rHead", 9))
					.runTest(10000);
		} catch (final IOException | InvalidMidiDataException
				| BoRCommandException | MidiUnavailableException exc) {
			exc.printStackTrace();
		}
	}

	// TODO find a better way to inject MIDI file
	private static String createTestFilePath() {
		final String fileSeparator = System.getProperty("file.separator");
		final String executionDir = System.getProperty("user.dir");
		return String.format("%s%sMIDI%sdire_straits-sultans_of_swing.mid",
				executionDir, fileSeparator, fileSeparator);
	}
}
