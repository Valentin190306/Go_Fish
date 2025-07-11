package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.model.enums.Value;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Hand implements Serializable {
    private final List<Card> cards;
    private List<Card> transferenceCards;

    public Hand() {
        this.cards = new ArrayList<>();
        this.transferenceCards = new ArrayList<>();
    }

    public void addCard(Card card) {
        transferenceCards = new ArrayList<>();
        transferenceCards.add(card);
        cards.add(card);
        cards.sort(Comparator.comparing(Card::getNumber));
    }

    public void addCards(List<Card> cards) {
        transferenceCards = new ArrayList<>(cards);
        transferenceCards.sort(Comparator.comparing(Card::getNumber));
        this.cards.addAll(cards);
        this.cards.sort(Comparator.comparing(Card::getNumber));
    }

    public void clear() {
        cards.clear();
        transferenceCards.clear();
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public List<Card> removeCardsByValue(Value value) {
        List<Card> cardsToRemove = new ArrayList<>();
        for (Card card : cards) {
            if (card.getNumber().equals(value))
                cardsToRemove.add(card);
        }
        cards.removeAll(cardsToRemove);
        transferenceCards = new ArrayList<>(cardsToRemove);
        return cardsToRemove;
    }

    public void removeCards(List<Card> cardsToRemove) {
        cards.removeAll(cardsToRemove);
        transferenceCards = new ArrayList<>(cardsToRemove);
    }

    public boolean hasCardOfValue(Value value) {
        return cards.stream().anyMatch(card -> card.getNumber().equals(value));
    }

    public Map<Value, Integer> countCardsByValue() {
        Map<Value, Integer> counts = new HashMap<>();

        for (Card card : cards) {
            Value value = card.getNumber();
            counts.put(value, counts.getOrDefault(value, 0) + 1);
        }

        return counts;
    }

    public Map<Value, List<Card>> getSetsByValue() {
        Map<Value, List<Card>> setsByValue = new HashMap<>();
        Map<Value, Integer> valueCounts = countCardsByValue();

        for (Map.Entry<Value, Integer> entry : valueCounts.entrySet()) {
            if (entry.getValue() >= 4) {
                List<Card> cardsOfValue = cards.stream()
                        .filter(card -> card.getNumber().equals(entry.getKey()))
                        .collect(Collectors.toList());
                setsByValue.put(entry.getKey(), cardsOfValue);
            }
        }

        return setsByValue;
    }

    public List<Card> getTransferenceCards() {
        return List.copyOf(transferenceCards);
    }

    public int size() {
        return cards.size();
    }

    public String toString() {
        return cards.toString();
    }
}