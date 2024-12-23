package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;

import java.util.Collections;
import java.util.Stack;

public class Deck implements IDeck {
    private final Stack<Card> cards;

    public Deck() {
        cards = new Stack<>();
        initializeDeck();
        shuffle();
    }

    // Inicializa el mazo con las 52 combinaciones de Rank y Suit
    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.push(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.pop();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public int size() {
        return cards.size();
    }
}
