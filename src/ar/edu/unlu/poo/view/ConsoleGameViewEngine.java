package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.RemoteException;
import java.util.List;

public class ConsoleGameViewEngine extends JPanel implements IObservadorRemoto, IGameView {
    private final GameWindow gameWindow;
    private final JTextArea consoleArea;
    private final JTextField inputField;
    private final SemiController controller;
    private String placeholder;
    private boolean isGameOver = false;

    public ConsoleGameView(GameWindow gameWindow, SemiController controller) {
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
                if (!input.isEmpty() && controller.handlePlayerInput(input)) {
                    inputField.setText("");
                }
            } else if (!input.isEmpty() && controller.handlePlayerExit(input)) {
                inputField.setText("");
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public void appendToConsole(String text) {
        consoleArea.append(text + "\n");
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    @Override
    public void start() {
        gameWindow.showGame();
        setVisible(true);
    }

    @Override
    public void notifyGameIntroduction(IPlayer player) {
        appendToConsole("> Bienvenido jugador " + player.getName() + "...");
    }

    @Override
    public void notifyTurnSwitch(IPlayer player) {
        //consoleArea.setText("");
        //consoleArea.setCaretPosition(0);
        appendToConsole("> Turno de " + player.getName());
    }

    @Override
    public void notifyGameOver() {
        appendToConsole("> No hay mas cartas en estas aguas...\n> El juego ha terminado.");
    }

    @Override
    public void notifyPlayerAction(IPlayer targetPlayer, IPlayer player, ICard queriedCard, boolean isPlayerTurn) {
        String[] vocabulary = {"reclama", "pide", "exige", "suplica", "mendiga"};
        String expression = vocabulary[(int)(Math.random() * vocabulary.length)];
        appendToConsole(isPlayerTurn
                ? "> Le " + expression + "s un " + queriedCard.getNumber().getValue() + " a " + targetPlayer.getName() + "..."
                : "> " + player.getName() + " " + expression + " un " + queriedCard.getNumber() + " a " + targetPlayer.getName() + "...");
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
    public void notifyAmountOfSets(int amount) {
        appendToConsole("> Tienes " + amount + " sets...");
    }

    @Override
    public void notifyClientPlayerGoneFishing() {
        appendToConsole("> Has ido a pescar...");
    }

    @Override
    public void notifyFishedCard(ICard card) {
        appendToConsole("> Pescaste un " + card.getNumber().getValue() + " de " + card.getSuit().getValue() + "...");
    }

    @Override
    public void notifyPlayerGoneFishing(IPlayer player) {
        appendToConsole("> " + player.getName() + " fue a pescar...");
    }

    @Override
    public void setPlayerTurn(boolean isPlayerTurn) {
        inputField.setEnabled(isPlayerTurn);
        appendToConsole("__________________________________________________");
        appendToConsole(isPlayerTurn ? "> Es tu turno. Haz tu jugada." : "> Esperando el turno del oponente...");
    }

    @Override
    public void notifyReceivedCards(java.util.List<ICard> cards) {
        appendToConsole("> Cartas recibidas:");
        for (ICard card : cards) {
            appendToConsole("\t" + card.getNumber().getValue() + " de " + card.getSuit().getValue());
        }
    }

    @Override
    public void notifyLostCards(java.util.List<ICard> cards) {
        appendToConsole("> Cartas cedidas:");
        for (ICard card : cards) {
            appendToConsole("\t" + card.getNumber().getValue() + " de " + card.getSuit().getValue());
        }
    }

    @Override
    public void updateHand(IHand hand) {
        appendToConsole("> Tu mano:");
        for (ICard card : hand.getCards()) {
            appendToConsole("\t" + card.getNumber().getValue() + " de " + card.getSuit().getValue());
        }
    }

    @Override
    public void showPlayersAndCards(IDeck deck, java.util.List<IPlayer> players) {
        appendToConsole("> " + deck.size() + " cartas en pila...");
        appendToConsole("> Cartas en la mesa:");
        for (IPlayer player : players) {
            appendToConsole("\t" + player.getName() + ": " + player.getHand().size() + " cartas");
        }
    }

    @Override
    public void updateScores(List<IPlayer> players) {
        appendToConsole("> Puntajes:");
        for (IPlayer player : players) {
            appendToConsole("\t" + player.getName() + ": " + player.getHand().getScore());
        }
    }

    @Override
    public void spawnExitOption() {
        this.placeholder = "Ingrese -exit- para volver al menu principal.";
        this.isGameOver = true;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof GameState gameState) {
            try {
                switch (gameState) {
                    case AWAITING_PLAYERS -> {
                    }
                    case NEW_STATUS_PLAYER -> {
                    }
                    case READY -> {
                    }
                    case WAITING_ACTION -> {
                    }
                    case GO_FISH -> {
                    }
                    case TRANSFERRING_CARDS -> {
                    }
                    case PLAYER_COMPLETED_SET -> {
                    }
                    case TURN_SWITCH -> {
                    }
                    case GAME_OVER -> {
                    }
                    default -> throw new IllegalArgumentException("Estado inválido del modelo.");
                }
            } catch (Exception e) {
                handleException(e);
            }
        }
        else {
            handleException(new IllegalArgumentException("Señal inválida del modelo."));
        }
    }
}
