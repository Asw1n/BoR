package BoRServer;

import java.util.ArrayList;
import java.util.List;


/** This class channels a midi channel to a midi instrument and keep track of the Note range of each channel/instrument
 * @author Aswin
 *
 */
public class Channels {
    static final int CHANNELCOUNT=16;
    private List<ChannelInfo> channels ;
    
    public Channels() {
        channels = new ArrayList<ChannelInfo>(16);
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
    public void setIntrument(int channelNo, int instrument) {
        ChannelInfo channel = channels.get(channelNo);
        channel.instrument=instrument;
        channel.highestNote=0;
        channel.lowestNote=127;
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
    public int getInstrument(int channel) {
       return channels.get(channel).instrument; 
    }
    
    public String getInstrumentName(int channel) {
        if (getInstrument(channel)==0) return "";
        else return Instrument.values()[getInstrument(channel)].toString();
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
    public List<Brick> getBricks(int channelNo) {
        return channels.get(channelNo).bricks;
    }
    
    public String getBrickNames(int channelNo) {
        String names= "";
        List<Brick> bricks = getBricks(channelNo);
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
        if (channels.get(channelNo).instrument==0) return false;
        return true;
    }

    /** Returns a true if at least one brick is on the channel
     * @param channelNo
     * @return
     */
    public boolean hasBrick(int channelNo) {
        if (channels.get(channelNo).bricks.size()==0) return false;
        return true;
    }

    
   
    /** Stores the instrument and Note range for a single channel
     * @author Aswin
     *
     */
    private class ChannelInfo {
        private int instrument= 0;
        private int highestNote=0;
        private int lowestNote=127;
        private List<Brick> bricks ;
        
        private ChannelInfo() {
            bricks = new ArrayList<Brick>();
        }
    }


}
