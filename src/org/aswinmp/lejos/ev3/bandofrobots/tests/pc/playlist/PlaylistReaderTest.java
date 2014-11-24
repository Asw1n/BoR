package org.aswinmp.lejos.ev3.bandofrobots.tests.pc.playlist;

import java.io.IOException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.Playlist;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.persistence.PlaylistReader;
import org.aswinmp.lejos.ev3.bandofrobots.tests.pc.AbstractBorPCTest;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BorFilePathCreator;

public class PlaylistReaderTest extends AbstractBorPCTest {

  private static final String PLAYLIST_TEST_FILE = "playlist1.txt";

  public static void main(final String[] args) {
    try {
      // read playlist from file
      final Playlist playlist = new PlaylistReader()
          .readFromFile(BorFilePathCreator
              .assembleAbsoluteFileFromUserDir("playlists", PLAYLIST_TEST_FILE));
      // assert result
      System.out.println(playlist.dump());
    } catch (final IOException ioe) {
      System.err.println(ioe);
    }

  }

}
