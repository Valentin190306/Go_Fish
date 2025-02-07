package ar.edu.unlu.poo.interfaces;

import java.util.List;

public interface IGameView {
    /**
     * Hace visible a la vista
     */
    void start();

    /**
     * Notifica el cambio de turno
     * @param player jugando el turno siguiente
     */
    void notifyTurnSwitch(IPlayer player);

    /**
     * Notifica el final del juego
     */
    void notifyGameOver();

    /**
     * Notifica que el jugador pregunto por una carta
     * @param targetPlayer al que se le preguntó
     * @param player jugando su turno
     */
    void notifyPlayerAction(IPlayer targetPlayer, IPlayer player);

    void notifyInvalidInputFormat();
    void notifyInvalidPlayer();
    void notifyUnknownState();

    /**
     * Saluda al jugador
     * @param player para obtener el nombre del jugador
     */
    void notifyGameIntroduction(IPlayer player);

    /**
     * Notifica que el jugador local fue a pescar (recoger carta de la pila)
     */
    void notifyClientPlayerGoneFishing();

    /**
     * Muestra la carta pescada (recogida de la pila) por el jugador
     * @param card para obtener el número y palo
     */
    void notifyFishedCard(ICard card);

    /**
     * Muestra la o las cartas obtenidas en la jugada
     * @param cards cartas nuevas en la mano del jugador
     */
    void notifyReceivedCards(List<ICard> cards);

    /**
     * Muestra la o las cartas cedidas a otro jugador
     * @param cards perdidas de la mano del jugador cliente
     */
    void notifyLostCards(List<ICard> cards);

    /**
     * Notifica la cantidad de conjuntos presentes en la mano del jugador (puntaje)
     * @param amount de conjuntos
     */
    void notifyAmountOfSets(int amount);

    /**
     * Notifica que otro jugador fue a pescar
     * @param player para obtener el nombre
     */
    void notifyPlayerGoneFishing(IPlayer player);

    /**
     * Muestra a los jugadores en la partida y la cantidad de cartas en la pila
     * @param deck para obtener la cantidad de cartas
     * @param players para obtener sus nombres
     */
    void showPlayersAndCards(IDeck deck, List<IPlayer> players);

    /**
     * Habilita al jugador a juagar su turno
     * @param isPlayerTurn
     */
    void setPlayerTurn(boolean isPlayerTurn);

    /**
     * Actualiza la vista con la mano del jugador
     * @param hand del jugador
     */
    void updateHand(IHand hand);

    /**
     * Actualiza los puntajes de los jugadores
     * @param players para obtener las manos y los puntajes
     */
    void updateScores(List<IPlayer> players); // Actualiza los puntajes en la vista
}
