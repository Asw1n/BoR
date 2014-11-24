package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import java.util.Random;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.Drumm3r.TorsoLocation;
import org.aswinmp.lejos.ev3.bandofrobots.utils.AbstractRandomlyDelayedExecutor;

/**
 * A runnable that randomly moves the torso of the {@link Drumm3r}.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class RandomlyDelayedMoveTorsoExecutor extends
		AbstractRandomlyDelayedExecutor {

	private final Drumm3r drumm3r;
	private final TorsoLocation[] availableTorsoLocations = TorsoLocation
			.values();

	/**
	 * Constructor.
	 * 
	 * @param randomMax
	 *            the upper limit for random milliseconds.
	 * @param drumm3r
	 *            the {@link Drumm3r}
	 */
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
