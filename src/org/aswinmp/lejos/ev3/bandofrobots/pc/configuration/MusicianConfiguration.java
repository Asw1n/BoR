package org.aswinmp.lejos.ev3.bandofrobots.pc.configuration;

import java.io.File;

import com.sun.istack.internal.NotNull;

public class MusicianConfiguration {

	@NotNull
	private final String brickIdentifier;
	@NotNull
	private final int channel;
	@NotNull
	private final File midiFile;

	public MusicianConfiguration(final File midiFile,
			final String brickIdentifier, final int channel) {
		this.brickIdentifier = brickIdentifier;
		this.channel = channel;
		this.midiFile = midiFile;
		assert brickIdentifier != null : "Identifier of brick must not be null";
		assert channel > -1 : String.format("invalid channel %d", channel);
		assert midiFile != null : "MIDI file must not be null";
	}

	public File getMidiFile() {
		return midiFile;
	}

	public String getBrickIdentifier() {
		return brickIdentifier;
	}

	public int getChannel() {
		return channel;
	}

}
