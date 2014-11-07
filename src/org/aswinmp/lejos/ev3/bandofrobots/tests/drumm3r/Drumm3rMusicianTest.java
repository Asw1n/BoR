package org.aswinmp.lejos.ev3.bandofrobots.tests.drumm3r;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.PlayCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.SelectSongCommand;

public class Drumm3rMusicianTest {

	public static void main(final String[] args) {
		try {
			// instantiate and configure BoR controller
			final BoRController boRController = new BoRController();
			boRController.setSequencer(MidiSystem.getSequencer()
					.getDeviceInfo());
			boRController.setSynthesizer(MidiSystem.getSynthesizer()
					.getDeviceInfo());
			// set song
			final String filePath = createTestFilePath();
			new SelectSongCommand(boRController).selectSong(filePath);
			// TODO assign brick
			// play song
			new PlayCommand(boRController).play();
			Thread.sleep(2000);
		} catch (final IOException | InvalidMidiDataException
				| BoRCommandException | MidiUnavailableException
				| InterruptedException exc) {
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
