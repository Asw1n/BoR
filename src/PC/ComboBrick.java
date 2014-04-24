package PC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import lejos.hardware.BrickFinder;

public class ComboBrick implements ComboBoxModel<BrickInfo>{
  static List<BrickInfo> bricks;
  BrickInfo selected;
  static BrickInfo deselect = new BrickInfo("Unassigned","","");

  public ComboBrick() {
    if (bricks==null) scan();
  }

  @Override
  public void addListDataListener(ListDataListener arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public BrickInfo getElementAt(int arg0) {
    if (arg0==bricks.size()) return deselect;
    return bricks.get(arg0);
  }

  @Override
  public int getSize() {
    return bricks.size()+1;
  }

  @Override
  public void removeListDataListener(ListDataListener arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Object getSelectedItem() {
    return selected;
  }

  @Override
  public void setSelectedItem(Object arg0) {
    selected=(BrickInfo) arg0;
  }
  
  /**
   * Refreshes the list of bricks
   */
  public void scan() {
    try {
      bricks=new ArrayList<BrickInfo>();
      for(lejos.hardware.BrickInfo info : BrickFinder.discover()) {
        bricks.add(new BrickInfo(info));
      }
      selected=deselect;
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
