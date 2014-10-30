package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;

public class Drumm3rMusician extends AbstractMusician {

	public static void main(final String[] args) {
		try {
			final Drumm3rMusician drumm3rMusician = new Drumm3rMusician();
			// register
			drumm3rMusician.register();
		} catch (RemoteException | AlreadyBoundException exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
	}

}
