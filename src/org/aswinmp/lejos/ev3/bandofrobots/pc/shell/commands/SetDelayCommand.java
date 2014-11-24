package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "delay", parameters = "time", description = "sets the delay for the music. Delay must be a long value")
public class SetDelayCommand extends AbstractBoRCommand {

  public SetDelayCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void setDelay(final String time) throws BoRCommandException {
    long delay = 0;
    try {
      delay = Long.parseLong(time);
      getBoRController().setSoundDelay(delay);
      System.out.println(String.format("Delay set to %d", delay));
    } catch (final NumberFormatException nfe) {
      throw new BoRCommandException(String.format("Invalid format '%s'for delay (must be a long value)", time));
    }
  }
}
