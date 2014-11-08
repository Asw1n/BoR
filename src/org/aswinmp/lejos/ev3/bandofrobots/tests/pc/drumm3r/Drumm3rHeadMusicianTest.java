package org.aswinmp.lejos.ev3.bandofrobots.tests.pc.drumm3r;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.tests.pc.AbstractMusicianTest;
import org.aswinmp.lejos.ev3.bandofrobots.tests.pc.BrickChannelAssignment;

/**
 * 
 * @author mpscholz
 * @todo Convert this to a JUnit test
 */
public class Drumm3rHeadMusicianTest extends AbstractMusicianTest {

	private static final String MIDI_FILE = "dire_straits-sultans_of_swing.mid";

	public Drumm3rHeadMusicianTest(final String testMIDIFile,
			final BrickChannelAssignment... brickChannelAssignments) {
		super(testMIDIFile, brickChannelAssignments);
	}

	public static void main(final String[] args) {
		try {
			new Drumm3rHeadMusicianTest(createTestFilePath(MIDI_FILE),
					new BrickChannelAssignment("Drumm3rHead", 9))
					.runTest(10000);
		} catch (final IOException | InvalidMidiDataException
				| BoRCommandException | MidiUnavailableException exc) {
			exc.printStackTrace();
		}
	}

}
