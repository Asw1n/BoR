package PC;

public class BrickInfo extends lejos.hardware.BrickInfo {

  public BrickInfo(String name, String ipAddress, String type) {
    super(name, ipAddress, type);
  }
  
  public BrickInfo(lejos.hardware.BrickInfo info) {
    this(info.getName(),info.getIPAddress(),info.getType());
  }

  public String toString() {
    return getName() + " (" + getIPAddress() + ")";
  }

  
}
