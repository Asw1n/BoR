package org.aswinmp.lejos.ev3.bandofrobots.pc.shell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.QuitCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.SelectSongCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.ShellCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.ShellExecute;

import PC.BoRController;

/**
 * An input process for the interactive BoR shell.
 * 
 * @author mpscholz
 * 
 */
public class CommandProcessor {

	private final Map<String, Object> commandMap = new HashMap<>();
	private final BoRController boRController = new BoRController();

	public CommandProcessor() throws MidiUnavailableException {
		// set default sequencer
		boRController.setSequencer(MidiSystem.getSequencer().getDeviceInfo());
		// set default synthesizer
		boRController.setSynthesizer(MidiSystem.getSynthesizer()
				.getDeviceInfo());
		// TODO populate command map dynamically by reflection on commands
		// package
		final QuitCommand quitCommand = new QuitCommand(boRController);
		commandMap.put(quitCommand.getClass().getAnnotation(ShellCommand.class)
				.label(), quitCommand);
		final SelectSongCommand displayMidiChannelsCommand = new SelectSongCommand(
				boRController);
		commandMap.put(
				displayMidiChannelsCommand.getClass()
						.getAnnotation(ShellCommand.class).label(),
				displayMidiChannelsCommand);
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
		// split input into command label and params
		final String[] inputParts = input.split(" ");
		final String commandLabel = inputParts[0];
		final String[] parameters = new String[inputParts.length - 1];
		System.arraycopy(inputParts, 1, parameters, 0, inputParts.length - 1);
		// retrieve matching command
		final Object command = commandMap.get(commandLabel);
		if (command == null) {
			// show help
			displayHelp();
		} else {
			// is this the quit command?
			if (command instanceof QuitCommand) {
				result = true;
			}
			// execute command
			invokeShellExecutes(command, parameters);
		}
		return result;

	}

	private void displayHelp() {
		System.out.println("Available commands:");
		for (final Object command : commandMap.values()) {
			final ShellCommand shellCommand = command.getClass().getAnnotation(
					ShellCommand.class);
			System.out.println(String.format("%s %s [%s]",
					shellCommand.label(), shellCommand.parameters(),
					shellCommand.description()));
		}
	}

	private void invokeShellExecutes(final Object shellCommand,
			final Object[] parameters) throws InvocationTargetException,
			IllegalAccessException {
		final Method[] methods = shellCommand.getClass().getMethods();
		for (final Method method : methods) {
			final ShellExecute shellExecute = method
					.getAnnotation(ShellExecute.class);
			if (shellExecute != null) {
				method.invoke(shellCommand, parameters);
			}
		}
	}
}