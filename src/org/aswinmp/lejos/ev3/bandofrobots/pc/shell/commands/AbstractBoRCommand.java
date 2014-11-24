package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

public abstract class AbstractBoRCommand {

  private final CommandContext commandContext;

  public AbstractBoRCommand(final CommandContext commandContext) {
    this.commandContext = commandContext;
  }

  protected final CommandContext getCommandContext() {
    return commandContext;
  }

  protected final BoRController getBoRController() throws BoRCommandException {
    try {
      return getCommandContext().getBoRController();
    } catch (final MidiUnavailableException mue) {
      throw new BoRCommandException(mue);
    }
  }
}
