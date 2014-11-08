package org.aswinmp.lejos.ev3.bandofrobots.tests.brick.drumm3r;

import org.aswinmp.lejos.ev3.bandofrobots.tests.brick.AbstractRandomlyDelayedExecutor;

public class RandomlyDelayedActionExecutor extends
		AbstractRandomlyDelayedExecutor {

	private final Drumm3rAction drumm3rAction;

	public RandomlyDelayedActionExecutor(final Drumm3rAction drumm3rAction) {
		this.drumm3rAction = drumm3rAction;
	}

	@Override
	protected void execute() {
		drumm3rAction.execute();
	}

}
