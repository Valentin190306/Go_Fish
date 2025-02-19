package ar.edu.unlu.poo.view;

import javax.swing.*;
import java.awt.*;

public class RulesPanel extends JPanel {

    public RulesPanel(GameWindow gameWindow) {
        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Reglas", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(title, gbc);

        JTextArea rulesText = getJTextArea();
        rulesText.setFont(new Font("Arial", Font.PLAIN, 16));
        rulesText.setForeground(Color.WHITE);
        rulesText.setBackground(new Color(50, 50, 50));
        rulesText.setWrapStyleWord(true);
        rulesText.setLineWrap(true);
        rulesText.setEditable(false);

        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        add(rulesText, gbc);

        JButton btnBack = new JButton("Volver al Menú");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 16));
        btnBack.setBackground(new Color(70, 130, 180));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnBack.addActionListener(e -> gameWindow.showMenu());
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        add(btnBack, gbc);
    }

    private static JTextArea getJTextArea() {
        JTextArea rulesText = new JTextArea();
        rulesText.setText("Reglas del Juego:\n\n" +
                "1. Cada jugador recibe 7 cartas (o 5 si hay más de 3 jugadores).\n" +
                "2. En su turno, un jugador pide una carta a otro jugador.\n" +
                "3. Si el jugador tiene la carta solicitada, debe entregarla.\n" +
                "4. Si no la tiene, el jugador que pidió debe tomar una carta del mazo (Go Fish).\n" +
                "5. El objetivo es formar conjuntos de 4 cartas del mismo valor.\n" +
                "6. El juego continúa hasta que no haya más cartas.\n" +
                "7. Gana el jugador con más conjuntos completados.\n");
        rulesText.setWrapStyleWord(true);
        rulesText.setLineWrap(true);
        rulesText.setEditable(false);
        return rulesText;
    }
}
