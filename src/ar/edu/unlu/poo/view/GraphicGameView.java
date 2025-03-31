package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.view.graphicViewPanels.FishermenPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.HandPanel;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Objects;

public class GraphicGameView extends JPanel implements IGameView {
    private final GameWindow gameWindow;
    private IController controller;
    private final ImageIcon background;
    private final FishermenPanel fishermenPanel;
    private final HandPanel handPanel;

    public GraphicGameView(GameWindow gameWindow, IController controller) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        this.background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ar/edu/unlu/poo/view/assets/backgrounds/background.png")));
        this.fishermenPanel = new FishermenPanel();
        this.handPanel = new HandPanel();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(fishermenPanel, BorderLayout.CENTER);
        add(handPanel, BorderLayout.SOUTH);

        fishermenPanel.setPreferredSize(new Dimension(400, 150));
        fishermenPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        fishermenPanel.setVisible(true);
        handPanel.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void start() {
        gameWindow.showGame();
        setVisible(true);
    }

    private void handleException(Exception e) {

    }

    private void notifyGameIntroduction() {

    }

    private void notifyGameOver() {

    }

    private void notifyPlayerAction() {

    }

    private void notifyAmountOfSets() {

    }

    private void notifyFishedCard() {

    }

    private void notifyPlayerGoneFishing() {

    }

    private void notifyPlayerTurn() {

    }

    private void notifyTransferredCards() {

    }

    private void updateHand() {

    }

    private void showPlayersAndCards() {

    }

    private void updateScores() {

    }

    private void spawnExitOption() {

    }

    private void updateTurnState() {

    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof GameState gameState) {
            try {
                updateTurnState();
                switch (gameState) {
                    case READY, TURN_SWITCH -> {
                        notifyGameIntroduction();
                        showPlayersAndCards();
                        updateHand();
                        notifyPlayerTurn();
                    }
                    case GO_FISH -> {
                        notifyPlayerGoneFishing();
                        notifyFishedCard();
                    }
                    case TRANSFERRING_CARDS -> {
                        notifyTransferredCards();
                    }
                    case PLAYER_COMPLETED_SET -> {
                        notifyAmountOfSets();
                    }
                    case GAME_OVER -> {
                        notifyGameOver();
                        updateScores();
                        spawnExitOption();
                    }
                    case WAITING_ACTION, AWAITING_PLAYERS, NEW_STATUS_PLAYER -> {
                        handleException(new Exception("Estado del modelo fuera de orden."));
                    }
                }
            } catch (Exception e) {
                handleException(e);
            }
        }
        else {
            handleException(new IllegalArgumentException("Señal inválida del modelo."));
        }
    }

}
