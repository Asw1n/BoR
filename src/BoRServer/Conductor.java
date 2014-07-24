package BoRServer;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/** The conductor receives midi events from a sequencer and passes these on to the appropriate bricks.
 * @author Aswin
 *
 */
public class Conductor implements Receiver, MetaEventListener {
    private Channels channels;

    public void setSong(Song song) {
        channels = song.getChannels();
    }

    /* 
     * We are intereseted in tempo changes. This method receives all meta events, setting tempo is one of them 
     */
    @Override
    public void meta(MetaMessage metaMessage) {
        if (metaMessage.getMessage()[1] == 81) {
            // calculate tempo from meta message
            int tempo = 0;
            for (int p = 2; p >= 0; p--) {
                tempo += ((int) (metaMessage.getMessage()[5 - p] & 0xFF)) * Math.pow(255, p);
            }
            tempo = Math.round(4 * tempo / 1000f);
            System.out.println("Set tempo: " + tempo);
            
            // Send tempo to all musicians
            for (int channel = 0; channel < Channels.CHANNELCOUNT; channel++) {
                for (Brick brick : channels.getBricks(channel)) {
                    brick.setTempo(tempo);
                }
            }
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void send(MidiMessage arg0, long arg1) {
        if (arg0 instanceof ShortMessage) {
            ShortMessage message = (ShortMessage) arg0;
            int channel = message.getChannel();
            int command = message.getCommand();
            if (command == ShortMessage.NOTE_ON || command == ShortMessage.NOTE_OFF) {
                for (Brick brick : channels.getBricks(channel)) {
                    brick.sendEvent(message);
                }
            }
        }
    }

}
