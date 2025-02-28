package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IController;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScoresPanel extends JPanel {
    private final GameWindow gameWindow;
    private final IController controller;
    private LinkedHashMap<String, Integer> sortedScores;

    public ScoresPanel(GameWindow gameWindow, IController controller) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        this.sortedScores = new LinkedHashMap<>();
        printScores();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Puntajes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Map.Entry<String, Integer> entry : sortedScores.entrySet()) {
            String text = entry.getKey() + ": " + entry.getValue();
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            scoresPanel.add(label);
        }

        JScrollPane scrollPane = new JScrollPane(scoresPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Volver al Menú");
        backButton.addActionListener(e -> gameWindow.showMenu());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void printScores() {
        try {
            this.sortedScores = (LinkedHashMap<String, Integer>) controller.getScores();
        } catch (Exception e) {
            gameWindow.messagePopUp(e);
        }
        initComponents();
    }
}
