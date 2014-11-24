package org.aswinmp.lejos.ev3.bandofrobots.tests.pc.playlist;

import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.AssignmentType;
import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.MusicianConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.player.BorPlayer;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.BandConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.Playlist;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntry;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntryState;
import org.aswinmp.lejos.ev3.bandofrobots.tests.pc.AbstractBorPCTest;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BorFilePathCreator;

public class PlaylistTest extends AbstractBorPCTest {

  private static final String MIDI_FILE_NAME_1 = "dire_straits-sultans_of_swing.mid";
  private static final String MIDI_FILE_NAME_2 = "adele-set_fire_to_the_rain.mid";
  private static final long TEST_DURATION = 10000;

  public static void main(final String[] args) {
    try {
      new PlaylistTest().runTest();
    } catch (final MidiUnavailableException muExc) {
      System.err.println(muExc);
    }
  }

  private void runTest() throws MidiUnavailableException {
    // create playlist
    final BandConfiguration bandConfiguration1 = new BandConfiguration("TestDrumm3rSing3r", BorFilePathCreator
        .assembleAbsoluteFileFromUserDir("MIDI", MIDI_FILE_NAME_1));
    bandConfiguration1
        .add(new MusicianConfiguration("Drumm3r", 9, AssignmentType.MUSICIAN));
    bandConfiguration1
        .add(new MusicianConfiguration("Sing3r", 5, AssignmentType.SINGER));
    final PlaylistEntry playlistEntry1 = new PlaylistEntry(bandConfiguration1, PlaylistEntryState.WAITING);
    final BandConfiguration bandConfiguration2 = new BandConfiguration("TestDrumm3rSing3r", BorFilePathCreator
        .assembleAbsoluteFileFromUserDir("MIDI", MIDI_FILE_NAME_2));
    bandConfiguration2
        .add(new MusicianConfiguration("Drumm3r", 9, AssignmentType.MUSICIAN));
    bandConfiguration2
        .add(new MusicianConfiguration("Sing3r", 5, AssignmentType.SINGER));
    final PlaylistEntry playlistEntry2 = new PlaylistEntry(bandConfiguration2, PlaylistEntryState.WAITING);
    final Playlist playlist = new Playlist("PlaylistTest", playlistEntry1, playlistEntry2);
    // instantiate BoRPlayer
    final BorPlayer borPlayer = new BorPlayer(createBoRController());
    // play playlist in a separate thread, so we can stop it
    new Thread(new Runnable() {
      @Override
      public void run() {
        borPlayer.play(playlist);
      }
    }).start();
    // run test for specified time only
    stopPlayerAfterPeriod(borPlayer, TEST_DURATION);
  }

}
