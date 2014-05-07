package PC;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/** Model to show a IMmapping table in a table editor
 * @author Aswin
 *
 */
public class IMmapModel extends AbstractTableModel{
  /**
   * 
   */
  private static final long serialVersionUID = -5686596864300068524L;
  List<IMMap> instruments = new ArrayList<IMMap>();

  public IMmapModel() {
    
  }
  
  
  @Override
  public String getColumnName(int col) {
    switch (col) {
      case 0: return "Instrument";
      case 1: return "Channel";
      case 2: return "EV3";
      case 3: return "Supress";
      default: return "Error";
    }
}
  
  public Class getColumnClass(int columnIndex) {
    if (columnIndex==2) return BrickHub.class;
    if (columnIndex==3) return Boolean.class;
    return String.class;
  }
  
  public boolean isCellEditable(int row, int col) { 
    if (col==2 || col==3 ) return true;
    else return false;
    }
  

  @Override
  public int getColumnCount() {
    // TODO Auto-generated method stub
    return 4;
  }

  @Override
  public int getRowCount() {
    // TODO Auto-generated method stub
    return instruments.size();
  }

  @Override
  public Object getValueAt(int row, int col) {
    IMMap instrument=(IMMap) instruments.get(row);
    
    switch (col) {
      case 0:
        return new Integer(instrument.getInstrument());
      case 1:
        return new Integer(instrument.getChannel());
      case 2:
        return instrument.getBrick();
      case 3:
        return new Boolean(instrument.isSupressed());
      default:
        return "Error";
    }
  }
  
  @Override
  public void setValueAt(Object value, int row, int col) {
    if (col==2) {
    IMMap instrument=instruments.get(row);
    instrument.setBrick((BrickHub)value);
    }
  }
  
  
  
  public void setFile(File file) {
    instruments=MidiUtil.getInstruments(file);
    this.fireTableDataChanged();
  }


  public List<IMMap> getMap() {
    // TODO Auto-generated method stub
    return instruments;
  }

}
