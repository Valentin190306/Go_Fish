package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {
    private JButton continueGameButton, newGameButton, changeNameButton, changeViewButton;
    private final Controller controller;

    public GameWindow(Controller controller) {
        this.controller = controller;

        setTitle("Menú del Juego");
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
        JMenuItem menuChangeView = new JMenuItem("Seleccionar vista");

        gameMenu.add(menuContinue);
        gameMenu.add(menuNew);
        confMenu.add(menuChangeName);
        confMenu.add(menuChangeView);

        menuBar.add(gameMenu);
        menuBar.add(confMenu);

        // Agregar eventos
        menuContinue.addActionListener(this);
        menuNew.addActionListener(this);
        menuChangeName.addActionListener(this);
        menuChangeView.addActionListener(this);

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