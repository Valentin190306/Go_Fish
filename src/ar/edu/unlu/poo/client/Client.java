package ar.edu.unlu.poo.client;

import ar.edu.unlu.poo.controller.Controller;
import ar.edu.unlu.poo.interfaces.IGameView;
import ar.edu.unlu.poo.view.ConsoleGameView;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.rmi.RemoteException;

public class Client {
    private static final String clientHost = "127.0.0.1";
    private static final String serverHost = "127.0.0.1";
    private static final int clientPort = 7490;
    private static final int serverPort = 1234;

    public static void main (String[] args) throws RemoteException {
        Controller controller = new Controller();
        IGameView gameView = new ConsoleGameView(controller);
        Cliente cliente = new Cliente(clientHost, clientPort, serverHost, serverPort);
        try {
            cliente.iniciar(controller);
            gameView.start();
        } catch (RemoteException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Fallo de RED",
                    JOptionPane.ERROR_MESSAGE));

        } catch (RMIMVCException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Fallo de RMI",
                    JOptionPane.ERROR_MESSAGE));
        }
    }
}