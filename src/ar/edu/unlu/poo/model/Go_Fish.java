package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Go_Fish extends ObservableRemoto implements IGo_Fish {
    private static Go_Fish instance = null;
    private Deck deck;
    private final List<Player> players;
    private int currentPlayerIndex;
    private GameState gameState;
    private Player targetPlayer;

    private Go_Fish() throws RemoteException {
        this.deck = new Deck.Builder().build();
        this.players = new ArrayList<>();
        this.gameState = GameState.WAITING_PLAYERS;
    }

    public static IGo_Fish getInstance() throws RemoteException {
        if (instance == null) {
            instance = new Go_Fish();
        }
        return instance;
    }

    @Override
    public void start() throws RemoteException {
        if (gameState != GameState.READY) {
            throw new IllegalStateException("La partida no está lista para iniciar. Estado actual: " + gameState);
        }
        dealStartingCards();
        selectFirstPlayer();
        gameNotifyObservers(GameState.READY);
    }

    private void dealStartingCards() throws RemoteException {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida para repartir cartas.");
        }
        int handSize = 7 - (players.size() - 4);
        for (Player player : players) {
            for (int i = 0; i < handSize; i++) {
                Card drawn = deck.drawCard();
                if (drawn == null) {
                    throw new IllegalStateException("El mazo se quedó sin cartas durante la repartición.");
                }
                player.getHand().addCard(drawn);
            }
        }
    }

    private void selectFirstPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida para seleccionar el primer turno.");
        }
        this.currentPlayerIndex = (int) (Math.random() * players.size());
    }

    @Override
    public void playTurn(Value valueRequested, IPlayer targetPlayer) throws RemoteException {
        if (valueRequested == null) {
            throw new IllegalArgumentException("El valor solicitado no puede ser null.");
        } if (targetPlayer == null) {
            throw new IllegalArgumentException("El jugador objetivo no puede ser null.");
        } if (!players.contains((Player) targetPlayer)) {
            throw new IllegalArgumentException("El jugador objetivo no pertenece a la partida.");
        } if (targetPlayer.getHand().hasCardOfValue(valueRequested)) {
            this.targetPlayer = (Player) targetPlayer;
            transferringCardsToPlayer(valueRequested, (Player) targetPlayer);
        } else playerWentFishing();

        nextPlayer();
    }

    private void transferringCardsToPlayer(Value value, Player player) throws RemoteException {
        if (value == null || player == null) {
            throw new IllegalArgumentException("Valor o jugador nulo no permitido.");
        }
        Player currentPlayer = players.get(currentPlayerIndex);
        List<Card> cardsTransferred = player.getHand().removeCardsByValue(value);

        if (cardsTransferred.isEmpty()) {
            throw new IllegalStateException("No se encontraron cartas para transferir.");
        }
        currentPlayer.getHand().addCards(cardsTransferred);
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
        }
        checkGameIsOver();
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

    @Override
    public IPlayer addPlayer() throws RemoteException {
        if (gameState != GameState.WAITING_PLAYERS) {
            throw new IllegalStateException("Una partida ya ha comenzado.");
        } if (players.size() == 7) {
            throw new IllegalStateException("Máximo cantidad de jugadores conectados.");
        }
        Player newPlayer = new Player();
        players.add(newPlayer);
        return newPlayer;
    }

    @Override
    public void removePlayer(int ID) throws RemoteException {
        IPlayer playerToRemove = getPlayerByID(ID);

        if (playerToRemove == null) {
            throw new IllegalArgumentException("No existe un jugador con el ID: " + ID);
        }
        players.remove((Player) playerToRemove);

        if (players.size() < 4) {
            gameNotifyObservers(GameState.GAME_OVER);
        }
    }

    @Override
    public IPlayer getPlayerByID(int ID) throws RemoteException {
        for (Player player : players){
            if (player.getID() == ID) return player;
        }
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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }

        for (Player player : players) {
            if (player.getName().equals(name)) return player;
        }
        throw new IllegalArgumentException("No se encontró un jugador con el nombre: " + name);
    }

    @Override
    public List<IPlayer> getPlayers() {
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
    public void reload() {
        this.deck = new Deck.Builder().build();
        this.gameState = GameState.WAITING_PLAYERS;
        for (Player player : players) {
            player.setPlaying(false);
        }
    }

    private void gameNotifyObservers(GameState gameState) throws RemoteException {
        this.gameState = gameState;
        super.notificarObservadores(gameState);
    }
}