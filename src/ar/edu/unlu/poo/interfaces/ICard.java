package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.Rank;
import ar.edu.unlu.poo.model.enums.Suit;

public interface ICard {
    Rank getRank();
    Suit getSuit();
}
