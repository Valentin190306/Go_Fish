package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.model.enums.Suit;

public interface ICard {
    Value getRank();
    Suit getSuit();
}
