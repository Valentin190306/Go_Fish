package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.interfaces.IGame;
import ar.edu.unlu.poo.interfaces.IPlayer;

import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;

public class Game extends ObservableRemoto implements IGame {
    private final Deck deck;
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex;
    private GameState gameState;
    private Player targetPlayer;

    public Game() {
        this.gameState = GameState.FILLING_LOBBY;
        this.deck = new Deck.Builder().build();
        //this.players = players;
        //this.currentPlayerIndex = (int) (Math.random() * players.size());
    }

    @Override
    public void dealStartingCards() throws RemoteException {
        int initialHandSize = 7;
        for (Player player : players) {
            for (int i = 0; i < initialHandSize; i++) {
                player.getHand().addCard(deck.drawCard());
            }
        }
        gameNotifysObservers(GameState.DEALING_CARDS);
    }

    @Override
    public void playTurn(Value valueRequested, Player targetPlayer) throws RemoteException {
        if (targetPlayer.getHand().hasCardOfValue(valueRequested)) {
            this.targetPlayer = targetPlayer;
            transferringCardsToPlayer(valueRequested, targetPlayer);
        } else {
            playerWentFishing();
        }
        nextPlayer();
    }

    private void transferringCardsToPlayer(Value value, Player player) throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.getHand().addCards(player.getHand().removeCardsByValue(value));
        gameNotifysObservers(GameState.TRANSFERRING_CARDS);

        if (player.getHand().checkForSets()) {
            gameNotifysObservers(GameState.PLAYER_COMPLETED_SET);
        }
    }

    private void playerWentFishing() throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        Card lastDrawnCard = deck.drawCard();
        if (lastDrawnCard != null) {
            gameNotifysObservers(GameState.GO_FISH);
            currentPlayer.getHand().addCard(lastDrawnCard);

            if (currentPlayer.getHand().checkForSets()) {
                gameNotifysObservers(GameState.PLAYER_COMPLETED_SET);
            }
        } else {
            gameNotifysObservers(GameState.GAME_OVER);
        }
    }

    private void nextPlayer() throws RemoteException {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        gameNotifysObservers(GameState.TURN_SWITCH);
    }

    @Override
    public IDeck getDeck() {
        return deck;
    }

    @Override
    public boolean isGameOver() throws RemoteException {
        boolean isOver = deck.isEmpty();
        if (isOver) {
            gameNotifysObservers(GameState.GAME_OVER);
        }
        return isOver;
    }

    @Override
    public boolean existsPlayerInGame(IPlayer player) {
        return players.contains(player);
    }

    @Override
    public IPlayer getTargetPlayer() {
        return targetPlayer;
    }

    @Override
    public IPlayer getPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) return player;
        }
        return null;
    }

    @Override
    public List<IPlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public IPlayer getCurrentPlayerPlayingTurn() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public GameState getGameState() { return gameState; }

    public void gameNotifysObservers(GameState gameState) throws RemoteException {
        this.gameState = gameState;
        super.notificarObservadores(gameState);
    }
}