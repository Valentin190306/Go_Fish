package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class OpponentsPanel extends JPanel {

    private final GraphicGameView parentView;

    // Componentes UI
    private JToggleButton[] opponentButtons;
    private JLabel[] opponentLabels;
    private ButtonGroup buttonGroup;

    // Estado del panel
    private ArrayList<IPlayer> opponents;
    private IPlayer selectedOpponent;

    // Constantes
    private static final int MAX_OPPONENTS = 2;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 100;
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 11);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 12);

    /**
     * Constructor del panel de oponentes
     */
    public OpponentsPanel(GraphicGameView parentView) {
        this.parentView = parentView;
        this.opponents = new ArrayList<>();
        this.selectedOpponent = null;

        initializeUI();
        setupLayout();
        setupEventHandlers();
    }

    /**
     * Inicializa todos los componentes de la interfaz
     */
    private void initializeUI() {
        opponentButtons = new JToggleButton[MAX_OPPONENTS];
        opponentLabels = new JLabel[MAX_OPPONENTS];
        buttonGroup = new ButtonGroup();

        for (int i = 0; i < MAX_OPPONENTS; i++) {
            createOpponentButton(i);
            createOpponentLabel(i);
        }

        configurePanelAppearance();
    }

    /**
     * Crea un botón de oponente con configuración inicial
     */
    private void createOpponentButton(int index) {
        opponentButtons[index] = new JToggleButton();
        JToggleButton button = opponentButtons[index];

        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFocusPainted(false);

        button.setEnabled(false);
        button.setToolTipText("Slot vacío");

        buttonGroup.add(button);
    }

    /**
     * Crea un label para mostrar información del oponente
     */
    private void createOpponentLabel(int index) {
        opponentLabels[index] = new JLabel("Vacío", SwingConstants.CENTER);
        JLabel label = opponentLabels[index];

        label.setFont(LABEL_FONT);
        label.setForeground(Color.WHITE);
        label.setOpaque(false);
    }

    /**
     * Configura la apariencia del panel principal
     */
    private void configurePanelAppearance() {
        setPreferredSize(new Dimension(800, 140));
        setOpaque(false);

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Oponentes - Selecciona tu objetivo",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                TITLE_FONT,
                Color.WHITE
        ));
    }

    /**
     * Configura el layout del panel
     */
    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        for (int i = 0; i < MAX_OPPONENTS; i++) {
            JPanel container = createOpponentContainer(i);

            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.weightx = 1.0;

            add(container, gbc);
        }
    }

    /**
     * Crea un contenedor para botón y label de un oponente
     */
    private JPanel createOpponentContainer(int index) {
        JPanel container = new JPanel(new BorderLayout(0, 5));
        container.setOpaque(false);

        container.add(opponentButtons[index], BorderLayout.CENTER);
        container.add(opponentLabels[index], BorderLayout.SOUTH);

        return container;
    }

    /**
     * Configura los manejadores de eventos
     */
    private void setupEventHandlers() {
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            final int opponentIndex = i;

            opponentButtons[i].addActionListener(e -> selectOpponent(opponentIndex));

            // Agregar efectos hover
            opponentButtons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    handleMouseEnter(opponentIndex);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    handleMouseExit(opponentIndex);
                }
            });
        }
    }

    /**
     * Maneja la selección de un oponente
     */
    private void selectOpponent(int index) {
        if (index >= 0 && index < opponents.size()) {
            selectedOpponent = opponents.get(index);
            parentView.onOpponentSelected(selectedOpponent);
            updateButtonStyles();
        }
    }

    /**
     * Maneja el evento mouse entered
     */
    private void handleMouseEnter(int index) {
        if (index < opponents.size() && opponentButtons[index].isEnabled()) {
            opponentButtons[index].setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));

            IPlayer opponent = opponents.get(index);
            opponentLabels[index].setText(
                    opponent.getName() + " (" + opponent.getHandSize() + " cartas, "
                            + opponent.getScore() + " sets)"
            );
        }
    }

    /**
     * Maneja el evento mouse exited
     */
    private void handleMouseExit(int index) {
        if (index < opponents.size() && opponentButtons[index].isEnabled()) {
            if (selectedOpponent == null || !selectedOpponent.equals(opponents.get(index))) {
                opponentButtons[index].setBorder(UIManager.getBorder("Button.border"));
            }

            IPlayer opponent = opponents.get(index);
            opponentLabels[index].setText(opponent.getName());
        }
    }

    /**
     * Actualiza la lista de oponentes y refresca la UI
     */
    public void updateOpponents() {
        try {
            opponents = parentView.getController().fetchOpponents();
            refreshUI();
        } catch (RemoteException e) {
            parentView.handleException(e);
            // Estado seguro en caso de error
            opponents = new ArrayList<>();
            refreshUI();
        }
    }

    /**
     * Refresca la interfaz de usuario
     */
    private void refreshUI() {
        SwingUtilities.invokeLater(() -> {
            clearSelection();
            updateOpponentDisplays();
            updateButtonStyles();
            revalidate();
            repaint();
        });
    }

    /**
     * Actualiza la visualización de cada oponente
     */
    private void updateOpponentDisplays() {
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            if (i < opponents.size()) {
                updateOpponentDisplay(i, opponents.get(i));
            } else {
                updateEmptySlot(i);
            }
        }
    }

    /**
     * Actualiza la visualización de un oponente específico
     */
    private void updateOpponentDisplay(int index, IPlayer opponent) {
        JToggleButton button = opponentButtons[index];
        JLabel label = opponentLabels[index];

        button.setEnabled(true);

        ImageIcon icon = loadOpponentIcon(index + 1);
        button.setIcon(icon);

        button.setToolTipText(createTooltip(opponent));

        label.setText(opponent.getName());
        label.setForeground(Color.WHITE);
    }

    /**
     * Actualiza un slot vacío
     */
    private void updateEmptySlot(int index) {
        JToggleButton button = opponentButtons[index];
        JLabel label = opponentLabels[index];

        button.setEnabled(false);
        button.setIcon(null);
        button.setToolTipText("Slot vacío");

        label.setText("Vacío");
        label.setForeground(Color.GRAY);
    }

    /**
     * Carga el icono de un oponente
     */
    private ImageIcon loadOpponentIcon(int playerNumber) {
        try {
            String iconPath = "src/ar/edu/unlu/poo/view/assets/playerIcons/fisherman" + playerNumber + ".png";

            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error cargando icono " + playerNumber + ": " + e.getMessage());
        }

        return null;
    }

    /**
     * Crea un tooltip con información del oponente
     */
    private String createTooltip(IPlayer opponent) {
        return String.format(
                "<html><b>%s</b><br>Cartas: %d<br>Sets: %d<br><i>Click para seleccionar</i></html>",
                opponent.getName(),
                opponent.getHandSize(),
                opponent.getScore()
        );
    }

    /**
     * Actualiza los estilos de los botones según la selección
     */
    private void updateButtonStyles() {
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            JToggleButton button = opponentButtons[i];

            if (i < opponents.size()) {
                IPlayer opponent = opponents.get(i);
                if (selectedOpponent != null && selectedOpponent.equals(opponent)) {
                    button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                } else {
                    button.setBorder(UIManager.getBorder("Button.border"));
                }
            }
        }
    }

    /**
     * Limpia la selección actual
     */
    public void clearSelection() {
        buttonGroup.clearSelection();
        selectedOpponent = null;
    }

    /**
     * Obtiene el oponente seleccionado
     */
    public IPlayer getSelectedOpponent() {
        return selectedOpponent;
    }

    /**
     * Habilita/deshabilita la interacción con los oponentes
     */
    public void setOpponentsEnabled(boolean enabled) {
        for (int i = 0; i < MAX_OPPONENTS && i < opponents.size(); i++) {
            opponentButtons[i].setEnabled(enabled);
        }
    }

    /**
     * Actualiza el estado del panel según el turno
     */
    public void updateTurnState(boolean isClientTurn) {
        setOpponentsEnabled(isClientTurn);

        if (!isClientTurn) {
            clearSelection();
            updateButtonStyles();
        }
    }
}