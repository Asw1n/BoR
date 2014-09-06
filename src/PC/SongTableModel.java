package PC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.table.AbstractTableModel;

/**
 * Model to show a IMmapping table in a table editor
 * 
 * @author Aswin
 * 
 */
public class SongTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -5686596864300068524L;
	List<InstrumentMusicianMap> instruments = new ArrayList<InstrumentMusicianMap>();
	Song thisSong = new Song();

	public SongTableModel() {

	}

	@Override
	public String getColumnName(final int col) {
		switch (col) {
		case 0:
			return "Instrument";
		case 1:
			return "Channel";
		case 2:
			return "EV3";
		case 3:
			return "Lowest tone";
		case 4:
			return "Highest tone";
		default:
			return "Error";
		}
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		if (columnIndex == 2)
			return BrickHub.class;
		return Integer.class;
	}

	@Override
	public boolean isCellEditable(final int row, final int col) {
		if (col == 2)
			return true;
		else
			return false;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		return thisSong.getNumberOfInstruments();
	}

	@Override
	public Object getValueAt(final int row, final int col) {
		final InstrumentMusicianMap instrument = thisSong.getInstrument(row);
		switch (col) {
		case 0:
			return new Integer(instrument.getInstrument());
		case 1:
			return new Integer(instrument.getChannel());
		case 2:
			return instrument.getBrick();
		case 3:
			return new Integer(instrument.getLowestNote());
		case 4:
			return new Integer(instrument.getHighestNote());
		default:
			return "Error";
		}
	}

	@Override
	public void setValueAt(final Object value, final int row, final int col) {
		if (col == 2) {
			final InstrumentMusicianMap instrument = thisSong
					.getInstrument(row);
			instrument.setBrick((BrickHub) value);
		}
	}

	public void setFile(final File file) {
		thisSong = new Song();
		try {
			thisSong.setSong(file);
			this.fireTableDataChanged();
		} catch (final InvalidMidiDataException | IOException exc) {
			System.err.println("Could not set song: " + exc);
		}
	}

	public Song getSong() {
		return thisSong;
	}

}
