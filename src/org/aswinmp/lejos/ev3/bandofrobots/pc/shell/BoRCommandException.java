package org.aswinmp.lejos.ev3.bandofrobots.pc.shell;

public class BoRCommandException extends Exception {

	private static final long serialVersionUID = 1614272792560550583L;

	public BoRCommandException(final String message) {
		super(message);
	}

	public BoRCommandException(final Throwable cause) {
		super(cause);
	}

	public BoRCommandException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BoRCommandException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
