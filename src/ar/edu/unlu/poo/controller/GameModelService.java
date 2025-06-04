package ar.edu.unlu.poo.controller;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.interfaces.IGo_Fish;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import ar.edu.unlu.poo.interfaces.*;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.enums.Value;

public class GameModelService {
    private final IGo_Fish model;

    public GameModelService(IGo_Fish model) {
        this.model = model;
    }

    public IPlayer connectPlayer() throws RemoteException {
        return model.connectPlayer();
    }

    public void disconnectPlayer(IPlayer clientPlayer, IGameController controller) throws RemoteException {
        model.disconnectPlayer(controller, (Player) clientPlayer);
    }

    public IPlayer fetchClientPlayer(IPlayer clientPlayer) throws RemoteException {
        return model.getPlayer((Player) clientPlayer);
    }

    public ArrayList<IPlayer> fetchPlayers() throws RemoteException {
        return model.getPlayers();
    }

    public Value fetchQueriedValue() throws RemoteException {
        return model.getQueriedValue();
    }

    public IPlayer fetchPlayingPlayer() throws RemoteException {
        return model.getCurrentPlayerInTurn();
    }

    public IPlayer fetchTargetPlayer() throws RemoteException {
        return model.getTargetPlayer();
    }

    public IDeck fetchDeck() throws RemoteException {
        return model.getDeck();
    }

    public void setPlayerReady(IPlayer clientPlayer) throws RemoteException {
        model.setPlayerReady((Player) clientPlayer);
    }

    public void configPlayerName(IPlayer clientPlayer, String name) throws RemoteException {
        model.configPlayerName((Player) clientPlayer, name);
    }

    public void setPlayerWaiting(IPlayer clientPlayer) throws RemoteException {
        model.setPlayerWaiting((Player) clientPlayer);
    }

    public void playTurn(Value requestedValue, IPlayer targetPlayer) throws RemoteException {
        model.playTurn(requestedValue, (Player) targetPlayer);
    }

    public IPlayer getPlayerByName(String name) throws RemoteException {
        return model.getPlayerCalled(name);
    }

    public GameState getGameState() throws RemoteException {
        return model.getGameState();
    }

    public HashMap<String, Integer> fetchGameScoreList() throws RemoteException {
        return model.getGameScores();
    }

    public HashMap<String, Integer> fetchHighScoreList() throws RemoteException {
        return model.getHighScoreList();
    }
}
