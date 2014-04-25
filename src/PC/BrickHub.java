package PC;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Brick.Musician;

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
        Registry registry = LocateRegistry.getRegistry(getIPAddress());
        hub = (Musician) registry.lookup("Musician");
      }
      catch (Exception e) {
        System.err.println("Client exception: " + e.toString());
        e.printStackTrace();
      }
    }
  }

  public Musician getHub() {
    return hub;
  }
  
  public void disconnect() {
  }

}
