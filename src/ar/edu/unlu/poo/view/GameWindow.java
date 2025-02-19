package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {
    private final Controller controller;
    private String playerName = "Guest";
    private JPanel viewContainer;
    private CardLayout cardLayout;

    public GameWindow(Controller controller) {
        this.controller = controller;
        setTitle("Go Fish");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar contenedor de vistas con CardLayout
        cardLayout = new CardLayout();
        viewContainer = new JPanel(cardLayout);

        // Agregar las vistas
        viewContainer.add(createMenuPanel(), "Menu");
        viewContainer.add(new ConsoleGameView(controller), "ConsoleGame");
        viewContainer.add(new RulesPanel(this), "Rules");

        add(viewContainer, BorderLayout.CENTER);
        //setJMenuBar(createMenuBar());
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

        JButton btnContinue = createStyledButton("Continuar Partida");
        JButton btnNewGame = createStyledButton("Nueva Partida");
        JButton btnChangeName = createStyledButton("Cambiar Nombre");
        JButton btnChangeView = createStyledButton("Cambiar Vista");
        JButton btnRules = createStyledButton("Reglas");

        btnNewGame.addActionListener(e -> cardLayout.show(viewContainer, "ConsoleGame"));
        btnRules.addActionListener(e -> cardLayout.show(viewContainer, "Rules"));

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(btnContinue, gbc);

        gbc.gridy = 2;
        panel.add(btnNewGame, gbc);

        gbc.gridy = 3;
        panel.add(btnChangeName, gbc);

        gbc.gridy = 4;
        panel.add(btnChangeView, gbc);

        gbc.gridy = 5;
        panel.add(btnRules, gbc);

        return panel;
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
/*
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Jugar");
        JMenuItem menuNew = new JMenuItem("Nueva Partida");

        menuNew.addActionListener(e -> cardLayout.show(viewContainer, "ConsoleGame"));

        gameMenu.add(menuNew);
        menuBar.add(gameMenu);
        return menuBar;
    }
*/
    @Override
    public void actionPerformed(ActionEvent e) {
        // Lógica para manejar acciones del menú
    }

    public void showMenu() {
        cardLayout.show(viewContainer, "Menu");
    }

    public void start() {
        setVisible(true);
    }
}