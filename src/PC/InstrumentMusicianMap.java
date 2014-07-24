package PC;

import PC.BrickHub;
import PC.InstrumentMusicianMap;

import java.io.File;

/** The Instrument Musican Map maps an instrument in a midi file to a remote brick (arobot musician).
 * 
 * @author Aswin
 *
 */
public class InstrumentMusicianMap {
  int channel=0;
  int instrument=0;
  boolean supress=false;
  BrickHub brickHub=null;
  int highestNote=0;
  int lowestNote=127;
   

  /** Contructor
   * @param file
   * Name of the Midi file for which the mapping is intended
   * @param channel
   * Number of the Midi channel of the instrument is 
   * @param instrument
   * Number representing the instrument (See General Midi instrument specification
   */
  public InstrumentMusicianMap( int channel, int instrument) {
    this.channel=channel;
    this.instrument=instrument;
  }
  
  /** Returns the highest note for this instrument in the song
   * @return
   */
  public int getHighestNote() {
    return highestNote;
  }


  protected void updateDynamicRange(int note) {
    if (note<lowestNote) lowestNote=note;
    if (note>highestNote) highestNote=note;
  }

  protected void setHighestNote(int highestNote) {
    this.highestNote = highestNote;
  }



  /** Returns the lowest note for this instrument in the song
   * @return
   */
  public int getLowestNote() {
    return lowestNote;
  }



  protected void setLowestNote(int lowestNote) {
    this.lowestNote = lowestNote;
  }

  

    
  /** Returns a BrickHub object describing the brick the instrument is mapped to
   * @return
   */
  public BrickHub getBrick() {
    return brickHub;
  }

  /** Maps a Brick to an instrument
   * @param ev3
   */
  public void setBrick(BrickHub brickHub) {
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
      if (instrument>0)
    return instrument;
      else {
          System.err.println("Error: instrument 0");
          return 1;
      }
  }
  
 // TODO: probably misplaced here
  public boolean isEqual(InstrumentMusicianMap other) {
    if (other.getChannel()==channel && other.getInstrument()==instrument) return true;
    return false;
  }

  public boolean isSupressed() {
    // TODO Auto-generated method stub
    return supress;
  }

  public void dump() {
    System.out.println("Channel :"+channel);
    System.out.println("Instrument :"+instrument);
    System.out.println("Lowest note: "+lowestNote);
    System.out.println("Highest note: "+highestNote);
    if (brickHub==null)
      System.out.println("Not mapped");
    else
      System.out.println("brickHub.toString()");
  }

}
