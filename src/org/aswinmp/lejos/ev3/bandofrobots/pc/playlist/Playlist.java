package org.aswinmp.lejos.ev3.bandofrobots.pc.playlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.istack.internal.NotNull;

/**
 * A playlist for BoR. Consists of a list of {@link PlaylistEntry}s.
 * @author Matthias Paul Scholz
 */
public class Playlist implements Iterable<PlaylistEntry> {

  private final List<PlaylistEntry> playlistEntries = new ArrayList<PlaylistEntry>();

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
    playlistEntries.add(playlistEntry);
  }

  public void remove(final PlaylistEntry playlistEntry) {
    playlistEntries.remove(playlistEntry);
  }

  public void reset() {
    playlistEntries.clear();
  }

  @Override
  public String toString() {
    return name;
  }

  public String dump() {
    final StringBuilder toStringBuilder = new StringBuilder(name).append("\n");
    for (final PlaylistEntry playlistEntry : playlistEntries) {
      toStringBuilder.append("\n").append(playlistEntry.dump()).append("\n");
    }
    return toStringBuilder.toString();
  }

  @Override
  public Iterator<PlaylistEntry> iterator() {
    return playlistEntries.iterator();
  }

}
