package ar.edu.unlu.poo.interfaces;

import ar.edu.unlu.poo.model.enums.Value;
import ar.edu.unlu.poo.model.enums.Suit;

public interface ICard {
    /**
     * Obtener el número de la carta
     * @return Value
     */
    Value getNumber();

    /**
     * Obtener el palo de la carta
     * @return Suit
     */
    Suit getSuit();
}
