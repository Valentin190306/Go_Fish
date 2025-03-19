package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.view.graphicViewPanels.FishermenPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.HandPanel;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
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

    public void notifyTurnSwitch(IPlayer player) {

    }

    public void notifyGameOver() {

    }

    public void notifyPlayerAction(IPlayer targetPlayer, IPlayer player, ICard queriedCard, boolean isPlayerTurn) {

    }

    public void handleException(Exception e) {

    }

    public void notifyGameIntroduction(IPlayer player) {

    }

    public void notifyClientPlayerGoneFishing() {

    }

    public void notifyFishedCard(ICard card) {

    }

    public void notifyReceivedCards(List<ICard> cards) {

    }

    public void notifyLostCards(List<ICard> cards) {

    }

    public void notifyAmountOfSets(int amount) {

    }

    public void notifyPlayerGoneFishing(IPlayer player) {

    }

    public void showPlayersAndCards(IDeck deck, List<IPlayer> players) {

    }

    @Override
    public void setPlayerTurn(boolean isPlayerTurn) {

    }


    public void updateHand(IHand hand) {

    }


    public void updateScores(List<IPlayer> players) {

    }


    public void spawnExitOption() {

    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {

    }
}
