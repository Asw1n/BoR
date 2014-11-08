package org.aswinmp.lejos.ev3.bandofrobots.tests.pc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.AssignMusicianCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.PlayCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.QuitCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.SelectSongCommand;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands.StopCommand;

public abstract class AbstractMusicianTest {

	private final String testMIDIFile;
	private final BrickChannelAssignment[] brickChannelAssignments;

	protected AbstractMusicianTest(final String testMIDIFile,
			final BrickChannelAssignment... brickChannelAssignments) {
		this.testMIDIFile = testMIDIFile;
		this.brickChannelAssignments = brickChannelAssignments;
	}

	protected void runTest(final long playTimeInMilliseconds)
			throws IOException, MidiUnavailableException,
			InvalidMidiDataException, BoRCommandException {
		// instantiate and configure BoR controller
		final BoRController boRController = new BoRController();
		boRController.setSequencer(MidiSystem.getSequencer().getDeviceInfo());
		boRController.setSynthesizer(MidiSystem.getSynthesizer()
				.getDeviceInfo());
		// set song
		new SelectSongCommand(boRController).selectSong(testMIDIFile);
		// assign bricks
		for (final BrickChannelAssignment brickChannelAssignment : brickChannelAssignments) {
			new AssignMusicianCommand(boRController).assignBrick(
					brickChannelAssignment.getBrickName(),
					brickChannelAssignment.getChannel());
		}
		// play song for some time, then quit
		new PlayCommand(boRController).play();
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				new StopCommand(boRController).stop();
				new QuitCommand(boRController).quit();
				timer.cancel();
			}
		}, playTimeInMilliseconds);
	}

	// TODO find a better way to inject MIDI file
	protected static String createTestFilePath(final String midiTestFileName) {
		final String fileSeparator = System.getProperty("file.separator");
		final String executionDir = System.getProperty("user.dir");
		return String.format("%s%sMIDI%s%s", executionDir, fileSeparator,
				fileSeparator, midiTestFileName);
	}
}
