package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

public class QuitCommand implements BoRCommand {

	@Override
	public String getLabel() {
		return "quit";
	}

	@Override
	public String getDescription() {
		return "quits the shell";
	}

	@Override
	public void execute() {
		// nothing to do
	}

}
