package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.interfaces.IMatch;
import ar.edu.unlu.poo.interfaces.IPlayer;

import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;

public class Match extends ObservableRemoto implements IMatch {
    private static Match instance = null;

    private Deck deck;
    //private int deckNumber = 1;
    private List<Player> players;
    private int currentPlayerIndex;
    private GameState gameState;
    private Player targetPlayer;

    public Match() throws RemoteException {
        this.deck = new Deck.Builder().build();
        this.gameState = GameState.FILLING_LOBBY;
        this.players = new ArrayList<>();
    }

    public static Match getInstance() throws RemoteException {
        if (instance == null) {
            instance = new Match();
        }
        return instance;
    }

    @Override
    public void start() throws RemoteException {
        if (gameState == GameState.READY) {
            this.gameState = GameState.DEALING_CARDS;
            dealStartingCards();
            selectFirstPlayer();
        }
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

    private void UpdateGameStatus() throws RemoteException {
        if (players.size() == 4) gameNotifyObservers(GameState.READY);
    }

    @Override
    public IPlayer addPlayer() throws RemoteException {
        if (gameState != GameState.READY) {
            Player player = new Player();
            players.add(player);
            UpdateGameStatus();
            return player;
        } else return null;
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
    /*
    @Override
    public int getDeckNumber() {
        return deckNumber;
    }

    @Override
    public void setDeckNumber(int deckNumber) {
        this.deckNumber = deckNumber;
    }
    */
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

    @Override
    public void reload() throws Exception {
        if (gameState == GameState.GAME_OVER) {
            this.deck = new Deck.Builder().build();
            this.gameState = GameState.FILLING_LOBBY;
            this.players = new ArrayList<>();
        } else throw new Exception();
    }
}