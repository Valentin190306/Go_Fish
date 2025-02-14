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

    public Game() throws RemoteException {
        this.deck = new Deck.Builder().build();
    }

    @Override
    public void start() throws RemoteException {
        selectFirstPlayer();
        dealStartingCards();
    }

    private void dealStartingCards() throws RemoteException {
        int initialHandSize = 7;
        for (Player player : players) {
            for (int i = 0; i < initialHandSize; i++) {
                player.getHand().addCard(deck.drawCard());
            }
        }
    }

    private void selectFirstPlayer() {
        this.currentPlayerIndex = (int) (Math.random() * players.size());
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
        gameNotifyObservers(GameState.TRANSFERRING_CARDS);

        if (player.getHand().checkForSets()) {
            gameNotifyObservers(GameState.PLAYER_COMPLETED_SET);
        }
    }

    private void playerWentFishing() throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        Card lastDrawnCard = deck.drawCard();
        if (lastDrawnCard != null) {
            gameNotifyObservers(GameState.GO_FISH);
            currentPlayer.getHand().addCard(lastDrawnCard);

            if (currentPlayer.getHand().checkForSets()) {
                gameNotifyObservers(GameState.PLAYER_COMPLETED_SET);
            }
        } else {
            gameNotifyObservers(GameState.GAME_OVER);
        }
    }

    private void nextPlayer() throws RemoteException {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if(!isGameOver()) gameNotifyObservers(GameState.TURN_SWITCH);
    }

    public boolean isGameOver() throws RemoteException {
        boolean isOver = deck.isEmpty();
        if (isOver) gameNotifyObservers(GameState.GAME_OVER);
        return isOver;
    }

    private void isGameReady() throws RemoteException {
        if (players.size() == 4) gameNotifyObservers(GameState.READY);
        else gameNotifyObservers(GameState.FILLING_LOBBY);
    }

    @Override
    public int addPlayer(String name) throws RemoteException {
        Player player = new Player(name);
        players.add(player);
        isGameReady();
        return player.getID();
    }

    @Override
    public int addPlayer(IPlayer player) throws RemoteException {
        if (player instanceof  Player) players.add((Player) player);
        isGameReady();
        return player.getID();
    }

    @Override
    public void removePlayer(int ID) throws RemoteException {
        players.remove((Player) getPlayerByID(ID));
        if (players.size() < 4) gameNotifyObservers(GameState.GAME_OVER);
    }

    @Override
    public IPlayer getPlayerByID(int ID) throws RemoteException {
        for (Player player : players)
            if (player.getID() == ID) return player;
        return null;
    }

    @Override
    public IDeck getDeck() {
        return deck;
    }

    @Override
    public IPlayer getTargetPlayer() {
        return targetPlayer;
    }

    @Override
    public Player getPlayerCalled(String name) throws RemoteException {
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
    public GameState getGameState() throws RemoteException { return gameState; }

    public void gameNotifyObservers(GameState gameState) throws RemoteException {
        this.gameState = gameState;
        super.notificarObservadores(gameState);
    }
}