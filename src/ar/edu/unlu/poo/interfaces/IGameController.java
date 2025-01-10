package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.observer.Observer;

import java.rmi.RemoteException;

public interface IGameController extends Observer {
    boolean handlePlayerInput(String input); // Procesar entrada desde la vista
    void initializeGame() throws RemoteException; // Inicializar y registrar el controlador
    void setView(IGameView view);
}

