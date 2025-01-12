package ar.edu.unlu.poo.model.enums;

import java.io.Serializable;

// Enum para los palos de las cartas
public enum Suit implements Serializable {
    HEARTS("corazones"),
    DIAMONDS("diamantes"),
    CLUBS("treboles"),
    SPADES("picas");

    private final String value;

    Suit(String value) { this.value = value; }

    public String getValue() { return value; }
}
