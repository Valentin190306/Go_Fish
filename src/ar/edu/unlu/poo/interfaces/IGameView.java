package ar.edu.unlu.poo.interfaces;

public interface IGameView {
    void handleException(Exception e);

    void start();

    void notifyGameIntroduction();

    void notifyGameOver();

    void notifyPlayerAction();

    void notifyAmountOfSets();

    void notifyFishedCard();

    void notifyPlayerGoneFishing();

    void notifyPlayerTurn();

    void notifyTransferredCards();

    void updateHand();

    void showPlayersAndCards();

    void updateScores();

    void spawnExitOption();

    void updateTurnState();
}
