package org.aswinmp.lejos.ev3.bandofrobots.tests.brick;

import org.aswinmp.lejos.ev3.bandofrobots.utils.AbstractRandomlyDelayedExecutor;

/**
 * A runnable that randomly executes a {@link RobotAction}.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class RandomlyDelayedActionExecutor extends
		AbstractRandomlyDelayedExecutor {

	private final RobotAction robotAction;

	/**
	 * Constructor.
	 * 
	 * @param randomMax
	 *            the upper limit for random delays between the action in
	 *            milliseconds
	 * @param robotAction
	 *            the {@link RobotAction} to execute
	 */
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
