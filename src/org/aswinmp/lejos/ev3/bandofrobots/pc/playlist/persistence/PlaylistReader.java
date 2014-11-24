package org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.AssignmentType;
import org.aswinmp.lejos.ev3.bandofrobots.pc.configuration.MusicianConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.BandConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.Playlist;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntry;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BorFilePathCreator;

/**
 * A simple reader for playlists persisted in a text file.
 * @author Matthias Paul Scholz
 */
public class PlaylistReader {

  private static final String SONG_MARKER = "song";
  private static final String MUSICIAN_MARKER = "musician";
  private static final String SINGER_MARKER = "singer";

  /**
   * Reads a {@link Playlist} from a text file.
   * @param playlistFile
   *          the text file
   * @return the {@link Playlist}
   * @throws IOException
   *           thrown in case the file can't be found or has invalid content
   */
  public Playlist readFromFile(final File playlistFile) throws IOException {
    final Playlist result = new Playlist(playlistFile.getName());
    // open file and parse lines
    try (BufferedReader reader = new BufferedReader(new FileReader(playlistFile))) {
      // read playlists
      PlaylistEntry playlistEntry;
      while (null != (playlistEntry = readPlaylistEntry(reader))) {
        result.add(playlistEntry);
      }
    }
    return result;
  }

  private PlaylistEntry readPlaylistEntry(final BufferedReader reader)
      throws IOException {
    PlaylistEntry result = null;
    BandConfiguration bandConfiguration = null;
    final Set<MusicianConfiguration> musicianConfigurations = new HashSet<MusicianConfiguration>();
    String line;
    do {
      line = reader.readLine();
      if (isSongLine(line)) {
        bandConfiguration = parseSongLine(line);
      } else if (isMusicianLine(line)) {
        musicianConfigurations
            .add(parseAssignmentLine(line, AssignmentType.MUSICIAN));
      } else if (isSingerLine(line)) {
        musicianConfigurations
            .add(parseAssignmentLine(line, AssignmentType.SINGER));
      }
    } while (!isEntrySeparator(line));
    /*
     * in case a song line has been found, add musician configurations and
     * create play list entry
     */
    if (bandConfiguration == null) {
      if (!musicianConfigurations.isEmpty()) {
        throw new IOException(String.format("Found musician configurations '%s' without an associated song", musicianConfigurations
            .toArray()));
      }
    } else {
      for (final MusicianConfiguration musicianConfiguration : musicianConfigurations) {
        bandConfiguration.add(musicianConfiguration);
      }
      result = new PlaylistEntry(bandConfiguration);
    }
    return result;
  }

  private BandConfiguration parseSongLine(final String songLine)
      throws IOException {
    final String[] lineMembers = songLine.split(" ");
    if (lineMembers.length != 2) {
      throw new IOException(String.format("Invalid song line '%s'", songLine));
    }
    // we expect all MIDI files to be located in the folder 'MIDI'
    final File midiFile = BorFilePathCreator
        .assembleAbsoluteFileFromUserDir("MIDI", lineMembers[1]);
    // we use the name of the MIDI file as the name of the band configuration
    return new BandConfiguration(midiFile.getName(), midiFile);
  }

  private MusicianConfiguration parseAssignmentLine(final String musicianLine, final AssignmentType assignmentType)
      throws IOException {
    final String[] lineMembers = musicianLine.split(" ");
    if (lineMembers.length != 3) {
      throw new IOException(String.format("Invalid assignment line '%s'", musicianLine));
    }
    final String brickIdentifier = lineMembers[1];
    int channel;
    try {
      channel = Integer.parseInt(lineMembers[2]);
    } catch (final NumberFormatException nfe) {
      throw new IOException(String.format("Invalid channel number '%s' in assignment line '%s'", lineMembers[2], musicianLine));
    }
    return new MusicianConfiguration(brickIdentifier, channel, assignmentType);
  }

  private boolean isEntrySeparator(final String line) {
    return line == null || line.isEmpty();
  }

  private boolean isSongLine(final String line) {
    return line != null && line.startsWith(SONG_MARKER);
  }

  private boolean isMusicianLine(final String line) {
    return line != null && line.startsWith(MUSICIAN_MARKER);
  }

  private boolean isSingerLine(final String line) {
    return line != null && line.startsWith(SINGER_MARKER);
  }

}
