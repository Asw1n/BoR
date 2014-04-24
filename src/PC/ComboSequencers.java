package PC;

import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;


public class ComboSequencers implements ComboBoxModel<MidiDevice.Info> {
  static List<MidiDevice.Info> sequencers=MidiUtil.getSequencers();
  MidiDevice.Info selected;
  
  public ComboSequencers() {
    try {
      selected=MidiSystem.getSequencer().getDeviceInfo();
    }
    catch (MidiUnavailableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override
  public void addListDataListener(ListDataListener arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Info getElementAt(int arg0) {
    // TODO Auto-generated method stub
    return sequencers.get(arg0);
  }

  @Override
  public int getSize() {
    // TODO Auto-generated method stub
    return sequencers.size();
  }

  @Override
  public void removeListDataListener(ListDataListener arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Object getSelectedItem() {
    // TODO Auto-generated method stub
    return selected;
  }

  @Override
  public void setSelectedItem(Object arg0) {
    selected=(Info) arg0;
    
  }


}
