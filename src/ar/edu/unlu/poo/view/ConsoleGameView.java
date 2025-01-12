package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IGameController;
import ar.edu.unlu.poo.interfaces.IGameView;
import ar.edu.unlu.poo.model.enums.Rank;
import ar.edu.unlu.poo.model.enums.Suit;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ConsoleGameView extends JFrame implements IGameView {
    private final JTextArea consoleArea;
    private final JTextField inputField;

    public ConsoleGameView(IGameController controller) {
        controller.setView(this);

        setTitle("Go Fish - Terminal View");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        setVisible(true);
    }

    private void appendToConsole(String message) {
        consoleArea.append(message + "\n");
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    @Override
    public void notifyGameIntroduction(String playerName) {
        appendToConsole("> Bienvenido jugador " + playerName + "...");
        appendToConsole("> Formato de entrada válido: <VALOR_CARTA> <NOMBRE_JUGADOR>");
    }

    @Override
    public void notifyTurnSwitch(String playerName) { appendToConsole("> Turno de " + playerName + "..."); }

    @Override
    public void notifyGameOver() {
        appendToConsole("> El juego ha terminado. ¡Gracias por jugar!");
    }

    @Override
    public void notifyPlayerAction() {
        appendToConsole("> Un jugador realizó una acción.");
    }

    @Override
    public void notifyInvalidInputFormat() { appendToConsole("!> Formato de entrada inválido. Use: <RANGO> <NOMBRE_JUGADOR>"); }

    @Override
    public void notifyAmountOfSets(int amount) { appendToConsole("> Tienes " + amount + " sets..."); }

    @Override
    public void notifyClientPlayerGoneFishing() { appendToConsole("> Has ido a pescar..."); }

    @Override
    public void notifyFishedCard(Rank rank, Suit suit) { appendToConsole("> Pescaste un " + rank.getValue() + " de " + suit.getValue() + "..."); }

    @Override
    public void notifyPlayerGoneFishing(String playerName) { appendToConsole("> " + playerName + " fue a pescar..."); }

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
    public void notifyReceivedCards(List<Map.Entry<Rank, Suit>> receivedCards) {
        appendToConsole("> Cartas recibidas: ");
        for (Map.Entry<Rank, Suit> card : receivedCards) {
            appendToConsole("\t" + card.getKey().getValue() + " de " + card.getValue().getValue());
        }
    }

    @Override
    public void notifyLostCards(List<Map.Entry<Rank, Suit>> lostCards) {
        appendToConsole("> Cartas cedidas: ");
        for (Map.Entry<Rank, Suit> card : lostCards) {
            appendToConsole("\t" + card.getKey().getValue() + " de " + card.getValue().getValue());
        }
    }

    @Override
    public void updateHand(List<Map.Entry<Rank, Suit>> hand) {
        appendToConsole("> Tu mano: ");
        for (Map.Entry<Rank, Suit> card : hand) {
            appendToConsole("\t" + card.getKey().getValue() + " de " + card.getValue().getValue());
        }
    }

    @Override
    public void showPlayersAndCards(int deckSize, List<Map.Entry<String, Integer>> playersAndCards) {
        appendToConsole("> " + deckSize + " cartas en pila...");
        appendToConsole("> Cartas en la mesa: ");
        for (Map.Entry<String, Integer> player : playersAndCards) {
            appendToConsole("\t" + player.getKey() + ": " + player.getValue() + " cartas");
        }

    }

    @Override
    public void updateScores(List<Map.Entry<String, Integer>> scores) {
        appendToConsole("> Puntajes:");
        for (Map.Entry<String, Integer> score : scores) {
            appendToConsole("\t" + score.getKey() + ": " + score.getValue());
        }
    }
}


