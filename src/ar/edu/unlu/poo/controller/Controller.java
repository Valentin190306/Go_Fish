package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public class Controller implements IController, IControladorRemoto {
    private IGo_Fish model = null;
    private IGameView view = null;
    private IGameWindow gameWindow = null;
    private IPlayer clientPlayer;

    public Controller() throws RemoteException {}

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T model) throws RemoteException {
        this.model = (IGo_Fish) model;
    }

    @Override
    public void setView(IGameView view) throws IllegalArgumentException {
        if (view == null) {
            throw new IllegalArgumentException("La vista no puede ser nula");
        }
        this.view = view;
    }

    @Override
    public void setGameWindow(IGameWindow gameWindow) throws RemoteException{
        this.gameWindow = gameWindow;
    }

    @Override
    public void connect() throws RemoteException {
        this.clientPlayer = model.connectPlayer();
    }

    @Override
    public void disconnect() throws RemoteException {
        model.disconnectPlayer(this, (Player) clientPlayer);
    }

    @Override
    public HashMap<String, Integer> getScores() throws RemoteException {
        return model.getScoreList();
    }

    @Override
    public IPlayer getClientPlayer() {
        return clientPlayer;
    }

    @Override
    public void changeClientPlayerName(String name) throws RemoteException{
        model.configPlayerName((Player) clientPlayer, name);
    }

    @Override
    public void setClientPlayerReady() throws RemoteException {
        model.setPlayerReady((Player) clientPlayer);
    }

    @Override
    public List<IPlayer> getPlayerList() throws RemoteException {
        return model.getPlayers();
    }

    @Override
    public boolean handlePlayerInput(String input) throws RemoteException{
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("La entrada del jugador no puede estar vacía");
        }
        if (model.getGameState() == GameState.GAME_OVER) {
            handlePlayerExit(input);
        }
        boolean isValid = false;
        String[] parts = input.split(" ");
        if (parts.length == 2) {
            try {
                Value valueRequested = parseValue(parts[0]);
                IPlayer targetPlayer = model.getPlayerCalled(parts[1]);

                if (targetPlayer != null
                        && targetPlayer != clientPlayer
                        && model.getPlayer((Player) clientPlayer).getHand().hasCardOfValue(valueRequested)) {
                    model.playTurn(valueRequested, (Player) targetPlayer);
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

    @Override
    public boolean handlePlayerExit(String input) throws RemoteException{
        boolean isValid = false;
        if (input.equals("exit")) {
            try {
                model.setPlayerWaiting((Player) clientPlayer);
            } catch (Exception e) {
                view.handleException(e);
            }
        } else {
            throw new IllegalArgumentException("Entrada inválida, -exit- para volver al menu principal.");
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
        try {
            view.notifyAmountOfSets(model.getPlayer((Player) clientPlayer)
                    .getHand()
                    .getScore());
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void clientPlayerReceiveCards() {
        try {
            if (model.getCurrentPlayerInTurn().equals(clientPlayer)) {
                view.notifyReceivedCards(model.getPlayer((Player) clientPlayer)
                        .getHand()
                        .getTransferenceCards());
            } else if (model.getTargetPlayer().equals(clientPlayer)) {
                view.notifyLostCards(model.getTargetPlayer().getHand()
                        .getTransferenceCards());
            }
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void playerGoneFishing() {
        try {
            if (model.getCurrentPlayerInTurn().equals(clientPlayer)) {
                view.notifyClientPlayerGoneFishing();
                view.notifyFishedCard(model.getPlayer((Player) clientPlayer)
                        .getHand()
                        .getTransferenceCards()
                        .get(0));
            } else {
                view.notifyPlayerGoneFishing(model.getCurrentPlayerInTurn());
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
            boolean clientIsCurrentPlayer = model
                    .getCurrentPlayerInTurn()
                    .equals(clientPlayer);
            view.setPlayerTurn(clientIsCurrentPlayer);
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void showPlayerHand() {
        try {
            clientPlayer = model.getPlayer((Player) clientPlayer);
            view.updateHand(clientPlayer.getHand());
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    private void notifyTurnSwitch() throws RemoteException {
        try {
            view.notifyTurnSwitch(model.getCurrentPlayerInTurn());
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

    private void notifyPlayerAction() {
        try {
            if (model.getTargetPlayer() != null) {
                boolean clientIsCurrentPlayer = model
                        .getCurrentPlayerInTurn()
                        .equals(clientPlayer);
                view.notifyPlayerAction(model.getTargetPlayer(),
                        model.getCurrentPlayerInTurn(), model.getQueriedCard(),
                        clientIsCurrentPlayer);
            }
        } catch (Exception e) {
            view.handleException(e);
        }
    }

    @Override
    public void actualizar(IObservableRemoto model, Object event) {
        if (event instanceof GameState gameState) {
            try {
                switch (gameState) {
                    case NEW_STATUS_PLAYER -> gameWindow.getLobbyCard().updatePlayerList(this.model.getPlayers());
                    case READY -> {
                        view.start();
                        this.model.start();
                        view.notifyGameIntroduction(this.model.getPlayer((Player) clientPlayer));
                        showPlayersAndCards();
                        showPlayerHand();
                        handlePlayerTurn();
                    }
                    case TURN_SWITCH -> {
                        notifyTurnSwitch();
                        showPlayersAndCards();
                        showPlayerHand();
                        handlePlayerTurn();
                    }
                    case GAME_OVER -> {
                        handleGameOver();
                        view.spawnExitOption();
                    }
                    case GO_FISH -> {
                        notifyPlayerAction();
                        playerGoneFishing();
                    }
                    case TRANSFERRING_CARDS -> {
                        notifyPlayerAction();
                        clientPlayerReceiveCards();
                    }
                    case PLAYER_COMPLETED_SET -> {
                        clientPlayerSetsInHand();
                    }
                    default -> throw new IllegalArgumentException("Estado de juego desconocido");
                }
            } catch (RemoteException re) {
                view.handleException(re);
            }
        }
    }
}