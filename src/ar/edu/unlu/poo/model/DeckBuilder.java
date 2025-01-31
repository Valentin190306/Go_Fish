package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.model.enums.Suit;
import ar.edu.unlu.poo.model.enums.Value;

public class DeckBuilder {
    private Deck deck;

    public DeckBuilder() {
        this.deck = new Deck();
        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                deck.addCard(new Card(value, suit));
            }
        }
    }

    public Deck getDeck() {
        return deck;
    }
}
