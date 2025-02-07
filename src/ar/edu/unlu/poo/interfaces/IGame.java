package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.poo.model.Player;
import ar.edu.unlu.poo.model.enums.Value;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IGame extends IObservableRemoto {
    /**
     * Se reparten las cartas a los juagadores
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    void dealStartingCards() throws RemoteException;

    /**
     * El juego realiza la jugada del jugador en turno
     * @param valueRequested número de la carta preguntada
     * @param targetPlayer jugador al que se le preguntó
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    void playTurn(Value valueRequested, Player targetPlayer) throws RemoteException;

    /**
     * Pregunta si se terminó la partida
     * @return boolean
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    boolean isGameOver() throws RemoteException;

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
     * Pregunta si existe un jugador en la lista de jugadores
     * @param player
     * @return boolean
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    boolean existsPlayerInGame(IPlayer player) throws RemoteException;

    /**
     * Obtiene un jugador buscado por su nombre
     * @param name nombre del jugador
     * @return jugador buscado
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    IPlayer getPlayerByName(String name) throws RemoteException;

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
    GameState getGameState();

    /**
     * Obtiene la pila de cartas de la partida
     * @return pila de cartas
     * @throws RemoteException a manejarse por el controlador o la vista
     */
    IDeck getDeck() throws RemoteException;
}
