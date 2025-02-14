package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.Value;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IGame extends IObservableRemoto {

    void start() throws RemoteException;

    /**
     * El juego realiza la jugada del jugador en turno
     * @param valueRequested número de la carta preguntada
     * @param targetPlayer jugador al que se le preguntó
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    void playTurn(Value valueRequested, Player targetPlayer) throws RemoteException;

    /**
     * Obtiene el jugador jugando el turno en un momento dado
     * @return jugador usando el turno
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    IPlayer getCurrentPlayerPlayingTurn() throws RemoteException;

    /**
     * Obtiene el jugador al que se le preguntó
     * @return jugador objetivo
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    IPlayer getTargetPlayer() throws RemoteException;

    /**
     *
     * @param name
     * @return
     */
    IPlayer getPlayerCalled(String name) throws RemoteException;

    /**
     * Obtiene la lista de jugadores en la partida
     * @return lista de jugadores
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    List<IPlayer> getPlayers() throws RemoteException;

    /**
     * Obtiene el estado del juego
     * @return valor enumerado
     */
    GameState getGameState() throws RemoteException;

    /**
     *
     * @param name
     * @return
     * @throws RemoteException
     */
    int addPlayer(String name) throws RemoteException;

    int addPlayer(IPlayer player) throws RemoteException;

    /**
     *
     * @param ID
     * @throws RemoteException
     */
    void removePlayer(int ID) throws RemoteException;

    IPlayer getPlayerByID(int ID) throws RemoteException;

    /**
     * Obtiene la pila de cartas de la partida
     * @return pila de cartas
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    IDeck getDeck() throws RemoteException;
}
