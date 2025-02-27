package ar.edu.unlu.poo.interfaces;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface IController {

    HashMap<String, Integer> getScores() throws IOException, ClassNotFoundException;

    IPlayer getClientPlayer();

    void changeClientPlayerName(String name) throws RemoteException;

    void setClientPlayerReady() throws RemoteException;

    void setGameWindow(IGameWindow gameWindow);

    List<IPlayer> getPlayerList() throws RemoteException;

    void disconnect() throws RemoteException;

    boolean handlePlayerInput(String input);

    void connect() throws RemoteException;

    void disconnectPlayer() throws RemoteException;

    void setView(IGameView view);
}

