package BoRBrick;

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

import org.aswinmp.lejos.ev3.bandofrobots.musicians.guitarist.Guitarist;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

/**
 * Main entry for BoR on the brick. <br>
 * Use this program to make the brick wait for a connection initiated by the PC.
 * 
 * @author Aswin
 * 
 */
public class StartBoR {

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
			final Musician obj = new Guitarist();
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

}
