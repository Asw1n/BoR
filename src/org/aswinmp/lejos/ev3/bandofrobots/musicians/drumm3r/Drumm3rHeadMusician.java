package org.aswinmp.lejos.ev3.bandofrobots.musicians.drumm3r;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import lejos.hardware.Sound;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;

public class Drumm3rHeadMusician extends AbstractMusician {

	private final Drumm3rHead drumm3rHead;

	public static void main(final String[] args) {
		try {
			final Drumm3rHeadMusician drumm3rHeadMusician = new Drumm3rHeadMusician();
			// register
			drumm3rHeadMusician.register();
			// play sound to signal that setting up the Drumm3rHead is complete
			Sound.beepSequenceUp();
		} catch (RemoteException | AlreadyBoundException exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
	}

	public Drumm3rHeadMusician() {
		super();
		drumm3rHead = new Drumm3rHead();
		drumm3rHead.reset();
	}

	@Override
	public void start() {
		super.start();
		drumm3rHead.openEyes();
		drumm3rHead.enableLEDPattern(true);
	}

	@Override
	public void stop() {
		super.stop();
		drumm3rHead.reset();
	}

}
