package PC;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import lejos.utility.Delay;

public class TimeBuffer extends Thread implements Transmitter, Receiver{
    private Queue<MidiMessage>  messages= new ConcurrentLinkedQueue<MidiMessage> ();
    private Queue<Long>  timeStamps= new ConcurrentLinkedQueue<Long> ();
    private long delay=0;
    private Receiver receiver;
    
    public TimeBuffer(long delay) {
        setDelay(delay);
    }

    @Override
    public void run() {
        System.out.println("Ttime buffer started");
        while(true) {
            if (!messages.isEmpty() & !timeStamps.isEmpty()) {
                MidiMessage event=messages.poll();
                long timeStamp=timeStamps.poll();
                long now=System.currentTimeMillis();
                if(timeStamp>now) 
                    Delay.msDelay(timeStamp-now);
                if (receiver !=null) {
                    receiver.send(event, -1);
                }
            }
            yield();
        }
    }
    
    public void setDelay(long delay) {
        this.delay=delay;
    }
    
    @Override
    public synchronized void  send(MidiMessage event, long timeStamp) {
        messages.add(event);
        timeStamps.add(System.currentTimeMillis()+delay);
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
    public void setReceiver(Receiver receiver) {
        this.receiver=receiver;
    }

}
