package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.RemoteException;

public class ConsoleGameViewEngine implements IObservadorRemoto {
    private final GameWindow gameWindow;
    private final JTextArea consoleArea;
    private final JTextField inputField;
    private final IController controller;
    private String placeholder;
    private boolean isGameOver = false;

    public ConsoleGameView(GameWindow gameWindow, IController controller) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        this.consoleArea = new JTextArea();
        this.inputField = new JTextField();
        this.placeholder = "<VALOR_CARTA> <NOMBRE_JUGADOR>";
        initComponents();
    }

    private void initComponents() {
        setSize(600, 400);
        setLayout(new BorderLayout());

        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consoleArea.setBackground(Color.BLACK);
        consoleArea.setForeground(Color.GREEN);
        add(new JScrollPane(consoleArea), BorderLayout.CENTER);

        inputField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.GREEN);
        inputField.addActionListener(e -> processInput());
        add(inputField, BorderLayout.SOUTH);

        inputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(inputField.getText().equals(placeholder)) {
                    inputField.setText("");
                    inputField.setForeground(Color.GREEN);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(inputField.getText().isEmpty()) {
                    inputField.setForeground(Color.GRAY);
                    inputField.setText(placeholder);
                }
            }
        });
    }

    private void processInput() {
        String input = inputField.getText().trim();
        try {
            if (!isGameOver) {
                if (!input.isEmpty() && controller.handlePlayerInput(input)) {
                    inputField.setText("");
                }
            } else if (!input.isEmpty() && controller.handlePlayerExit(input)) {
                inputField.setText("");
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public void appendToConsole(String text) {
        consoleArea.append(text + "\n");
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    @Override
    public void start() {
        gameWindow.showGame();
        setVisible(true);
    }

    @Override
    public void handleException(Exception e) {
        if (e instanceof RMIMVCException) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "RMI Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            appendToConsole("!> " + e.getMessage());
        }
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {

    }
}
