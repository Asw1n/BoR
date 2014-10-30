package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r.test;


public class RandomlyDelayedActionExecutor extends
		AbstractRandomlyDelayedExecutor {

	interface Drumm3rAction {
		void execute();
	}

	private final Drumm3rAction drumm3rAction;

	public RandomlyDelayedActionExecutor(final Drumm3rAction drumm3rAction) {
		this.drumm3rAction = drumm3rAction;
	}

	@Override
	protected void execute() {
		drumm3rAction.execute();
	}

}
