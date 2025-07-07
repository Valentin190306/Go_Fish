package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.IGameWindow;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;

@Deprecated
public class PanelController implements IControladorRemoto, IObservadorRemoto {
    private IGameWindow gameWindow;
    private GameModelService service;

    public void setGameWindow(IGameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof GameState gameState) {
            try {
                switch (gameState) {
                    case NEW_STATUS_PLAYER -> {
                        gameWindow.getLobbyCard().updatePlayerList();
                    }
                    case READY -> {
                        gameWindow.showGameCard();
                    }
                    default -> {
                        // Se ignoran los estados no relevantes al lobby
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // O manejo centralizado
            }
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        if (t instanceof IGo_Fish model) {
            // Podés pasar null como player si no se necesita todavía
            this.service = new GameModelService(model);
        }
    }

    public GameModelService getService() {
        return service;
    }
}


