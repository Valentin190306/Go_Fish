package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.ICard;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.model.enums.Suit;

import java.io.Serializable;
import java.util.Objects;

public class Card implements ICard, Serializable {
    private final Value value;
    private final Suit suit;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    @Override
    public Value getRank() {
        return value;
    }

    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return value + " de " + suit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return value == card.value && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, suit);
    }
}
