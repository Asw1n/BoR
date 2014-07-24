package BoRServer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import lejos.hardware.BrickFinder;

/** Model to show Song in a table editor
 * @author Aswin
 *
 */
public class BrickTableModel extends AbstractTableModel{
    private static final long serialVersionUID = -7664718307333629791L;
    static List<Brick> bricks=new ArrayList<Brick>();
    List<Brick> selectedBricks=new ArrayList<Brick>();
    static {scan();}
    
    
    
    
    /**
     * Refreshes the list of bricks
     */
    public static void scan() {
        bricks.clear();
        // TODO: remove after testing
        for (int i=0;i<10;i++) bricks.add(new Brick("Test"+i,"127.0.0.1","EV3"));
        
        
        try {
        for(lejos.hardware.BrickInfo info : BrickFinder.discover()) {
            Brick brick = new Brick(info);
            if (brick.isMusician()) {
                bricks.add(new Brick(info));
            }
        }
        }
        catch (IOException e) {
        }
        
      }
  
  
  
  
  @Override
  public String getColumnName(int col) {
    switch (col) {
      case 0: return "Select";
      case 1: return "Name";
      case 2: return "IP address";
      case 3: return "Type";
      default: return "Error";
    }
}
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
public Class getColumnClass(int col) {
      if (col==0) return Boolean.class;
      return String.class;
  }
  
  public boolean isCellEditable(int row, int col) { 
      switch (col) {
          case 0: return true;
          default: return false;
        }
    }
  

  @Override
  public int getColumnCount() {
    return 4;
  }

  @Override
  public int getRowCount() {
      return bricks.size();
  }
  
  public void setValueAt(Object value, int row, int col) {
      Brick brick=bricks.get(row);
      if (value==Boolean.FALSE) {
         selectedBricks.remove(brick);
      }
      else {
          selectedBricks.add(brick);
      }
  }
  

  @Override
  public Object getValueAt(int row, int col) {
      Brick brick=bricks.get(row);
    switch (col) {
        case 0 :
            for (Brick selectedBrick : selectedBricks) {
                if (brick.equals(selectedBrick)) return true; 
            }
            return false;
      case 1:
          return brick.getName();
      case 2:
          return brick.getIPAddress();
      case 3:
        return brick.getType();
      default:
        return "Error";
    }
  }


public void resfresh() {
    scan();
    this.fireTableDataChanged();
}




public void setSelectedBricks(List<Brick> selectedBricks) {
    this.selectedBricks=selectedBricks;
}




public List<Brick> getSelectedBricks() {
    return selectedBricks;
}
  

 
}
