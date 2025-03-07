package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public class SemiController implements IControladorRemoto {
    private IGo_Fish model;

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.model = (IGo_Fish) t;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {

    }
}
