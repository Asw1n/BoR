package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.File;

import PC.BoRController;
import PC.Song;

@ShellCommand(label = "song", parameters = "path_to_MIDI_file", description = "selects a song serialized in a MIDI file")
public class SelectSongCommand {

	private final BoRController boRController;

	public SelectSongCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void selectSong(final String filePath) {
		final Song song = new Song();
		song.setSong(new File(filePath));
		boRController.setSong(song);
		System.out.println("Set song to " + song.getFileName());
	}
}
