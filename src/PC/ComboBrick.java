package PC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import lejos.hardware.BrickFinder;

/** Model for a combobox that allows to select a remote EV3 brick
 * @author Aswin
 *
 */
public class ComboBrick implements ComboBoxModel<BrickHub>{
  ArrayList<ListDataListener> listeners =new ArrayList<ListDataListener>();
  static List<BrickHub> bricks;
  static {
    scan();
  }
  BrickHub selected;
  static BrickHub deselect = new BrickHub("Unassigned","","");

  public ComboBrick() {
  }

  @Override
  public void addListDataListener(ListDataListener arg0) {
    listeners.add(arg0);
  }

  @Override
  public BrickHub getElementAt(int arg0) {
    if (arg0==bricks.size()) return deselect;
    return bricks.get(arg0);
  }

  @Override
  public int getSize() {
    return bricks.size()+1;
  }

  @Override
  public void removeListDataListener(ListDataListener arg0) {
    listeners.remove(arg0);
  }

  @Override
  public Object getSelectedItem() {
    return selected;
  }

  @Override
  public void setSelectedItem(Object arg0) {
    ListDataEvent event=new  ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0);
    for (ListDataListener listener : listeners) {
      listener.contentsChanged(event);
    }
    selected=(BrickHub) arg0;
  }
  
  /**
   * Refreshes the list of bricks
   */
  public static void scan() {
      bricks=new ArrayList<BrickHub>();
      try {
      for(lejos.hardware.BrickInfo info : BrickFinder.discover()) {
        bricks.add(new BrickHub(info));
      }
      }
      catch (IOException e) {
      }
    }

}
