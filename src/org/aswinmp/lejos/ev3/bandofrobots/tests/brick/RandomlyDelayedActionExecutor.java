package org.aswinmp.lejos.ev3.bandofrobots.tests.brick;

public class RandomlyDelayedActionExecutor extends
		AbstractRandomlyDelayedExecutor {

	private final RobotAction robotAction;

	public RandomlyDelayedActionExecutor(final int randomMax,
			final RobotAction robotAction) {
		super(randomMax);
		this.robotAction = robotAction;
	}

	@Override
	protected void execute() {
		robotAction.execute();
	}

}
