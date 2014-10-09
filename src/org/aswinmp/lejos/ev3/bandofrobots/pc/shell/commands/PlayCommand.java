package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import BoRServer.BoRController;

@ShellCommand(label = "play", parameters = "", description = "plays the selected song")
public class PlayCommand {

	private final BoRController boRController;

	public PlayCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void play() {
		if (! boRController.getSong().isSet()) {
			System.out.println("No song selected");
		} else {
			boRController.play();
		}
	}
}