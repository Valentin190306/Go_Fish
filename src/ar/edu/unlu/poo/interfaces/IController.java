package ar.edu.unlu.poo.interfaces;

import java.rmi.RemoteException;
import java.util.List;

public interface IController {

    IPlayer getClientPlayer();

    void setClientPlayerReady() throws RemoteException;

    void setLobby(ILobby lobby);

    List<IPlayer> getPlayerList() throws RemoteException;

    void disconnect() throws RemoteException;

    boolean handlePlayerInput(String input);

    void connect() throws RemoteException;

    void disconnectPlayer() throws RemoteException;

    void setView(IGameView view);
}

