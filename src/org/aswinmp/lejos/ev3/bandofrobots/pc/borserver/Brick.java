package org.aswinmp.lejos.ev3.bandofrobots.pc.borserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.ShortMessage;

import lejos.hardware.BrickInfo;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.Musician;
import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

/**
 * Represents a remote EV3 brick. This class establish the connection to the
 * remote brick and sends (translated) midi events to it. <br>
 * The class knows two modes, buffered and unbuffered. In unbuffered mode every
 * midi event is sent to the brick as soon as it is received. While the event is
 * processed (remotely) the current thread is locked. In buffered mode midi
 * events are sent using a seperate thread. If new events are recieved while the
 * current event is still being processed, then these events are discarded. <br>
 * Buffered mode ensures smooth operation even on fast songs with losts of
 * notes.
 * 
 * 
 * @author Aswin
 * 
 */
public class Brick extends lejos.hardware.BrickInfo {

  private Musician           hub;
  private Buffer             buffer;
  static public boolean      bufferedMode = true;
  static private List<Brick> bricks       = new ArrayList<Brick>();

  public static boolean isBufferedMode() {
    return bufferedMode;
  }

  /**
   * Midi events can be buffered for sending to the remote brick. Buffered Mode
   * has the advantage of not holding up the stream of Midi events.
   * 
   * @param bufferedMode
   */
  public static void setBufferedMode(final boolean bufferedMode) {
    Brick.bufferedMode = bufferedMode;
  }

  public static Brick get(final lejos.hardware.BrickInfo info) {
    for (final Brick existing : bricks) {
      if (existing.isEqual(info)) {
        return existing;
      }
    }
    final Brick newBrick = new Brick(info);
    bricks.add(newBrick);
    return newBrick;
  }

  private boolean isEqual(final BrickInfo info) {
    if (this.getIPAddress().equals(info.getIPAddress()))
      return true;
    return false;
  }

  private Brick(final String name, final String ipAddress, final String type) {
    super(name, ipAddress, type);
  }

  private Brick(final lejos.hardware.BrickInfo info) {
    this(info.getName(), info.getIPAddress(), info.getType());
  }

  @Override
  public String toString() {
    return getName() + " (" + getIPAddress() + ")";
  }

  public boolean isMusician() {
    try {
      LocateRegistry.getRegistry(getIPAddress(), 1098);
      return true;
    } catch (final RemoteException e) {
      return false;
    }

  }

  /**
   * Connects to the remote Brick
   */
  public void connect() {
    if (hub == null) {
      try {
        BrickLogger.info("Connect to %s", this.toString());
        final Registry registry = LocateRegistry.getRegistry(getIPAddress(), 1098);
        final Remote obj = registry.lookup("Musician");
        hub = (Musician) obj;
        if (bufferedMode) {
          buffer = new Buffer();
          buffer.setDaemon(true);
          buffer.start();
        }
      } catch (final Exception e) {
        BrickLogger.error("Connection to %s refused. Check if Musician is running on the brick.", this.toString());
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

  private void noteOff(final int tone) {
    if (hub != null) {
      try {
        // System.out.println(this.toString() + " Tone off: " + tone);
        hub.noteOff(tone);
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }
  }

  private void voiceOff(final int tone) {
    if (hub != null) {
      try {
        // System.out.println(this.toString() + " Tone off: " + tone);
        hub.voiceOff(tone);
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }
  }

  private void noteOn(final int tone, final int intensity) {
    if (hub != null) {
      try {
        // System.out.print(this.toString());
        // System.out.println(String.format("Tone on: %d, %d ", tone,
        // intensity));
        hub.noteOn(tone, intensity);
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }
  }

  private void voiceOn(final int tone, final int intensity) {
    if (hub != null) {
      try {
        // System.out.print(this.toString());
        // System.out.println(String.format("Tone on: %d, %d ", tone,
        // intensity));
        hub.voiceOn(tone, intensity);
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }
  }

  /**
   * Sends a midi event to the brick.
   * 
   * @param event
   */
  public void sendInstrumentEvent(final ShortMessage event) {
    if (buffer != null) {
      buffer.addInstrumentEvent(event);
    } else {
      processInstrumentEvent(event);
    }
  }

  /**
   * Sends a midi event to the brick.
   * 
   * @param event
   */
  public void sendVoiceEvent(final ShortMessage event) {
    if (buffer != null) {
      buffer.addVoiceEvent(event);
    } else {
      processVoiceEvent(event);
    }
  }

  private void processInstrumentEvent(final ShortMessage event) {
    switch (event.getCommand()) {
    case ShortMessage.NOTE_ON: {
      if (event.getData2() == 0)
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

  private void processVoiceEvent(final ShortMessage event) {
    switch (event.getCommand()) {
    case ShortMessage.NOTE_ON: {
      if (event.getData2() == 0)
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
   * 
   * @author Aswin
   * 
   */
  private class Buffer extends Thread {
    private ShortMessage instrumentEvent = null;
    private ShortMessage voiceEvent      = null;
    private boolean      pleaseStop      = false;

    public void halt() {
      pleaseStop = true;
    }

    public void addInstrumentEvent(final ShortMessage event) {
      this.instrumentEvent = event;
    }

    public void addVoiceEvent(final ShortMessage event) {
      this.voiceEvent = event;
    }

    @Override
    public void run() {
      while (!pleaseStop) {
        // System.out.print(".");
        if (instrumentEvent == null && voiceEvent == null) {
          Thread.yield();
        } else {
          if (voiceEvent != null) {
            final ShortMessage event2 = voiceEvent;
            voiceEvent = null;
            processVoiceEvent(event2);

          } else {
            final ShortMessage event2 = instrumentEvent;
            instrumentEvent = null;
            processInstrumentEvent(event2);
            Thread.yield();
          }
        }
      }
    }
  }

  public void sendDynamicRange(final int lowestNote, final int highestNote) {
    if (hub != null) {
      try {
        hub.setDynamicRange(lowestNote, highestNote);
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }

  }

  public void sendStart() {
    if (hub != null) {
      try {
        hub.start();
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }
  }

  public void setTempo(final int tempo) {
    if (hub != null) {
      try {
        hub.setTempo(tempo);
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }

  }

  public void sendStop() {
    if (hub != null) {
      try {
        hub.stop();
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }
  }

  public void sendBeat(final ShortMessage message) {
    if (hub != null) {
      try {
        hub.beat();
      } catch (final RemoteException e) {
        e.printStackTrace();
        hub = null;
      }
    }

  }

}
