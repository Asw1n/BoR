package org.aswinmp.lejos.ev3.bandofrobots.pc.playlist;

import com.sun.istack.internal.NotNull;

/**
 * An entry for a playlist. Consists of a {@link BandConfiguration} with a
 * {@link PlaylistEntryState}.
 * @author Matthias Paul Scholz
 */
public class PlaylistEntry {

  @NotNull
  private final BandConfiguration bandConfiguration;
  private PlaylistEntryState state;

  /**
   * Constructor.
   * @param bandConfiguration
   *          the {@link BandConfiguration}
   */
  public PlaylistEntry(final BandConfiguration bandConfiguration) {
    this.bandConfiguration = bandConfiguration;
    assert bandConfiguration != null : "band configuration must not be null";
  }

  /**
   * Constructor.
   * @param bandConfiguration
   *          the {@link BandConfiguration}
   * @param state
   *          the initial {@link PlaylistEntryState}
   */
  public PlaylistEntry(final BandConfiguration bandConfiguration,
      final PlaylistEntryState state) {
    this(bandConfiguration);
    this.state = state;
  }

  public PlaylistEntryState getState() {
    return state;
  }

  public void setState(final PlaylistEntryState playlistEntryState) {
    this.state = playlistEntryState;
  }

  public BandConfiguration getBandConfiguration() {
    return bandConfiguration;
  }

  @Override
  public String toString() {
    return String.format("[%s: %s]", bandConfiguration, state);
  }

  public String dump() {
    return String.format("[%s\nstate: %s]", bandConfiguration.dump(), state);

  }
}
