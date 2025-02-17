package ar.edu.unlu.poo.interfaces;

import java.rmi.RemoteException;

public interface IController {

    boolean handlePlayerInput(String input);

    void setView(IGameView view);
}

