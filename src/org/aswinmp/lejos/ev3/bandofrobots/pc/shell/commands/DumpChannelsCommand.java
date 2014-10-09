package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import BoRServer.BoRController;

@ShellCommand(label = "channels", parameters = "", description = "dumps the channels mappings for the selected song")
public class DumpChannelsCommand {

	private final BoRController boRController;

	public DumpChannelsCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void dumpInstruments() {
		if (! boRController.getSong().isSet()) {
			System.out.println("No song selected");
		} else {
		  boRController.getSong().dumpChannels();
		}
	}
}
