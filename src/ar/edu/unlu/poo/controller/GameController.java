package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public class GameController implements IGameController, IControladorRemoto {
    private IGame game;
    private IGameView view;
    private final IPlayer clientPlayer;

    public GameController(IGame game, IPlayer clientPlayer) throws RemoteException {
        setModeloRemoto(game);
        this.clientPlayer = clientPlayer;
        game.agregarObservador(this);
    }

    @Override
    public void setView(IGameView view) {
        this.view = view;
    }

    @Override
    public boolean handlePlayerInput(String input) {
        boolean isValid = false;
        String[] parts = input.split(" ");
        if (parts.length == 2) {
            try {
                Value valueRequested = parseRank(parts[0]);
                IPlayer targetPlayer = game.getPlayerByName(parts[1]);

                if (targetPlayer != null
                        && targetPlayer != clientPlayer
                        && clientPlayer.hasCardOfRank(valueRequested)) {
                    game.playTurn(valueRequested, (Player) targetPlayer);
                    isValid = true;
                } else {
                    view.notifyInvalidPlayer();
                }
            } catch (IllegalArgumentException e) {
                view.notifyInvalidInputFormat();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
        view.notifyInvalidInputFormat();
        }
        return isValid;
    }

    private Value parseRank(String input) {
        for (Value value : Value.values()) {
            if (value.getValue().equalsIgnoreCase(input)) {
                return value;
            }
        }
        view.notifyInvalidInputFormat();
        return null;
    }

    private void clientPlayerSetsInHand() {
        view.notifyAmountOfSets(clientPlayer.getScore());
    }

    private void clientPlayerReceiveCards() throws RemoteException {
        if (game.getCurrentPlayer() == clientPlayer) {
            view.notifyReceivedCards(clientPlayer.getTransferenceCards());
        } else if (game.getTargetPlayer() == clientPlayer) {
            view.notifyLostCards(game.getTargetPlayer().getTransferenceCards());
        }
    }

    private void playerGoneFishing() throws RemoteException {
        if (game.getCurrentPlayer() == clientPlayer) {
            view.notifyClientPlayerGoneFishing();
            view.notifyFishedCard(clientPlayer.getTransferenceCards().get(0));
        } else {
            view.notifyPlayerGoneFishing(game.getCurrentPlayer());
        }
    }

    private void showPlayersAndCards() throws RemoteException {
        view.showPlayersAndCards(game.getDeck(), game.getPlayers());
    }

    private void handlePlayerTurn() throws RemoteException {
        boolean isCurrentPlayer = game.getCurrentPlayer().getName().equals(clientPlayer.getName());
        view.setPlayerTurn(isCurrentPlayer);
    }

    private void showPlayerHand() {
        System.out.println(clientPlayer.getName() + " : " + clientPlayer.getHand());
        view.updateHand(clientPlayer.getHand());
    }

    private void notifyTurnSwitch() throws RemoteException { view.notifyTurnSwitch(game.getCurrentPlayer()); }

    private void handleGameOver() throws RemoteException {
        view.notifyGameOver();
        view.updateScores(game.getPlayers());
    }

    private void controllerLog(GameState gameState) {
        System.out.println("Controller(" + clientPlayer.getName() + "): " + gameState);
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T model) throws RemoteException {
        this.game = (IGame) model;
    }

    @Override
    public void actualizar(IObservableRemoto model, Object event) throws RemoteException {
        if (event instanceof GameState gameState) {
            controllerLog(gameState);
            switch (gameState) {
                case DEALING_CARDS -> {
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
                }
                case GAME_OVER -> {
                    handleGameOver();
                }
                case GO_FISH -> {
                    playerGoneFishing();
                }
                case TRANSFERRING_CARDS -> {
                    clientPlayerReceiveCards();
                }
                case PLAYER_COMPLETED_SET -> {
                    clientPlayerSetsInHand();
                }
                default -> {
                    view.notifyUnknownState();
                }
            }
        }
    }
}