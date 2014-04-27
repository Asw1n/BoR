package PC;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import lejos.hardware.BrickInfo;

/** The Instrument Musican Map maps an instrument in a midi file to a a robot musician.
 * 
 * @author Aswin
 *
 */
public class IMMap {
  File file=null;
  int channel=0;
  int instrument=0;
  boolean supress=false;
  BrickInfo brickInfo=null;
  Musician musician;
   

  /** Contructor
   * @param file
   * Name of the Midi file for which the mapping is intended
   * @param channel
   * Number of the Midi channel of the instrument is 
   * @param instrument
   * Number representing the instrument (See General Midi instrument specification
   */
  public IMMap(File file, int channel, int instrument) {
    this.channel=channel;
    this.instrument=instrument;
  }
  
  public Musician getMusician() {
    return musician;
  }

  public void connect() {
    musician=null;
    try {
      Registry registry = LocateRegistry.getRegistry(brickInfo.getIPAddress());
      musician = (Musician) registry.lookup("Musician");
  } catch (Exception e) {
      System.err.println("Client exception: " + e.toString());
      e.printStackTrace();
  }
  }
  
   
  /** Returns a BrickInfo object describing the brick the instrument is mapped to
   * @return
   */
  public BrickInfo getBrickInfo() {
    return brickInfo;
  }

  /** Maps a Brick to an instrument
   * @param ev3
   */
  public void setBrickInfo(BrickInfo brickInfo) {
    this.brickInfo = brickInfo;
  }

  /** Returns the channel for this mapping
   * @return
   */
  public int getChannel() {
    return channel;
  }

  /** Returns the instrument for this mapping
   * @return
   */
  public int getInstrument() {
    return instrument;
  }
  
 // TODO: probably misplaced here
  public boolean isEqual(IMMap other) {
    if (other.getChannel()==channel && other.getInstrument()==instrument) return true;
    return false;
  }

  public boolean isSupressed() {
    // TODO Auto-generated method stub
    return supress;
  }

}
