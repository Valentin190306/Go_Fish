package ar.edu.unlu.poo.client;

import ar.edu.unlu.poo.controller.GameController;
import ar.edu.unlu.poo.interfaces.IGameController;
import ar.edu.unlu.poo.interfaces.IGameWindow;
import ar.edu.unlu.poo.view.GameWindow;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.rmi.RemoteException;

public class Client {
    private static final String clientHost = "127.0.0.1";
    private static final String serverHost = "127.0.0.1";
    private static final int clientPort = 0;
    private static final int serverPort = 1234;

    public static void main (String[] args) {
        IGameController controller = GameController.getInstance();
        try {
            Cliente client = new Cliente(clientHost, clientPort, serverHost, serverPort);
            client.iniciar(controller);
            IGameWindow gameWindow = new GameWindow(controller);
            controller.setGameWindow(gameWindow);
            gameWindow.start();
        } catch (RemoteException | RMIMVCException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE));
            System.exit(1);
        }
    }
}