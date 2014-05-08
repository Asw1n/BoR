package PC;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/** Model to show a IMmapping table in a table editor
 * @author Aswin
 *
 */
public class SongTableModel extends AbstractTableModel{
  /**
   * 
   */
  private static final long serialVersionUID = -5686596864300068524L;
  List<InstrumentMusicianMap> instruments = new ArrayList<InstrumentMusicianMap>();
  Song thisSong = new Song();
  
  
  public SongTableModel() {
    
  }
  
  
  @Override
  public String getColumnName(int col) {
    switch (col) {
      case 0: return "Instrument";
      case 1: return "Channel";
      case 2: return "EV3";
      case 3: return "Lowest tone";
      case 4: return "Highest tone";
      default: return "Error";
    }
}
  
  public Class getColumnClass(int columnIndex) {
    if (columnIndex==2) return BrickHub.class;
    return Integer.class;
  }
  
  public boolean isCellEditable(int row, int col) { 
    if (col==2 ) return true;
    else return false;
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
  public Object getValueAt(int row, int col) {
    InstrumentMusicianMap instrument=thisSong.getInstrument(row);
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
  public void setValueAt(Object value, int row, int col) {
    if (col==2) {
    InstrumentMusicianMap instrument=thisSong.getInstrument(row);
    instrument.setBrick((BrickHub)value);
    }
  }
  
  public void setFile(File file) {
    thisSong=new Song();
    thisSong.setSong(file);
    this.fireTableDataChanged();
  }


  public Song getSong() {
    return thisSong;
  }
 
}
