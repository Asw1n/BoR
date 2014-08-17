package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import PC.BoRController;
import PC.Song;

@ShellCommand(label = "instruments", parameters = "", description = "dumps the instrument mappings for the selected song")
public class DumpInstrumentsCommand {

	private final BoRController boRController;

	public DumpInstrumentsCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void dumpInstruments() {
		final Song song = boRController.getSong();
		if (song == null) {
			System.out.println("No song selected");
		} else {
			song.dump();
		}
	}
}
