package org.aswinmp.lejos.ev3.bandofrobots.pc.configuration;

import com.sun.istack.internal.NotNull;

/**
 * The configuration for a musician. Maps bricks to channels with a particular
 * {@link AssignmentType}.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class MusicianConfiguration {

	@NotNull
	private final String brickIdentifier;
	@NotNull
	private final int channel;
	@NotNull
	private final AssignmentType assignmentType;

	/**
	 * Constructor.
	 * 
	 * @param brickIdentifier
	 *            the identifier of the brick.
	 * @param channel
	 *            the channel
	 * @param assignmentType
	 *            the {@link AssignmentType}
	 */
	public MusicianConfiguration(final String brickIdentifier,
			final int channel, final AssignmentType assignmentType) {
		this.brickIdentifier = brickIdentifier;
		this.channel = channel;
		this.assignmentType = assignmentType;
		assert brickIdentifier != null : "Identifier of brick must not be null";
		assert channel > -1 : String.format("invalid channel %d", channel);
		assert assignmentType != null : "Assignment type must not be null";
	}

	public String getBrickIdentifier() {
		return brickIdentifier;
	}

	public int getChannel() {
		return channel;
	}

	public AssignmentType getAssignmentType() {
		return assignmentType;
	}

	@Override
	public String toString() {
		return String.format("Brick '%s' on channel %d as %s", brickIdentifier,
				channel, assignmentType);
	}

}
