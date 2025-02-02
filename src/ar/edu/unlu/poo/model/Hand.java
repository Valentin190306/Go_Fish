package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.ICard;
import ar.edu.unlu.poo.interfaces.IHand;
import ar.edu.unlu.poo.model.enums.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hand implements Serializable, IHand {
    private final List<Card> cards;
    private List<Card> transferenceCards;
    private int score;

    public Hand() {
        this.cards = new ArrayList<>();
        this.score = 0;
    }

    public void addCard(Card card) {
        transferenceCards = new ArrayList<>();
        transferenceCards.add(card);
        cards.add(card);
        cards.sort(Comparator.comparing(Card::getRank));
    }

    public void addCards(List<Card> cards) {
        transferenceCards = cards;
        transferenceCards.sort(Comparator.comparing(Card::getRank));
        this.cards.addAll(cards);
    }

    public List<ICard> getCards() {
        return Collections.singletonList((ICard) cards);
    }

    public List<Card> removeCardsByValue(Value value) {
        List<Card> cardsToRemove = new ArrayList<>();
        for (Card card : cards) {
            if (card.getRank().equals(value))
                cardsToRemove.add(card);
        }
        cards.removeAll(cardsToRemove);
        transferenceCards = cardsToRemove;
        return cardsToRemove;
    }

    @Override
    public boolean hasCardOfValue(Value value) {
        return cards.stream().anyMatch(card -> card.getRank() == value);
    }

    public boolean checkForSets() {
        boolean areSets = false;

        int[] rankCounts = new int[Value.values().length];

        for (Card card : cards)
            rankCounts[card.getRank().ordinal()]++;

        for (int rankCount : rankCounts) {
            if (rankCount == 4) {
                areSets = true;
                break;
            }
        }
        return areSets;
    }

    @Override
    public List<ICard> getTransferenceCards() {
        return new ArrayList<>(transferenceCards);
    }

    @Override
    public int getScore() {
        int[] rankCounts = new int[Value.values().length];
        this.score = 0;

        for (Card card : cards)
            rankCounts[card.getRank().ordinal()]++;

        for (int rankCount : rankCounts) {
            if (rankCount == 4) {
                score++;
            }
        }
        return score;
    }

    @Override
    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}

