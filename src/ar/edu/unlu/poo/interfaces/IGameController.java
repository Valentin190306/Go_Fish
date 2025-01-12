package ar.edu.unlu.poo.interfaces;

import java.rmi.RemoteException;

public interface IGameController {
    boolean handlePlayerInput(String input); // Procesar entrada desde la vista
    void setView(IGameView view);
}

