package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OpponentsPanel extends JPanel {

    private final GraphicGameView parentView;

    private JToggleButton[] opponentButtons;
    private JLabel[] opponentLabels;
    private ButtonGroup buttonGroup;

    private ArrayList<IPlayer> opponents;
    private IPlayer selectedOpponent;

    private static final int MAX_OPPONENTS = 2;
    private static final int BUTTON_SIZE = 100;
    private static final int ICON_SIZE = 90;
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

        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setMinimumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setMaximumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFocusPainted(false);

        button.setMargin(new Insets(3, 3, 3, 3));

        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setIconTextGap(0);

        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

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
        setPreferredSize(new Dimension(800, 180));
        setOpaque(false);

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Oponentes",
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

        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
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
        JPanel container = new JPanel(new BorderLayout(0, 8));
        container.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(opponentButtons[index]);

        container.add(buttonPanel, BorderLayout.CENTER);
        container.add(opponentLabels[index], BorderLayout.SOUTH);

        return container;
    }

    /**
     * Configura los manejadores de eventos
     */
    private void setupEventHandlers() {
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            final int opponentIndex = i;

            opponentButtons[i].addActionListener(e -> {
                if (opponentButtons[opponentIndex].isEnabled()) {
                    selectOpponent(opponentIndex);
                }
            });

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
            updateButtonStyles();
        }
    }

    /**
     * Maneja el evento mouse entered
     */
    private void handleMouseEnter(int index) {
        JToggleButton button = opponentButtons[index];

        if (index < opponents.size()) {
            IPlayer opponent = opponents.get(index);

            opponentLabels[index].setText(
                    opponent.getName() + " (" + opponent.getHandSize() + " cartas, "
                            + opponent.getScore() + " sets)"
            );

            if (selectedOpponent != null && selectedOpponent.equals(opponent)) {
                button.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
            } else {
                button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));
            }
        } else {
            // Slot vacío
            button.setToolTipText("<html><b>Slot vacío</b><br><i>Esperando jugador...</i></html>");
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    /**
     * Maneja el evento mouse exited
     */
    private void handleMouseExit(int index) {
        JToggleButton button = opponentButtons[index];

        if (index < opponents.size()) {
            IPlayer opponent = opponents.get(index);

            opponentLabels[index].setText(opponent.getName());

            if (selectedOpponent != null && selectedOpponent.equals(opponent)) {
                button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            } else {
                button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }
        }
    }

    public void updateOpponents() {
        try {
            opponents = parentView.getController().fetchOpponents();
            refreshUI();
        } catch (RemoteException e) {
            parentView.handleException(e);
        }
    }

    private void refreshUI() {
        SwingUtilities.invokeLater(() -> {
            clearSelection();
            updateOpponentDisplays();
            updateButtonStyles();
            revalidate();
            repaint();
        });
    }

    private void updateOpponentDisplays() {
        for (int i = 0; i < MAX_OPPONENTS; i++) {
            if (i < opponents.size()) {
                updateOpponentDisplay(i, opponents.get(i));
            }
        }
    }

    private void updateOpponentDisplay(int index, IPlayer opponent) {
        JToggleButton button = opponentButtons[index];
        JLabel label = opponentLabels[index];

        button.setEnabled(true);

        ImageIcon icon = loadOpponentIcon(index + 1);
        button.setIcon(icon);

        label.setText(opponent.getName());
        label.setForeground(Color.WHITE);

        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }

    /**
     * Carga el icono de un oponente
     */
    private ImageIcon loadOpponentIcon(int playerNumber) {
        try {
            String iconPath = "src/ar/edu/unlu/poo/view/assets/playerIcons/fisherman" + playerNumber + ".png";

            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledImage = icon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error cargando icono " + playerNumber + ": " + e.getMessage());
            return null;
        }
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
                    button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                } else {
                    button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                }
            } else {
                button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
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
            JToggleButton button = opponentButtons[i];
            button.setEnabled(enabled);

            if (i < opponents.size()) {
                IPlayer opponent = opponents.get(i);
                String tooltipText = createSimpleSetTooltip(opponent, enabled);
                button.setToolTipText(tooltipText);
            }
        }
    }

    private String createSimpleSetTooltip(IPlayer opponent, boolean enabled) {
        StringBuilder tooltip = new StringBuilder();
        tooltip.append("<html><b>").append(opponent.getName()).append("</b><br>");
        tooltip.append("Cartas: ").append(opponent.getHandSize()).append("<br>");
        tooltip.append("Sets: ").append(opponent.getScore()).append("<br>");

        if (opponent.hasCompletedSets()) {
            tooltip.append("<br><b>Sets:</b> ");
            List<List<Card>> completedSets = opponent.getCompletedSets();

            for (int i = 0; i < completedSets.size(); i++) {
                if (i > 0) tooltip.append(", ");
                Set<String> uniqueValues = completedSets.get(i).stream()
                        .map(card -> card.getNumber().getValue())
                        .collect(Collectors.toSet());
                tooltip.append(uniqueValues);
            }
        }

        tooltip.append("<br>").append(enabled ? "<i>Click para seleccionar</i>" : "<i>No es tu turno</i>");
        tooltip.append("</html>");

        return tooltip.toString();
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