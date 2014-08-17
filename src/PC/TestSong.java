package PC;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

public class TestSong {
	Song mySong = new Song();

	public static void main(final String[] args) {
		final TestSong foobar = new TestSong();
		try {
			foobar.mySong.setSong(new File("MIDI/blues_tempo_changes.mid"));
		} catch (final InvalidMidiDataException | IOException exc) {
			System.err.println("Could not set song: " + exc);
		}
	}

}
