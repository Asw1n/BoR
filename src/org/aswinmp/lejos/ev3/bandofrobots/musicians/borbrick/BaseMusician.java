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

import org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s.Blu3s;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

/**
 * Base class for musicians. Implements Musician. <br>
 * Provides debug information for Musician methods. <br>
 * Provides some utility methods (in the future).
 * 
 * @author Aswin
 *
 */
public class BaseMusician implements Musician {
  protected int     tempo             = 500;
  protected boolean running           = false;
  private long      nextBeat          = 0;
  int               beatNo            = 0;
  int               pulseNo           = 0;
  private boolean   generateBeatPulse = true;
  Runner            runner;
  private int       beatPulseDevider  = 1;
  protected boolean verbose           = false;
  private boolean   synchroniseBeat   = false;

  public boolean isVerbose() {
    return verbose;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

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
      final Musician obj = new Blu3s();
      final Musician stub = (Musician) UnicastRemoteObject.exportObject(obj, 0);

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
        if (!current.isUp() || current.isLoopback() || current.isVirtual())
          continue;
      } catch (final SocketException e) {
        System.err.println("Failed to get network properties: " + e);
      }
      final Enumeration<InetAddress> addresses = current.getInetAddresses();
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
    if (verbose)
      System.out.println("Musician " + this.getClass().getName() + " loaded.");
    runner = new Runner();
    runner.setDaemon(true);
    runner.start();
  }

  @Override
  public void start() {
    if (verbose)
      System.out.println("Start song");
    nextBeat = System.currentTimeMillis();
    running = true;
  }

  @Override
  public void stop() {
    if (verbose)
      System.out.println("End song");
    running = false;
  }

  @Override
  public void setTempo(int tempo) {
    if (verbose)
      System.out.println("Set tempo: " + tempo);
    this.tempo = tempo;
  }

  @Override
  public void noteOn(int tone, int intensity) {
    if (verbose)
      System.out.println(String.format("Tone on: %d, %d ", tone, intensity));
  }

  @Override
  public void noteOff(int tone) {
    if (verbose)
      System.out.println("Tone off: " + tone);
  }

  @Override
  public void voiceOn(int tone, int intensity) {
    if (verbose)
      System.out.println(String.format("Voice on: %d, %d ", tone, intensity));
  }

  @Override
  public void voiceOff(int tone) {
    if (verbose)
      System.out.println("Voice off: " + tone);
  }

  @Override
  public void setDynamicRange(int lowestNote, int highestNote)
      throws RemoteException {
    if (verbose)
      System.out.println("Dynamic range: " + lowestNote + " to " + highestNote);
  }

  public void generateBeatPulse(boolean v) {
    generateBeatPulse = v;
  }

  /**
   * Specify how many times per quarter note a beat pulse is raised
   * 
   * @param devider
   */
  public void setBeatPulseDevider(int devider) {
    this.beatPulseDevider = devider;
  }

  protected void beatPulse(int beatNo, int pulseNo) {
    if ( verbose)
      System.out.println(String.format("BeatPulse: %d, %d", beatNo, pulseNo));
  }

  private class Runner extends Thread {
    @Override
    public void run() {
      if (verbose)
        System.out.println("Beat provider started ");
      while (true) {
        long time = System.currentTimeMillis();
        if (running) {
          if (synchroniseBeat) {
            beatNo++;
            if (beatNo == 4)
              beatNo = 0;
            pulseNo = 0;
            beatPulse(beatNo, pulseNo);
            nextBeat += tempo / beatPulseDevider;
            pulseNo++;
            synchroniseBeat=false;
          } else if (time >= nextBeat) {
            if (generateBeatPulse)
              beatPulse(beatNo, pulseNo);
            nextBeat += tempo / beatPulseDevider;
            pulseNo++;
            if (pulseNo == beatPulseDevider) {
              pulseNo = 0;
              beatNo++;
            }
            if (beatNo == 4)
              beatNo = 0;
          }
        }
        Thread.yield();

      }
    }

  }

  @Override
  public void Beat() throws RemoteException {
    synchroniseBeat=true;
  }
}
