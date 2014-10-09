package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import BoRServer.BoRController;

@ShellCommand(label = "song", parameters = "path_to_MIDI_file", description = "selects a song serialized in a MIDI file")
public class SelectSongCommand {

	private final BoRController boRController;

	public SelectSongCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void selectSong(final String filePath) {
		try {
			boRController.getSong().setFilePath(filePath);
			System.out.println("Set song to " + boRController.getSong().getFilePath());
		} catch (final InvalidMidiDataException | IOException exc) {
			System.err.println("Could not set song: " + exc);
		}
	}
}
