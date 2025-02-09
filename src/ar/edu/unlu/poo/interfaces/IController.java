package ar.edu.unlu.poo.interfaces;

import java.rmi.RemoteException;

public interface IController {
    void setClientPlayer(String name) throws RemoteException;

    boolean handlePlayerInput(String input);
    void setView(IGameView view);
}

