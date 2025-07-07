package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.view.graphicViewPanels.GameInfoPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.OpponentsPanel;
import ar.edu.unlu.poo.view.graphicViewPanels.PlayerHandPanel;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GraphicGameView extends JPanel implements IGameView {
    private final IGameController controller;
    private final GameWindow gameWindow;

    private OpponentsPanel opponentsPanel;
    private GameInfoPanel gameInfoPanel;
    private PlayerHandPanel playerHandPanel;

    private ImageIcon background;

    /**
     * Constructor que inicializa la vista gráfica
     * @param controller Controlador del juego para manejar acciones
     */
    public GraphicGameView(GameWindow gameWindow, IGameController controller) {
        this.controller = controller;
        this.gameWindow = gameWindow;
        initializeComponents();
        setupLayout();
    }

    /**
     * Inicializa los componentes principales de la vista
     */
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

    /**
     * Configura el layout de la vista con las tres secciones
     */
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
        SwingUtilities.invokeLater(() -> {
            gameInfoPanel.displayTextWithTimestamp("¡Bienvenido al juego!");
            gameInfoPanel.displayText("Esperando a que todos los jugadores estén listos...");
        });
    }

    @Override
    public void notifyGameOver() {
        SwingUtilities.invokeLater(() -> {
            try {
                HashMap<String, Integer> scores = controller.fetchGameScoreList();
                gameInfoPanel.displayTextWithTimestamp("¡JUEGO TERMINADO!");
                gameInfoPanel.displayText("Puntuaciones finales:");

                for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                    gameInfoPanel.displayText(entry.getKey() + ": " + entry.getValue() + " puntos");
                }

                opponentsPanel.setOpponentsEnabled(false);
                playerHandPanel.setCardsEnabled(false);

            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyPlayerAction() {
        SwingUtilities.invokeLater(() -> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                IPlayer targetPlayer = controller.fetchTargetPlayer();
                Value queriedValue = controller.fetchQueriedValue();

                if (playingPlayer != null && targetPlayer != null && queriedValue != null) {
                    gameInfoPanel.displayTextWithTimestamp(
                            playingPlayer.getName() + " pidió cartas " + queriedValue.getValue() +
                                    " a " + targetPlayer.getName()
                    );
                }

            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyAmountOfSets() {
        SwingUtilities.invokeLater(() -> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                if (playingPlayer != null) {
                    int sets = playingPlayer.getScore();
                    gameInfoPanel.displayText(playingPlayer.getName() + " completó un set. Sets: " + sets);
                }
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyFishedCard() {
        SwingUtilities.invokeLater(() -> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                if (playingPlayer != null) {
                    gameInfoPanel.displayText(playingPlayer.getName() + " pescó una carta del mazo.");
                }
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyPlayerGoneFishing() {
        SwingUtilities.invokeLater(() -> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                IPlayer targetPlayer = controller.fetchTargetPlayer();

                if (playingPlayer != null && targetPlayer != null) {
                    gameInfoPanel.displayTextWithTimestamp(
                            targetPlayer.getName() + " le dijo 'Go Fish!' a " + playingPlayer.getName()
                    );
                }
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyPlayerTurn() {
        SwingUtilities.invokeLater(() -> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                if (playingPlayer != null) {
                    gameInfoPanel.displayTextWithTimestamp("Turno de: " + playingPlayer.getName());
                }
                updateTurnState();
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void notifyTransferredCards() {
        SwingUtilities.invokeLater(() -> {
            try {
                IPlayer playingPlayer = controller.fetchPlayingPlayer();
                IPlayer targetPlayer = controller.fetchTargetPlayer();
                Value queriedValue = controller.fetchQueriedValue();

                if (playingPlayer != null && targetPlayer != null && queriedValue != null) {
                    gameInfoPanel.displayText(
                            targetPlayer.getName() + " le dio cartas " + queriedValue.getValue() +
                                    " a " + playingPlayer.getName()
                    );
                }
            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void updateHand() {
        SwingUtilities.invokeLater(() -> {
            try {
                playerHandPanel.updateHand();
            } catch (Exception e) {
                handleException(e);
            }
        });
    }

    @Override
    public void showPlayersAndCards() {
        SwingUtilities.invokeLater(() -> {
            try {
                opponentsPanel.updateOpponents();
            } catch (Exception e) {
                handleException(e);
            }
        });
    }

    @Override
    public void updateScores() {
        SwingUtilities.invokeLater(() -> {
            try {
                HashMap<String, Integer> scores = controller.fetchGameScoreList();
                gameInfoPanel.displayTextWithTimestamp("Puntuaciones actuales:");

                for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                    gameInfoPanel.displayText(entry.getKey() + ": " + entry.getValue() + " sets");
                }

            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    @Override
    public void spawnExitOption() {
        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "¿Deseas salir del juego?",
                    "Salir del Juego",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                gameWindow.showMenu();
            }
        });
    }

    @Override
    public void updateTurnState() {
        SwingUtilities.invokeLater(() -> {
            try {
                IPlayer clientPlayer = controller.fetchClientPlayer();
                IPlayer playingPlayer = controller.fetchPlayingPlayer();

                boolean isClientTurn = clientPlayer.equals(playingPlayer);

                // Actualizar estado de los paneles
                opponentsPanel.updateTurnState(isClientTurn);
                playerHandPanel.updateTurnState(isClientTurn);

                // Mostrar información de turno
                if (isClientTurn) {
                    gameInfoPanel.displayTextWithTimestamp("¡Es tu turno! Selecciona una carta y un oponente.");
                }

            } catch (RemoteException e) {
                handleException(e);
            }
        });
    }

    // =========================
    // MÉTODOS DE COMUNICACIÓN CON PANELES
    // =========================

    /**
     * Getter para el controlador (usado por los paneles)
     */
    public IGameController getController() {
        return controller;
    }

    /**
     * Obtiene el oponente seleccionado del OpponentsPanel
     * @return El oponente seleccionado o null
     */
    public IPlayer getSelectedOpponent() {
        return opponentsPanel.getSelectedOpponent();
    }

    /**
     * Método para que los paneles notifiquen selecciones
     * @param selectedOpponent Oponente seleccionado
     */
    public void onOpponentSelected(IPlayer selectedOpponent) {
        gameInfoPanel.displayText("Oponente seleccionado: " + selectedOpponent.getName());
    }

    /**
     * Método para que playerHandPanel notifique selección de carta
     * @param selectedValue Valor de carta seleccionado
     */
    public void onCardSelected(Value selectedValue) {
        gameInfoPanel.displayText("Carta seleccionada: " + selectedValue.getValue());
    }

    /**
     * Método para ejecutar jugada
     * @param value Valor solicitado
     * @param targetPlayer Jugador objetivo
     */
    public void playTurn(Value value, String targetPlayer) {
        try {
            controller.clientPlaysTurn(value, targetPlayer);
            gameInfoPanel.displayTextWithTimestamp("Pidiendo cartas " + value.getValue() + " a " + targetPlayer);
        } catch (RemoteException e) {
            handleException(e);
        }
    }
}