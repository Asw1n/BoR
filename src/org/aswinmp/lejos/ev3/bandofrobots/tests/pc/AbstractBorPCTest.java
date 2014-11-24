package org.aswinmp.lejos.ev3.bandofrobots.tests.pc;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.aswinmp.lejos.ev3.bandofrobots.pc.player.BorPlayer;

public abstract class AbstractBorPCTest {

	// TODO find a better way to inject MIDI files
	protected static File createMidiFile(final String midiTestFileName) {
		final String fileSeparator = System.getProperty("file.separator");
		final String executionDir = System.getProperty("user.dir");
		return new File(String.format("%s%sMIDI%s%s", executionDir,
				fileSeparator, fileSeparator, midiTestFileName));
	}

	protected void stopPlayerAfterPeriod(final BorPlayer borPlayer,
			final long periodInMilliseconds) {
		System.out.println(String.format(
				"Will stop BoR player after %d milliseconds",
				periodInMilliseconds));
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println(String.format(
						"%d milliseconds have elapsed => stopping BoR player",
						periodInMilliseconds));
				borPlayer.shutDown();
				timer.cancel();
			}
		}, periodInMilliseconds);
	}
}
