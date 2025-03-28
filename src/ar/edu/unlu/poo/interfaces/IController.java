package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IController {
    IGameView getGameView();

    void setGameView(IGameView gameView);

    void connect() throws RemoteException;

    void disconnect() throws RemoteException;

    IPlayer fetchClientPlayer() throws RemoteException;

    ArrayList<IPlayer> fetchPlayers() throws RemoteException;

    ICard fetchQueriedCard() throws RemoteException;

    IPlayer fetchPlayingPlayer() throws RemoteException;

    IPlayer fetchTargetPlayer() throws RemoteException;

    IDeck fetchDeck() throws RemoteException;

    HashMap<String, Integer> fetchScores() throws RemoteException;

    void setClientPlayerReady() throws RemoteException;

    void updateClientPlayerName(String name) throws RemoteException;

    boolean handlePlayerInput(String input) throws RemoteException;

    boolean handlePlayerExit(String input) throws RemoteException;

    <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException;
}
