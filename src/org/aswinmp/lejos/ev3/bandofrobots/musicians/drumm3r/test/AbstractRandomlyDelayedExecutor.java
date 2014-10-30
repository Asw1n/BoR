package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.test;

import java.util.Random;

import lejos.utility.Delay;

public abstract class AbstractRandomlyDelayedExecutor implements Runnable {

	protected abstract void execute();

	private boolean stopped = false;

	@Override
	public void run() {
		// create random delay
		final Random random = new Random();
		while (!stopped) {
			Delay.msDelay(random.nextInt(1000));
			execute();
		}
	}

	public void stop() {
		stopped = true;
	}

}
