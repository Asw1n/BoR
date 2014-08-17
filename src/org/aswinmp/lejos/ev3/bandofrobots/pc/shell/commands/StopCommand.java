package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import PC.BoRController;

@ShellCommand(label = "stop", parameters = "", description = "stops the song that is played currently")
public class StopCommand {

	private final BoRController boRController;

	public StopCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void play() {
		boRController.stop();
	}
}
