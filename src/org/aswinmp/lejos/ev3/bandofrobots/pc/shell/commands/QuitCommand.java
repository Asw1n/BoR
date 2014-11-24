package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "quit", parameters = "", description = "quits the shell")
public class QuitCommand extends AbstractBoRCommand {

  public QuitCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void quit() throws BoRCommandException {
    getBoRController().close();
  }
}
