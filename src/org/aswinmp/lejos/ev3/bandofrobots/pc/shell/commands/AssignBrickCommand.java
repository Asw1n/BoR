package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import BoRServer.BoRController;
import BoRServer.Brick;
import BoRServer.Channels;
import BoRServer.Song;

@ShellCommand(label = "assign", parameters = "brick_name channel", description = "assigns a connected brick to channel of the selected song")
public class AssignBrickCommand {

  private final BoRController boRController;

  public AssignBrickCommand(final BoRController boRController) {
    this.boRController = boRController;
  }

  @ShellExecute
  public void assignBrick(final String brickName,
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
              channels.setBrick(new Brick(brickInfo), channelNo);
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