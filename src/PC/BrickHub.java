package PC;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import BoRBrick.Musician;

/** Containes information about a remote EV3 brick and has methods to establish a connection to the brick.
 * @author Aswin
 *
 */
public class BrickHub extends lejos.hardware.BrickInfo {

  private Musician hub;

  public BrickHub(String name, String ipAddress, String type) {
    super(name, ipAddress, type);
  }

  public BrickHub(lejos.hardware.BrickInfo info) {
    this(info.getName(), info.getIPAddress(), info.getType());
  }

  public String toString() {
    return getName() + " (" + getIPAddress() + ")";
  }

  public void connect() {
    if (hub == null) {
      try {
        Registry registry = LocateRegistry.getRegistry(getIPAddress(),1098);
        for (String l : registry.list()) {
          System.out.println(l);
        }
        Remote obj= registry.lookup("Musician");
        hub = (Musician) obj;
      }
      catch (Exception e) {
        System.err.println("Client exception: " + e.toString());
        e.printStackTrace();
        hub=null;
      }
    }
  }

  public Musician getHub() {
    return hub;
  }
  
  public void disconnect() {
    hub=null;
  }
  
  public void noteOff(int tone) {
    if (hub != null) {
      try {
        hub.noteOff(tone);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }


  public void noteOn( int tone) {
    if (hub != null) {
      try {
        hub.noteOn(tone);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

}
