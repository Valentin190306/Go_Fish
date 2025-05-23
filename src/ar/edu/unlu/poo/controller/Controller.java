package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements IController {
    private IGo_Fish model;
    private IGameView gameView;
    private IPlayer clientPlayer;

    @Override
    public IGameView getGameView() {
        return gameView;
    }

    @Override
    public void setGameView(IGameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void connect() throws RemoteException {
        clientPlayer = model.connectPlayer();
    }

    @Override
    public void disconnect() throws RemoteException {
        model.disconnectPlayer(this, (Player) clientPlayer);
    }

    @Override
    public IPlayer fetchClientPlayer() throws RemoteException {
        clientPlayer = model.getPlayer((Player) clientPlayer);
        return clientPlayer;
    }

    @Override
    public ArrayList<IPlayer> fetchPlayers() throws RemoteException {
        return model.getPlayers();
    }

    @Override
    public ICard fetchQueriedCard() throws RemoteException {
        return model.getQueriedCard();
    }

    @Override
    public IPlayer fetchPlayingPlayer() throws RemoteException {
        return model.getCurrentPlayerInTurn();
    }

    @Override
    public IPlayer fetchTargetPlayer() throws RemoteException {
        return model.getTargetPlayer();
    }

    @Override
    public IDeck fetchDeck() throws RemoteException {
        return model.getDeck();
    }

    @Override
    public HashMap<String, Integer> fetchScores() throws RemoteException {
        return model.getScoreList();
    }

    @Override
    public void setClientPlayerReady() throws RemoteException {
        model.setPlayerReady((Player) clientPlayer);
        clientPlayer = model.getPlayer((Player) clientPlayer);
    }

    @Override
    public void updateClientPlayerName(String name) throws RemoteException {
        model.configPlayerName((Player) clientPlayer, name);
        clientPlayer = model.getPlayer((Player) clientPlayer);
    }

    @Override
    public void handleTurnInput(Value requestedValue, IPlayer targetPlayer) throws RemoteException {
        model.playTurn(requestedValue, (Player) targetPlayer);
    }

    @Override
    public boolean handlePlayerInput(String input) throws RemoteException {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("La entrada del jugador no puede estar vacía");
        }
        if (model.getGameState() == GameState.GAME_OVER) {
            handlePlayerExit(input);
        }
        boolean isValid = false;
        String[] parts = input.split(" ");
        if (parts.length == 2) {
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
        } else {
            throw new IllegalArgumentException("Formato inválido. Use: <RANGO> <NOMBRE_JUGADOR>");
        }
        return isValid;
    }

    @Override
    public boolean handlePlayerExit(String input) throws RemoteException{
        boolean isValid = false;
        if (input.equals("exit")) {
            model.setPlayerWaiting((Player) clientPlayer);
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

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.model = (IGo_Fish) t;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof GameState gameState) {
            try {
                gameView.updateTurnState();
                switch (gameState) {
                    case READY -> {
                        gameView.notifyGameIntroduction();
                        gameView.showPlayersAndCards();
                        gameView.updateHand();
                        gameView.notifyPlayerTurn();
                    }
                    case TURN_SWITCH -> {
                        gameView.notifyPlayerAction();
                        gameView.notifyGameIntroduction();
                        gameView.showPlayersAndCards();
                        gameView.updateHand();
                        gameView.notifyPlayerTurn();
                    }
                    case GO_FISH -> {
                        gameView.notifyPlayerGoneFishing();
                        gameView.notifyFishedCard();
                    }
                    case TRANSFERRING_CARDS -> {
                        gameView.notifyTransferredCards();
                    }
                    case PLAYER_COMPLETED_SET -> {
                        gameView.notifyAmountOfSets();
                    }
                    case GAME_OVER -> {
                        gameView.notifyGameOver();
                        gameView.updateScores();
                        gameView.spawnExitOption();
                    }
                    case WAITING_ACTION, AWAITING_PLAYERS, NEW_STATUS_PLAYER -> {
                        gameView.handleException(new Exception("Estado del modelo fuera de orden."));
                    }
                }
            } catch (Exception e) {
                gameView.handleException(e);
            }
        }
        else {
            gameView.handleException(new IllegalArgumentException("Señal inválida del modelo."));
        }
    }
}
