package org.aswinmp.lejos.ev3.bandofrobots.pc.player;

import java.io.IOException;
import java.util.Iterator;

import javax.sound.midi.InvalidMidiDataException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Song;
import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.MusicianConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.BandConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.Playlist;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntry;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntryState;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.AssignMusicianCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.AssignSingerCommand;

/**
 * A player that is able to play the entries of a {@link Playlist} in sequence.
 * @author Matthias Paul Scholz
 */
public class BorPlayer {

  private Playlist playlist;
  private final BoRController boRController;

  public BorPlayer(final BoRController boRController) {
    super();
    this.boRController = boRController;
  }

  /**
   * Plays the configured {@link Playlist}.
   */
  public void play(final Playlist playlist) {
    System.out.println(String.format("Playing playlist '%s'", playlist));
    this.playlist = playlist;
    final Iterator<PlaylistEntry> iterator = playlist.iterator();
    while (iterator.hasNext()) {
      final PlaylistEntry playlistEntry = iterator.next();
      if (PlaylistEntryState.WAITING == playlistEntry.getState()) {
        final BandConfiguration bandConfiguration = playlistEntry
            .getBandConfiguration();
        try {
          play(bandConfiguration);
          playlistEntry.setState(PlaylistEntryState.RUNNING);
        } catch (BoRCommandException | IOException | InvalidMidiDataException exc) {
          System.err
              .println(String
                  .format("Could not play band configuration '%s': %s", bandConfiguration, exc));
        }
        // wait until band configuration has stopped playing
        waitForCurrentBandConfigurationStopped();
        // set proper state
        playlistEntry.setState(PlaylistEntryState.COMPLETE);
      }
    }
    System.out.println("Playlist complete");
  }

  // /**
  // * Stops playing the play list.
  // */
  // public void stop() {
  // System.out.println("Stopping player");
  // boRController.stop();
  // }

  /**
   * Cancels the running and all waiting playlist entries and shuts down the
   * player.
   */
  public void shutDown() {
    System.out.println("Shutting down player");
    boRController.stop();
    // cancel all waiting playlist entries
    if (playlist != null) {
      final Iterator<PlaylistEntry> iterator = playlist.iterator();
      while (iterator.hasNext()) {
        final PlaylistEntry playlistEntry = iterator.next();
        if ((PlaylistEntryState.WAITING == playlistEntry.getState())
            || (PlaylistEntryState.RUNNING == playlistEntry.getState())) {
          System.out.println(String.format("Cancelling %s", playlistEntry));
          playlistEntry.setState(PlaylistEntryState.CANCELLED);
        }
      }
    }
    // close controller
    boRController.close();
  }

  private void play(final BandConfiguration bandConfiguration)
      throws BoRCommandException, IOException, InvalidMidiDataException {
    System.out.println(String
        .format("Playing band configuration %s", bandConfiguration));
    // configure BoR controller
    boRController.setSoundDelay(bandConfiguration.getDelay());
    // set song
    final Song song = boRController.getSong();
    song.setFilePath(bandConfiguration.getMidiFile().getAbsolutePath());
    // assign musicians
    System.out.println("Assigning musicians");
    final Iterator<MusicianConfiguration> iterator = bandConfiguration
        .iterator();
    while (iterator.hasNext()) {
      final MusicianConfiguration musicianConfiguration = iterator.next();
      try {
        assignMusician(musicianConfiguration, boRController);
      } catch (final BoRCommandException borExc) {
        System.err
            .println(String
                .format("Could not assign musician '%s': %s", musicianConfiguration, borExc));
      }
    }
    // play song
    System.out.println(String.format("Playing '%s'...", bandConfiguration
        .getName()));
    boRController.play();
  }

  private void assignMusician(final MusicianConfiguration musicianConfiguration, final BoRController boRController)
      throws BoRCommandException {
    // assign musicians according to assignment type
    // TODO use visitor pattern
    System.out.println(String.format("Assigning %s", musicianConfiguration));
    final CommandContext commandContext = new CommandContext();
    switch (musicianConfiguration.getAssignmentType()) {
    case MUSICIAN:
      new AssignMusicianCommand(commandContext)
          .assignBrick(musicianConfiguration.getBrickIdentifier(), musicianConfiguration
              .getChannel());
      break;
    case SINGER:
      new AssignSingerCommand(commandContext).assignVoice(musicianConfiguration
          .getBrickIdentifier(), musicianConfiguration.getChannel());
      break;
    default:
      throw new UnsupportedOperationException(String.format("Invalid assigment type %s", musicianConfiguration
          .getAssignmentType()));
    }
  }

  private void waitForCurrentBandConfigurationStopped() {
    do {
      try {
        Thread.sleep(100);
      } catch (final InterruptedException ie) {
        System.err.println(ie);
      }
    } while (boRController.isPlaying());
  }

}
