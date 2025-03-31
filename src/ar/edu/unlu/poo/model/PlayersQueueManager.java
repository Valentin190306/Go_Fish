package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

@Deprecated
public class PlayersQueueManager extends ObservableRemoto {
    private ArrayList<Player> players;
    private int currentPlayerIndex;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void removePlayer(IPlayer clientPlayer) throws RemoteException {
        int removedIndex = players.indexOf((Player) clientPlayer);
        if (removedIndex < 0) {
            throw new IllegalArgumentException("No existe el jugador a remover.");
        }
        players.remove(removedIndex);

        if (removedIndex <= currentPlayerIndex && currentPlayerIndex > 0) {
            currentPlayerIndex--;
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

    private GameState arePlayersReadyCheck() throws RemoteException {
        boolean areReady = true;

        for (Player player : players) {
            if (!player
                    .getPlayerState()
                    .equals(PlayerState.READY)) {
                areReady = false;
                break;
            }
        }

        if (areReady && players.size() == 4) {              // Recordar preestablecerlo a 3 o 4
            return GameState.READY;
        }
        return null;
    }

    public void configPlayerName(Player remotePlayer, String name) throws RemoteException {
        Player player = playerLookUp(remotePlayer);
        if (player == null) {
            throw new IllegalArgumentException("Jugador inexistente o no registrado en el modelo.");
        }
        player.setName(name);
    }

    public IPlayer getPlayer(Player remotePlayer) throws RemoteException {
        Player player = playerLookUp(remotePlayer);
        if (player == null) {
            throw new IllegalArgumentException("Jugador inexistente o no registrado en el modelo.");
        }
        return player;
    }

    public IPlayer getPlayerCalled(String name) throws RemoteException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }

        for (Player player : players) {
            if (player.getName().equals(name)) return player;
        }
        throw new IllegalArgumentException("No se encontró un jugador con el nombre: " + name);
    }

    public GameState setPlayerReady(Player remotePlayer) throws RemoteException {
        Player player = playerLookUp(remotePlayer);
        if (player == null) {
            throw new IllegalArgumentException("Jugador inexistente o no registrado en el modelo.");
        }
        if (player.getPlayerState() != PlayerState.WAITING) {
            throw new RemoteException("Transición de estado inválida.");
        }
        player.setPlayerState(PlayerState.READY);
        arePlayersReadyCheck();
        return GameState.NEW_STATUS_PLAYER;
    }

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

}