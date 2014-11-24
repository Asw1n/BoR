package org.aswinmp.lejos.ev3.bandofrobots.pc.playlist;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.MusicianConfiguration;

import com.sun.istack.internal.NotNull;

/**
 * A configuration for the band. Maps bricks to channels of a particular MIDI
 * file.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class BandConfiguration implements Iterable<MusicianConfiguration> {

	private final Collection<MusicianConfiguration> musicianConfigurations = new HashSet<MusicianConfiguration>();

	@NotNull
	private final String name;
	private final long delay;
	@NotNull
	private final File midiFile;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the configuration
	 * @param midiFile
	 *            the MIDI {@link File}
	 */
	public BandConfiguration(final String name, final File midiFile) {
		this(name, midiFile, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the configuration
	 * @param midiFile
	 *            the MIDI {@link File}
	 * @param delay
	 *            the delay between server and brick musicians
	 */
	public BandConfiguration(final String name, final File midiFile,
			final long delay) {
		this.name = name;
		this.delay = delay;
		this.midiFile = midiFile;
		assert name != null : "Name must not be null";
		assert midiFile != null : "MIDI file must not be null";
	}

	public void add(final MusicianConfiguration musicianConfiguration) {
		musicianConfigurations.add(musicianConfiguration);
	}

	public void remove(final MusicianConfiguration musicianConfiguration) {
		musicianConfigurations.remove(musicianConfiguration);
	}

	/**
	 * Resets the configuration to the initial state. Removes all
	 * {@link MusicianConfiguration}s.
	 */
	public void reset() {
		musicianConfigurations.clear();
	}

	public String getName() {
		return name;
	}

	public long getDelay() {
		return delay;
	}

	public File getMidiFile() {
		return midiFile;
	}

	@Override
	public String toString() {
		return String.format("'%s' [%s]", name, midiFile);
	}

	@Override
	public Iterator<MusicianConfiguration> iterator() {
		return musicianConfigurations.iterator();
	}

	public String dump() {
		final StringBuilder toStringBuilder = new StringBuilder(name)
				.append(": ").append(midiFile).append("\nAssigned musicians:");
		for (final MusicianConfiguration musicianConfiguration : musicianConfigurations) {
			toStringBuilder.append("\n").append(musicianConfiguration);
		}
		toStringBuilder.append("\n----");
		return toStringBuilder.toString();
	}

}
