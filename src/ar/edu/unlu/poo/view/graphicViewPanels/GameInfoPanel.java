package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInfoPanel extends JPanel {

    private JTextArea messageArea;
    private JScrollPane messageScrollPane;
    private JPanel setsPanel;
    private JPanel setsContainer;

    private final List<String> currentTurnMessages;
    private String currentTurnPlayer;

    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 120);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 215, 0);
    private static final Color MESSAGE_AREA_BG = new Color(0, 0, 0, 160);
    private static final Font MAIN_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);

    public GameInfoPanel(GraphicGameView parent) {
        this.currentTurnMessages = new ArrayList<>();
        this.currentTurnPlayer = null;
        initializeComponents();
        setupLayout();
        setupStyling();
    }

    private void initializeComponents() {
        messageArea = new CustomTransparentTextArea();
        messageArea.setFont(MAIN_FONT);
        messageArea.setForeground(TEXT_COLOR);
        messageArea.setBackground(MESSAGE_AREA_BG);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageArea.setOpaque(false);

        messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setOpaque(false);
        messageScrollPane.getViewport().setOpaque(false);
        messageScrollPane.getViewport().setBackground(MESSAGE_AREA_BG);
        messageScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        messageScrollPane.getVerticalScrollBar().setOpaque(false);
        messageScrollPane.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 100));

        JLabel setsLabel = new JLabel("Sets Completados:");
        setsLabel.setFont(TITLE_FONT);
        setsLabel.setForeground(TEXT_COLOR);
        setsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        setsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setsContainer.setOpaque(false);

        setsPanel = new JPanel(new BorderLayout());
        setsPanel.setOpaque(false);
        setsPanel.add(setsLabel, BorderLayout.NORTH);
        setsPanel.add(setsContainer, BorderLayout.CENTER);
        setsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                "Tu Progreso",
                0, 0, TITLE_FONT, TEXT_COLOR
        ));
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(messageScrollPane, BorderLayout.CENTER);
        add(setsPanel, BorderLayout.SOUTH);
    }

    private void setupStyling() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 300));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2d.dispose();
    }

    public void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            addMessageToCurrentTurn(message);
            updateMessageDisplay();
            messageArea.repaint();
        });
    }

    public void displayGameOver(HashMap<String, Integer> scores) {
        StringBuilder gameOverMessage = new StringBuilder("¡JUEGO TERMINADO! - ");

        String winner = "";
        int maxScore = -1;

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                winner = entry.getKey();
            }
        }

        gameOverMessage.append(String.format("Ganador: %s con %d puntos", winner, maxScore));

        SwingUtilities.invokeLater(() -> {
            clearCurrentTurnMessages();
            addMessageToCurrentTurn(gameOverMessage.toString());
            updateMessageDisplay();
            scrollToBottom();
            messageArea.repaint();
        });
    }

    public void updateSetsFromPlayer(IPlayer clientPlayer) {
        SwingUtilities.invokeLater(() -> {
            setsContainer.removeAll();

            if (clientPlayer != null && clientPlayer.hasCompletedSets()) {
                List<List<Card>> completedSets = clientPlayer.getCompletedSets();

                for (List<Card> set : completedSets) {
                    if (!set.isEmpty()) {
                        Value setValue = set.get(0).getNumber();
                        JLabel setLabel = createSetLabel(setValue.getValue());
                        setsContainer.add(setLabel);
                    }
                }
            } else {
                JLabel noSetsLabel = new JLabel("Ningún set completado aún");
                noSetsLabel.setForeground(Color.LIGHT_GRAY);
                noSetsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                setsContainer.add(noSetsLabel);
            }

            setsContainer.revalidate();
            setsContainer.repaint();
        });
    }

    public void showFishedCardPopup(Card fishedCard) {
        SwingUtilities.invokeLater(() -> {
            JDialog popup = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Carta Pescada", true);
            popup.setLayout(new BorderLayout());
            popup.setSize(300, 200);
            popup.setLocationRelativeTo(this);

            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(new Color(0, 100, 0));

            JLabel cardLabel = new JLabel(String.format("<html><center>¡Go Fish!<br><br>Pescaste del mazo:<br>%s de %s</center></html>",
                    fishedCard.getNumber().getValue(), fishedCard.getSuit().getValue()),
                    SwingConstants.CENTER);
            cardLabel.setFont(new Font("Arial", Font.BOLD, 16));
            cardLabel.setForeground(Color.WHITE);

            JButton okButton = new JButton("OK");
            okButton.setPreferredSize(new Dimension(80, 30));
            okButton.addActionListener(e -> popup.dispose());

            contentPanel.add(cardLabel, BorderLayout.CENTER);
            contentPanel.add(okButton, BorderLayout.SOUTH);

            popup.add(contentPanel);
            popup.setVisible(true);
        });
    }

    private void addMessageToCurrentTurn(String message) {
        if (message != null && !message.trim().isEmpty()) {
            currentTurnMessages.add(message);
        }
    }

    private void updateMessageDisplay() {
        if (currentTurnMessages.isEmpty()) {
            messageArea.setText("Esperando eventos del juego...");
            return;
        }

        StringBuilder displayText = new StringBuilder();
        for (int i = 0; i < currentTurnMessages.size(); i++) {
            displayText.append(currentTurnMessages.get(i));
            if (i < currentTurnMessages.size() - 1) {
                displayText.append("\n");
            }
        }

        messageArea.setText(displayText.toString());
    }

    private void clearCurrentTurnMessages() {
        currentTurnMessages.clear();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        });
    }

    private JLabel createSetLabel(String value) {
        JLabel setLabel = new JLabel(String.format("%s", value));
        setLabel.setFont(new Font("Arial", Font.BOLD, 12));
        setLabel.setForeground(Color.WHITE);
        setLabel.setOpaque(true);
        setLabel.setBackground(new Color(0, 128, 0));
        setLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        setLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return setLabel;
    }

    public void clearMessages() {
        SwingUtilities.invokeLater(() -> {
            clearCurrentTurnMessages();
            updateMessageDisplay();
            messageArea.repaint();
        });
    }

    /**
     * Clase interna para manejar correctamente la transparencia del JTextArea
     */
    private static class CustomTransparentTextArea extends JTextArea {

        public CustomTransparentTextArea() {
            super();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            super.paintComponent(g);

            g2d.dispose();
        }

        @Override
        public boolean isOpaque() {
            return false;
        }
    }
}