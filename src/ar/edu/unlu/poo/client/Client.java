package ar.edu.unlu.poo.client;

import ar.edu.unlu.poo.controller.Controller;
import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.poo.interfaces.IGameWindow;
import ar.edu.unlu.poo.view.GameWindow;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;

import javax.swing.*;
import java.rmi.RemoteException;

public class Client {
    private static final String clientHost = "127.0.0.1";
    private static final String serverHost = "127.0.0.1";
    private static final int clientPort = 0;
    private static final int serverPort = 1234;

    public static void main (String[] args) throws RemoteException {
        IController controller = new Controller();
        try {
            Cliente client = new Cliente(clientHost, clientPort, serverHost, serverPort);
            client.iniciar((IControladorRemoto) controller);
            IGameWindow gameWindow = new GameWindow(controller);
            gameWindow.start();
        } catch (RemoteException | RMIMVCException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE));
            System.exit(1);
        }
    }
}