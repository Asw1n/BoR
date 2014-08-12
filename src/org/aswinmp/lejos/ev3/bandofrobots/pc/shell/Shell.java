package org.aswinmp.lejos.ev3.bandofrobots.pc.shell;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

/**
 * Interactive command shell to trigger client side interaction with the
 * musicians on the brick.
 * 
 * @author mpscholz
 * 
 */
public class Shell {

	public static void main(final String[] args)
			throws InvocationTargetException, IllegalAccessException {
		// start shell
		System.out.println("Band of Robots Interactive Shell");
		// instantiate input processor
		final InputProcessor inputProcessor = new InputProcessor();
		// read input from console until user quits
		try (final Scanner inputScanner = new Scanner(System.in)) {
			boolean quit = false;
			while (!quit) {
				// process user input
				quit = inputProcessor.process(inputScanner.next());
			}
			// bye
			System.out.println("Bye!");
		}
	}

}
