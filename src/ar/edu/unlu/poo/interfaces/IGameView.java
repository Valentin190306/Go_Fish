package ar.edu.unlu.poo.interfaces;

import java.util.List;

public interface IGameView {

    void start();

    void notifyTurnSwitch(IPlayer player);

    void notifyGameOver();

    void notifyPlayerAction(IPlayer targetPlayer, IPlayer player, ICard queriedCard, boolean isPlayerTurn);

    void handleException(Exception e);

    void notifyGameIntroduction(IPlayer player);

    void notifyClientPlayerGoneFishing();

    void notifyFishedCard(ICard card);

    void notifyReceivedCards(List<ICard> cards);

    void notifyLostCards(List<ICard> cards);

    void notifyAmountOfSets(int amount);

    void notifyPlayerGoneFishing(IPlayer player);

    void showPlayersAndCards(IDeck deck, List<IPlayer> players);

    void setPlayerTurn(boolean isPlayerTurn);

    void updateHand(IHand hand);

    void updateScores(List<IPlayer> players);

    void spawnExitOption();
}
