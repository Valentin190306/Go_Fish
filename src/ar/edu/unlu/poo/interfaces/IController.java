package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.view.LobbyPanel;

import java.rmi.RemoteException;
import java.util.List;

public interface IController {

    IPlayer getClientPlayer();

    void changeClientPlayerName(String name) throws RemoteException;

    void setClientPlayerReady() throws RemoteException;

    void setLobby(LobbyPanel lobby);

    List<IPlayer> getPlayerList() throws RemoteException;

    void disconnect() throws RemoteException;

    boolean handlePlayerInput(String input);

    void connect() throws RemoteException;

    void disconnectPlayer() throws RemoteException;

    void setView(IGameView view);
}

