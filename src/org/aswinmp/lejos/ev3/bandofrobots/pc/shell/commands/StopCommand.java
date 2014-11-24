package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "stop", parameters = "", description = "stops the song that is played currently")
public class StopCommand extends AbstractBoRCommand {

  public StopCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void stop() throws BoRCommandException {
    getBoRController().stop();
  }
}
