package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IGameController;
import ar.edu.unlu.poo.interfaces.IGameView;
import ar.edu.unlu.poo.view.viewPanels.LobbyPanel;
import ar.edu.unlu.poo.view.viewPanels.MenuPanel;
import ar.edu.unlu.poo.view.viewPanels.RulesPanel;
import ar.edu.unlu.poo.view.viewPanels.ScoresPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class GameWindow extends JFrame {
    private final IGameController controller;
    private IGameView gameView;
    private String playerName = null;
    private final JPanel viewContainer;
    private final CardLayout cardLayout;
    private final MenuPanel menuCard;
    private final LobbyPanel lobbyCard;
    private final ScoresPanel scoresCard;

    public GameWindow(IGameController controller) throws RemoteException {
        this.controller = controller;
        this.gameView = new GraphicGameView(this, controller);

        this.menuCard = new MenuPanel(this, controller);
        this.lobbyCard = new LobbyPanel(controller);
        this.scoresCard = new ScoresPanel(this, controller);

        lobbyCard.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                lobbyCard.start();
            }
        });

        setTitle("Go Fish");
        setSize(800, 600);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Seguro que quieres salir?", "Confirmar salida",
                        JOptionPane.YES_NO_OPTION);

                try {
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controller.fetchClientPlayer() != null) {
                            controller.disconnect();
                        }
                        dispose();
                        System.exit(0);
                    }
                } catch (RemoteException ex) {
                    handleException(ex);
                }
            }
        });

        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        viewContainer = new JPanel(cardLayout);

        viewContainer.add(menuCard, "Menu");
        viewContainer.add(lobbyCard, "Lobby");
        viewContainer.add(scoresCard, "Scores");
        viewContainer.add(new RulesPanel(this), "Rules");

        add(viewContainer, BorderLayout.CENTER);
        showMenu();
    }

    public void handleException(Exception e) {
        JOptionPane.showMessageDialog(null,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public IGameView getGameView() {
        return gameView;
    }

    public void setGameView(IGameView gameView) {
        this.gameView = gameView;
        viewContainer.add((Component) gameView, "View");
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public MenuPanel getMenuCard() {
        return menuCard;
    }

    public LobbyPanel getLobbyCard() {
        return lobbyCard;
    }

    public ScoresPanel getScoresCard() {
        return scoresCard;
    }

    public void configureControllerAndView() {
        try {
            controller.connect();
            controller.setGameView(this.gameView);

            if (playerName != null) {
                controller.updateClientPlayerName(playerName);
            }
            viewContainer.add((Component) gameView, "View");
        } catch (RemoteException e) {
            handleException(e);
        }
    }

    public void showCard(String cardName) {
        cardLayout.show(viewContainer, cardName);
    }

    public void showMenu() {
        showCard("Menu");
    }

    public void showScores() {
        showCard("Scores");
        scoresCard.printScores();
    }

    public void showGameCard() {
        lobbyCard.removeAll();
        showCard("View");
    }

    public void start() {
        setVisible(true);
    }
}