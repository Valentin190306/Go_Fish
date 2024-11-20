package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.Rank;
import ar.edu.unlu.poo.model.Suit;

public interface ICard {
    Rank getRank();
    Suit getSuit();
}
