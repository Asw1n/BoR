package org.aswinmp.lejos.ev3.bandofrobots.musicians.sing3r;

import org.aswinmp.lejos.ev3.bandofrobots.utils.AbstractRandomlyDelayedExecutor;

public class RandomlyDelayedMicroExecutor extends
		AbstractRandomlyDelayedExecutor {

	private final Sing3r sing3r;

	public RandomlyDelayedMicroExecutor(final int randomMax, final Sing3r sing3r) {
		super(randomMax);
		this.sing3r = sing3r;
	}

	@Override
	protected void execute() {
		// move
		sing3r.swingArms(false);
	}

}
