package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Channels;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Song;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;

@ShellCommand(label = "metronome", parameters = "channel", description = "sets the metronome channel of the selected song")
public class SetMetronomeCommand {

	private final BoRController boRController;

	public SetMetronomeCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void setMetronome(final String channelName)
			throws BoRCommandException {
		final Song song = boRController.getSong();
		if (!song.isSet()) {
			throw new BoRCommandException("No song selected");
		}
		final int channelNo = Integer.parseInt(channelName);
		final Channels channels = song.getChannels();

		if (channelNo < 0 || channelNo >= Channels.CHANNELCOUNT) {
			throw new BoRCommandException(String.format("invalid channel %d",
					channelNo));
		}
		if (!channels.hasInstrument(channelNo)) {
			throw new BoRCommandException(String.format(
					"No instrument on channel %d", channelNo));
		}
		song.setMetronomeChannel(channelNo);
		System.out.println(String.format("Set channel '%s' to metronome",
				channelNo));

	}

}
