package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel que muestra información y eventos del juego Go Fish
 * Incluye área de mensajes principales y panel de sets completados
 */
public class GameInfoPanel extends JPanel {

    private JTextArea messageArea;
    private JScrollPane messageScrollPane;
    private JPanel setsPanel;
    private JPanel setsContainer;

    private final List<String> currentTurnMessages;
    private String currentTurnPlayer; // Para detectar cambios de turno

    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 120);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 215, 0); // Dorado
    private static final Color TURN_COLOR = new Color(0, 255, 0);
    private static final Color MESSAGE_AREA_BG = new Color(0, 0, 0, 160);
    private static final Font MAIN_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);

    /**
     * Constructor del panel de información del juego
     * @param parent Vista padre que contiene este panel
     */
    public GameInfoPanel(GraphicGameView parent) {
        this.currentTurnMessages = new ArrayList<>();
        this.currentTurnPlayer = null;
        initializeComponents();
        setupLayout();
        setupStyling();
    }

    /**
     * Inicializa los componentes del panel
     */
    private void initializeComponents() {
        messageArea = new JTextArea();
        messageArea.setFont(MAIN_FONT);
        messageArea.setForeground(TEXT_COLOR);
        messageArea.setBackground(MESSAGE_AREA_BG);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setOpaque(false);
        messageScrollPane.getViewport().setOpaque(false);
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

    /**
     * Configura el layout del panel
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(messageScrollPane, BorderLayout.CENTER);
        add(setsPanel, BorderLayout.SOUTH);
    }

    /**
     * Configura el estilo visual del panel
     */
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

    /**
     * Muestra un mensaje general en el panel (se acumula hasta cambio de turno)
     * @param message Mensaje a mostrar
     */
    public void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            addMessageToCurrentTurn(message);
            updateMessageDisplay();
        });
    }

    /**
     * Muestra un mensaje de turno con color especial si es el turno del cliente
     * Este método limpia los mensajes acumulados y reinicia para el nuevo turno
     * @param message Mensaje a mostrar
     * @param isClientTurn True si es el turno del cliente
     */
    public void displayTurnMessage(String message, boolean isClientTurn) {
        SwingUtilities.invokeLater(() -> {
            // Detectar cambio de turno y limpiar mensajes acumulados
            String newTurnPlayer = extractPlayerFromTurnMessage(message);
            if (shouldClearMessages(newTurnPlayer)) {
                clearCurrentTurnMessages();
            }

            currentTurnPlayer = newTurnPlayer;

            // Agregar mensaje de turno con formato especial
            String turnMessage = formatTurnMessage(message, isClientTurn);
            addMessageToCurrentTurn(turnMessage);
            updateMessageDisplay();

            // Scroll automático al final
            scrollToBottom();
        });
    }

    /**
     * Muestra la pantalla de fin de juego con puntuaciones
     * @param scores Mapa de puntuaciones finales
     */
    public void displayGameOver(HashMap<String, Integer> scores) {
        StringBuilder gameOverMessage = new StringBuilder("¡JUEGO TERMINADO! - ");

        // Encontrar al ganador
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
        });
    }

    /**
     * Actualiza la visualización de sets completados del jugador cliente
     * @param clientPlayer Jugador cliente
     */
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

    /**
     * Muestra un pop-up con la carta pescada
     * @param fishedCard Carta pescada
     */
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

    /**
     * Agrega un mensaje a la lista del turno actual
     * @param message Mensaje a agregar
     */
    private void addMessageToCurrentTurn(String message) {
        if (message != null && !message.trim().isEmpty()) {
            currentTurnMessages.add(message);
        }
    }

    /**
     * Actualiza la visualización con todos los mensajes acumulados
     */
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

    /**
     * Limpia los mensajes del turno actual
     */
    private void clearCurrentTurnMessages() {
        currentTurnMessages.clear();
    }

    /**
     * Extrae el nombre del jugador de un mensaje de turno
     * @param turnMessage Mensaje de turno
     * @return Nombre del jugador o null si no se puede extraer
     */
    private String extractPlayerFromTurnMessage(String turnMessage) {
        if (turnMessage.contains("¡Es tu turno!")) {
            return "CLIENT"; // Identificador especial para el cliente
        } else if (turnMessage.startsWith("Turno de ")) {
            return turnMessage.substring(9); // Extraer nombre después de "Turno de "
        }
        return null;
    }

    /**
     * Determina si se deben limpiar los mensajes basado en el cambio de turno
     * @param newTurnPlayer Nuevo jugador del turno
     * @return true si se debe limpiar, false en caso contrario
     */
    private boolean shouldClearMessages(String newTurnPlayer) {
        return currentTurnPlayer != null &&
                newTurnPlayer != null &&
                !currentTurnPlayer.equals(newTurnPlayer);
    }

    /**
     * Formatea el mensaje de turno con estilo especial
     * @param message Mensaje original
     * @param isClientTurn Si es el turno del cliente
     * @return Mensaje formateado
     */
    private String formatTurnMessage(String message, boolean isClientTurn) {
        String prefix = isClientTurn ? ">>> " : "--- ";
        return prefix + message + " " + prefix;
    }

    /**
     * Hace scroll automático hacia el final del área de mensajes
     */
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        });
    }

    /**
     * Crea una etiqueta para mostrar un set completado
     * @param value Valor del set
     * @return JLabel configurado
     */
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

    /**
     * Método para limpiar manualmente los mensajes (útil para testing)
     */
    public void clearMessages() {
        SwingUtilities.invokeLater(() -> {
            clearCurrentTurnMessages();
            updateMessageDisplay();
        });
    }
}