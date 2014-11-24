package org.aswinmp.lejos.ev3.bandofrobots.tests.pc.band;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.AssignmentType;
import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.MusicianConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.BandConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.tests.pc.AbstractBandTest;

public class Drumm3rSing3rTest extends AbstractBandTest {

	private static final String MIDI_FILE_NAME = "dire_straits-sultans_of_swing.mid";
	private static final long TEST_DURATION = 30000;

	public static void main(final String[] args) {
		// create BandConfiguration
		final BandConfiguration bandConfiguration = new BandConfiguration(
				"TestDrumm3rSing3r", createMidiFile(MIDI_FILE_NAME));
		bandConfiguration.add(new MusicianConfiguration("Drumm3r", 9,
				AssignmentType.MUSICIAN));
		bandConfiguration.add(new MusicianConfiguration("Sing3r", 5,
				AssignmentType.SINGER));
		// run test
		try {
			new Drumm3rSing3rTest().runTest(bandConfiguration, TEST_DURATION);
		} catch (final IOException | InvalidMidiDataException
				| BoRCommandException | MidiUnavailableException exc) {
			System.err.println(exc);
			exc.printStackTrace();
		}
	}

}
