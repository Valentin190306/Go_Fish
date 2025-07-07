package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GameInfoPanel extends JPanel {

    private final GraphicGameView parentView;

    // Componentes principales
    private JTextArea textArea;
    private JScrollPane scrollPane;

    // Configuración del área de texto
    private static final int ROWS = 8;
    private static final int COLUMNS = 50;
    private static final int MAX_LINES = 100; // Límite de líneas para evitar overflow

    /**
     * Constructor del panel de información del juego
     * @param parentView Vista principal para comunicación
     */
    public GameInfoPanel(GraphicGameView parentView) {
        this.parentView = parentView;

        initializeComponents();
        setupLayout();
    }

    /**
     * Inicializa los componentes del panel
     */
    private void initializeComponents() {
        // Crear área de texto
        textArea = new JTextArea(ROWS, COLUMNS);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(255, 255, 255, 200));
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textArea.setOpaque(false);

        // Crear scroll pane
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Configurar panel
        setPreferredSize(new Dimension(800, 200));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Jugadas",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12),
                Color.WHITE
        ));
        setOpaque(false);
    }

    /**
     * Configura el layout del panel
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Método genérico para mostrar texto en el área de información
     * @param text Texto a mostrar
     */
    public void displayText(String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(text + "\n");

            // Mantener límite de líneas
            limitTextAreaLines();

            // Auto-scroll al final
            scrollToBottom();
        });
    }

    /**
     * Método genérico para mostrar texto con timestamp
     * @param text Texto a mostrar
     */
    public void displayTextWithTimestamp(String text) {
        String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
        displayText("[" + timestamp + "] " + text);
    }

    /**
     * Limpia todo el contenido del área de texto
     */
    public void clearText() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("");
        });
    }

    /**
     * Establece un texto específico (reemplaza todo el contenido)
     * @param text Texto a establecer
     */
    public void setText(String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.setText(text);
            scrollToBottom();
        });
    }

    /**
     * Limita el número de líneas en el área de texto para evitar overflow
     */
    private void limitTextAreaLines() {
        String[] lines = textArea.getText().split("\n");
        if (lines.length > MAX_LINES) {
            StringBuilder newText = new StringBuilder();
            for (int i = lines.length - MAX_LINES; i < lines.length; i++) {
                newText.append(lines[i]).append("\n");
            }
            textArea.setText(newText.toString());
        }
    }

    /**
     * Hace scroll automático hacia abajo
     */
    private void scrollToBottom() {
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Obtiene el contenido actual del área de texto
     * @return Contenido del área de texto
     */
    public String getCurrentText() {
        return textArea.getText();
    }

    /**
     * Habilita o deshabilita el área de texto
     * @param enabled true para habilitar, false para deshabilitar
     */
    public void setTextAreaEnabled(boolean enabled) {
        SwingUtilities.invokeLater(() -> {
            textArea.setEnabled(enabled);
            if (enabled) {
                textArea.setBackground(Color.WHITE);
            } else {
                textArea.setBackground(Color.LIGHT_GRAY);
            }
        });
    }
}