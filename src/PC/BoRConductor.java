package PC;

import java.rmi.RemoteException;
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
  private boolean verbose=true;
  List<IMMap>    map;
  Musician[] musicians;

  public BoRConductor() {
  }

  public void setMap(List<IMMap> map) {
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
      //MidiUtil.dumpMessage(message);
    ShortMessage shortMessage = (ShortMessage) message;
        switch (shortMessage.getCommand()) {
          case ShortMessage.NOTE_ON: {
            noteOn(shortMessage.getChannel(),shortMessage.getData1());
            break;
          }
          case ShortMessage.NOTE_OFF: {
            noteOff(shortMessage.getChannel(),shortMessage.getData1());
            }
            break;
          case ShortMessage.PROGRAM_CHANGE: {
            setChannel(shortMessage.getChannel(), shortMessage.getData1());
            break;
          }
          case ShortMessage.START: {
            start();
            break;
          }
          case ShortMessage.STOP: {
            //stop();
            break;
          }
          default:
            break;
        }
    }
    }

  private void noteOff(int channel, int tone) {
    if (verbose) System.out.println("                                   ".substring(0, channel)+".");
    if (musicians[channel] != null) {
      try {
        musicians[channel].noteOff(tone);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }


  private void noteOn(int channel, int tone) {
    if (verbose) System.out.println("                                   ".substring(0, channel)+"*");
    if (musicians[channel] != null) {
      try {

        musicians[channel].noteOn(tone);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }
  
  private void start() {
    if (verbose) System.out.println("Start");
  for (Musician musician : musicians) {
    if (musician != null) {
      try {
        musician.start();
      }
      catch (RemoteException e) {
      }
    }
  }
  }
  
  private void stop() {
    if (verbose) System.out.println("Stop");
    for (Musician musician : musicians) {
      if (musician != null) {
        try {
          musician.stop();
        }
        catch (RemoteException e) {
        }
      }
    }

  }

  private void setChannel(int channel, int instrument) {
    musicians[channel] = null;
    for (IMMap immap : map) {
      if (immap.getChannel() == channel && immap.getInstrument() == instrument) {
        if (verbose) System.out.print("Setting instrument "+ instrument + " on channel " + channel +" to ");
        if (immap.getBrickInfo() instanceof BrickHub) {
          musicians[channel] = immap.getBrickInfo().getHub();
          if (verbose) System.out.println(immap.getBrickInfo().toString());
        }
        else {
          musicians[channel]=null;
          if (verbose) System.out.println("null");
        }
        break;
      }
    }

  }

  public void open() {
    musicians = new Musician[16];
  }

}
