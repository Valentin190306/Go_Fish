package ar.edu.unlu.poo.client;

import ar.edu.unlu.poo.controller.Controller;
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

    public static void main (String[] args) throws RemoteException {
        Controller controller = new Controller();
        GameWindow gameWindow  = new GameWindow(controller);
        try {
            Cliente client = new Cliente(clientHost, clientPort, serverHost, serverPort);
            client.iniciar(controller);
            gameWindow.start();
        } catch (RemoteException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Fallo de RED",
                    JOptionPane.ERROR_MESSAGE));
            System.exit(1);
        } catch (RMIMVCException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Fallo de RMI",
                    JOptionPane.ERROR_MESSAGE));
            System.exit(1);
        }
    }
}