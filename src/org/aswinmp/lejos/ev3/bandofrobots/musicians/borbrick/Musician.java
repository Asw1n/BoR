package org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Interface that defines the BoR musicians
 * @author Aswin
 *
 */
public interface Musician extends Remote {
  static final long serialVersionUID = 43L;

  /** The start() method is called at the start of a song 
   * @throws RemoteException
   */
  public void start() throws RemoteException;
  
  /** The stop method indicates that a song ends
   * @throws RemoteException
   */
  public void stop() throws RemoteException;
  
  /** Specifies the tempo of the current song 
   * @param tempo
   * The length of a quarter note expressed in msec
   * @throws RemoteException
   */
  public void setTempo(int tempo) throws RemoteException;
  
  /** Indicates the start of a tone
   * @param tone
   * The tone expressed in see midi spec
   * @param intensity
   * The intensity of the tone
   * @throws RemoteException
   */
  public void noteOn(int tone, int intensity) throws RemoteException;
  
  /** Indicates the end of a tone
   * @param tone
   * The tone expressed in ?
   * @throws RemoteException
   */
  public void noteOff(int tone) throws RemoteException;
  
  /** Indicates the start of a voice
   * @param tone
   * The tone expressed in see midi spec
   * @param intensity
   * The intensity of the tone
   * @throws RemoteException
   */
  public void voiceOn(int tone, int intensity) throws RemoteException;
  
  /** Indicates the end of a voice
   * @param tone
   * The tone expressed in see midi spec 
   * @throws RemoteException
   */
  public void voiceOff(int tone) throws RemoteException;
  
  

  /** Communicates the dynamic range of the instrument to the musician
   * @param lowestNote
   * The highest note found in the song
   * @param highestNote
   * The lowest note found in the song
   */
  public void setDynamicRange(int lowestNote, int highestNote) throws RemoteException;
  
  /** Indicates the start of a quarter note. This method is only executed when a metronome channel is set on BoR server.
   * @throws RemoteException
   */
  public void Beat() throws RemoteException;

}
