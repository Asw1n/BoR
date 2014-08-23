package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.IOException;

import lejos.hardware.BrickFinder;

@ShellCommand(label = "bricks", parameters = "", description = "dumps the connected bricks")
public class DumpBricksCommand {

	@ShellExecute
	public void dumpBricks() {
		System.out.println("Connected bricks:");
		try {
			for (final lejos.hardware.BrickInfo info : BrickFinder.discover()) {
				System.out.println(String.format("%s (%s)", info.getName(),
						info.getIPAddress()));
			}
		} catch (final IOException ioe) {
			System.err.println(ioe);
		}
	}
}
