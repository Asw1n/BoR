package PC;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class InstrumentTableModel extends AbstractTableModel{
  /**
   * 
   */
  private static final long serialVersionUID = -5686596864300068524L;
  List<InstrumentMap> instruments = new ArrayList<InstrumentMap>();

  public InstrumentTableModel() {
    // TODO Auto-generated constructor stub
  }
  
  public String getColumnName(int col) {
    switch (col) {
      case 0: return "Channel";
      case 1: return "Instrument";
      case 2: return "EV3";
      default: return "Error";
    }
}
  
  public boolean isCellEditable(int row, int col)
  { return false; }
  

  @Override
  public int getColumnCount() {
    // TODO Auto-generated method stub
    return 3;
  }

  @Override
  public int getRowCount() {
    // TODO Auto-generated method stub
    return instruments.size();
  }

  @Override
  public Object getValueAt(int row, int col) {
    InstrumentMap instrument=(InstrumentMap) instruments.get(row);
    switch (col) {
      case 0:
        return new Integer(instrument.getChannel());
      case 1:
        return new Integer(instrument.getInstrument());
      case 2:
        if (instrument.getEv3()==null) return "Not Assigned";
        else return instrument.getEv3().getName();
      default:
        return "Error";
    }
  }
  
  public void setFile(File file) {
    instruments=MidiUtil.getInstruments(file);
  }

}
