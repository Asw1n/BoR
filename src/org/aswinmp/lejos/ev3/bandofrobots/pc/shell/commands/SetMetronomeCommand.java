package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Channels;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Song;


@ShellCommand(label = "metronome", parameters = "channel", description = "sets the metronome channel of the selected song")
public class SetMetronomeCommand {

  private final BoRController boRController;

  public SetMetronomeCommand(final BoRController boRController) {
    this.boRController = boRController;
  }

  @ShellExecute
  public void setMetronome(final String channelName) {
    final Song song = boRController.getSong();
    if (!song.isSet()) {
      System.out.println("No song selected");
    } else {
      final int channelNo = Integer.parseInt(channelName);
      final Channels channels = song.getChannels();

      if (channelNo < 0 || channelNo >= Channels.CHANNELCOUNT) {
        System.out.println(String.format("invalid channel %d", channelNo));
      } else {
        if (!channels.hasInstrument(channelNo)) {
          System.out.println(String.format("No instrument on channel %d",
              channelNo));
        } else {
          song.setMetronomeChannel( channelNo);
          System.out.println(String.format(
              "Set channel '%s' to metronome",  channelNo));
        }
      }
    }
  }

 
}
