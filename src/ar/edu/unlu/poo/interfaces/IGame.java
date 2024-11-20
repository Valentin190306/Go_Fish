package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.Rank;
import ar.edu.unlu.poo.observer.Observer;

import java.util.List;

public interface IGame {
    void dealInitialCards();
    void playTurn(Rank rankRequested, Player targetPlayer);
    boolean isGameOver();
    IPlayer getCurrentPlayer();
    IPlayer getTargetPlayer();
    boolean existsPlayerInGame(Player player);
    IPlayer getPlayerByName(String name);
    List<IPlayer> getPlayers();
    GameState getGameState();
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    IDeck getDeck();
}
