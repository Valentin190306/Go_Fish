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
        this.gameWindow = gameWindow;
    }

    @Override
    public void setGameView(IGameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void connect() throws RemoteException {
        clientPlayer = service.connectPlayer();
    }

    @Override
    public void disconnect() throws RemoteException {
        service.disconnectPlayer(clientPlayer, this);
    }

    @Override
    public IPlayer fetchClientPlayer() throws RemoteException {
        clientPlayer = service.fetchClientPlayer(clientPlayer);
        return clientPlayer;
    }

    @Override
    public ArrayList<IPlayer> fetchPlayers() throws RemoteException {
        return service.fetchPlayers();
    }

    @Override
    public ArrayList<IPlayer> fetchOpponents() throws RemoteException {
        ArrayList<IPlayer> opponents = service.fetchPlayers();
        opponents.remove(clientPlayer);
        return opponents;
    }

    @Override
    public Value fetchQueriedValue() throws RemoteException {
        return service.fetchQueriedValue();
    }

    @Override
    public IPlayer fetchPlayingPlayer() throws RemoteException {
        return service.fetchPlayingPlayer();
    }

    @Override
    public IPlayer fetchTargetPlayer() throws RemoteException {
        return service.fetchTargetPlayer();
    }

    @Override
    public IDeck fetchDeck() throws RemoteException {
        return service.fetchDeck();
    }

    @Override
    public void setClientPlayerReady() throws RemoteException {
        service.setPlayerReady(clientPlayer);
        clientPlayer = service.fetchClientPlayer(clientPlayer);
    }

    @Override
    public void updateClientPlayerName(String name) throws RemoteException {
        service.configPlayerName(clientPlayer, name);
        clientPlayer = service.fetchClientPlayer(clientPlayer);
    }

    @Override
    public HashMap<String, Integer> fetchGameScoreList() throws RemoteException {
        return service.fetchGameScoreList();
    }

    @Override
    public HashMap<String, Integer> fetchHighScoreList() throws RemoteException {
        return service.fetchHighScoreList();
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        IGo_Fish model = (IGo_Fish) t;
        this.service = new GameModelService(model);
    }

    @Override
    public void clientPlaysTurn(Value valueRequested, String targetPlayerName) throws RemoteException {
        IPlayer targetPlayer = service.getPlayerByName(targetPlayerName);

        IPlayer clientPlayer = service.fetchClientPlayer(this.clientPlayer);
        if (targetPlayer != null
                && !targetPlayer.equals(clientPlayer)
                && clientPlayer.hasCardOfValue(valueRequested)) {
            service.playTurn(valueRequested, targetPlayer);
        } else {
            throw new IllegalArgumentException("Jugador inexistente o jugada inválida");
        }
    }

    private void returnToWaitingClient() throws RemoteException {
        service.setPlayerWaiting(clientPlayer);
    }

    // TODO Crear lógica de recarga del modelo y guardado
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

