package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.ICard;
import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.poo.model.enums.Suit;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

public class Go_Fish extends ObservableRemoto implements IGo_Fish {
    private static Go_Fish instance = null;
    private Deck deck;
    private final ArrayList<Player> players;
    private int currentPlayerIndex;
    private GameState gameState;
    private Player currentPlayer;
    private Player targetPlayer = null;
    private Card queriedCard = null;

    public static IGo_Fish getInstance() {
        if (instance == null) {
            instance = new Go_Fish();
        }
        return instance;
    }

    private Go_Fish() {
        this.deck = new Deck.Builder().build();
        this.players = new ArrayList<>();
        this.gameState = GameState.AWAITING_PLAYERS;
    }

    @Override
    public void setFilePath(String filePath) throws RemoteException {
        ScoreSerializer.filePath = filePath;
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
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                Card drawn = deck.drawCard();
                player.getHand().addCard(drawn);
            }
            player.setPlayerState(PlayerState.PLAYING);
        }
    }

    private void selectFirstPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida para seleccionar el primer turno.");
        }
        currentPlayerIndex = (int) (Math.random() * players.size());
        currentPlayer = players.get(currentPlayerIndex);
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
            queriedCard = new Card(valueRequested, Suit.SPADES);
            transferringCardsToPlayer(valueRequested, localTargetPlayer);
        } else {
            playerWentFishing();
        }
        checkGameIsOver();
        playerGotSets();
        nextPlayer();
    }

    private void transferringCardsToPlayer(Value value, Player player) throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        List<Card> cardsTransferred = player.getHand().removeCardsByValue(value);
        currentPlayer.getHand().addCards(cardsTransferred);
        gameNotifyObservers(GameState.TRANSFERRING_CARDS);
    }

    private void playerWentFishing() throws RemoteException {
        Player currentPlayer = players.get(currentPlayerIndex);
        Card lastDrawnCard = deck.drawCard();

        if (lastDrawnCard != null) {
            currentPlayer.getHand().addCard(lastDrawnCard);
            gameNotifyObservers(GameState.GO_FISH);
        }
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
        currentPlayer = players.get(currentPlayerIndex);
        gameNotifyObservers(GameState.TURN_SWITCH);
    }

    private void updateScoreData() throws RemoteException {
        HashMap<String, Integer> scores = new HashMap<>();
        for (Player player : players) {
            scores.put(player.getName(), player.getHand().getScore());
        }
        try {
            ScoreSerializer.updateHighScores(scores);
        } catch (IOException e) {
            throw new RemoteException("Error al actualizar el registro de puntajes.");
        }
    }

    private void checkGameIsOver() throws RemoteException {
        if (deck.isEmpty() || players.size() <= 1) {
            updateScoreData();
            reload();
            gameNotifyObservers(GameState.GAME_OVER);
        }
    }

    private Player playerLookUp(Player player) {
        for (Player searchedPlayer : players) {
            if (searchedPlayer.equals(player)) {
                return searchedPlayer;
            }
        }
        return null;
    }

    private void arePlayersReadyCheck() throws RemoteException {
        boolean areReady = true;

        for (Player player : players) {
            if (!player
                    .getPlayerState()
                    .equals(PlayerState.READY)) {
                areReady = false;
                break;
            }
        }

        if (areReady && players.size() == 2) {              // Recordar preestablecerlo a 3 o 4
            gameNotifyObservers(GameState.READY);
        }
    }

    @Override
    public void removePlayer(IPlayer clientPlayer) throws RemoteException {
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
            gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
        }
        if (players.isEmpty()) {
            reload();
        }
    }

    @Override
    public IPlayer connectPlayer() throws RemoteException {
        if (gameState.ordinal() >= GameState.READY.ordinal()) {
            throw new IllegalStateException("Una partida ya ha comenzado.");
        }
        if (players.size() == 4) {
            throw new IllegalStateException("Máxima cantidad de jugadores conectados.");
        }
        Player newPlayer = new Player();
        players.add(newPlayer);
        gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
        return newPlayer;
    }

    @Override
    public void disconnectPlayer(IObservadorRemoto controller, Player player) throws RemoteException {
        removePlayer(player);
        removerObservador(controller);
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
    public void setPlayerReady(Player remotePlayer) throws RemoteException {
        Player player = playerLookUp(remotePlayer);
        if (player == null) {
            throw new IllegalArgumentException("Jugador inexistente o no registrado en el modelo.");
        }
        if (player.getPlayerState() != PlayerState.WAITING) {
            throw new RemoteException("Transición de estado inválida.");
        }
        player.setPlayerState(PlayerState.READY);
        arePlayersReadyCheck();
        gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
    }

    @Override
    public void setPlayerWaiting(Player remotePlayer) throws RemoteException {
        Player player = playerLookUp(remotePlayer);
        if (player == null) {
            throw new IllegalArgumentException("Jugador inexistente o no registrado en el modelo.");
        }
        if (player.getPlayerState() != PlayerState.PLAYING) {
            throw new RemoteException("Transición de estado inválida.");
        }
        player.setPlayerState(PlayerState.WAITING);
    }

    @Override
    public IDeck getDeck() throws RemoteException {
        return deck;
    }

    @Override
    public IPlayer getTargetPlayer() throws RemoteException {
        return targetPlayer;
    }

    @Override
    public ICard getQueriedCard() throws RemoteException {
        return queriedCard;
    }

    @Override
    public ArrayList<IPlayer> getPlayers() throws RemoteException {
        return new ArrayList<>(players);
    }

    @Override
    public IPlayer getCurrentPlayerInTurn() throws RemoteException {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida.");
        }
        return currentPlayer;
    }

    @Override
    public HashMap<String, Integer> getScoreList() throws RemoteException {
        try {
            return ScoreSerializer.sortScores(ScoreSerializer.deserialize());
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public GameState getGameState() throws RemoteException {
        return gameState;
    }

    @Override
    public void reload() throws RemoteException {
        deck = new Deck.Builder().build();
        gameState = GameState.AWAITING_PLAYERS;
        currentPlayer = null;
        targetPlayer = null;
        queriedCard = null;

        for (Player player : players) {
            player.getHand().clear();
        }
    }

    private void gameNotifyObservers(GameState gameState) throws RemoteException {
        this.gameState = gameState;
        super.notificarObservadores(gameState);
    }
}