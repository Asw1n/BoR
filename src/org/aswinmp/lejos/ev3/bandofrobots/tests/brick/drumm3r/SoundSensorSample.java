package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

public class SoundSensorSample {

	private static Port SOUND_SENSOR = SensorPort.S2;
	private static final int INITIAL_DRUMMING_DELAY = 1000;

	private final Drumm3r drumm3r;
	private final SoundSensorListener soundSensorListener;
	private final Drumming drumming;

	public static void main(final String[] args) {
		final SoundSensorSample soundSensorSample = new SoundSensorSample();
		soundSensorSample.boot();
		Button.waitForAnyPress();
		soundSensorSample.shutdown();
	}

	public SoundSensorSample() {
		this.drumm3r = new Drumm3r();
		soundSensorListener = new SoundSensorListener(new NXTSoundSensor(
				SOUND_SENSOR));
		drumming = new Drumming(INITIAL_DRUMMING_DELAY);
	}

	public void boot() {
		BrickLogger.info("Calibrating ..");
		drumm3r.calibrate();
		drumm3r.reset();
		BrickLogger.info("Starting sound listener ..");
		new Thread(soundSensorListener).start();
		BrickLogger.info("Starting drumming ..");
		new Thread(drumming).start();
		Sound.beepSequence();
	}

	public void shutdown() {
		BrickLogger.info("Shutting down ..");
		drumming.stop();
		soundSensorListener.stop();
		drumm3r.reset();
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
				drumming.setDelay((int) Math.max(0, INITIAL_DRUMMING_DELAY
						- soundValue * INITIAL_DRUMMING_DELAY));
			}
		}

		void stop() {
			stopped = true;
		}
	}

	class Drumming implements Runnable {

		private volatile boolean stopped;
		private int delay;

		public Drumming(final int initialDelay) {
			this.delay = initialDelay;
		}

		@Override
		public void run() {
			while (!stopped) {
				Delay.msDelay(delay);
				drumm3r.drum(false);
			}
		}

		void stop() {
			stopped = true;
		}

		public void setDelay(final int delay) {
			this.delay = delay;
		}

	}
}
