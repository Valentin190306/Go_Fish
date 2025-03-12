package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface IController extends IObservadorRemoto {

    HashMap<String, Integer> getScores() throws RemoteException;

    IPlayer getClientPlayer() throws RemoteException;

    void changeClientPlayerName(String name) throws RemoteException;

    void setClientPlayerReady() throws RemoteException;

    void setGameWindow(IGameWindow gameWindow) throws RemoteException;

    List<IPlayer> getPlayerList() throws RemoteException;

    void disconnect() throws RemoteException;

    boolean handlePlayerInput(String input) throws RemoteException;

    void connect() throws RemoteException;

    void setView(IGameView view) throws RemoteException;

    boolean handlePlayerExit(String input) throws RemoteException;
}

