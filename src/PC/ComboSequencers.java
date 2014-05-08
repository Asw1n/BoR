package PC;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;


/** Model for a combobox that allows to select a sequencer
 * @author Aswin
 *
 */
public class ComboSequencers implements ComboBoxModel<MidiDevice.Info> {
  static List<MidiDevice.Info> sequencers=getSequencers();
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
  
  private static List<MidiDevice.Info> getSequencers() {
    List<MidiDevice.Info> devices=new ArrayList<MidiDevice.Info>();
    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    for (int i = 0; i < infos.length; i++) {
        try {
            device = MidiSystem.getMidiDevice(infos[i]);
            if (device instanceof Sequencer) {
              devices.add(infos[i]);
            }
            } catch (MidiUnavailableException e) {
              // Handle or throw exception...
        }
    }
    return devices;
  }
  
  @Override
  public void addListDataListener(ListDataListener arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public MidiDevice.Info getElementAt(int arg0) {
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
