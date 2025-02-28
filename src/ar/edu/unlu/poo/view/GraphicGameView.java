package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
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
