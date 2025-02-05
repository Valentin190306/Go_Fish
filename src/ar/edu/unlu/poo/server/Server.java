package ar.edu.unlu.poo.server;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;

public class Server {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        Chat modelo = new Chat();
        Servidor servidor = new Servidor(IP, PORT);
        try {
            servidor.iniciar(modelo);
        }catch (RemoteException e) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                null,
                "Error de conexión: \n" + e.getCause(),
                "Fallo de RED",
                JOptionPane.ERROR_MESSAGE));
        } catch (RMIMVCException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                null,
                "Error al crear el objeto de acceso remoto del modelo o del controlador: \n" + e.getCause(),
                "Fallo de RMI",
                JOptionPane.ERROR_MESSAGE));
        }
    }
}
