package org.aswinmp.lejos.ev3.bandofrobots.tests.pc.drumm3r;

public class BrickChannelAssignment {

	private final String brickName;
	private final int channel;

	public BrickChannelAssignment(final String brickName, final int channel) {
		this.brickName = brickName;
		this.channel = channel;
	}

	public String getBrickName() {
		return brickName;
	}

	public int getChannel() {
		return channel;
	}

}
