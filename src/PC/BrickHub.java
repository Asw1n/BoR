package PC;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.sound.midi.ShortMessage;

import BoRBrick.Musician;

/**
 * Represents a remote EV3 brick. 
 * This class establish the connection to the remote brick and sends (translated) midi events to it. <br>
 * The class knows two modes, buffered and unbuffered.
 * In unbuffered mode every midi event is sent to the brick as soon as it is received. 
 * While the event is processed (remotely) the current thread is locked.
 * In buffered mode midi events are sent using a seperate thread. 
 * If new events are recieved while the current event is still being processed,
 * then these events are discarded. <br>
 * Buffered mode ensures smooth operation even on fast songs with losts of notes. 
 *
 * 
 * @author Aswin
 * 
 */
public class BrickHub extends lejos.hardware.BrickInfo {

  private Musician hub;
  private Buffer   buffer;
  static public boolean bufferedMode=true;
  
  
  public static boolean isBufferedMode() {
    return bufferedMode;
  }

  /** Midi events can be buffered for sending to the remote brick. Buffered Mode has the advantage of not 
   * holding up the stream of Midi events.
   * @param bufferedMode
   */
  public static void setBufferedMode(boolean bufferedMode) {
    BrickHub.bufferedMode = bufferedMode;
  }


  public BrickHub(String name, String ipAddress, String type) {
    super(name, ipAddress, type);
  }

  public BrickHub(lejos.hardware.BrickInfo info) {
    this(info.getName(), info.getIPAddress(), info.getType());
  }

  public String toString() {
    return getName() + " (" + getIPAddress() + ")";
  }

  /**
   * Connects to the remote Brick
   */
  public void connect() {
    if (hub == null) {
      try {
        System.out.println("Connect to " + this.toString());
        Registry registry = LocateRegistry.getRegistry(getIPAddress(), 1098);
        Remote obj = registry.lookup("Musician");
        hub = (Musician) obj;
        if (bufferedMode) {
          buffer = new Buffer();
          buffer.setDaemon(true);
          buffer.start();
        }
      }
      catch (Exception e) {
        System.err.println("Client exception: " + e.toString());
        e.printStackTrace();
        hub = null;
      }
    }
  }



  /**
   * disconnect from the remote brick
   */
  public void disconnect() {
    hub = null;
    if (buffer != null) {
      buffer.halt();
      buffer = null;
    }

  }

  private void noteOff(int tone) {
    if (hub != null) {
      try {
        System.out.println(this.toString() + " Tone off: " + tone);
        hub.noteOff(tone);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  private void noteOn(int tone) {
    if (hub != null) {
      try {
        System.out.println(this.toString() + " Tone on: " + tone);
        hub.noteOn(tone);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  /** Sends a midi event to the brick.
   * @param event
   */
  public void sendEvent(ShortMessage event) {
    if (buffer != null) {
      buffer.addEvent(event);
    }
    else {
      processEvent(event);
    }
  }
  
  
  private void processEvent(ShortMessage event) {
    switch (event.getCommand()) {
      case ShortMessage.NOTE_ON: {
        if (event.getData2()==0)
          noteOff(event.getData1());
        else
          noteOn(event.getData1());
        break;
      }
      case ShortMessage.NOTE_OFF: {
          noteOff(event.getData1());
      }
        break;
      default:
    }
  }

  /**
   * A buffer for sending Midi events to the remote brick
   * @author Aswin
   *
   */
  private class Buffer extends Thread {
    private ShortMessage event      = null;
    private boolean      pleaseStop = false;

    public void halt() {
      pleaseStop = true;
    }

    public void addEvent(ShortMessage event) {
      if (this.event != null) {
        System.out.println("Midi event discarded");
      }
      this.event = event;
    }

    public void run() {
      while (!pleaseStop) {
        if (event == null) {
          //Thread.yield();
        }
        else {
          ShortMessage event2=event;
          event = null;
          processEvent(event2);
          //Thread.yield();
        }
      }
    }
  }

  public void sendDynamicRange(int lowestNote, int highestNote) {
    try {
      hub.setDynamicRange(lowestNote,  highestNote);
    }
    catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  public void sendStart() {
    try {
      hub.start();
    }
    catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

public void setTempo(int tempo) {
    try {
        hub.setTempo(tempo);
    }
    catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    
}

}
