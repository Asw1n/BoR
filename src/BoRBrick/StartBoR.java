package BoRBrick;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class StartBoR {

  public static void main(String[] args) throws RemoteException{
    System.out.println("Registring BaseMusician to RMI registry");
  try {
    Musician obj = new BaseMusician();
  Musician stub = (Musician) UnicastRemoteObject.exportObject(obj, 0);

  // Bind the remote object's stub in the registry
  Registry registry = LocateRegistry.getRegistry();
  registry.bind("Musician", stub);
  for (String l : registry.list()) {
    System.out.println(l);
  }

  System.err.println("Musician ready");
}
catch (Exception e) {
  System.err.println("Musician exception: " + e.toString());
  e.printStackTrace();
}
  }

}
