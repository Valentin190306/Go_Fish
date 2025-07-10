package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IGo_Fish extends IObservableRemoto {

    void playTurn(Value queriedValue, Player targetPlayer) throws RemoteException;

    IPlayer connectPlayer() throws RemoteException;

    void disconnectPlayer(IObservadorRemoto controller, Player player) throws RemoteException;

    void configPlayerName(Player remotePlayer, String name) throws RemoteException;

    IPlayer getPlayer(Player remotePlayer) throws RemoteException;

    IPlayer getPlayerCalled(String name) throws RemoteException;

    void setPlayerReady(Player remotePlayer) throws RemoteException;

    void setPlayerWaiting(Player remotePlayer) throws RemoteException;

    IDeck getDeck() throws RemoteException;

    IPlayer getTargetPlayer() throws RemoteException;

    Value getQueriedValue() throws RemoteException;

    ArrayList<IPlayer> getPlayers() throws RemoteException;

    IPlayer getCurrentPlayerInTurn() throws RemoteException;

    HashMap<String, Integer> getHighScoreList() throws RemoteException;

    HashMap<String, Integer> getGameScores() throws RemoteException;

    GameState getGameState() throws RemoteException;

    void reload() throws RemoteException;
}
