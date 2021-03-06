package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.sing3r;

import java.io.File;

import lejos.hardware.Button;
import lejos.hardware.Sound;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.sing3r.Sing3r;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

/**
 * A sample application that illustrates the creation of sounds. Uses the
 * {@link Sing3r} robot.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class PlaySoundsSample {

	private final Sing3r sing3r;
	private final DistanceListener distanceListener;

	public static void main(final String[] args) {
		final PlaySoundsSample playSoundsSample = new PlaySoundsSample();
		playSoundsSample.boot();
		Button.waitForAnyPress();
		playSoundsSample.shutdown();
	}

	public PlaySoundsSample() {
		this.sing3r = new Sing3r();
		distanceListener = new DistanceListener();
	}

	public void boot() {
		// BrickLogger.info("Calibrating ..");
		// drumm3r.calibrate();
		// drumm3r.reset();
		sing3r.openEyes();
		Sound.playSample(new File("Hello.wav"), 100);
		BrickLogger.info("Starting ultrasonic listener ..");
		new Thread(distanceListener).start();
	}

	public void shutdown() {
		Sound.playSample(new File("Thank you.wav"), 100);
		BrickLogger.info("Shutting down ..");
		distanceListener.stop();
		Sound.playSample(new File("Goodbye.wav"), 100);
		sing3r.reset();
	}

	class DistanceListener implements Runnable {

		private volatile boolean stopped;

		@Override
		public void run() {
			while (!stopped) {
				final float distance = sing3r.getDistanceToEyes();
				// BrickLogger.info("%f", distance);
				if (distance < 1) {
					final float frequency = distance * 10000;
					BrickLogger.info("%f", frequency);
					Sound.playTone((int) frequency, 250, 100);
				}

			}
		}

		void stop() {
			stopped = true;
		}
	}

}
