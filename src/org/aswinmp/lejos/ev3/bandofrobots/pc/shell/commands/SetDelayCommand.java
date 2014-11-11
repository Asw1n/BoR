package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;

@ShellCommand(label = "delay", parameters = "time", description = "sets the delay for the music")
public class SetDelayCommand {

	private final BoRController boRController;

	public SetDelayCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void setDelay(final String time)
			throws BoRCommandException {
	  long delay=0;
	   try {
	     delay = Long.parseLong(time);
	     boRController.setSoundDelay(delay);
	     System.out.println("Delay set to "
	         + boRController.getSoundDelay());
	    } catch (final NumberFormatException nfe) {
	      throw new BoRCommandException(nfe);
	    }
	}
}
