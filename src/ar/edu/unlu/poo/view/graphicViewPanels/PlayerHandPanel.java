package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.enums.Suit;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.rmi.RemoteException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerHandPanel extends JPanel {

    private final GraphicGameView parentView;

    private JPanel cardsPanel;
    private JScrollPane cardsScrollPane;
    private JButton requestCardButton;

    private final Map<Value, JToggleButton> cardButtons;
    private ButtonGroup cardButtonGroup;

    private Value selectedCardValue;

    private static final int CARD_BUTTON_WIDTH = 80;
    private static final int CARD_BUTTON_HEIGHT = 110;
    private static final int REQUEST_BUTTON_HEIGHT = 40;

    /**
     * Constructor del panel de mano del jugador
     * @param parentView Vista principal para comunicación
     */
    public PlayerHandPanel(GraphicGameView parentView) {
        this.parentView = parentView;
        this.cardButtons = new HashMap<>();
        this.cardButtonGroup = new ButtonGroup();
        this.selectedCardValue = null;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    /**
     * Inicializa los componentes del panel
     */
    private void initializeComponents() {
        // Panel para las cartas - TRANSPARENTE
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        cardsPanel.setOpaque(false);

        cardsScrollPane = new JScrollPane(cardsPanel);
        cardsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cardsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        cardsScrollPane.setPreferredSize(new Dimension(800, 140));

        cardsScrollPane.setOpaque(false);
        cardsScrollPane.getViewport().setOpaque(false);
        cardsScrollPane.setBorder(null);

        cardsScrollPane.getHorizontalScrollBar().setOpaque(false);
        cardsScrollPane.getVerticalScrollBar().setOpaque(false);

        requestCardButton = new JButton("Pedir Carta");
        requestCardButton.setPreferredSize(new Dimension(120, REQUEST_BUTTON_HEIGHT));
        requestCardButton.setEnabled(false);
        requestCardButton.setFont(new Font("Arial", Font.BOLD, 12));

        setPreferredSize(new Dimension(800, 200));
        setBorder(createTransparentTitledBorder());
        setOpaque(false);
    }

    /**
     * Crea un borde con título semi-transparente
     * @return TitledBorder semi-transparente
     */
    private TitledBorder createTransparentTitledBorder() {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2), // Línea semi-transparente
                "Tu mano",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12),
                new Color(255, 255, 255, 200)
        );
    }

    /**
     * Configura el layout del panel
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        add(cardsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(requestCardButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configura los manejadores de eventos
     */
    private void setupEventHandlers() {
        requestCardButton.addActionListener(e -> handleRequestCard());
    }

    /**
     * Maneja la acción de pedir carta
     */
    private void handleRequestCard() {
        if (selectedCardValue != null) {
            IPlayer selectedOpponent = getSelectedOpponentFromParent();

            if (selectedOpponent != null) {
                parentView.playTurn(selectedCardValue, selectedOpponent.getName());

                clearSelection();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un oponente antes de pedir una carta.",
                        "Selección Requerida",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Obtiene el oponente seleccionado desde la vista principal
     * @return El oponente seleccionado o null
     */
    private IPlayer getSelectedOpponentFromParent() {
        return parentView.getSelectedOpponent();
    }

    /**
     * Actualiza la visualización de la mano del jugador
     */
    public void updateHand() {
        try {
            SwingUtilities.invokeLater(() -> {
                cardsPanel.removeAll();
                cardButtonGroup = new ButtonGroup();
                cardButtons.clear();
                selectedCardValue = null;

                try {
                    List<Card> availableCards = parentView
                            .getController()
                            .fetchClientPlayer()
                            .getAvailableCards();

                    Map<Value, List<Card>> cardsByValue = availableCards.stream()
                            .collect(Collectors.groupingBy(Card::getNumber));

                    for (Map.Entry<Value, List<Card>> entry : cardsByValue.entrySet()) {
                        Value value = entry.getKey();
                        List<Card> cardsOfValue = entry.getValue();
                        int count = cardsOfValue.size();

                        Card representativeCard = cardsOfValue.get(0);

                        createCardButton(value, representativeCard.getSuit(), count);
                    }

                } catch (RemoteException ex) {
                    parentView.handleException(ex);
                }

                updateRequestButtonState();

                cardsPanel.revalidate();
                cardsPanel.repaint();
            });
        } catch (Exception e) {
            parentView.handleException(e);
        }
    }


    /**
     * Crea un botón para un valor de carta específico
     * @param value Valor de la carta
     * @param count Cantidad de cartas de ese valor
     */
    private void createCardButton(Value value, Suit suit, int count) {
        JToggleButton cardButton = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (isSelected()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(Color.BLUE);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
                    g2d.dispose();
                }

                if (count > 1) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2d.setColor(Color.RED);
                    g2d.fillOval(getWidth() - 20, 2, 16, 16);

                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 10));
                    FontMetrics fm = g2d.getFontMetrics();
                    String countStr = String.valueOf(count);
                    int textWidth = fm.stringWidth(countStr);
                    int textHeight = fm.getAscent();

                    g2d.drawString(countStr,
                            getWidth() - 20 + (16 - textWidth) / 2,
                            2 + (16 + textHeight) / 2 - 2);

                    g2d.dispose();
                }
            }
        };

        cardButton.setPreferredSize(new Dimension(CARD_BUTTON_WIDTH, CARD_BUTTON_HEIGHT));
        cardButton.setFocusPainted(false);
        cardButton.setContentAreaFilled(false);
        cardButton.setBorderPainted(false);

        String fileName = value.getValue().toLowerCase() + "-" + suit.getValue().toLowerCase() + ".png";
        String imagePath ="src/ar/edu/unlu/poo/view/assets/cards/" + fileName;

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);

            Image scaledImage = icon.getImage().getScaledInstance(
                    CARD_BUTTON_WIDTH - 4, CARD_BUTTON_HEIGHT - 4,
                    Image.SCALE_SMOOTH);

            cardButton.setIcon(new ImageIcon(scaledImage));
        }

        cardButton.addChangeListener(e -> cardButton.repaint());

        cardButton.addActionListener(e -> handleCardSelection(value));
        cardButtons.put(value, cardButton);
        cardButtonGroup.add(cardButton);
        cardsPanel.add(cardButton);
    }

    /**
     * Maneja la selección de una carta
     * @param value Valor de la carta seleccionada
     */
    private void handleCardSelection(Value value) {
        selectedCardValue = value;
        updateRequestButtonState();
        updateCardButtonSelection(value);
    }

    /**
     * Actualiza la apariencia visual de los botones según la selección
     * @param selectedValue Valor de la carta seleccionada
     */
    private void updateCardButtonSelection(Value selectedValue) {
        for (Map.Entry<Value, JToggleButton> entry : cardButtons.entrySet()) {
            JToggleButton button = entry.getValue();
            if (entry.getKey() == selectedValue) {
                button.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            } else {
                button.setBorder(UIManager.getBorder("Button.border"));
            }
        }
    }

    /**
     * Actualiza el estado del botón de pedir carta
     */
    private void updateRequestButtonState() {
        boolean hasSelection = (selectedCardValue != null);
        requestCardButton.setEnabled(hasSelection);
    }

    /**
     * Limpia la selección actual
     */
    public void clearSelection() {
        cardButtonGroup.clearSelection();
        selectedCardValue = null;
        updateRequestButtonState();

        for (JToggleButton button : cardButtons.values()) {
            button.setBorder(UIManager.getBorder("Button.border"));
        }
    }

    /**
     * Obtiene el valor de carta actualmente seleccionado
     * @return El valor seleccionado o null si no hay selección
     */
    public Value getSelectedCardValue() {
        return selectedCardValue;
    }

    /**
     * Habilita o deshabilita la interacción con las cartas
     * @param enabled true para habilitar, false para deshabilitar
     */
    public void setCardsEnabled(boolean enabled) {
        for (JToggleButton button : cardButtons.values()) {
            button.setEnabled(enabled);
        }

        requestCardButton.setEnabled(enabled && selectedCardValue != null);
    }

    /**
     * Actualiza el estado visual basado en el turno actual
     * @param isClientTurn true si es el turno del cliente
     */
    public void updateTurnState(boolean isClientTurn) {
        setCardsEnabled(isClientTurn);

        if (!isClientTurn) {
            clearSelection();
        }
    }
}