package ar.edu.unlu.poo.view.viewPanels;

import ar.edu.unlu.poo.interfaces.IGameController;
import ar.edu.unlu.poo.view.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScoresPanel extends JPanel {
    private final GameWindow gameWindow;
    private final IGameController controller;
    private LinkedHashMap<String, Integer> sortedScores;
    private JPanel scoresPanel;

    public ScoresPanel(GameWindow gameWindow, IGameController controller) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        this.sortedScores = new LinkedHashMap<>();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Puntajes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Crear el panel de puntajes y mantener referencia
        scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(scoresPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Volver al Menú");
        backButton.addActionListener(e -> gameWindow.showMenu());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateScoresDisplay() {
        scoresPanel.removeAll();

        if (sortedScores.isEmpty()) {
            JLabel noScoresLabel = new JLabel("No hay puntajes disponibles");
            noScoresLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noScoresLabel.setForeground(Color.GRAY);
            scoresPanel.add(noScoresLabel);
        } else {
            for (Map.Entry<String, Integer> entry : sortedScores.entrySet()) {
                String text = entry.getKey() + ": " + entry.getValue();
                JLabel label = new JLabel(text);
                label.setFont(new Font("Arial", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                scoresPanel.add(label);
            }
        }

        scoresPanel.revalidate();
        scoresPanel.repaint();
    }

    public void printScores() {
        try {
            Map<String, Integer> scoreData = controller.fetchHighScoreList();

            if (scoreData instanceof LinkedHashMap<String, Integer> scores) {
                this.sortedScores = scores;
            } else if (scoreData != null) {
                this.sortedScores = new LinkedHashMap<>(scoreData);
            } else {
                throw new ClassCastException("El controlador no devolvió un Map válido");
            }

            updateScoresDisplay();

        } catch (ClassCastException e) {
            System.err.println("Error de tipo de datos: " + e.getMessage());
            handleScoresError("Error en el formato de los puntajes");
        } catch (Exception e) {
            System.err.println("Error al cargar puntajes: " + e.getMessage());
            handleScoresError("No se pudieron cargar los puntajes");
        }
    }

    private void handleScoresError(String message) {
        sortedScores.clear();
        scoresPanel.removeAll();

        JLabel errorLabel = new JLabel(message);
        errorLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        errorLabel.setForeground(Color.RED);
        scoresPanel.add(errorLabel);

        scoresPanel.revalidate();
        scoresPanel.repaint();

        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.WARNING_MESSAGE);
    }
}