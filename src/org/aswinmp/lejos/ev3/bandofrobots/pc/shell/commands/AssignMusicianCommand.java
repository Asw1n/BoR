package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;

import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Brick;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Channels;
import org.aswinmp.lejos.ev3.bandofrobots.pc.borserver.Song;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.BoRCommandException;
import org.aswinmp.lejos.ev3.bandofrobots.pc.shell.CommandContext;

@ShellCommand(label = "musician", parameters = "brick_name channel", description = "assigns a brick to play a channel of the selected song")
public class AssignMusicianCommand extends AbstractBoRCommand {

  public AssignMusicianCommand(final CommandContext commandContext) {
    super(commandContext);
  }

  @ShellExecute
  public void assignBrick(final String brickName, final String instrumentChannelText)
      throws BoRCommandException {
    int channelNo = -1;
    try {
      channelNo = Integer.parseInt(instrumentChannelText);
    } catch (final NumberFormatException nfe) {
      throw new BoRCommandException(nfe);
    }
    assignBrick(brickName, channelNo);
  }

  public void assignBrick(final String brickName, final int channelNo)
      throws BoRCommandException {
    final Song song = getBoRController().getSong();
    if (!song.isSet()) {
      throw new BoRCommandException("No song selected");
    }

    final Channels channels = song.getChannels();

    if (channelNo < 0 || channelNo >= Channels.CHANNELCOUNT) {
      throw new BoRCommandException(String.format("invalid channel %d", channelNo));
    }
    if (!channels.hasInstrument(channelNo)) {
      throw new BoRCommandException(String.format("No instrument on channel %d", channelNo));
    }
    try {
      final BrickInfo brickInfo = findBrickInfo(brickName);
      if (brickInfo == null) {
        throw new BoRCommandException(String.format("No brick connected matching '%s'", brickName));
      }
      channels.setInstrumentBrick(Brick.get(brickInfo), channelNo);
      System.out
          .println(String
              .format("Brick '%s' assigned as musician to channel %d", brickName, channelNo));

    } catch (final IOException exc) {
      throw new BoRCommandException(exc);
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
