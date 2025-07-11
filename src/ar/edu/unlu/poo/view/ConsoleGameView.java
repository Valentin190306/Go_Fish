package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IGameController;
import ar.edu.unlu.poo.interfaces.IGameView;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.enums.Value;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleGameView extends JPanel implements IGameView {
    private final JTextArea consoleArea;
    private final JTextField inputField;
    private final GameWindow gameWindow;
    private final IGameController controller;
    private String placeholder;
    private boolean isPlayerTurn;
    private boolean isGameOver = false;

    public ConsoleGameView(GameWindow gameWindow, IGameController controller) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        this.consoleArea = new JTextArea();
        this.inputField = new JTextField();
        this.placeholder = "<VALOR_CARTA> <NOMBRE_JUGADOR>";
        initComponents();
    }

    private void initComponents() {
        setSize(600, 400);
        setLayout(new BorderLayout());

        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consoleArea.setBackground(Color.BLACK);
        consoleArea.setForeground(Color.GREEN);
        add(new JScrollPane(consoleArea), BorderLayout.CENTER);

        inputField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.GREEN);
        inputField.addActionListener(e -> processInput());
        add(inputField, BorderLayout.SOUTH);

        inputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(inputField.getText().equals(placeholder)) {
                    inputField.setText("");
                    inputField.setForeground(Color.GREEN);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(inputField.getText().isEmpty()) {
                    inputField.setForeground(Color.GRAY);
                    inputField.setText(placeholder);
                }
            }
        });
    }

    private void processInput() {
        String input = inputField.getText().trim();
        try {
            if (!isGameOver) {
                if (!input.isEmpty()) {
                    handlePlayerInput(input);
                    inputField.setText("");
                }
            } else if (!input.isEmpty()) {
                handlePlayerExit(input);
                inputField.setText("");
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public void handlePlayerInput(String input) {
        try {
            if (input == null || input.isBlank()) {
                throw new IllegalArgumentException("La entrada del jugador no puede estar vacía");
            }

            String[] parts = input.split(" ");

            if (parts.length == 2) {
                Value valueRequested = parseValue(parts[0]);
                controller.clientPlaysTurn(valueRequested, parts[1]);
            } else {
                handleException(new IllegalArgumentException("Formato inválido. Use: <RANGO> <NOMBRE_JUGADOR>"));
            }
        } catch (RemoteException e) {
            handleException(e);
        }
    }

    private Value parseValue(String input) throws IllegalArgumentException {
        for (Value value : Value.values()) {
            if (value.getValue().equalsIgnoreCase(input)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Rango inválido. Use: <RANGO> <NOMBRE_JUGADOR>");
    }

    private void handlePlayerExit(String input) {
        if (input.equals("exit")) {
            gameWindow.showMenu();
        } else {
            gameWindow.handleException(new IllegalArgumentException("Entrada inválida. Use 'exit' para salir."));
        }
    }

    private void appendToConsole(String text) {
        consoleArea.append(text + "\n");
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    @Override
    public void handleException(Exception e) {
        SwingUtilities.invokeLater(() -> {
            appendToConsole("ERROR: " + e.getMessage());
        });
    }

    @Override
    public void start() {
        setVisible(true);
    }

    @Override
    public void notifyGameIntroduction() {
        try {
            String playerName = controller.fetchClientPlayer().getName();
            appendToConsole("================================================");
            appendToConsole("           BIENVENIDO AL GO FISH GAME");
            appendToConsole("================================================");
            appendToConsole("> Bienvenido jugador " + playerName + "...");
            appendToConsole("> Objetivo: Formar sets de 4 cartas del mismo valor");
            appendToConsole("> Formato de entrada: <VALOR_CARTA> <NOMBRE_JUGADOR>");
            appendToConsole("================================================");
        } catch (RemoteException e) {
            handleException(e);
        }
    }

    @Override
    public void notifyGameOver() {
        try {
            HashMap<String, Integer> scores = controller.fetchGameScoreList();

            appendToConsole("================================================");
            appendToConsole("                JUEGO TERMINADO");
            appendToConsole("================================================");
            appendToConsole("> No hay mas cartas en estas aguas...");
            appendToConsole("> El juego ha terminado.");
            appendToConsole("");
            appendToConsole("PUNTUACIONES FINALES:");
            appendToConsole("─────────────────────");

            scores.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> appendToConsole(String.format("  %s: %d sets",
                            entry.getKey(), entry.getValue())));

            appendToConsole("================================================");
        } catch (RemoteException e) {
            handleException(e);
        }

    }

    @Override
    public void notifyPlayerAction() {
        String[] vocabulary = {"reclama", "pide", "exige", "suplica", "mendiga"};
        String expression = vocabulary[(int) (Math.random() * vocabulary.length)];

        try {
            IPlayer playingPlayer = controller.fetchPlayingPlayer();
            IPlayer targetPlayer = controller.fetchTargetPlayer();
            Value queriedValue = controller.fetchQueriedValue();

            if (playingPlayer != null && targetPlayer != null && queriedValue != null) {
                String message = isPlayerTurn
                        ? String.format("> Le %s un %s a %s...", expression, queriedValue.getValue(), targetPlayer.getName())
                        : String.format("> %s %s un %s a %s...", playingPlayer.getName(), expression, queriedValue.getValue(), targetPlayer.getName());

                appendToConsole(message);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void notifyAmountOfSets() {
        try {
            IPlayer playingPlayer = controller.fetchPlayingPlayer();
            IPlayer clientPlayer = controller.fetchClientPlayer();

            if (playingPlayer != null && playingPlayer.hasCompletedSets()) {
                List<List<Card>> completedSets = playingPlayer.getCompletedSets();
                List<Card> lastSet = completedSets.get(completedSets.size() - 1);
                Value completedValue = lastSet.get(0).getNumber();

                String message = String.format("¡%s completó un set de %s!",
                        playingPlayer.getName(), completedValue.getValue());
                appendToConsole(message);

                if (clientPlayer.equals(playingPlayer)) {
                    appendToConsole(String.format("> Tienes %d sets completados", completedSets.size()));
                    appendToConsole("  Sets completados:");
                    for (List<Card> set : completedSets) {
                        appendToConsole("    - " + set.get(0).getNumber().getValue());
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
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
                        appendToConsole(String.format("> Fuiste a pescar y obtuviste: %s de %s",
                                fishedCard.getNumber().getValue(), fishedCard.getSuit().getValue()));
                    }
                } else {
                    appendToConsole(String.format("> %s fue a pescar una carta del mazo",
                            playingPlayer.getName()));
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void notifyPlayerGoneFishing() {
        try {
            IPlayer playingPlayer = controller.fetchPlayingPlayer();
            IPlayer targetPlayer = controller.fetchTargetPlayer();

            if (playingPlayer != null && targetPlayer != null) {
                appendToConsole(String.format("> ¡Go Fish! %s no tiene las cartas solicitadas.",
                        targetPlayer.getName()));
                appendToConsole(String.format("> %s debe pescar del mazo.", playingPlayer.getName()));
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void notifyPlayerTurn() {
        try {
            IPlayer playingPlayer = controller.fetchPlayingPlayer();
            IPlayer clientPlayer = controller.fetchClientPlayer();

            if (playingPlayer != null && clientPlayer != null) {
                boolean isClientTurn = clientPlayer.equals(playingPlayer);

                appendToConsole("__________________________________________________");
                if (isClientTurn) {
                    appendToConsole("> ¡ES TU TURNO " + playingPlayer.getName() + "!");
                    appendToConsole("> Haz tu jugada: <VALOR_CARTA> <NOMBRE_JUGADOR>");
                } else {
                    appendToConsole(String.format("> Turno de %s - Esperando jugada...",
                            playingPlayer.getName()));
                }
                appendToConsole("__________________________________________________");
            }
            updateTurnState();
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void notifyTransferredCards() {
        try {
            IPlayer clientPlayer = controller.fetchClientPlayer();
            IPlayer playingPlayer = controller.fetchPlayingPlayer();
            IPlayer targetPlayer = controller.fetchTargetPlayer();
            Value queriedValue = controller.fetchQueriedValue();

            List<Card> transferredCards = clientPlayer.getLastTransferenceCards();

            if (clientPlayer.equals(targetPlayer)) {
                appendToConsole(String.format("> %s entregó cartas %s a %s",
                        targetPlayer.getName(), queriedValue.getValue(), playingPlayer.getName()));
                appendToConsole("> Cartas cedidas:");
                for (Card card : transferredCards) {
                    appendToConsole("    " + card.getNumber().getValue() + " de " + card.getSuit().getValue());
                }
            } else if (clientPlayer.equals(playingPlayer)) {
                appendToConsole(String.format("> %s te entregó cartas %s",
                        targetPlayer.getName(), queriedValue.getValue()));
                appendToConsole("> Cartas adquiridas:");
                for (Card card : transferredCards) {
                    appendToConsole("    " + card.getNumber().getValue() + " de " + card.getSuit().getValue());
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void updateHand() {
        try {
            IPlayer clientPlayer = controller.fetchClientPlayer();
            List<Card> transferredCards = clientPlayer.getLastTransferenceCards();

            appendToConsole("─────────────────────────────────────────────────");
            appendToConsole("CARTAS EN TU MANO:");

            appendToConsole(clientPlayer.getAvailableCards().toString());

            appendToConsole("─────────────────────────────────────────────────");
            appendToConsole("CARTAS RECIBIDAS:");

            appendToConsole(transferredCards.toString());

            appendToConsole(String.format("  Total de cartas en mano: %d", clientPlayer.getHandSize()));

            if (clientPlayer.hasCompletedSets()) {
                appendToConsole("");
                appendToConsole("SETS COMPLETADOS:");
                appendToConsole(clientPlayer.getCompletedSets().toString());
                appendToConsole(String.format("  Total de sets: %d", clientPlayer.getScore()));
            }

            appendToConsole("─────────────────────────────────────────────────");
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void showPlayersAndCards() {
        try {
            List<IPlayer> players = controller.fetchPlayers();

            appendToConsole("ESTADO DEL JUEGO:");
            appendToConsole("─────────────────");
            appendToConsole("");
            appendToConsole("JUGADORES:");
            for (IPlayer player : players) {
                String indicator = "";
                if (player.equals(controller.fetchClientPlayer())) {
                    indicator = " (TÚ)";
                } else if (player.equals(controller.fetchPlayingPlayer())) {
                    indicator = " (JUGANDO)";
                }

                appendToConsole(String.format("  %s: %d cartas, %d sets%s, %s",
                        player.getName(), player.getHandSize(),
                        player.getScore(), indicator,
                        player.getCompletedSets()));
            }
            appendToConsole("─────────────────");
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void updateScores() {
        try {
            HashMap<String, Integer> scores = controller.fetchGameScoreList();

            appendToConsole("PUNTUACIONES ACTUALES:");
            appendToConsole("─────────────────────");

            scores.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        String indicator = "";
                        try {
                            if (entry.getKey().equals(controller.fetchClientPlayer().getName())) {
                                indicator = " (TÚ)";
                            }
                        } catch (RemoteException e) {
                            handleException(e);
                        }
                        appendToConsole(String.format("  %s: %d sets%s",
                                entry.getKey(), entry.getValue(), indicator));
                    });

            appendToConsole("─────────────────────");
        } catch (RemoteException e) {
            handleException(e);
        }
    }

    @Override
    public void spawnExitOption() {
        this.placeholder = "Ingrese 'exit' para volver al menú principal";
        this.isGameOver = true;
        inputField.setForeground(Color.YELLOW);
        inputField.setText(placeholder);

        appendToConsole("================================================");
        appendToConsole("         PRESIONA ENTER PARA CONTINUAR");
        appendToConsole("================================================");
    }

    @Override
    public void updateTurnState() {
        try {
            IPlayer clientPlayer = controller.fetchClientPlayer();
            IPlayer playingPlayer = controller.fetchPlayingPlayer();

            isPlayerTurn = clientPlayer.equals(playingPlayer);

            if (!isGameOver) {
                inputField.setBackground(isPlayerTurn ? Color.DARK_GRAY : Color.BLACK);
                inputField.setEnabled(isPlayerTurn);
            }
        } catch (RemoteException e) {
            handleException(e);
        }

    }
}