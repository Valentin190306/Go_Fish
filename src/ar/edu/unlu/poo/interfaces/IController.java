package ar.edu.unlu.poo.interfaces;

import java.rmi.RemoteException;
import java.util.List;

public interface IController {

    IPlayer getClientPlayer();

    void setLobby(ILobby lobby);

    List<IPlayer> getPlayerList() throws RemoteException;

    boolean handlePlayerInput(String input);

    void setView(IGameView view);
}

