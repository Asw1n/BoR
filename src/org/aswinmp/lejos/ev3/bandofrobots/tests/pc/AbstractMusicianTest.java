package org.aswinmp.lejos.ev3.bandofrobots.tests.pc;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.MusicianConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.player.BorPlayer;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.BandConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.Playlist;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntry;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntryState;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;

public abstract class AbstractMusicianTest extends AbstractBorPCTest {

  protected void runTest(final File midiFile, final MusicianConfiguration musicianConfiguration, final long playTimeInMilliseconds)
      throws IOException, MidiUnavailableException, InvalidMidiDataException,
      BoRCommandException {
    // create band configuration containing the specified MusicConfiguration
    // only
    final BandConfiguration bandConfiguration = new BandConfiguration(String
        .format("TestBandConfiguration_%s", musicianConfiguration
            .getBrickIdentifier()), midiFile);
    bandConfiguration.add(musicianConfiguration);
    // create playlist containing the band configuration only
    final PlaylistEntry playlistEntry = new PlaylistEntry(bandConfiguration, PlaylistEntryState.WAITING);
    final Playlist playlist = new Playlist(String.format("TestPlaylist_%s", musicianConfiguration
        .getBrickIdentifier()), playlistEntry);
    // instantiate BoRPlayer
    final BorPlayer borPlayer = new BorPlayer(createBoRController());
    // play band configuration
    borPlayer.play(playlist);
    // run test for specified time only
    stopPlayerAfterPeriod(borPlayer, playTimeInMilliseconds);
  }

}
