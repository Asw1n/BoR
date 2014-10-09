package org.aswinmp.lejos.ev3.bandofrobots.musicians.borbrick;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;




/** Base class for musicians. Implements Musician. <br>
 * Provides debug information for Musician methods. <br>
 * Provides some utility methods (in the future).
 * @author Aswin
 *
 */
public class BaseMusician implements Musician {
    protected int tempo =500;
    protected boolean running=false;
    private long nextBeat=0;
    private boolean generateBeatPulse=true;
    Runner runner;
    private int beatPulseDevider=1;
    
    
    public static void main(final String[] args) throws RemoteException {
      Registry registry;
      final List<String> ips = getIPAddresses();
      // Use last IP address, which will be Wifi, it it exists
      String lastIp = null;
      for (final String ip : ips) {
        lastIp = ip;
        System.out.println(ip);
      }
      System.out.println("Setting java.rmi.server.hostname to " + lastIp);
      System.setProperty("java.rmi.server.hostname", lastIp);

      System.out.println("Starting RMI registry using port 1098");
      try {
        // Musician obj = new BaseMusician();
        final Musician obj = new GuitarPlayer();
        final Musician stub = (Musician) UnicastRemoteObject.exportObject(
            obj, 0);

        // Bind the remote object's stub in the registry
        registry = LocateRegistry.createRegistry(1098);
        registry.bind("Musician", stub);
        final TextLCD lcd = LocalEV3.get().getTextLCD();
        lcd.clear();
        lcd.drawString("Musician waiting for conductor to connect", 0, 0);
        System.err.println("Musician ready");
      } catch (final Exception e) {
        System.err.println("Musician exception: " + e.toString());
        e.printStackTrace();
      }
    }

    /**
     * Get all the IP addresses for the device
     */
    public static List<String> getIPAddresses() {
      final List<String> result = new ArrayList<String>();
      Enumeration<NetworkInterface> interfaces;
      try {
        interfaces = NetworkInterface.getNetworkInterfaces();
      } catch (final SocketException e) {
        System.err.println("Failed to get network interfaces: " + e);
        return null;
      }
      while (interfaces.hasMoreElements()) {
        final NetworkInterface current = interfaces.nextElement();
        try {
          if (!current.isUp() || current.isLoopback()
              || current.isVirtual())
            continue;
        } catch (final SocketException e) {
          System.err.println("Failed to get network properties: " + e);
        }
        final Enumeration<InetAddress> addresses = current
            .getInetAddresses();
        while (addresses.hasMoreElements()) {
          final InetAddress current_addr = addresses.nextElement();
          if (current_addr.isLoopbackAddress())
            continue;
          result.add(current_addr.getHostAddress());
        }
      }
      return result;
    }
    
    
 

  public BaseMusician() {
  System.out.println("Musician "+this.getClass().getName()+ " loaded.");
  runner = new Runner();
  runner.setDaemon(true);
  runner.start();
}

  @Override
  public void start() {
    System.out.println("Start song");
    nextBeat=System.currentTimeMillis();
    running=true;
  }

  @Override
  public void stop() {
    System.out.println("End song");
    running=false;
  }

  @Override
  public void setTempo(int tempo) {
    System.out.println("Set tempo: " + tempo);
    this.tempo=tempo;
  }

  @Override
  public void noteOn(int tone) {
    System.out.println("Tone on: " + tone);
  }

  @Override
  public void noteOff(int tone) {
    System.out.println("Tone off: " + tone);
  }

  @Override
  public void setDynamicRange(int lowestNote, int highestNote) throws RemoteException {
    System.out.println("Dynamic range: " + lowestNote + " to " + highestNote );
  }
  
  public void generateBeatPulse(boolean v) {
      generateBeatPulse =v;
  }
  
  /** 
   * Specify how many times per quarter note a beat pulse is raised
   * @param devider
   */
  public void setBeatPulseDevider(int devider) {
    this.beatPulseDevider=devider;
  }
  
  protected void beatPulse(int beatNo) {
      System.out.println("Beat" );
  }
  
  private class Runner extends Thread {
      int beatNo =0;
      @Override
      public void run() {
        while (true) {
          long time=System.currentTimeMillis();
          if (running) {
              if (time>=nextBeat) {
                  beatNo ++;
                  if (beatNo>4*beatPulseDevider) beatNo=1;
                  if (generateBeatPulse)
                      beatPulse(beatNo);
                  nextBeat+=tempo/beatPulseDevider;
              }
          }
          Thread.yield();
          

        }
      }

    }
}
  
 

