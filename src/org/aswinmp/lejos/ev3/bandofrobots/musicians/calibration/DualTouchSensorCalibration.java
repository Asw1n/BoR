package org.aswinmp.lejos.ev3.bandofrobots.musicians.calibration;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Calibrates the position limb by detecting touch events on two touch sensors
 * that are mounted at min and max location of the trunr ange.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class DualTouchSensorCalibration implements CalibrationStrategy {
	private final int speed;
	private final Port minPort;
	private final Port maxPort;
	private boolean reverse;

	public DualTouchSensorCalibration(final Port minPort, final Port maxPort,
			final int speed) {
		this.speed = speed;
		this.minPort = minPort;
		this.maxPort = maxPort;
	}

	public DualTouchSensorCalibration(final Port minPort, final Port maxPort,
			final int speed, final boolean reverse) {
		this(minPort, maxPort, speed);
		this.reverse = reverse;
	}

	@Override
	public LimbRange calibrate(final RegulatedMotor motor) {
		final int originalSpeed = motor.getSpeed();
		motor.setSpeed(speed);
		final int minimum = findLimit(minPort, motor, reverse);
		final int maximum = findLimit(maxPort, motor, !reverse);
		motor.setSpeed(originalSpeed);
		return new LimbRange(minimum, maximum);
	}

	private int findLimit(final Port touchSensorPort,
			final RegulatedMotor motor, final boolean forward) {
		final EV3TouchSensor touchSensor = new EV3TouchSensor(touchSensorPort);
		final SampleProvider touchSampleProvider = touchSensor.getTouchMode();
		final float[] sample = new float[touchSampleProvider.sampleSize()];
		boolean limitDetected = false;
		while (!limitDetected) {
			Delay.msDelay(10);
			if (forward) {
				motor.forward();
			} else {
				motor.backward();
			}
			touchSampleProvider.fetchSample(sample, 0);
			limitDetected = (sample[0] == 1);
		}
		final int result = motor.getTachoCount();
		motor.stop();
		touchSensor.close();
		return result;
	}
}
