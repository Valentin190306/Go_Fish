package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IGameManager;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameManager extends ObservableRemoto implements IGameManager {
    private static GameManager instance = null;
    private static String filePath = null;
    private IGo_Fish match = null;
    private GameState gameState = GameState.AWAITING_PLAYERS;
    private final ArrayList<Player> players;

    public static IGameManager getInstance() {
        if (GameManager.instance == null) {
            GameManager.instance = new GameManager();
        }
        return GameManager.instance;
    }

    private GameManager() {
        this.players = new ArrayList<>();
    }

    @Override
    public void createNewMatch() {
        match = new Go_Fish(this, players);
    }

    @Override
    public void saveMatch() throws IOException {
        serialize(match);
    }

    @Override
    public void restoreMatch() throws IOException, ClassNotFoundException {
        match = deserialize();
    }

    @Override
    public IPlayer connectPlayer() throws RemoteException {
        if (gameState.ordinal() >= GameState.READY.ordinal()) {
            throw new IllegalStateException("Una partida ya ha comenzado.");
        }
        if (players.size() == 7) {
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

        if (gameState.ordinal() < GameState.READY.ordinal()) {
            gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
        }
    }

    @Override
    public void disconnectPlayer(IObservadorRemoto controller, Player player) throws RemoteException {
        players.remove(player);
        match.removePlayer(player);
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
    public ArrayList<IPlayer> getPlayers() throws RemoteException {
        return new ArrayList<>(players);
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
        player.setPlayerState(PlayerState.READY);
        arePlayersReadyCheck();
        gameNotifyObservers(GameState.NEW_STATUS_PLAYER);
    }

    Player playerLookUp(Player player) {
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
        if (areReady) gameNotifyObservers(GameState.READY);
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        GameManager.filePath = filePath;
    }

    public static void serialize(IGo_Fish match) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(match);
        }
    }

    public static Go_Fish deserialize() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Go_Fish) ois.readObject();
        }
    }

    void gameNotifyObservers(GameState gameState) throws RemoteException {
        this.gameState = gameState;
        super.notificarObservadores(gameState);
    }
}
