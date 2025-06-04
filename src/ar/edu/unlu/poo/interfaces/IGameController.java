package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IGameController extends IControladorRemoto, IObservadorRemoto {

    void setGameWindow(IGameWindow gameWindow) throws RemoteException;

    void setGameView(IGameView gameView) throws RemoteException;

    void connect() throws RemoteException;

    void disconnect() throws RemoteException;

    IPlayer fetchClientPlayer() throws RemoteException;

    ArrayList<IPlayer> fetchPlayers() throws RemoteException;

    Value fetchQueriedValue() throws RemoteException;

    IPlayer fetchPlayingPlayer() throws RemoteException;

    IPlayer fetchTargetPlayer() throws RemoteException;

    IDeck fetchDeck() throws RemoteException;

    void setClientPlayerReady() throws RemoteException;

    void updateClientPlayerName(String name) throws RemoteException;

    HashMap<String, Integer> fetchGameScoreList() throws RemoteException;

    HashMap<String, Integer> fetchHighScoreList() throws RemoteException;

    <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException;

    void clientPlaysTurn(Value valueRequested, String targetPlayer) throws RemoteException;
}