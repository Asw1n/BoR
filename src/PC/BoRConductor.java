package PC;

import java.rmi.RemoteException;

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
  IMMap[]    map;
  Musician[] musicians;

  public BoRConductor() {
  }

  public void setMap(IMMap[] map) {
    this.map = map;
  }

  @Override
  public void close() {
    // TODO: find out how to terminate hubs
    musicians = null;

  }

  @Override
  public void send(MidiMessage message, long timestamp) {
    if (message instanceof ShortMessage) {
      MidiUtil.dumpMessage(message);
//    ShortMessage shortMessage = (ShortMessage) message;
//      try {
//        switch (shortMessage.getCommand()) {
//          case ShortMessage.NOTE_ON: {
//            if (musicians[shortMessage.getChannel()] != null) {
//              musicians[shortMessage.getChannel()].ToneOn(shortMessage.getData1());
//            }
//            break;
//          }
//          case ShortMessage.NOTE_OFF: {
//            if (musicians[shortMessage.getChannel()] != null) {
//              musicians[shortMessage.getChannel()].ToneOff(shortMessage.getData1());
//            }
//            break;
//          }
//          case ShortMessage.PROGRAM_CHANGE: {
//            setChannel(shortMessage.getChannel(), shortMessage.getData1());
//            break;
//          }
//          case ShortMessage.START: {
//            for (Musician musician : musicians) {
//              if (musician != null) {
//                musician.start();
//              }
//            }
//            break;
//          }
//          case ShortMessage.STOP: {
//            for (Musician musician : musicians) {
//              if (musician != null) {
//                musician.stop();
//              }
//            }
//            break;
//          }
//          default:
//            break;
//        }
//      }
//      catch (RemoteException e) {
//        System.err.println("Error sending Midi message to musician");
//        e.printStackTrace();
//      }
    }

  }

  private void setChannel(int channel, int instrument) {
    musicians[channel] = null;
    for (IMMap immap : map) {
      if (immap.getChannel() == channel && immap.getInstrument() == instrument) {
        musicians[channel] = immap.getBrickInfo().getHub();
        break;
      }
    }

  }

  public void open() {
    musicians = new Musician[16];
  }

}
