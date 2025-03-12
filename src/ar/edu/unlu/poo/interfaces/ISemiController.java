package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISemiController extends IObservadorRemoto {
    void connect() throws RemoteException;

    void disconnect(IObservadorRemoto clientViewObserver, IPlayer clientPlayer) throws RemoteException;

    void registerLocalObserver(IObservadorRemoto observer);

    void unregisterLocalObserver(IObservadorRemoto observer);

    ArrayList<IPlayer> fetchPlayers() throws RemoteException;

    IPlayer fetchPlayer(IPlayer player) throws RemoteException;

    ICard fetchQueriedCard() throws RemoteException;

    IPlayer fetchPlayingPlayer() throws RemoteException;

    boolean handlePlayerInput(String input) throws RemoteException;

    boolean handlePlayerExit(String input) throws RemoteException;
}
