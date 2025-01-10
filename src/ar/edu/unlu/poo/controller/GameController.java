package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.Rank;
import ar.edu.unlu.poo.model.Suit;

import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements IGameController {
    private final IGame game;
    private IGameView view;
    private final IPlayer clientPlayer;

    public GameController(IGame game, IPlayer clientPlayer) throws RemoteException {
        this.game = game;
        this.clientPlayer = clientPlayer;
        game.addObserver(this);
    }

    @Override
    public void setView(IGameView view) {
        this.view = view;
    }

    @Override
    public void initializeGame() throws RemoteException {
        game.addObserver(this);
    }

    @Override
    public boolean handlePlayerInput(String input) {
        boolean isValid = false;
        String[] parts = input.split(" ");
        if (parts.length == 2) {
            try {
                Rank rankRequested = parseRank(parts[0]);
                IPlayer targetPlayer = game.getPlayerByName(parts[1]);

                if (targetPlayer != null
                        && targetPlayer != clientPlayer
                        && clientPlayer.hasCardOfRank(rankRequested)) {
                    game.playTurn(rankRequested, (Player) targetPlayer);
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

    private Rank parseRank(String input) {
        for (Rank rank : Rank.values()) {
            if (rank.getValue().equalsIgnoreCase(input)) {
                return rank;
            }
        }
        view.notifyInvalidInputFormat();
        return null;
    }

    @Override
    public void update(GameState gameState) {
        controllerLog(gameState);
        switch (gameState) {
            case DEALING_CARDS -> {
                view.notifyGameIntroduction(clientPlayer.getName());
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

    private void clientPlayerSetsInHand() {
        view.notifyAmountOfSets(clientPlayer.getScore());
    }

    private void clientPlayerReceiveCards() {
        if (game.getCurrentPlayer() == clientPlayer) {
            List<Map.Entry<Rank, Suit>> newCards = new ArrayList<>();
            for (ICard card : clientPlayer.getTransferenceCards()) {
                newCards.add(new AbstractMap.SimpleEntry<>(card.getRank(), card.getSuit()));
            }
            view.notifyReceivedCards(newCards);
        } else if (game.getTargetPlayer() == clientPlayer) {
            List<Map.Entry<Rank, Suit>> lostCards = new ArrayList<>();
            for (ICard card : game.getTargetPlayer().getTransferenceCards()) {
                lostCards.add(new AbstractMap.SimpleEntry<>(card.getRank(), card.getSuit()));
            }
            view.notifyLostCards(lostCards);
        }
    }

    private void playerGoneFishing() {
        if (game.getCurrentPlayer() == clientPlayer) {
            view.notifyClientPlayerGoneFishing();
            ICard fishedCard = clientPlayer.getTransferenceCards().get(0);
            view.notifyFishedCard(fishedCard.getRank(), fishedCard.getSuit());
        } else {
            view.notifyPlayerGoneFishing(game.getCurrentPlayer().getName());
        }
    }

    private void showPlayersAndCards() {
        List<Map.Entry<String, Integer>> playersCardCount = new ArrayList<>();
        for (IPlayer player : game.getPlayers()) {
            playersCardCount.add(new AbstractMap.SimpleEntry<>(player.getName(), player.getHand().size()));
        }
        view.showPlayersAndCards(game.getDeck().size(), playersCardCount);
    }

    private void handlePlayerTurn() {
        boolean isCurrentPlayer = game.getCurrentPlayer().getName().equals(clientPlayer.getName());
        view.setPlayerTurn(isCurrentPlayer);
    }

    private void showPlayerHand() {
        List<Map.Entry<Rank, Suit>> hand = new ArrayList<>();
        System.out.println(clientPlayer.getName() + " : " + clientPlayer.getHand());
        for (ICard card : clientPlayer.getHand()) {
            hand.add(new AbstractMap.SimpleEntry<>(card.getRank(), card.getSuit()));
        }
        view.updateHand(hand);
    }

    private void notifyTurnSwitch() { view.notifyTurnSwitch(game.getCurrentPlayer().getName()); }

    private void handleGameOver() {
        view.notifyGameOver();
        List<Map.Entry<String, Integer>> scores = new ArrayList<>();
        for (IPlayer player : game.getPlayers()) {
            scores.add(new AbstractMap.SimpleEntry<>(player.getName(), player.getHand().size()));
        }
        view.updateScores(scores);
    }

    private void controllerLog(GameState gameState) {
        System.out.println("Controller(" + clientPlayer.getName() + "): " + gameState);
    }
}