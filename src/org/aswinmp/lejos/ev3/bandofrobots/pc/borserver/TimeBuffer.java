package org.aswinmp.lejos.ev3.bandofrobots.pc.borserver;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import lejos.utility.Delay;

/**
 * The Time Buffer delays midi events sent from a Midi Transmitter to a Midi
 * Receiver and must be placed between the two.
 * @author Aswin Bouwmeester
 */
public class TimeBuffer extends Thread implements Transmitter, Receiver {
  private final Queue<MidiMessage> messages = new ConcurrentLinkedQueue<MidiMessage>();
  private final Queue<Long> timeStamps = new ConcurrentLinkedQueue<Long>();
  private long delay = 0;
  private Receiver receiver;

  public TimeBuffer(final long delay) {
    setDelay(delay);
  }

  @Override
  public void run() {
    // System.out.println("Time buffer started");
    while (true) {
      if (!messages.isEmpty() && !timeStamps.isEmpty()) {
        final MidiMessage event = messages.poll();
        final long timeStamp = timeStamps.poll();
        final long now = System.currentTimeMillis();
        if (timeStamp > now)
          Delay.msDelay(timeStamp - now);
        if (receiver != null) {
          receiver.send(event, -1);
        }
      } else
        yield();
    }
  }

  public void setDelay(final long delay) {
    this.delay = delay;
  }

  @Override
  public synchronized void send(final MidiMessage event, final long timeStamp) {
    messages.add(event);
    timeStamps.add(System.currentTimeMillis() + delay);
  }

  @Override
  public void close() {
    messages.clear();
    timeStamps.clear();
    receiver.close();
  }

  @Override
  public Receiver getReceiver() {
    return receiver;
  }

  @Override
  public void setReceiver(final Receiver receiver) {
    this.receiver = receiver;
  }

  public long getDelay() {
    return delay;
  }

}
