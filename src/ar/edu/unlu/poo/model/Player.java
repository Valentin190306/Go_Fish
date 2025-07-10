package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.PlayerState;
import ar.edu.unlu.poo.model.enums.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player implements Serializable, IPlayer {
    private static int IDCounter = 0;
    private final int ID;
    private String name;

    private final Hand hand;
    private final List<List<Card>> completedSets;
    private Card fishedCard = null;

    private PlayerState playerState = PlayerState.WAITING;

    public Player() {
        this.name = "Player" + IDCounter;
        this.ID = IDCounter;
        IDCounter++;
        this.hand = new Hand();
        this.completedSets = new ArrayList<>();
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    @Override
    public PlayerState getPlayerState() {
        return playerState;
    }

    /**
     * Recibe una carta
     */
    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Recibe múltiples cartas y procesa automáticamente los sets completados
     */
    public void receiveCards(List<Card> cards) {
        hand.addCards(cards);
        processCompletedSets();
    }

    public void fishing(Card card) {
        fishedCard = card;
        hand.addCard(card);
        processCompletedSets();
    }

    @Override
    public Card getFishedCard() {
        return fishedCard;
    }

    /**
     * Remueve cartas de un valor específico de la mano (para dar cartas a otro jugador)
     */
    public List<Card> giveCardsByValue(Value value) {
        return hand.removeCardsByValue(value);
    }

    /**
     * Verifica si el jugador tiene cartas de un valor específico
     */
    @Override
    public boolean hasCardOfValue(Value value) {
        return hand.hasCardOfValue(value);
    }

    /**
     * Obtiene las cartas disponibles para jugar (después de apartar sets)
     */
    @Override
    public List<Card> getAvailableCards() {
        return hand.getCards();
    }

    /**
     * Obtiene las cartas disponibles para jugar (después de apartar sets)
     */
    public List<Card> getAllCards() {
        List<Card> cards = hand.getCards();

        for (List<Card> set : completedSets) {
            cards.addAll(set);
        }

        return cards;
    }

    /**
     * Cuenta cartas por valor de las cartas disponibles
     */
    @Override
    public Map<Value, Integer> countAvailableCardsByValue() {
        return hand.countCardsByValue();
    }

    /**
     * Obtiene el número de cartas en la mano
     */
    @Override
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Obtiene las cartas de la última transacción
     */
    @Override
    public List<Card> getLastTransferenceCards() {
        return hand.getTransferenceCards();
    }

    /**
     * Limpia la mano del jugador
     */
    public void clearHand() {
        hand.clear();
        completedSets.clear();
    }

    /**
     * Procesa automáticamente los sets completados y los mueve fuera de la mano
     */
    private void processCompletedSets() {
        Map<Value, List<Card>> setsByValue = hand.getSetsByValue();

        for (Map.Entry<Value, List<Card>> entry : setsByValue.entrySet()) {
            List<Card> setCards = entry.getValue();
            List<Card> completedSet = setCards.subList(0, 4);
            completedSets.add(new ArrayList<>(completedSet));

            hand.removeCards(completedSet);
        }
    }

    /**
     * Obtiene todos los sets completados por el jugador
     */
    @Override
    public List<List<Card>> getCompletedSets() {
        return new ArrayList<>(completedSets);
    }

    /**
     * Obtiene el puntaje del jugador (igual al número de sets completados)
     */
    @Override
    public int getScore() {
        return completedSets.size();
    }

    /**
     * Verifica si el jugador tiene sets completados
     */
    @Override
    public boolean hasCompletedSets() {
        return !completedSets.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player player = (Player) obj;
        return ID == player.ID;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", availableCards=" + getAvailableCards() +
                ", completedSets=" + completedSets.size() +
                '}';
    }
}