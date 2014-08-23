package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import PC.BoRController;
import PC.BrickHub;
import PC.InstrumentMusicianMap;
import PC.Song;

@ShellCommand(label = "assign", parameters = "brick_name instrument_channel", description = "assigns a connected brick to an instrument of the selected song")
public class AssignBrickCommand {

	private final BoRController boRController;

	public AssignBrickCommand(final BoRController boRController) {
		this.boRController = boRController;
	}

	@ShellExecute
	public void assignBrick(final String brickName,
			final String instrumentChannelText) {
		final Song song = boRController.getSong();
		if (song == null) {
			System.out.println("No song selected");
		} else {
			final int instrumentChannel = Integer
					.parseInt(instrumentChannelText);
			final InstrumentMusicianMap instrumentMusicianMap = findInstrument(
					song, instrumentChannel);
			if (instrumentMusicianMap == null) {
				System.out.println(String.format(
						"No instrument in song found matching channel %d",
						instrumentChannel));
			} else {
				try {
					final BrickInfo brickInfo = findBrickInfo(brickName);
					if (brickInfo == null) {
						System.out.println(String.format(
								"No brick connected matching '%s'", brickName));
					} else {
						instrumentMusicianMap.setBrick(new BrickHub(brickInfo));
						System.out.println(String.format(
								"Brick '%s' assigned to channel %d", brickName,
								instrumentChannel));
					}
				} catch (final IOException ioe) {
					System.err.println(ioe);
				}

			}
		}
	}

	private InstrumentMusicianMap findInstrument(final Song song,
			final int instrumentChannel) {
		InstrumentMusicianMap result = null;
		for (final InstrumentMusicianMap instrumentMusicianMap : song
				.getMapping()) {
			if (instrumentMusicianMap.getChannel() == instrumentChannel) {
				result = instrumentMusicianMap;
				break;
			}
		}
		return result;
	}

	private BrickInfo findBrickInfo(final String brickName) throws IOException {
		BrickInfo result = null;
		for (final lejos.hardware.BrickInfo info : BrickFinder.discover()) {
			if (brickName.equals(info.getName())) {
				result = info;
				break;
			}
		}
		return result;
	}
}
