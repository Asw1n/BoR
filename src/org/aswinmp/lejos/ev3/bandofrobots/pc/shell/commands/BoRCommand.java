package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

/**
 * 
 * Intercae for a shell command.
 * 
 * @author mpscholz
 * 
 */
public interface BoRCommand {

	/**
	 * @return the label of the command the user can type in to trigger
	 *         execution.
	 */
	String getLabel();

	/**
	 * @return a description of the command.
	 */
	String getDescription();

	/**
	 * Executes the command.
	 */
	void execute();
}
