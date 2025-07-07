package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class OpponentsPanel extends JPanel {

    private final GraphicGameView parentView;

    // Botones para cada oponente (máximo 3)
    private JToggleButton[] opponentButtons;
    private ButtonGroup buttonGroup;

    // Labels para mostrar información adicional
    private JLabel[] opponentLabels;

    // Datos de oponentes
    private ArrayList<IPlayer> opponents;
    private IPlayer selectedOpponent;

    // Dimensiones y configuración
    private static final int MAX_OPPONENTS = 2;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 100;

    /**
     * Constructor del panel de oponentes
     * @param parentView Vista principal para comunicación
     */
    public OpponentsPanel(GraphicGameView parentView) {
        this.parentView = parentView;
        this.opponents = new ArrayList<>();
        this.selectedOpponent = null;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    /**
     * Inicializa los componentes del panel
     */
    private void initializeComponents() {
        opponentButtons = new JToggleButton[MAX_OPPONENTS];
        opponentLabels = new JLabel[MAX_OPPONENTS];
        buttonGroup = new ButtonGroup();

        // Crear botones y labels para cada oponente
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            // Crear botón toggle
            opponentButtons[i] = new JToggleButton();
            opponentButtons[i].setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            opponentButtons[i].setEnabled(false); // Deshabilitado hasta que haya oponentes
            opponentButtons[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            opponentButtons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            opponentButtons[i].setFocusPainted(false);

            // Agregar al grupo para comportamiento toggle exclusivo
            buttonGroup.add(opponentButtons[i]);

            // Crear label informativo
            opponentLabels[i] = new JLabel("Sin oponente");
            opponentLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            opponentLabels[i].setFont(new Font("Arial", Font.PLAIN, 10));
        }

        setPreferredSize(new Dimension(800, 140));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Oponentes",
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
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Configurar espaciado
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Agregar botones y labels en pares
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            // Panel para cada oponente (botón + label)
            JPanel opponentContainer = new JPanel(new BorderLayout());
            opponentContainer.add(opponentButtons[i], BorderLayout.CENTER);
            opponentContainer.add(opponentLabels[i], BorderLayout.SOUTH);

            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;

            add(opponentContainer, gbc);
        }
    }

    /**
     * Configura los manejadores de eventos
     */
    private void setupEventHandlers() {
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            final int opponentIndex = i;

            opponentButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleOpponentSelection(opponentIndex);
                }
            });
        }
    }

    /**
     * Maneja la selección de un oponente
     * @param opponentIndex Índice del oponente seleccionado
     */
    private void handleOpponentSelection(int opponentIndex) {
        if (opponentIndex < opponents.size()) {
            selectedOpponent = opponents.get(opponentIndex);

            // Comunicar selección a la vista principal
            parentView.onOpponentSelected(selectedOpponent);

            // Actualizar apariencia visual
            updateButtonSelection(opponentIndex);
        }
    }

    /**
     * Actualiza la apariencia visual de los botones según la selección
     * @param selectedIndex Índice del botón seleccionado
     */
    private void updateButtonSelection(int selectedIndex) {
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            if (i == selectedIndex) {
                opponentButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            } else {
                opponentButtons[i].setBorder(UIManager.getBorder("Button.border"));
            }
        }
    }

    /**
     * Actualiza la información de los oponentes
     */
    public void updateOpponents() {
        try {
            // Obtener oponentes del controlador
            opponents = parentView.getController().fetchOpponents();

            SwingUtilities.invokeLater(this::updateOpponentsDisplay);

        } catch (RemoteException e) {
            parentView.handleException(e);
        }
    }

    /**
     * Actualiza la visualización de los oponentes en el panel
     */
    private void updateOpponentsDisplay() {
        // Limpiar selección actual
        buttonGroup.clearSelection();
        selectedOpponent = null;

        // Actualizar cada botón
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            if (i < opponents.size()) {
                IPlayer opponent = opponents.get(i);

                // Habilitar botón
                opponentButtons[i].setEnabled(true);

                // Configurar texto del botón
                int cardCount = opponent.getHandSize();
                opponentButtons[i].setText(opponent.getName() + " (" + cardCount + ")");

                // Configurar label adicional
                opponentLabels[i].setText("Cartas: " + cardCount);

                opponentButtons[i].setIcon(new ImageIcon("assets/playerIcons/fisherman" + i+1 + ".png"));

            } else {
                // Deshabilitar botón vacío
                opponentButtons[i].setEnabled(false);
                opponentButtons[i].setText("Sin oponente");
                opponentButtons[i].setIcon(null);
                opponentLabels[i].setText("Sin oponente");
            }

            // Resetear border
            opponentButtons[i].setBorder(UIManager.getBorder("Button.border"));
        }
    }

    /**
     * Obtiene el oponente actualmente seleccionado
     * @return El oponente seleccionado o null si no hay selección
     */
    public IPlayer getSelectedOpponent() {
        return selectedOpponent;
    }

    /**
     * Limpia la selección actual
     */
    public void clearSelection() {
        buttonGroup.clearSelection();
        selectedOpponent = null;

        // Resetear borders
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            opponentButtons[i].setBorder(UIManager.getBorder("Button.border"));
        }
    }

    /**
     * Habilita o deshabilita la interacción con los oponentes
     * @param enabled true para habilitar, false para deshabilitar
     */
    public void setOpponentsEnabled(boolean enabled) {
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            if (i < opponents.size()) {
                opponentButtons[i].setEnabled(enabled);
            }
        }
    }

    /**
     * Actualiza el estado visual basado en el turno actual
     * @param isClientTurn true si es el turno del cliente
     */
    public void updateTurnState(boolean isClientTurn) {
        setOpponentsEnabled(isClientTurn);

        if (!isClientTurn) {
            clearSelection();
        }
    }
}
