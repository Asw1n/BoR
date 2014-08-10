package org.aswinmp.lejos.ev3.bandofrobots.pc.shell;

import java.util.HashMap;
import java.util.Map;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.BoRCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.QuitCommand;

/**
 * An input process for the interactive BoR shell.
 * 
 * @author mpscholz
 * 
 */
public class InputProcessor {

	private final Map<String, BoRCommand> commandMap = new HashMap<>();

	public InputProcessor() {
		// populate command map
		// TODO populate dynamically by introspection
		final QuitCommand quitCommand = new QuitCommand();
		commandMap.put(quitCommand.getLabel(), quitCommand);
	}

	/**
	 * Processes the user input.
	 * 
	 * @param input
	 *            the user input
	 * @return true in case the user signaled "quit", else false
	 */
	public boolean process(final String input) {
		boolean result = false;
		// retrieve matching command
		final BoRCommand command = commandMap.get(input);
		if (command == null) {
			// show help
			displayHelp();
		} else {
			// is this the quit ommand?
			if (command instanceof QuitCommand) {
				result = true;
			}
			// else execute command
			command.execute();
		}
		return result;

	}

	private void displayHelp() {
		System.out.println("Available commands:");
		for (final BoRCommand command : commandMap.values()) {
			System.out.println(command.getLabel() + " ["
					+ command.getDescription() + "]");
		}
	}

}
