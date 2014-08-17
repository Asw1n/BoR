package org.aswinmp.lejos.ev3.bandofrobots.pc.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import javax.sound.midi.MidiUnavailableException;

/**
 * Interactive command shell to trigger client side interaction with the
 * musicians on the brick.
 * 
 * @author mpscholz
 * @todo use Log4J for logging
 */
public class Shell {

	public static void main(final String[] args)
			throws InvocationTargetException, IllegalAccessException,
			IOException, MidiUnavailableException {
		// start shell
		System.out.println("Band of Robots Interactive Shell");
		// instantiate input processor
		final CommandProcessor inputProcessor = new CommandProcessor();
		// read input from console until user quits
		final BufferedReader inputReader = new BufferedReader(
				new InputStreamReader(System.in));
		boolean quit = false;
		try {
			while (!quit) {
				System.out.print("> ");
				// process user input
				quit = inputProcessor.process(inputReader.readLine());
			}
		} finally {
			inputReader.close();
		}

		// bye
		System.out.println("Bye!");
	}
}
