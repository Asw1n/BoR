package PC;

import PC.BrickHub;
import PC.IMMap;

import java.io.File;

/** The Instrument Musican Map maps an instrument in a midi file to a remote brick (arobot musician).
 * 
 * @author Aswin
 *
 */
public class IMMap {
  File file=null;
  int channel=0;
  int instrument=0;
  boolean supress=false;
  BrickHub brickHub=null;
   

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
  

  public void connect() {
    brickHub.connect();
  }
  
   
  /** Returns a BrickInfo object describing the brick the instrument is mapped to
   * @return
   */
  public BrickHub getBrickInfo() {
    return brickHub;
  }

  /** Maps a Brick to an instrument
   * @param ev3
   */
  public void setBrickInfo(BrickHub brickHub) {
    this.brickHub = brickHub;
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
