package org.aswinmp.lejos.ev3.bandofrobots.pc.borserver;

import java.util.ArrayList;
import java.util.List;


/** This class channels a midi channel to a midi instrument and keep track of the Note range of each channel/instrument
 * @author Aswin
 *
 */
public class Channels {
    public static final int CHANNELCOUNT=16;
    private List<ChannelInfo> channels ;
    protected List<Brick> instrumentBricks = new ArrayList<Brick>();
    protected List<Brick> voiceBricks = new ArrayList<Brick>();
    protected List<Brick> allBricks = new ArrayList<Brick>();
    
    
    public Channels() {
        channels = new ArrayList<ChannelInfo>(CHANNELCOUNT);
        for (int channelNo=0; channelNo<CHANNELCOUNT;channelNo++) {
           channels.add(channelNo, new ChannelInfo());
        }
    }
    
    protected void clear() {
        for (int channelNo=0; channelNo<CHANNELCOUNT;channelNo++) {
            channels.set(channelNo, new ChannelInfo());
         }
    }
    
    /** Assigns an instrument to a channel
     * @param channel
     * channel, 0 based (<16)
     * @param instrument
     * instrument, standard General MIDI Level 1 instruments (0 = undefined)
     */
    public void setIntrument(int channelNo, Integer instrument) {
        ChannelInfo channel = channels.get(channelNo);
        if (!channel.instruments.contains(instrument)) {
          channel.instruments.add(instrument);
        }
        channel.highestNote=0;
        channel.lowestNote=127;
    }
    
    public void setInstrumentBrick(Brick brick, Integer channelNo) {
      ChannelInfo channel = channels.get(channelNo);
      if (!channel.instrumentBricks.contains(brick)) {
        channel.instrumentBricks.add(brick);
        addInstrumentBrick(brick);
      }
    }
    
    private void addInstrumentBrick(Brick brick) {
      if (!instrumentBricks.contains(brick))
        instrumentBricks.add(brick);
      if (!allBricks.contains(brick))
        allBricks.add(brick);
    }
 
    private void addvoiceBrick(Brick brick) {
      if (!voiceBricks.contains(brick))
        voiceBricks.add(brick);
      if (!allBricks.contains(brick))
        allBricks.add(brick);
    }
 
    
    public void setVoiceBrick(Brick brick, int channelNo) {
      ChannelInfo channel = channels.get(channelNo);
      if (!channel.voiceBricks.contains(brick)) {
        channel.voiceBricks.add(brick);
        addvoiceBrick(brick);
      }
    }
    
    
  
    
    /** Updates the Note range for this channel/instrument. <br>
     * Updates the highestNote or the lowestNote if the note is out of the current range 
     * @param channel
     * @param note
     */
    public void updateRange(int channelNo, int note) {
        ChannelInfo channel = channels.get(channelNo);
            if (note<channel.lowestNote) channel.lowestNote=note;
            if (note>channel.highestNote) channel.highestNote=note;
          }
    
    /** Returns the uinstrument assigned to the channel
     * @param channel
     * @return
     */
    public List<Integer> getInstruments(int channel) {
       return channels.get(channel).instruments; 
    }
    
 
        

    /** returns the highest note for this channel/instrument
     * @param channel
     * @return
     */
    public int getHighestNote(int channel) {
        return channels.get(channel).highestNote; 
     }

    /** returns the lowest note for this channel/instrument
     * @param channel
     * @return
     */
    public int getLowestNote(int channel) {
        return channels.get(channel).lowestNote; 
     }
    
    /** Returns the list of bricks that listen to the channel
     * @param channelNo
     * @return
     */
    public List<Brick> getInstrumentBricks(int channelNo) {
        return channels.get(channelNo).instrumentBricks;
    }

    /** Returns the list of bricks that listen to the channel
     * @param channelNo
     * @return
     */
    public List<Brick> getVoiceBricks(int channelNo) {
        return channels.get(channelNo).voiceBricks;
    }
    
    
    /** Returns a list of bricks assigned to any of the channels for playing an instrument
     * @return
     */
    public List<Brick> getInstrumentBricks() {
      return instrumentBricks;
    }

    /** Returns a list of bricks assigned to any of the channels for singing
     * @return
     */
    public List<Brick> getVoiceBricks() {
      return voiceBricks;
    }

    /** Returns a list of bricks assigned to any of the channels for singing or playing
     * @return
     */
    public List<Brick> getAllBricks() {
      return allBricks;
    }
    
    
    
    public String getInstrumentBrickNames(int channelNo) {
        String names= "";
        List<Brick> bricks = getInstrumentBricks(channelNo);
        int size=bricks.size();
        for (int i=0;i<size;i++) {
            names = names.concat(bricks.get(i).toString());
            if (i<size-1) {
                names = names.concat(", ");
            }
        }
        return names;
    }
    
    /** Returns a true if at least one instrument is on the channel
     * @param channelNo
     * @return
     */
    public boolean hasInstrument(int channelNo) {
        if (channels.get(channelNo).instruments.isEmpty()) return false;
        return true;
    }

    /** Returns a true if at least one brick is on the channel
     * @param channelNo
     * @return
     */
    public boolean hasBrick(int channelNo) {
        if (channels.get(channelNo).instrumentBricks.size()==0) return false;
        return true;
    }
    
    public void dump() {
      for (int channelNo=0; channelNo<CHANNELCOUNT;channelNo++) {
        dump(channelNo);
     }
    }
    
    public void dump(int channelNo) {
      if (hasInstrument(channelNo)) {
        System.out.println("Channel: " + channelNo); 
      channels.get(channelNo).dump();
      System.out.println(); 
       }
    }

    
   
    /** Stores the instrument and Note range for a single channel
     * @author Aswin
     *
     */
    private class ChannelInfo {
      private List<Integer> instruments ;
        private int highestNote=0;
        private int lowestNote=127;
        private List<Brick> instrumentBricks ;
        private List<Brick> voiceBricks ;
        
        private ChannelInfo() {
            instrumentBricks = new ArrayList<Brick>();
            voiceBricks = new ArrayList<Brick>();
            instruments = new ArrayList<Integer>();
        }
        
        public void dump() {
          System.out.println("  Instruments: ");
          for (Integer instrument : instruments) {
            System.out.println("    " + instrument + " " +  Instrument.values()[instrument].toString());
          }
          System.out.println("  Lowest note: "+ lowestNote);
          System.out.println("  Highest note: "+ highestNote);
          System.out.println("  Bricks assigned as instrument: ");
          for (Brick brick : instrumentBricks) {
            System.out.println(String.format("    %s (%s)", brick.getName(),
                brick.getIPAddress()));
          }
          System.out.println("  Bricks assigned as voice: ");
          for (Brick brick : voiceBricks) {
            System.out.println(String.format("    %s (%s)", brick.getName(),
                brick.getIPAddress()));
          }
          
          
        }
    }






}
