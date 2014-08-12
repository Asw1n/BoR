package org.aswinmp.lejos.ev3.bandofrobots.pc.shell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.QuitCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.ShellCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.ShellExecute;

/**
 * An input process for the interactive BoR shell.
 * 
 * @author mpscholz
 * 
 */
public class InputProcessor {

	private final Map<String, Object> commandMap = new HashMap<>();

	public InputProcessor() {
		// TODO populate command map dynamically by reflection on commands
		// package
		final QuitCommand quitCommand = new QuitCommand();
		final String label = quitCommand.getClass()
				.getAnnotation(ShellCommand.class).label();
		commandMap.put(label, quitCommand);
	}

	/**
	 * Processes the user input.
	 * 
	 * @param input
	 *            the user input
	 * @return true in case the user signaled "quit", else false
	 */
	public boolean process(final String input)
			throws InvocationTargetException, IllegalAccessException {
		boolean result = false;
		// retrieve matching command
		final Object command = commandMap.get(input);
		if (command == null) {
			// show help
			displayHelp();
		} else {
			// is this the quit command?
			if (command instanceof QuitCommand) {
				result = true;
			}
			// execute command
			invokeShellExecutes(command);
		}
		return result;

	}

	private void displayHelp() {
		System.out.println("Available commands:");
		for (final Object command : commandMap.values()) {
			final ShellCommand shellCommand = command.getClass().getAnnotation(
					ShellCommand.class);
			System.out.println(shellCommand.label() + " ["
					+ shellCommand.description() + "]");
		}
	}

	private void invokeShellExecutes(final Object shellCommand)
			throws InvocationTargetException, IllegalAccessException {
		final Method[] methods = shellCommand.getClass().getMethods();
		for (final Method method : methods) {
			final ShellExecute shellExecute = method
					.getAnnotation(ShellExecute.class);
			if (shellExecute != null) {
				method.invoke(shellCommand);
			}
		}
	}
}
