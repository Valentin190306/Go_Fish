package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.ICard;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class SemiController implements IControladorRemoto, IObservadorRemoto {
    private IGo_Fish model;
    private final ArrayList<IObservadorRemoto> clientSideObservers;

    public SemiController(ArrayList<IObservadorRemoto> clientSideObservers) throws RemoteException {
        this.clientSideObservers = clientSideObservers;
    }

    public IPlayer connect() throws RemoteException {
        return model.connectPlayer();
    }

    public void disconnect(IObservadorRemoto clientViewObserver, IPlayer clientPlayer) throws RemoteException {
        model.disconnectPlayer(clientViewObserver, (Player) clientPlayer);
    }

    public void registerLocalObserver(IObservadorRemoto observer) {
        clientSideObservers.add(observer);
    }

    public void unregisterLocalObserver(IObservadorRemoto observer) {
        clientSideObservers.remove(observer);
    }

    public ArrayList<IPlayer> fetchPlayers() throws RemoteException {
        return model.getPlayers();
    }

    public IPlayer fetchPlayer(IPlayer player) throws RemoteException {
        return model.getPlayer((Player) player);
    }

    public ICard fetchQueriedCard() throws RemoteException {
        return model.getQueriedCard();
    }

    public IPlayer fetchPlayingPlayer() throws RemoteException {
        return model.getCurrentPlayerPlayingTurn();
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.model = (IGo_Fish) t;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
    }
}
