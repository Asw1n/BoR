package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Song;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "channels", parameters = "", description = "dumps the channels mappings for the selected song")
public class DumpChannelsCommand extends AbstractBoRCommand {

  public DumpChannelsCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void dumpInstruments() throws BoRCommandException {
    final Song song = getBoRController().getSong();
    if (!song.isSet()) {
      throw new BoRCommandException("No song selected");
    }
    song.dumpChannels();

  }
}
