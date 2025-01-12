package ar.edu.unlu.poo.model;

import ar.edu.unlu.poo.interfaces.ICard;
import ar.edu.unlu.poo.model.enums.Rank;
import ar.edu.unlu.poo.model.enums.Suit;

import java.io.Serializable;
import java.util.Objects;

public class Card implements ICard, Serializable {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " de " + suit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
