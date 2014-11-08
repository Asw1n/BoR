package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import java.util.Random;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r;
import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r.TorsoLocation;
import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.AbstractRandomlyDelayedExecutor;

public class RandomlyDelayedMoveTorsoExecutor extends
		AbstractRandomlyDelayedExecutor {

	private final Drumm3r drumm3r;
	private final TorsoLocation[] availableTorsoLocations = TorsoLocation
			.values();

	public RandomlyDelayedMoveTorsoExecutor(final int randomMax,
			final Drumm3r drumm3r) {
		super(randomMax);
		this.drumm3r = drumm3r;
	}

	@Override
	protected void execute() {
		// create random torso location
		final int randomEnumIndex = new Random()
				.nextInt(availableTorsoLocations.length);
		final TorsoLocation randomTorsoLocation = availableTorsoLocations[randomEnumIndex];
		// move torso
		drumm3r.moveTorsoTo(randomTorsoLocation);
	}
}
