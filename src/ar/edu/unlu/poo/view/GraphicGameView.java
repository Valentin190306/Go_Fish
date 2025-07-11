package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IGameController;
import ar.edu.unlu.poo.interfaces.IGameView;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.view.graphicViewPanels.GameInfoPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.OpponentsPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.PlayerHandPanel;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphicGameView extends JPanel implements IGameView {
    private final IGameController controller;
    private final GameWindow gameWindow;

    private OpponentsPanel opponentsPanel;
    private GameInfoPanel gameInfoPanel;
    private PlayerHandPanel playerHandPanel;

    private ImageIcon background;

    public GraphicGameView(GameWindow gameWindow, IGameController controller) {
        this.controller = controller;
        this.gameWindow = gameWindow;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        try {
            background = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/backgrounds/background.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            background = null;
        }

        opponentsPanel = new OpponentsPanel(this);
        gameInfoPanel = new GameInfoPanel(this);
        playerHandPanel = new PlayerHandPanel(this);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 700));
        setOpaque(true);
    }

    private void setupLayout() {
        add(opponentsPanel, BorderLayout.NORTH);
        add(gameInfoPanel, BorderLayout.CENTER);
        add(playerHandPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(34, 139, 34));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void handleException(Exception e) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error del Juego",
                JOptionPane.ERROR_MESSAGE));
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }

    @Override
    public void notifyGameIntroduction() {
    }

    @Override
    public void notifyGameOver() {
        SwingUtilities.invokeLater(()-> {
            try {
                HashMap<String, Integer> scores = controller.fetchGameScoreList();
                gameInfoPanel.displayGameOver(scores);

                opponentsPanel.setOpponentsEnabled(false);
                playerHandPanel.setCardsEnabled(false);
                gameInfoPanel.setEnabled(false);
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyPlayerAction() {
        SwingUtilities.invokeLater(()-> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                IPlayer targetPlayer = controller.fetchTargetPlayer();
                Value queriedValue = controller.fetchQueriedValue();

                if (playingPlayer != null && targetPlayer != null && queriedValue != null) {
                    String message = String.format("%s pidió cartas %s a %s",
                            playingPlayer.getName(),
                            queriedValue.getValue(),
                            targetPlayer.getName());
                    gameInfoPanel.displayMessage(message);
                }
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyAmountOfSets() {
        SwingUtilities.invokeLater(()-> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                IPlayer clientPlayer = controller.fetchClientPlayer();

                if (playingPlayer != null && playingPlayer.hasCompletedSets()) {
                    List<List<Card>> completedSets = playingPlayer.getCompletedSets();
                    List<Card> lastSet = completedSets.get(completedSets.size() - 1);
                    Value completedValue = lastSet.get(0).getNumber();

                    String message = String.format("¡%s completó un set de %s!",
                            playingPlayer.getName(), completedValue.getValue());
                    gameInfoPanel.displayMessage(message);

                    if (clientPlayer.equals(playingPlayer)) {
                        gameInfoPanel.updateSetsFromPlayer(clientPlayer);
                    }
                }
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyFishedCard() {
        try {
            IPlayer playingPlayer = controller.fetchPlayingPlayer();
            IPlayer clientPlayer = controller.fetchClientPlayer();

            if (playingPlayer != null) {
                if (clientPlayer.equals(playingPlayer)) {
                    Card fishedCard = playingPlayer.getFishedCard();
                    if (fishedCard != null) {
                        String message = String.format("Fuiste a pescar y obtuviste: %s de %s",
                                fishedCard.getNumber().getValue(),
                                fishedCard.getSuit().toString());
                        gameInfoPanel.displayMessage(message);
                        gameInfoPanel.showFishedCardPopup(fishedCard);
                    }
                } else {
                    String message = String.format("%s fue a pescar una carta del mazo",
                            playingPlayer.getName());
                    gameInfoPanel.displayMessage(message);
                }
            }
        } catch (RemoteException e) {
            handleException(e);
        }
    }

    @Override
    public void notifyPlayerGoneFishing() {
        SwingUtilities.invokeLater(()-> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                IPlayer targetPlayer = controller.fetchTargetPlayer();

                if (playingPlayer != null && targetPlayer != null) {
                    String message = String.format("¡Go Fish! %s no tiene las cartas solicitadas. %s debe pescar del mazo.",
                            targetPlayer.getName(), playingPlayer.getName());
                    gameInfoPanel.displayMessage(message);
                }
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyPlayerTurn() {
        SwingUtilities.invokeLater(()-> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                IPlayer clientPlayer = controller.fetchClientPlayer();

                if (playingPlayer != null && clientPlayer != null) {
                    boolean isClientTurn = clientPlayer.equals(playingPlayer);
                    String message = isClientTurn ?
                            "¡Es tu turno!" :
                            String.format("Turno de %s", playingPlayer.getName());

                    gameInfoPanel.displayMessage(message);
                }
                updateTurnState();
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyTransferredCards() {
        SwingUtilities.invokeLater(()-> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                IPlayer targetPlayer = controller.fetchTargetPlayer();
                Value queriedValue = controller.fetchQueriedValue();

                if (playingPlayer != null && targetPlayer != null && queriedValue != null) {
                    String message = String.format("%s entregó cartas %s a %s",
                            targetPlayer.getName(),
                            queriedValue.getValue(),
                            playingPlayer.getName());
                    gameInfoPanel.displayMessage(message);
                }
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void updateHand() {
        SwingUtilities.invokeLater(()-> {
            try {
                playerHandPanel.updateHand();
                IPlayer clientPlayer = controller.fetchClientPlayer();
                gameInfoPanel.updateSetsFromPlayer(clientPlayer);
            } catch (Exception e) {
                handleException(e);
            }
        });
    }

    @Override
    public void showPlayersAndCards() {
        SwingUtilities.invokeLater(()-> {
            try {
                opponentsPanel.updateOpponents();
            } catch (Exception e) {
                handleException(e);
            }
        });
    }

    @Override
    public void updateScores() {
        SwingUtilities.invokeLater(()->{
            try {
                HashMap<String, Integer> scores = controller.fetchGameScoreList();
                StringBuilder scoresText = new StringBuilder("Puntuaciones: ");

                for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                    scoresText.append(entry.getKey()).append(": ").append(entry.getValue()).append(" | ");
                }

                gameInfoPanel.displayMessage(scoresText.toString());
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void spawnExitOption() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Deseas salir del juego?",
                "Salir del Juego",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            gameWindow.showMenu();
        }
    }

    @Override
    public void updateTurnState() {
        SwingUtilities.invokeLater(()-> {
            try {
                gameInfoPanel.clearMessages();
                IPlayer clientPlayer = controller.fetchClientPlayer();
                IPlayer playingPlayer = controller.fetchPlayingPlayer();

                boolean isClientTurn = clientPlayer.equals(playingPlayer);

                opponentsPanel.updateTurnState(isClientTurn);
                playerHandPanel.updateTurnState(isClientTurn);
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    public IGameController getController() {
        return controller;
    }

    public IPlayer getSelectedOpponent() {
        return opponentsPanel.getSelectedOpponent();
    }

    public void playTurn(Value value, String targetPlayer) {
        try {
            controller.clientPlaysTurn(value, targetPlayer);
            String message = String.format("Pidiendo cartas %s a %s", value.getValue(), targetPlayer);
            gameInfoPanel.displayMessage(message);
        } catch (RemoteException e) {
            handleException(e);
        }
    }
}