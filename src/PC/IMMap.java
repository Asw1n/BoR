package PC;

import java.io.File;

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
  BrickInfo ev3=null;

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

  /** Returns a BrickInfo object describing the brick the instrument is mapped to
   * @return
   */
  public BrickInfo getEv3() {
    return ev3;
  }

  /** Maps a Brick to an instrument
   * @param ev3
   */
  public void setEv3(BrickInfo ev3) {
    this.ev3 = ev3;
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

}
