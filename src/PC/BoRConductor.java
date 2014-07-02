package PC;

import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
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
public class BoRConductor implements Receiver, MetaEventListener {
  List<InstrumentMusicianMap>    map;
  BrickHub[] musicians;

  public BoRConductor() {
  }

  public void setMap(List<InstrumentMusicianMap> map) {
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
  
  private void setTempo(int b) {
      for (InstrumentMusicianMap immap : map) {
          if (immap.brickHub != null)
              immap.brickHub.setTempo(b);
      }
  }

 
  


  private void setChannel(int channel, int instrument) {
    
    for (InstrumentMusicianMap immap : map) {
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

  public void start() {
    for (InstrumentMusicianMap immap : map) {
      if (immap.getBrick() instanceof BrickHub) {
        immap.getBrick().sendDynamicRange(immap.lowestNote, immap.highestNote);
        immap.getBrick().sendStart();
      }
    }
    
  }

@Override
public void meta(MetaMessage metaMessage) {
        switch (metaMessage.getMessage()[1]) {
            case 81: {
                int b=0;
                for (int p=2;p>=0;p--) {
                    b += ((int)(metaMessage.getMessage()[5-p] & 0xFF)) * Math.pow(255,p);
                }
                b=Math.round(b/1000f);
                System.out.println("Set tempo: "+b);
                setTempo(b);
                break;
            }
            default:
                break;
           }
                
}

}
