package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;

@ShellCommand(label = "play", parameters = "", description = "plays the selected song")
public class PlayCommand {

	private final BoRController boRController;

	public PlayCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void play() throws BoRCommandException {
		if (!boRController.getSong().isSet()) {
			throw new BoRCommandException("No song selected");
		}
		boRController.play();

	}
}
