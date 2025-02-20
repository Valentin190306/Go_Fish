package ar.edu.unlu.poo.interfaces;

import java.rmi.RemoteException;
import java.util.List;

public interface IController {

    boolean isClientPLayerPLying();

    void setClientPlayerName(String playerName);

    void playerIsReady();

    List<IPlayer> getPlayerList() throws RemoteException;

    boolean handlePlayerInput(String input);

    void setView(IGameView view);
}

