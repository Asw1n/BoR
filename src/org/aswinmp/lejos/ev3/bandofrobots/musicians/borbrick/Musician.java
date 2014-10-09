package org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Interface that defines the remote EV3 Hub
 * @author Aswin
 *
 */
public interface Musician extends Remote {
  static final long serialVersionUID = 43L;

  /** The start() method indicates that a song starts
   * @throws RemoteException
   */
  public void start() throws RemoteException;
  
  /** The end() method indicates that a song ends
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
   * The tone expressed in ?
   * @throws RemoteException
   */
  public void noteOn(int tone) throws RemoteException;
  
  /** Indicates the end of a tone
   * @param tone
   * The tone expressed in ?
   * @throws RemoteException
   */
  public void noteOff(int tone) throws RemoteException;

  /** Communicates the dynamic range of the instrument to the musician
   * @param lowestNote
   * The highest note found in the song
   * @param highestNote
   * The lowest note found in the song
   */
  public void setDynamicRange(int lowestNote, int highestNote) throws RemoteException;

}
