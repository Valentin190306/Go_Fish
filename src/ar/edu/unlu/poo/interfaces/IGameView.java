package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Card;
import ar.edu.unlu.poo.model.Rank;
import ar.edu.unlu.poo.model.Suit;

import java.util.List;
import java.util.Map;

public interface IGameView {
    void notifyTurnSwitch(String playerName); // Notifica un cambio de turno
    void notifyGameOver(); // Notifica el final del juego
    void notifyPlayerAction(); // Notifica que un jugador realizó una acción
    void notifyInvalidInputFormat(); // Notifica que el formato de entrada es inválido
    void notifyInvalidPlayer(); // Notifica que el jugador solicitado no existe
    void notifyUnknownState(); // Notifica que el estado del juego no es reconocible
    void notifyGameIntroduction(String playerName); // Saluda al jugador de la vista
    void notifyClientPlayerGoneFishing(); // Notifica que un jugador pescó
    void notifyFishedCard(Rank rank, Suit suit); // Notifica la carta que el jugador pescó
    void notifyReceivedCards(List<Map.Entry<Rank, Suit>> receivedCards); // Notifica las cartas recibidas por el jugador
    void notifyLostCards(List<Map.Entry<Rank, Suit>> lostCards); // Notifica las cartas perdidas del jugador
    void notifyAmountOfSets(int amount); // Notifica cantidad de sets del jugador
    void notifyPlayerGoneFishing(String playerName); // Notifica que otro jugador pescó
    void showPlayersAndCards(int deckSize, List<Map.Entry<String, Integer>> scores); // Muestra los jugadores y la cantidad de cartas
    void setPlayerTurn(boolean isPlayerTurn); // Indica si es el turno del jugador
    void updateHand(List<Map.Entry<Rank, Suit>> hand); // Actualiza la vista con la mano del jugador
    void updateScores(List<Map.Entry<String, Integer>> scores); // Actualiza los puntajes en la vista
}
