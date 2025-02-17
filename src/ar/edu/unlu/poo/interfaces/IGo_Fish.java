package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;

import java.rmi.RemoteException;
import java.util.List;

public interface IGo_Fish {
    void start() throws RemoteException;

    void playTurn(Value valueRequested, IPlayer targetPlayer) throws RemoteException;

    boolean checkGameIsOver() throws RemoteException;

    void removePlayer(int ID) throws RemoteException;

    IPlayer getPlayerByID(int ID) throws RemoteException;

    IDeck getDeck();

    IPlayer getTargetPlayer();

    Player getPlayerCalled(String name) throws RemoteException;

    List<IPlayer> getPlayers();

    IPlayer getCurrentPlayerPlayingTurn();

    GameState getGameState() throws RemoteException;
}
