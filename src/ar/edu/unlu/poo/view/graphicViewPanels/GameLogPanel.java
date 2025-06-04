package ar.edu.unlu.poo.view.graphicViewPanels;

import javax.swing.*;
import java.awt.*;

public class GameLogPanel extends JPanel {

    private final JTextArea logArea;

    public GameLogPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        logArea.setForeground(Color.WHITE);

        // Fondo transparente
        logArea.setOpaque(false);
        logArea.setBackground(new Color(0, 0, 0, 0));
        logArea.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null); // o podés usar un TitledBorder si querés enmarcar el área

        add(scrollPane, BorderLayout.CENTER);
    }

    public void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void clearLog() {
        logArea.setText("");
    }
}

