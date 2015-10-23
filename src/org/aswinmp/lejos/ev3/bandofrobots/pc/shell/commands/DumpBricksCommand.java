package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.IOException;

import lejos.hardware.BrickFinder;

import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "bricks", parameters = "", description = "dumps the connected bricks")
public class DumpBricksCommand extends AbstractBoRCommand {

  public DumpBricksCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void dumpBricks() throws BoRCommandException {
    System.out.println("Connected bricks:");
    try {
      for (final lejos.hardware.BrickInfo info : BrickFinder.discover()) {
        System.out.println(String.format("%s (%s)", info.getName(), info
            .getIPAddress()));
      }
    } catch (final Exception ioe) {
      throw new BoRCommandException(ioe);
    }
  }
}
