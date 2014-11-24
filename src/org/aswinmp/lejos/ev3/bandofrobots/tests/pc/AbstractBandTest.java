package org.aswinmp.lejos.ev3.bandofrobots.tests.pc;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.player.BorPlayer;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.BandConfiguration;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.Playlist;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntry;
import org.aswinmp.lejos.ev3.bandofrobots.pc.playlist.PlaylistEntryState;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;

public abstract class AbstractBandTest extends AbstractBorPCTest {

	protected void runTest(final BandConfiguration bandConfiguration,
			final long playTimeInMilliseconds) throws IOException,
			MidiUnavailableException, InvalidMidiDataException,
			BoRCommandException {
		// create playlist containing the band configuration only
		final PlaylistEntry playlistEntry = new PlaylistEntry(
				bandConfiguration, PlaylistEntryState.WAITING);
		final Playlist playlist = new Playlist(String.format("TestPlaylist_%s",
				bandConfiguration.getName()), playlistEntry);
		// instantiate BoRPlayer
		final BorPlayer borPlayer = new BorPlayer(playlist);
		// play band configuration
		borPlayer.play();
		// run test for specified time only
		stopPlayerAfterPeriod(borPlayer, playTimeInMilliseconds);
	}

}
