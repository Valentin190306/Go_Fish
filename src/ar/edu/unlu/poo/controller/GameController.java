package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameController implements IGameController {
    private static GameController instance = null;
    private GameModelService service;
    private IGameView gameView;
    private IGameWindow gameWindow;
    private IPlayer clientPlayer;

    public static IGameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    private GameController() {}

    @Override
    public void setGameWindow(IGameWindow gameWindow) throws RemoteException {
        try {
            this.gameWindow = gameWindow;
        } catch (Exception e) {
            gameWindow.handleException(e);
        }
    }

    @Override
    public void setGameView(IGameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void connect() throws RemoteException {
        try {
            clientPlayer = service.connectPlayer();
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
    }

    @Override
    public void disconnect() throws RemoteException {
        try {
            service.disconnectPlayer(clientPlayer, this);
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
    }

    @Override
    public IPlayer fetchClientPlayer() throws RemoteException {
        try {
            if (this.clientPlayer == null) {
                return null;
            }
            return service.fetchClientPlayer(clientPlayer);
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public ArrayList<IPlayer> fetchPlayers() throws RemoteException {
        try {
            return service.fetchPlayers();
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public ArrayList<IPlayer> fetchOpponents() throws RemoteException {
        try {
            ArrayList<IPlayer> opponents = service.fetchPlayers();
            opponents.remove(clientPlayer);
            return opponents;
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public Value fetchQueriedValue() throws RemoteException {
        try {
            return service.fetchQueriedValue();
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public IPlayer fetchPlayingPlayer() throws RemoteException {
        try {
            return service.fetchPlayingPlayer();
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public IPlayer fetchTargetPlayer() throws RemoteException {
        try {
            return service.fetchTargetPlayer();
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public IDeck fetchDeck() throws RemoteException {
        try {
            return service.fetchDeck();
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public void setClientPlayerReady() throws RemoteException {
        try {
            service.setPlayerReady(clientPlayer);
            clientPlayer = service.fetchClientPlayer(clientPlayer);
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
    }

    @Override
    public void updateClientPlayerName(String name) throws RemoteException {
        try {
            service.configPlayerName(clientPlayer, name);
            clientPlayer = service.fetchClientPlayer(clientPlayer);
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
    }

    @Override
    public HashMap<String, Integer> fetchGameScoreList() throws RemoteException {
        try {
            return service.fetchGameScoreList();
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public HashMap<String, Integer> fetchHighScoreList() throws RemoteException {
        try {
            return service.fetchHighScoreList();
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
        return null;
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        IGo_Fish model = (IGo_Fish) t;
        this.service = new GameModelService(model);
    }

    @Override
    public void clientPlaysTurn(Value valueRequested, String targetPlayerName) throws RemoteException {
        try {
            IPlayer targetPlayer = service.getPlayerByName(targetPlayerName);
            IPlayer clientPlayer = service.fetchClientPlayer(this.clientPlayer);
            if (targetPlayer != null
                    && !targetPlayer.equals(clientPlayer)
                    && clientPlayer.hasCardOfValue(valueRequested)) {
                service.playTurn(valueRequested, targetPlayer);
            } else {
                gameWindow.handleException(new IllegalArgumentException("Jugador inexistente o jugada inválida"));
            }
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
    }

    private void returnToWaitingClient() throws RemoteException {
        try {
            service.setPlayerWaiting(clientPlayer);
        } catch (RemoteException e) {
            gameWindow.handleException(e);
        }
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof GameState gameState) {
            try {
                switch (gameState) {
                    case READY -> {
                        gameWindow.showGameCard();
                        gameView.updateTurnState();
                        gameView.notifyGameIntroduction();
                        gameView.showPlayersAndCards();
                        gameView.updateHand();
                        gameView.notifyPlayerTurn();
                    }
                    case TURN_SWITCH -> {
                        gameView.updateTurnState();
                        gameView.notifyPlayerAction();
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
                        gameView.updateHand();
                    }
                    case PLAYER_COMPLETED_SET -> {
                        gameView.notifyAmountOfSets();
                    }
                    case GAME_OVER -> {
                        gameView.updateScores();
                        gameView.notifyGameOver();
                        gameView.spawnExitOption();
                        returnToWaitingClient();
                    }
                    case NEW_STATUS_PLAYER -> {
                        gameWindow.getLobbyCard().updatePlayerList();
                    }
                    case WAITING_ACTION, AWAITING_PLAYERS -> {
                        gameView.handleException(new Exception("Estado del modelo fuera de orden."));
                    }
                }
            } catch (Exception e) {
                gameWindow.handleException(e);
            }
        } else {
            gameWindow.handleException(new IllegalArgumentException("Señal inválida del modelo."));
        }
    }
}

