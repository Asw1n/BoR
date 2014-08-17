package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation interface for a shell command.
 * 
 * @author mpscholz
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.TYPE })
public @interface ShellCommand {

	/**
	 * @return the label of the command the user can type in to trigger
	 *         execution.
	 */
	String label();

	String parameters();

	/**
	 * @return a description of the command.
	 */
	String description();

}
