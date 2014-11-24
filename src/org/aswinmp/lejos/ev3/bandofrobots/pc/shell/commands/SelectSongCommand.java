package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Song;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "song", parameters = "path_to_MIDI_file", description = "selects a song serialized in a MIDI file")
public class SelectSongCommand extends AbstractBoRCommand {

  public SelectSongCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void selectSong(final String filePath)
      throws InvalidMidiDataException, IOException, BoRCommandException {
    final Song song = getBoRController().getSong();
    song.setFilePath(filePath);
    System.out.println(String.format("Set song to '%s'", song.getFilePath()));
  }
}
