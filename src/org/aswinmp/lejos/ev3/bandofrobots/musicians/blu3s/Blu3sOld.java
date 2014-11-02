package org.aswinmp.lejos.ev3.bandofrobots.musicians.blu3s;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import lejos.hardware.BrickFinder;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

import org.aswinmp.lejos.ev3.bandofrobots.musicians.AbstractMusician;

public class Blu3sOld /* extends AbstractMusician */ {
//	LimbOld rightHand, leftHand, foot, head;
//	boolean isUp = true;
//	boolean isSet = false;
//	int countDown = 0;
//
//	static LED led = LocalEV3.get().getLED();
//	boolean footup = false;
//	int pos = 1;
//	private final EV3UltrasonicSensor eyes;
//
//	public static void main(final String[] args) {
//		try {
//			final Blu3sOld blu3s = new Blu3sOld();
//			// register
//			blu3s.register();
//			// set LED pattern
//			led.setPattern(1);
//		} catch (RemoteException | AlreadyBoundException exc) {
//			System.err.println(exc.getMessage());
//			exc.printStackTrace();
//		}
//	}
//
//	public Blu3sOld() {
//		super();
//		setBeatPulseDevider(2);
//		rightHand = new LimbOld(new EV3MediumRegulatedMotor(MotorPort.A), -20);
//		leftHand = new LimbOld(new EV3MediumRegulatedMotor(MotorPort.B), -150);
//		foot = new LimbOld(new EV3MediumRegulatedMotor(MotorPort.C), -15);
//		head = new LimbOld(new EV3MediumRegulatedMotor(MotorPort.D), -45);
//		head.setLogicRange(0, 127);
//		rightHand.setLogicRange(-1, 1);
//		LCD.clear();
//
//		// head.calibrate(5, 1, -15, 0.2f);
//		// rightHand.calibrate(5, 1, -5, 0.2f);
//		// leftHand.calibrate(10, -1, 170, 0.2f);
//		// foot.calibrate(7, 1, -60, 0.2f);
//
//		leftHand.setSpeed(1);
//		rightHand.setSpeed(1);
//
//		head.testRange();
//		foot.testRange();
//		leftHand.testRange();
//		rightHand.testRange();
//
//		eyes = new EV3UltrasonicSensor(BrickFinder.getDefault().getPort("S2"));
//		eyes.disable();
//
//	}
//
//	@Override
//	public void setDynamicRange(final int lowestNote, final int highestNote) {
//		super.setDynamicRange(lowestNote, highestNote);
//		leftHand.setLogicRange(lowestNote, highestNote);
//	}
//
//	@Override
//	public void noteOn(final int tone, final int intensity) {
//		super.noteOn(tone, intensity);
//		// the right hand moves up or down on a note. It changes direction every
//		// quarter note.
//		if (!isSet) {
//			rightHand.setSpeed(127f / intensity);
//			if (isUp) {
//				rightHand.moveToMin(true);
//			} else {
//				rightHand.moveToMax(true);
//			}
//			isUp = !isUp;
//			isSet = true;
//		}
//		// The left hand moves full speed to the position
//		leftHand.setSpeed(1);
//		leftHand.moveTo(tone, true);
//	}
//
//	@Override
//	public void voiceOn(final int tone, final int intensity) {
//		super.voiceOn(tone, intensity);
//		// The mouth opens when singing according to intensity of the note.
//		head.moveTo(intensity, true);
//	}
//
//	@Override
//	public void voiceOff(final int tone) {
//		super.voiceOff(tone);
//		head.moveToMin(true);
//	}
//
//	@Override
//	public void start() {
//		super.start();
//		eyes.enable();
//		led.setPattern(3);
//	}
//
//	@Override
//	public void stop() {
//		// Let every limb go to default position and put to rest;
//		super.stop();
//		head.setSpeed(0.2f);
//		led.setPattern(1);
//		leftHand.setSpeed(0.2f);
//		leftHand.moveToMin(true);
//		rightHand.setSpeed(0.2f);
//		rightHand.moveToMin(true);
//		foot.setSpeed(0.1f);
//		foot.moveToMin(true);
//		Delay.msDelay(1200);
//		head.moveToMin(true);
//		leftHand.rest();
//		rightHand.rest();
//		head.rest();
//		foot.rest();
//		eyes.disable();
//		countDown = 0;
//	}
//
//	@Override
//	public void beatPulse(final int beatNo, final int pulseNo) {
//		super.beatPulse(beatNo, pulseNo);
//
//		// The left arm may change direction every DIV * quarter note
//		isSet = false;
//
//		// Have some LEDs at the start to synchronize video shots
//		if (countDown < 8) {
//			if (pulseNo % 2 == 0) {
//				led.setPattern(2);
//			} else {
//				led.setPattern(0);
//			}
//			countDown++;
//		}
//
//		// The leg goes up and down every quarter note
//		if (pulseNo % 2 == 0) {
//			foot.setSpeed(1);
//			foot.moveToMin(true);
//		} else {
//			foot.setSpeed(0.1f);
//			foot.moveToMax(true);
//		}
//	}
//
}
