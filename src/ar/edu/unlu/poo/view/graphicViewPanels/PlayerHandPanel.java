package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.enums.Suit;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
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

        // Scroll pane para las cartas - COMPLETAMENTE TRANSPARENTE
        cardsScrollPane = new JScrollPane(cardsPanel);
        cardsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cardsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        cardsScrollPane.setPreferredSize(new Dimension(800, 140));

        // Hacer el scroll pane completamente transparente
        cardsScrollPane.setOpaque(false);
        cardsScrollPane.getViewport().setOpaque(false);
        cardsScrollPane.setBorder(null); // Eliminar borde del scroll pane

        // Hacer las barras de scroll transparentes
        cardsScrollPane.getHorizontalScrollBar().setOpaque(false);
        cardsScrollPane.getVerticalScrollBar().setOpaque(false);

        // Botón para pedir carta - MANTENER OPACO (visible)
        requestCardButton = new JButton("Pedir Carta");
        requestCardButton.setPreferredSize(new Dimension(120, REQUEST_BUTTON_HEIGHT));
        requestCardButton.setEnabled(false); // Deshabilitado hasta tener selección
        requestCardButton.setFont(new Font("Arial", Font.BOLD, 12));

        // Configurar panel principal - TRANSPARENTE CON BORDE SEMI-TRANSPARENTE
        setPreferredSize(new Dimension(800, 180));
        setBorder(createTransparentTitledBorder());
        setOpaque(false); // Panel principal transparente
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

        // Panel superior con las cartas
        add(cardsScrollPane, BorderLayout.CENTER);

        // Panel inferior con el botón - TRANSPARENTE
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
            // Obtener oponente seleccionado de OpponentsPanel a través de parentView
            IPlayer selectedOpponent = getSelectedOpponentFromParent();

            if (selectedOpponent != null) {
                // Comunicar jugada a la vista principal
                parentView.playTurn(selectedCardValue, selectedOpponent.getName());

                // Limpiar selección después de la jugada
                clearSelection();
            } else {
                // Mostrar mensaje de error
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
        JToggleButton cardButton = new JToggleButton();
        cardButton.setPreferredSize(new Dimension(CARD_BUTTON_WIDTH, CARD_BUTTON_HEIGHT));
        cardButton.setText(value.getValue() + " (" + count + ")");
        cardButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        cardButton.setHorizontalTextPosition(SwingConstants.CENTER);
        cardButton.setFocusPainted(false);

        String imagePath = String.format("view/assets/cards/%s-%s.png",
                value.getValue().toLowerCase(),
                suit.getValue().toLowerCase());

        cardButton.setIcon(new ImageIcon(imagePath));

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

        // Comunicar selección a la vista principal
        parentView.onCardSelected(selectedCardValue);

        // Actualizar estado del botón de pedir carta
        updateRequestButtonState();

        // Actualizar apariencia visual
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

        // Resetear borders
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

        // Solo habilitar botón de pedir si hay selección y está habilitado
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