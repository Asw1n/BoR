package BoRBrick;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;

public class StartBoR {

  public static void main(String[] args) throws RemoteException{
    Registry registry;
    System.out.println("Starting RMI registry using port 1098");
  try {
    Musician obj = new GuitarPlayer();
  Musician stub = (Musician) UnicastRemoteObject.exportObject(obj, 0);

  // Bind the remote object's stub in the registry
  registry = LocateRegistry.createRegistry(1098);
  registry.bind("Musician", stub);
  for (String l : registry.list()) {
    System.out.println(l);
  }
  TextLCD lcd = LocalEV3.get().getTextLCD();
  lcd.clear();
  lcd.drawString("Musician waiting for conductor..", 0, 0);
  System.err.println("Musician ready");
}
catch (Exception e) {
  System.err.println("Musician exception: " + e.toString());
  e.printStackTrace();
}
  }

}