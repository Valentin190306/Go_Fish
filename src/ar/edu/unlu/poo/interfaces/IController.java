package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IController {
    void connect() throws RemoteException;

    void disconnect(IObservadorRemoto clientViewObserver, IPlayer clientPlayer) throws RemoteException;

    void registerLocalObserver(IObservadorRemoto observer);

    void unregisterLocalObserver(IObservadorRemoto observer);

    IPlayer fetchClientPlayer() throws RemoteException;

    ArrayList<IPlayer> fetchPlayers() throws RemoteException;

    IPlayer fetchPlayer(IPlayer player) throws RemoteException;

    ICard fetchQueriedCard() throws RemoteException;

    IPlayer fetchPlayingPlayer() throws RemoteException;

    IPlayer fetchTargetPlayer() throws RemoteException;

    IDeck fetchDeck() throws RemoteException;

    boolean handlePlayerInput(String input) throws RemoteException;

    boolean handlePlayerExit(String input) throws RemoteException;

    <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException;
}
