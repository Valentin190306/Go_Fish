package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.Rank;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IGame extends IObservableRemoto {
    void dealInitialCards() throws RemoteException;
    void playTurn(Rank rankRequested, Player targetPlayer) throws RemoteException;
    boolean isGameOver() throws RemoteException;
    IPlayer getCurrentPlayer() throws RemoteException;
    IPlayer getTargetPlayer() throws RemoteException;
    boolean existsPlayerInGame(Player player) throws RemoteException;
    IPlayer getPlayerByName(String name) throws RemoteException;
    List<IPlayer> getPlayers() throws RemoteException;
    GameState getGameState();
    IDeck getDeck() throws RemoteException;
}
