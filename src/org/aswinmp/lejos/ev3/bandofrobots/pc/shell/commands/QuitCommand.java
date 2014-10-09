package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import BoRServer.BoRController;

@ShellCommand(label = "quit", parameters = "", description = "quits the shell")
public class QuitCommand {

	private final BoRController boRController;

	public QuitCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void quit() {
		boRController.close();
	}
}
