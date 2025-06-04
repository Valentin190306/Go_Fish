package ar.edu.unlu.poo.server;

import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.model.Go_Fish;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;

public class Server {
    private static final String serverHost = "127.0.0.1";
    private static final int serverPort = 1234;
    private static final String filePath = "/ar/edu/unlu/poo/server/scores.dat";

    public static void main(String[] args) {
        Servidor server = new Servidor(serverHost, serverPort);
        try {
            Go_Fish model = Go_Fish.getInstance();
            model.setFilePath(filePath);
            server.iniciar(model);
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
