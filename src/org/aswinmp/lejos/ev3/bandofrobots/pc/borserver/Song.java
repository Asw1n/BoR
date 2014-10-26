package org.aswinmp.lejos.ev3.bandofrobots.pc.borserver;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;




/** This class represents a Midi song 
 * @author Aswin
 *
 */
public class Song {
    protected File     song;
    protected Channels channels = new Channels();
    protected boolean isSet;
    protected int metronomeChannel =0;
    protected boolean metronomeIsSet=false;

    public boolean isMetronomeIsSet() {
      return metronomeIsSet;
    }

    /** Sets a midi song by its pathname
     * @param fileName
     * @throws IOException 
     * @throws InvalidMidiDataException 
     */
    public void setFilePath(String fileName) throws InvalidMidiDataException, IOException {
        setSong(new File(fileName));

    }

    /** Sets a midi song using a File
     * @param song
     * @throws IOException 
     * @throws InvalidMidiDataException 
     */
    protected void setSong(File song) throws InvalidMidiDataException, IOException {
        this.song = song;
        channels.clear();
        scanSong();
    }

    /** Returns the pathname of a midi song
     * @return
     */
    public String getFilePath() {
        return song.getAbsolutePath();
    }

    /** Returns the file object of a midi song
     * @return
     */
    protected File getSong() {
        return song;
    }
    
    /** Returns the Channels used in the midi song
     * @return
     */
    public Channels getChannels() {
        return channels;
    }
    
    public void dumpChannels() {
      channels.dump();
    }

    /**
     * Scans the song to determine the instrument used on each of the 16 channels and the highest and lowest note played on each channel. 
     * @throws IOException 
     * @throws InvalidMidiDataException 
     */
    protected void scanSong() throws InvalidMidiDataException, IOException {
            setSet(false);
            Sequence seq = MidiSystem.getSequence(song);
            Track[] tracks = seq.getTracks();
            for (Track track : tracks) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    if (event.getMessage() instanceof ShortMessage) {
                        ShortMessage message = (ShortMessage) event.getMessage();
                        int channel = message.getChannel();
                        int command = message.getCommand();
                        int data1 = message.getData1();
                        //int data2 = message.getData2();
                        switch (command) {
                            case ShortMessage.PROGRAM_CHANGE: {
                                channels.setIntrument(channel, data1+1);
                                break;
                            }
                            case ShortMessage.NOTE_ON: {
                                channels.updateRange(channel, data1);
                                break;
                            }
                            default:
                                break;
                        }
                    }
                }
            }
            setSet(true);

    }

    public boolean isSet() {
      return isSet;
    }

    private void setSet(boolean isSet) {
      this.isSet = isSet;
    }

    public void setMetronomeChannel(int channelNo) {
      metronomeIsSet=true;
      metronomeChannel=channelNo;
    }

}
