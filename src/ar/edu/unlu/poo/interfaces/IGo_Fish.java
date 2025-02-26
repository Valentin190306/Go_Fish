package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IGo_Fish {
    void start() throws RemoteException;

    void playTurn(Value valueRequested, Player targetPlayer) throws RemoteException;

    boolean checkGameIsOver() throws RemoteException;

    IPlayer addPlayer() throws RemoteException;

    void removePlayer(IPlayer clientPlayer) throws RemoteException;

    void disconnectPlayer(IObservadorRemoto controller, Player player) throws RemoteException;

    void configPlayerName(Player remotePlayer, String name) throws RemoteException;

    IPlayer getPlayer(Player remotePlayer) throws RemoteException;

    void setPlayerReady(Player remotePlayer) throws RemoteException;

    IDeck getDeck() throws RemoteException;

    IPlayer getTargetPlayer() throws RemoteException;

    IPlayer getPlayerCalled(String name) throws RemoteException;

    ArrayList<IPlayer> getPlayers() throws RemoteException;

    IPlayer getCurrentPlayerPlayingTurn() throws RemoteException;

    GameState getGameState() throws RemoteException;

    void reload() throws RemoteException;
}
