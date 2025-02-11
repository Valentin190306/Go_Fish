package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.controller.Controller;
import ar.edu.unlu.poo.interfaces.IGameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class GameWindow extends JFrame implements ActionListener {
    private JFrame contentFrame;
    private final Controller controller;
    private String playerName = "guest";
    private IGameView gameView;

    public GameWindow(Controller controller) {
        this.controller = controller;

        setTitle("Go Fish app");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Agregar la barra de menú
        setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Jugar");
        JMenu confMenu = new JMenu("Vista");

        JMenuItem menuContinue = new JMenuItem("Continuar partida");
        JMenuItem menuNew = new JMenuItem("Nueva partida");
        JMenuItem menuChangeName = new JMenuItem("Cambiar nombre");
        JMenu menuChangeView = new JMenu("Seleccionar vista");

        JMenuItem menuConsoleView = new JMenuItem("Consola");
        JMenuItem menuGraphicView = new JMenuItem("Gráfico");

        gameMenu.add(menuContinue);
        gameMenu.add(menuNew);
        confMenu.add(menuChangeName);
        confMenu.add(menuChangeView);

        menuChangeView.add(menuConsoleView);
        menuChangeView.add(menuGraphicView);

        menuBar.add(gameMenu);
        menuBar.add(confMenu);

        // Agregar eventos
        menuContinue.addActionListener(this);

        menuNew.addActionListener(e -> {
            contentFrame.setContentPane((Container) gameView);
        });

        menuChangeName.addActionListener(e -> {
            String input;
            do {
                input = JOptionPane.showInputDialog(null, "Ingresa tu nombre:");
                if (input == null) { // Si el usuario cancela
                    JOptionPane.showMessageDialog(null, "Operación cancelada.");
                    break;
                }
            } while (input.trim().isEmpty()); // Evita valores vacíos
            try {
                controller.setClientPlayer(playerName);
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        menuConsoleView.addActionListener(e -> gameView = new ConsoleGameView(controller));

        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (e.getSource() instanceof JButton) ? (JButton) e.getSource() : null;
        JMenuItem sourceMenu = (e.getSource() instanceof JMenuItem) ? (JMenuItem) e.getSource() : null;

        if (source != null) {
            JOptionPane.showMessageDialog(this, "Botón presionado: " + source.getText());
        } else if (sourceMenu != null) {
            JOptionPane.showMessageDialog(this, "Menú seleccionado: " + sourceMenu.getText());
        }
    }

    public void start() {
        this.setVisible(true);
    }
}