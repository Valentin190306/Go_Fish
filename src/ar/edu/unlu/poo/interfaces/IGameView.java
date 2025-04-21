package ar.edu.unlu.poo.interfaces;


import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public interface IGameView {

    void start();

    void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException;
}
