package org.aswinmp.lejos.ev3.bandofrobots.utils;

import java.util.Random;

import lejos.utility.Delay;

public abstract class AbstractRandomlyDelayedExecutor implements Runnable {

	protected abstract void execute();

	private boolean stopped = false;
	private final int randomMax;

	protected AbstractRandomlyDelayedExecutor(final int randomMax) {
		this.randomMax = randomMax;
	}

	@Override
	public void run() {
		// create random delay
		final Random random = new Random();
		while (!stopped) {
			Delay.msDelay(random.nextInt(randomMax));
			execute();
		}
	}

	public void stop() {
		stopped = true;
	}

}
