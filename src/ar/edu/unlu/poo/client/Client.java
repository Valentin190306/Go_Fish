package ar.edu.unlu.poo.client;

import ar.edu.unlu.poo.controller.Controller;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.rmi.RemoteException;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final String CLIENT_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;
    private static final int CLIENT_PORT = 7490;

    public static void main(String[] args) {
        Controller controller = new Controller();
        IVista vista = new VistaGrafica(controlador);
        Cliente cliente = new Cliente(CLIENT_IP, CLIENT_PORT, SERVER_IP, SERVER_PORT);
        vista.iniciar(); // muestra la vista gráfica
        try {
            cliente.iniciar(controller);
        } catch (RemoteException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    "Error de conexión: \n" + e.getCause(),
                    "Fallo de RED",
                    JOptionPane.ERROR_MESSAGE));
        } catch (RMIMVCException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    "error al crear el objeto de acceso remoto del modelo o del controlador: \n" + e.getCause(),
                    "Fallo de RMI",
                    JOptionPane.ERROR_MESSAGE));
        }
    }
}