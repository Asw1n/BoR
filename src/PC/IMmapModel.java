package PC;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

public class IMmapModel extends AbstractTableModel{
  /**
   * 
   */
  private static final long serialVersionUID = -5686596864300068524L;
  List<IMMap> instruments = new ArrayList<IMMap>();

  public IMmapModel() {
    setFile(new File("C:/Users/Aswin/Downloads/rndmdngt.mid"));
  }
  
  public String getColumnName(int col) {
    switch (col) {
      case 0: return "Channel";
      case 1: return "Instrument";
      case 2: return "EV3";
      default: return "Error";
    }
}
  
  public boolean isCellEditable(int row, int col) { 
    if (col==2 ) return true;
    else return false; }
  

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
    IMMap instrument=(IMMap) instruments.get(row);
    switch (col) {
      case 0:
        return new Integer(instrument.getChannel());
      case 1:
        return new Integer(instrument.getInstrument());
      case 2:
        return new JComboBox<BrickInfo>(new ComboBrick());
      default:
        return "Error";
    }
  }
  
  public void setFile(File file) {
    instruments=MidiUtil.getInstruments(file);
  }

}
