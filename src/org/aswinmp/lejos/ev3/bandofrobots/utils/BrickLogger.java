package org.aswinmp.lejos.ev3.bandofrobots.utils;

// TODO implement log4j or sl4j interface
public class BrickLogger {

	public static void info(final String message, final Object... args) {
		System.out.println(String.format(message, args));
	}

	public static void error(final String message, final Object... args) {
		System.err.println(String.format(message, args));
	}

	public static void error(final String message, final Throwable throwable) {
		System.err.println(String.format("%s: %s", message,
				throwable.getMessage()));
	}
}
