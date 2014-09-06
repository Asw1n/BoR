package BoRServer;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;


/** Model for a combobox that allows to select a Synthesizer
 * @author Aswin
 *
 */
public class ComboSynthesizers implements ComboBoxModel<MidiDevice.Info> {
  static List<MidiDevice.Info> synthesizers=getSynthesizers();
  MidiDevice.Info selected;
  
  public ComboSynthesizers() {
    try {
      selected=MidiSystem.getSynthesizer().getDeviceInfo();
    }
    catch (MidiUnavailableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static List<MidiDevice.Info> getSynthesizers() {
    List<MidiDevice.Info> devices=new ArrayList<MidiDevice.Info>();
    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    for (int i = 0; i < infos.length; i++) {
        try {
            device = MidiSystem.getMidiDevice(infos[i]);
            if (device instanceof Synthesizer) {
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
  public Info getElementAt(int arg0) {
    // TODO Auto-generated method stub
    return synthesizers.get(arg0);
  }

  @Override
  public int getSize() {
    // TODO Auto-generated method stub
    return synthesizers.size();
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
