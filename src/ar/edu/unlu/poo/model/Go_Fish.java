package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.poo.model.enums.Value;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Go_Fish implements IGo_Fish, Serializable {
    private final GameManager gameManager;
    private final Deck deck;
    private final ArrayList<Player> players;
    private int currentPlayerIndex;
    private GameState gameState;
    private Player targetPlayer;

    public Go_Fish(GameManager gameManager, ArrayList<Player> players) {
        this.gameManager = gameManager;
        this.deck = new Deck.Builder().build();
        this.players = players;
        this.gameState = GameState.AWAITING_PLAYERS;
    }

    @Override
    public void start() throws RemoteException {
        if (gameState != GameState.READY) return;
        dealStartingCards();
        selectFirstPlayer();
        this.gameState = GameState.WAITING_ACTION;
    }

    private void dealStartingCards() throws RemoteException {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida para repartir cartas.");
        }
        int handSize = 7;
        if (players.size() >= 4) {
            handSize = 7 - (players.size() - 4);
        }
        if (handSize <= 0) {
            throw new IllegalStateException("El tamaño de la mano es inválido: " + handSize);
        }
        for (Player player : players) {
            for (int i = 0; i < handSize; i++) {
                Card drawn = deck.drawCard();
                if (drawn == null) {
                    throw new IllegalStateException("El mazo se quedó sin cartas durante la repartición.");
                }
                player.getHand().addCard(drawn);
            }
            player.setPlayerState(PlayerState.PLAYING);
        }
    }

    private void selectFirstPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida para seleccionar el primer turno.");
        }
        this.currentPlayerIndex = (int) (Math.random() * players.size());
    }

    @Override
    public void playTurn(Value valueRequested, Player targetPlayer) throws RemoteException {
        if (valueRequested == null) {
            throw new IllegalArgumentException("El valor solicitado no puede ser null.");
        }
        if (targetPlayer == null) {
            throw new IllegalArgumentException("El jugador objetivo no puede ser null.");
        }
        Player localTargetPlayer = gameManager.playerLookUp(targetPlayer);

        if (localTargetPlayer == null) {
            throw new IllegalArgumentException("El jugador objetivo no pertenece a la partida.");
        }
        if (localTargetPlayer.getHand().hasCardOfValue(valueRequested)) {
            this.targetPlayer = localTargetPlayer;
            transferringCardsToPlayer(valueRequested, localTargetPlayer);
        } else {
            playerWentFishing();
        }
        nextPlayer();
        playerGotSets();
    }

    private void transferringCardsToPlayer(Value value, Player player) throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        List<Card> cardsTransferred = player.getHand().removeCardsByValue(value);
        currentPlayer.getHand().addCards(cardsTransferred);
        matchNotifyObservers(GameState.TRANSFERRING_CARDS);
    }

    private void playerWentFishing() throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        Card lastDrawnCard = deck.drawCard();

        if (lastDrawnCard != null) {
            currentPlayer.getHand().addCard(lastDrawnCard);
            matchNotifyObservers(GameState.GO_FISH);
        }
        checkGameIsOver();
    }

    private void playerGotSets() throws RemoteException {
        if (players.get(currentPlayerIndex).getHand().checkForSets()) {
            matchNotifyObservers(GameState.PLAYER_COMPLETED_SET);
        }
    }

    private void nextPlayer() throws RemoteException {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores para pasar el turno.");
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        if (!checkGameIsOver()) {
            matchNotifyObservers(GameState.TURN_SWITCH);
        }
    }

    @Override
    public boolean checkGameIsOver() throws RemoteException {
        boolean isOver = deck.isEmpty();

        if (deck.isEmpty()) {
            matchNotifyObservers(GameState.GAME_OVER);
        }
        return isOver;
    }

    @Override
    public void checkRemovedPlayerIndex(IPlayer clientPlayer) throws RemoteException {
        int removedIndex = players.indexOf((Player) clientPlayer);
        if (removedIndex < 0) {
            throw new IllegalArgumentException("No existe el jugador a remover.");
        }
        deck.addCardsBack(clientPlayer.getHand().getCards());
        deck.shuffle();
        players.remove(removedIndex);

        if (removedIndex <= currentPlayerIndex && currentPlayerIndex > 0) {
            currentPlayerIndex--;
        }
        if (gameState.ordinal() < GameState.READY.ordinal()) {
            matchNotifyObservers(GameState.NEW_STATUS_PLAYER);
        }
    }

    @Override
    public IDeck getDeck() throws RemoteException{
        return deck;
    }

    @Override
    public IPlayer getTargetPlayer() throws RemoteException{
        return targetPlayer;
    }

    @Override
    public IPlayer getCurrentPlayerPlayingTurn() throws RemoteException {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida.");
        }
        return players.get(currentPlayerIndex);
    }

    @Override
    public GameState getGameState() throws RemoteException {
        return gameState;
    }

    private void matchNotifyObservers(GameState gameState) throws RemoteException {
        this.gameState = gameState;
        gameManager.gameNotifyObservers(gameState);
    }
}