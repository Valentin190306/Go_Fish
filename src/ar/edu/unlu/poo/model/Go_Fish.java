package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;

public class Go_Fish extends ObservableRemoto implements IGo_Fish {
    private static Go_Fish instance;
    private Deck deck;
    private final PlayerManager playerManager;

    private GameState gameState;
    private Value queriedValue;

    private static final int PLAYER_CAP = 3;
    private static final int SCORE_CAP = 3;

    private Go_Fish() {
        this.deck = new Deck.Builder().build();
        this.playerManager = new PlayerManager();
        this.gameState = GameState.AWAITING_PLAYERS;
        this.queriedValue = null;
    }

    public static Go_Fish getInstance() {
        if (instance == null) {
            instance = new Go_Fish();
        }
        return instance;
    }

    @Override
    public IPlayer connectPlayer() throws RemoteException {
        IPlayer newPlayer = playerManager.addPlayer();
        gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
        return newPlayer;
    }

    @Override
    public void disconnectPlayer(IObservadorRemoto controller, Player player) throws RemoteException {
        if (controller == null) {
            throw new IllegalArgumentException("El observador no puede ser nulo.");
        }
        if (player == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }

        deck.addCardsBack(player.getAllCards());
        deck.shuffle();

        playerManager.removePlayer(player);
        removerObservador(controller);

        gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
    }

    @Override
    public void configPlayerName(Player remotePlayer, String name) throws RemoteException {
        if (remotePlayer == null) {
            throw new IllegalArgumentException("Referencia de jugador nula.");
        }
        playerManager.setPlayerName(remotePlayer, name);
    }

    @Override
    public IPlayer getPlayer(Player remotePlayer) throws RemoteException {
        if (remotePlayer == null) {
            throw new IllegalArgumentException("Referencia de jugador nula.");
        }
        return playerManager.getPlayer(remotePlayer);
    }

    @Override
    public IPlayer getPlayerCalled(String name) throws RemoteException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }
        return playerManager.getPlayerCalled(name);
    }

    @Override
    public ArrayList<IPlayer> getPlayers() throws RemoteException {
        return new ArrayList<>(playerManager.getPlayers());
    }

    @Override
    public void setPlayerReady(Player remotePlayer) throws RemoteException {
        if (remotePlayer == null) {
            throw new IllegalArgumentException("Referencia de jugador nula.");
        }
        playerManager.setPlayerReady(remotePlayer);
        gameNotifyObservers(GameState.NEW_STATUS_PLAYER);

        if (playerManager.areAllReady(PLAYER_CAP)) {
            start();
        }
    }

    @Override
    public void setPlayerWaiting(Player remotePlayer) throws RemoteException {
        if (remotePlayer == null) {
            throw new IllegalArgumentException("Referencia de jugador nula.");
        }
        playerManager.setPlayerWaiting(remotePlayer);

        if (playerManager.getCommonState().equals(PlayerState.WAITING)) {
            reload();
        }
    }

    public void start() throws RemoteException {
        dealStartingCards();
        playerManager.selectFirstPlayer();
        gameNotifyObservers(GameState.READY);
        this.gameState = GameState.WAITING_ACTION;
    }

    private void dealStartingCards() {
        List<Player> players = playerManager.getPlayers();
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida para repartir cartas.");
        }
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                Card drawn = deck.drawCard();
                player.receiveCard(drawn);
            }
            player.setPlayerState(PlayerState.PLAYING);
        }
    }

    @Override
    public void playTurn(Value valueRequested, Player remoteTarget) throws RemoteException {
        if (valueRequested == null) {
            throw new IllegalArgumentException("La carta solicitada no puede ser null.");
        }

        if (remoteTarget == null ) {
            throw new IllegalArgumentException("El jugador objetivo no puede ser null.");
        }

        Player currentPlayer = playerManager.getCurrent();
        if (currentPlayer == null || currentPlayer.getPlayerState() != PlayerState.PLAYING) {
            throw new IllegalStateException("No es el turno de un jugador válido o el jugador no puede jugar.");
        }

        Player localTarget = playerManager.lookup(remoteTarget);
        if (localTarget == null || localTarget.getPlayerState() != PlayerState.PLAYING) {
            throw new IllegalArgumentException("El jugador objetivo no está disponible para jugar.");
        }

        if (!playerManager.getCurrent().hasCardOfValue(valueRequested)) {
            throw new IllegalArgumentException("No puedes pedir cartas que no tienes en tu mano.");
        }

        queriedValue = valueRequested;
        playerManager.setTargetPlayer(localTarget);

        if (localTarget.hasCardOfValue(valueRequested)) {
            transferringCardsToPlayer(valueRequested, localTarget);
        } else {
            playerWentFishing();
            playerManager.nextPlayer();
            gameNotifyObservers(GameState.TURN_SWITCH);
        }

        ensurePlayerHasCards(playerManager.getCurrent());
        //playerManager.nextPlayer();
        //gameNotifyObservers(GameState.TURN_SWITCH);
        playerGotSets();
        checkGameIsOver();
    }

    private void transferringCardsToPlayer(Value value, Player fromPlayer) throws RemoteException {
        Player currentPlayer = playerManager.getCurrent();
        List<Card> cardsTransferred = fromPlayer.giveCardsByValue(value);
        currentPlayer.receiveCards(cardsTransferred);
        gameNotifyObservers(GameState.TRANSFERRING_CARDS);
    }

    private void playerWentFishing() throws RemoteException {
        Player currentPlayer = playerManager.getCurrent();
        currentPlayer.fishing(deck.drawCard());
        gameNotifyObservers(GameState.GO_FISH);
    }

    private void playerGotSets() throws RemoteException {
        Player currentPlayer = playerManager.getCurrent();
        if (currentPlayer.hasCompletedSets()) {
            gameNotifyObservers(GameState.PLAYER_COMPLETED_SET);
        }
    }

    private void ensurePlayerHasCards(Player player) {
        if (player.getHandSize() == 0 && !deck.isEmpty()) {
            player.receiveCard(deck.drawCard());
        }
    }

    private void checkGameIsOver() throws RemoteException {
        if (deck.isEmpty()) {
            gameNotifyObservers(GameState.GAME_OVER);
        }

        boolean score_capped = false;
        List<Player> players = playerManager.getPlayers();
        for (Player player : players ) {
           if (player.getScore() == SCORE_CAP) {
               score_capped = true;
               break;
           }
        }

        if (score_capped) {
            updateScoreList(getGameScores());
            gameNotifyObservers(GameState.GAME_OVER);
        }
    }

    @Override
    public IDeck getDeck() throws RemoteException {
        return deck;
    }

    @Override
    public IPlayer getTargetPlayer() throws RemoteException {
        Player target = playerManager.getTargetPlayer();
        if (target == null) {
            throw new IllegalStateException("No se ha seleccionado un jugador objetivo todavía.");
        }
        return target;
    }

    @Override
    public Value getQueriedValue() throws RemoteException {
        if (queriedValue == null) {
            throw new IllegalStateException("No se ha consultado ninguna carta todavía.");
        }
        return queriedValue;
    }

    @Override
    public IPlayer getCurrentPlayerInTurn() throws RemoteException {
        return playerManager.getCurrent();
    }

    @Override
    public HashMap<String, Integer> getHighScoreList() throws RemoteException {
        return ScoreSerializer.getHighScores();
    }

    @Override
    public HashMap<String, Integer> getGameScores() throws RemoteException {
        HashMap<String, Integer> scores = new HashMap<>();

        for (Player player : playerManager.getPlayers()) {
            scores.put(player.getName(), player.getScore());
        }

        return scores;
    }

    private void updateScoreList(HashMap<String, Integer> scores) throws RemoteException {
        ScoreSerializer.updateHighScores(scores);
    }

    @Override
    public GameState getGameState() throws RemoteException {
        return gameState;
    }

    @Override
    public void reload() throws RemoteException {
        deck = new Deck.Builder().build();
        gameState = GameState.AWAITING_PLAYERS;
        queriedValue = null;
        playerManager.reset();
    }

    private void gameNotifyObservers(GameState newState) throws RemoteException {
        if (newState == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo.");
        }
        this.gameState = newState;
        super.notificarObservadores(newState);
    }
}