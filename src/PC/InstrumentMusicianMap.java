package PC;

/**
 * The Instrument Musican Map maps an instrument in a midi file to a remote
 * brick (arobot musician).
 * 
 * @author Aswin
 * 
 */
public class InstrumentMusicianMap {
	int channel = 0;
	int instrument = 0;
	boolean supress = false;
	BrickHub brickHub = null;
	int highestNote = 0;
	int lowestNote = 127;

	/**
	 * Constructor
	 * 
	 * @param file
	 *            Name of the Midi file for which the mapping is intended
	 * @param channel
	 *            Number of the Midi channel of the instrument is
	 * @param instrument
	 *            Number representing the instrument (See General Midi
	 *            instrument specification
	 */
	public InstrumentMusicianMap(final int channel, final int instrument) {
		this.channel = channel;
		this.instrument = instrument;
	}

	/**
	 * Returns the highest note for this instrument in the song
	 * 
	 * @return
	 */
	public int getHighestNote() {
		return highestNote;
	}

	protected void updateDynamicRange(final int note) {
		if (note < lowestNote)
			lowestNote = note;
		if (note > highestNote)
			highestNote = note;
	}

	protected void setHighestNote(final int highestNote) {
		this.highestNote = highestNote;
	}

	/**
	 * Returns the lowest note for this instrument in the song
	 * 
	 * @return
	 */
	public int getLowestNote() {
		return lowestNote;
	}

	protected void setLowestNote(final int lowestNote) {
		this.lowestNote = lowestNote;
	}

	/**
	 * Returns a BrickHub object describing the brick the instrument is mapped
	 * to
	 * 
	 * @return
	 */
	public BrickHub getBrick() {
		return brickHub;
	}

	/**
	 * Maps a Brick to an instrument
	 * 
	 * @param ev3
	 */
	public void setBrick(final BrickHub brickHub) {
		this.brickHub = brickHub;
	}

	/**
	 * Returns the channel for this mapping
	 * 
	 * @return
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * Returns the instrument for this mapping
	 * 
	 * @return
	 */
	public int getInstrument() {
		return instrument;
	}

	// TODO: probably misplaced here
	public boolean isEqual(final InstrumentMusicianMap other) {
		if (other.getChannel() == channel
				&& other.getInstrument() == instrument)
			return true;
		return false;
	}

	public boolean isSupressed() {
		return supress;
	}

	public void dump() {
		System.out.println("Instrument :" + instrument);
		System.out.println("\tChannel :" + channel);
		System.out.println("\tLowest note: " + lowestNote);
		System.out.println("\tHighest note: " + highestNote);
		if (brickHub == null)
			System.out.println("\tMapped to: -");
		else
			System.out.println("\tMapped to: " + brickHub.toString());
	}

}
