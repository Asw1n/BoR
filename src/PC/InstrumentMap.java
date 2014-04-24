package PC;

import lejos.hardware.Brick;

public class InstrumentMap {
  int channel=0;
  int instrument=0;
  Brick ev3=null;

  public InstrumentMap(int channel, int instrument) {
    this.channel=channel;
    this.instrument=instrument;
  }

  public Brick getEv3() {
    return ev3;
  }

  public void setEv3(Brick ev3) {
    this.ev3 = ev3;
  }

  public int getChannel() {
    return channel;
  }

  public int getInstrument() {
    return instrument;
  }
  
  public boolean isEqual(InstrumentMap other) {
    if (other.getChannel()==channel && other.getInstrument()==instrument) return true;
    return false;
  }

}
