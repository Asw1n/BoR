package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.IOException;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.BoRController;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Brick;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Channels;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Song;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;

@ShellCommand(label = "singer", parameters = "brick_name channel", description = "assigns a brick to sing a channel of the selected song")
public class AssignSingerCommand {

  private final BoRController boRController;

  public AssignSingerCommand(final BoRController boRController) {
    this.boRController = boRController;
  }

  @ShellExecute
  public void assignVoice(final String brickName,
      final String instrumentChannelText) {
    final Song song = boRController.getSong();
    if (!song.isSet()) {
      System.out.println("No song selected");
    } else {
      final int channelNo = Integer.parseInt(instrumentChannelText);
      final Channels channels = song.getChannels();

      if (channelNo < 0 || channelNo >= Channels.CHANNELCOUNT) {
        System.out.println(String.format("invalid channel %d", channelNo));
      } else {
        if (!channels.hasInstrument(channelNo)) {
          System.out.println(String.format("No instrument on channel %d",
              channelNo));
        } else {
          try {
            final BrickInfo brickInfo = findBrickInfo(brickName);
            if (brickInfo == null) {
              System.out.println(String.format(
                  "No brick connected matching '%s'", brickName));
            } else {
              channels.setVoiceBrick(Brick.get(brickInfo), channelNo);
              System.out.println(String.format(
                  "Brick '%s' assigned to channel %d", brickName, channelNo));
            }
          } catch (final IOException ioe) {
            System.err.println(ioe);
          }
        }
      }
    }
  }

  private BrickInfo findBrickInfo(final String brickName) throws IOException {
    BrickInfo result = null;
    for (final lejos.hardware.BrickInfo info : BrickFinder.discover()) {
      if (brickName.equals(info.getName())
          || brickName.equals(info.getIPAddress())) {
        result = info;
        break;
      }
    }
    return result;
  }
}
