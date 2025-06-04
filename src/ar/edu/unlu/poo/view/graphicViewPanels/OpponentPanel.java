package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IGameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class OpponentPanel extends JPanel {
    private final IGameController controller;
    private final List<OpponentView> opponents;

    public OpponentPanel(IGameController controller) {
        this.controller = controller;
        this.opponents = List.of(
                new OpponentView("Jugador 1"),
                new OpponentView("Jugador 2"),
                new OpponentView("Jugador 3")
        );

        setLayout(new GridLayout(1, opponents.size(), 10, 0));
        setOpaque(false);

        for (OpponentView view : opponents) {
            add(view);
        }
    }

    public void updateOpponent(int index, String name, int cardCount, ImageIcon avatar) {
        if (index >= 0 && index < opponents.size()) {
            opponents.get(index).update(name, cardCount, avatar);
        }
    }

    public void setActionListener(int index, ActionListener listener) {
        if (index >= 0 && index < opponents.size()) {
            opponents.get(index).setActionListener(listener);
        }
    }

    // Clase interna para representar visualmente a un oponente
    private static class OpponentView extends JPanel {
        private final JLabel nameLabel;
        private final JLabel cardCountLabel;
        private final JButton selectButton;
        private final JLabel avatarLabel;

        public OpponentView(String name) {
            setLayout(new BorderLayout());
            setOpaque(false);

            nameLabel = new JLabel(name, SwingConstants.CENTER);
            cardCountLabel = new JLabel("Cartas: 0", SwingConstants.CENTER);
            avatarLabel = new JLabel();
            avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
            selectButton = new JButton("Seleccionar");

            add(nameLabel, BorderLayout.NORTH);
            add(avatarLabel, BorderLayout.CENTER);
            add(cardCountLabel, BorderLayout.SOUTH);
            add(selectButton, BorderLayout.EAST);
        }

        public void update(String name, int cardCount, ImageIcon avatar) {
            nameLabel.setText(name);
            cardCountLabel.setText("Cartas: " + cardCount);
            avatarLabel.setIcon(avatar);
        }

        public void setActionListener(ActionListener listener) {
            for (ActionListener al : selectButton.getActionListeners()) {
                selectButton.removeActionListener(al);
            }
            selectButton.addActionListener(listener);
        }
    }
}
