package org.aswinmp.lejos.ev3.bandofrobots.tests.pc;

import java.util.Timer;
import java.util.TimerTask;

import org.aswinmp.lejos.ev3.bandofrobots.pc.player.BorPlayer;

public abstract class AbstractBorPCTest {

  protected void stopPlayerAfterPeriod(final BorPlayer borPlayer, final long periodInMilliseconds) {
    System.out
        .println(String
            .format("Will stop BoR player after %d milliseconds", periodInMilliseconds));
    final Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println(String
            .format("%d milliseconds have elapsed => stopping BoR player", periodInMilliseconds));
        borPlayer.shutDown();
        timer.cancel();
      }
    }, periodInMilliseconds);
  }
}
