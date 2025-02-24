package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public class Controller implements IController, IControladorRemoto {
    private IGo_Fish model = null;
    private IGameView view = null;
    private ILobby lobby = null;
    private IPlayer clientPlayer = null;

    public Controller() throws RemoteException {}

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T model) throws RemoteException {
        this.model = (IGo_Fish) model;
    }

    @Override
    public void connect() throws RemoteException {
        this.clientPlayer = model.addPlayer();
    }

    @Override
    public void disconnectPlayer() throws RemoteException {
        model.removePlayer(clientPlayer);
    }

    @Override
    public void setView(IGameView view) throws IllegalArgumentException {
        if (view == null) {
            throw new IllegalArgumentException("La vista no puede ser nula");
        }
        this.view = view;
    }

    @Override
    public IPlayer getClientPlayer() {
        return clientPlayer;
    }

    @Override
    public void changeClientPlayerName(String name) throws RemoteException{
        this.clientPlayer = model.configPlayerName(clientPlayer, name);
    }

    @Override
    public void setClientPlayerReady() throws RemoteException {
        model.setPlayerReady((Player) clientPlayer);
    }

    @Override
    public void setLobby(ILobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public List<IPlayer> getPlayerList() throws RemoteException {
        return model.getPlayers();
    }

    @Override
    public void disconnect() throws RemoteException {
        model.disconnectPlayer(this, clientPlayer);
    }

    @Override
    public boolean handlePlayerInput(String input) throws IllegalArgumentException {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("La entrada del jugador no puede estar vacía");
        }
        boolean isValid = false;
        String[] parts = input.split(" ");
        if (parts.length == 2) {
            try {
                Value valueRequested = parseValue(parts[0]);
                IPlayer targetPlayer = model.getPlayerCalled(parts[1]);

                if (targetPlayer != null
                        && targetPlayer != clientPlayer
                        && clientPlayer.getHand().hasCardOfValue(valueRequested)) {
                    model.playTurn(valueRequested, targetPlayer);
                    isValid = true;
                } else {
                    throw new IllegalArgumentException("Jugador inexistente o movimiento inválido");
                }
            } catch (IllegalArgumentException e) {
                view.handleException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Formato inválido. Use: <RANGO> <NOMBRE_JUGADOR>");
        }
        return isValid;
    }

    private Value parseValue(String input) throws IllegalArgumentException {
        for (Value value : Value.values()) {
            if (value.getValue().equalsIgnoreCase(input)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Rango inválido. Use: <RANGO> <NOMBRE_JUGADOR>");
    }

    private void clientPlayerSetsInHand() {
        view.notifyAmountOfSets(clientPlayer.getHand().getScore());
    }

    private void clientPlayerReceiveCards() {
        try {
            if (model.getCurrentPlayerPlayingTurn() == clientPlayer) {
                view.notifyReceivedCards(clientPlayer.getHand().getTransferenceCards());
            } else if (model.getTargetPlayer() == clientPlayer) {
                view.notifyLostCards(model.getTargetPlayer().getHand().getTransferenceCards());
            }
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void playerGoneFishing() {
        try {
            if (model.getCurrentPlayerPlayingTurn() == clientPlayer) {
                view.notifyClientPlayerGoneFishing();
                view.notifyFishedCard(clientPlayer.getHand().getTransferenceCards().get(0));
            } else {
                view.notifyPlayerGoneFishing(model.getCurrentPlayerPlayingTurn());
            }
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void showPlayersAndCards() {
        try {
            view.showPlayersAndCards(model.getDeck(), model.getPlayers());
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void handlePlayerTurn() throws RemoteException {
        try {
            boolean isCurrentPlayer = model.getCurrentPlayerPlayingTurn().equals(clientPlayer);
            view.setPlayerTurn(isCurrentPlayer);
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void showPlayerHand() {
        try {
            view.updateHand(clientPlayer.getHand());
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void notifyTurnSwitch() throws RemoteException {
        try {
            view.notifyTurnSwitch(model.getCurrentPlayerPlayingTurn());
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void handleGameOver() throws RemoteException {
        try {
            view.notifyGameOver();
            view.updateScores(model.getPlayers());
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    @Override
    public void actualizar(IObservableRemoto model, Object event) {
        if (event instanceof GameState gameState) {
            try {
                switch (gameState) {
                    case NEW_STATUS_PLAYER -> lobby.updatePlayerList(this.model.getPlayers());
                    case READY -> {
                        lobby.switchToGameView();
                        view.start();
                        this.model.start();
                        view.notifyGameIntroduction(clientPlayer);
                        showPlayersAndCards();
                        showPlayerHand();
                        handlePlayerTurn();
                    }
                    case TURN_SWITCH -> {
                        notifyTurnSwitch();
                        showPlayersAndCards();
                        showPlayerHand();
                        handlePlayerTurn();
                        view.notifyPlayerAction(this.model.getTargetPlayer(), this.model.getCurrentPlayerPlayingTurn());
                    }
                    case GAME_OVER -> handleGameOver();
                    case GO_FISH -> playerGoneFishing();
                    case TRANSFERRING_CARDS -> clientPlayerReceiveCards();
                    case PLAYER_COMPLETED_SET -> clientPlayerSetsInHand();
                    default -> throw new IllegalArgumentException("Estado de juego desconocido");
                }
            } catch (RemoteException re) {
                view.handleException(re);
            }
        }
    }
}