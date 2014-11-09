package org.aswinmp.lejos.ev3.bandofrobots.musicians;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.aswinmp.lejos.ev3.bandofrobots.utils.BrickLogger;

/**
 * Base class for musicians. Implements Musician. <br>
 * Provides debug information for Musician methods. <br>
 * Provides some utility methods (in the future).
 * 
 * @author Aswin
 * 
 */
public abstract class AbstractMusician implements Musician {
	protected int tempo = 500;
	protected boolean running = false;
	private long nextBeat = 0;
	int beatNo = 0;
	int pulseNo = 0;
	private boolean generateBeatPulse = true;
	Runner runner;
	private int beatPulseDevider = 1;
	/*
	 * TODO verbosity should be handled by a configuring the logger instance,
	 * not by configuring the Musician
	 */
	protected boolean verbose = false;
	private boolean synchroniseBeat = false;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(final boolean verbose) {
		this.verbose = verbose;
	}

	protected void register() throws RemoteException, AlreadyBoundException {
		BrickLogger.info("Registering %s", this);
		Registry registry;
		final List<String> ips = getIPAddresses();
		// Use last IP address, which will be Wifi, it it exists
		String lastIp = null;
		for (final String ip : ips) {
			lastIp = ip;
			System.out.println(ip);
		}
		BrickLogger.info("Setting java.rmi.server.hostname to %s", lastIp);
		System.setProperty("java.rmi.server.hostname", lastIp);

		final int registryPort = 1098;
		BrickLogger.info("Starting RMI registry using port %d", registryPort);
		final Musician stub = (Musician) UnicastRemoteObject.exportObject(this,
				0);

		// Bind the remote object's stub in the registry
		registry = LocateRegistry.createRegistry(registryPort);
		registry.bind("Musician", stub);
		BrickLogger.info("Musician waiting for conductor to connect");
		BrickLogger.info("Musician %s ready", this);
	}

	/**
	 * Get all the IP addresses for the device
	 */
	public static List<String> getIPAddresses() {
		final List<String> result = new ArrayList<String>();
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (final SocketException soExc) {
			BrickLogger.error("Failed to get network interfaces", soExc);
			return null;
		}
		while (interfaces.hasMoreElements()) {
			final NetworkInterface current = interfaces.nextElement();
			try {
				if (!current.isUp() || current.isLoopback()
						|| current.isVirtual())
					continue;
			} catch (final SocketException soExc) {
				BrickLogger.error("Failed to get network properties", soExc);
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

	public AbstractMusician() {
		if (verbose)
			BrickLogger.info("Musician %s loaded", this);
		runner = new Runner();
		runner.setDaemon(true);
		runner.start();
	}

	@Override
	public void start() {
		if (verbose)
			BrickLogger.info("Starting song");
		nextBeat = System.currentTimeMillis();
		running = true;
	}

	@Override
	public void stop() {
		if (verbose)
			BrickLogger.info("Ending song");
		running = false;
	}

	@Override
	public void setTempo(final int tempo) {
		if (verbose)
			BrickLogger.info("Set tempo to %d", tempo);
		this.tempo = tempo;
	}

	@Override
	public void noteOn(final int tone, final int intensity) {
		if (verbose)
			BrickLogger.info("Tone %d, %d on", tone, intensity);
	}

	@Override
	public void noteOff(final int tone) {
		if (verbose)
			BrickLogger.info("Tone %s off", tone);
	}

	@Override
	public void voiceOn(final int tone, final int intensity) {
		if (verbose)
			BrickLogger.info("Voice %d, %d on", tone, intensity);
	}

	@Override
	public void voiceOff(final int tone) {
		if (verbose)
			BrickLogger.info("Voice %d off", tone);
	}

	@Override
	public void setDynamicRange(final int lowestNote, final int highestNote)
			throws RemoteException {
		if (verbose)
			BrickLogger
					.info("Dynamic range: %d to %d", lowestNote, highestNote);
	}

	public void generateBeatPulse(final boolean v) {
		generateBeatPulse = v;
	}

	/**
	 * Specify how many times per quarter note a beat pulse is raised
	 * 
	 * @param devider
	 */
	public void setBeatPulseDevider(final int devider) {
		this.beatPulseDevider = devider;
	}

	protected void beatPulse(final int beatNo, final int pulseNo) {
		if (verbose)
			BrickLogger.info("BeatPulse %d, %d", beatNo, pulseNo);
	}

	private class Runner extends Thread {
		@Override
		public void run() {
			if (verbose)
				BrickLogger.info("Beat provider started");
			while (true) {
				final long time = System.currentTimeMillis();
				if (running) {
					if (synchroniseBeat) {
						beatNo++;
						if (beatNo == 4)
							beatNo = 0;
						pulseNo = 0;
						beatPulse(beatNo, pulseNo);
						nextBeat += tempo / beatPulseDevider;
						pulseNo++;
						synchroniseBeat = false;
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
		synchroniseBeat = true;
	}
}
