package BoRBrick;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;

public class BaseMusician implements Musician {



    @Override
    public void start(){
    }

    @Override
    public void stop(){
    }

    @Override
    public void setTempo(int tempo) {
    }

    @Override
    public void noteOn(int tone)  {
      LocalEV3.get().getLED().setPattern(1);
    }

    @Override
    public void noteOff(int tone) {
      LocalEV3.get().getLED().setPattern(0);
    }}
