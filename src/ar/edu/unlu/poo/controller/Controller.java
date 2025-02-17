package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public class Controller implements IController, IControladorRemoto {
    private IGo_Fish model;
    private IGameView view;
    private IPlayer clientPlayer;

    public Controller() throws RemoteException {}

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T model) throws RemoteException {
        this.model = (IGo_Fish) model;

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
                Value valueRequested = parseValue(parts[0]);
                IPlayer targetPlayer = model.getPlayerCalled(parts[1]);

                if (targetPlayer != null
                        && targetPlayer != clientPlayer
                        && clientPlayer.getHand().hasCardOfValue(valueRequested)) {
                    model.playTurn(valueRequested, (Player) targetPlayer);
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

    private Value parseValue(String input) {
        for (Value value : Value.values()) {
            if (value.getValue().equalsIgnoreCase(input)) {
                return value;
            }
        }
        view.notifyInvalidInputFormat();
        return null;
    }

    private void clientPlayerSetsInHand() {
        view.notifyAmountOfSets(clientPlayer.getHand().getScore());
    }

    private void clientPlayerReceiveCards() throws RemoteException {
        if (model.getCurrentPlayerPlayingTurn() == clientPlayer) {
            view.notifyReceivedCards(clientPlayer.getHand().getTransferenceCards());
        } else if (model.getTargetPlayer() == clientPlayer) {
            view.notifyLostCards(model.getTargetPlayer().getHand().getTransferenceCards());
        }
    }

    private void playerGoneFishing() throws RemoteException {
        if (model.getCurrentPlayerPlayingTurn() == clientPlayer) {
            view.notifyClientPlayerGoneFishing();
            view.notifyFishedCard(clientPlayer.getHand().getTransferenceCards().get(0));
        } else {
            view.notifyPlayerGoneFishing(model.getCurrentPlayerPlayingTurn());
        }
    }

    private void showPlayersAndCards() throws RemoteException {
        view.showPlayersAndCards(model.getDeck(), model.getPlayers());
    }

    private void handlePlayerTurn() throws RemoteException {
        boolean isCurrentPlayer = model.getCurrentPlayerPlayingTurn().getName().equals(clientPlayer.getName());
        view.setPlayerTurn(isCurrentPlayer);
    }

    private void showPlayerHand() {
        System.out.println(clientPlayer.getName() + " : " + clientPlayer.getHand().toString());
        view.updateHand(clientPlayer.getHand());
    }

    private void notifyTurnSwitch() throws RemoteException { view.notifyTurnSwitch(model.getCurrentPlayerPlayingTurn()); }

    private void handleGameOver() throws RemoteException {
        view.notifyGameOver();
        view.updateScores(model.getPlayers());
    }

    private void controllerLog(GameState gameState) {
        System.out.println("Controller(" + clientPlayer.getName() + "): " + gameState);
    }

    @Override
    public void actualizar(IObservableRemoto model, Object event) throws RemoteException {
        if (event instanceof GameState gameState) {
            controllerLog(gameState);
            switch (gameState) {
                case READY -> {
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