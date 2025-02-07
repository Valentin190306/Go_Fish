package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.ICard;
import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.model.enums.Suit;

import java.io.Serializable;

public class Card implements ICard, Serializable {
    private final Value value;
    private final Suit suit;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    @Override
    public Value getNumber() {
        return value;
    }

    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return value.getValue() + " de " + suit.getValue();
    }
}
