package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.RMIMVCException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.RemoteException;
import java.util.List;

public class ConsoleGameView extends JPanel implements IGameView {
    private final JTextArea consoleArea;
    private final JTextField inputField;
    private final IGameController controller;
    private String placeholder;
    private boolean isPlayerTurn;
    private boolean isGameOver = false;

    public ConsoleGameView(GameWindow gameWindow, IGameController controller) {
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
            } else if (!input.isEmpty() && controller.handlePlayerExit(input)) {
                inputField.setText("");
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public void handlePlayerInput(String input) throws RemoteException {
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
    }

    private Value parseValue(String input) throws IllegalArgumentException {
        for (Value value : Value.values()) {
            if (value.getValue().equalsIgnoreCase(input)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Rango inválido. Use: <RANGO> <NOMBRE_JUGADOR>");
    }

    private void appendToConsole(String text) {
        consoleArea.append(text + "\n");
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    @Override
    public void handleException(Exception e) {
        if (e instanceof RMIMVCException) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "RMI Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            appendToConsole("!> " + e.getMessage());
        }
    }

    @Override
    public void start() {
        //gameWindow.showGameCard();
        setVisible(true);
    }

    @Override
    public void notifyGameIntroduction() {
        try {
            appendToConsole("> Bienvenido jugador " + controller.fetchClientPlayer().getName() + "...");
        } catch (RemoteException e) {
            handleException(e);
        }
    }

    @Override
    public void notifyGameOver() {
        appendToConsole("> No hay mas cartas en estas aguas...\n> El juego ha terminado.");
    }

    @Override
    public void notifyPlayerAction() {
        String[] vocabulary = {"reclama", "pide", "exige", "suplica", "mendiga"};
        String expression = vocabulary[(int) (Math.random() * vocabulary.length)];

        try {
            String cardValue = controller
                    .fetchQueriedValue()
                    .toString();

            String targetPlayer = controller
                    .fetchTargetPlayer()
                    .getName();

            String message = isPlayerTurn
                    ? String.format("> Le %ss un %s a %s...", expression, cardValue, targetPlayer)
                    : String.format("> %s %s un %s a %s...", controller.fetchPlayingPlayer().getName(), expression, cardValue, targetPlayer);

            appendToConsole(message);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void notifyAmountOfSets() {
        try {
            appendToConsole("> Tienes " + controller.fetchDeck().size() + " sets...");
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void notifyFishedCard() {
        try {
            Card fishedCard = controller.fetchClientPlayer()
                    .getHand()
                    .getTransferenceCards()
                    .get(0);

            appendToConsole(String.format("> Pescaste un %s de %s...", fishedCard.getNumber().getValue(), fishedCard.getSuit().getValue()));
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void notifyPlayerGoneFishing() {
        if (isPlayerTurn) {
            appendToConsole("> Has ido a pescar...");
        } else {
            try {
                IPlayer player = controller.fetchPlayingPlayer();
                appendToConsole("> " + player.getName() + " fue a pescar...");
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    @Override
    public void notifyPlayerTurn() {
        try {
            IPlayer player = controller.fetchPlayingPlayer();

            appendToConsole("__________________________________________________");
            appendToConsole(isPlayerTurn ?
                    "> Es tu turno. Haz tu jugada." : "> Esperando el turno de " + player.getName() + "...");
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void notifyTransferredCards() {
        try {
            IPlayer clientPlayer = controller.fetchClientPlayer();
            IPlayer turnPlayer = controller.fetchPlayingPlayer();
            IPlayer targetPlayer = controller.fetchTargetPlayer();

            List<Card> cards = controller
                    .fetchClientPlayer()
                    .getHand()
                    .getTransferenceCards();

            if (clientPlayer.equals(targetPlayer) || clientPlayer.equals(turnPlayer)) {
                appendToConsole(clientPlayer.equals(targetPlayer) ? "> Cartas cedidas:" : "> Cartas adquiridas:");

                for (Card card : cards) {
                    appendToConsole("\t" + card.getNumber().getValue() + " de " + card.getSuit().getValue());
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void updateHand() {
        try {
            List<Card> cards = controller
                    .fetchClientPlayer()
                    .getHand()
                    .getTransferenceCards();

            appendToConsole("> Tu mano:");
            for (Card card : cards) {
                appendToConsole("\t" + card.getNumber().getValue() + " de " + card.getSuit().getValue());
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void showPlayersAndCards() {
        try {
            IDeck deck = controller.fetchDeck();
            List<IPlayer> players = controller.fetchPlayers();

            appendToConsole("> " + deck.size() + " cartas en pila...");
            appendToConsole("> Cartas en la mesa:");
            for (IPlayer player : players) {
                appendToConsole("\t" + player.getName() + ": " + player.getHand().size() + " cartas");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void updateScores() {
        try {
            List<IPlayer> players = controller.fetchPlayers();

            appendToConsole("> Puntajes:");
            for (IPlayer player : players) {
                appendToConsole("\t" + player.getName() + ": " + player.getHand().getScore());
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void spawnExitOption() {
        this.placeholder = "Ingrese -exit- para volver al menu principal.";
        this.isGameOver = true;
    }

    @Override
    public void updateTurnState() {
        try {
            isPlayerTurn = controller
                    .fetchPlayingPlayer()
                    .equals(controller.fetchClientPlayer());
        } catch (RemoteException e) {
            handleException(e);
        }
    }
}