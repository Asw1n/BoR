package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "stop-playlist", parameters = "", description = "stops the playlist that is played currently")
public class StopPlaylistCommand extends AbstractBoRCommand {

  public StopPlaylistCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void stop() throws BoRCommandException {
    try {
      getCommandContext().getBorPlayer().shutDown();
    } catch (final MidiUnavailableException mue) {
      throw new BoRCommandException(mue);
    }
  }
}
