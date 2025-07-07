package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.model.enums.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private Player targetPlayer;

    public Player addPlayer() {
        Player player = new Player();
        players.add(player);
        return player;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void setPlayerName(Player player, String name) {
        player.setName(name);
    }

    public Player getPlayer(Player player) {
        for (Player p : players) {
            if (p.equals(player)) {
                return p;
            }
        }
        return null;
    }

    public Player getPlayerCalled(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayerReady(Player player) {
        Player playerReady = getPlayer(player);
        playerReady.setPlayerState(PlayerState.READY);
    }

    public void setPlayerWaiting(Player player) {
        Player playerWaiting = getPlayer(player);
        playerWaiting.setPlayerState(PlayerState.WAITING);
    }

    public boolean areAllReady(int minPlayers) {
        if (players.size() < minPlayers) return false;
        for (Player p : players) {
            if (p.getPlayerState() != PlayerState.READY) {
                return false;
            }
        }
        return true;
    }

    public void selectFirstPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida.");
        }
        currentPlayerIndex = (int) (Math.random() * players.size());
    }

    public Player getCurrent() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }

    public void nextPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores para pasar el turno.");
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player lookup(Player remotePlayer) {
        return players.stream()
                .filter(p -> p.equals(remotePlayer))
                .findFirst()
                .orElse(null);
    }

    public void setTargetPlayer(Player target) {
        this.targetPlayer = target;
    }

    public PlayerState getCommonState() {
        if (players.isEmpty()) return null;

        PlayerState commonState = players.get(0).getPlayerState();

        for (int i = 1; i < players.size(); i++) {
            if (!players.get(i).getPlayerState().equals(commonState)) {
                return null;
            }
        }

        return commonState;
    }

    public Player getTargetPlayer() {
        return this.targetPlayer;
    }

    public void reset() {
        currentPlayerIndex = 0;
        targetPlayer = null;

        for (Player player : players) {
            player.clearHand();
        }
    }
}