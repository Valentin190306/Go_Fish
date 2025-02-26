package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IGameManager extends IObservableRemoto {

    void createNewMatch();

    void saveMatch() throws IOException;

    void restoreMatch() throws IOException, ClassNotFoundException;

    IPlayer connectPlayer() throws RemoteException;

    void removePlayer(IPlayer clientPlayer) throws RemoteException;

    void disconnectPlayer(IObservadorRemoto controller, Player player) throws RemoteException;

    void configPlayerName(Player remotePlayer, String name) throws RemoteException;

    IPlayer getPlayer(Player remotePlayer) throws RemoteException;

    ArrayList<IPlayer> getPlayers() throws RemoteException;

    IPlayer getPlayerCalled(String name) throws RemoteException;

    void setPlayerReady(Player remotePlayer) throws RemoteException;

    String getFilePath();

    void setFilePath(String filePath);
}
