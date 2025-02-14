package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.controller.Controller;
import ar.edu.unlu.poo.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {
    private final Controller controller;
    private String playerName = "guest";
    private JPanel viewContainer;
    private CardLayout cardLayout;

    public GameWindow(Controller controller) {
        this.controller = controller;
        setTitle("Go Fish app");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar contenedor de vistas con CardLayout
        cardLayout = new CardLayout();
        viewContainer = new JPanel(cardLayout);

        // Agregar las vistas
        viewContainer.add(createMenuPanel(), "Menu");
        viewContainer.add(new ConsoleGameView(controller), "ConsoleGame");

        add(viewContainer, BorderLayout.CENTER);
        setJMenuBar(createMenuBar());
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 3, 10, 10));

        JButton btnContinue = new JButton("Continuar partida");
        JButton btnNewGame = new JButton("Nueva partida");
        JButton btnChangeName = new JButton("Cambiar nombre");
        JButton btnChangeView = new JButton("Cambiar vista");

        btnNewGame.addActionListener(e -> cardLayout.show(viewContainer, "ConsoleGame"));

        panel.add(btnContinue);
        panel.add(btnNewGame);
        panel.add(btnChangeName);
        panel.add(btnChangeView);

        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Jugar");
        JMenuItem menuNew = new JMenuItem("Nueva partida");

        menuNew.addActionListener(e -> cardLayout.show(viewContainer, "ConsoleGame"));

        gameMenu.add(menuNew);
        menuBar.add(gameMenu);
        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Lógica para manejar acciones del menú
    }

    public void start() {
        setVisible(true);
    }
}