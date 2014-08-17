package PC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Song {
	private List<InstrumentMusicianMap> instruments;
	private File songFile;

	public Song() {
		instruments = new ArrayList<InstrumentMusicianMap>();
	}

	public void setSong(final File song) throws InvalidMidiDataException,
			IOException {
		this.songFile = song;
		instruments = new ArrayList<InstrumentMusicianMap>();
		scan();
	}

	public File getSong() {
		return songFile;
	}

	public String getFileName() {
		return songFile.getAbsolutePath();
	}

	protected InstrumentMusicianMap findInstrument(final int channel,
			final int instrument) {
		for (final InstrumentMusicianMap myInstrument : instruments) {
			if (myInstrument.getInstrument() == instrument
					&& myInstrument.getChannel() == channel)
				return myInstrument;
		}
		return null;
	}

	public int getNumberOfInstruments() {
		return instruments.size();
	}

	public InstrumentMusicianMap getInstrument(final int index) {
		return instruments.get(index);
	}

	/**
	 * This method scans the song for instruments that are used and determines
	 * the dynamic range of each instrument.
	 */
	private void scan() throws InvalidMidiDataException, IOException {
		InstrumentMusicianMap thisInstrument;
		final InstrumentMusicianMap[] channel = new InstrumentMusicianMap[16];

		final Sequence seq = MidiSystem.getSequence(songFile);
		final Track[] tracks = seq.getTracks();
		for (final Track track : tracks) {
			for (int i = 0; i < track.size(); i++) {
				final MidiEvent event = track.get(i);
				if (event.getMessage() instanceof ShortMessage) {
					final ShortMessage message = (ShortMessage) event
							.getMessage();
					if (message.getCommand() == ShortMessage.PROGRAM_CHANGE) {
						// a change of instrument is detected, if it new add it
						// to the instrument list
						thisInstrument = findInstrument(message.getChannel(),
								message.getData1());
						if (thisInstrument == null) {
							thisInstrument = new InstrumentMusicianMap(
									message.getChannel(), message.getData1());
							instruments.add(thisInstrument);
						}
						// make the instrument the current instrument on the
						// channel
						channel[thisInstrument.getChannel()] = thisInstrument;
					}
					if (message.getCommand() == ShortMessage.NOTE_ON) {
						// A note is detected, update the dynamic range for this
						// instrument
						channel[message.getChannel()]
								.updateDynamicRange(message.getData1());
						;
					}
				}
			}
		}

	}

	public void dump() {
		System.out.println(getFileName());
		for (final InstrumentMusicianMap thisInstrument : instruments) {
			thisInstrument.dump();
		}
	}

	public List<InstrumentMusicianMap> getMapping() {
		return instruments;
	}

	public File getFile() {
		return songFile;
	}

}
