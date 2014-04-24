package PC;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.sound.midi.*;


public class MidiUtil {
  
  private MidiUtil() {};
  
static void dumpSequence(Sequence seq, int levels) {
  System.out.println("divType = " + seq.getDivisionType());
  System.out.println("microsLength = " + seq.getMicrosecondLength());
  System.out.println("resolution = " + seq.getResolution());
  System.out.println("tickLength = " + seq.getTickLength());
  if (levels>0) {
    int i=0;
    for (Track track : seq.getTracks()) {
      i++;
      System.out.println("\n======================= Track " + i + " =======================");
      dumpTrack(track, levels-1);
    }
  }
}

static void dumpTrack(Track track, int levels) {
  System.out.println("size = "+ track.ticks());
  System.out.println("events = "+ track.size());
  if (levels>0) {
    for (int i=0;i<track.size();i++) {
      dumpEvent(track.get(i), levels-1);
    }
  }
}

static void dumpEvent(MidiEvent event, int levels) {
  int statusInt = (int)event.getMessage().getStatus();
  String statusCode = new String(Integer.toString(statusInt, 16).toUpperCase());
  int code=(int)Integer.valueOf((""+statusCode.charAt(0)), 16);
  if (code==8 || code==9) return; 
  
  
  System.out.println("TimeStamp = " + event.getTick());
  System.out.print("Meassage type = " );
  if (event.getMessage() instanceof ShortMessage)
    System.out.println("Short " );
  if (event.getMessage() instanceof MetaMessage)
    System.out.println("Meta " );
  if (event.getMessage() instanceof SysexMessage)
    System.out.println("Sysex " );
  if (levels>0) 
    dumpMessage(event.getMessage());
}

static void dumpMessage(MidiMessage message) {
  int statusInt = (int)message.getStatus();
  String statusCode = new String(Integer.toString(statusInt, 16).toUpperCase());
  int channel = (int)Integer.valueOf((""+statusCode.charAt(1)), 16)+1;

  int dataBytes = message.getLength();
  int[] data = new int[dataBytes];
  for (int datam = 0; datam < dataBytes; datam ++ )
      data[datam] = ((int)message.getMessage()[datam] & 0xFF);
  int code=(int)Integer.valueOf((""+statusCode.charAt(0)), 16);
  switch (code) {
      case 8:
          System.out.print("Note Off ");
          System.out.print("cha="+channel+" ");
          System.out.print("nn="+((int)message.getMessage()[1] & 0xFF) + " ");
          System.out.print("vel="+((int)message.getMessage()[2] & 0xFF) + " ");
          break;
      case 9:
          System.out.print("Note On ");
          System.out.print("cha="+channel+" ");
          System.out.print("nn=" + ((int)message.getMessage()[1] & 0xFF) + " ");
          System.out.print("vel=" + ((int)message.getMessage()[2] & 0xFF) + " ");
          break;
      case 10:
          System.out.print("polyphonic key pressure ");
          System.out.print("cha="+channel+" ");
          System.out.print("key="+((int)message.getMessage()[1] & 0xFF) + " ");
          System.out.print("val="+((int)message.getMessage()[2] & 0xFF) + " ");
          break;
      case 11:
          if (((int)message.getMessage()[1] & 0xFF) < 120) {
              System.out.print("control change ");
              System.out.print("cha=" +channel+" ");
              System.out.print("ctr=" + ((int)message.getMessage()[1] & 0xFF) + " ");
              System.out.print("val=" + ((int)message.getMessage()[2] & 0xFF) + " ");
          } else {
              System.out.print("channel mode message ");
              if (((int)message.getMessage()[1] & 0xFF) == 120)
                  System.out.print("All Sound Off");
              else if (((int)message.getMessage()[1] & 0xFF) == 121)
                  System.out.print("Reset All Controllers");
              else if ((((int)message.getMessage()[1] & 0xFF) == 122) && ((int)message.getMessage()[2] & 0xFF) == 0)
                  System.out.print("Local Control: Off");
              else if ((((int)message.getMessage()[1] & 0xFF) == 122) && ((int)message.getMessage()[2] & 0xFF) == 127)
                  System.out.print("Local Control: On");
              else if (((int)message.getMessage()[1] & 0xFF) >= 123) {
                  System.out.print("All Notes Off");
                  if (((int)message.getMessage()[1] & 0xFF) == 124)
                      System.out.print("& Omni Mode Off");
                  else if (((int)message.getMessage()[1] & 0xFF) == 125)
                      System.out.print("& Omni Mode On");
                  else if ((((int)message.getMessage()[1] & 0xFF) == 126) && ((int)message.getMessage()[2] & 0xFF) == 0)
                      System.out.print("& Mono Mode On");
                  else if ((((int)message.getMessage()[1] & 0xFF) == 126) && ((int)message.getMessage()[2] & 0xFF) >= 1)
                      System.out.print("& Mono Mode On + " + ((int)message.getMessage()[2] & 0xFF) + " Channels");
                  else if (((int)message.getMessage()[1] & 0xFF) == 127)
                      System.out.print("& Poly Mode On");
              } else System.out.print("Uknown");
          }
          break;
      case 12:
          System.out.print("program change ");
          System.out.print("cha="+channel+" ");
          System.out.print("program="+((int)message.getMessage()[1] & 0xFF));
          break;
      case 13:
          System.out.print("channel pressure ");
          System.out.print("ch=" + (int)Integer.valueOf((""+statusCode.charAt(1)), 16)+" ");
          System.out.print("val=" + ((int)message.getMessage()[1] & 0xFF));
          break;
      case 14:
          System.out.print("pitch wheel change ");
          System.out.print("ch="+(int)Integer.valueOf((""+statusCode.charAt(1)), 16));
          System.out.print(" "+((int)message.getMessage()[1] & 0xFF));
          System.out.print(" "+((int)message.getMessage()[2] & 0xFF));
          break;
      case 15:
          switch ((int)Integer.valueOf((""+statusCode.charAt(1)), 16)) {
              case 0:
                  System.out.print("system exclusive (details hidden)");
                  break;
              case 1:
                  System.out.print("Midi Time QTR");
                  break;
              case 2:
                  System.out.print("song position pointer ");
                  System.out.print("lsb " + ((int)message.getMessage()[1] & 0xFF) + " ");
                  System.out.print("msb " + ((int)message.getMessage()[2] & 0xFF));
                  break;
              case 3:
                  System.out.print("song select ");
                  System.out.print("no " + ((int)message.getMessage()[1] & 0xFF));
                  break;
              case 6:
                  System.out.print("tune request");
                  break;
              case 7:
                  System.out.print("end of exclusive");
                  break;
              case 8:
                  System.out.print("timing clock");
                  break;
              case 9:
                  System.out.print("undefined");
                  break;
              case 10:
                  System.out.print("start");
                  break;
              case 11:
                  System.out.print("continue");
                  break;
              case 12:
                  System.out.print("stop");
                  break;
              case 13:
                  System.out.print("undefined");
                  break;
              case 14:
                  System.out.print("avtice sensing");
                  break;
              case 15:
                  switch ((Integer.toString((int)message.getMessage()[1],16).toUpperCase()).charAt(0)) {
                      case '2':
                          switch ((Integer.toString((int)message.getMessage()[1],16).toUpperCase()).charAt(1)) {
                              case 'F':
                                  System.out.print("TrkEnd");
                                  break;
                              default:
                                  System.out.print("Uknown FF 2");
                                  break;
                          }
                          break;
                      case '5':
                          switch ((Integer.toString((int)message.getMessage()[1],16).toUpperCase()).charAt(1)) {
                              case '1':
                                  String a = (Integer.toHexString((int)message.getMessage()[3] & 0xFF));
                                  String b = (Integer.toHexString((int)message.getMessage()[4] & 0xFF));
                                  String c = (Integer.toHexString((int)message.getMessage()[5] & 0xFF));
                                  if (a.length() == 1) a = ("0"+a);
                                  if (b.length() == 1) b = ("0"+b);
                                  if (c.length() == 1) c = ("0"+c);
                                  String whole = a+b+c;
                                  int newTempo = Integer.parseInt(whole,16);
                                  System.out.print("Tempo Change: " + newTempo);
                                  break;
                              case '4':
                                  System.out.print("SMPTE ");
                                  break;
                              case '8':
                                  System.out.print("TimeSig ");
                                  System.out.print(Integer.toString(message.getMessage()[3]));
                                  System.out.print("/"+((int)message.getMessage()[4])*2);
                                  System.out.print(" " + (Integer.toString(message.getMessage()[5]))+ " ");
                                  System.out.print(" " + (Integer.toString(message.getMessage()[6]))+ " ");
                                  break;
                          }  
                          break;
                      default:
                          break;
                  }
                  break;
              default:
                  System.out.print("Uknown System Message");
                  break;
          }
          break;
      default:
          System.out.print("Unknown Message");
          break;
  }
  System.out.println();
  
}

public static List getInstrumentsOld(Sequence seq) {
  List<Integer> instruments = new ArrayList<Integer>();
  Track[] tracks=seq.getTracks();
  for (Track track : tracks) {
    for (int i=0; i<track.size();i++) {
      MidiEvent event=track.get(i);
      if (event.getMessage() instanceof ShortMessage) {
        ShortMessage message=(ShortMessage)event.getMessage();
        if (message.getCommand()==ShortMessage.PROGRAM_CHANGE) {
          instruments.add(message.getData1());
          //System.out.println("Instrument = " + message.getData1() + " Channel = " + message.getChannel());
        }
      }
    }
  }
  Collections.sort(instruments);
  return instruments;
}


public static List<InstrumentMap> getInstruments(File file) {
  Sequence seq;
  List<InstrumentMap> instruments = new ArrayList<InstrumentMap>();
  try {
    seq = MidiSystem.getSequence(file);
    Track[] tracks=seq.getTracks();
    for (Track track : tracks) {
      for (int i=0; i<track.size();i++) {
        MidiEvent event=track.get(i);
        if (event.getMessage() instanceof ShortMessage) {
          ShortMessage message=(ShortMessage)event.getMessage();
          if (message.getCommand()==ShortMessage.PROGRAM_CHANGE) {
            InstrumentMap newMap=new InstrumentMap(message.getChannel(),message.getData1());
            boolean unique=true;
            for (InstrumentMap existing : instruments) {
              if (newMap.isEqual(existing)) {
                unique=false;
                break;
              }
            }
            if (unique) {
              instruments.add(new InstrumentMap(message.getChannel(),message.getData1()));
            System.out.println("Instrument = " + message.getData1() + " Channel = " + message.getChannel());
            }
          }
        }
      }
    }
  }
  catch (InvalidMidiDataException e) {
    e.printStackTrace();
  }
  catch (IOException e) {
    e.printStackTrace();
  }

  return instruments;
}


public static List<MidiDevice.Info> getSequencers() {
  List<MidiDevice.Info> devices=new ArrayList<MidiDevice.Info>();
  MidiDevice device;
  MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
  for (int i = 0; i < infos.length; i++) {
      try {
          device = MidiSystem.getMidiDevice(infos[i]);
          if (device instanceof Sequencer) {
            devices.add(infos[i]);
          }
          } catch (MidiUnavailableException e) {
            // Handle or throw exception...
      }
  }
  return devices;
}

public static List<MidiDevice.Info> getSynthesizers() {
  List<MidiDevice.Info> devices=new ArrayList<MidiDevice.Info>();
  MidiDevice device;
  MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
  for (int i = 0; i < infos.length; i++) {
      try {
          device = MidiSystem.getMidiDevice(infos[i]);
          if (device instanceof Synthesizer) {
            devices.add(infos[i]);
          }
          } catch (MidiUnavailableException e) {
            // Handle or throw exception...
      }
  }
  return devices;
}



}
