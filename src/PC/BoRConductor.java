package PC;

import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;



/** The BoRConductor receives MIDI events from the sequencer when this is playing a song.
 * It determines to what channel and instrument this message belongs. If there is a misician that "plays"
 * the corresponding instrument, then the conductor will translate the midi message into appropriate method call on  
 * the musician.<br>
 * Currently the conductor only dumps the midi message to ystem.out. 
 * @author Aswin
 *
 */
public class BoRConductor implements Receiver {
  List<IMMap>    map;
  BrickHub[] musicians;

  public BoRConductor() {
  }

  public void setMap(List<IMMap> map) {
    this.map = map;
  }

  @Override
  public void close() {
    musicians = null;

  }

  @Override
  public void send(MidiMessage message, long timestamp) {
    if (message instanceof ShortMessage) {
      //MidiUtil.dumpMessage(message);
    ShortMessage shortMessage = (ShortMessage) message;
    int channel=shortMessage.getChannel();
    BrickHub hub=musicians[channel];
        switch (shortMessage.getCommand()) {
          case ShortMessage.NOTE_ON: {
            if (hub != null) hub.sendEvent(shortMessage);
            break;
          }
          case ShortMessage.NOTE_OFF: {
            if (hub != null) hub.sendEvent(shortMessage);
            }
            break;
          case ShortMessage.PROGRAM_CHANGE: {
            setChannel(shortMessage.getChannel(), shortMessage.getData1());
            break;
          }
          default:
            break;
        }
    }
    }

 
  


  private void setChannel(int channel, int instrument) {
    musicians[channel] = null;
    for (IMMap immap : map) {
      if (immap.getChannel() == channel && immap.getInstrument() == instrument) {
        if (immap.getBrick() instanceof BrickHub) {
          musicians[channel] = immap.getBrick();
        }
        else {
          musicians[channel]=null;
        }
        break;
      }
    }

  }

  public void open() {
    musicians = new BrickHub[16];
  }

}
