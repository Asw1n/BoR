package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.sing3r;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.sing3r.Sing3r;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

/**
 * Sample that illustrates reading the external sound. Uses the {@link Sing3r}
 * robot.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class SoundSensorSample {

	private static Port SOUND_SENSOR = SensorPort.S2;

	private final Sing3r sing3r;
	private final SoundSensorListener soundSensorListener;
	private final Moving moving;

	public static void main(final String[] args) {
		final SoundSensorSample soundSensorSample = new SoundSensorSample();
		soundSensorSample.boot();
		Button.waitForAnyPress();
		soundSensorSample.shutdown();
	}

	public SoundSensorSample() {
		this.sing3r = new Sing3r();
		soundSensorListener = new SoundSensorListener(new NXTSoundSensor(
				SOUND_SENSOR));
		moving = new Moving();
	}

	public void boot() {
		BrickLogger.info("Calibrating ..");
		sing3r.calibrate();
		sing3r.reset();
		BrickLogger.info("Starting sound listener ..");
		new Thread(soundSensorListener).start();
		BrickLogger.info("Starting drumming ..");
		new Thread(moving).start();
		Sound.beepSequence();
	}

	public void shutdown() {
		BrickLogger.info("Shutting down ..");
		moving.stop();
		soundSensorListener.stop();
		sing3r.reset();
	}

	class SoundSensorListener implements Runnable {

		private final NXTSoundSensor soundSensor;
		private volatile boolean stopped;

		public SoundSensorListener(final NXTSoundSensor soundSensor) {
			this.soundSensor = soundSensor;
		}

		@Override
		public void run() {
			final SampleProvider soundSampleProvider = soundSensor.getDBMode();
			final float[] sample = new float[soundSampleProvider.sampleSize()];
			while (!stopped) {
				soundSampleProvider.fetchSample(sample, 0);
				final float soundValue = sample[0];
				BrickLogger.info("db: %f", soundValue);
				// TODO set speed properly
				// moving.setSpeed(?);
			}
		}

		void stop() {
			stopped = true;
		}
	}

	class Moving implements Runnable {

		private volatile boolean stopped;
		private float speed = 0;

		@Override
		public void run() {
			while (!stopped) {
				Delay.msDelay(100);
				// TODO set speed properly
				// sing3r.setMoveSpeed(speed);
			}
		}

		void stop() {
			stopped = true;
		}

		public void setSpeed(final float speed) {
			this.speed = speed;
		}

	}
}
