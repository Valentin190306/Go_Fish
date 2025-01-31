package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

public class Deck implements IDeck, Serializable {
    private final Stack<Card> cards;

    public Deck() {
        cards = new Stack<>();
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void addCard(Card card) {
        cards.push(card);
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
