package ar.edu.unlu.poo.view.viewPanels;

import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.poo.interfaces.IGameView;
import ar.edu.unlu.poo.view.ConsoleGameView;
import ar.edu.unlu.poo.view.GameWindow;
import ar.edu.unlu.poo.view.GraphicGameView;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MenuPanel extends JPanel {
    private final GameWindow gameWindow;
    private final IController controller;
    private final ImageIcon background;

    public MenuPanel(GameWindow gameWindow, IController controller) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        this.background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ar/edu/unlu/poo/view/assets/backgrounds/background.png")));
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Go Fish", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        JButton btnPlayGame = createStyledButton("Jugar");
        JButton btnScores = createStyledButton("Puntajes históricos");
        JButton btnChangeName = createStyledButton("Cambiar Nombre");
        JButton btnChangeView = createStyledButton("Cambiar Vista");
        JButton btnRules = createStyledButton("Reglas");

        btnPlayGame.addActionListener(e -> {
            gameWindow.configureControllerAndView();
            gameWindow.showCard("Lobby");
        });

        btnScores.addActionListener(e -> gameWindow.showCard("Scores"));

        btnChangeName.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(null,
                    "Ingrese su nombre",
                    "Nombre del jugador",
                    JOptionPane.QUESTION_MESSAGE);

            if (newName != null && !newName.trim().isEmpty()) {
                gameWindow.setPlayerName(newName);
            }
        });

        btnChangeView.addActionListener(e -> {
            gameWindow.setGameView(popSelectGameView());
        });

        btnRules.addActionListener(e -> gameWindow.showCard("Rules"));

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(btnPlayGame, gbc);

        gbc.gridy = 2;
        panel.add(btnScores, gbc);

        gbc.gridy = 3;
        panel.add(btnChangeName, gbc);

        gbc.gridy = 4;
        panel.add(btnChangeView, gbc);

        gbc.gridy = 5;
        panel.add(btnRules, gbc);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private IGameView popSelectGameView() {
        String[] options = {"Consola", "Gráfica"};
        JComboBox<String> comboBox = new JComboBox<>(options);

        int result = JOptionPane.showConfirmDialog(null,
                comboBox,
                "Selecciona un estilo de vista",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        try {
            if (result == JOptionPane.OK_OPTION) {
                String selectedItem = (String) comboBox.getSelectedItem();

                if ("Consola".equals(selectedItem)) {
                    if (gameWindow.getGameView() instanceof ConsoleGameView) {
                        return new ConsoleGameView(gameWindow, controller);
                    }
                }
                else if ("Gráfica".equals(selectedItem)) {
                    if (!(gameWindow.getGameView() instanceof GraphicGameView)) {
                        return new GraphicGameView(gameWindow, controller);
                    }
                }
            }
        } catch (NullPointerException e) {
            gameWindow.messagePopUp(e);
        }
        return gameWindow.getGameView();
    }
}
