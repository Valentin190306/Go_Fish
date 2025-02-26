package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;

import java.util.List;

public class GraphicGameView implements IGameView {
    private IController controller;

    public GraphicGameView(IController controller) {
        this.controller = controller;
    }

    @Override
    public void start() {

    }

    @Override
    public void notifyTurnSwitch(IPlayer player) {

    }

    @Override
    public void notifyGameOver() {

    }

    @Override
    public void notifyPlayerAction(IPlayer targetPlayer, IPlayer player, boolean isPlayerTurn) {

    }

    @Override
    public void handleException(Exception e) {

    }

    @Override
    public void notifyGameIntroduction(IPlayer player) {

    }

    @Override
    public void notifyClientPlayerGoneFishing() {

    }

    @Override
    public void notifyFishedCard(ICard card) {

    }

    @Override
    public void notifyReceivedCards(List<ICard> cards) {

    }

    @Override
    public void notifyLostCards(List<ICard> cards) {

    }

    @Override
    public void notifyAmountOfSets(int amount) {

    }

    @Override
    public void notifyPlayerGoneFishing(IPlayer player) {

    }

    @Override
    public void showPlayersAndCards(IDeck deck, List<IPlayer> players) {

    }

    @Override
    public void setPlayerTurn(boolean isPlayerTurn) {

    }

    @Override
    public void updateHand(IHand hand) {

    }

    @Override
    public void updateScores(List<IPlayer> players) {

    }
}
