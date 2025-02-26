package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.poo.interfaces.IGameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class GameWindow extends JFrame {
    private final IController controller;
    private IGameView gameView;
    private String playerName = null;
    private final JPanel viewContainer;
    private final CardLayout cardLayout;

    public GameWindow(IController controller) {
        this.controller = controller;
        this.gameView = new ConsoleGameView(this, controller);

        setTitle("Go Fish");
        setSize(800, 600);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Seguro que quieres salir?", "Confirmar salida",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    System.out.println("Guardando datos...");
                    try {
                        if (controller.getClientPlayer() != null) {
                            controller.disconnect();
                        }
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(null,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    dispose();
                    System.exit(0);
                }
            }
        });

        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        viewContainer = new JPanel(cardLayout);

        viewContainer.add(createMenuPanel(), "Menu");
        viewContainer.add(createLobbyPanel(), "Lobby");
        viewContainer.add(new RulesPanel(this), "Rules");

        add(viewContainer, BorderLayout.CENTER);
    }

    private LobbyPanel createLobbyPanel() {
        LobbyPanel lobbyPanel = new LobbyPanel(controller);
        controller.setLobby(lobbyPanel);
        return lobbyPanel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Go Fish", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
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
            configureControllerAndView();
            showCard("Lobby");
        });

        btnScores.addActionListener(e -> {});

        btnChangeName.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this,
                    "Ingrese su nombre",
                    "Nombre del jugador",
                    JOptionPane.QUESTION_MESSAGE);
            if (newName != null && !newName.trim().isEmpty()) {
                this.playerName = newName;
            }
        });

        btnChangeView.addActionListener(e -> {
            this.gameView = popSelectGameView();
            viewContainer.add((Component) gameView, "View");
        });

        btnRules.addActionListener(e -> showCard("Rules"));

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

        return panel;
    }

    private void configureControllerAndView() {
        try {
            controller.connect();
            controller.setView(this.gameView);

            if (playerName != null) {
                controller.changeClientPlayerName(playerName);
            }
            viewContainer.add((Component) gameView, "View");
        } catch (RemoteException re) {
            JOptionPane.showMessageDialog(this,
                    re.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
                    return new ConsoleGameView(this, controller);
                } else if ("Gráfica".equals(selectedItem)) {
                    return new GraphicGameView(controller);
                }
            }
        } catch (NullPointerException ne) {
            JOptionPane.showMessageDialog(null,
                    "Selección inválida",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return this.gameView;
    }

    private void showCard(String cardName) {
        cardLayout.show(viewContainer, cardName);
    }

    public void showMenu() {
        showCard("Menu");
    }

    public void startGame() {
        if (gameView != null) {
            showCard("View");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay una vista de juego disponible",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void start() {
        setVisible(true);
    }
}