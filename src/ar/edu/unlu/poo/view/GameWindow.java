package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.poo.interfaces.IGameView;
import ar.edu.unlu.poo.interfaces.IGameWindow;
import ar.edu.unlu.poo.view.viewPanels.LobbyPanel;
import ar.edu.unlu.poo.view.viewPanels.MenuPanel;
import ar.edu.unlu.poo.view.viewPanels.RulesPanel;
import ar.edu.unlu.poo.view.viewPanels.ScoresPanel;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class GameWindow extends JFrame implements IGameWindow, IObservadorRemoto {
    private final IController controller;
    private IGameView gameView;
    private String playerName = null;
    private final JPanel viewContainer;
    private final CardLayout cardLayout;
    private final MenuPanel menuCard;
    private final LobbyPanel lobbyCard;
    private final ScoresPanel scoresCard;

    public GameWindow(IController controller) throws RemoteException {
        this.controller = controller;
        this.gameView = new GraphicGameView(this, controller);

        this.menuCard = new MenuPanel(this, controller);
        this.lobbyCard = new LobbyPanel(controller);
        this.scoresCard = new ScoresPanel(this, controller);

        setTitle("Go Fish");
        setSize(800, 600);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Seguro que quieres salir?", "Confirmar salida",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        if (controller.fetchClientPlayer() != null) {
                            controller.disconnect();
                        }
                    } catch (RemoteException ex) {
                        messagePopUp(ex);
                    }
                    dispose();
                    System.exit(0);
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

    public void messagePopUp(Exception e) {
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

    @Override
    public MenuPanel getMenuCard() {
        return menuCard;
    }

    @Override
    public LobbyPanel getLobbyCard() {
        return lobbyCard;
    }

    @Override
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
            messagePopUp(e);
        }
    }

    public void showCard(String cardName) {
        cardLayout.show(viewContainer, cardName);
    }

    public void showMenu() {
        showCard("Menu");
    }

    public void showGame() {
        lobbyCard.removeAll();
        showCard("View");
    }

    @Override
    public void start() {
        setVisible(true);
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {

    }
}