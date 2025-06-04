package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.view.graphicViewPanels.GameLogPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.OpponentPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.PlayerHandPanel;

import javax.swing.*;
import java.awt.*;

public class GraphicGameView extends JPanel implements IGameView {
    private final IGameController controller;

    private OpponentPanel opponentPanel;
    private GameLogPanel logPanel;
    private PlayerHandPanel handPanel;

    private Image backgroundImage;

    public GraphicGameView(GameWindow window, IGameController controller) {
        this.controller = controller;
        loadBackground();
        initializeComponents();
        layoutComponents();
    }

    private void loadBackground() {
        backgroundImage = new ImageIcon("/ar/edu/unlu/poo/view/assets/backgrounds/background.png").getImage();
    }

    private void initializeComponents() {
        opponentPanel = new OpponentPanel(controller);
        logPanel = new GameLogPanel();
        handPanel = new PlayerHandPanel(controller);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);
        add(opponentPanel, BorderLayout.NORTH);
        add(logPanel, BorderLayout.CENTER);
        add(handPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Métodos de IGameView aún no implementados (pueden ser stubs por ahora)
    @Override
    public void handleException(Exception e) {
    }

    @Override
    public void start() {
    }

    @Override
    public void notifyGameIntroduction() {
    }

    @Override
    public void notifyGameOver() {
    }

    @Override
    public void notifyPlayerAction() {
    }

    @Override
    public void notifyAmountOfSets() {
    }

    @Override
    public void notifyFishedCard() {
    }

    @Override
    public void notifyPlayerGoneFishing() {
    }

    @Override
    public void notifyPlayerTurn() {
    }

    @Override
    public void notifyTransferredCards() {
    }

    @Override
    public void updateHand() {
    }

    @Override
    public void showPlayersAndCards() {
    }

    @Override
    public void updateScores() {
    }

    @Override
    public void spawnExitOption() {
    }

    @Override
    public void updateTurnState() {
    }
}
