package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import PC.BoRController;
import PC.Song;

@ShellCommand(label = "play", parameters = "", description = "plays the selected song")
public class PlayCommand {

	private final BoRController boRController;

	public PlayCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void play() {
		final Song song = boRController.getSong();
		if (song == null) {
			System.out.println("No song selected");
		} else {
			boRController.open();
			boRController.play();
		}
	}
}
