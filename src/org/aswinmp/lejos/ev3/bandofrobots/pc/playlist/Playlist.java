package org.aswinmp.lejos.ev3.bandofrobots.pc.playlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.istack.internal.NotNull;

/**
 * 
 * A playlist for BoR. Consists of a list of {@link PlaylistEntry}s.
 * 
 * @author Matthias Paul Scholz
 * 
 */
public class Playlist implements Iterable<PlaylistEntry> {

	private final List<PlaylistEntry> playlistEntrys = new ArrayList<PlaylistEntry>();

	@NotNull
	private final String name;

	public Playlist(final String name) {
		this.name = name;
		assert name != null : "Name must not be null";
	}

	public Playlist(final String name, final PlaylistEntry... playlistEntrys) {
		this(name);
		for (final PlaylistEntry playlistEntry : playlistEntrys) {
			add(playlistEntry);
		}
	}

	public void add(final PlaylistEntry playlistEntry) {
		playlistEntrys.add(playlistEntry);
	}

	public void remove(final PlaylistEntry playlistEntry) {
		playlistEntrys.remove(playlistEntry);
	}

	public void reset() {
		playlistEntrys.clear();
	}

	@Override
	public String toString() {
		return name;
	}

	public String dump() {
		final StringBuilder toStringBuilder = new StringBuilder(name)
				.append("\nPlaylist entries:");
		for (final PlaylistEntry playlistEntry : playlistEntrys) {
			toStringBuilder.append("\n").append(playlistEntry);
		}
		toStringBuilder.append("\n----");
		return toStringBuilder.toString();
	}

	@Override
	public Iterator<PlaylistEntry> iterator() {
		return playlistEntrys.iterator();
	}

}
