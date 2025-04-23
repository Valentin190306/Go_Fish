package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.view.graphicViewPanels.FishermenPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.HandPanel;

import javax.swing.*;
import java.awt.*;
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
    public void handleException(Exception e) {

    }

    @Override
    public void start() {
        gameWindow.showGame();
        setVisible(true);
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
