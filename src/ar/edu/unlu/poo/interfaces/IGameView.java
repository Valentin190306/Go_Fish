package ar.edu.unlu.poo.interfaces;

import java.util.List;

public interface IGameView {
    void notifyTurnSwitch(String playerName); // Notifica un cambio de turno
    void notifyGameOver(); // Notifica el final del juego
    void notifyPlayerAction(); // Notifica que un jugador realizó una acción
    void notifyInvalidInputFormat(); // Notifica que el formato de entrada es inválido
    void notifyInvalidPlayer(); // Notifica que el jugador solicitado no existe
    void notifyUnknownState(); // Notifica que el estado del juego no es reconocible
    void notifyGameIntroduction(IPlayer player); // Saluda al jugador de la vista
    void notifyClientPlayerGoneFishing(); // Notifica que un jugador pescó
    void notifyFishedCard(ICard card); // Notifica la carta que el jugador pescó
    void notifyReceivedCards(List<ICard> cards); // Notifica las cartas recibidas por el jugador
    void notifyLostCards(List<ICard> cards); // Notifica las cartas perdidas del jugador
    void notifyAmountOfSets(int amount); // Notifica cantidad de sets del jugador
    void notifyPlayerGoneFishing(IPlayer player); // Notifica que otro jugador pescó
    void showPlayersAndCards(int deckSize, List<ICard> players); // Muestra los jugadores y la cantidad de cartas
    void setPlayerTurn(boolean isPlayerTurn); // Indica si es el turno del jugador
    void updateHand(List<ICard> hand); // Actualiza la vista con la mano del jugador
    void updateScores(List<IPlayer> players); // Actualiza los puntajes en la vista
}
