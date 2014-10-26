package org.aswinmp.lejos.ev3.bandofrobots.pc.borserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.ShortMessage;

import lejos.hardware.BrickInfo;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick.Musician;

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
public class Brick extends lejos.hardware.BrickInfo  {

  private Musician hub;
  private Buffer   buffer;
  static public boolean bufferedMode=true;
  static private List<Brick> bricks = new ArrayList<Brick>();
  
  
 
  
  public static boolean isBufferedMode() {
    return bufferedMode;
  }

  /** Midi events can be buffered for sending to the remote brick. Buffered Mode has the advantage of not 
   * holding up the stream of Midi events.
   * @param bufferedMode
   */
  public static void setBufferedMode(boolean bufferedMode) {
    Brick.bufferedMode = bufferedMode;
  }
  
  public static Brick get(lejos.hardware.BrickInfo info) {
    for ( Brick existing : bricks) {
      if (existing.isEqual(info)) {
        return existing;
      }
    }
    Brick newBrick=new Brick(info);
    bricks.add(newBrick);
    return newBrick;
    }
    
  private boolean isEqual(BrickInfo info) {
    if (this.getIPAddress().equals(info.getIPAddress())) return true;
    return false;
  }

  private Brick(String name, String ipAddress, String type) {
    super(name, ipAddress, type);
  }

  private Brick(lejos.hardware.BrickInfo info) {
    this(info.getName(), info.getIPAddress(), info.getType());
  }

  public String toString() {
    return getName() + " (" + getIPAddress() + ")";
  }
  
  public boolean isMusician() {
      try {
        LocateRegistry.getRegistry(getIPAddress(), 1098);
        return true;
    }
    catch (RemoteException e) {
       return false;
    }
      
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
         System.err.println("Connection refused. Check if musician is running on the brick.");
        hub = null;
      }
    }
  }



  /**
   * disconnect from the remote brick
   */
  public void disconnect() {
    if (buffer != null) {
      buffer.halt();
      buffer = null;
    }
    hub = null;
  }

  private void noteOff(int tone) {
    if (hub != null) {
      try {
//        System.out.println(this.toString() + " Tone off: " + tone);
        hub.noteOff(tone);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }
  
  private void voiceOff(int tone) {
    if (hub != null) {
      try {
//        System.out.println(this.toString() + " Tone off: " + tone);
        hub.voiceOff(tone);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  private void noteOn(int tone, int intensity) {
    if (hub != null) {
      try {
//        System.out.print(this.toString());
//        System.out.println(String.format("Tone on: %d, %d ", tone, intensity));
        hub.noteOn(tone, intensity);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  private void voiceOn(int tone, int intensity) {
    if (hub != null) {
      try {
//        System.out.print(this.toString());
//        System.out.println(String.format("Tone on: %d, %d ", tone, intensity));
        hub.voiceOn(tone, intensity);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }
  
  /** Sends a midi event to the brick.
   * @param event
   */
  public void sendInstrumentEvent(ShortMessage event) {
    if (buffer != null) {
      buffer.addInstrumentEvent(event);
    }
    else {
      processInstrumentEvent(event);
    }
  }
  
  /** Sends a midi event to the brick.
   * @param event
   */
  public void sendVoiceEvent(ShortMessage event) {
    if (buffer != null) {
      buffer.addVoiceEvent(event);
    }
    else {
      processVoiceEvent(event);
    }
  } 
  
  
  private void processInstrumentEvent(ShortMessage event) {
    switch (event.getCommand()) {
      case ShortMessage.NOTE_ON: {
        if (event.getData2()==0)
          noteOff(event.getData1());
        else
          noteOn(event.getData1(), event.getData2());
        break;
      }
      case ShortMessage.NOTE_OFF: {
          noteOff(event.getData1());
      }
        break;
      default:
    }
  }
  
  private void processVoiceEvent(ShortMessage event) {
    switch (event.getCommand()) {
      case ShortMessage.NOTE_ON: {
        if (event.getData2()==0)
         voiceOff(event.getData1());
        else
          voiceOn(event.getData1(), event.getData2());
        break;
      }
      case ShortMessage.NOTE_OFF: {
          voiceOff(event.getData1());
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
    private ShortMessage instrumentEvent      = null;
    private ShortMessage voiceEvent      = null;
    private boolean      pleaseStop = false;

    public void halt() {
      pleaseStop = true;
    }

    public void addInstrumentEvent(ShortMessage event) {
      this.instrumentEvent = event;
    }

    public void addVoiceEvent(ShortMessage event) {
      this.voiceEvent = event;
    }

    public void run() {
      while (!pleaseStop) {
        //System.out.print(".");
        if (instrumentEvent == null && voiceEvent == null) {
          Thread.yield();
        }
        else {
          if (voiceEvent != null) {
            ShortMessage event2=voiceEvent;
            voiceEvent = null;
            processVoiceEvent(event2);
            
          }
          else {
            ShortMessage event2=instrumentEvent;
            instrumentEvent = null;
            processInstrumentEvent(event2);
            Thread.yield();
          }
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

public void sendStop() {
    try {
        hub.stop();
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
}

public void sendBeat(ShortMessage message) {
  try {
    hub.Beat();
  } catch (RemoteException e) {
    e.printStackTrace();
  }
  
}

}
