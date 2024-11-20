package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.ICard;
import ar.edu.unlu.poo.interfaces.IPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Player implements IPlayer {
    private final String name;
    private final List<Card> hand;
    private List<Card> transferenceCards;
    private int score;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    public void addCard(Card card) {
        transferenceCards = new ArrayList<>();
        transferenceCards.add(card);
        hand.add(card);
        hand.sort(Comparator.comparing(Card::getRank));
    }

    public void addCards(List<Card> cards) {
        transferenceCards = cards;
        transferenceCards.sort(Comparator.comparing(Card::getRank));
        hand.addAll(cards);
    }

    public List<Card> removeCardsByRank(Rank rank) {
        List<Card> cardsToRemove = new ArrayList<>();
        for (Card card : hand) {
            if (card.getRank().equals(rank))
                cardsToRemove.add(card);
        }
        hand.removeAll(cardsToRemove);
        transferenceCards = cardsToRemove;
        return cardsToRemove;
    }

    @Override
    public boolean hasCardOfRank(Rank rank) {
        return hand.stream().anyMatch(card -> card.getRank() == rank);
    }

    public boolean checkForSets() {
        boolean areSets = false;

        int[] rankCounts = new int[Rank.values().length];

        for (Card card : hand) rankCounts[card.getRank().ordinal()]++;

        for (int rankCount : rankCounts) {
            if (rankCount == 4) {
                score++;
                areSets = true;
            }
        }
        return areSets;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<ICard> getHand() {
        return new ArrayList<>(hand);
    }

    @Override
    public List<ICard> getTransferenceCards() {
        return new ArrayList<>(transferenceCards);
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String handToString() {
        return hand.toString();
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                ", score=" + score +
                '}';
    }
}