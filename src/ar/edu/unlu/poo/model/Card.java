package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.model.enums.Suit;
import ar.edu.unlu.poo.model.enums.Value;

import java.io.Serializable;

public class Card implements Serializable {
    private final Value value;
    private final Suit suit;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public Value getNumber() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card card = (Card) obj;
        return card.getNumber() == value && card.getSuit() == suit;
    }

    @Override
    public String toString() {
        return value.getValue() + " de " + suit.getValue();
    }
}

