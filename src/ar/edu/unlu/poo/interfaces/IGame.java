package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.Value;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IGame extends IObservableRemoto {
    void dealStartingCards() throws RemoteException;
    void playTurn(Value valueRequested, Player targetPlayer) throws RemoteException;
    boolean isGameOver() throws RemoteException;
    IPlayer getCurrentPlayerPlayingTurn() throws RemoteException;
    IPlayer getTargetPlayer() throws RemoteException;
    boolean existsPlayerInGame(Player player) throws RemoteException;
    IPlayer getPlayerByName(String name) throws RemoteException;
    List<IPlayer> getPlayers() throws RemoteException;
    GameState getGameState();
    IDeck getDeck() throws RemoteException;
}
