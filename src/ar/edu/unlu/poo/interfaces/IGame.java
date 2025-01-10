package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.Rank;
import ar.edu.unlu.poo.observer.Observer;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IGame extends IObservableRemoto {
    void dealInitialCards() throws RemoteException;
    void playTurn(Rank rankRequested, Player targetPlayer) throws RemoteException;
    boolean isGameOver() throws RemoteException;
    IPlayer getCurrentPlayer();
    IPlayer getTargetPlayer();
    boolean existsPlayerInGame(Player player);
    IPlayer getPlayerByName(String name);
    List<IPlayer> getPlayers();
    GameState getGameState();
    void addObserver(Observer observer) throws RemoteException;
    void removeObserver(Observer observer) throws RemoteException;
    IDeck getDeck();
}
