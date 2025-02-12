package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConsoleGameView extends JPanel implements IGameView {
    private final JTextArea consoleArea;
    private final JTextField inputField;
    private final IController controller;

    public ConsoleGameView(IController controller) {
        this.controller = controller;
        controller.setView(this);

        setSize(600, 400);

        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consoleArea.setBackground(Color.BLACK);
        consoleArea.setForeground(Color.GREEN);

        JScrollPane scrollPane = new JScrollPane(consoleArea);

        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.GREEN);

        inputField.addActionListener(e -> {
            String input = inputField.getText();
            boolean isValid = controller.handlePlayerInput(input);
            while (!isValid) isValid = controller.handlePlayerInput(input);
            inputField.setText("");
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
    }

    public void start() {
        setVisible(true);
    }

    private void appendToConsole(String message) {
        consoleArea.append(message + "\n");
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    @Override
    public void notifyGameIntroduction(IPlayer player) {
        appendToConsole("> Bienvenido jugador " + player.getName() + "...");
        appendToConsole("> Formato de entrada válido: <VALOR_CARTA> <NOMBRE_JUGADOR>");
    }

    @Override
    public void notifyTurnSwitch(IPlayer player) { appendToConsole("> Turno de " + player.getName() + "..."); }

    @Override
    public void notifyGameOver() {
        appendToConsole("> El juego ha terminado. ¡Gracias por jugar!");
    }

    @Override
    public void notifyPlayerAction(IPlayer targetPlayer, IPlayer player) {
        appendToConsole("> " + player.getName() + " le pregunta al pescador " + targetPlayer.getName() + "...");
    }

    @Override
    public void notifyInvalidInputFormat() { appendToConsole("!> Formato de entrada inválido. Use: <RANGO> <NOMBRE_JUGADOR>"); }

    @Override
    public void notifyAmountOfSets(int amount) { appendToConsole("> Tienes " + amount + " sets..."); }

    @Override
    public void notifyClientPlayerGoneFishing() { appendToConsole("> Has ido a pescar..."); }

    @Override
    public void notifyFishedCard(ICard card) { appendToConsole("> Pescaste un " + card.getNumber().getValue() + " de " + card.getSuit().getValue() + "..."); }

    @Override
    public void notifyPlayerGoneFishing(IPlayer player) { appendToConsole("> " + player.getName() + " fue a pescar..."); }

    @Override
    public void notifyInvalidPlayer() {
        appendToConsole("!> Jugador no encontrado.");
    }

    @Override
    public void notifyUnknownState() {
        appendToConsole("!> Estado desconocido del juego.");
    }

    @Override
    public void setPlayerTurn(boolean isPlayerTurn) {
        inputField.setEnabled(isPlayerTurn);
        appendToConsole("__________________________________________________");
        if (isPlayerTurn) {
            appendToConsole("> Es tu turno. Haz tu movimiento.");
        } else {
            appendToConsole("> Esperando el turno del oponente...");
        }
    }

    @Override
    public void notifyReceivedCards(List<ICard> cards) {
        appendToConsole("> Cartas recibidas: ");
        for (ICard card : cards) {
            appendToConsole("\t" + card.getNumber().getValue() + " de " + card.getSuit().getValue());
        }
    }

    @Override
    public void notifyLostCards(List<ICard> cards) {
        appendToConsole("> Cartas cedidas: ");
        for (ICard card : cards) {
            appendToConsole("\t" + card.getNumber().getValue() + " de " + card.getSuit().getValue());
        }
    }

    @Override
    public void updateHand(IHand hand) {
        appendToConsole("> Tu mano: ");
        for (ICard card : hand.getCards()) {
            appendToConsole("\t" + card.getNumber().getValue() + " de " + card.getSuit().getValue());
        }
    }

    @Override
    public void showPlayersAndCards(IDeck deck, List<IPlayer> players) {
        appendToConsole("> " + deck.size() + " cartas en pila...");
        appendToConsole("> Cartas en la mesa: ");
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
}


