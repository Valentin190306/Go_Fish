package ar.edu.unlu.poo.model;

// Enum para los palos de las cartas
public enum Suit {
    HEARTS("corazones"),
    DIAMONDS("diamantes"),
    CLUBS("treboles"),
    SPADES("picas");

    private final String value;

    Suit(String value) { this.value = value; }

    public String getValue() { return value; }
}
