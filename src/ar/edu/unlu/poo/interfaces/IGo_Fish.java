package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IGo_Fish extends IObservableRemoto {
    void start() throws RemoteException;

    void playTurn(Value valueRequested, IPlayer targetPlayer) throws RemoteException;

    boolean checkGameIsOver() throws RemoteException;

    IPlayer addPlayer() throws RemoteException;

    void removePlayer(IPlayer player) throws RemoteException;

    IPlayer getPlayerByID(int ID) throws RemoteException;

    void setPlayerReady(Player player) throws RemoteException;

    IDeck getDeck() throws RemoteException;

    IPlayer getTargetPlayer() throws RemoteException;

    Player getPlayerCalled(String name) throws RemoteException;

    ArrayList<IPlayer> getPlayers() throws RemoteException;

    IPlayer getCurrentPlayerPlayingTurn() throws RemoteException;

    GameState getGameState() throws RemoteException;

    void reload() throws RemoteException;
}
