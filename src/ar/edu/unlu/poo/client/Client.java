package ar.edu.unlu.poo.client;

import ar.edu.unlu.poo.controller.Controller;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import java.rmi.RemoteException;

public class Client {
    Controller controller = new Controller();
    IVista vista = new VistaGrafica(controlador);
    Cliente cliente = new Cliente(ip, port, ipServidor, portServidor);
    vista.iniciar(); // muestra la vista gráfica
    try {
        cliente.iniciar(controller); // enlaza el controlador con el modelo remoto
    } catch (RemoteException e) {
        // error de conexión
        e.printStackTrace();
    } catch (RMIMVCException e) {
        // error al crear el objeto de acceso remoto del modelo o del controlador
        e.printStackTrace();
    }
}
