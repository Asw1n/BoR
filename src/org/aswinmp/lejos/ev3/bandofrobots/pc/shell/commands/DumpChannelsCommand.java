package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;

@ShellCommand(label = "channels", parameters = "", description = "dumps the channels mappings for the selected song")
public class DumpChannelsCommand {

	private final BoRController boRController;

	public DumpChannelsCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void dumpInstruments() throws BoRCommandException {
		if (!boRController.getSong().isSet()) {
			throw new BoRCommandException("No song selected");
		}
		boRController.getSong().dumpChannels();

	}
}
