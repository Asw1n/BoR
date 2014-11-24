package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Song;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "play", parameters = "", description = "plays the selected song")
public class PlayCommand extends AbstractBoRCommand {

  public PlayCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void play() throws BoRCommandException {
    final Song song = getBoRController().getSong();
    if (!song.isSet()) {
      throw new BoRCommandException("No song selected");
    }
    getBoRController().play();

  }
}
