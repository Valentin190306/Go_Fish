package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.Suit;
import ar.edu.unlu.poo.model.enums.Value;

public interface ICard {

    Value getNumber();

    Suit getSuit();
}
