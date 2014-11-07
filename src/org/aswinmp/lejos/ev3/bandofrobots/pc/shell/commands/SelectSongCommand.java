package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;

@ShellCommand(label = "song", parameters = "path_to_MIDI_file", description = "selects a song serialized in a MIDI file")
public class SelectSongCommand {

	private final BoRController boRController;

	public SelectSongCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void selectSong(final String filePath)
			throws InvalidMidiDataException, IOException {
		boRController.getSong().setFilePath(filePath);
		System.out.println("Set song to "
				+ boRController.getSong().getFilePath());
	}
}
