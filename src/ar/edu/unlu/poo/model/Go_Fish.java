package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Go_Fish extends ObservableRemoto implements IGo_Fish, Serializable {
    private static Go_Fish instance = null;
    private Deck deck;
    private final ArrayList<Player> players;
    private int currentPlayerIndex;
    private GameState gameState;
    private Player targetPlayer;

    private Go_Fish() throws RemoteException {
        this.deck = new Deck.Builder().build();
        this.players = new ArrayList<>();
        this.gameState = GameState.AWAITING_PLAYERS;
    }

    public static IGo_Fish getInstance() throws RemoteException {
        if (Go_Fish.instance == null) {
            Go_Fish.instance = new Go_Fish();
        }
        return Go_Fish.instance;
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
        Player localTargetPlayer = playerLookUp(targetPlayer);

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
    }

    private void transferringCardsToPlayer(Value value, Player player) throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        List<Card> cardsTransferred = player.getHand().removeCardsByValue(value);
        currentPlayer.getHand().addCards(cardsTransferred);

        gameNotifyObservers(GameState.TRANSFERRING_CARDS);
        playerGotSets();
    }

    private void playerWentFishing() throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        Card lastDrawnCard = deck.drawCard();

        if (lastDrawnCard != null) {
            currentPlayer.getHand().addCard(lastDrawnCard);
            gameNotifyObservers(GameState.GO_FISH);
            playerGotSets();
        }
        checkGameIsOver();
    }

    private void playerGotSets() throws RemoteException {
        if (players.get(currentPlayerIndex).getHand().checkForSets()) {
            gameNotifyObservers(GameState.PLAYER_COMPLETED_SET);
        }
    }

    private void nextPlayer() throws RemoteException {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores para pasar el turno.");
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        if (!checkGameIsOver()) {
            gameNotifyObservers(GameState.TURN_SWITCH);
        }
    }

    @Override
    public boolean checkGameIsOver() throws RemoteException {
        boolean isOver = deck.isEmpty();

        if (deck.isEmpty()) {
            gameNotifyObservers(GameState.GAME_OVER);
        }
        return isOver;
    }

    private void arePlayersReadyCheck() throws RemoteException {
        boolean areReady = true;

        for (Player player : players) {
            if (!player.getPlayerState().equals(PlayerState.READY)) {
                areReady = false;
                break;
            }
        }
        if (areReady) gameNotifyObservers(GameState.READY);
    }

    @Override
    public IPlayer addPlayer() throws RemoteException {
        if (gameState.ordinal() >= GameState.READY.ordinal()) {
            throw new IllegalStateException("Una partida ya ha comenzado.");
        } if (players.size() == 7) {
            throw new IllegalStateException("Máxima cantidad de jugadores conectados.");
        }
        Player newPlayer = new Player();
        players.add(newPlayer);
        gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
        return newPlayer;
    }

    @Override
    public void removePlayer(IPlayer clientPlayer) throws RemoteException {
        int removedIndex = players.indexOf((Player) clientPlayer);
        if (removedIndex < 0) {
            throw new IllegalArgumentException("No existe el jugador a remover.");
        }
        players.remove(removedIndex);

        if (removedIndex <= currentPlayerIndex && currentPlayerIndex > 0) {
            currentPlayerIndex--;
        }
        if (gameState.ordinal() < GameState.READY.ordinal()) {
            gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
        }
    }

    @Override
    public void disconnectPlayer(IObservadorRemoto controller, Player player) throws RemoteException {
        removerObservador(controller);
        removePlayer(player);
    }

    private Player playerLookUp(Player player) {
        for (Player searchedPlayer : players) {
            if (searchedPlayer.equals(player)) {
                return searchedPlayer;
            }
        }
        return null;
    }

    @Override
    public void configPlayerName(Player remotePlayer, String name) throws RemoteException {
        Player player = playerLookUp(remotePlayer);
        if (player == null) {
            throw new IllegalArgumentException("Jugador inexistente o no registrado en el modelo.");
        }
        player.setName(name);
    }

    @Override
    public IPlayer getPlayer(Player remotePlayer) throws RemoteException {
        Player player = playerLookUp(remotePlayer);
        if (player == null) {
            throw new IllegalArgumentException("Jugador inexistente o no registrado en el modelo.");
        }
        return player;
    }

    @Override
    public void setPlayerReady(Player remotePlayer) throws RemoteException {
        Player player = playerLookUp(remotePlayer);
        if (player == null) {
            throw new IllegalArgumentException("Jugador inexistente o no registrado en el modelo.");
        }
        player.setPlayerState(PlayerState.READY);
        arePlayersReadyCheck();
        gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
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
    public IPlayer getPlayerCalled(String name) throws RemoteException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }

        for (Player player : players) {
            if (player.getName().equals(name)) return player;
        }
        throw new IllegalArgumentException("No se encontró un jugador con el nombre: " + name);
    }

    @Override
    public ArrayList<IPlayer> getPlayers() throws RemoteException {
        return new ArrayList<>(players);
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

    @Override
    public void reload() throws RemoteException {
        this.deck = new Deck.Builder().build();
        this.gameState = GameState.AWAITING_PLAYERS;
        for (Player player : players) {
            player.getHand().clear();
            player.setPlayerState(PlayerState.READY);
        }
    }

    private void gameNotifyObservers(GameState gameState) throws RemoteException {
        this.gameState = gameState;
        super.notificarObservadores(gameState);
    }
}