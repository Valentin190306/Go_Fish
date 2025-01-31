package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.model.enums.Suit;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

public class Deck implements IDeck, Serializable {
    private final Stack<Card> cards;

    public Deck() {
        cards = new Stack<>();
        initializeDeck();
        shuffle();
    }

    // Inicializa el mazo con las 52 combinaciones de Rank y Suit
    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                cards.push(new Card(value, suit));
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
