package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.player.BorPlayer;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.Playlist;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntry;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntryState;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.persistence.PlaylistReader;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "start-playlist", parameters = "path_to_playlist_file", description = "plays the specified playlist")
public class StartPlaylistCommand extends AbstractBoRCommand {

  public StartPlaylistCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void play(final String filePath) throws IOException,
      MidiUnavailableException {
    // read playlist from file
    final Playlist playlist = new PlaylistReader()
        .readFromFile(new File(filePath));
    // dump playlist
    System.out.println("Will play playlist:");
    System.out.println(playlist.dump());
    // configure playlist for playing
    final Iterator<PlaylistEntry> playlistEntries = playlist.iterator();
    while (playlistEntries.hasNext()) {
      playlistEntries.next().setState(PlaylistEntryState.WAITING);
    }
    // instantiate BoRPlayer
    final BorPlayer borPlayer = getCommandContext().getBorPlayer();
    // play playlist in a separate thread, so we can stop it
    new Thread(new Runnable() {
      @Override
      public void run() {
        borPlayer.play(playlist);
      }
    }).start();
  }
}
