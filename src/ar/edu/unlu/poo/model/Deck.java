package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.IDeck;
import ar.edu.unlu.poo.model.enums.Suit;
import ar.edu.unlu.poo.model.enums.Value;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck implements IDeck, Serializable {
    private final Stack<Card> cards;

    private Deck(Builder builder) {
        this.cards = builder.cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void addCardsBack(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public Card drawCard() {
        return cards.isEmpty() ? null : cards.pop();
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public int size() {
        return cards.size();
    }

    public static class Builder {
        private Stack<Card> cards = new Stack<>();

        public void addAllCards() {
            for (Suit suit : Suit.values()) {
                for (Value value : Value.values()) {
                    cards.push(new Card(value, suit));
                }
            }
        }

        public Deck build() {
            Deck deck = new Deck(this);
            addAllCards();
            deck.shuffle();
            return deck;
        }
    }
}