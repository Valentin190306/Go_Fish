package ar.edu.unlu.poo.server;

import ar.edu.unlu.poo.interfaces.IMatch;
import ar.edu.unlu.poo.model.Match;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;

public class Server {
    private static final String serverHost = "127.0.0.1";
    private static final int serverPort = 1234;

    public static void main(String[] args) throws RemoteException {
        IMatch model = new Match();
        Servidor server = new Servidor(serverHost, serverPort);
        try {
            server.iniciar(model);
        }catch (RemoteException e) {
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
